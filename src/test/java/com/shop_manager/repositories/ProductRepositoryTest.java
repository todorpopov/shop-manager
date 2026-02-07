package com.shop_manager.repositories;

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


class ProductRepositoryTest {

    private ProductRepository productRepository;
    private InMemoryDatabase database;

    @BeforeEach
    void setUp() {
        productRepository = ProductRepository.getInstance();
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

        productRepository.addProduct(product);

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

        productRepository.addProduct(product);

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

        productRepository.addProduct(product1);

        assertThrows(AlreadyExistsException.class, () -> productRepository.addProduct(product2));
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

        productRepository.addProduct(product1);

        assertThrows(ConstraintViolationException.class, () -> productRepository.addProduct(product2));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_NullName() {
        Product product = new Product(
            null,
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productRepository.addProduct(product));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_EmptyName() {
        Product product = new Product(
            "",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productRepository.addProduct(product));
    }

    @Test
    void testAddProduct_ThrowsConstraintViolationException_NegativePrice() {
        Product product = new Product(
            "Apple",
            new BigDecimal("-1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );

        assertThrows(ConstraintViolationException.class, () -> productRepository.addProduct(product));
    }

    @Test
    void testGetProductById_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productRepository.addProduct(product);
        Long productId = product.getId();

        Product retrieved = productRepository.getProductById(productId);

        assertNotNull(retrieved);
        assertEquals(productId, retrieved.getId());
        assertEquals("Apple", retrieved.getName());
        assertEquals(new BigDecimal("1.50"), retrieved.getDeliveryPrice());
        assertEquals(ProductCategory.FOOD, retrieved.getCategory());
    }

    @Test
    void testGetProductById_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> productRepository.getProductById(999L));
    }

    @Test
    void testGetAllProducts_Empty() {
        List<Product> products = productRepository.getAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void testGetAllProducts_Multiple() throws AlreadyExistsException, ConstraintViolationException {
        Product product1 = new Product("Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);
        Product product2 = new Product("Laptop", new BigDecimal("1200.00"), LocalDate.now().plusYears(2), ProductCategory.NON_FOOD);
        Product product3 = new Product("Banana", new BigDecimal("0.80"), LocalDate.now().plusDays(15), ProductCategory.FOOD);

        productRepository.addProduct(product1);
        productRepository.addProduct(product2);
        productRepository.addProduct(product3);

        List<Product> products = productRepository.getAllProducts();

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
        productRepository.addProduct(product);
        Long productId = product.getId();

        Product updatedProduct = new Product(
            productId,
            "Apple",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(45),
            ProductCategory.FOOD
        );
        productRepository.updateProduct(updatedProduct);

        Product retrieved = productRepository.getProductById(productId);
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

        assertThrows(NotFoundException.class, () -> productRepository.updateProduct(product));
    }

    @Test
    void testUpdateProduct_ThrowsConstraintViolationException() throws AlreadyExistsException, ConstraintViolationException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productRepository.addProduct(product);
        Long productId = product.getId();

        Product invalidUpdate = new Product(
            productId,
            "",
            new BigDecimal("2.00"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        assertThrows(ConstraintViolationException.class, () -> productRepository.updateProduct(invalidUpdate));
    }

    @Test
    void testDeleteProduct_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Product product = new Product(
            "Apple",
            new BigDecimal("1.50"),
            LocalDate.now().plusDays(30),
            ProductCategory.FOOD
        );
        productRepository.addProduct(product);
        Long productId = product.getId();

        productRepository.deleteProduct(productId);

        assertThrows(NotFoundException.class, () -> productRepository.getProductById(productId));
        assertEquals(0, productRepository.getAllProducts().size());
    }

    @Test
    void testDeleteProduct_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> productRepository.deleteProduct(999L));
    }
}
