package me.noitcereon.configuration;

import java.io.*;
import java.util.Map;
import java.util.Objects;

/**
 * ConfigSaver works in tandem with the {@link ConfigLoader} class. This class is responsible for saving new configuration values
 * to a configuration file.
 *
 * @author Noitcereon
 * @since 0.0.1
 */
public class ConfigSaver implements ConfigurationSaver {
    private final ConfigLoader configLoader;
    private final String configFileName;

    public ConfigSaver() {
        this.configFileName = Constants.HARDCODED_CONFIG_FILE;
        this.configLoader = ConfigLoader.getInstance();
    }

    public ConfigSaver(String configFileName) {
        this.configFileName = configFileName;
        this.configLoader = ConfigLoader.getInstance(configFileName);
    }

    /**
     * Adds a new value to an existing key or creates a key with the given value in the configuration file.
     * @param key   The String value representing the key used to get the value.
     * @param value A String value representing the value associated with the key.
     * @return A message indicating success or failure to save.
     */
    public String saveProperty(String key, String value) {
        Map<String, String> existingProperties = configLoader.getProperties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        if (existingProperties.containsKey(key)) {
            // TODO: implement updateExistingConfiguration method that works with resources and handles personal-configuration vs default configuration
            updateExistingConfiguration(key, value, classloader);
            return "Updated existing key: '" + key + "' with value: '" + value + "'";
        }
        // TODO: implement addNewConfiguration method that works with resources and handles personal-configuration vs default configuration
        addNewConfiguration(key, value, classloader);
        return "Added new key: '" + key + "' with value: '" + value + "'";
    }

    private void addNewConfiguration(String key, String value, ClassLoader classloader) {
        try (InputStream inputStream = classloader.getResourceAsStream(configFileName)) {
            Objects.requireNonNull(inputStream);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(configFileName));
            inputStream.transferTo(outputStream);

            byte[] configurationLine = (System.lineSeparator() + key + "=" + value + System.lineSeparator()).getBytes();
            outputStream.write(configurationLine);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateExistingConfiguration(String key, String value, ClassLoader classloader) {
        try (InputStream inputStream = classloader.getResourceAsStream(configFileName)) {
            Objects.requireNonNull(inputStream);

            OutputStream outputStream = new ByteArrayOutputStream(2048);
            inputStream.transferTo(outputStream);

            byte[] bytes = (key + "=" + value).getBytes();
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
