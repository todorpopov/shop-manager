package com.shop_manager.ui;

import com.shop_manager.ui.enums.ScreenName;
import com.shop_manager.ui.screens.MainScreen;
import com.shop_manager.ui.screens.ManageScreen;

import java.util.HashMap;
import java.util.Scanner;

public class ScreenManager {
    private static ScreenManager instance;
    private Screen currentScreen;
    private final Scanner scanner;
    private boolean running;
    private final HashMap<ScreenName, Screen> screens = new HashMap<>();;

    private ScreenManager() {
        this.scanner = new Scanner(System.in);
        this.running = true;
        initializeScreens();
    }

    public static synchronized ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void startUi() {
        currentScreen = screens.get(ScreenName.MAIN);

        while (running && currentScreen != null) {
            clearScreen();
            currentScreen.display();
            Screen nextScreen = currentScreen.handleInput();

            if (nextScreen != null) {
                currentScreen = nextScreen;
            }
        }

        shutdown();
    }

    public void stop() {
        running = false;
    }

    public String nextLine() {
        return scanner.nextLine().trim();
    }

    public Screen goToScreen(ScreenName screenName) {
        currentScreen = screens.get(screenName);
        return currentScreen;
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void shutdown() {
        if (scanner != null) {
            scanner.close();
        }
    }

    private void initializeScreens() {
        screens.put(ScreenName.MAIN, new MainScreen(this));
        screens.put(ScreenName.MANAGE, new ManageScreen(this));
    }
}


