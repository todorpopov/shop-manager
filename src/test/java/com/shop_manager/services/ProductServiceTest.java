package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Product;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.storage_engine.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ProductServiceTest {

    private ProductService productService;
    private InMemoryDatabase database;

    @BeforeEach
    void setUp() {
        productService = ProductService.getInstance();
        database = InMemoryDatabase.getInstance();
        database.clearAll();
    }

    @AfterEach
    void tearDown() {
        database.clearAll();
    }

    @Test
    void testAddProduct_Success() throws AlreadyExistsException, ConstraintViolationException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        productService.addProduct(product);

        assertNotNull(product.getId());
        assertEquals(1L, product.getId());
    }

    @Test
    void testAddProduct_WithExplicitId() throws AlreadyExistsException, ConstraintViolationException {
        Product product = new Product(
            10L,
            "Laptop",
            new BigDecimal("1200.00"),
            LocalDate.now().plusYears(2),
            ProductCategory.NON_FOOD
        );

        productService.addProduct(product);

        assertEquals(10L, product.getId());
    }

    @Test
    void testAddProduct_ThrowsAlreadyExistsException() throws AlreadyExistsException, ConstraintViolationException {
        Product product1 = new Product(
            1L,
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        Product product2 = new Product(
            1L,
            "Orange",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        productService.addProduct(product1);

        assertThrows(AlreadyExistsException.class, () -> productService.addProduct(product2));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_DuplicateName() throws AlreadyExistsException, ConstraintViolationException {
        Product product1 = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        Product product2 = new Product(
            "Apple",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        productService.addProduct(product1);

        assertThrows(ConstraintViolationException.class, () -> productService.addProduct(product2));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_NullName() {
        Product product = new Product(
            null,
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productService.addProduct(product));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_EmptyName() {
        Product product = new Product(
            "",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productService.addProduct(product));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_NegativePrice() {
        Product product = new Product(
            "Apple",
            new BigDecimal("-1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productService.addProduct(product));
    }

    @Test
    void testGetProductById_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productService.addProduct(product);
        Long productId = product.getId();

        Product retrieved = productService.getProductById(productId);

        assertNotNull(retrieved);
        assertEquals(productId, retrieved.getId());
        assertEquals("Apple", retrieved.getName());
        assertEquals(new BigDecimal("1.50"), retrieved.getDeliveryPrice());
        assertEquals(ProductCategory.FOOD, retrieved.getCategory());
    }

    @Test
    void testGetProductById_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    void testGetAllProducts_Empty() {
        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetAllProducts_Multiple() throws AlreadyExistsException, ConstraintViolationException {
        Product product1 = new Product("Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);
        Product product2 = new Product("Laptop", new BigDecimal("1200.00"), LocalDate.now().plusYears(2), ProductCategory.NON_FOOD);
        Product product3 = new Product("Banana", new BigDecimal("0.80"), LocalDate.now().plusDays(15), ProductCategory.FOOD);

        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(3, products.size());
    }

    @Test
    void testUpdateProduct_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productService.addProduct(product);
        Long productId = product.getId();

        Product updatedProduct = new Product(
            productId,
            "Apple",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(45),
            ProductCategory.FOOD
        );
        productService.updateProduct(updatedProduct);

        Product retrieved = productService.getProductById(productId);
        assertEquals(new BigDecimal("2.00"), retrieved.getDeliveryPrice());
    }

    @Test
    void testUpdateProduct_ThrowsNotFoundException() {
        Product product = new Product(
            999L,
            "Nonexistent",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(NotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    void testUpdateProduct_ThrowsConstraintViolationException() throws AlreadyExistsException, ConstraintViolationException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productService.addProduct(product);
        Long productId = product.getId();

        Product invalidUpdate = new Product(
            productId,
            "",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        assertThrows(ConstraintViolationException.class, () -> productService.updateProduct(invalidUpdate));
    }

    @Test
    void testDeleteProduct_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productService.addProduct(product);
        Long productId = product.getId();

        productService.deleteProduct(productId);

        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
        assertEquals(0, productService.getAllProducts().size());
    }

    @Test
    void testDeleteProduct_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(999L));
    }
}

