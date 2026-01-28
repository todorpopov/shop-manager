package com.shop_manager.models;

import com.shop_manager.storage_engine.annotations.Length;
import com.shop_manager.storage_engine.annotations.Min;
import com.shop_manager.storage_engine.annotations.NotNull;

import java.math.BigDecimal;

public class Cashier extends BaseModel {
    @NotNull
    @Length(min = 1, max = 255)
    private final String name;

    @NotNull
    @Min(0)
    private final BigDecimal monthlySalary;

    public Cashier(long id, String name, BigDecimal monthlySalary) {
        super(id);
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public Cashier(String name, BigDecimal monthlySalary) {
        super(null);
        this.name = name;
        this.monthlySalary = monthlySalary;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    @Override
    public String toString() {
        return "Cashier{" +
            "id=" + id +
            ", monthlySalary=" + monthlySalary +
            ", name='" + name + '\'' +
            '}';
    }
}
