package com.shop_manager.ui.screens;

import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;


public class MainScreen extends BaseScreen {
    public MainScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      SHOP MANAGER - MAIN MENU        ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        System.out.println("1. Manage Entities");
        System.out.println("2. Enter Store");
        System.out.println("3. Load Receipts");
        System.out.println("4. Exit");
        System.out.println();
        System.out.print("Please select an option (1-4): ");
    }

    @Override
    public Screen handleInput() {
        String input = screenManager.nextLine();

        switch (input) {
            case "1":
                return screenManager.goToScreen(ScreenName.MANAGE);

            case "2":
                System.out.println("Enter Store menu - Coming soon!");
                waitForKey();
                return this;
            case "3":
                System.out.println("Load Receipts menu - Coming soon!");
                waitForKey();
                return this;

            case "4":
                screenManager.stop();
                return null;

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

