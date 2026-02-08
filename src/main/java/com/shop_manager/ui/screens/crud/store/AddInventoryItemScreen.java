package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.services.ProductService;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class AddInventoryItemScreen extends BaseScreen {
    private final StoreService storeService;
    private final ProductService productService;

    public AddInventoryItemScreen(ScreenManager screenManager, StoreService storeService, ProductService productService) {
        super(screenManager);
        this.storeService = storeService;
        this.productService = productService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   ADD INVENTORY ITEM TO STORE        ║");
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

            String productIdStr = InputUtility.readString(screenManager, "Enter product ID: ", 1, 10);
            long productId;
            try {
                productId = Long.parseLong(productIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Product ID must be a valid number.");
                waitForKey();
                return this;
            }

            productService.getProductById(productId);

            System.out.println();

            Integer quantity = readQuantity("Enter quantity: ");

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      CONFIRMATION                    ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Store ID: " + storeId);
            System.out.println("Product ID: " + productId);
            System.out.println("Quantity: " + quantity);
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to add this item to the store inventory?")) {
                System.out.println("Operation cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
            }

            storeService.addInventoryItem(storeId, productId, quantity);

            System.out.println("Inventory item added to store successfully!");
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

    private Integer readQuantity(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = screenManager.nextLine();

            if (input.equals("0")) {
                throw new ScreenCanceledException("Operation cancelled by user.");
            }

            if (input.isBlank()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);

                if (value <= 0) {
                    System.out.println("Error: Quantity must be greater than 0.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

