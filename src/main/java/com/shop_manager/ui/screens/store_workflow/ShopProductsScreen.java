package com.shop_manager.ui.screens.store_workflow;

import com.shop_manager.models.InventoryItem;
import com.shop_manager.models.Product;
import com.shop_manager.models.Store;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.utils.StringUtility;

import java.math.BigDecimal;
import java.util.List;

public class ShopProductsScreen extends BaseScreen {
    private final CheckoutService checkoutService;

    public ShopProductsScreen(ScreenManager screenManager, CheckoutService checkoutService) {
        super(screenManager);
        this.checkoutService = checkoutService;
    }

    @Override
    public void display() {
        Store selectedStore = checkoutService.getCurrentStore();
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.printf("║  SHOPPING AT: %-62s ║%n", selectedStore.getName());
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<InventoryItem> availableItems = checkoutService.getAvailableProducts();

        if (availableItems.isEmpty()) {
            System.out.println("No products available in this store.");
            System.out.println();
        } else {
            System.out.println("Available Products:");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-4s %-25s %-15s %-12s %-10s%n", "No.", "Product Name", "Price", "Stock", "Expiry");
            System.out.println("--------------------------------------------------------------------------------");

            for (int i = 0; i < availableItems.size(); i++) {
                InventoryItem item = availableItems.get(i);
                Product product = item.getProduct();
                BigDecimal price = product.calculateSellingPrice(selectedStore);

                String expiryStr = product.getExpirationDate() != null
                    ? product.getExpirationDate().toString()
                    : "N/A";

                System.out.printf("%-4d %-25s $%-14.2f %-12d %-10s%n",
                    i + 1,
                    StringUtility.truncate(product.getName(), 25),
                    price,
                    item.getQuantity(),
                    expiryStr);
            }
            System.out.println("--------------------------------------------------------------------------------");
        }

        if (checkoutService.isCartEmpty()) {
            System.out.println();
            System.out.println("Current Cart is empty.");
            System.out.println("--------------------------------------------------------------------------------");
        } else {
            System.out.println();
            System.out.println("Current Cart:");
            System.out.println("--------------------------------------------------------------------------------");
            checkoutService.getCart().forEach((product, quantity) ->
                System.out.printf("  %s x %d%n", product.getName(), quantity));
            System.out.println("--------------------------------------------------------------------------------");
        }

        System.out.println();
        if (!availableItems.isEmpty()) {
            System.out.println("Enter product number to add to cart");
        }
        System.out.println("C. Proceed to Checkout");
        System.out.println("0. Cancel and Go Back");
        System.out.println();
        System.out.print("Your choice: ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine().toUpperCase();

        if (input.equals("0")) {
            checkoutService.clearCart();
            return screenManager.goToScreen(ScreenName.SELECT_STORE_SCREEN);
        }

        if (input.equals("C")) {
            if (checkoutService.isCartEmpty()) {
                System.out.println("Your cart is empty. Please add items before checkout.");
                waitForKey();
                return this;
            }
            return screenManager.goToScreen(ScreenName.SELECT_CASHIER_SCREEN);
        }

        List<InventoryItem> availableItems = checkoutService.getAvailableProducts();
        if (availableItems.isEmpty()) {
            System.out.println("Invalid option. Please try again.");
            waitForKey();
            return this;
        }

        try {
            int selection = Integer.parseInt(input);
            if (selection < 1 || selection > availableItems.size()) {
                System.out.println("Invalid product selection. Please try again.");
                waitForKey();
                return this;
            }

            InventoryItem selectedItem = availableItems.get(selection - 1);
            Product selectedProduct = selectedItem.getProduct();

            int maxAvailable = checkoutService.getMaxAvailableQuantity(selectedItem);

            if (maxAvailable <= 0) {
                System.out.println("No more stock available for this product.");
                waitForKey();
                return this;
            }

            System.out.printf("How many %s would you like to add? (Max: %d): ",
                selectedProduct.getName(), maxAvailable);
            String quantityStr = screenManager.nextLine();

            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                    waitForKey();
                    return this;
                }
                if (quantity > maxAvailable) {
                    System.out.println("Not enough stock available.");
                    waitForKey();
                    return this;
                }

                checkoutService.addToCart(selectedProduct, quantity);
                System.out.printf("Added %d x %s to cart.%n", quantity, selectedProduct.getName());
                waitForKey();
                return this;

            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a number.");
                waitForKey();
                return this;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number, 'C', or '0'.");
            waitForKey();
            return this;
        }
    }


    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

