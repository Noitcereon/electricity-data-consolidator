package me.noitcereon.external.api.eloverblik.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@EloverblikApiModel
public record MyEnergyDataMarketDocument(@JsonProperty("mRID") String mRid,
                                         @JsonProperty("createdDateTime") String createdDateTime,
                                         @JsonProperty("sender_MarketParticipant.name") String senderMarketParticipationName,
                                         @JsonProperty("sender_MarketParticipant.mRID") EIC senderMarketParticipationMRid,
                                         @JsonProperty("period.timeInterval") PeriodtimeInterval periodTimeInterval,
                                         @JsonProperty("TimeSeries") List<TimeSeries> timeSeries) {
}
