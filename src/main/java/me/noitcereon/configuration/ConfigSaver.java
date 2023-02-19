package me.noitcereon.configuration;

import me.noitcereon.exceptions.NotImplementedException;

/**
 * ConfigSaver works in tandem with the {@link ConfigLoader} class. This class is responsible for saving new configuration values
 * to a configuration file.
 *
 * @author Noitcereon
 * @since 0.0.1
 */
public class ConfigSaver {
    private static ConfigSaver instance;
    private String configurationFile;
    private ConfigSaver(){
        // Prevent accidental instantiation.
        configurationFile = "configuration.conf";
    }
    private ConfigSaver(String configFileName){
        // Prevent accidental instantiation.
        configurationFile = configFileName;
    }

    /**
     * Adds a new value to an existing key or creates a key with the given value in the configuration file.
     * @param key The String value representing the key used to get the value.
     * @param value A String value representing the value associated with the key.
     * @return A message indicating success or failure to save.
     */
    public static String saveProperty(String key, String value){
        throw new NotImplementedException();
    }
    public static ConfigSaver getInstance(){
        if(instance == null){
            instance = new ConfigSaver();
        }
        return instance;
    }
    public static ConfigSaver getInstance(String configFileName){
        if(instance == null){
            instance = new ConfigSaver(configFileName);
        }
        return instance;
    }
}
