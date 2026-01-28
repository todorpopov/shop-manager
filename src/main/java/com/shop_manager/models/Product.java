package com.shop_manager.models;

import com.shop_manager.models.enums.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Product extends BaseModel {
    private final String name;
    private final BigDecimal deliveryPrice;
    private final LocalDate expirationDate;

    protected Product(
        long id,
        String name,
        BigDecimal deliveryPrice,
        LocalDate expirationDate
    ) {
        super(id);
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.expirationDate = expirationDate;
    }

    public abstract ProductCategory getCategory();

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public long daysUntilExpiration() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
}
