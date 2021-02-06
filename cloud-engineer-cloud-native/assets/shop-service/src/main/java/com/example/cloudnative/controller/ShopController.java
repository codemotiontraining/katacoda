package com.example.cloudnative.controller;

import com.example.cloudnative.service.ProductService;
import com.example.cloudnative.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.cloudnative.domain.*;
import com.example.cloudnative.dto.*;

@RestController
public class ShopController {

	@Autowired
	private ShopService shopService;

	@Autowired
	private ProductService productService;

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@GetMapping(value = "/v1/getShop/{shopId}", produces = "application/json")
	@CrossOrigin
	public Shop getShopDetailsV1(@PathVariable String shopId) {
		return shopService.getDetails(shopId);
	}

	@GetMapping(value = "/v1/getInventory/{shopId}", produces = "application/json")
	@CrossOrigin
	public Inventory getShopInventoryV1(@PathVariable String shopId) {
		return shopService.getInventory(shopId);
	}

	@GetMapping(value = "/v1/getProductDetails/{shopId}", produces = "application/json") //?productId=XYZK
	@CrossOrigin
	public ProductDetails getShopProductDetailsV1(@PathVariable String shopId, @RequestParam String productId) {
		return productService.getProductDetails(shopId, productId);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@GetMapping(value = "/v2/shops/{shopId}", produces = "application/json")
	@CrossOrigin
	public Shop getShopDetailsV2(@PathVariable String shopId) {
		return shopService.getDetails(shopId);
	}

    @GetMapping(value = "/v2/shops/{shopId}/products", produces = "application/json")
    @CrossOrigin
	public Inventory getShopInventoryV2(@PathVariable String shopId) {
		return shopService.getInventory(shopId);
	}

	@GetMapping(value = "/v2/shops/{shopId}/products/{productId}", produces = "application/json")
	@CrossOrigin
	public ProductDetails getShopProductDetailsV2(@PathVariable String shopId, @PathVariable String productId) {
		return productService.getProductDetails(shopId, productId);
	}

}
