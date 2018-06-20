package com.inventory.entities;

import javax.persistence.*;


@Entity
@Table(name="inventory")
public class InventoryItem {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="product_code",nullable=false,unique=true)
	private String productCode;
	
	@Column(name="quantity")
	private Integer availableQuantity =0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	
}
