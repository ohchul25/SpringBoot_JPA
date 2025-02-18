package com.prac.source.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prac.source.order.entity.OrderDtl;


public interface OrderDtlRepository extends JpaRepository<OrderDtl, Integer>{
	
	@Query(value = ""
			+ "SELECT "
			+ "	o.*, p.product_stock "
			+ "FROM "
			+ "	order_detail o "
			+ "		JOIN product p ON o.product_id = p.product_id "
			+ "WHERE o.order_id = :orderId"
			, nativeQuery = true)
	List<OrderDtl> findByOrderId(int orderId);
	
	 
}
