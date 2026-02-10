package com.shop_manager.ui.screens.crud.receipt;

import com.shop_manager.models.Receipt;
import com.shop_manager.services.ReceiptService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.utils.StringUtility;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewAllReceiptsScreen extends BaseScreen {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ReceiptService receiptService;

    public ViewAllReceiptsScreen(ScreenManager screenManager, ReceiptService receiptService) {
        super(screenManager);
        this.receiptService = receiptService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             ALL RECEIPTS                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<Receipt> receipts = receiptService.getAllReceipts();

        if (receipts.isEmpty()) {
            System.out.println("No receipts found in the database.");
            System.out.println();
        } else {
            System.out.println("Total Receipts: " + receipts.size());
            System.out.println();
            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-20s %-20s %-20s %-10s%n",
                "ID", "Store", "Cashier", "Issued At", "Total");
            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");

            for (Receipt receipt : receipts) {
                String storeName = StringUtility.truncate(receipt.getStore().getName(), 20);
                String cashierName = StringUtility.truncate(receipt.getCashier().getName(), 20);
                String issuedAt = receipt.getIssuedAt().format(DATE_TIME_FORMATTER);

                System.out.printf("%-5d %-20s %-20s %-20s %-10s%n",
                    receipt.getId(),
                    storeName,
                    cashierName,
                    issuedAt,
                    receipt.getTotalAmount().toPlainString()
                );
            }

            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
            System.out.println();
        }

        System.out.println("Press Enter to return to Receipts Menu...");
    }

    @Override
    public Screen handleInput() {
        screenManager.nextLine();
        return screenManager.goToScreen(ScreenName.MANAGE_RECEIPTS_SCREEN);
    }
}

