package com.shop_manager.models;

import java.math.BigDecimal;

public class ReceiptItem {
    private final Product product;
    private final int quantity;
    private final BigDecimal pricePerUnit;

    public ReceiptItem(Product product, int quantity, BigDecimal pricePerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalPrice() {
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }
}
