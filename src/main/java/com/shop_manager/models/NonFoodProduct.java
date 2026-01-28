package com.shop_manager.models;

import com.shop_manager.models.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NonFoodProduct extends Product {
    public NonFoodProduct(
        long id,
        String name,
        BigDecimal deliveryPrice,
        LocalDate expirationDate
    ) {
        super(id, name, deliveryPrice, expirationDate);
    }

    @Override
    public ProductCategory getCategory() {
        return ProductCategory.NON_FOOD;
    }
}