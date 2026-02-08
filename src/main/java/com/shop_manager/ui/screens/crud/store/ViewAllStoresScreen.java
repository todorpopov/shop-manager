package com.shop_manager.ui.screens.crud.store;

import com.shop_manager.models.Store;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.util.List;

public class ViewAllStoresScreen extends BaseScreen {
    private final StoreService storeService;

    public ViewAllStoresScreen(ScreenManager screenManager, StoreService storeService) {
        super(screenManager);
        this.storeService = storeService;
    }

    @Override
    public void display() {
        System.out.println("╔════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                           ALL STORES                                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<Store> stores = storeService.getAllStores();

        if (stores.isEmpty()) {
            System.out.println("No stores found in the database.");
            System.out.println();
        } else {
            System.out.println("Total Stores: " + stores.size());
            System.out.println();
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-25s %-12s %-12s %-18s %-12s %-10s %-12s%n",
                "ID", "Name", "Food %", "Non-Food %", "Days for Disc.", "Discount %", "Receipts", "Turnover");
            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

            for (Store store : stores) {
                String name = store.getName();
                if (name.length() > 25) {
                    name = name.substring(0, 22) + "...";
                }

                System.out.printf("%-5d %-25s %-12.2f %-12.2f %-18d %-12.2f %-10d %-12.2f%n",
                    store.getId(),
                    name,
                    store.getFoodMarkupPercent(),
                    store.getNonFoodMarkupPercent(),
                    store.getDaysBeforeExpirationForDiscount(),
                    store.getDiscountPercent(),
                    store.getIssuedReceiptsCount(),
                    store.getTurnover()
                );
            }

            System.out.println("──────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
            System.out.println();
        }

        System.out.println("Press Enter to return to Store Menu...");
    }

    @Override
   public Screen handleInput() {
        screenManager.nextLine();
        return screenManager.goToScreen(ScreenName.MANAGE_STORES_SCREEN);
    }
}

