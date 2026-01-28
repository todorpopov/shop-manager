package com.shop_manager.repositories;

import com.shop_manager.models.Store;
import com.shop_manager.storage_engine.InMemoryDatabase;
import com.shop_manager.storage_engine.InMemoryTable;

import java.util.List;

public class StoreRepository {
    private static StoreRepository instance;

    private final InMemoryTable<Store> stores = InMemoryDatabase.getInstance().stores();

    private StoreRepository() {}

    public static StoreRepository getInstance(){
        if(instance == null){
            instance = new StoreRepository();
        }
        return instance;
    }

    public void addStore(Store store){
        stores.insert(store);
    }

    public Store getStoreById(Long id){
        return stores.get(id);
    }

    public List<Store> getAllStores(){
        return stores.all();
    }

    public void updateStore(Store store){
        stores.update(store);
    }

    public void deleteStore(Long id){
        stores.delete(id);
    }
}
