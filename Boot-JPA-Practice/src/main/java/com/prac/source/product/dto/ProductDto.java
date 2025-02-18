package com.prac.source.product.dto;

import java.util.Date;

import com.prac.source.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	
	private int id; // 상품 ID
	private String name; // 상품 이름
	private String description; // 상품 설명
	private int price; // 상품 가격
	private int stock; // 재고 수량
	private Date createdAt; // 생성 시간
	private Date updatedAt; // 수정 시간

	public static ProductDto entityToDto(Product product) {
	    return new ProductDto(
	        product.getProductId(),
	        product.getProductName(),
	        product.getProductDescription(),
	        product.getProductPrice(),
	        product.getProductStock(),
	        product.getCreatedAt(),
	        product.getUpdatedAt()
	    );
	}

}
