package com.prac.source.product.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
@Entity
@Data
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="product_id")
	@Getter
    private int productId;	 // 상품 ID

	@Getter
    @Column(name="product_name", nullable = false)
    private String productName;	// 상품 이름

	@Getter
	@Column(name="product_description", nullable = true)
    private String productDescription;	// 상품 설명
	
	@Getter
    @Column(name="product_price", nullable = false)
    private int productPrice;	 // 상품 가격

	@Getter
    @Column(name="product_stock", nullable = false, insertable = false, updatable = false)
    private int productStock;	// 재고 수량

	@Getter
    @Column(name="created_at", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;	// 생성 시간
    
	@Getter
    @Column(name="updated_at", nullable = false, insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;	// 수정 시간
	
	

}
