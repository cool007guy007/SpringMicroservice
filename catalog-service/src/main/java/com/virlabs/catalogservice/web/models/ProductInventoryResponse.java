package com.virlabs.catalogservice.web.models;


public class ProductInventoryResponse {
    private String productCode;
    private int availableQuantity=0;
    
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
    
    
}
