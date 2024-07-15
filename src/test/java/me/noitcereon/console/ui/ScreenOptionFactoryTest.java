package me.noitcereon.console.ui;

import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.ConfigurationKeys;
import me.noitcereon.configuration.ConfigurationLoader;
import me.noitcereon.configuration.ConfigurationSaver;
import me.noitcereon.external.api.eloverblik.ElOverblikApiController;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.utilities.FileNameGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class ScreenOptionFactoryTest {

    private ElOverblikApiController mockElOverblikApi;
    private ScreenOptionFactory screenOptionFactory;
    private ConfigurationLoader mockConfigLoader;
    private ConfigurationSaver mockConfigSaver;

    @BeforeEach
    public void setup() {
        mockElOverblikApi = mock(ElOverblikApiController.class);
        mockConfigLoader = mock(ConfigurationLoader.class);
        mockConfigSaver = mock(ConfigurationSaver.class);
        screenOptionFactory = new ScreenOptionFactory(mockConfigLoader, mockConfigSaver, mockElOverblikApi);
    }

    @Test
    void givenElOverblikApiMocksAndConfigMocks_WhenTriggeringFetchLatestMeterDataWithSuccessOutcome_ThenScreenContentIndicatesSuccess() {
        // Scenarios to test (based on Screen.getContent):
        // 1. No meteringspoints - Expected: Bad result screen
        // 2. No meterdata - Expected: Bad result screen
        // 3. Meterdata returned (success) - Expected: Good result screen
        LocalDate fetchDataFromDate = LocalDate.of(2024, 1, 1);
        Mockito.when(mockConfigLoader.getProperty(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE))
                .thenReturn(Optional.of(fetchDataFromDate.toString()));
        LocalDate fetchDataToDate = LocalDate.now().minusDays(1);
        Mockito.when(mockElOverblikApi.getMeteringPoints(Mockito.anyBoolean()))
                .thenReturn(Optional.of(new ArrayList<>()));
        Mockito.when(mockElOverblikApi.getMeterDataCsvFile(Mockito.any(), eq(fetchDataFromDate), eq(fetchDataToDate), eq(TimeAggregation.HOUR)))
                .thenReturn(MethodOutcome.SUCCESS);

        String dataDirectory = System.getProperty("user.dir") + File.separator + "dataFromApi" + File.separator;
        String filePath = dataDirectory + FileNameGenerator.meterDataCsvFile(fetchDataFromDate, fetchDataToDate);
        String expectedContent = "MeterData was saved to '%s'".formatted(filePath);

        ScreenOption optionUnderTest = screenOptionFactory.fetchMeterDataBasedOnLastFetchTime();

        Screen actual = optionUnderTest.execute();

        Mockito.verify(mockConfigSaver, times(1)).saveProperty(eq(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE), Mockito.anyString());
        Assertions.assertEquals(expectedContent, actual.getContent());
    }
}