package com.prac.source.order.entity;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "order_detail")
@Entity
public class OrderDtl {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
    private int sn;	 // 주문 상세 순번

	@Getter
	@Column(name = "order_id", nullable = false)
	private int orderId;	 // 주문 ID
	
	@Getter
	@Column(name = "order_detail_stock", nullable = false)
	private int orderDetailStock;	 // 주문 당시 수량
	
//	@Getter
//	@Column(name = "product_id", nullable = false, insertable = false, updatable = false)
//	private int productId;	 // 상품 ID --> Product에서 참조

	@Getter
	@Column(name = "order_detail_name", nullable = false)
    private String orderDetailName;	// 주문 당시 상품 이름
	
	@Getter
	@Column(name = "order_detail_description", nullable = true)
	private String orderDetailDescription;	// 주문 당시 상품 설명
	
	@Getter
	@Column(name = "order_detail_price", nullable = false)
    private int orderDetailPrice;	 // 주문당시 상품 가격
	
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false) // Cart 테이블의 product_id와 Product 테이블의 id 매핑
    private Product product; // Product와의 연관 관계

	@Getter
	@JsonIgnore
	@Column(name = "created_at", nullable = true, insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;	// 생성 시간
    

}
