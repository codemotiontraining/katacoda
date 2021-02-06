package com.example.cloudnative.repository;

import com.example.cloudnative.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private static Map<String, Product> productsDb = new HashMap<String, Product>();

    static {
        Product p1 = new Product("CDF5463GG56", "Macina Caffè", "Macchina Macina Caffè manuale");
        Product p2 = new Product("CDW4415HG36", "Set tazze", "Set 6 tazze da caffè");
        Product p3 = new Product("CDQ7422JE97", "Set Posate argentate", "Set 12 posate argentate");
        Product p4 = new Product("CDE9441KQ88", "Bollitore", "Bollitore elettrico");
        Product p5 = new Product("CDT7470LA79", "Profumatore", "Profumatore ambienti cannella e magnolia");
        Product p6 = new Product("CDO4499FS60", "Puff", "Puff porta oggetti grigio velluto");
        Product p7 = new Product("CDP1407DU51", "Lanterna", "Lanterna porta candele legno bianco");
        Product p8 = new Product("CDI2455ST42", "Vaso", "Vaso fiori grande bianco con decorazioni");
        Product p9 = new Product("CDY3438SY33", "Quadro", "Quadro 50x70 paesaggio illustrazione");

        productsDb.put(p1.getId(), p1);
        productsDb.put(p2.getId(), p2);
        productsDb.put(p3.getId(), p3);
        productsDb.put(p4.getId(), p4);
        productsDb.put(p5.getId(), p5);
        productsDb.put(p6.getId(), p6);
        productsDb.put(p7.getId(), p7);
        productsDb.put(p8.getId(), p8);
        productsDb.put(p9.getId(), p9);
    }

    @Override
    public Product findById(String productId) {
        return productsDb.get(productId);
    }
}
