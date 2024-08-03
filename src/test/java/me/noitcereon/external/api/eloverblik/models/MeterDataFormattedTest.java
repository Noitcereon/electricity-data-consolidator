package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

class MeterDataFormattedTest {

    @Test
    void givenMeterData_WhenConvertingObjectToCsvStringWithHeaders_ThenFormatIsAsExpected() {
        String meteringPointId = "571313174001764929";
        LocalDateTime fromDateTime = LocalDateTime.of(2024, Month.MAY, 9, 16, 0);
        LocalDateTime toDateTime = LocalDateTime.of(2024, Month.MAY, 9, 17, 0);
        String hourOfDay = "1";
        String amount = "0,752";
        String measurementUnit = "KWH";
        String quality = "Målt";

        MeterDataFormatted meterData = new MeterDataFormatted(meteringPointId, fromDateTime, toDateTime, hourOfDay, amount, measurementUnit, quality);
        String expectedFormat = "Målepunkt id;Fra dato;Fra tidspunkt;Til dato;Til tidspunkt;Mængde;Måleenhed;Kvalitet;Type;TimeSlag" + System.lineSeparator()
                + "571313174001764929;09-05-2024;16:00;09-05-2024;17:00;0,752;KWH;Målt;Tidsserie;1" + System.lineSeparator();

        // 571313174001764929;09-05-2024 16:00:00;09-05-2024 17:00:00;0,752;KWH;Målt;Tidsserie
        String actualFormat = meterData.asCsv( true);

        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    void givenMeterData_WhenConvertingObjectToCsvString_ThenFormatIsAsExpected() {
        String meteringPointId = "571313174001764929";
        LocalDateTime fromDateTime = LocalDateTime.of(2024, Month.MAY, 9, 16, 0);
        LocalDateTime toDateTime = LocalDateTime.of(2024, Month.MAY, 9, 17, 0);
        String hourOfDay = "1";
        String amount = "0,752";
        String measurementUnit = "KWH";
        String quality = "Målt";

        MeterDataFormatted meterData = new MeterDataFormatted(meteringPointId, fromDateTime, toDateTime, hourOfDay, amount, measurementUnit, quality);
        String expectedFormat = "571313174001764929;09-05-2024;16:00;09-05-2024;17:00;0,752;KWH;Målt;Tidsserie;1" + System.lineSeparator();

        String actualFormat = meterData.asCsv(false);

        Assertions.assertEquals(expectedFormat, actualFormat);
    }

    @Test
    void givenApiResponseWithSixResultsContainingTimeSeriesData_WhenExtractingTimeSeriesInfoToModel_ThenArrayIsExpectedSize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // This json is the http body of a response from a request to ElOverblikApiEndpoints.java#getMeterDataRawEndPoint
        String json = """
                    {"result":[{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174001764929","businessType":"A01","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174001764929"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"0.002","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174001764929","stackTrace":null},{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174001765605","businessType":"A04","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174001765605"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"2.042","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"3.759","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"2.984","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"2.516","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"3.541","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"1.424","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"3.231","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"2.951","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"2.226","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"2.407","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"2.355","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"5.094","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"4.521","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"1.665","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"2.184","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"1.643","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"2.23","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"2.921","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"2.506","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"2.81","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"2.291","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"2.567","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"1.235","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"2.169","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174001765605","stackTrace":null},{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174001765612","businessType":"A01","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174001765612"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"0.002","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174001765612","stackTrace":null},{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174002608215","businessType":"A04","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174002608215"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"10.929","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"0.0","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174002608215","stackTrace":null},{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174002840721","businessType":"A04","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174002840721"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"2.042","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"3.759","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"2.984","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"2.516","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"3.541","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"1.424","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"3.231","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"2.951","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"2.226","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"2.407","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"2.355","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"5.094","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"4.521","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"1.663","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"2.184","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"1.643","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"2.23","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"2.921","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"2.506","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"2.81","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"2.291","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"2.567","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"1.235","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"2.169","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174002840721","stackTrace":null},{"MyEnergyData_MarketDocument":{"mRID":"0HN5HTJO031U1:00000158","createdDateTime":"2024-08-01T09:22:07Z","sender_MarketParticipant.name":"","sender_MarketParticipant.mRID":{"codingScheme":null,"name":null},"period.timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"TimeSeries":[{"mRID":"571313174200537942","businessType":"A04","curveType":"A01","measurement_Unit.name":"KWH","MarketEvaluationPoint":{"mRID":{"codingScheme":"A10","name":"571313174200537942"}},"Period":[{"resolution":"PT1H","timeInterval":{"start":"2024-02-17T23:00:00Z","end":"2024-02-18T23:00:00Z"},"Point":[{"position":"1","out_Quantity.quantity":"2.042","out_Quantity.quality":"A04"},{"position":"2","out_Quantity.quantity":"3.759","out_Quantity.quality":"A04"},{"position":"3","out_Quantity.quantity":"2.984","out_Quantity.quality":"A04"},{"position":"4","out_Quantity.quantity":"2.516","out_Quantity.quality":"A04"},{"position":"5","out_Quantity.quantity":"3.541","out_Quantity.quality":"A04"},{"position":"6","out_Quantity.quantity":"1.424","out_Quantity.quality":"A04"},{"position":"7","out_Quantity.quantity":"3.231","out_Quantity.quality":"A04"},{"position":"8","out_Quantity.quantity":"2.951","out_Quantity.quality":"A04"},{"position":"9","out_Quantity.quantity":"2.226","out_Quantity.quality":"A04"},{"position":"10","out_Quantity.quantity":"2.407","out_Quantity.quality":"A04"},{"position":"11","out_Quantity.quantity":"2.355","out_Quantity.quality":"A04"},{"position":"12","out_Quantity.quantity":"5.094","out_Quantity.quality":"A04"},{"position":"13","out_Quantity.quantity":"4.521","out_Quantity.quality":"A04"},{"position":"14","out_Quantity.quantity":"1.665","out_Quantity.quality":"A04"},{"position":"15","out_Quantity.quantity":"2.184","out_Quantity.quality":"A04"},{"position":"16","out_Quantity.quantity":"1.643","out_Quantity.quality":"A04"},{"position":"17","out_Quantity.quantity":"2.23","out_Quantity.quality":"A04"},{"position":"18","out_Quantity.quantity":"2.921","out_Quantity.quality":"A04"},{"position":"19","out_Quantity.quantity":"2.506","out_Quantity.quality":"A04"},{"position":"20","out_Quantity.quantity":"2.81","out_Quantity.quality":"A04"},{"position":"21","out_Quantity.quantity":"2.291","out_Quantity.quality":"A04"},{"position":"22","out_Quantity.quantity":"2.567","out_Quantity.quality":"A04"},{"position":"23","out_Quantity.quantity":"1.235","out_Quantity.quality":"A04"},{"position":"24","out_Quantity.quantity":"2.169","out_Quantity.quality":"A04"}]}]}]},"success":true,"errorCode":10000,"errorText":"NoError","id":"571313174200537942","stackTrace":null}]}
                """;
        int resultsInResponse = 6;
        int pointsInTimeSeries = 24;
        int expectedSize = resultsInResponse * pointsInTimeSeries;
        MyEnergyDataMarketDocumentResponseListApiResponse responseBody = mapper.readValue(json, MyEnergyDataMarketDocumentResponseListApiResponse.class);

        List<MeterDataFormatted> actual = new ArrayList<>();
        for (MyEnergyDataMarketDocumentResponse energyDataDocument : responseBody.result()) {
            actual.addAll(MeterDataFormatted.from(energyDataDocument.myEnergyDataMarketDocument().timeSeries()));
        }

        Assertions.assertEquals(expectedSize, actual.size());
    }

