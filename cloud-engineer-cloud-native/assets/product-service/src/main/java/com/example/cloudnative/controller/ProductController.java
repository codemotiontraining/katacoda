package com.example.cloudnative.controller;

import com.example.cloudnative.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.cloudnative.domain.Product;

@RestController
public class ProductController {

    @Autowired
	private ProductRepository productRepository;

	@GetMapping(value = "/products/{productId}", produces = "application/json")
	@CrossOrigin
	public Product getProductDetails(@PathVariable String productId) {
		return productRepository.findById(productId);
	}
}
