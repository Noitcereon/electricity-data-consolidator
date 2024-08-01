package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@EloverblikApiModel
public record Point(String position, @JsonProperty("out_Quantity.quantity") String outQuantity,
                    @JsonProperty("out_Quantity.quality") String outQuantityQuality) {
}
