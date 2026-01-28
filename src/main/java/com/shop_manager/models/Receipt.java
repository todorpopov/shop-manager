package com.shop_manager.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt extends BaseModel {
    private final Cashier cashier;
    private final LocalDateTime issuedAt;
    private final List<ReceiptItem> items;
    private final BigDecimal totalAmount;

    public Receipt(
        long id,
        Cashier cashier,
        LocalDateTime issuedAt,
        List<ReceiptItem> items,
        BigDecimal totalAmount
    ) {
        super(id);
        this.cashier = cashier;
        this.issuedAt = issuedAt;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public List<ReceiptItem> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
