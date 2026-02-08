package com.shop_manager.ui.screens.manage;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

public class ManageCashiersScreen extends BaseScreen {
    public ManageCashiersScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      MANAGE CASHIERS                 ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Create Cashier");
        System.out.println("2. View All Cashiers");
        System.out.println("3. Update Cashier");
        System.out.println("4. Delete Cashier");
        System.out.println();
        System.out.println("0. Back to Manage Menu");
        System.out.println();
        System.out.print("Please select an option (0-4): ");
    }

    @Override
    public Screen handleInput() {
        String input = this.screenManager.nextLine();

        switch (input) {
            case "1":
                return screenManager.goToScreen(ScreenName.CREATE_CASHIER_SCREEN);
            case "2":
                return screenManager.goToScreen(ScreenName.VIEW_ALL_CASHIERS_SCREEN);
            case "3":
                return screenManager.goToScreen(ScreenName.UPDATE_CASHIER_SCREEN);
            case "4":
                return screenManager.goToScreen(ScreenName.MANAGE_SCREEN);
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
