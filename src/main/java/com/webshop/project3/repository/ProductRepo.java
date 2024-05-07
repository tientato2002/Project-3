package com.webshop.project3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.webshop.project3.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE :x ")
	Page<Product> searchByName(@Param("x") String s, Pageable pageable);
}
