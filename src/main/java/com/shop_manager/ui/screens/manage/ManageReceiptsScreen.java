package com.shop_manager.ui.screens.manage;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

public class ManageReceiptsScreen extends BaseScreen {
    public ManageReceiptsScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       MANAGE RECEIPTS                ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. View All Receipts");
        System.out.println();
        System.out.println("0. Back to Manage Menu");
        System.out.println();
        System.out.print("Please select an option (0-1): ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine();

        switch (input) {
            case "1":
                return screenManager.goToScreen(ScreenName.VIEW_ALL_RECEIPTS_SCREEN);
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

