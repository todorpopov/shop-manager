package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Store;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class DeleteStoreScreen extends BaseScreen {
    private final StoreService storeService;

    public DeleteStoreScreen(ScreenManager screenManager, StoreService storeService) {
        super(screenManager);
        this.storeService = storeService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      DELETE STORE                    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String storeIdStr = InputUtility.readString(screenManager, "Enter store ID to delete: ", 1, 10);
            long storeId;
            try {
                storeId = Long.parseLong(storeIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Store ID must be a valid number.");
                waitForKey();
                return this;
            }

            Store storeToDelete = storeService.getStoreById(storeId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      STORE TO DELETE                 ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("ID: " + storeToDelete.getId());
            System.out.println("Name: " + storeToDelete.getName());
            System.out.println("Food Markup Percentage: " + storeToDelete.getFoodMarkupPercent() + "%");
            System.out.println("Non-Food Markup Percentage: " + storeToDelete.getNonFoodMarkupPercent() + "%");
            System.out.println("Days Before Expiration for Discount: " + storeToDelete.getDaysBeforeExpirationForDiscount());
            System.out.println("Discount Percentage: " + storeToDelete.getDiscountPercent() + "%");
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Are you sure you want to delete this store?")) {
                System.out.println("Store deletion cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
            }

            storeService.deleteStore(storeId);

            System.out.println("Store deleted successfully!");
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Store deletion cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

