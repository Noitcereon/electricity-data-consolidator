package me.noitcereon.console.ui;

import me.noitcereon.MethodOutcome;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiController;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static me.noitcereon.console.ui.ScreenFactory.resultScreen;

/**
 * Contains the available options for the application.
 */
public class ScreenOptionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ScreenOptionFactory.class);
    private ScreenOptionFactory(){
        // Don't instantiate, because the ScreenOptions should only be available through static methods.
    }
    public static ScreenOption mainMenuOption() {
        return new ScreenOption("Displays the main menu", () -> ScreenFactory.createMainMenu().displayScreenAndAskForInput().execute());
    }
    public static ScreenOption fetchMeterData(){
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate dayBeforeYesterDay = yesterday.minusDays(1);
        return new ScreenOption("Fetch meterdata from yesterday (" + yesterday + ")", () -> {
            ElOverblikApiController elOverblikApi = new ElOverblikApiController();
            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            if(meteringPoints.isEmpty()){
                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            }
            MethodOutcome outcome = elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), dayBeforeYesterDay, yesterday, TimeAggregation.HOUR);
            if(outcome.equals(MethodOutcome.SUCCESS)){
                return ScreenFactory.resultScreen("MeterData was saved to file.");
            }
            return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
        });
    }
    public static ScreenOption fetchMeterDataCustomPeriod(){
        return new ScreenOption("Fetch meterdata from a period you define", () -> {
            System.out.println("Enter dateFrom in format YYYY-MM-DD");
            LocalDate dateFrom = LocalDate.parse(Screen.getScannerInstance().nextLine());
            System.out.println("Enter dateTo in format YYYY-MM-DD");
            LocalDate dateTo = LocalDate.parse(Screen.getScannerInstance().nextLine());
            ElOverblikApiController elOverblikApi = new ElOverblikApiController();
            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            if(meteringPoints.isEmpty()){
                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            }
            MethodOutcome outcome = elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), dateFrom, dateTo, TimeAggregation.HOUR);
            if(outcome.equals(MethodOutcome.SUCCESS)){
                return ScreenFactory.resultScreen("MeterData was saved to file.");
            }
            return ScreenFactory.resultScreen("Something went wrong when trying to fetch MeterData.");
        });
    }
    public static ScreenOption showResult(String resultDisplayText){
        return new ScreenOption("Displays the result of an action", () -> resultScreen(resultDisplayText));
    }
    public static ScreenOption exitApplication(){
        return new ScreenOption("Exits the application.", () -> {
            System.exit(0);
            return null; // this should never be reached.
        });
    }

}
