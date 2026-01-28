package com.shop_manager.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Store extends BaseModel {
    private final String name;

    private final double foodMarkupPercent;
    private final double nonFoodMarkupPercent;

    private final int daysBeforeExpirationForDiscount;
    private final double discountPercent;

    private final Map<Long, InventoryItem> inventory;
    private final List<Cashier> cashiers;
    private final List<Receipt> receipts;

    private long issuedReceiptsCount;
    private BigDecimal turnover;

    public Store(
        long id,
        String name,
        double foodMarkupPercent,
        double nonFoodMarkupPercent,
        int daysBeforeExpirationForDiscount,
        double discountPercent,
        Map<Long, InventoryItem> inventory
    ) {
        super(id);
        this.name = name;
        this.foodMarkupPercent = foodMarkupPercent;
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
        this.discountPercent = discountPercent;
        this.inventory = inventory;
        this.cashiers = new ArrayList<>();
        this.receipts = new ArrayList<>();
        this.turnover = BigDecimal.ZERO;
    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
    }

    public void addReceipt(Receipt receipt) {
        receipts.add(receipt);
        issuedReceiptsCount++;
        turnover = turnover.add(receipt.getTotalAmount());
    }

    public String getName() {
        return name;
    }

    public double getFoodMarkupPercent() {
        return foodMarkupPercent;
    }

    public double getNonFoodMarkupPercent() {
        return nonFoodMarkupPercent;
    }

    public int getDaysBeforeExpirationForDiscount() {
        return daysBeforeExpirationForDiscount;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public Map<Long, InventoryItem> getInventory() {
        return inventory;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public long getIssuedReceiptsCount() {
        return issuedReceiptsCount;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }
}
