package com.shop_manager.ui.screens.store_workflow;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Store;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.utils.StringUtility;

import java.math.BigDecimal;
import java.util.Map;

public class CheckoutScreen extends BaseScreen {
    private final CheckoutService checkoutService;

    public CheckoutScreen(ScreenManager screenManager, CheckoutService checkoutService) {
        super(screenManager);
        this.checkoutService = checkoutService;
    }

    @Override
    public void display() {
        Store store = checkoutService.getCurrentStore();
        Cashier cashier = SelectCashierScreen.getSelectedCashier();
        Map<Product, Integer> cart = checkoutService.getCart();

        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            CHECKOUT                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.printf("Store: %s%n", store.getName());
        System.out.printf("Cashier: %s%n", cashier.getName());
        System.out.println();
        System.out.println("Items:");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-30s %-10s %-15s %-15s%n", "Product", "Quantity", "Price/Unit", "Subtotal");
        System.out.println("--------------------------------------------------------------------------------");

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal pricePerUnit = product.calculateSellingPrice(store);
            BigDecimal subtotal = pricePerUnit.multiply(BigDecimal.valueOf(quantity));

            System.out.printf("%-30s %-10d $%-14.2f $%-14.2f%n",
                StringUtility.truncate(product.getName(), 30),
                quantity,
                pricePerUnit,
                subtotal);
        }

        BigDecimal total = checkoutService.calculateTotal();
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-56s $%-14.2f%n", "TOTAL:", total);
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("1. Confirm Purchase");
        System.out.println("0. Cancel and Go Back");
        System.out.println();
        System.out.print("Your choice: ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine();

        if (input.equals("0")) {
            return screenManager.goToScreen(ScreenName.SELECT_CASHIER_SCREEN);
        }

        if (input.equals("1")) {
            try {
                Cashier cashier = SelectCashierScreen.getSelectedCashier();
                checkoutService.processCheckout(cashier);
                System.out.println();
                System.out.println("Purchase completed successfully!");
                System.out.println("Receipt has been generated and saved.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MAIN_SCREEN);
            } catch (Exception e) {
                System.out.println("Error processing checkout: " + e.getMessage());
                waitForKey();
                return this;
            }
        }

        System.out.println("Invalid option. Please try again.");
        waitForKey();
        return this;
    }


    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}
