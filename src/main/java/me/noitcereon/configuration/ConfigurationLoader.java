package me.noitcereon.configuration;

import java.util.Optional;

public interface ConfigurationLoader {
    Optional<String> getProperty(String key);

    /**
     * Fetches the latest configuration properties and caches them.
     */
    void forceRefreshCache();
    String getApiKey();
    String getDataAccessToken();
}
