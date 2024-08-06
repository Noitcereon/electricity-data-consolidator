package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@EloverblikApiModel
public record MyEnergyDataMarketDocumentResponse(boolean success, int errorCode, String errorText, String id,
                                                 String stackTrace,
                                                 @JsonProperty("MyEnergyData_MarketDocument") MyEnergyDataMarketDocument myEnergyDataMarketDocument) {
}
