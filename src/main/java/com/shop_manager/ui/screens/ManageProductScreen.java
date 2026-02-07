package com.shop_manager.ui.screens;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

public class ManageProductScreen extends BaseScreen {
    public ManageProductScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      MANAGE PRODUCTS                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Create Product");
        System.out.println("2. View All Products");
        System.out.println("3. View Product by ID");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Back to Manage Menu");
        System.out.println();
        System.out.print("Please select an option (1-6): ");
    }

    @Override
    public Screen handleInput() {
        String input = this.screenManager.nextLine();

        switch (input) {
            case "1":
                return screenManager.goToScreen(ScreenName.CREATE_PRODUCT_SCREEN);
            case "2":
                return screenManager.goToScreen(ScreenName.VIEW_ALL_PRODUCTS_SCREEN);
            case "3":
                System.out.println("View Product by ID - Coming soon!");
                waitForKey();
                return this;
            case "4":
                System.out.println("Update Product - Coming soon!");
                waitForKey();
                return this;
            case "5":
                System.out.println("Delete Product - Coming soon!");
                waitForKey();
                return this;
            case "6":
                return screenManager.goToScreen(ScreenName.MANAGE_SCREEN);
            default:
                System.out.println("Invalid option. Please try again.");
                waitForKey();
                return this;
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

