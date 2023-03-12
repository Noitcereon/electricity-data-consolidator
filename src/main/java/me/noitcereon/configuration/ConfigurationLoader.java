package me.noitcereon.configuration;

import java.util.Optional;

public interface ConfigurationLoader {
    Optional<String> getProperty(String key);
    String getApiKey();
    String getDataAccessToken();
}
