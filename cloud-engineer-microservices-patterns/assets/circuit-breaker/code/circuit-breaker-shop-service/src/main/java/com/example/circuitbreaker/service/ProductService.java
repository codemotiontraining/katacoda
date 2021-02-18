package com.example.circuitbreaker.service;

import com.example.circuitbreaker.domain.Product;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${productService.url}")
    private String productServiceUrl;

    @HystrixCommand(fallbackMethod = "fetchProductDetailsFallback")
    @SuppressWarnings("unused")
    public Product fetchProductDetails(String productId) {
        return restTemplate.exchange( productServiceUrl + "/products/{productsId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Product>(){},
                productId)
                .getBody();
    }

    private Product fetchProductDetailsFallback(String productId) {
        System.out.println("Product Service is down!!! fallback route enabled...");
        return null;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
