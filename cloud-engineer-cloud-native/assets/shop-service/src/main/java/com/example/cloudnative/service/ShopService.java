package com.example.cloudnative.service;

import com.example.cloudnative.domain.Shop;
import com.example.cloudnative.dto.Inventory;

public interface ShopService {
    Shop getDetails(String shopId);
    Inventory getInventory(String shopId);
}
