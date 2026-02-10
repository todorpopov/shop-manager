package com.shop_manager.repositories;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.models.Store;
import com.shop_manager.models.enums.ProductCategory;
import com.shop_manager.storage_engine.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ReceiptRepositoryTest {

    private ReceiptRepository receiptRepository;
    private InMemoryDatabase database;

    @BeforeEach
    void setUp() {
        receiptRepository = ReceiptRepository.getInstance();
        database = InMemoryDatabase.getInstance();
        database.clearAll();
    }

    @AfterEach
    void tearDown() {
        database.clearAll();
    }

    private Store createStore() {
        return new Store(1L, "Test Store", 10.0, 8.0, 3, 5.0);
    }

    @Test
    void testAddReceipt_Success() throws AlreadyExistsException, ConstraintViolationException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(
            store,
            cashier,
            LocalDateTime.now(),
            items,
            new BigDecimal("10.00")
        );

        receiptRepository.addReceipt(receipt);

        assertNotNull(receipt.getId());
        assertEquals(1L, receipt.getId());
    }

    @Test
    void testAddReceipt_WithExplicitId() throws AlreadyExistsException, ConstraintViolationException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(
            10L,
            store,
            cashier,
            LocalDateTime.now(),
            items,
            new BigDecimal("10.00")
        );

        receiptRepository.addReceipt(receipt);

        assertEquals(10L, receipt.getId());
    }

    @Test
    void testAddReceipt_ThrowsAlreadyExistsException() throws AlreadyExistsException, ConstraintViolationException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt1 = new Receipt(1L, store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));
        Receipt receipt2 = new Receipt(1L, store, cashier, LocalDateTime.now(), items, new BigDecimal("15.00"));

        receiptRepository.addReceipt(receipt1);

        assertThrows(AlreadyExistsException.class, () -> receiptRepository.addReceipt(receipt2));
    }

    @Test
    void testAddReceipt_ThrowsConstraintViolationException_NullCashier() {
        Store store = createStore();
        List<ReceiptItem> items = new ArrayList<>();
        Receipt receipt = new Receipt(
            store,
            null,
            LocalDateTime.now(),
            items,
            new BigDecimal("10.00")
        );

        assertThrows(ConstraintViolationException.class, () -> receiptRepository.addReceipt(receipt));
    }

    @Test
    void testAddReceipt_ThrowsConstraintViolationException_NullItems() {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Receipt receipt = new Receipt(
            store,
            cashier,
            LocalDateTime.now(),
            null,
            new BigDecimal("10.00")
        );

        assertThrows(ConstraintViolationException.class, () -> receiptRepository.addReceipt(receipt));
    }

    @Test
    void testAddReceipt_ThrowsConstraintViolationException_NegativeTotalAmount() {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        List<ReceiptItem> items = new ArrayList<>();
        Receipt receipt = new Receipt(
            store,
            cashier,
            LocalDateTime.now(),
            items,
            new BigDecimal("-10.00")
        );

        assertThrows(ConstraintViolationException.class, () -> receiptRepository.addReceipt(receipt));
    }

    @Test
    void testGetReceiptById_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));
        receiptRepository.addReceipt(receipt);
        Long receiptId = receipt.getId();

        Receipt retrieved = receiptRepository.getReceiptById(receiptId);

        assertNotNull(retrieved);
        assertEquals(receiptId, retrieved.getId());
        assertEquals(cashier.getId(), retrieved.getCashier().getId());
        assertEquals(new BigDecimal("10.00"), retrieved.getTotalAmount());
        assertEquals(1, retrieved.getItems().size());
    }

    @Test
    void testGetReceiptById_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> receiptRepository.getReceiptById(999L));
    }

    @Test
    void testGetAllReceipts_Empty() {
        List<Receipt> receipts = receiptRepository.getAllReceipts();

        assertNotNull(receipts);
        assertTrue(receipts.isEmpty());
    }

    @Test
    void testGetAllReceipts_Multiple() throws AlreadyExistsException, ConstraintViolationException {
        Store store = createStore();
        Cashier cashier1 = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Cashier cashier2 = new Cashier(2L, "Jane Smith", new BigDecimal("3000.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items1 = new ArrayList<>();
        items1.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        List<ReceiptItem> items2 = new ArrayList<>();
        items2.add(new ReceiptItem(product, 3, new BigDecimal("2.00")));

        List<ReceiptItem> items3 = new ArrayList<>();
        items3.add(new ReceiptItem(product, 10, new BigDecimal("2.00")));

        Receipt receipt1 = new Receipt(store, cashier1, LocalDateTime.now(), items1, new BigDecimal("10.00"));
        Receipt receipt2 = new Receipt(store, cashier2, LocalDateTime.now(), items2, new BigDecimal("6.00"));
        Receipt receipt3 = new Receipt(store, cashier1, LocalDateTime.now(), items3, new BigDecimal("20.00"));

        receiptRepository.addReceipt(receipt1);
        receiptRepository.addReceipt(receipt2);
        receiptRepository.addReceipt(receipt3);

        List<Receipt> receipts = receiptRepository.getAllReceipts();

        assertNotNull(receipts);
        assertEquals(3, receipts.size());
    }

    @Test
    void testUpdateReceipt_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));
        receiptRepository.addReceipt(receipt);
        Long receiptId = receipt.getId();

        List<ReceiptItem> updatedItems = new ArrayList<>();
        updatedItems.add(new ReceiptItem(product, 10, new BigDecimal("2.00")));

        Receipt updatedReceipt = new Receipt(
            receiptId,
            store,
            cashier,
            LocalDateTime.now(),
            updatedItems,
            new BigDecimal("20.00")
        );
        receiptRepository.updateReceipt(updatedReceipt);

        Receipt retrieved = receiptRepository.getReceiptById(receiptId);
        assertEquals(new BigDecimal("20.00"), retrieved.getTotalAmount());
    }

    @Test
    void testUpdateReceipt_ThrowsNotFoundException() {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        List<ReceiptItem> items = new ArrayList<>();
        Receipt receipt = new Receipt(999L, store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));

        assertThrows(NotFoundException.class, () -> receiptRepository.updateReceipt(receipt));
    }

    @Test
    void testUpdateReceipt_ThrowsConstraintViolationException() throws AlreadyExistsException, ConstraintViolationException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));
        receiptRepository.addReceipt(receipt);
        Long receiptId = receipt.getId();

        Receipt invalidUpdate = new Receipt(receiptId, store, cashier, LocalDateTime.now(), items, new BigDecimal("-5.00"));
        assertThrows(ConstraintViolationException.class, () -> receiptRepository.updateReceipt(invalidUpdate));
    }

    @Test
    void testDeleteReceipt_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = createStore();
        Cashier cashier = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Product product = new Product(1L, "Apple", new BigDecimal("1.50"), LocalDate.now().plusDays(30), ProductCategory.FOOD);

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        Receipt receipt = new Receipt(store, cashier, LocalDateTime.now(), items, new BigDecimal("10.00"));
        receiptRepository.addReceipt(receipt);
        Long receiptId = receipt.getId();

        receiptRepository.deleteReceipt(receiptId);

        assertThrows(NotFoundException.class, () -> receiptRepository.getReceiptById(receiptId));
        assertEquals(0, receiptRepository.getAllReceipts().size());
    }

    @Test
    void testDeleteReceipt_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> receiptRepository.deleteReceipt(999L));
    }
}
