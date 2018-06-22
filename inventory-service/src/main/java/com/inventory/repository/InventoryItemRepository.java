package com.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

	Optional<InventoryItem> findByProductCode(String productCode);
}
