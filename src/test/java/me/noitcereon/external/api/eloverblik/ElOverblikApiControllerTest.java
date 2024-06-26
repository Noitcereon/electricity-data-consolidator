package me.noitcereon.external.api.eloverblik;

import me.noitcereon.CustomJunitTag;
import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.configuration.SimpleConfigSaver;
import me.noitcereon.external.api.eloverblik.models.MeterDataReadingsDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag(CustomJunitTag.INTEGRATION_TEST)
class ElOverblikApiControllerTest {

    private static ElOverblikApiController controller;
    private static final Logger LOG = LoggerFactory.getLogger(ElOverblikApiControllerTest.class);
    @BeforeAll // Runs a single time when this tests in this class are to be executed
    public static void setUp() {
        controller = new ElOverblikApiController(SimpleConfigSaver.getInstance(), SimpleConfigLoader.getInstance());
    }
    @Test
    void givenApiTokenIsConfigured_WhenRetrievingDataAccessToken_ThenAccessTokenIsNotNull() {
        String accessToken = controller.retrieveDataAccessToken();
        assertNotNull(accessToken);
        LOG.info(accessToken);
    }

    /**
     * This test is just an attempt to verify if the token is at least somewhat correct. Not a very good test probably...
     */
    @Test
    void givenApiTokenIsConfigured_WhenRetrievingDataAccessToken_ThenAccessTokenIsAbove3400Chars() {
        int expectedMinTokenLength = 3400; // Note: The token is not a constant length.
        String accessToken = controller.retrieveDataAccessToken();
        int actualTokenLength = accessToken.length();

        assertTrue(expectedMinTokenLength < actualTokenLength);
    }
    @Test
    void givenDataAccessToken_WhenRetrievingMeteringPoints_ThenResponseWithDataIsReturned(){
        // Arrange
        boolean includeAll = false;
        String unexpectedMeteringPointId = "";

        // Act
        Optional<List<MeteringPointApiDto>> meteringPointsOptional = controller.getMeteringPoints(includeAll);
        // Assert
        List<MeteringPointApiDto> meteringsPoints = meteringPointsOptional.orElseThrow();
        if(meteringsPoints.isEmpty()) Assertions.fail("No meteringpoint data for some reason.");
        String meteringPointId = meteringsPoints.get(0).getMeteringPointId();
        Assertions.assertNotEquals(unexpectedMeteringPointId, meteringPointId);
    }
    @Test
    @Disabled("Because it is not implemented yet.")
    void givenDataAccessToken_WhenRetrievingMeterData_ThenResponseWithDataIsReturned(){
        // Arrange
        LocalDate dateFrom = LocalDate.of(2024, Month.FEBRUARY, 18);
        LocalDate dateTo = LocalDate.of(2024, Month.FEBRUARY, 19);
        TimeAggregation timeAggregation = TimeAggregation.HOUR;
        // Act
        Optional<List<MeterDataReadingsDto>> meterDataOptional = controller.getMeterDataInPeriod(dateFrom, dateTo, timeAggregation);
        // Assert
        List<MeterDataReadingsDto> meterData = meterDataOptional.orElseThrow();
        if(meterData.isEmpty()) Assertions.fail("No meterData for some reason.");
        // maybe more assertions?
    }
    @Test
    @Disabled("Because it is not implemented yet.")
    void givenDataAccessToken_WhenRetrievingMeterData_ThenResponseWithDataIsReturnedAsCsv(){
        // Arrange
        LocalDate dateFrom = LocalDate.of(2024, Month.FEBRUARY, 18);
        LocalDate dateTo = LocalDate.of(2024, Month.FEBRUARY, 19);
        TimeAggregation timeAggregation = TimeAggregation.HOUR;
        // Act
        Optional<List<MeterDataReadingsDto>> meterDataOptional = controller.getMeterDataInPeriod(dateFrom, dateTo, timeAggregation);
        // Assert
        List<MeterDataReadingsDto> meterData = meterDataOptional.orElseThrow();
        if(meterData.isEmpty()) Assertions.fail("No meterData for some reason.");
        // maybe more assertions?
    }
    @Test
    void givenDataAccessTokenAndMeteringPointsAndFromToDate_WhenFetchingHourlyMeterDataAsFile_ThenSuccessIsReturned(){
        LocalDate startFirstOfJan2024 = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endSecondOfJan2024 = LocalDate.of(2024, Month.JANUARY, 2);
        Optional<List<MeteringPointApiDto>> meteringPoints = controller.getMeteringPoints(false);
        MethodOutcome result = null;
        if(meteringPoints.isPresent()){
            result = controller.getMeterDataCsvFile(meteringPoints.orElseThrow(), startFirstOfJan2024, endSecondOfJan2024, TimeAggregation.HOUR);
        }
        Assertions.assertNotNull(result);
        Assertions.assertEquals(MethodOutcome.SUCCESS, result);
        // TODO Add assertion that file contains meterdata? or is should this be another test?
    }
}