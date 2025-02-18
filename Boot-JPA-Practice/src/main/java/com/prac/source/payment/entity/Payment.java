package com.prac.source.payment.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "payment")
@Entity
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	@Getter
    private int paymentId;	 // 결제 ID

	@Column(name = "order_id")
	@Getter
	private int orderId;	 // 주문 ID

	@Getter
	@Column(name = "customer_id", nullable = false)
	private int customerId;	 // 고객 ID
	
	@Getter
	@Column(name = "payment_amount", nullable = false)
	private int paymentAmount;	 // 결제 금액

	@Getter
    @Column(name = "payment_status",nullable = false)
    private String paymentStatus;	// 결제 상태

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
