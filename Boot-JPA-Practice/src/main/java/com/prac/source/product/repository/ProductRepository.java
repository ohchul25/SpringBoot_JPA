package com.prac.source.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prac.source.product.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Integer>{
	List<Product> findAll();
	
    @Modifying
    @Query("UPDATE Product p SET p.productStock = :stock WHERE p.productId = :productId")
    int updateProductStock(@Param("productId") int productId, @Param("stock") int stock);

}
