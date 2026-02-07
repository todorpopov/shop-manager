package com.shop_manager;

import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.ui.ScreenManager;

public class Main
{
    public static void main(String[] args) {
        ReceiptLoaderService receiptLoaderService = new ReceiptLoaderService();

        ScreenManager screenManager = new ScreenManager(receiptLoaderService);
        screenManager.startUi();
    }
}
