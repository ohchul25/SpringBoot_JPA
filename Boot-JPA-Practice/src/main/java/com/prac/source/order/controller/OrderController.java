package com.prac.source.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prac.source.cart.entity.Cart;
import com.prac.source.common.ResponseApi;
import com.prac.source.common.responseCode;
import com.prac.source.order.entity.Order;
import com.prac.source.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	private OrderService orderService;
	
	@Autowired	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	// 1. 주문요청 - 고객 id
	@PostMapping(value="/{customerId}")
	public ResponseApi<Order> order(@PathVariable("customerId") int customerId) throws Exception {
		
		return new ResponseApi<>(true, orderService.order(customerId),responseCode.SUCCESS_ORDER.getMessage());

	}
	

}
