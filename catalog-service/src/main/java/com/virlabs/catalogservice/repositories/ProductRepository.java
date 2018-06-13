package com.virlabs.catalogservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.virlabs.catalogservice.entities.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	Optional<Product> findByCode(String code);

}
