package me.noitcereon.external.api.eloverblik.models;


import com.fasterxml.jackson.annotation.JsonProperty;

@EloverblikApiModel
public record MarketEvaluationPoint(@JsonProperty("mRID") MarketEvaluationMeteringPoint mRid) {
}
