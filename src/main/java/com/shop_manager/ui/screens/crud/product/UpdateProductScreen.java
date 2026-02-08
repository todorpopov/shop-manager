package com.shop_manager.ui.screens.crud.product;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Product;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.services.ProductService;
import com.shop_manager.ui.BaseScreen;
import com.shop_manager.ui.Screen;
import com.shop_manager.ui.ScreenManager;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.exceptions.ScreenCanceledException;
import com.shop_manager.ui.utils.InputUtility;
import com.shop_manager.ui.utils.UpdateUtility;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UpdateProductScreen extends BaseScreen {
    private final ProductService productService;

    public UpdateProductScreen(ScreenManager screenManager, ProductService productService) {
        super(screenManager);
        this.productService = productService;
    }

    @Override
    public void display() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║      UPDATE PRODUCT                  ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("(Enter \"0\" at any time to cancel)");
        System.out.println();
    }

    @Override
    public Screen handleInput() {
        try {
            String productIdStr = InputUtility.readString(screenManager, "Enter product ID to update: ", 1, 10);
            long productId;
            try {
                productId = Long.parseLong(productIdStr);
            } catch (NumberFormatException e) {
                System.out.println("Error: Product ID must be a valid number.");
                waitForKey();
                return this;
            }

            Product existingProduct = productService.getProductById(productId);

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      CURRENT PRODUCT DETAILS         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + existingProduct.getName());
            System.out.println("Delivery Price: " + existingProduct.getDeliveryPrice());
            System.out.println("Category: " + existingProduct.getCategory());
            System.out.println("Expiration Date: " + (existingProduct.getExpirationDate() != null
                ? existingProduct.getExpirationDate() : "N/A"));
            System.out.println();

            System.out.println("Leave field empty to keep current value.");
            System.out.println();

            String newName = UpdateUtility.readUpdatedString(screenManager, "Enter new product name", existingProduct.getName());
            if (newName == null) {
                newName = existingProduct.getName();
            }

            BigDecimal newDeliveryPrice = UpdateUtility.readUpdatedBigDecimal(screenManager, "Enter new delivery price", existingProduct.getDeliveryPrice());
            if (newDeliveryPrice == null) {
                newDeliveryPrice = existingProduct.getDeliveryPrice();
            }

            ProductCategory newCategory = UpdateUtility.readUpdatedEnum(screenManager, "Select new product category", ProductCategory.values(), existingProduct.getCategory());
            if (newCategory == null) {
                newCategory = existingProduct.getCategory();
            }

            LocalDate newExpirationDate;
            if (newCategory == ProductCategory.FOOD) {
                newExpirationDate = UpdateUtility.readUpdatedFoodDate(screenManager, "Enter new expiration date", existingProduct.getExpirationDate());
            } else {
                newExpirationDate = UpdateUtility.readUpdatedNonFoodDate(screenManager, "Enter new expiration date", existingProduct.getExpirationDate());
            }
            if (newExpirationDate == null) {
                newExpirationDate = existingProduct.getExpirationDate();
            }

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║      UPDATED PRODUCT SUMMARY         ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("Name: " + newName);
            System.out.println("Delivery Price: " + newDeliveryPrice);
            System.out.println("Category: " + newCategory);
            System.out.println("Expiration Date: " + (newExpirationDate != null ? newExpirationDate : "N/A"));
            System.out.println();

            if (!InputUtility.readConfirmation(screenManager, "Do you want to update this product?")) {
                System.out.println("Product update cancelled.");
                waitForKey();
                return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);
            }

            Product updatedProduct = new Product(
                existingProduct.getId(),
                newName,
                newDeliveryPrice,
                newExpirationDate,
                newCategory
            );

            productService.updateProduct(updatedProduct);

            System.out.println("Product updated successfully!");
            System.out.println("Product ID: " + updatedProduct.getId());
            waitForKey();

            return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);

        } catch (NotFoundException e) {
            System.out.println("Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (AlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("A product with this name already exists. Please try again with a different name.");
            waitForKey();
            return this;
        } catch (ConstraintViolationException e) {
            System.out.println("Validation Error: " + e.getMessage());
            waitForKey();
            return this;
        } catch (ScreenCanceledException e) {
            System.out.println("Product update cancelled.");
            waitForKey();
            return screenManager.goToScreen(ScreenName.MANAGE_PRODUCTS_SCREEN);
        }
    }


    private void waitForKey() {
        System.out.println("\nPress Enter to continue...");
        screenManager.nextLine();
    }
}
