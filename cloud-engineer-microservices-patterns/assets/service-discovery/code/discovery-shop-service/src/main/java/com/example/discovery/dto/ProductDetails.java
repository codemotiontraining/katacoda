package com.example.discovery.dto;

import com.example.discovery.domain.Product;
import com.example.discovery.domain.Shop;

public class ProductDetails {
	private Shop shop;
	private Product product;
	
	public ProductDetails() {}
	
	public ProductDetails(Shop shop, Product product) {
		this.shop = shop;
		this.product = product;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
