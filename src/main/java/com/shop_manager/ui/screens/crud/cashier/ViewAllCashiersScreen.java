package com.shop_manager.ui.screens.crud.cashier;

import com.shop_manager.models.Cashier;
import com.shop_manager.services.CashierService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.util.List;

public class ViewAllCashiersScreen extends BaseScreen {
    private final CashierService cashierService;

    public ViewAllCashiersScreen(ScreenManager screenManager, CashierService cashierService) {
        super(screenManager);
        this.cashierService = cashierService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                             ALL CASHIERS                                             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<Cashier> cashiers = cashierService.getAllCashiers();

        if (cashiers.isEmpty()) {
            System.out.println("No cashiers found in the database.");
            System.out.println();
        } else {
            System.out.println("Total Cashiers: " + cashiers.size());
            System.out.println();
            System.out.println("────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-30s %-15s%n",
                "ID", "Name", "Monthly Salary");
            System.out.println("────────────────────────────────────────────────────────────────────────────────────────");

            for (Cashier cashier : cashiers) {
                String name = cashier.getName();
                if (name.length() > 30) {
                    name = name.substring(0, 27) + "...";
                }

                System.out.printf("%-5d %-30s %-15s%n",
                    cashier.getId(),
                    name,
                    cashier.getMonthlySalary()
                );
            }

            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
            System.out.println();
        }

        System.out.println("Press Enter to return to Cashier Menu...");
    }

    @Override
   public Screen handleInput() {
        screenManager.nextLine();
        return screenManager.goToScreen(ScreenName.MANAGE_CASHIERS_SCREEN);
    }
}

