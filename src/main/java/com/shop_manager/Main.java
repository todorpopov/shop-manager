package com.shop_manager;

import com.shop_manager.models.*;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.repositories.CashierRepository;
import com.shop_manager.repositories.ProductRepository;
import com.shop_manager.repositories.ReceiptRepository;
import com.shop_manager.repositories.StoreRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) {
        StoreRepository sr = StoreRepository.getInstance();
        Store store = new Store(
            10,
            "Test Store",
            0,
            0,
            0,
            0
        );
        sr.addStore(store);
        sr.getAllStores().forEach(System.out::println);
    }
}
