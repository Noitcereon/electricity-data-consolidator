package me.noitcereon.external.api.eloverblik.models;

import java.util.List;

@EloverblikApiModel
public record MeterDataReadingsDto(String meteringPointId, List<MeterDataReadingDto> readings) {

}
