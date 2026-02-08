package com.shop_manager.ui.screens.crud.cashier;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.services.CashierService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;
import com.shop_manager.ui.utils.UpdateUtility;

import java.math.BigDecimal;

public class UpdateCashierScreen extends BaseScreen {
    private final CashierService cashierService;

    public UpdateCashierScreen(ScreenManager screenManager, CashierService cashierService) {
        super(screenManager);
        this.cashierService = cashierService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      UPDATE CASHIER                  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String productIdStr = InputUtility.readString(screenManager, "Enter cashier ID to update: ", 1, 10);
            long productId;
            try {
                productId = Long.parseLong(productIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Cashier ID must be a valid number.");
                waitForKey();
                return this;
            }

            Cashier existingCashier = cashierService.getCashierById(productId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      CURRENT PRODUCT DETAILS         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + existingCashier.getName());
            System.out.println("Monthly Salary: " + existingCashier.getMonthlySalary());
            System.out.println();

            System.out.println("Leave field empty to keep current value.");
            System.out.println();

            String newName = UpdateUtility.readUpdatedString(screenManager, "Enter new cashier name", existingCashier.getName());
            if (newName == null) {
                newName = existingCashier.getName();
            }

            BigDecimal newMonthlySalry = UpdateUtility.readUpdatedBigDecimal(screenManager, "Enter new monthly salary", existingCashier.getMonthlySalary());
            if (newMonthlySalry == null) {
                newMonthlySalry = existingCashier.getMonthlySalary();
            }

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      UPDATED CASHIER SUMMARY         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + newName);
            System.out.println("Monthly Salary: " + newMonthlySalry);
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to update this cashier?")) {
                System.out.println("Cashier update cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
            }

            Cashier updateCashier = new Cashier(
                existingCashier.getId(),
                newName,
                newMonthlySalry
            );

            cashierService.updateCashier(updateCashier);

            System.out.println("Cashier updated successfully!");
            System.out.println("Cashier ID: " + updateCashier.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (AlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("A cashier with this name already exists. Please try again with a different name.");
            waitForKey();
            return this;
        } catch (ConstraintViolationException e) {
            System.out.println("Validation Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Cashier update cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
        }
    }


    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}
