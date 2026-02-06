package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Store;
import com.shop_manager.storage_engine.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class StoreServiceTest {

    private StoreService storeService;
    private InMemoryDatabase database;

    @BeforeEach
    void setUp() {
        storeService = StoreService.getInstance();
        database = InMemoryDatabase.getInstance();
        database.clearAll();
    }

    @AfterEach
    void tearDown() {
        database.clearAll();
    }

    @Test
    void testAddStore_Success() throws AlreadyExistsException, ConstraintViolationException {
        Store store = new Store(
            "Main Store",
            20.0,
            15.0,
            5,
            10.0
        );

        storeService.addStore(store);

        assertNotNull(store.getId());
        assertEquals(1L, store.getId());
    }

    @Test
    void testAddStore_WithExplicitId() throws AlreadyExistsException, ConstraintViolationException {
        Store store = new Store(
            10L,
            "Branch Store",
            25.0,
            18.0,
            7,
            12.0
        );

        storeService.addStore(store);

        assertEquals(10L, store.getId());
    }

    @Test
    void testAddStore_ThrowsAlreadyExistsException() throws AlreadyExistsException, ConstraintViolationException {
        Store store1 = new Store(1L, "Main Store", 20.0, 15.0, 5, 10.0);
        Store store2 = new Store(1L, "Branch Store", 25.0, 18.0, 7, 12.0);

        storeService.addStore(store1);

        assertThrows(AlreadyExistsException.class, () -> storeService.addStore(store2));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_DuplicateName() throws AlreadyExistsException, ConstraintViolationException {
        Store store1 = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        Store store2 = new Store("Main Store", 25.0, 18.0, 7, 12.0);

        storeService.addStore(store1);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store2));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_NullName() {
        Store store = new Store(null, 20.0, 15.0, 5, 10.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_EmptyName() {
        Store store = new Store("", 20.0, 15.0, 5, 10.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_NegativeFoodMarkup() {
        Store store = new Store("Main Store", -5.0, 15.0, 5, 10.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_NegativeNonFoodMarkup() {
        Store store = new Store("Main Store", 20.0, -10.0, 5, 10.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_NegativeDaysBeforeExpiration() {
        Store store = new Store("Main Store", 20.0, 15.0, -3, 10.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testAddStore_ThrowsConstraintViolationException_NegativeDiscount() {
        Store store = new Store("Main Store", 20.0, 15.0, 5, -5.0);

        assertThrows(ConstraintViolationException.class, () -> storeService.addStore(store));
    }

    @Test
    void testGetStoreById_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        storeService.addStore(store);
        Long storeId = store.getId();

        Store retrieved = storeService.getStoreById(storeId);

        assertNotNull(retrieved);
        assertEquals(storeId, retrieved.getId());
        assertEquals("Main Store", retrieved.getName());
        assertEquals(20.0, retrieved.getFoodMarkupPercent());
        assertEquals(15.0, retrieved.getNonFoodMarkupPercent());
    }

    @Test
    void testGetStoreById_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> storeService.getStoreById(999L));
    }

    @Test
    void testGetAllStores_Empty() {
        List<Store> stores = storeService.getAllStores();

        assertNotNull(stores);
        assertTrue(stores.isEmpty());
    }

    @Test
    void testGetAllStores_Multiple() throws AlreadyExistsException, ConstraintViolationException {
        Store store1 = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        Store store2 = new Store("Branch Store", 25.0, 18.0, 7, 12.0);
        Store store3 = new Store("Downtown Store", 22.0, 16.0, 6, 11.0);

        storeService.addStore(store1);
        storeService.addStore(store2);
        storeService.addStore(store3);

        List<Store> stores = storeService.getAllStores();

        assertNotNull(stores);
        assertEquals(3, stores.size());
    }

    @Test
    void testUpdateStore_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        storeService.addStore(store);
        Long storeId = store.getId();

        Store updatedStore = new Store(storeId, "Main Store", 25.0, 20.0, 7, 15.0);
        storeService.updateStore(updatedStore);

        Store retrieved = storeService.getStoreById(storeId);
        assertEquals(25.0, retrieved.getFoodMarkupPercent());
        assertEquals(20.0, retrieved.getNonFoodMarkupPercent());
    }

    @Test
    void testUpdateStore_ThrowsNotFoundException() {
        Store store = new Store(999L, "Nonexistent", 20.0, 15.0, 5, 10.0);

        assertThrows(NotFoundException.class, () -> storeService.updateStore(store));
    }

    @Test
    void testUpdateStore_ThrowsConstraintViolationException() throws AlreadyExistsException, ConstraintViolationException {
        Store store = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        storeService.addStore(store);
        Long storeId = store.getId();

        Store invalidUpdate = new Store(storeId, "", 25.0, 20.0, 7, 15.0);
        assertThrows(ConstraintViolationException.class, () -> storeService.updateStore(invalidUpdate));
    }

    @Test
    void testDeleteStore_Success() throws AlreadyExistsException, ConstraintViolationException, NotFoundException {
        Store store = new Store("Main Store", 20.0, 15.0, 5, 10.0);
        storeService.addStore(store);
        Long storeId = store.getId();

        storeService.deleteStore(storeId);

        assertThrows(NotFoundException.class, () -> storeService.getStoreById(storeId));
        assertEquals(0, storeService.getAllStores().size());
    }

    @Test
    void testDeleteStore_ThrowsNotFoundException() {
        assertThrows(NotFoundException.class, () -> storeService.deleteStore(999L));
    }
}

