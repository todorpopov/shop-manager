package com.shop_manager.repositories;

import com.shop_manager.models.Receipt;
import com.shop_manager.storage_engine.InMemoryDatabase;
import com.shop_manager.storage_engine.InMemoryTable;

import java.util.List;

public class ReceiptRepository {
    private static ReceiptRepository instance;

    private final InMemoryTable<Receipt> receipts = InMemoryDatabase.getInstance().receipts();

    private ReceiptRepository() {}

    public static ReceiptRepository getInstance() {
        if (instance == null) {
            instance = new ReceiptRepository();
        }
        return instance;
    }

    public void addReceipt(Receipt receipt) {
        receipts.insert(receipt);
    }

    public Receipt getReceiptById(Long id) {
        return receipts.get(id);
    }

    public List<Receipt> getAllReceipts() {
        return receipts.all();
    }

    public void updateReceipt(Receipt receipt) {
        receipts.update(receipt);
    }

    public void deleteReceipt(Long id) {
        receipts.delete(id);
    }
}
