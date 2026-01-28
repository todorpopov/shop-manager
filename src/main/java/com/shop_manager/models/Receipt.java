package com.shop_manager.models;

import com.shop_manager.storage_engine.annotations.Min;
import com.shop_manager.storage_engine.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt extends BaseModel {
    @NotNull
    private final Cashier cashier;

    @NotNull
    private final LocalDateTime issuedAt;

    @NotNull
    private final List<ReceiptItem> items;

    @NotNull
    @Min(0)
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

    public Receipt(
        Cashier cashier,
        LocalDateTime issuedAt,
        List<ReceiptItem> items,
        BigDecimal totalAmount
    ) {
        super(null);
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

    @Override
    public String toString() {
        return "Receipt{" +
            "id=" + id +
            ", totalAmount=" + totalAmount +
            ", items=" + items +
            ", issuedAt=" + issuedAt +
            ", cashier=" + cashier +
            '}';
    }
}
