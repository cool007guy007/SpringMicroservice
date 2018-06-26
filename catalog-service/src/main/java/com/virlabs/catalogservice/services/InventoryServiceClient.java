package com.virlabs.catalogservice.services;

import java.util.*;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.virlabs.catalogservice.utils.MyThreadLocalsHolder;
import com.virlabs.catalogservice.web.models.ProductInventoryResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryServiceClient {

	private final RestTemplate restTemplate;
	private final InventoryServiceFeignClient inventoryServiceFeignClient;

	private static final String INVENTORY_API_PATH = "http://inventory-service/api/";

	private static final Logger log = LoggerFactory.getLogger(InventoryServiceClient.class);

	@Autowired
	public InventoryServiceClient(RestTemplate restTemplate, InventoryServiceFeignClient inventoryServiceFeignClient) {
		this.restTemplate = restTemplate;
		this.inventoryServiceFeignClient = inventoryServiceFeignClient;

	}

	@HystrixCommand(fallbackMethod = "getDefaultProductInventoryLevels", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") }

	)

	public List<ProductInventoryResponse> getProductInventoryLevels() {
		return this.inventoryServiceFeignClient.getInventoryLevels();
	}

	@SuppressWarnings("unused")
	List<ProductInventoryResponse> getDefaultProductInventoryLevels() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "getDefaultProductInventoryByCode")
	public Optional<ProductInventoryResponse> getProductInventoryByCode(String code) {

		log.info("CorrelationID ::" + MyThreadLocalsHolder.getCorrelationId());
		ResponseEntity<ProductInventoryResponse> itemResponseEntity = restTemplate
				.getForEntity(INVENTORY_API_PATH + "inventory/{code}", ProductInventoryResponse.class, code);

		// Simulate Delay
		/*
		 * try { java.util.concurrent.TimeUnit.SECONDS.sleep(5); } catch
		 * (InterruptedException e) { e.printStackTrace(); }
		 */

		if (itemResponseEntity.getStatusCode() == HttpStatus.OK) {
			Integer quantity = itemResponseEntity.getBody().getAvailableQuantity();
			log.info("Available quantity: " + quantity);
			return Optional.ofNullable(itemResponseEntity.getBody());
		} else {
			log.error("Unable to get inventory level for product_code: " + code + ", StatusCode: "
					+ itemResponseEntity.getStatusCode());
			return Optional.empty();
		}
	}

	@SuppressWarnings("unused")
	Optional<ProductInventoryResponse> getDefaultProductInventoryByCode(String productCode) {
		log.info("Returning default ProductInventoryByCode for productCode: " + productCode);
		log.info("CorrelationID: " + MyThreadLocalsHolder.getCorrelationId());
		ProductInventoryResponse response = new ProductInventoryResponse();
		response.setProductCode(productCode);
		response.setAvailableQuantity(50);
		return Optional.ofNullable(response);
	}

}
