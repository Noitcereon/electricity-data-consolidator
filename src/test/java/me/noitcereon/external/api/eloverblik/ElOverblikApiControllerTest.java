package me.noitcereon.external.api.eloverblik;

import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.configuration.SimpleConfigSaver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

@Tag("IntegrationTest")
class ElOverblikApiControllerTest {

    private static ElOverblikApiController controller;
    private static final Logger LOG = LoggerFactory.getLogger(ElOverblikApiControllerTest.class);
    @BeforeAll // Runs a single time when this tests in this class are to be executed
    public static void setUp() {
        controller = new ElOverblikApiController(SimpleConfigSaver.getInstance(), SimpleConfigLoader.getInstance());
    }
    @Test
    void givenNothing_WhenRetrievingDataAccessToken_ThenAccessTokenIsNotNull() {
        String accessToken = controller.retrieveDataAccessToken();
        assertNotNull(accessToken);
        LOG.info(accessToken);
    }

    /**
     * This test is just an attempt to verify if the token is at least somewhat correct. Not a very good test probably...
     */
    @Test
    void givenNothing_WhenRetrievingDataAccessToken_ThenAccessTokenIsAbove3400Chars() {
        int expectedMinTokenLength = 3400; // Note: The token is not a constant length.
        String accessToken = controller.retrieveDataAccessToken();
        int actualTokenLength = accessToken.length();

        assertTrue(expectedMinTokenLength < actualTokenLength);
    }
}