package com.shop_manager.ui.screens.store_workflow;

import com.shop_manager.models.Store;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.util.List;

public class SelectStoreScreen extends BaseScreen {
    private final StoreService storeService;
    private final CheckoutService checkoutService;
    private List<Store> stores;

    public SelectStoreScreen(ScreenManager screenManager, StoreService storeService, CheckoutService checkoutService) {
        super(screenManager);
        this.storeService = storeService;
        this.checkoutService = checkoutService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           SELECT STORE                                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        stores = storeService.getAllStores();

        if (stores.isEmpty()) {
            System.out.println("No stores available.");
            System.out.println();
            System.out.println("0. Go Back");
        } else {
            System.out.println("Available Stores:");
            System.out.println();

            for (int i = 0; i < stores.size(); i++) {
                Store store = stores.get(i);
                System.out.printf("%d. %s%n", i + 1, store.getName());
            }

            System.out.println();
            System.out.println("0. Go Back");
        }

        System.out.println();
        System.out.print("Please select a store: ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            return screenManager.goToScreen(ScreenName.MAIN_SCREEN);
        }

        if (stores.isEmpty()) {
            System.out.println("Invalid option. Please try again.");
            waitForKey();
            return this;
        }

        try {
            int selection = Integer.parseInt(input);
            if (selection < 1 || selection > stores.size()) {
                System.out.println("Invalid store selection. Please try again.");
                waitForKey();
                return this;
            }

            Store selectedStore = stores.get(selection - 1);
            checkoutService.setCurrentStore(selectedStore);
            return screenManager.goToScreen(ScreenName.SHOP_PRODUCTS_SCREEN);

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
