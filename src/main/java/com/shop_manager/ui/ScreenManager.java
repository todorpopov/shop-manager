package com.shop_manager.ui;

import com.shop_manager.services.ProductService;
import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.screens.CreateProductScreen;
import com.shop_manager.ui.screens.DeleteProductScreen;
import com.shop_manager.ui.screens.MainScreen;
import com.shop_manager.ui.screens.ManageProductScreen;
import com.shop_manager.ui.screens.ManageScreen;
import com.shop_manager.ui.screens.ReceiptLoaderScreen;
import com.shop_manager.ui.screens.ReceiptRenderScreen;
import com.shop_manager.ui.screens.UpdateProductScreen;
import com.shop_manager.ui.screens.ViewAllProductsScreen;

import java.util.HashMap;
import java.util.Scanner;

public class ScreenManager {
    private Screen currentScreen;
    private final Scanner scanner;
    private boolean running;
    private final HashMap<ScreenName, Screen> screens = new HashMap<>();

    private final ReceiptLoaderService receiptLoaderService;
    private final ProductService productService;

    public ScreenManager(
        ReceiptLoaderService receiptLoaderService,
        ProductService productService
    ) {
        this.scanner = new Scanner(System.in);
        this.running = true;
        this.receiptLoaderService = receiptLoaderService;
        this.productService = productService;
        initializeScreens();
    }

    public void startUi() {
        currentScreen = screens.get(ScreenName.MAIN_SCREEN);

        while (running && currentScreen != null) {
            clearScreen();
            currentScreen.display();
            Screen nextScreen = currentScreen.handleInput();

            if (nextScreen != null) {
                currentScreen = nextScreen;
            }
        }

        shutdown();
    }

    public void stop() {
        running = false;
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public Screen goToScreen(ScreenName screenName) {
        currentScreen = screens.get(screenName);
        return currentScreen;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void shutdown() {
        if (scanner != null) {
            scanner.close();
        }
    }

    private void initializeScreens() {
        screens.put(ScreenName.MAIN_SCREEN, new MainScreen(this));
        screens.put(ScreenName.MANAGE_SCREEN, new ManageScreen(this));
        screens.put(ScreenName.PRODUCT_MANAGE_SCREEN, new ManageProductScreen(this));
        screens.put(ScreenName.CREATE_PRODUCT_SCREEN, new CreateProductScreen(this, productService));
        screens.put(ScreenName.VIEW_ALL_PRODUCTS_SCREEN, new ViewAllProductsScreen(this, productService));
        screens.put(ScreenName.UPDATE_PRODUCT_SCREEN, new UpdateProductScreen(this, productService));
        screens.put(ScreenName.DELETE_PRODUCT_SCREEN, new DeleteProductScreen(this, productService));
        screens.put(ScreenName.RECEIPT_LOADER_SCREEN, new ReceiptLoaderScreen(this, receiptLoaderService));
        screens.put(ScreenName.RECEIPT_RENDER_SCREEN, new ReceiptRenderScreen(this));
    }
}


