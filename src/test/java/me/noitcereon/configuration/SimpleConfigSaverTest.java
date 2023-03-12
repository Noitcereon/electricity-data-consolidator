package me.noitcereon.configuration;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SimpleConfigSaverTest {

    SimpleConfigSaver configSaver = SimpleConfigSaver.getInstance();
    SimpleConfigLoader configLoader = SimpleConfigLoader.getInstance();
    @Test
    void givenValidInput_WhenSavingProperty_ThenReturnSuccessMessage() {
        String key = "simple-config-saver-key";
        String value = "simple-config-saver-value";
        String expected = "Updated " + key + "to the value " + value;

        String actual = configSaver.saveProperty(key, value);
        assertEquals(expected, actual);
    }
    @Test
    void givenValidInput_WhenSavingProperty_ThenSavedValueCanBeLoaded() {
        String key = "simple-config-saver-key";
        String expectedValue = "simple-config-saver-value";
        String expectedOutput = "Updated " + key + "to the value " + expectedValue;

        String actualOutput = configSaver.saveProperty(key, expectedValue);
        String actualValue = configLoader.getProperty(key).orElseThrow();

        assertEquals(expectedOutput, actualOutput);
        assertEquals(expectedValue, actualValue);
    }
}