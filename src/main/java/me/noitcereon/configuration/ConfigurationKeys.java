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

    /**
     * Configuration to keep track of the latest date meter data was fetched, so
     * it is easy to only fetch data in the period that has been missed.
     */
    public static final String LATEST_METER_DATA_FETCH_DATE = "latest-meter-data-fetch-date";
}
