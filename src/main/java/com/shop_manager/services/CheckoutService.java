package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.models.Cashier;
import com.shop_manager.models.InventoryItem;
import com.shop_manager.models.Product;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.models.Store;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutService {
    private static CheckoutService instance;

    private final ReceiptService receiptService;
    private final ReceiptLoaderService receiptLoaderService;

    private Store currentStore;
    private final Map<Product, Integer> cart;

    private CheckoutService() {
        this.receiptService = ReceiptService.getInstance();
        this.receiptLoaderService = ReceiptLoaderService.getInstance();
        this.cart = new HashMap<>();
    }

    public static CheckoutService getInstance() {
        if (instance == null) {
            instance = new CheckoutService();
        }
        return instance;
    }

    public void setCurrentStore(Store store) {
        this.currentStore = store;
        cart.clear();
    }

    public Store getCurrentStore() {
        return currentStore;
    }

    public Map<Product, Integer> getCart() {
        return cart;
    }

    public void addToCart(Product product, int quantity) {
        int currentQuantity = cart.getOrDefault(product, 0);
        cart.put(product, currentQuantity + quantity);
    }

    public void clearCart() {
        cart.clear();
    }

    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public int getCartQuantity(Product product) {
        return cart.getOrDefault(product, 0);
    }

    public List<InventoryItem> getAvailableProducts() {
        if (currentStore == null) {
            return List.of();
        }

        List<InventoryItem> available = new ArrayList<>();
        for (InventoryItem item : currentStore.getInventory()) {
            Product product = item.getProduct();
            if (!product.isExpired() && item.getQuantity() > 0) {
                available.add(item);
            }
        }
        return available;
    }

    public int getMaxAvailableQuantity(InventoryItem item) {
        int currentInCart = getCartQuantity(item.getProduct());
        return item.getQuantity() - currentInCart;
    }

    public BigDecimal calculateTotal() {
        if (currentStore == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal pricePerUnit = product.calculateSellingPrice(currentStore);
            BigDecimal subtotal = pricePerUnit.multiply(BigDecimal.valueOf(quantity));
            total = total.add(subtotal);
        }
        return total;
    }

    public void processCheckout(Cashier cashier) throws AlreadyExistsException, ConstraintViolationException, IOException {
        if (currentStore == null) {
            throw new IllegalStateException("No store selected");
        }
        if (cashier == null) {
            throw new IllegalArgumentException("Cashier must not be null");
        }
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        List<ReceiptItem> receiptItems = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal pricePerUnit = product.calculateSellingPrice(currentStore);

            ReceiptItem receiptItem = new ReceiptItem(product, quantity, pricePerUnit);
            receiptItems.add(receiptItem);

            InventoryItem inventoryItem = findInventoryItem(product);
            if (inventoryItem != null) {
                inventoryItem.decreaseQuantity(quantity);
            }
        }

        BigDecimal totalAmount = calculateTotal();

        Receipt receipt = new Receipt(
            currentStore,
            cashier,
            LocalDateTime.now(),
            receiptItems,
            totalAmount
        );

        receiptService.addReceipt(receipt);
        currentStore.addReceipt(receipt);
        receiptLoaderService.saveReceipt(receipt);

        cart.clear();
    }

    private InventoryItem findInventoryItem(Product product) {
        if (currentStore == null) {
            return null;
        }

        for (InventoryItem item : currentStore.getInventory()) {
            if (item.getProduct().getId().equals(product.getId())) {
                return item;
            }
        }
        return null;
    }
}
