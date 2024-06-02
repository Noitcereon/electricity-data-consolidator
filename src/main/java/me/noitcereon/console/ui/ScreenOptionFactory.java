package me.noitcereon.console.ui;

import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiController;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.utilities.FileNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public ScreenOptionFactory(){
        configLoader =  SimpleConfigLoader.getInstance();
        configSaver = SimpleConfigSaver.getInstance();
        this.elOverblikApi = new ElOverblikApiController();
    }
    public ScreenOptionFactory(ElOverblikApiController elOverblikApiController){
        configLoader = SimpleConfigLoader.getInstance();
        configSaver = SimpleConfigSaver.getInstance();
        this.elOverblikApi = elOverblikApiController;
    }
    public ScreenOptionFactory(ConfigurationLoader configLoader, ConfigurationSaver configSaver, ElOverblikApiController elOverblikApiController){
        this.configLoader = configLoader;
        this.configSaver = configSaver;
        this.elOverblikApi = elOverblikApiController;
    }
    public ScreenOption mainMenuOption() {
        return new ScreenOption("Displays the main menu", () -> ScreenFactory.createMainMenu().displayScreenAndAskForInput().execute());
    }
    public ScreenOption fetchMeterData(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate dayBeforeYesterDay = yesterday.minusDays(1);
        return new ScreenOption("Fetch meterdata from yesterday (" + yesterday + ")", () -> {
            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            if(meteringPoints.isEmpty()){
                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            }
            MethodOutcome outcome = elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), dayBeforeYesterDay, yesterday, TimeAggregation.HOUR);
            if(outcome.equals(MethodOutcome.SUCCESS)){
                return displayMeterDataSuccessResultScreen(dayBeforeYesterDay, yesterday);
            }
            return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
        });
    }

    protected Screen displayMeterDataSuccessResultScreen(LocalDate fromDate, LocalDate toDate) {
        String fileName = FileNameGenerator.meterDataCsvFile(fromDate, toDate);
        return ScreenFactory.resultScreen("MeterData was saved to '%s'".formatted(fileName));
    }

    public ScreenOption fetchMeterDataBasedOnLastFetchTime(){
        LocalDate latestFetchDate = LocalDate.now().minusDays(2);
        Optional<String> latestFetchDateFromConf =  configLoader.getProperty(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE);
        if(latestFetchDateFromConf.isPresent()){
            latestFetchDate = LocalDate.parse(latestFetchDateFromConf.get());
        }
        LocalDate fromDate = latestFetchDate;
        LocalDate toDate = LocalDate.now().minusDays(1);
        return new ScreenOption("Fetch MeterData based on latest fetch date (from %s to %s)".formatted(fromDate, toDate), () -> {
            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            if(meteringPoints.isEmpty()){
                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            }
            MethodOutcome outcome = elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), fromDate, toDate, TimeAggregation.HOUR);
            if(outcome.equals(MethodOutcome.SUCCESS)){
                System.out.println(configSaver.saveProperty(ConfigurationKeys.LATEST_METER_DATA_FETCH_DATE, toDate.toString()));
                return displayMeterDataSuccessResultScreen(fromDate, toDate);
            }
            return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
        });
    }
    public ScreenOption fetchMeterDataCustomPeriod(){
        return new ScreenOption("Fetch meterdata from a period you define", () -> {
            try{
                System.out.println("Enter dateFrom in format YYYY-MM-DD");
                LocalDate dateFrom = LocalDate.parse(Screen.getScannerInstance().nextLine());
                System.out.println("Enter dateTo in format YYYY-MM-DD");
                LocalDate dateTo = LocalDate.parse(Screen.getScannerInstance().nextLine());
                Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
                if(meteringPoints.isEmpty()){
                    LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                    throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
                }
                MethodOutcome outcome = elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), dateFrom, dateTo, TimeAggregation.HOUR);
                if(outcome.equals(MethodOutcome.SUCCESS)){
                    return displayMeterDataSuccessResultScreen(dateFrom, dateTo);
                }
                return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
            }catch (DateTimeParseException parseException){
                System.err.println("Failed to parse input as date ("+ parseException.getMessage()+ ") . Going back to main menu.");
                return ScreenFactory.createMainMenu();
            }
        });
    }

    public static ScreenOption exitApplication(){
        return new ScreenOption("Exits the application.", () -> {
            System.exit(0);
            return null; // this should never be reached.
        });
    }

}
