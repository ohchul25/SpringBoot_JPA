package com.prac.source.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac.source.cart.entity.Cart;
import com.prac.source.common.ResponseApi;
import com.prac.source.common.responseCode;
import com.prac.source.order.entity.Order;
import com.prac.source.payment.entity.Payment;
import com.prac.source.payment.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	private PaymentService paymentService;
	
	@Autowired	
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	// 2. 결제요청 - 고객 id
	@PostMapping(value="/{customerId}")
	public ResponseApi<Payment> payment(@PathVariable("customerId") int customerId) throws Exception {
		return new ResponseApi<>(true, paymentService.processPayment(customerId), responseCode.SUCCESS_PAYMENT.getMessage());

	}

}
