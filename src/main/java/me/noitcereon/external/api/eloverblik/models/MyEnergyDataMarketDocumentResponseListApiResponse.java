package me.noitcereon.external.api.eloverblik.models;

import java.util.List;

/**
 * A wrapper around a list of {@link MyEnergyDataMarketDocumentResponse}.
 *
 * @param result
 */
@EloverblikApiModel
public record MyEnergyDataMarketDocumentResponseListApiResponse(
        List<MyEnergyDataMarketDocumentResponse> result) {

}
