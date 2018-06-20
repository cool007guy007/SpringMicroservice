package com.virlabs.catalogservice.services;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.virlabs.catalogservice.entities.Product;
import com.virlabs.catalogservice.repositories.ProductRepository;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j


public class ProductService {
	
	private final ProductRepository productRepository;
	private final RestTemplate restTemplate;
	
	@Autowired
	public ProductService(ProductRepository productRepository,RestTemplate restTemplate){
		this.productRepository = productRepository;
		this.restTemplate= restTemplate;
	}
	
	public List<Product>  findAllProducts(){
		return productRepository.findAll();
	}
	
	public Optional<Product> findByProductCode(String productCode) {
		 Optional<Product> productoptional = productRepository.findByCode(productCode);
		 System.out.println("Fetching inventory level for product_code: "+productCode);
		 
		 ResponseEntity<ProductInventoryResponse> itemResponse = restTemplate.getForEntity("http://inventory-service/api/inventory/{productCode}", ProductInventoryResponse.class, productCode);
		 if(itemResponse.getStatusCode()==HttpStatus.OK) {
			 
			Integer quantity = itemResponse.getBody().getAvailableQuantity();
			System.out.println("Available Quantity"+quantity);
			
			productoptional.get().setInStock(quantity>0);
		 }else {
                System.out.println("Unable to get inventory level for product_code: "+productCode +
                ", StatusCode: "+itemResponse.getStatusCode());
            }

		 
		 return productoptional;
	}

}
