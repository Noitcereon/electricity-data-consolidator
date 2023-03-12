package me.noitcereon.configuration;

import java.util.Map;

public interface ConfigurationSaver {
    /**
     * Adds a new value to an existing key or creates a key with the given value in a configuration file.
     *
     * @param key   The String value representing the key used to get the value.
     * @param value A String value representing the value associated with the key.
     * @return A message indicating success or failure to save.
     */
    public String saveProperty(String key, String value);
}
