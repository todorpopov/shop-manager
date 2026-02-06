package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.storage_engine.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CashierServiceTest {

    private CashierService cashierService;
    private InMemoryDatabase database;

    @BeforeEach
    void setUp() {
        cashierService = CashierService.getInstance();
        database = InMemoryDatabase.getInstance();
        database.clearAll();
    }

    @AfterEach
    void tearDown() {
        database.clearAll();
    }

    @Test
    void testAddCashier_Success() throws AlreadyExistsException, ConstraintViolationException {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("2500.00"));

        cashierService.addCashier(cashier);

        assertNotNull(cashier.getId());
        assertEquals(1L, cashier.getId());
    }

    @Test
    void testAddCashier_WithExplicitId() throws AlreadyExistsException, ConstraintViolationException {
        Cashier cashier = new Cashier(10L, "Jane Smith", new BigDecimal("3000.00"));

        cashierService.addCashier(cashier);

        assertEquals(10L, cashier.getId());
    }

    @Test
    void testAddCashier_ThrowsAlreadyExistsException() throws AlreadyExistsException, ConstraintViolationException {
        Cashier cashier1 = new Cashier(1L, "John Doe", new BigDecimal("2500.00"));
        Cashier cashier2 = new Cashier(1L, "Jane Smith", new BigDecimal("3000.00"));

        cashierService.addCashier(cashier1);

        assertThrows(AlreadyExistsException.class, () -> cashierService.addCashier(cashier2));
    }

    @Test
    void testAddCashier_ThrowsConstraintViolationException_NullName() {
        Cashier cashier = new Cashier(null, new BigDecimal("2500.00"));

        assertThrows(ConstraintViolationException.class, () -> cashierService.addCashier(cashier));
    }

    @Test
    void testAddCashier_ThrowsConstraintViolationException_EmptyName() {
        Cashier cashier = new Cashier("", new BigDecimal("2500.00"));

        assertThrows(ConstraintViolationException.class, () -> cashierService.addCashier(cashier));
    }

    @Test
    void testAddCashier_ThrowsConstraintViolationException_NegativeSalary() {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("-100.00"));

        assertThrows(ConstraintViolationException.class, () -> cashierService.addCashier(cashier));
    }

    @Test
    void testGetCashierById_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("2500.00"));
        cashierService.addCashier(cashier);
        Long cashierId = cashier.getId();

        Cashier retrieved = cashierService.getCashierById(cashierId);

        assertNotNull(retrieved);
        assertEquals(cashierId, retrieved.getId());
        assertEquals("John Doe", retrieved.getName());
        assertEquals(new BigDecimal("2500.00"), retrieved.getMonthlySalary());
    }

    @Test
    void testGetCashierById_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> cashierService.getCashierById(999L));
    }

    @Test
    void testGetAllCashiers_Empty() {
        List<Cashier> cashiers = cashierService.getAllCashiers();

        assertNotNull(cashiers);
        assertTrue(cashiers.isEmpty());
    }

    @Test
    void testGetAllCashiers_Multiple() throws AlreadyExistsException, ConstraintViolationException {
        Cashier cashier1 = new Cashier("John Doe", new BigDecimal("2500.00"));
        Cashier cashier2 = new Cashier("Jane Smith", new BigDecimal("3000.00"));
        Cashier cashier3 = new Cashier("Bob Johnson", new BigDecimal("2800.00"));

        cashierService.addCashier(cashier1);
        cashierService.addCashier(cashier2);
        cashierService.addCashier(cashier3);

        List<Cashier> cashiers = cashierService.getAllCashiers();

        assertNotNull(cashiers);
        assertEquals(3, cashiers.size());
    }

    @Test
    void testUpdateCashier_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("2500.00"));
        cashierService.addCashier(cashier);
        Long cashierId = cashier.getId();

        Cashier updatedCashier = new Cashier(cashierId, "John Doe Updated", new BigDecimal("3500.00"));
        cashierService.updateCashier(updatedCashier);

        Cashier retrieved = cashierService.getCashierById(cashierId);
        assertEquals("John Doe Updated", retrieved.getName());
        assertEquals(new BigDecimal("3500.00"), retrieved.getMonthlySalary());
    }

    @Test
    void testUpdateCashier_ThrowsNotFoundException() {
        Cashier cashier = new Cashier(999L, "Nonexistent", new BigDecimal("2500.00"));

        assertThrows(NotFoundException.class, () -> cashierService.updateCashier(cashier));
    }

    @Test
    void testUpdateCashier_ThrowsConstraintViolationException() throws AlreadyExistsException, ConstraintViolationException {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("2500.00"));
        cashierService.addCashier(cashier);
        Long cashierId = cashier.getId();

        Cashier invalidUpdate = new Cashier(cashierId, "", new BigDecimal("3000.00"));
        assertThrows(ConstraintViolationException.class, () -> cashierService.updateCashier(invalidUpdate));
    }

    @Test
    void testDeleteCashier_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Cashier cashier = new Cashier("John Doe", new BigDecimal("2500.00"));
        cashierService.addCashier(cashier);
        Long cashierId = cashier.getId();

        cashierService.deleteCashier(cashierId);

        assertThrows(NotFoundException.class, () -> cashierService.getCashierById(cashierId));
        assertEquals(0, cashierService.getAllCashiers().size());
    }

    @Test
    void testDeleteCashier_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> cashierService.deleteCashier(999L));
    }
}

