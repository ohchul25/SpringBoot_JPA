package com.prac.source.cart.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prac.source.product.entity.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	@Getter
    private int cartId;	 // 장바구니 ID
	
	@Getter
	@Column(name = "customer_id", nullable = false)
	private int customerId;	 // 고객 ID
	
//	@Getter
//	@Column(name = "product_id", nullable = false)
//	private int productId;	 // 상품 ID --> PRODUCT에서 참조

	@Getter
	@Column(name = "cart_stock", nullable = false)
    private int cartStock;	// 재고 수량

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false) // Cart 테이블의 product_id와 Product 테이블의 id 매핑
    private Product product; // Product와의 연관 관계
    
	@Getter
	@JsonIgnore
	@Column(name = "created_at", nullable = true, insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;	// 생성 시간
    
	@Getter
	@JsonIgnore
	@Column(name = "updated_at", nullable = true, insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;	// 수정 시간


}
