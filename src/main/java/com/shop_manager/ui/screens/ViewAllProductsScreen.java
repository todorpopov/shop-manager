package com.shop_manager.ui.screens;

import com.shop_manager.models.Product;
import com.shop_manager.services.ProductService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;

import java.util.List;

public class ViewAllProductsScreen extends BaseScreen {
    private final ProductService productService;

    public ViewAllProductsScreen(ScreenManager screenManager, ProductService productService) {
        super(screenManager);
        this.productService = productService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                              ALL PRODUCTS                                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════════════════╝");
        System.out.println();

        List<Product> products = productService.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found in the database.");
            System.out.println();
        } else {
            System.out.println("Total Products: " + products.size());
            System.out.println();
            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
            System.out.printf("%-5s %-30s %-15s %-12s %-15s %-10s%n",
                "ID", "Name", "Price", "Category", "Expiration", "Expired");
            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");

            for (Product product : products) {
                String status = product.isExpired() ? "True" : "False";

                String name = product.getName();
                if (name.length() > 30) {
                    name = name.substring(0, 27) + "...";
                }

                String expirationStr = product.getExpirationDate() != null
                    ? product.getExpirationDate().toString()
                    : "N/A";

                System.out.printf("%-5d %-30s %-15s %-12s %-15s %-10s%n",
                    product.getId(),
                    name,
                    product.getDeliveryPrice(),
                    product.getCategory(),
                    expirationStr,
                    status
                );
            }

            System.out.println("─────────────────────────────────────────────────────────────────────────────────────────");
            System.out.println();
        }

        System.out.println("Press Enter to return to Product Menu...");
    }

    @Override
    public Screen handleInput() {
        screenManager.nextLine();
        return screenManager.goToScreen(ScreenName.PRODUCT_MANAGE_SCREEN);
    }
}

