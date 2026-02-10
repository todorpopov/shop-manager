package com.shop_manager;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Store;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.services.CashierService;
import com.shop_manager.services.CheckoutService;
import com.shop_manager.services.ProductService;
import com.shop_manager.services.ReceiptLoaderService;
import com.shop_manager.services.ReceiptService;
import com.shop_manager.services.StoreService;
import com.shop_manager.ui.ScreenManager;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main
{
    public static void main(String[] args) {
        ReceiptLoaderService receiptLoaderService = ReceiptLoaderService.getInstance();
        ProductService productService = ProductService.getInstance();
        CashierService cashierService = CashierService.getInstance();
        StoreService storeService = StoreService.getInstance();
        CheckoutService checkoutService = CheckoutService.getInstance();
        ReceiptService receiptService = ReceiptService.getInstance();

        try {
            bootstrapEnvironment(storeService, cashierService, productService);
        } catch (Exception e) {
            System.err.println("Error bootstrapping environment: " + e.getMessage());
        }

        ScreenManager screenManager = new ScreenManager(
            receiptLoaderService,
            productService,
            cashierService,
            storeService,
            checkoutService,
            receiptService
        );
        screenManager.startUi();
    }

    private static void bootstrapEnvironment(
        StoreService storeService,
        CashierService cashierService,
        ProductService productService
    ) throws AlreadyExistsException, ConstraintViolationException, NotFoundException {

        Product apple = new Product("Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(7), ProductCategory.FOOD);
        Product banana = new Product("Banana", new BigDecimal("0.80"), LocalDate.now().plusDays(5), ProductCategory.FOOD);
        Product bread = new Product("Bread", new BigDecimal("2.00"), LocalDate.now().plusDays(3), ProductCategory.FOOD);
        Product milk = new Product("Milk", new BigDecimal("3.50"), LocalDate.now().plusDays(10), ProductCategory.FOOD);
        Product cheese = new Product("Cheese", new BigDecimal("5.00"), LocalDate.now().plusDays(14), ProductCategory.FOOD);
        Product expiringSoon = new Product("Yogurt", new BigDecimal("2.50"), LocalDate.now().plusDays(2), ProductCategory.FOOD);

        Product laptop = new Product("Laptop", new BigDecimal("800.00"), null, ProductCategory.NON_FOOD);
        Product phone = new Product("Phone", new BigDecimal("500.00"), null, ProductCategory.NON_FOOD);
        Product headphones = new Product("Headphones", new BigDecimal("50.00"), null, ProductCategory.NON_FOOD);
        Product notebook = new Product("Notebook", new BigDecimal("3.00"), null, ProductCategory.NON_FOOD);
        Product pen = new Product("Pen", new BigDecimal("1.00"), null, ProductCategory.NON_FOOD);

        productService.addProduct(apple);
        productService.addProduct(banana);
        productService.addProduct(bread);
        productService.addProduct(milk);
        productService.addProduct(cheese);
        productService.addProduct(expiringSoon);
        productService.addProduct(laptop);
        productService.addProduct(phone);
        productService.addProduct(headphones);
        productService.addProduct(notebook);
        productService.addProduct(pen);

        Cashier john = new Cashier("John Doe", new BigDecimal("2500.00"));
        Cashier jane = new Cashier("Jane Smith", new BigDecimal("2800.00"));
        Cashier bob = new Cashier("Bob Johnson", new BigDecimal("2600.00"));
        Cashier alice = new Cashier("Alice Williams", new BigDecimal("2700.00"));

        cashierService.addCashier(john);
        cashierService.addCashier(jane);
        cashierService.addCashier(bob);
        cashierService.addCashier(alice);

        Store mainStore = new Store("Main Street Store", 20.0, 15.0, 3, 10.0);
        Store mallStore = new Store("Shopping Mall Store", 25.0, 20.0, 5, 15.0);
        Store downtownStore = new Store("Downtown Store", 18.0, 12.0, 2, 8.0);

        storeService.addStore(mainStore);
        storeService.addStore(mallStore);
        storeService.addStore(downtownStore);

        storeService.addCashier(mainStore.getId(), john.getId());
        storeService.addCashier(mainStore.getId(), jane.getId());
        storeService.addCashier(mallStore.getId(), bob.getId());
        storeService.addCashier(mallStore.getId(), alice.getId());
        storeService.addCashier(downtownStore.getId(), john.getId());

        storeService.addInventoryItem(mainStore.getId(), apple.getId(), 100);
        storeService.addInventoryItem(mainStore.getId(), banana.getId(), 80);
        storeService.addInventoryItem(mainStore.getId(), bread.getId(), 50);
        storeService.addInventoryItem(mainStore.getId(), milk.getId(), 60);
        storeService.addInventoryItem(mainStore.getId(), expiringSoon.getId(), 30);
        storeService.addInventoryItem(mainStore.getId(), laptop.getId(), 10);
        storeService.addInventoryItem(mainStore.getId(), phone.getId(), 15);
        storeService.addInventoryItem(mainStore.getId(), headphones.getId(), 25);

        storeService.addInventoryItem(mallStore.getId(), apple.getId(), 120);
        storeService.addInventoryItem(mallStore.getId(), banana.getId(), 100);
        storeService.addInventoryItem(mallStore.getId(), cheese.getId(), 40);
        storeService.addInventoryItem(mallStore.getId(), milk.getId(), 80);
        storeService.addInventoryItem(mallStore.getId(), laptop.getId(), 20);
        storeService.addInventoryItem(mallStore.getId(), phone.getId(), 25);
        storeService.addInventoryItem(mallStore.getId(), notebook.getId(), 100);
        storeService.addInventoryItem(mallStore.getId(), pen.getId(), 200);

        storeService.addInventoryItem(downtownStore.getId(), bread.getId(), 40);
        storeService.addInventoryItem(downtownStore.getId(), milk.getId(), 50);
        storeService.addInventoryItem(downtownStore.getId(), cheese.getId(), 30);
        storeService.addInventoryItem(downtownStore.getId(), headphones.getId(), 20);
        storeService.addInventoryItem(downtownStore.getId(), notebook.getId(), 50);
    }
}
