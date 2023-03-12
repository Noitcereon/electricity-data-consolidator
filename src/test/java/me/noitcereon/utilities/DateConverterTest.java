package me.noitcereon.utilities;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class DateConverterTest {

    @Test
    void givenStringDateInFormatYYYYMMDD_WhenConvertingToLocalDate_ExpectSameDate() {
        String lastDayOfJan2000 = "2000-01-31";

        LocalDate localDate = DateConverter.toLocalDate(lastDayOfJan2000);

        assertEquals(2000, localDate.getYear());
        assertEquals(Month.JANUARY, localDate.getMonth());
        assertEquals(31, localDate.getDayOfMonth());
    }

    @Test
    void givenLocalDate_WhenConvertingToStringFormat_ExpectSameDate() {
        LocalDate dateToConvertToStr = LocalDate.of(2000, Month.JANUARY, 31);
        String expectedDate = "2000-01-31";
        String actualDate = DateConverter.toStringFromLocalDate(dateToConvertToStr);
        assertEquals(expectedDate, actualDate);
    }
}