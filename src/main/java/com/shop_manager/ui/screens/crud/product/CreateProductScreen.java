package com.shop_manager.ui.screens.crud.product;

import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.models.Product;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.services.ProductService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateProductScreen extends BaseScreen {
    private final ProductService productService;

    public CreateProductScreen(ScreenManager screenManager, ProductService productService) {
        super(screenManager);
        this.productService = productService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      CREATE NEW PRODUCT              ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String name = InputUtility.readString(screenManager, "Enter product name: ", 1, 255);

            BigDecimal deliveryPrice = InputUtility.readBigDecimal(
                screenManager,
                "Enter delivery price: ",
                BigDecimal.ZERO
            );

            ProductCategory category = InputUtility.readEnum(
                screenManager,
                "\nSelect product category:",
                ProductCategory.values()
            );

            LocalDate expirationDate;
            if (category == ProductCategory.FOOD) {
                expirationDate = InputUtility.readDate(screenManager, "\nEnter expiration date");
            } else {
                expirationDate = InputUtility.readOptionalDate(screenManager, "\nEnter expiration date");
            }

            System.out.println();

            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      PRODUCT SUMMARY                 ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + name);
            System.out.println("Delivery Price: " + deliveryPrice);
            System.out.println("Category: " + category.name());
            System.out.println("Expiration Date: " + (expirationDate != null ? expirationDate : "N/A"));
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to create this product?")) {
                System.out.println("Product creation cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);
            }

            Product product = new Product(name, deliveryPrice, expirationDate, category);
            productService.addProduct(product);

            System.out.println("Product created successfully!");
            System.out.println("Product ID: " + product.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);

        } catch (ConstraintViolationException e) {
            System.out.println("Validation Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Product creation cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);
        }
    }

    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}

