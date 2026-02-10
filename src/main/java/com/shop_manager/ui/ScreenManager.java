package com.shop_manager.ui;

import com.shop_manager.services.CashierService;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.services.ProductService;
import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.screens.crud.cashier.CreateCashierScreen;
import com.shop_manager.ui.screens.crud.cashier.DeleteCashierScreen;
import com.shop_manager.ui.screens.crud.cashier.UpdateCashierScreen;
import com.shop_manager.ui.screens.crud.cashier.ViewAllCashiersScreen;
import com.shop_manager.ui.screens.crud.product.CreateProductScreen;
import com.shop_manager.ui.screens.crud.product.DeleteProductScreen;
import com.shop_manager.ui.screens.MainScreen;
import com.shop_manager.ui.screens.manage.ManageCashiersScreen;
import com.shop_manager.ui.screens.manage.ManageProductsScreen;
import com.shop_manager.ui.screens.manage.ManageScreen;
import com.shop_manager.ui.screens.manage.ManageStoresScreen;
import com.shop_manager.ui.screens.receipt_loader.ReceiptLoaderScreen;
import com.shop_manager.ui.screens.receipt_loader.ReceiptRenderScreen;
import com.shop_manager.ui.screens.crud.product.UpdateProductScreen;
import com.shop_manager.ui.screens.crud.product.ViewAllProductsScreen;
import com.shop_manager.ui.screens.crud.store.AddCashierScreen;
import com.shop_manager.ui.screens.crud.store.AddInventoryItemScreen;
import com.shop_manager.ui.screens.crud.store.CreateStoreScreen;
import com.shop_manager.ui.screens.crud.store.DeleteStoreScreen;
import com.shop_manager.ui.screens.crud.store.UpdateStoreScreen;
import com.shop_manager.ui.screens.crud.store.ViewAllStoresScreen;
import com.shop_manager.ui.screens.store_workflow.CheckoutScreen;
import com.shop_manager.ui.screens.store_workflow.SelectCashierScreen;
import com.shop_manager.ui.screens.store_workflow.SelectStoreScreen;
import com.shop_manager.ui.screens.store_workflow.ShopProductsScreen;

import java.util.HashMap;
import java.util.Scanner;

public class ScreenManager {
    private Screen currentScreen;
    private final Scanner scanner;
    private boolean running;
    private final HashMap<ScreenName, Screen> screens = new HashMap<>();

    private final ReceiptLoaderService receiptLoaderService;
    private final ProductService productService;
    private final CashierService cashierService;
    private final StoreService storeService;
    private final CheckoutService checkoutService;

    public ScreenManager(
        ReceiptLoaderService receiptLoaderService,
        ProductService productService,
        CashierService cashierService,
        StoreService storeService,
        CheckoutService checkoutService
    ) {
        this.scanner = new Scanner(System.in);
        this.running = true;
        this.receiptLoaderService = receiptLoaderService;
        this.productService = productService;
        this.cashierService = cashierService;
        this.storeService = storeService;
        this.checkoutService = checkoutService;
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

        screens.put(ScreenName.MANAGE_PRODUCTS_SCREEN, new ManageProductsScreen(this));
        screens.put(ScreenName.CREATE_PRODUCT_SCREEN, new CreateProductScreen(this, productService));
        screens.put(ScreenName.VIEW_ALL_PRODUCTS_SCREEN, new ViewAllProductsScreen(this, productService));
        screens.put(ScreenName.UPDATE_PRODUCT_SCREEN, new UpdateProductScreen(this, productService));
        screens.put(ScreenName.DELETE_PRODUCT_SCREEN, new DeleteProductScreen(this, productService));

        screens.put(ScreenName.MANAGE_CASHIERS_SCREEN, new ManageCashiersScreen(this));
        screens.put(ScreenName.CREATE_CASHIER_SCREEN, new CreateCashierScreen(this, cashierService));
        screens.put(ScreenName.VIEW_ALL_CASHIERS_SCREEN, new ViewAllCashiersScreen(this, cashierService));
        screens.put(ScreenName.UPDATE_CASHIER_SCREEN, new UpdateCashierScreen(this, cashierService));
        screens.put(ScreenName.DELETE_CASHIER_SCREEN, new DeleteCashierScreen(this, cashierService));

        screens.put(ScreenName.MANAGE_STORES_SCREEN, new ManageStoresScreen(this));
        screens.put(ScreenName.CREATE_STORE_SCREEN, new CreateStoreScreen(this, storeService));
        screens.put(ScreenName.VIEW_ALL_STORES_SCREEN, new ViewAllStoresScreen(this, storeService));
        screens.put(ScreenName.UPDATE_STORE_SCREEN, new UpdateStoreScreen(this, storeService));
        screens.put(ScreenName.DELETE_STORE_SCREEN, new DeleteStoreScreen(this, storeService));
        screens.put(ScreenName.ADD_CASHIER_SCREEN, new AddCashierScreen(this, storeService, cashierService));
        screens.put(ScreenName.ADD_INVENTORY_ITEM_SCREEN, new AddInventoryItemScreen(this, storeService, productService));

        screens.put(ScreenName.SELECT_STORE_SCREEN, new SelectStoreScreen(this, storeService, checkoutService));
        screens.put(ScreenName.SHOP_PRODUCTS_SCREEN, new ShopProductsScreen(this, checkoutService));
        screens.put(ScreenName.SELECT_CASHIER_SCREEN, new SelectCashierScreen(this, checkoutService));
        screens.put(ScreenName.CHECKOUT_SCREEN, new CheckoutScreen(this, checkoutService));

        screens.put(ScreenName.RECEIPT_LOADER_SCREEN, new ReceiptLoaderScreen(this, receiptLoaderService));
        screens.put(ScreenName.RECEIPT_RENDER_SCREEN, new ReceiptRenderScreen(this));
    }
}


