package com.shop_manager.repositories;

import com.shop_manager.models.Cashier;
import com.shop_manager.storage_engine.InMemoryTable;
import com.shop_manager.storage_engine.InMemoryDatabase;

import java.util.List;

public class CashierRepository {
    private static CashierRepository instance;

    private final InMemoryTable<Cashier> cashiers = InMemoryDatabase.getInstance().cashiers();

    private CashierRepository() {}

    public static CashierRepository getInstance(){
        if(instance == null){
            instance = new CashierRepository();
        }
        return instance;
    }

    public void addCashier(Cashier cashier) {
        cashiers.insert(cashier);
    }

    public Cashier getCashierById(Long id) {
        return cashiers.get(id);
    }

    public List<Cashier> getAllCashiers() {
        return cashiers.all();
    }

    public void updateCashier(Cashier cashier) {
        cashiers.update(cashier);
    }

    public void deleteCashier(long id) {
        cashiers.delete(id);
    }
}
