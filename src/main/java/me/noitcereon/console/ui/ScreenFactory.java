package me.noitcereon.console.ui;

import java.util.HashMap;
import java.util.Map;

public class ScreenFactory {
    public static Screen createMainMenu() {
        Map<Integer, ScreenOption> options = new HashMap<>();

        options.put(1, ScreenOptionFactory.fetchMeterData());
        options.put(2, ScreenOptionFactory.fetchMeterDataCustomPeriod());

        return new Screen("Electricity Data Consolidator App", options);
    }
    public static Screen resultScreen(String content){
        HashMap<Integer, ScreenOption> options = new HashMap<>();
        options.put(1, ScreenOptionFactory.mainMenuOption());
        return new Screen("Result Screen", content, options);
    }
}
