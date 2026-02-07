package com.shop_manager;

import com.shop_manager.ui.ScreenManager;

public class Main
{
    public static void main(String[] args) {
        ScreenManager screenManager = ScreenManager.getInstance();
        screenManager.startUi();
    }
}