    @Test
    void givenMeterDataCsvStringInOriginalFormat_WhenCreatingMeterDataFormattedObject_ThenCsvDataLinesMatchListSize() {
        String meterDataCsvAsGivenByApi = """
                Målepunkt id;Fra dato;Til dato;Mængde;Måleenhed;Kvalitet;Type
                571313174001764929;01-05-2024 23:00:00;02-05-2024 00:00:00;0,065;KWH;Målt;Tidsserie
                571313174001764929;01-05-2024 00:00:00;01-05-2024 01:00:00;0,053;KWH;Målt;Tidsserie
                571313174001764929;01-05-2024 01:00:00;01-05-2024 02:00:00;0,0;KWH;Målt;Tidsserie
                571313174001764929;01-05-2024 02:00:00;01-05-2024 03:00:00;0,0;KWH;Målt;Tidsserie
                571313174001764929;01-05-2024 03:00:00;01-05-2024 04:00:00;0,0;KWH;Målt;Tidsserie
                571313174200537942;02-05-2024 22:00:00;02-05-2024 23:00:00;0,095;KWH;Målt;Tidsserie""";
        int expectedSize = 6;

        List<MeterDataFormatted> actual = MeterDataFormatted.parseFrom(meterDataCsvAsGivenByApi);

        Assertions.assertEquals(expectedSize, actual.size());
    }
    @Test
    void givenMeterDataCsvStringInOriginalFormat_WhenCreatingMeterDataFormattedObject_ThenCsvDataIsConvertedCorrectly() {
        String meterDataCsvAsGivenByApi = """
                Målepunkt id;Fra dato;Til dato;Mængde;Måleenhed;Kvalitet;Type
                571313174001764929;01-05-2024 23:00:00;02-05-2024 00:00:00;0,065;KWH;Målt;Tidsserie
                """;
        int expectedListSize = 1;
        String expectedId = "571313174001764929";
        LocalDateTime expectedFromDateTime = LocalDateTime.of(2024, Month.MAY, 1, 23, 0);
        LocalDateTime expectedToDateTime = LocalDateTime.of(2024, Month.MAY, 2, 0, 0);
        String expectedMeasurementUnit = "KWH";
        String expectedDataQuality = "Målt";

        List<MeterDataFormatted> actualData = MeterDataFormatted.parseFrom(meterDataCsvAsGivenByApi);
        MeterDataFormatted actualDataOne = actualData.get(0);

        Assertions.assertEquals(expectedListSize, actualData.size());
        Assertions.assertEquals(expectedId, actualDataOne.meteringPointId());
        Assertions.assertEquals(expectedFromDateTime, actualDataOne.fromDateTime());
        Assertions.assertEquals(expectedToDateTime, actualDataOne.toDateTime());
        Assertions.assertEquals(expectedMeasurementUnit, actualDataOne.measurementUnit());
        Assertions.assertEquals(expectedDataQuality, actualDataOne.quality());
    }
}
