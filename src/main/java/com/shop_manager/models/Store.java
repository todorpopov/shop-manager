package com.shop_manager.models;

import com.shop_manager.storage_engine.annotations.Length;
import com.shop_manager.storage_engine.annotations.Min;
import com.shop_manager.storage_engine.annotations.NotNull;
import com.shop_manager.storage_engine.annotations.Unique;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Store extends BaseModel {
    @NotNull
    @Unique
    @Length(min = 1, max = 255)
    private final String name;

    @NotNull
    @Min(0)
    private final double foodMarkupPercent;
    @NotNull
    @Min(0)
    private final double nonFoodMarkupPercent;

    @NotNull
    @Min(0)
    private final int daysBeforeExpirationForDiscount;
    @NotNull
    @Min(0)
    private final double discountPercent;

    private final List<InventoryItem> inventory;
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
        double discountPercent
    ) {
        super(id);
        this.name = name;
        this.foodMarkupPercent = foodMarkupPercent;
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
        this.discountPercent = discountPercent;
        this.inventory = new ArrayList<>();
        this.cashiers = new ArrayList<>();
        this.receipts = new ArrayList<>();
        this.issuedReceiptsCount = 0;
        this.turnover = BigDecimal.ZERO;
    }

    public Store(
        String name,
        double foodMarkupPercent,
        double nonFoodMarkupPercent,
        int daysBeforeExpirationForDiscount,
        double discountPercent
    ) {
        super(null);
        this.name = name;
        this.foodMarkupPercent = foodMarkupPercent;
        this.nonFoodMarkupPercent = nonFoodMarkupPercent;
        this.daysBeforeExpirationForDiscount = daysBeforeExpirationForDiscount;
        this.discountPercent = discountPercent;
        this.inventory = new ArrayList<>();
        this.cashiers = new ArrayList<>();
        this.receipts = new ArrayList<>();
        this.issuedReceiptsCount = 0;
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

    public List<InventoryItem> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public List<Cashier> getCashiers() {
        return Collections.unmodifiableList(cashiers);
    }

    public List<Receipt> getReceipts() {
        return Collections.unmodifiableList(receipts);
    }

    public long getIssuedReceiptsCount() {
        return issuedReceiptsCount;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public BigDecimal calculateTotalDeliveryCosts() {
        return inventory.stream()
            .map(item -> item.getProduct().getDeliveryPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalSalaryExpenses() {
        return cashiers.stream()
            .map(Cashier::getMonthlySalary)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalExpenses() {
        return calculateTotalDeliveryCosts().add(calculateTotalSalaryExpenses());
    }

    public BigDecimal calculateProfit() {
        return turnover.subtract(calculateTotalExpenses());
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", foodMarkupPercent=" + foodMarkupPercent +
            ", nonFoodMarkupPercent=" + nonFoodMarkupPercent +
            ", daysBeforeExpirationForDiscount=" + daysBeforeExpirationForDiscount +
            ", discountPercent=" + discountPercent +
            ", inventory=" + inventory +
            ", cashiers=" + cashiers +
            ", receipts=" + receipts +
            ", issuedReceiptsCount=" + issuedReceiptsCount +
            ", turnover=" + turnover +
            '}';
    }
}
