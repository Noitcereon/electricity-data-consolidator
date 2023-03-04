package me.noitcereon.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    private final ConfigLoader configLoader = ConfigLoader.getInstance("test-configuration.conf");
    @Test
    void givenTestConfiguration_WhenRetrievingExistingProperty_ThenReturnValueOfGivenKey() {
        String expected = "Hello World";

        String actual = configLoader.getProperty("my-test-value").orElseThrow();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenTestConfiguration_WhenRetrievingAllProperties_ThenReturnAllProperties() {
        Map<String, String> expected = new HashMap<>();
        expected.put("my-test-value", "Hello World");
        expected.put("test-value-2", "two");
        expected.put("nothing-is-associated-with-this-key", "");

        Map<String, String> actual = configLoader.getProperties();

        assertEquals(expected, actual);
    }
    @Test
    void givenTestConfiguration_WhenRetrievingAllProperties_ThenDoNotThrowException() {
        assertDoesNotThrow(() -> {
            Map<String, String> actual = configLoader.getProperties();
            assertFalse(actual.isEmpty());
        });
    }
    @Test
    void givenTestConfiguration_WhenRetrievingBlankValue_BlankIsReadAsEmptyString() {
        String expected = "";

        String actual = configLoader.getProperty("nothing-is-associated-with-this-key").orElseThrow();

        assertEquals(expected, actual);
    }
}