package me.noitcereon.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    private ConfigLoader configLoader = ConfigLoader.getInstance("test-configuration.conf");
    @Test
    void givenTestConfiguration_WhenRetrievingExistingProperty_ThenReturnValueOfGivenKey() {
        String expected = "Hello World";

        String actual = configLoader.getProperty("my-test-value");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenTestConfiguration_WhenRetrievingAllProperties_ThenReturnAllProperties() {
        Map<String, String> expected = new HashMap<>();
        expected.put("my-test-value", "Hello World");
        expected.put("test-value-2", "two");

        Map<String, String> actual = configLoader.getProperties();

        assertEquals(expected, actual);
    }
}