package com.shop_manager;

import com.shop_manager.services.ProductService;
import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.ui.ScreenManager;

public class Main
{
    public static void main(String[] args) {
        ReceiptLoaderService receiptLoaderService = ReceiptLoaderService.getInstance();
        ProductService productService = ProductService.getInstance();

        ScreenManager screenManager = new ScreenManager(receiptLoaderService, productService);
        screenManager.startUi();
    }
}
