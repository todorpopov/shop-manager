package com.shop_manager.exceptions;

public class ExpiredProductException extends Exception {
    private final String productName;

    public ExpiredProductException(String productName) {
        super(String.format("Product '%s' has expired and cannot be sold", productName));
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
}

