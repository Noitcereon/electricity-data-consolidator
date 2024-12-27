package me.noitcereon.external.api.eloverblik.models;

import me.noitcereon.external.api.eloverblik.TimeAggregation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * A model representing MeterData from ElOverblikApi in a customized format. Intended for writing meter-data into a specific format in e.g. a CSV file.
 *
 * @author Thomas B. Andersen
 */
public record MeterDataFormatted(String meteringPointId, LocalDateTime fromDateTime, LocalDateTime toDateTime,
                                 String hourOfDay, String amount, String measurementUnit, String quality) {

    /**
     * The default headers/format sent from the El Overblik API, when calling {@link me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint#getMeterDataCsvEndPoint(LocalDate, LocalDate, TimeAggregation)}
     */
    public static final String EL_OVERBLIK_CSV_HEADERS = "Målepunkt id;Fra dato;Til dato;Mængde;Måleenhed;Kvalitet;Type";

    /**
     * The custom CSV headers/format the data is transformed to by this class.
     */
    public static final String CUSTOM_HEADERS = "Målepunkt id;Fra dato;Fra tidspunkt;Til dato;Til tidspunkt;Mængde;Måleenhed;Kvalitet;Type;TimeSlag";

    /**
     * For example: "29-01-2024"
     */
    private static final DateTimeFormatter DAY_MONTH_YEAR = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * For example: "2011-12-03T10:15:30Z"
     */
    private static final DateTimeFormatter YEAR_MONTH_DAY_T_HOUR_MINUTE_SECOND_Z = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());

    /**
     * For example "01-05-2024 01:00:00"
     */
    private static final DateTimeFormatter DAY_MONTH_YEAR_HH_MM_SS = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static final String NEW_LINE_REGEX = "\\n|\\r\\n|\\r";
    private static final String MAALEPUNKT_ID = "Målepunkt id"; // Refactored into instance variable, because it's used in multiple places.

    public static List<MeterDataFormatted> from(TimeSeries timeSeries) {
        // For every Period
        // If Period has 24 entries/is of hourly data
        // extract information from the period as FormattedMeterData
        // Add it to formattedMeterData list
        // Return formattedMeterData.
        List<MeterDataFormatted> output = new ArrayList<>();
        for (Period period : timeSeries.periods()) {
            for (Point point : period.points()) {
                String fromDateTimeStr = period.timeInterval().start(); // timeInterval.start
                String toDateTimeStr = period.timeInterval().end(); // timeInterval.end
                LocalDateTime fromDateTime = LocalDateTime.parse(fromDateTimeStr, YEAR_MONTH_DAY_T_HOUR_MINUTE_SECOND_Z);
                LocalDateTime toDateTime = LocalDateTime.parse(toDateTimeStr, YEAR_MONTH_DAY_T_HOUR_MINUTE_SECOND_Z);

                String amount = point.outQuantity(); // out_Quantity.quantity
                String quality = point.outQuantityQuality(); // out_Quantity.quality
                String hourOfDay = point.position();
                MeterDataFormatted formattedMeterData = new MeterDataFormatted(timeSeries.mRid(), fromDateTime, toDateTime,
                        hourOfDay, amount, timeSeries.measurementUnitName(), quality);
                output.add(formattedMeterData);
            }
        }
        return output;
    }

    public static List<MeterDataFormatted> from(List<TimeSeries> timeSeries) {
        List<MeterDataFormatted> output = new ArrayList<>();
        for (TimeSeries x : timeSeries) {
            output.addAll(from(x));
        }
        return output;
    }

    /**
     * <p>Tries to parse a string with CSV MeterData (with headers) into a list of MeterDataFormatted objects.</p>
     * Example argument:<br>
     * <pre>Målepunkt id;Fra dato;Til dato;Mængde;Måleenhed;Kvalitet;Type
     * 571313174001764929;01-05-2024 00:00:00;01-05-2024 01:00:00;0,053;KWH;Målt;Tidsserie</pre>
     *
     * @param apiCsvFileContents MeterData CSV file contents as provided by ELOverblikApi v1.
     * @return The given argument as a list of {@link MeterDataFormatted}.
     */
    public static List<MeterDataFormatted> parseFrom(String apiCsvFileContents) {

        String normalizedCsvInput = apiCsvFileContents.stripIndent().strip();
        if (!normalizedCsvInput.contains(MAALEPUNKT_ID)) {
            throw new IllegalArgumentException("Can't parse normalized 'apiCsvFileContents': '" + normalizedCsvInput + "'");
        }

        return Arrays.stream(apiCsvFileContents.split(NEW_LINE_REGEX))
                .parallel() // To utilize multiple threads
                .map(MeterDataFormatted::parseCsvLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    /**
     * @param meterDataAsCsvLine Example parseable input: "571313174001764929;01-05-2024 13:00:00;01-05-2024 14:00:00;1,417;KWH;Målt;Tidsserie"
     * @return An Optional with MeterDataFormatted object or an empty Optional if unable to parse the given argument.
     */
    private static Optional<MeterDataFormatted> parseCsvLine(String meterDataAsCsvLine) {
        try {
            if (meterDataAsCsvLine.contains(MAALEPUNKT_ID)) {
                return Optional.empty(); // This is csv header and thus cannot be parsed.
            }
            String[] meterDataArray = meterDataAsCsvLine.split(";");
            if (meterDataArray.length != 7) return Optional.empty();
            String meteringPointId = meterDataArray[0];
            LocalDateTime fromTime = LocalDateTime.parse(meterDataArray[1], DAY_MONTH_YEAR_HH_MM_SS); // Should match format: "01-05-2024 01:00:00"
            LocalDateTime toTime = LocalDateTime.parse(meterDataArray[2], DAY_MONTH_YEAR_HH_MM_SS);
            String hourOfDay = String.valueOf(fromTime.getHour());
            String amount = meterDataArray[3];
            String unitOfMeasurement = meterDataArray[4];
            String dataQuality = meterDataArray[5];
            return Optional.of(new MeterDataFormatted(meteringPointId, fromTime, toTime, hourOfDay, amount, unitOfMeasurement, dataQuality));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Returns the MeterDataFormatted as a csv String using ';' as delimited. Data goes in the following order:
     * Målepunkt id;Fra dato;Fra tidspunkt;Til dato;Til tidspunkt;Mængde;Måleenhed;Kvalitet;Type
     *
     * @return A String in csv format using the provided delimiter.
     * @apiNote This method could be refactored to use a builder pattern (but would require some more changes to be possible), so the format is more flexible.
     */
    public String asCsv(boolean withHeaders) {
        String delimiter = ";";
        StringBuilder sb = new StringBuilder(64); // Assuming that it'll be a string of at least 64 characters for slight performance gain.
        String hourOfDayFormatted = hourOfDay.length() == 1 ? "0" + hourOfDay : hourOfDay; // prepend with zero for single digit numbers, to match user's data format.
        if (withHeaders) {
            sb.append(MAALEPUNKT_ID).append(delimiter)
                    .append("Fra dato")
                    .append(delimiter)
                    .append("Til dato")
                    .append(delimiter)
                    .append("Mængde")
                    .append(delimiter)
                    .append("Måleenhed")
                    .append(delimiter)
                    .append("Kvalitet")
                    .append(delimiter)
                    .append("Type")
                    .append(delimiter)
                    .append("TimeSlag") // Hour of the day. This format is used (instead of the 'datetime' from original) to be compatible with user's data format.
                    .append(System.lineSeparator());
        }
        sb.append(meteringPointId).append(delimiter);
        sb.append(fromDateTime.toLocalDate().format(DAY_MONTH_YEAR)).append(delimiter);
        sb.append(toDateTime.toLocalDate().format(DAY_MONTH_YEAR)).append(delimiter);
        sb.append(amount).append(delimiter);
        sb.append(measurementUnit).append(delimiter);
        sb.append(quality).append(delimiter);
        sb.append("Tidsserie").append(delimiter);
        sb.append(hourOfDayFormatted).append(System.lineSeparator());

        return sb.toString();
    }
}
