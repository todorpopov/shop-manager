package com.shop_manager.ui.screens.manage;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

public class ManageScreen extends BaseScreen {
    public ManageScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      MANAGE ENTITIES                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Manage Stores");
        System.out.println("2. Manage Products");
        System.out.println("3. Manage Cashiers");
        System.out.println();
        System.out.println("0. Back to Main Menu");
        System.out.println();
        System.out.print("Please select an option (0-3): ");
    }

    @Override
    public Screen handleInput() {
        String input = this.screenManager.nextLine();

        switch (input) {
            case "1":
                System.out.println("Manage Stores - Coming soon!");
                waitForKey();
                return this;
            case "2":
                return screenManager.goToScreen(ScreenName.PRODUCT_MANAGE_SCREEN);
            case "3":
                System.out.println("Manage Cashiers - Coming soon!");
                waitForKey();
                return this;
            case "0":
                return screenManager.goToScreen(ScreenName.MAIN_SCREEN);
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
