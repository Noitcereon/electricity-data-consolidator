package me.noitcereon.console.ui;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ScreenFactory {
    public static Screen createMainMenu() {
        Map<Integer, ScreenOption> options = new HashMap<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startOfLastWeek = LocalDate.now();
        LocalDate endOfLastWeek = LocalDate.now();
        options.put(1, new ScreenOption("Fetch meterdata from yesterday (" + yesterday + ")", () -> {
            System.out.println("I executed an action!"); // TODO replace this with a message saying that data is being fetched.
            //            ElOverblikApiController elOverblikApi = new ElOverblikApiController();
            //            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            //            if(meteringPoints.isEmpty()){
            //                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
            //                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            //            }
            //            elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), TimeAggregation.HOUR);
            return new Screen("Electricity Data Consolidator App", options); // TODO replace this with a MeterData display screen or soemthing
        }));

        return new Screen("Electricity Data Consolidator App", options);
    }
}
