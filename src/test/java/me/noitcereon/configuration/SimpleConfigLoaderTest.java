package me.noitcereon.configuration;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SimpleConfigLoaderTest {

    private SimpleConfigLoader loader = SimpleConfigLoader.getInstance();

    @Test
    void givenKey_WhenGettingProperty_ThenExpectANonNullStringValue() {
        String expected = "manually-entered-value";
        Optional<String> actual = loader.getProperty("simpleConfigLoaderTest");

        assertNotNull(actual);
        assertEquals(expected, actual.orElseThrow());
    }

    @Test
    void getInstanceShouldAlwaysReturnTheSameInstance() {
        SimpleConfigLoader expected = loader;
        SimpleConfigLoader actual = SimpleConfigLoader.getInstance();

        assertEquals(expected, actual);
    }
}