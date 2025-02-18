package com.prac.source.common;

import org.springframework.stereotype.Component;

import com.prac.source.customer.entity.Customer;
import com.prac.source.customer.repository.CustomerRepository;
import com.prac.source.product.entity.Product;
import com.prac.source.product.repository.ProductRepository;


@Component  // 스프링 빈으로 등록
public class CommonFunction {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public CommonFunction(ProductRepository productRepository
    						, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public Customer checkUser(int customerId) {
    	return customerRepository.findById(customerId)
    			.orElseThrow(() -> new IllegalArgumentException("유저정보가 존재하지 않습니다. customerId : " + customerId));
    }
    
    public Product getProduct(int productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. productId : " + productId));
    }
}