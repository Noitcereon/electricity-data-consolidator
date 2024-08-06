package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @param resolution   Describes what the `point` parameter represents. An example would be "PT1H", which is Hour. (a more apt name for resolution would be "periodType")
 * @param timeInterval An object describing start and end time of the period.
 * @param points        A list of data points.
 * @see <a href="https://energinet.dk/media/4beb0pgg/customer-and-third-party-api-for-datahub-eloverblik-data-description_validfrom_20240626.pdf">API Data documentation</a>
 */
@EloverblikApiModel
public record Period(String resolution, PeriodtimeInterval timeInterval, @JsonProperty("Point") List<Point> points) {
}
