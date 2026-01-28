package com.shop_manager.repositories;

import com.shop_manager.models.Product;
import com.shop_manager.storage_engine.InMemoryDatabase;
import com.shop_manager.storage_engine.InMemoryTable;

import java.util.List;

public class ProductRepository {
    private static ProductRepository instance;

    private final InMemoryTable<Product> products = InMemoryDatabase.getInstance().products();

    private ProductRepository() {}

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public void addProduct(Product product) {
        products.insert(product);
    }

    public Product getProductById(Long id) {
        return products.get(id);
    }

    public List<Product> getAllProducts() {
        return products.all();
    }

    public void updateProduct(Product product) {
        products.update(product);
    }

    public void deleteProduct(Long id) {
        products.delete(id);
    }
}
