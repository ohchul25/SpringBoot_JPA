package com.prac.source.product.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prac.source.product.dto.ProductDto;
import com.prac.source.product.entity.Product;
import com.prac.source.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	@Autowired	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
    public List<ProductDto> getList() {
        return productRepository.findAll().stream()
                .map(ProductDto::entityToDto)
                .collect(Collectors.toList());
    }



}
