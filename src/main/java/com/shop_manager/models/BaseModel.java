package com.shop_manager.models;

import com.shop_manager.models.interfaces.Identifiable;

public class BaseModel implements Identifiable {
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
