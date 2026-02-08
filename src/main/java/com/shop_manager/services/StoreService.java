package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.models.InventoryItem;
import com.shop_manager.models.Product;
import com.shop_manager.models.Store;
import com.shop_manager.repositories.StoreRepository;

import java.util.List;

public class StoreService {
    private static StoreService instance;

    private final StoreRepository storeRepository = StoreRepository.getInstance();

    private final CashierService cashierService = CashierService.getInstance();
    private final ProductService productService = ProductService.getInstance();

    private StoreService() {}

    public static StoreService getInstance() {
        if(instance == null) {
            instance = new StoreService();
        }
        return instance;
    }

    public void addStore(Store store) throws AlreadyExistsException, ConstraintViolationException {
        storeRepository.addStore(store);
    }

    public Store getStoreById(Long id) throws NotFoundException {
        return storeRepository.getStoreById(id);
    }

    public List<Store> getAllStores() {
        return storeRepository.getAllStores();
    }

    public void updateStore(Store store) throws NotFoundException, ConstraintViolationException {
        storeRepository.updateStore(store);
    }

    public void deleteStore(Long id) throws NotFoundException {
        storeRepository.deleteStore(id);
    }

    public void addCashier(Long storeId, Long cashierId) throws NotFoundException {
        Store store = getStoreById(storeId);
        Cashier cashier = cashierService.getCashierById(cashierId);
        store.addCashier(cashier);
    }

    public void addInventoryItem(Long storeId, Long productId, Integer quantity) throws NotFoundException {
        Store store = getStoreById(storeId);
        Product product = this.productService.getProductById(productId);
        InventoryItem inventoryItem = new InventoryItem(product, quantity);
        store.addInventoryItem(inventoryItem);
    }
}
