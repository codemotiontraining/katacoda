package com.example.cloudnative.service;

import com.example.cloudnative.domain.Shop;
import com.example.cloudnative.dto.Inventory;
import com.example.cloudnative.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Override
    public Shop getDetails(String shopId) {
        return shopRepository.findById(shopId);
    }

    @Override
    public Inventory getInventory(String shopId) {
        return Optional.ofNullable(shopRepository.findById(shopId))
                       .map(shop -> new Inventory(shop, shopRepository.findInventoryProductsByShopId(shopId)))
                       .orElseGet(Inventory::new);
    }
}
