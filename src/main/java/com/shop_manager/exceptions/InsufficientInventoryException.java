package com.shop_manager.exceptions;

public class InsufficientInventoryException extends Exception {
    private final String productName;
    private final int requested;
    private final int available;

    public InsufficientInventoryException(String productName, int requested, int available) {
        super(String.format("Insufficient inventory for product '%s'. Requested: %d, Available: %d, Missing: %d",
            productName, requested, available, requested - available));
        this.productName = productName;
        this.requested = requested;
        this.available = available;
    }

    public String getProductName() {
        return productName;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }

    public int getMissing() {
        return requested - available;
    }
}

