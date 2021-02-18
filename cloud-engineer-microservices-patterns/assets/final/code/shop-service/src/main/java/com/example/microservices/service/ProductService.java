package com.example.microservices.service;

import com.example.microservices.domain.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    @Autowired
    RestTemplate restTemplate;

    private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(ProductService.class.getName());

    @HystrixCommand(fallbackMethod = "fetchProductDetailsFallback")
    @SuppressWarnings("unused")
    public Product fetchProductDetails(String productId) {
        return restTemplate.exchange( "http://product-service/products/{productsId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Product>(){},
                productId)
                .getBody();
    }

    private Product fetchProductDetailsFallback(String productId) {
        log.warning("Product Service is down!!! fallback route enabled...");
        return null;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
