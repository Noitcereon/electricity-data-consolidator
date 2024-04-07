package me.noitcereon;


import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiController;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        try{
            System.out.println("Welcome to this unfinished console application.");
            System.out.println("The intention is to make a simple application, which can ");
            System.out.println("retrieve data from various electricty data APIs.");

            ElOverblikApiController elOverblikApi = new ElOverblikApiController();
            Optional<List<MeteringPointApiDto>> meteringPoints = elOverblikApi.getMeteringPoints(false);
            if(meteringPoints.isEmpty()){
                LOG.error("Failed to retrieve meteringsPoints from API, so can't fetch MeterData.");
                throw new ElectricityConsolidatorRuntimeException("No metering points, so can't continue.");
            }
            elOverblikApi.getMeterDataCsvFile(meteringPoints.get(), LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), TimeAggregation.HOUR);
            System.out.println("Press ENTER to exit...");
            System.in.read(); // intentionally ignored.
            System.out.println("Exiting...");
        }
        catch (Exception e){
            LOG.error("An error occurred, which the application was not built to handle. Showing exception message and StackTrace:");
            LOG.error(e.getMessage(), e);
        }

    }
}