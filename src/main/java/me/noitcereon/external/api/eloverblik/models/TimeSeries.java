package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@EloverblikApiModel
public record TimeSeries(@JsonProperty("mRID") String mRid, String businessType, String curveType,
                         @JsonProperty("measurement_Unit.name") String measurementUnitName,
                         @JsonProperty("MarketEvaluationPoint") MarketEvaluationPoint marketEvaluationPoint,
                         @JsonProperty("Period") List<Period> period) {
}
