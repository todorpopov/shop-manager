package com.shop_manager;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.repositories.CashierRepository;
import com.shop_manager.repositories.ReceiptRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static void main(String[] args) {
        CashierRepository cr = CashierRepository.getInstance();
        ReceiptRepository rr = ReceiptRepository.getInstance();
        cr.addCashier(new Cashier("Asd", BigDecimal.valueOf(1000)));
        cr.getAllCashiers().forEach(System.out::println);

        Cashier savedCashier = cr.getCashierById(1L);
        List<ReceiptItem> items = new ArrayList<>();
        rr.addReceipt(new Receipt(savedCashier, LocalDateTime.now(), items, BigDecimal.TEN));
        rr.getAllReceipts().forEach(System.out::println);
    }
}
