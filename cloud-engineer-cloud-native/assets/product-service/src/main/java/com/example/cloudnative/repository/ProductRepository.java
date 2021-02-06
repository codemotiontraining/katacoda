package com.example.cloudnative.repository;

import com.example.cloudnative.domain.Product;

public interface ProductRepository {
    Product findById(String productId);
}
