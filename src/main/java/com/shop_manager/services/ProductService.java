package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Product;
import com.shop_manager.repositories.ProductRepository;

import java.util.List;

public class ProductService {
    private static ProductService instance;

    private final ProductRepository productRepository = ProductRepository.getInstance();

    private ProductService() {}

    public static ProductService getInstance() {
        if(instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public void addProduct(Product product) throws AlreadyExistsException, ConstraintViolationException {
        productRepository.addProduct(product);
    }

    public Product getProductById(Long id) throws NotFoundException {
        return productRepository.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public void updateProduct(Product product) throws NotFoundException, ConstraintViolationException {
        productRepository.updateProduct(product);
    }

    public void deleteProduct(Long id) throws NotFoundException {
        productRepository.deleteProduct(id);
    }
}
