package com.prac.source.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac.source.payment.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Integer>{
	
	 Payment findByCustomerId(int customerId);
	 
	 
}
