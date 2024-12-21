package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.external.api.eloverblik.ElOverblikApiAuthenticationHelper;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDtoListApiResponse;
import me.noitcereon.external.api.orchestration.ApiCallOrchestrator;
import me.noitcereon.external.api.orchestration.ApiCallResult;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class MeteringPointManager {

    public MeteringPointManager() {
        // Explicit MeteringPointManager.
        // Should have sensible defaults for any required dependencies to make it easy to use.
    }

    /**
     * @param includeAll When includeAll is false, only the metering points with relations are returned.
     *                   When includeAll is true the list will be a merge of the related metering points with a lookup
     *                   using CPR or CVR. CPR is used if you are private user.
     *                   CVR is used if you access as an employee.
     * @return Returns a list of metering points.
     */
    public Optional<List<MeteringPointApiDto>> getMeteringPoints(boolean includeAll) throws IOException, InterruptedException {
               HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(ElOverblikApiEndpoint.METERING_POINTS + "?includeAll=" + includeAll))
                .GET();
        HttpRequest request = ElOverblikApiAuthenticationHelper.addAuthHeader(requestBuilder);

        ApiCallResult<String> response = ApiCallOrchestrator.executeApiCall(request, HttpResponse.BodyHandlers.ofString());

        if (!response.isSuccess()) {
            return Optional.empty();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MeteringPointApiDtoListApiResponse responseBody = mapper.readValue(response.responseBody().orElseThrow(), MeteringPointApiDtoListApiResponse.class);

        return Optional.of(responseBody.getResult());
    }
}
