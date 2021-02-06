package com.example.cloudnative.repository;

import com.example.cloudnative.domain.Shop;

import java.util.List;

public interface ShopRepository {
    Shop findById(String shopId);
    List<String> findInventoryProductsByShopId(String shopId);
}
