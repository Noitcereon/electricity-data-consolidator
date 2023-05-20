package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MeteringPointApiDtoListApiResponseTest {

    @Test
    void givenTypicalApiResponseJson_WhenSerializingToJavaObject_ThenOutputIsNotNull() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        String json = """
                    {"result":[{"streetCode":"1324","streetName":"Sadelmagervej","buildingNumber":"2","floorId":null,"roomId":null,"citySubDivisionName":"Kvarmløse","municipalityCode":"316","locationDescription":null,"settlementMethod":null,"meterReadingOccurrence":"PT1H","firstConsumerPartyName":"Michael Bækgaard Nielsen","secondConsumerPartyName":null,"meterNumber":null,"consumerStartDate":"2018-09-16T22:00:00.000Z","meteringPointId":"571313174001764929","typeOfMP":"E18","balanceSupplierName":"Vindstød A/S","postcode":"4340","cityName":"Tølløse","hasRelation":true,"consumerCVR":null,"dataAccessCVR":null,"childMeteringPoints":[]},{"streetCode":"1324","streetName":"Sadelmagervej","buildingNumber":"2","floorId":null,"roomId":null,"citySubDivisionName":"Kvarmløse","municipalityCode":"316","locationDescription":null,"settlementMethod":"E02","meterReadingOccurrence":"PT1H","firstConsumerPartyName":"Michael Bækgaard Nielsen","secondConsumerPartyName":null,"meterNumber":null,"consumerStartDate":"2012-12-04T23:00:00.000Z","meteringPointId":"571313174200537942","typeOfMP":"E17","balanceSupplierName":"Vindstød A/S","postcode":"4340","cityName":"Tølløse","hasRelation":true,"consumerCVR":null,"dataAccessCVR":null,"childMeteringPoints":[{"parentMeteringPointId":"571313174200537942","meteringPointId":"571313174001765605","typeOfMP":"D07","meterReadingOccurrence":"PT1H","meterNumber":"353240"},{"parentMeteringPointId":"571313174200537942","meteringPointId":"571313174001765612","typeOfMP":"D06","meterReadingOccurrence":"PT1H","meterNumber":"353240"},{"parentMeteringPointId":"571313174200537942","meteringPointId":"571313174002608215","typeOfMP":"D14","meterReadingOccurrence":"PT1H","meterNumber":""}]}]}
                """;
        MeteringPointApiDtoListApiResponse responseBody = mapper.readValue(json, MeteringPointApiDtoListApiResponse.class);
        Assertions.assertNotNull(responseBody);
    }
}