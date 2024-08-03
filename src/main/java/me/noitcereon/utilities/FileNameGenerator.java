package me.noitcereon.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileNameGenerator {
    private FileNameGenerator(){
        // No need to instantiate static helper class.
    }
    public static String meterDataCsvFile(LocalDate dateFrom, LocalDate dateTo){
        return "meterdata" + dateFrom.format(DateTimeFormatter.ISO_DATE) + "-"+dateTo.format(DateTimeFormatter.ISO_DATE) + ".csv";
    }
    public static String meterDataCustomFormatCsvFile(LocalDate dateFrom, LocalDate dateTo){
        return "meterdata-custom-format" + dateFrom.format(DateTimeFormatter.ISO_DATE) + "-"+dateTo.format(DateTimeFormatter.ISO_DATE) + ".csv";
    }
}
