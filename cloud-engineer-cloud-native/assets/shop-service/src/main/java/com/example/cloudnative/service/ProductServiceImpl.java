package com.example.cloudnative.service;

import com.example.cloudnative.domain.Product;
import com.example.cloudnative.domain.Shop;
import com.example.cloudnative.dto.ProductDetails;
import com.example.cloudnative.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ShopRepository shopRepository;


    @Value("${productservice.url}")
    private String productServiceUrl;

    private static final String PRODUCT_DETAILS_URL = "%s/products/{productsId}";

    @Override
    public ProductDetails getProductDetails(String shopId, String productId) {
        return Optional.ofNullable(shopRepository.findById(shopId))
                                                 .map(shop -> composeProductDetailsResponse(shop, productId))
                                                 .orElseGet(() -> new ProductDetails());
    }

    private ProductDetails composeProductDetailsResponse(final Shop shop, String productId) {
        return new ProductDetails(shop, fetchProductDetails(productId));
    }

    private Product fetchProductDetails(String productId) {
        return restTemplate.exchange( String.format(PRODUCT_DETAILS_URL, productServiceUrl),
                                      HttpMethod.GET,
                                     null,
                                      new ParameterizedTypeReference<Product>(){},
                                      productId)
                            .getBody();
    }


}
