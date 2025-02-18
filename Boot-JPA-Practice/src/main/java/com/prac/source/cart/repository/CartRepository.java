package com.prac.source.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prac.source.cart.entity.Cart;

import jakarta.transaction.Transactional;


public interface CartRepository extends JpaRepository<Cart, Integer>{
	
	List<Cart> findByCustomerId(int customerId);
	Cart findByCustomerIdAndProduct_ProductId(int customerId, int productId);
	 
	@Query(value = ""
			+ "SELECT "
			+ "        c.cart_id , c.customer_id , c.cart_stock, p.* "
			+ "FROM "
			+ "	cart c "
			+ "		JOIN product p ON c.product_id = p.product_id "
			+ "WHERE c.customer_id = :customerId"
			, nativeQuery = true)
	List<Cart> findByCustomerIdJoinProduct(int customerId);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "DELETE FROM cart WHERE customer_id = :customerId AND product_id = :productId", nativeQuery = true)
	void deleteByCustomerIdAndProductId(@Param("customerId") int customerId, @Param("productId") int productId);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "DELETE FROM cart WHERE customer_id = :customerId", nativeQuery = true)
    void deleteByCustomerId(@Param("customerId") int customerId);
	 
}
