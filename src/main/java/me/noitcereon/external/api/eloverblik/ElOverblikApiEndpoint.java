package me.noitcereon.external.api.eloverblik;

import java.time.LocalDate;

public class ElOverblikApiEndpoint {
    private ElOverblikApiEndpoint(){
        // Do not instantiate.
    }

    private static final String BASE_URL = "https://api.eloverblik.dk/customerapi/api/";
    public static final String DATA_ACCESS_TOKEN = BASE_URL + "token";
    public static final String METERING_POINTS = BASE_URL + "meteringpoints/meteringpoints";

    private static final String METER_DATA_CSV = BASE_URL + "meterdata/timeseries/export/";

    public static String getMeterDataCsvEndPoint(LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit){
        return METER_DATA_CSV + dateFrom + "/" + dateTo + "/" + aggregationUnit.label;
    }

}
