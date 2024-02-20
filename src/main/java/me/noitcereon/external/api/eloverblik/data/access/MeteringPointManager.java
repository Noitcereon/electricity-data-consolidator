package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.configuration.ConfigurationLoader;
import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDtoListApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class MeteringPointManager {
    private ConfigurationLoader configLoader;
    private static final Logger LOG = LoggerFactory.getLogger(MeteringPointManager.class);

    public MeteringPointManager() {
        configLoader = SimpleConfigLoader.getInstance();
    }

    public MeteringPointManager(ConfigurationLoader configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * @param includeAll When includeAll is false, only the metering points with relations are returned.
     *                   When includeAll is true the list will be a merge of the related metering points with a lookup
     *                   using CPR or CVR. CPR is used if you are private user.
     *                   CVR is used if you access as an employee.
     * @return Returns a list of metering points.
     */
    public Optional<List<MeteringPointApiDto>> getMeteringPoints(boolean includeAll) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ElOverblikApiEndpoint.METERING_POINTS + "?includeAll=" + includeAll))
                .GET()
                .header("Authorization", "Bearer " + configLoader.getDataAccessToken())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            LOG.warn("Request to endpoint '{}' did not return 200 OK response. It instead returned HTTP Status '{}'", request.uri(), response.statusCode());
            return Optional.empty();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MeteringPointApiDtoListApiResponse responseBody = mapper.readValue(response.body(), MeteringPointApiDtoListApiResponse.class);

        return Optional.of(responseBody.getResult());
    }
}
