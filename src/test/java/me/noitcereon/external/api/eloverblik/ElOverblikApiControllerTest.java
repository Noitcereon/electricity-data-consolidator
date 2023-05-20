package me.noitcereon.external.api.eloverblik;

import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.configuration.SimpleConfigSaver;
import org.junit.jupiter.api.BeforeAll;
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
    @Test
    void givenNothing_WhenRetrievingDataAccessToken_ThenAccessTokenIsExactly3451Chars() {
        int expectedTokenStringLength = 3451; // Note: I am unsure if the length of the data access token is a constant.
        String accessToken = controller.retrieveDataAccessToken();
        int actualLength = accessToken.length();

        assertEquals(expectedTokenStringLength, actualLength);
    }
}