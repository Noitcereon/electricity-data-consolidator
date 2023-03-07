package me.noitcereon.configuration;

import me.noitcereon.exceptions.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ConfigLoader loads configuration properties based on the given key from a configuration file.
 * The default configuration file is named `configuration.conf`
 *
 * @author Noitcereon
 * @since 0.0.1
 */
public class ConfigLoader implements ConfigurationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
    private static ConfigLoader instance;
    private Map<String, String> configurationProperties;

    private ConfigLoader(String configFileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classloader.getResourceAsStream(configFileName)) {
            Objects.requireNonNull(inputStream);
            Scanner scanner = new Scanner(inputStream);
            configurationProperties = mapConfigFileToHashMap(scanner);
        } catch (IOException e) {
            LOG.error("Something went wrong during initialization of ConfigLoader");
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Takes the configuration file content and maps it to a HashMap.
     *
     * @param scanner The scanner to read the configuration file from.
     * @return A HashMap with all the configuration properties.
     */
    private HashMap<String, String> mapConfigFileToHashMap(Scanner scanner) {
        HashMap<String, String> output = new HashMap<>();
        try (scanner) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (isLineToBeIgnored(line)) continue;
                Map.Entry<String, String> keyValuePair = convertLineToKeyValuePair(line);
                output.put(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
        return output;
    }

    private static boolean isLineToBeIgnored(String line) {
        return line.startsWith("#") || line.startsWith("//") || line.isEmpty();
    }

    /**
     * @param line A line from the configuration file in the format of `key=value`
     * @return The line separated into a key-value pair.
     */
    private Map.Entry<String, String> convertLineToKeyValuePair(String line) {
        // TODO: Refactor this method to be more readable
        char delimiterChar = '=';
        char encapsulationChar = '"';
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isDelimiterReached = false;
        for (char character : line.toCharArray()) {
            if (character == delimiterChar) {
                isDelimiterReached = true;
                continue;
            }
            if (character == encapsulationChar) {
                continue; // The second time this is reached, it should be finished iterating through the line.charArray.
            }
            if (!isDelimiterReached) {
                key.append(character);
            }
            if (isDelimiterReached) {
                value.append(character);
            }
        }
        if (key.toString().equals("")) {
            throw new NullPointerException("Key must not be null or empty.");
        }
        if (value.isEmpty()) {
            LOG.warn("Warning: The value for the key {} is empty.", key);
        }
        Map.Entry<String, String> kvp = Map.entry(key.toString(), value.toString());

        Objects.requireNonNull(kvp);
        return kvp;
    }

    public Optional<String> getProperty(String key) {
        getInstance();
        String propertyValue = null;
        try {
            if (configurationProperties.containsKey(key)) {
                propertyValue = configurationProperties.get(key);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        if(propertyValue == null) return Optional.empty();
        return Optional.of(propertyValue);
    }

    public Map<String, String> getProperties() {
        return configurationProperties;
    }

    public static ConfigLoader getInstance(String configFileName) {
        throw new NotImplementedException("ConfigLoader is not fully implemented yet.");
//        if (Objects.isNull(instance)) {
//            instance = new ConfigLoader(configFileName);
//        }
//        return instance;
    }

    public static ConfigLoader getInstance() {
        throw new NotImplementedException("ConfigLoader is not fully implemented yet.");
//        if (Objects.isNull(instance)) {
//            instance = new ConfigLoader(Constants.HARDCODED_CONFIG_FILE);
//        }
//        return instance;
    }
}
