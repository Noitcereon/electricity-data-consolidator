package me.noitcereon.configuration;

/**
 * To prevent typos when retrieving configuration properties this class holds a static final reference you can use.
 */
public class ConfigurationKeys {
    private ConfigurationKeys() {
        // Prevent instantiation.
    }
    public static final String API_KEY = "api-key";
    public static final String DATA_ACCESS_TOKEN = "data-access-token";
    public static final String LAST_DATA_ACCESS_REFRESH = "last-data-access-refresh";
}
