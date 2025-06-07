package me.noitcereon.external.api.eloverblik;

/**
 * Reference: <a href="https://api.eloverblik.dk/CustomerApi/index.html">ElOverblik Swagger documentation</a> <br/>
 * Aggregation is used in, for example, `/api/meterdata/gettimeseries/{dateFrom}/{dateTo}/{aggregation}`, which
 * Returns a timeserie for each metering point in list.)
 *
 * <p>
 * The purpose of TimeAggregation is to split up the data into the wanted time chunks.
 * For instance, using HOUR means you want the data split into hours and thus have access
 * to hourly data instead of, say, daily data with DAY.
 * </p>
 */
public enum TimeAggregation {
    ACTUAL("Actual"),
    QUARTER("Quarter"),
    HOUR("Hour"),
    DAY("Day"),
    MONTH("Month"),
    YEAR("Year");

    /**
     * The actual value used by ElOverblikApi.
     */
    public final String label;

    TimeAggregation(String label) {
        this.label = label;
    }


}
