package me.noitcereon.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FileNameGeneratorTest {

    @Test
    void givenFromDateAndToDate_WhenGeneratingMeterDataCsvFileName_ThenNameIsAsExpected() {
        String expected = "meterdata2024-01-01-2025-05-02.csv";
        LocalDate dateFrom = LocalDate.of(2024, 1, 1);
        LocalDate dateTo = LocalDate.of(2025, 5,2);

        String actual = FileNameGenerator.meterDataCsvFile(dateFrom, dateTo);

        Assertions.assertEquals(expected, actual);
    }
}