package com.shop_manager.storage_engine;

import com.shop_manager.models.*;

public class InMemoryDatabase {
    private static InMemoryDatabase instance;

    private final InMemoryTable<Cashier> cashiers = new InMemoryTable<>();
    private final InMemoryTable<Receipt> receipts = new InMemoryTable<>();
    private final InMemoryTable<Product> products = new InMemoryTable<>();
    private final InMemoryTable<Store> stores = new InMemoryTable<>();

    private InMemoryDatabase() {}

    public static InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
        }
        return instance;
    }

    public InMemoryTable<Cashier> cashiers() { return cashiers; }
    public InMemoryTable<Receipt> receipts() { return receipts; }
    public InMemoryTable<Product> products() { return products; }
    public InMemoryTable<Store> stores() { return stores; }
}
