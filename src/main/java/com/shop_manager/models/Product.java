package com.shop_manager.models;

import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.storage_engine.annotations.Length;
import com.shop_manager.storage_engine.annotations.Min;
import com.shop_manager.storage_engine.annotations.NotNull;
import com.shop_manager.storage_engine.annotations.Unique;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Product extends BaseModel {
    @NotNull
    @Length(min = 1, max = 255)
    @Unique
    private final String name;

    @NotNull
    @Min(0)
    private final BigDecimal deliveryPrice;

    @NotNull
    private final LocalDate expirationDate;

    @NotNull
    private final ProductCategory category;

    public Product(
        long id,
        String name,
        BigDecimal deliveryPrice,
        LocalDate expirationDate,
        ProductCategory category
    ) {
        super(id);
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.expirationDate = expirationDate;
        this.category = category;
    }

    public Product(
        String name,
        BigDecimal deliveryPrice,
        LocalDate expirationDate,
        ProductCategory category
    ) {
        super(null);
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.expirationDate = expirationDate;
        this.category = category;
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now());
    }

    public long daysUntilExpiration() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public BigDecimal calculateSellingPrice(Store store) {
        double markupPercent = category == ProductCategory.FOOD
            ? store.getFoodMarkupPercent()
            : store.getNonFoodMarkupPercent();

        BigDecimal markupMultiplier = BigDecimal.valueOf(1 + markupPercent / 100.0);
        BigDecimal priceWithMarkup = deliveryPrice.multiply(markupMultiplier);

        if (daysUntilExpiration() <= store.getDaysBeforeExpirationForDiscount()
            && daysUntilExpiration() > 0) {
            BigDecimal discountMultiplier = BigDecimal.valueOf(1 - store.getDiscountPercent() / 100.0);
            return priceWithMarkup.multiply(discountMultiplier);
        }

        return priceWithMarkup;
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

    public ProductCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Product{" +
            "name='" + name + '\'' +
            ", deliveryPrice=" + deliveryPrice +
            ", expirationDate=" + expirationDate +
            ", id=" + id +
            '}';
    }
}
