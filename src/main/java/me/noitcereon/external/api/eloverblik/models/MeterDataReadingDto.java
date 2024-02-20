package me.noitcereon.external.api.eloverblik.models;

import java.util.Optional;

@EloverblikApiModel
public record MeterDataReadingDto(String readingDate, String registrationDate, String meterNumber, String meterReading,
                                  String measurementUnit) {

}
