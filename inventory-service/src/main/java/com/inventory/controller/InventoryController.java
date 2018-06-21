package com.inventory.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.entities.InventoryItem;
import com.inventory.repository.InventoryItemRepository;


import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class InventoryController {
	
	
	private final InventoryItemRepository inventoryItemRepository;
	private static final Logger log = (Logger) LoggerFactory.logger(InventoryController.class);

	@Autowired
	public InventoryController(InventoryItemRepository inventoryItemRepository) {
		this.inventoryItemRepository = inventoryItemRepository;
	}
	
	
	@GetMapping("/api/inventory/{productCode}")
	public ResponseEntity<InventoryItem> getProductByCode(@PathVariable("productCode") String productCode){
		log.info("Finding inventory for product code"+productCode);
		
		Optional<InventoryItem> inventoryItem= inventoryItemRepository.findByProductCode(productCode);
		
		if(inventoryItem.isPresent())
			return new ResponseEntity(inventoryItem,HttpStatus.OK);
		else
			return new ResponseEntity(HttpStatus.NOT_FOUND);
					
				
	}

}
