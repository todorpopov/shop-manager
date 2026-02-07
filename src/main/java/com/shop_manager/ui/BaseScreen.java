package com.shop_manager.ui;

import com.shop_manager.exceptions.NotImplementedException;

public class BaseScreen implements Screen {
    public final ScreenManager screenManager;

    public BaseScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void display() {
        throw new NotImplementedException("Display method not implemented for this screen");
    }

    @Override
    public Screen handleInput() {
        throw new NotImplementedException("Handle input method not implemented for this screen");
    }
}
