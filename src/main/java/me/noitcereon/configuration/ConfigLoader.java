package me.noitcereon.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author Noitcereon
 * @since 0.0.1
 * ConfigLoader loads configuration properties based on the given key from a configuration file.
 * The default configuration file is named `configuration.conf`
 */
public class ConfigLoader {
    private static ConfigLoader instance;
    private Map<String, String> configurationProperties;

    private ConfigLoader(String configFileName){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classloader.getResourceAsStream(configFileName)) {
            Objects.requireNonNull(inputStream);
            Scanner scanner = new Scanner(inputStream);
            configurationProperties = mapConfigFileToHashMap(scanner);
        } catch (IOException e) {
            System.err.println("Something went wrong during initialization of ConfigLoader");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Takes the configuration file content and maps it to a HashMap.
     * @param scanner The scanner to read the configuration file from.
     * @return A HashMap with all the configuration properties.
     */
    private HashMap<String, String> mapConfigFileToHashMap(Scanner scanner) {
        HashMap<String, String> output = new HashMap<>();
        try (scanner) {
            while (scanner.hasNextLine()) {
                Map.Entry<String, String> keyValuePair = convertLineToKeyValuePair(scanner.nextLine());
                output.put(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
        return output;
    }

    /**
     * @param line A line from the configuration file in the format of `key=value`
     * @return The line separated into a key-value pair.
     */
    private Map.Entry<String, String> convertLineToKeyValuePair(String line) {
        char delimiterChar = '=';
        char encapsulationChar = '"';
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isDelimiterReached = false;
        for (char character : line.toCharArray()){
            if(character == delimiterChar){
                isDelimiterReached = true;
                continue;
            }
            if(character == encapsulationChar){
                continue; // The second time this is reached, it should be finished iterating through the line.charArray.
            }
            if(!isDelimiterReached){
                key.append(character);
            }
            if(isDelimiterReached){
                value.append(character);
            }
        }
        if(key.toString().equals("")){
            throw new NullPointerException("Key must not be null or empty.");
        }
        if(value.toString().equals("")){
            throw new NullPointerException("Value must not be null or empty.");
        }
        Map.Entry<String, String> kvp = Map.entry(key.toString(), value.toString());

        Objects.requireNonNull(kvp);
        return kvp;
    }

    public String getProperty(String key) {
        getInstance();
        String output = null;
        try {
            if(configurationProperties.containsKey(key)){
                output = configurationProperties.get(key);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return output;
    }
    public Map<String, String> getProperties(){
        return configurationProperties;
    }
    public static ConfigLoader getInstance(String configFileName){
        if(Objects.isNull(instance)){
            instance = new ConfigLoader(configFileName);
        }
        return instance;
    }
    public static ConfigLoader getInstance(){
        if(Objects.isNull(instance)){
            instance = new ConfigLoader("configuration.conf");
        }
        return instance;
    }
}
