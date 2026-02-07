package com.shop_manager.services;

import com.shop_manager.models.Cashier;
import com.shop_manager.models.Product;
import com.shop_manager.models.Receipt;
import com.shop_manager.models.ReceiptItem;
import com.shop_manager.models.enums.ProductCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptLoaderServiceTest {
    private static final Path RECEIPTS_DIR = Path.of("receipts");

    private ReceiptLoaderService receiptLoaderService;

    @BeforeEach
    void setUp() throws IOException {
        receiptLoaderService = ReceiptLoaderService.getInstance();
        this.receiptLoaderService.clearReceiptsDirectory();
    }

    @AfterEach
    void tearDown() throws IOException {
        this.receiptLoaderService.clearReceiptsDirectory();
    }

    @Test
    void testSaveAndLoadReceipts() throws IOException {
        Receipt receipt1 = buildReceipt(1L, "John Doe", "Apple", ProductCategory.FOOD, new BigDecimal("10.00"));
        Receipt receipt2 = buildReceipt(2L, "Jane Smith", "Laptop", ProductCategory.NON_FOOD, new BigDecimal("1200.00"));

        receiptLoaderService.saveReceipt(receipt1);
        receiptLoaderService.saveReceipt(receipt2);

        List<Receipt> loadedReceipts = receiptLoaderService.loadReceipts();

        assertEquals(2, loadedReceipts.size());
        Receipt loaded1 = loadedReceipts.get(0);
        Receipt loaded2 = loadedReceipts.get(1);

        assertEquals(1L, loaded1.getId());
        assertEquals("John Doe", loaded1.getCashier().getName());
        assertEquals(ProductCategory.FOOD, loaded1.getItems().get(0).getProduct().getCategory());
        assertEquals(new BigDecimal("10.00"), loaded1.getTotalAmount());

        assertEquals(2L, loaded2.getId());
        assertEquals("Jane Smith", loaded2.getCashier().getName());
        assertEquals(ProductCategory.NON_FOOD, loaded2.getItems().getFirst().getProduct().getCategory());
        assertEquals(new BigDecimal("1200.00"), loaded2.getTotalAmount());
    }

    @Test
    void testLoadReceipts_WhenDirectoryMissing() throws IOException {
        this.receiptLoaderService.clearReceiptsDirectory();
        if (Files.exists(RECEIPTS_DIR)) {
            Files.delete(RECEIPTS_DIR);
        }

        List<Receipt> loadedReceipts = receiptLoaderService.loadReceipts();

        assertNotNull(loadedReceipts);
        assertTrue(loadedReceipts.isEmpty());
    }

    private Receipt buildReceipt(
        Long receiptId,
        String cashierName,
        String productName,
        ProductCategory category,
        BigDecimal totalAmount
    ) {
        Cashier cashier = new Cashier(100L + receiptId, cashierName, new BigDecimal("2500.00"));
        Product product = new Product(
            200L + receiptId,
            productName,
            new BigDecimal("1.50"),
            LocalDate.of(2026, 3, 1),
            category
        );

        List<ReceiptItem> items = new ArrayList<>();
        items.add(new ReceiptItem(product, 5, new BigDecimal("2.00")));

        return new Receipt(
            receiptId,
            cashier,
            LocalDateTime.now(),
            items,
            totalAmount
        );
    }
}
