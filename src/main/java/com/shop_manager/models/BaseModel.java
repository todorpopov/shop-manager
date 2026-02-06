package com.shop_manager.models;

import com.shop_manager.models.interfaces.Identifiable;
import com.shop_manager.storage_engine.annotations.NotNull;
import com.shop_manager.storage_engine.annotations.Unique;

public class BaseModel implements Identifiable {
    @NotNull
    @Unique
    Long id;

    public BaseModel(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
