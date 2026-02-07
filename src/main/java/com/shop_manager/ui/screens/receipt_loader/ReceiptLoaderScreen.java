package com.shop_manager.ui.screens.receipt_loader;

import com.shop_manager.models.Receipt;
import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceiptLoaderScreen extends BaseScreen {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ReceiptLoaderService receiptLoaderService;
    private List<Receipt> receipts;

    public ReceiptLoaderScreen(ScreenManager screenManager, ReceiptLoaderService receiptLoaderService) {
        super(screenManager);
        this.receiptLoaderService = receiptLoaderService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              LOAD RECEIPTS                                   ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        String errorMessage;
        try {
            receipts = receiptLoaderService.loadReceipts();
            errorMessage = null;
        } catch (IOException e) {
            errorMessage = "Error loading receipts: " + e.getMessage();
            receipts = List.of();
        }

        if (errorMessage != null) {
            System.out.println("Error: " + errorMessage);
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();
            System.out.print("Please select an option: ");
            return;
        }

        if (receipts.isEmpty()) {
            System.out.println("No receipts found in the receipts directory.");
            System.out.println();
            System.out.println("0. Back to Main Menu");
            System.out.println();
            System.out.print("Please select an option: ");
            return;
        }

        System.out.println("Found " + receipts.size() + " receipt(s):");
        System.out.println();

        for (int i = 0; i < receipts.size(); i++) {
            Receipt receipt = receipts.get(i);
            System.out.printf("%d. Receipt #%d - %s - Total: $%.2f%n",
                i + 1,
                receipt.getId(),
                receipt.getIssuedAt().format(DATE_TIME_FORMATTER),
                receipt.getTotalAmount()
            );
        }

        System.out.println();
        System.out.println("0. Back to Main Menu");
        System.out.println();
        System.out.print("Please select a receipt to view (0-" + receipts.size() + "): ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine();

        if ("0".equals(input)) {
            return screenManager.goToScreen(ScreenName.MAIN_SCREEN);
        }

        if (receipts == null || receipts.isEmpty()) {
            return screenManager.goToScreen(ScreenName.MAIN_SCREEN);
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice >= 1 && choice <= receipts.size()) {
                Receipt selectedReceipt = receipts.get(choice - 1);
                ReceiptRenderScreen renderScreen = (ReceiptRenderScreen) screenManager.goToScreen(ScreenName.RECEIPT_RENDER_SCREEN);
                renderScreen.setReceipt(selectedReceipt);
                return renderScreen;
            } else {
                System.out.println("Invalid option. Please try again.");
                waitForKey();
                return this;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            waitForKey();
            return this;
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}


