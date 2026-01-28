package com.shop_manager;

import com.shop_manager.models.Cashier;
import com.shop_manager.repositories.CashierRepository;

import java.math.BigDecimal;

public class Main
{
    public static void main(String[] args) {
        CashierRepository cr = CashierRepository.getInstance();
        cr.addCashier(new Cashier("1", BigDecimal.valueOf(-1)));
        cr.getAllCashiers().forEach(System.out::println);
    }
}
