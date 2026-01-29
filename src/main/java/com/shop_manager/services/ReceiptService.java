package com.shop_manager.services;

import com.shop_manager.exceptions.AlreadyExistsException;
import com.shop_manager.exceptions.ConstraintViolationException;
import com.shop_manager.exceptions.NotFoundException;
import com.shop_manager.models.Receipt;
import com.shop_manager.repositories.ReceiptRepository;

import java.util.List;

public class ReceiptService {
    private static ReceiptService instance;

    private final ReceiptRepository receiptRepository = ReceiptRepository.getInstance();

    private ReceiptService() {}

    public static ReceiptService getInstance() {
        if(instance == null) {
            instance = new ReceiptService();
        }
        return instance;
    }

    public void addReceipt(Receipt receipt) throws AlreadyExistsException, ConstraintViolationException {
        receiptRepository.addReceipt(receipt);
    }

    public Receipt getReceiptById(Long id) throws NotFoundException {
        return receiptRepository.getReceiptById(id);
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepository.getAllReceipts();
    }

    public void updateReceipt(Receipt receipt) throws NotFoundException, ConstraintViolationException {
        receiptRepository.updateReceipt(receipt);
    }

    public void deleteReceipt(Long id) throws NotFoundException {
        receiptRepository.deleteReceipt(id);
    }
}
