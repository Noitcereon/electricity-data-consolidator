package me.noitcereon.configuration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class SimpleConfigLoader implements ConfigurationLoader {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConfigLoader.class);
    private static SimpleConfigLoader instance;
    /**
     * Specifies which files are known to the SimpleConfigLoader and will be loaded.
     */
    private Set<String> files;
    private Map<String, String> propertyCache;
    private LocalTime lastRefreshTime;

    private SimpleConfigLoader() {
        files = new HashSet<>();
        files.add("api-key.conf");
        files.add("data-access-key.conf");
        files.add("simpleConfigLoaderTest.conf");
    }

    @Override
    public Optional<String> getProperty(String key) {
        refreshCache(key);
        if (propertyCache.containsKey(key)) return Optional.of(propertyCache.get(key));
        return Optional.empty();
    }

    private void refreshCache(String keyBeingLookedFor) {
        if (lastRefreshTime == null) {
            propertyCache = loadConfigurationProperties(keyBeingLookedFor);
            lastRefreshTime = LocalTime.now();
        }
        if (lastRefreshTime.plusMinutes(5).isAfter(LocalTime.now())) {
            return;
        }
        propertyCache = loadConfigurationProperties(keyBeingLookedFor);
        lastRefreshTime = LocalTime.now();
    }

    private Map<String, String> loadConfigurationProperties(String keyBeingLookedFor) {
        Map<String, String> output = new HashMap<>();
        files.add(keyBeingLookedFor + ".conf");
        for (String fileName : files) {
            Path file = getConfigFile(fileName);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (isLineToBeIgnored(line)) continue;
                    Map.Entry<String, String> keyValuePair = convertLineToKeyValuePair(line);
                    output.put(keyValuePair.getKey(), keyValuePair.getValue());
                }
            } catch (IOException e) {
                throw new ElectricityConsolidatorRuntimeException(e);
            }
        }

        return output;
    }

    private boolean isLineToBeIgnored(String line) {
        return line.startsWith("#") || line.startsWith("//") || line.isEmpty();
    }

    /**
     * Gets the Path of a file or creates it if it does not exist.
     *
     * @param fileName The name of the file, including extension.
     * @return {@link Path} of created file or existing file.
     */
    private Path getConfigFile(String fileName) {
        Path configFilePath = Path.of("./config/" + fileName);
        try {
            Files.createDirectories(Path.of("config"));
            Files.createFile(configFilePath);
        } catch (FileAlreadyExistsException e) {
            LOG.debug("File {} already exists.", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configFilePath;
    }

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
            LOG.error("Warning: The value for the key {} is empty.", key);
        }
        Map.Entry<String, String> kvp = Map.entry(key.toString(), value.toString());

        Objects.requireNonNull(kvp);
        return kvp;
    }

    public static SimpleConfigLoader getInstance() {
        if (instance == null) {
            instance = new SimpleConfigLoader();
        }
        return instance;
    }

}
