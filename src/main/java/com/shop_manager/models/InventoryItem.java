package com.shop_manager.models;

public class InventoryItem {
    private final Product product;
    private int quantity;

    public InventoryItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void decreaseQuantity(int amount) {
        this.quantity -= amount;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
