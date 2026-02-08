package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.services.CashierService;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class AddCashierScreen extends BaseScreen {
    private final StoreService storeService;
    private final CashierService cashierService;

    public AddCashierScreen(ScreenManager screenManager, StoreService storeService, CashierService cashierService) {
        super(screenManager);
        this.storeService = storeService;
        this.cashierService = cashierService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      ADD CASHIER TO STORE            ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String storeIdStr = InputUtility.readString(screenManager, "Enter store ID: ", 1, 10);
            long storeId;
            try {
                storeId = Long.parseLong(storeIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Store ID must be a valid number.");
                waitForKey();
                return this;
            }

            storeService.getStoreById(storeId);

            System.out.println();

            String cashierIdStr = InputUtility.readString(screenManager, "Enter cashier ID: ", 1, 10);
            long cashierId;
            try {
                cashierId = Long.parseLong(cashierIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Cashier ID must be a valid number.");
                waitForKey();
                return this;
            }

            cashierService.getCashierById(cashierId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      CONFIRMATION                    ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Store ID: " + storeId);
            System.out.println("Cashier ID: " + cashierId);
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to add this cashier to the store?")) {
                System.out.println("Operation cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
            }

            storeService.addCashier(storeId, cashierId);

            System.out.println("Cashier added to store successfully!");
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Operation cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

