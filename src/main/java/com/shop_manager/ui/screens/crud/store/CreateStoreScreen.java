package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.models.Store;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class CreateStoreScreen extends BaseScreen {
    private final StoreService storeService;

    public CreateStoreScreen(ScreenManager screenManager, StoreService storeService) {
        super(screenManager);
        this.storeService = storeService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      CREATE NEW STORE                ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String name = InputUtility.readString(screenManager, "Enter store name: ", 1, 255);

            double foodMarkupPercent = readPercentage("Enter food markup percentage: ");

            double nonFoodMarkupPercent = readPercentage("Enter non-food markup percentage: ");

            int daysBeforeExpirationForDiscount = readDays("Enter days before expiration for discount: ");

            double discountPercent = readPercentage("Enter discount percentage: ");

            System.out.println();

            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      STORE SUMMARY                   ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + name);
            System.out.println("Food Markup Percentage: " + foodMarkupPercent + "%");
            System.out.println("Non-Food Markup Percentage: " + nonFoodMarkupPercent + "%");
            System.out.println("Days Before Expiration for Discount: " + daysBeforeExpirationForDiscount);
            System.out.println("Discount Percentage: " + discountPercent + "%");
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to create this store?")) {
                System.out.println("Store creation cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
            }

            Store store = new Store(
                name,
                foodMarkupPercent,
                nonFoodMarkupPercent,
                daysBeforeExpirationForDiscount,
                discountPercent
            );
            storeService.addStore(store);

            System.out.println("Store created successfully!");
            System.out.println("Store ID: " + store.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);

        } catch (AlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("A store with this name already exists. Please try again with a different name.");
            waitForKey();
            return this;
        } catch (ConstraintViolationException e) {
            System.out.println("Validation Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Store creation cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
        }
    }

    private double readPercentage(String prompt) {
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
                double value = Double.parseDouble(input);

                if (value < 0) {
                    System.out.println("Error: Percentage must be at least 0.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    private int readDays(String prompt) {
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

                if (value < 0) {
                    System.out.println("Error: Days must be at least 0.");
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

