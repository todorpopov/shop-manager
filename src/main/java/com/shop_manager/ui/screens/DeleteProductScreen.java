package com.shop_manager.ui.screens;

import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Product;
import com.shop_manager.services.ProductService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class DeleteProductScreen extends BaseScreen {
    private final ProductService productService;

    public DeleteProductScreen(ScreenManager screenManager, ProductService productService) {
        super(screenManager);
        this.productService = productService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      DELETE PRODUCT                  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String productIdStr = InputUtility.readString(screenManager, "Enter product ID to delete: ", 1, 10);
            long productId;
            try {
                productId = Long.parseLong(productIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Product ID must be a valid number.");
                waitForKey();
                return this;
            }

            Product productToDelete = productService.getProductById(productId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      PRODUCT TO DELETE               ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("ID: " + productToDelete.getId());
            System.out.println("Name: " + productToDelete.getName());
            System.out.println("Delivery Price: " + productToDelete.getDeliveryPrice());
            System.out.println("Category: " + productToDelete.getCategory());
            System.out.println("Expiration Date: " + (productToDelete.getExpirationDate() != null
                ? productToDelete.getExpirationDate() : "N/A"));
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Are you sure you want to delete this product?")) {
                System.out.println("Product deletion cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.PRODUCT_MANAGE_SCREEN);
            }

            productService.deleteProduct(productId);

            System.out.println("Product deleted successfully!");
            waitForKey();

            return screenManager.goToScreen(ScreenName.PRODUCT_MANAGE_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Product deletion cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.PRODUCT_MANAGE_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

