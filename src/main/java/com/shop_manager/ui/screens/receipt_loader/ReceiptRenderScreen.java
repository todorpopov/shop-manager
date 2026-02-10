package com.shop_manager.ui.screens.receipt_loader;

import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class ReceiptRenderScreen extends BaseScreen {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Receipt receipt;

    public ReceiptRenderScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public void display() {
        if (receipt == null) {
            System.out.println("Error: No receipt to display");
            return;
        }

        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                    RECEIPT                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Receipt ID: " + receipt.getId());
        System.out.println("Date & Time: " + receipt.getIssuedAt().format(DATE_TIME_FORMATTER));
        System.out.println("Store: " + receipt.getStore().getName());
        System.out.println("Cashier: " + receipt.getCashier().getName());
        System.out.println();
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-40s %8s %12s %15s%n", "Product", "Qty", "Price/Unit", "Total");
        System.out.println("────────────────────────────────────────────────────────────────────────────────");

        for (ReceiptItem item : receipt.getItems()) {
            String productName = item.getProduct().getName();
            if (productName.length() > 40) {
                productName = productName.substring(0, 37) + "...";
            }

            System.out.printf("%-40s %8d %12s %15s%n",
                productName,
                item.getQuantity(),
                formatMoney(item.getPricePerUnit()),
                formatMoney(item.getTotalPrice())
            );
        }

        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        System.out.printf("%63s %15s%n", "TOTAL:", formatMoney(receipt.getTotalAmount()));
        System.out.println("════════════════════════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("Press Enter to go back...");
    }

    @Override
    public Screen handleInput() {
        screenManager.nextLine();
        return screenManager.goToScreen(ScreenName.RECEIPT_LOADER_SCREEN);
    }

    private String formatMoney(BigDecimal amount) {
        return String.format("$%.2f", amount);
    }
}
