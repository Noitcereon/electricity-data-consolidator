package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents data in one or more time periods.
 *
 * @param mRid
 * @param businessType
 * @param curveType
 * @param measurementUnitName   The type of unit associated with the {@link Point#outQuantity()} within {@link Period#points()}, e.g. KWH.
 * @param marketEvaluationPoint
 * @param periods               Contains all the data and to which time period they belong.
 */
@EloverblikApiModel
public record TimeSeries(@JsonProperty("mRID") String mRid, String businessType, String curveType,
                         @JsonProperty("measurement_Unit.name") String measurementUnitName,
                         @JsonProperty("MarketEvaluationPoint") MarketEvaluationPoint marketEvaluationPoint,
                         @JsonProperty("Period") List<Period> periods) {
}
