package com.shop_manager.ui.screens.crud.cashier;

import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.services.CashierService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

public class DeleteCashierScreen extends BaseScreen {
    private final CashierService cashierService;

    public DeleteCashierScreen(ScreenManager screenManager, CashierService cashierService) {
        super(screenManager);
        this.cashierService = cashierService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      DELETE CASHIER                  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String cashierIdStr = InputUtility.readString(screenManager, "Enter cashier ID to delete: ", 1, 10);
            long cashierId;
            try {
                cashierId = Long.parseLong(cashierIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Cashier ID must be a valid number.");
                waitForKey();
                return this;
            }

            Cashier cashierToDelete = cashierService.getCashierById(cashierId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      PRODUCT TO DELETE               ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("ID: " + cashierToDelete.getId());
            System.out.println("Name: " + cashierToDelete.getName());
            System.out.println("Monthly Salary: " + cashierToDelete.getMonthlySalary());
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Are you sure you want to delete this cashier?")) {
                System.out.println("Cashier deletion cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
            }

            cashierService.deleteCashier(cashierId);

            System.out.println("Cashier deleted successfully!");
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Cashier deletion cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}
