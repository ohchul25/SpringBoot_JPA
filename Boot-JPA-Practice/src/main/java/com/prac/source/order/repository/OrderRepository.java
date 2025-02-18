package com.prac.source.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac.source.order.entity.Order;


public interface OrderRepository extends JpaRepository<Order, Integer>{
	
	Order findByCustomerId(int customerId);
	
	Order findByCustomerIdAndOrderStatus(int customerId, String orderStatus);
	 
	 
}
