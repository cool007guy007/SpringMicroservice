package com.virlabs.catalogservice.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.virlabs.catalogservice.entities.Product;
import com.virlabs.catalogservice.exceptions.ProductNotFoundException;
import com.virlabs.catalogservice.services.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {
	
	private final ProductService productService;
	
	
	@Autowired
	public  ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("")
	public List<Product> getAllProducts(){
		return productService.findAllProducts();
	}
	
	@GetMapping("/{code}")
	public Product getProductbyCode(@PathVariable String code) {
		
		return productService.findByProductCode(code).orElseThrow(() -> new ProductNotFoundException("Product with code ["+code+"] doesn't exist"));
		
	}
	
	

}
