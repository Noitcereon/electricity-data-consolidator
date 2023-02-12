package me.noitcereon;

import me.noitcereon.configuration.ConfigLoader;

public class Main {
    public static void main(String[] args) {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        String apiKey = configLoader.getProperty("eloverblik-api-key");
        System.out.println(apiKey);
    }
}