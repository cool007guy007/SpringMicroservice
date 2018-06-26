package com.virlabs.catalogservice.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.virlabs.catalogservice.entities.Product;
import com.virlabs.catalogservice.repositories.ProductRepository;
import com.virlabs.catalogservice.utils.MyThreadLocalsHolder;
import com.virlabs.catalogservice.web.models.ProductInventoryResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j


public class ProductService {

	private final ProductRepository productRepository;
	private final InventoryServiceClient inventoryServiceClient;
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	public ProductService(ProductRepository productRepository, InventoryServiceClient inventoryServiceClient) {
		this.productRepository = productRepository;
		this.inventoryServiceClient = inventoryServiceClient;

	}

	public List<Product> findAllProducts() {

		List<Product> products = productRepository.findAll();
		final Map<String, Integer> inventoryLevels = getInventoryLevelsWithFeignClient();

		final List<Product> availableProducts = products.stream() 
				                 .filter(p -> inventoryLevels.get(p.getCode()) != null && inventoryLevels.get(p.getCode()) > 0) 
				                 .collect(Collectors.toList()); 
				         
		return availableProducts; 

	}

	private Map<String, Integer> getInventoryLevelsWithFeignClient() {
		log.info("Fetching inventory levels with FeignClient");
		Map<String, Integer> inventoryLevels = new HashMap<>();

		List<ProductInventoryResponse> inventory = inventoryServiceClient.getProductInventoryLevels();

		for (ProductInventoryResponse item : inventory) {
			inventoryLevels.put(item.getProductCode(), item.getAvailableQuantity());
		}

		log.debug("Inventory Levels: {}" + inventoryLevels);
		return inventoryLevels;
	}
	
	public  Optional<Product> findProductByCode(String code){
		
		Optional<Product> productOptional = productRepository.findByCode(code);
		if(productOptional.isPresent()) {
			
			String correlationId  = UUID.randomUUID().toString();
			
			MyThreadLocalsHolder.setCorrelationId(correlationId);
			
			log.info("Before CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId()); 
            log.info("Fetching inventory level for product_code: " + code);
            
           Optional<ProductInventoryResponse> itemResponseEntity =
        		   this.inventoryServiceClient.getProductInventoryByCode(code);
           
           if(itemResponseEntity.isPresent()) {
        	   Integer quantity = itemResponseEntity.get().getAvailableQuantity();
        	   productOptional.get().setInStock(quantity > 0);
           }
            log.info("After CorrelationID: "+ MyThreadLocalsHolder.getCorrelationId());
		}
		return productOptional;
	}
}
