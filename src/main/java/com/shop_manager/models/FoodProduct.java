package com.shop_manager.models;

import com.shop_manager.models.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FoodProduct extends Product {
    public FoodProduct(
        long id,
        String name,
        BigDecimal deliveryPrice,
        LocalDate expirationDate
    ) {
        super(id, name, deliveryPrice, expirationDate);
    }

    @Override
    public ProductCategory getCategory() {
        return ProductCategory.FOOD;
    }
}