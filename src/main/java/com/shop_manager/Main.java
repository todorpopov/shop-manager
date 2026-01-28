package com.shop_manager;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.repositories.CashierRepository;
import com.shop_manager.repositories.ProductRepository;
import com.shop_manager.repositories.ReceiptRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) {
        ProductRepository pr = ProductRepository.getInstance();

        Product p = new Product(
            "Cheese",
            BigDecimal.TEN,
            LocalDate.now(),
            ProductCategory.FOOD
        );
        Product p2 = new Product(
            null,
            BigDecimal.TEN,
            LocalDate.now(),
            ProductCategory.FOOD
        );

        pr.addProduct(p);
        pr.addProduct(p2);
        pr.getAllProducts().forEach(System.out::println);
    }
}
