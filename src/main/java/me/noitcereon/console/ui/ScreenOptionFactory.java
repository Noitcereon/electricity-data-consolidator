package me.noitcereon.console.ui;

import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiController;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointsRequest;
import me.noitcereon.utilities.FileNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Contains the available options for the application.
 */
public class ScreenOptionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ScreenOptionFactory.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;
    private final ElOverblikApiController elOverblikApi;
    private static final String CUSTOM_METER_DATA_FORMAT_ENABLED_KEY = "custom-meter-data-format-enabled";
    public static final String FALSE = "false";

    public ScreenOptionFactory() {
        configLoader = SimpleConfigLoader.getInstance();
        configSaver = SimpleConfigSaver.getInstance();
        this.elOverblikApi = new ElOverblikApiController();
    }

    public ScreenOptionFactory(ElOverblikApiController elOverblikApiController) {
        configLoader = SimpleConfigLoader.getInstance();
        configSaver = SimpleConfigSaver.getInstance();
        this.elOverblikApi = elOverblikApiController;
    }

    public ScreenOptionFactory(ConfigurationLoader configLoader, ConfigurationSaver configSaver, ElOverblikApiController elOverblikApiController) {
        this.configLoader = configLoader;
        this.configSaver = configSaver;
        this.elOverblikApi = elOverblikApiController;
    }

    public ScreenOption mainMenuOption() {
        return new ScreenOption("Displays the main menu", () -> ScreenFactory.createMainMenu().displayScreenAndAskForInput().execute());
    }

    public ScreenOption fetchMeterData() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate dayBeforeYesterDay = yesterday.minusDays(1);
        boolean useCustomMeterDataFormat = Boolean.parseBoolean(configLoader.getProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY, FALSE).get());
        return new ScreenOption("Fetch meterdata from yesterday (" + yesterday + ")", () -> {
            try{
                Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(true);
                if (meteringPoints.isEmpty()) {
                    LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                    throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
                }
                MeteringPointsRequest meteringPointsToGetDataFrom = MeteringPointsRequest.from(meteringPoints.get());
                MethodOutcome outcome = useCustomMeterDataFormat ?
                        elOverblikApi.fetchMeterDataCsvCustomFormat(meteringPointsToGetDataFrom, dayBeforeYesterDay, yesterday, TimeAggregation.HOUR)
                        : elOverblikApi.fetchMeterDataCsvFile(meteringPointsToGetDataFrom, dayBeforeYesterDay, yesterday, TimeAggregation.HOUR);
                if (outcome.equals(MethodOutcome.SUCCESS)) {
                    return displayMeterDataSuccessResultScreen(dayBeforeYesterDay, yesterday, useCustomMeterDataFormat);
                }
                return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
            }catch (IOException e){
                throw new ElectricityConsolidatorRuntimeException(e);
            }
        });
    }

    protected Screen displayMeterDataSuccessResultScreen(LocalDate fromDate, LocalDate toDate, boolean useCustomFormat) {
        String fileName = useCustomFormat ? FileNameGenerator.meterDataCustomFormatCsvFile(fromDate, toDate) : FileNameGenerator.meterDataCsvFile(fromDate, toDate);
        String dataDirectory = System.getProperty("user.dir") + File.separator + "dataFromApi" + File.separator;
        String fileLocation = dataDirectory + fileName;
        return ScreenFactory.resultScreen("MeterData was saved to '%s'".formatted(fileLocation));
    }

    public ScreenOption fetchMeterDataBasedOnLastFetchTime() {
        LocalDate latestFetchDate = LocalDate.now().minusDays(2);
        Optional<String> latestFetchDateFromConf = configLoader.getProperty(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE);
        boolean useCustomMeterDataFormat = Boolean.parseBoolean(configLoader.getProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY, FALSE).get());
        if (latestFetchDateFromConf.isPresent()) {
            latestFetchDate = LocalDate.parse(latestFetchDateFromConf.get());
        }
        LocalDate fromDate = latestFetchDate;
        LocalDate toDate = LocalDate.now().minusDays(1);
        return new ScreenOption("Fetch MeterData based on latest fetch date (from %s to %s)".formatted(fromDate, toDate), () -> {
            try{
                Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(true);
                if (meteringPoints.isEmpty()) {
                    LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                    throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
                }
                MeteringPointsRequest meteringPointsToGetDataFrom = MeteringPointsRequest.from(meteringPoints.get());
                MethodOutcome outcome = useCustomMeterDataFormat ?
                        elOverblikApi.fetchMeterDataCsvCustomFormat(meteringPointsToGetDataFrom, fromDate, toDate, TimeAggregation.HOUR)
                        : elOverblikApi.fetchMeterDataCsvFile(meteringPointsToGetDataFrom, fromDate, toDate, TimeAggregation.HOUR);
                if (outcome.equals(MethodOutcome.SUCCESS)) {
                    System.out.println(configSaver.saveProperty(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE, toDate.toString()));
                    return displayMeterDataSuccessResultScreen(fromDate, toDate, useCustomMeterDataFormat);
                }
                return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
            }catch (IOException e){
                throw new ElectricityConsolidatorRuntimeException(e);
            }
        });
    }

    public ScreenOption fetchMeterDataCustomPeriod() {
        boolean useCustomMeterDataFormat = Boolean.parseBoolean(configLoader.getProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY, FALSE).get());
        return new ScreenOption("Fetch meterdata from a period you define", () -> {
            try {
                System.out.println("Enter dateFrom in format YYYY-MM-DD");
                LocalDate dateFrom = LocalDate.parse(Screen.getScannerInstance().nextLine());
                System.out.println("Enter dateTo in format YYYY-MM-DD");
                LocalDate dateTo = LocalDate.parse(Screen.getScannerInstance().nextLine());
                Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(true);
                if (meteringPoints.isEmpty()) {
                    LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                    throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
                }
                MeteringPointsRequest meteringPointsToGetDataFrom = MeteringPointsRequest.from(meteringPoints.get());
                MethodOutcome outcome = useCustomMeterDataFormat ?
                        elOverblikApi.fetchMeterDataCsvCustomFormat(meteringPointsToGetDataFrom, dateFrom, dateTo, TimeAggregation.HOUR)
                        : elOverblikApi.fetchMeterDataCsvFile(meteringPointsToGetDataFrom, dateFrom, dateTo, TimeAggregation.HOUR);
                if (outcome.equals(MethodOutcome.SUCCESS)) {
                    return displayMeterDataSuccessResultScreen(dateFrom, dateTo, useCustomMeterDataFormat);
                }
                return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
            } catch (DateTimeParseException parseException) {
                System.err.println("Failed to parse input as date (" + parseException.getMessage() + ") . Going back to main menu.");
                return ScreenFactory.createMainMenu();
            } catch (IOException e){
                throw new ElectricityConsolidatorRuntimeException(e);
            }
        });
    }

    public static ScreenOption exitApplication() {
        return new ScreenOption("Exits the application.", () -> {
            System.exit(0);
            return null; // this should never be reached.
        });
    }

    public ScreenOption toggleMeterDataFormat() {

        Optional<String> loadedConfigOption = configLoader.getProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY);
        if (loadedConfigOption.isEmpty()) {
            configSaver.saveProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY, "true");
            loadedConfigOption = Optional.of("true");
        }
        boolean isCustomFormatEnabled = Boolean.parseBoolean(loadedConfigOption.get());

        return new ScreenOption("Toggle format of fetched meterdata (Currently: " + isCustomFormatEnabled + ")", () -> {
            boolean newConfigValue = !isCustomFormatEnabled;
            configSaver.saveProperty(CUSTOM_METER_DATA_FORMAT_ENABLED_KEY, String.valueOf(newConfigValue));

            String content = newConfigValue ? "Custom Meter Data Format is now enabled." : "Custom Meter Data Format is now disabled.";
            return ScreenFactory.resultScreen(content);
        });
    }
}
