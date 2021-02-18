package com.example.circuitbreaker.controller;

import com.example.circuitbreaker.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.circuitbreaker.domain.*;
import com.example.circuitbreaker.dto.*;

import java.util.*;

@RestController
public class ShopController {

	@Autowired
	private ProductService productService;

	static Map<String, Shop> shops = new HashMap<>();
    static Map<String, List<String>> productIds = new HashMap<>();

	static {
		Shop s1 = new Shop("WEDD321", "roma@casacasacasa.it", "067647284932734", "Via dei Gigli 43, Roma (IT)");
		Shop s2 = new Shop("OTDD422", "catania@casacasacasa.it", "06764728432734", "Via Franchetti 218, Catania (IT)");
		Shop s3 = new Shop("AQSW165", "milano@casacasacasa.it", "067647835932734", "Via Formidori 56, Milano (IT)");

		shops.put(s1.getId(), s1);
		shops.put(s2.getId(), s2);
		shops.put(s3.getId(), s3);

		productIds.put(s1.getId(), Arrays.asList("CDF5463GG56", "CDW4415HG36", "CDQ7422JE97"));
		productIds.put(s2.getId(), Arrays.asList("CDE9441KQ88", "CDT7470LA79", "CDO4499FS60"));
		productIds.put(s3.getId(), Arrays.asList("CDP1407DU51", "CDI2455ST42", "CDY3438SY33"));
	}

	@GetMapping("/shops/{shopId}")
	@CrossOrigin
	public Shop getShopDetails(@PathVariable String shopId) {
		return shops.get(shopId);
	}

    @GetMapping("/shops/{shopId}/products")
    @CrossOrigin
	public Inventory getShopInventory(@PathVariable String shopId) {
		return Optional.ofNullable(shops.get(shopId))
                       .map(shop -> new Inventory(shop, productIds.get(shopId)))
                       .orElseGet(() -> new Inventory());
	}

	@RequestMapping(value = "/shops/{shopId}/products/{productId}", method = RequestMethod.GET)
	@CrossOrigin
	public ProductDetails getShopProductDetails(@PathVariable String shopId, @PathVariable String productId) {
		return Optional.ofNullable(shops.get(shopId))
		               .map(shop -> composeProductDetailsResponse(shop, productId))
		               .orElseGet(() -> new ProductDetails());
	}

	private ProductDetails composeProductDetailsResponse(final Shop shop, String productId) {
		return new ProductDetails(shop, productService.fetchProductDetails(productId));
	}

}
