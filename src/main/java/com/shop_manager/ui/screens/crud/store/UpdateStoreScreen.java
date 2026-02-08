package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Store;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;
import com.shop_manager.ui.utils.UpdateUtility;

public class UpdateStoreScreen extends BaseScreen {
    private final StoreService storeService;

    public UpdateStoreScreen(ScreenManager screenManager, StoreService storeService) {
        super(screenManager);
        this.storeService = storeService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      UPDATE STORE                    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String storeIdStr = InputUtility.readString(screenManager, "Enter store ID to update: ", 1, 10);
            long storeId;
            try {
                storeId = Long.parseLong(storeIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Store ID must be a valid number.");
                waitForKey();
                return this;
            }

            Store existingStore = storeService.getStoreById(storeId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      CURRENT STORE DETAILS           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + existingStore.getName());
            System.out.println("Food Markup Percentage: " + existingStore.getFoodMarkupPercent() + "%");
            System.out.println("Non-Food Markup Percentage: " + existingStore.getNonFoodMarkupPercent() + "%");
            System.out.println("Days Before Expiration for Discount: " + existingStore.getDaysBeforeExpirationForDiscount());
            System.out.println("Discount Percentage: " + existingStore.getDiscountPercent() + "%");
            System.out.println();

            System.out.println("Leave field empty to keep current value.");
            System.out.println();

            String newName = UpdateUtility.readUpdatedString(screenManager, "Enter new store name", existingStore.getName());
            if (newName == null) {
                newName = existingStore.getName();
            }

            Double newFoodMarkupPercent = UpdateUtility.readUpdatedDouble(screenManager, "Enter new food markup percentage", existingStore.getFoodMarkupPercent());
            if (newFoodMarkupPercent == null) {
                newFoodMarkupPercent = existingStore.getFoodMarkupPercent();
            }

            Double newNonFoodMarkupPercent = UpdateUtility.readUpdatedDouble(screenManager, "Enter new non-food markup percentage", existingStore.getNonFoodMarkupPercent());
            if (newNonFoodMarkupPercent == null) {
                newNonFoodMarkupPercent = existingStore.getNonFoodMarkupPercent();
            }

            Integer newDaysBeforeExpirationForDiscount = UpdateUtility.readUpdatedInt(screenManager, "Enter new days before expiration for discount", existingStore.getDaysBeforeExpirationForDiscount());
            if (newDaysBeforeExpirationForDiscount == null) {
                newDaysBeforeExpirationForDiscount = existingStore.getDaysBeforeExpirationForDiscount();
            }

            Double newDiscountPercent = UpdateUtility.readUpdatedDouble(screenManager, "Enter new discount percentage", existingStore.getDiscountPercent());
            if (newDiscountPercent == null) {
                newDiscountPercent = existingStore.getDiscountPercent();
            }

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      UPDATED STORE SUMMARY           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + newName);
            System.out.println("Food Markup Percentage: " + newFoodMarkupPercent + "%");
            System.out.println("Non-Food Markup Percentage: " + newNonFoodMarkupPercent + "%");
            System.out.println("Days Before Expiration for Discount: " + newDaysBeforeExpirationForDiscount);
            System.out.println("Discount Percentage: " + newDiscountPercent + "%");
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to update this store?")) {
                System.out.println("Store update cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
            }

            Store updatedStore = new Store(
                existingStore.getId(),
                newName,
                newFoodMarkupPercent,
                newNonFoodMarkupPercent,
                newDaysBeforeExpirationForDiscount,
                newDiscountPercent
            );

            storeService.updateStore(updatedStore);

            System.out.println("Store updated successfully!");
            System.out.println("Store ID: " + updatedStore.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
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
            System.out.println("Store update cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

