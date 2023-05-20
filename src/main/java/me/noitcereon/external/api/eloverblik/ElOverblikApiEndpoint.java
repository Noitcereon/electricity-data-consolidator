package me.noitcereon.external.api.eloverblik;

public class ElOverblikApiEndpoint {
    private ElOverblikApiEndpoint(){
        // Do not instantiate.
    }

    private static final String BASE_URL = "https://api.eloverblik.dk/customerapi/api/";
    public static final String DATA_ACCESS_TOKEN = BASE_URL + "token";
    public static final String METERING_POINTS = BASE_URL + "meteringpoints/meteringpoints";

}
