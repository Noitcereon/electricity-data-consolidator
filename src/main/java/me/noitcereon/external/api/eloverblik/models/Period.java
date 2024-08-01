package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@EloverblikApiModel
public record Period(String resolution, PeriodtimeInterval timeInterval, @JsonProperty("Point") List<Point> point) {
}
