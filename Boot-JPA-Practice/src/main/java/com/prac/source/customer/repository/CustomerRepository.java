package com.prac.source.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac.source.customer.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{
}
