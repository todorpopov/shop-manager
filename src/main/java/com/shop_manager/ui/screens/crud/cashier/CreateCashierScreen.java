package com.shop_manager.ui.screens.crud.cashier;

import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.models.Cashier;
import com.shop_manager.services.CashierService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

import java.math.BigDecimal;

public class CreateCashierScreen extends BaseScreen {
    private final CashierService cashierService;

    public CreateCashierScreen(ScreenManager screenManager, CashierService cashierService) {
        super(screenManager);
        this.cashierService = cashierService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      CREATE NEW CASHIER              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String name = InputUtility.readString(screenManager, "Enter cashier name: ", 1, 255);

            BigDecimal monthlySalary = InputUtility.readBigDecimal(
                screenManager,
                "Enter monthly salary: ",
                BigDecimal.ZERO
            );

            System.out.println();

            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      PRODUCT SUMMARY                 ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + name);
            System.out.println("Monthly Salary: " + monthlySalary);
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to create this cashier?")) {
                System.out.println("Cashier creation cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
            }

            Cashier cashier = new Cashier(name, monthlySalary);
            cashierService.addCashier(cashier);

            System.out.println("Cashier created successfully!");
            System.out.println("Cashier ID: " + cashier.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);

        } catch (ConstraintViolationException e) {
            System.out.println("Validation Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Cashier creation cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}
