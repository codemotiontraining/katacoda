package com.example.cloudnative.service;

import com.example.cloudnative.dto.ProductDetails;

public interface ProductService {
    ProductDetails getProductDetails(String shopId, String productId);
}
