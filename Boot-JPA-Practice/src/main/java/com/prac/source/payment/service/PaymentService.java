package com.prac.source.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.prac.source.cart.entity.Cart;
import com.prac.source.cart.repository.CartRepository;
import com.prac.source.common.CommonFunction;
import com.prac.source.order.entity.Order;
import com.prac.source.order.entity.OrderDtl;
import com.prac.source.order.repository.OrderDtlRepository;
import com.prac.source.order.repository.OrderRepository;
import com.prac.source.payment.entity.Payment;
import com.prac.source.payment.repository.PaymentRepository;
import com.prac.source.product.entity.Product;
import com.prac.source.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {
	
	private PaymentRepository paymentRepository;
	private OrderRepository orderRepository;
	private OrderDtlRepository orderDtlRepository;
	private CartRepository cartRepository;
	private ProductRepository productRepository;
	private CommonFunction commonFunction;
	
	@Autowired	
	public PaymentService(PaymentRepository paymentRepository
							, OrderRepository orderRepository
							, OrderDtlRepository orderDtlRepository
							, CartRepository cartRepository
							, ProductRepository productRepository
							, CommonFunction commonFunction) {
		
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.orderDtlRepository = orderDtlRepository;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.commonFunction = commonFunction;
	}
	
	/*
	 * 가정) 주문요청 버튼과 결제 버튼이 나뉘어져 있음
	 * case1. 주문 요청 후 결제 성공
	 * case2. 주문 요청 후 결제 실패
	 * case3. 주문 요청 후 결제 대기(결제 start)
	 * case3-1. 주문 요청 후 결제 대기 중 장바구니 수정 (return cart table)
	 * case4. 주문 요청 후 결제 취소( == 결제 실패)
	 * case5. 주문 요청 후 주문 취소(not requirements API)
	 * */
	
    /* 2. 결제요청 - 고객 id
	 * */
	@Transactional
    public Payment processPayment(int customerId) throws Exception {
    	commonFunction.checkUser(customerId);
    	
    	Exception paymentException = null;
    	
        // 1. PreHandler: 주문 정보 확인
        Order orderInfo = preHandler(customerId);
    	
        // 결제 테이블에 데이터 생성
        Payment paymentInfo = Payment.builder()
                .orderId(orderInfo.getOrderId())
                .customerId(orderInfo.getCustomerId())
                .paymentAmount(orderInfo.getOrderAmount())
                .paymentStatus("0")
                .build();
        
        Payment payment = paymentRepository.save(paymentInfo);
        
        if (payment == null || payment.getPaymentId() == 0) {
            throw new RuntimeException("결제 데이터 생성에 실패했습니다.");
        }

        try {
        	// 2. 결제 처리
        	handlePayment(orderInfo, payment);
            payment.setPaymentStatus("1"); // 결제 성공
		} catch(Exception e){
            payment.setPaymentStatus("2"); // 결제 실패
			paymentException = e;
		} finally {
			postHandler(payment, orderInfo);
		}

        if (paymentException != null) {
            throw paymentException;
        }

        return payment;
    }

	public Order preHandler(int customerId) throws Exception {
        // 주문 테이블 조회
        Order orderInfo = orderRepository.findByCustomerIdAndOrderStatus(customerId, "0");
        if (orderInfo == null) {
            throw new Exception("주문 중인 건이 없습니다. customer_id : " + customerId);
        }
        return orderInfo;
    }
	
    public Payment handlePayment(Order orderInfo, Payment payment) throws Exception {
        
        // 외부 결제 API 호출
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderInfo.getOrderId());
        requestBody.put("amount", orderInfo.getOrderAmount());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://backtest.free.beeceptor.com/api/v1/payment",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "SUCCESS".equals(responseBody.get("status"))) {
            } else {
                throw new Exception("결제 실패: " + responseBody.get("message"));
            }
        } catch (Exception e) {
            throw new Exception("외부 결제 API 호출 중 오류가 발생했습니다.", e);
        }
        
        return payment;
    }
	
    public void postHandler(Payment payment, Order orderInfo) throws Exception {
    	if ("1".equals(payment.getPaymentStatus())) {
        	// 2-3-1. 성공시) 결제 테이블 상태값 1로 변경, 주문테이블 1로 변경, 장바구니 테이블에서 데이터 삭제
    		orderInfo.setOrderStatus("1");
            
    		// 장바구니 테이블에서 데이터 삭제
            cartRepository.deleteByCustomerId(orderInfo.getCustomerId());
            
        } else if ("2".equals(payment.getPaymentStatus())) {
        	// 2-3-2. 실패시) 결제 테이블 상태값 2로 변경, 주문테이블 2로 변경, 상품 테이블 재고 수정
        	// 주문상태 2 : 실패 세팅
            orderInfo.setOrderStatus("2");
            // 재고관리를 위한 주문 상세 내역 조회
            List<OrderDtl> orderList = orderDtlRepository.findByOrderId(orderInfo.getOrderId());
            for(OrderDtl dtl : orderList) {
            	System.err.println(dtl.getOrderId());
                int chkProductStock = productRepository.updateProductStock(dtl.getProduct().getProductId(), dtl.getOrderDetailStock() + dtl.getProduct().getProductStock());
                if (chkProductStock == 0) {
                    throw new RuntimeException("상품 재고 수량 수정에 실패했습니다.");
                }
            }
            
        }
    	
        Order changeOrderStatus = orderRepository.save(orderInfo);
        if (changeOrderStatus == null || changeOrderStatus.getOrderId() == 0) {
        	throw new RuntimeException("주문 상태 변경에 실패했습니다.");
        }
        
        Payment updatePaymentStatus = paymentRepository.save(payment);
        if (updatePaymentStatus == null || updatePaymentStatus.getPaymentId() == 0) {
        	throw new RuntimeException("결제 상태 변경에 실패했습니다.");
        }
    }
}
