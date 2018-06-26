package com.virlabs.catalogservice.services;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.virlabs.catalogservice.web.models.ProductInventoryResponse;


@FeignClient(name = "inventory-service")
public interface InventoryServiceFeignClient {
	
	
	@GetMapping("/api/inventory")
	List<ProductInventoryResponse> getInventoryLevels();
	
	@GetMapping("/api/inventory/{code}")
	List<ProductInventoryResponse> getInventoryByProductCode(String code);
	
}
