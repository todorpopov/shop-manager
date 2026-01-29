package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Cashier;
import com.shop_manager.repositories.CashierRepository;

import java.util.List;

public class CashierService {
    private static CashierService instance;

    private final CashierRepository cashierRepository = CashierRepository.getInstance();

    private CashierService() {}

    public static CashierService getInstance() {
        if(instance == null) {
            instance = new CashierService();
        }
        return instance;
    }

    public void addCashier(Cashier cashier) throws AlreadyExistsException, ConstraintViolationException {
        cashierRepository.addCashier(cashier);
    }

    public Cashier getCashierById(Long id) throws NotFoundException {
        return cashierRepository.getCashierById(id);
    }

    public List<Cashier> getAllCashiers() {
        return cashierRepository.getAllCashiers();
    }

    public void updateCashier(Cashier cashier) throws NotFoundException, ConstraintViolationException {
        cashierRepository.updateCashier(cashier);
    }

    public void deleteCashier(Long id) throws NotFoundException {
        cashierRepository.deleteCashier(id);
    }
}
