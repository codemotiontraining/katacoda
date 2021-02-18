package com.example.circuitbreaker.dto;

import com.example.circuitbreaker.domain.*;

import java.util.List;

public class Inventory {
	private Shop shop;
	private List<String> products;
	
	public Inventory() {}
	
	public Inventory(Shop shop, List<String> products) {
		this.shop = shop;
		this.products = products;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}
}
