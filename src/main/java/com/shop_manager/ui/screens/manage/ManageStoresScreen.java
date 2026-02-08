package com.shop_manager.ui.screens.manage;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

public class ManageStoresScreen extends BaseScreen {
    public ManageStoresScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      MANAGE STORES                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Create Store");
        System.out.println("2. View All Stores");
        System.out.println("3. Update Store");
        System.out.println("4. Delete Store");
        System.out.println();
        System.out.println("5. Add Cashier to Store");
        System.out.println("6. Add Inventory Item to Store");
        System.out.println();
        System.out.println("0. Back to Manage Menu");
        System.out.println();
        System.out.print("Please select an option (0-6): ");
    }

    @Override
    public Screen handleInput() {
        String input = this.screenManager.nextLine();

        switch (input) {
            case "1":
                return screenManager.goToScreen(ScreenName.CREATE_STORE_SCREEN);
            case "2":
                return screenManager.goToScreen(ScreenName.VIEW_ALL_STORES_SCREEN);
            case "3":
                return screenManager.goToScreen(ScreenName.UPDATE_STORE_SCREEN);
            case "4":
                return screenManager.goToScreen(ScreenName.DELETE_STORE_SCREEN);
            case "5":
                return screenManager.goToScreen(ScreenName.ADD_CASHIER_SCREEN);
            case "6":
                return screenManager.goToScreen(ScreenName.ADD_INVENTORY_ITEM_SCREEN);
            case "0":
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
