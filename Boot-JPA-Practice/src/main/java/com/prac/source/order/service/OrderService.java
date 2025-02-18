package com.prac.source.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac.source.cart.entity.Cart;
import com.prac.source.cart.repository.CartRepository;
import com.prac.source.common.CommonFunction;
import com.prac.source.order.entity.Order;
import com.prac.source.order.entity.OrderDtl;
import com.prac.source.order.repository.OrderDtlRepository;
import com.prac.source.order.repository.OrderRepository;
import com.prac.source.product.entity.Product;
import com.prac.source.product.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {
	
	private OrderRepository orderRepository;
	private OrderDtlRepository orderDtlRepository;
	private CartRepository cartRepository;
	private ProductRepository productRepository;
	private CommonFunction commonFunction;
	
	@Autowired	
	public OrderService(OrderRepository orderRepository
						, OrderDtlRepository orderDtlRepository
						, CartRepository cartRepository
						, ProductRepository productRepository
						, CommonFunction commonFunction) {
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
	 * case3. 주문 요청 후 결제 대기
	 * case3-1. 주문 요청 후 결제 대기 중 장바구니 수정 (return Order table)
	 * case4. 주문 요청 후 결제 취소
	 * case5. 주문 요청 후 주문 취소(not requirements API)
	 * */
    
	/*
	 * 1. 주문요청 - 고객id
	 * 1-1. 주문테이블 조회 (where order_status == 0) 
	 * 1-2-1). 조회값 null) 장바구니 테이블 조회, 주문테이블, 주문상세테이블 인서트, 상품테이블 재고 수량 수정
	 * 1-2-2). 조회값 not null) 요청중인 주문이 있음으로 리턴
	 *
	 */
	
    @Transactional
    public Order order(int customerId) throws Exception {
    	
    	// 고객 정보 있는지 조회하는 유효성 체크
    	commonFunction.checkUser(customerId);
    	
    	// 1-1. 주문테이블 조회 및 기존에 주문하는부분이 있는 유효성 체크
    	Order orderInfo = orderRepository.findByCustomerIdAndOrderStatus(customerId,"0");
        if (orderInfo != null) {
            throw new Exception("주문 요청중인 건이 존재합니다. customer_id : " + customerId);
        }
        
        // 1-2. 장바구니 테이블 조회, 주문테이블, 주문상세테이블 인서트, 상품테이블 재고 수량 수정
        // 1-2-1. 장바구니 테이블 조회 
        List<Cart> cart = cartRepository.findByCustomerIdJoinProduct(customerId);

        int amount = 0;
        List<OrderDtl> dtlList = new ArrayList<>();	// 주문상세에 담아줄 리스트

        try {
            for (Cart sub : cart) {
            	// 장바구니에 물건이 있는지 체크
                if (sub == null || sub.getProduct() == null) {
                    throw new IllegalArgumentException("장바구니에 잘못된 상품이 포함되어 있습니다. 상품 정보를 확인하세요.");
                }
                // 상품아이디에 대해 상품이 존해하는지 체크
                Product productNow = commonFunction.getProduct(sub.getProduct().getProductId());
                
                // 1-2-2. 주문상세 테이블 데이터 생성
                OrderDtl orderDtl = OrderDtl.builder()
                    .product(productNow)
                    .orderDetailStock(sub.getCartStock())
                    .orderDetailName(productNow.getProductName())
                    .orderDetailDescription(productNow.getProductDescription())
                    .orderDetailPrice(sub.getCartStock() * productNow.getProductPrice())
                    .build();
                dtlList.add(orderDtl);
                
                // 수량 체크 후 모자란 물량 있는지 체크
                if (sub.getProduct().getProductStock() - sub.getCartStock() < 0) {
                    throw new IllegalArgumentException("수량이 부족한 상품이 있습니다.");
                }
                
                // 1-2-3. 상품 테이블 재고관리
                int chkProductStock = productRepository.updateProductStock(productNow.getProductId(), productNow.getProductStock() - sub.getCartStock());
                if (chkProductStock == 0) {
                    throw new RuntimeException("상품 재고 수량 수정에 실패했습니다.");
                }
                
                // 1-2-4. 장바구니 테이블 조회 sum 구하기
                amount += sub.getCartStock() * sub.getProduct().getProductPrice();
            }
        } catch (Exception e) {
            throw new RuntimeException("잘못된 상품이 존재합니다. " + e.toString(), e);
        }

        
        // 1-2-5. 주문테이블 데이터 입력
        Order order = Order.builder()
        		.customerId(customerId)
        		.orderAmount(amount)
        		.orderStatus("0")
        		.build();
        Order success = orderRepository.save(order);
        
        if (success == null || success.getOrderId() == 0) {
            throw new RuntimeException("주문 요청에 실패했습니다.");
        }

        // 1-2-6. 주문 상세 테이블 데이터 입력
        for (OrderDtl orderDtl : dtlList) {
            orderDtl.setOrderId(order.getOrderId());
        }
        List<OrderDtl> chkList = orderDtlRepository.saveAll(dtlList);
        
        if (chkList.isEmpty()) {
            throw new RuntimeException("주문 요청 상세데이터 저장에 실패했습니다.");
        }
        
    	return success;
    }



}
