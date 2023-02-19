package me.noitcereon.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConfigSaverTest {

    private ConfigSaver configSaver;
    private final String testConfigurationFileName = "test-config-saver.conf";

    @BeforeEach
    public void init(){
        configSaver = new ConfigSaver(testConfigurationFileName);
    }

    @Test
    void givenKeyValuePair_WhenSavingProperty_ThenUpdateConfigurationFile() {
        String newKey = "one-plus-one-is";
        String expectedKeyValue = String.valueOf(UUID.randomUUID());
        configSaver.saveProperty(newKey, expectedKeyValue);

        ConfigLoader configLoader = ConfigLoader.getInstance(testConfigurationFileName);
        String actualKeyValue = configLoader.getProperty(newKey);

        assertEquals(expectedKeyValue, actualKeyValue);
    }
    @Test
    void givenKeyValuePair_WhenSavingProperty_ThenReturnSuccessMessage(){
        String newKey = "aKey";
        String expectedKeyValue = "aValue";
        String addedMessage = "Added new key";
        String updatedMessage = "Updated existing key";

        String returnedMessage = configSaver.saveProperty(newKey, expectedKeyValue);

        boolean expected = true;
        boolean actual = returnedMessage.contains(addedMessage) || returnedMessage.contains(updatedMessage);

        assertEquals(expected, actual);
    }
}