package com.shop_manager.ui.screens.store_workflow;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Store;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.util.List;

public class SelectCashierScreen extends BaseScreen {
    private final CheckoutService checkoutService;
    private static Cashier selectedCashier;

    public SelectCashierScreen(ScreenManager screenManager, CheckoutService checkoutService) {
        super(screenManager);
        this.checkoutService = checkoutService;
    }

    public static void setSelectedCashier(Cashier cashier) {
        selectedCashier = cashier;
    }

    public static Cashier getSelectedCashier() {
        return selectedCashier;
    }

    @Override
    public void display() {
        Store store = checkoutService.getCurrentStore();

        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         SELECT CASHIER                                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<Cashier> cashiers = store.getCashiers();

        if (cashiers.isEmpty()) {
            System.out.println("No cashiers available at this store.");
            System.out.println();
            System.out.println("0. Go Back");
        } else {
            System.out.println("Available Cashiers:");
            System.out.println();

            for (int i = 0; i < cashiers.size(); i++) {
                Cashier cashier = cashiers.get(i);
                System.out.printf("%d. [ID: %d] %s%n", i + 1, cashier.getId(), cashier.getName());
            }

            System.out.println();
            System.out.println("0. Go Back");
        }

        System.out.println();
        System.out.print("Please select a cashier: ");
    }

    @Override
    public Screen handleInput() {
        Store store = checkoutService.getCurrentStore();
        List<Cashier> cashiers = store.getCashiers();

        String input = screenManager.nextLine();

        if (input.equals("0")) {
            return screenManager.goToScreen(ScreenName.SHOP_PRODUCTS_SCREEN);
        }

        if (cashiers.isEmpty()) {
            System.out.println("Invalid option. Please try again.");
            waitForKey();
            return this;
        }

        try {
            int selection = Integer.parseInt(input);
            if (selection < 1 || selection > cashiers.size()) {
                System.out.println("Invalid cashier selection. Please try again.");
                waitForKey();
                return this;
            }

            selectedCashier = cashiers.get(selection - 1);
            return screenManager.goToScreen(ScreenName.CHECKOUT_SCREEN);

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
