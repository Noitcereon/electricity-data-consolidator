package me.noitcereon.console.ui;

import java.util.HashMap;
import java.util.Map;

public class ScreenFactory {
    private ScreenFactory() {
        // Don't instantiate static helper class.
    }

    public static Screen createMainMenu() {
        ScreenOptionFactory optionFactory = new ScreenOptionFactory();
        return createMainMenu(optionFactory);
    }

    public static Screen createMainMenu(ScreenOptionFactory optionFactory) {
        Map<Integer, ScreenOption> options = new HashMap<>();

        options.put(1, optionFactory.fetchMeterData());
        options.put(2, optionFactory.fetchMeterDataCustomPeriod());
        options.put(3, optionFactory.fetchMeterDataBasedOnLastFetchTime());
        options.put(4, optionFactory.toggleMeterDataFormat());

        return new Screen("Electricity Data Consolidator App", options);
    }

    public static Screen resultScreen(String content) {
        return resultScreen(new ScreenOptionFactory(), content);
    }

    public static Screen resultScreen(ScreenOptionFactory screenOptions, String content) {
        HashMap<Integer, ScreenOption> options = new HashMap<>();
        options.put(1, screenOptions.mainMenuOption());
        return new Screen("Result Screen", content, options);
    }
}
