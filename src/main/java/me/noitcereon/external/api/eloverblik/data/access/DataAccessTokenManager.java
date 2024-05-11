package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.exceptions.MissingApiKeyException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.models.StringApiResponse;
import me.noitcereon.utilities.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Optional;

public class DataAccessTokenManager {
    private static final Logger LOG = LoggerFactory.getLogger(DataAccessTokenManager.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;

    public DataAccessTokenManager() {
        configSaver = SimpleConfigSaver.getInstance();
        configLoader = SimpleConfigLoader.getInstance();
    }
    public DataAccessTokenManager(ConfigurationSaver configSaver, ConfigurationLoader configLoader) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
    }


    public String retrieveDataAccessToken() {
        String dataAccessToken;
        if (shouldUseCachedDataAccessToken()) {
            dataAccessToken = configLoader.getDataAccessToken();
            return dataAccessToken;
        }
        try {
            URI getDataAccessTokenEndPoint = new URI(ElOverblikApiEndpoint.DATA_ACCESS_TOKEN);
            HttpClient httpClient = HttpClient.newHttpClient();

            String apiKey = configLoader.getApiKey();
            if(apiKey.isEmpty()){
                throw new MissingApiKeyException("Could not retrieve El Overblik API key. Have you updated /config/api-key.conf? See README.md if in doubt about what it needs.");
            }
            HttpRequest httpRequest =
                    HttpRequest.newBuilder(getDataAccessTokenEndPoint)
                            .header("Authorization", "Bearer " + configLoader.getApiKey())
                            .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                ElectricityConsolidatorRuntimeException e = new ElectricityConsolidatorRuntimeException("Could not retrieve data access token.");
                String message = e.getMessage() + "Response from eloverblik API is: " + response;
                LOG.error(message, e);
            }
            ObjectMapper mapper = new ObjectMapper();
            StringApiResponse responseObject = mapper.readValue(response.body(), StringApiResponse.class);
            configSaver.saveProperty(ConfigurationKeys.DATA_ACCESS_TOKEN, responseObject.getResult());
            configSaver.saveProperty(ConfigurationKeys.LAST_DATA_ACCESS_REFRESH, LocalDate.now().format(DateConverter.DEFAULT_DATE_FORMAT));
            return responseObject.getResult();
        } catch (URISyntaxException e) {
            LOG.error("Invalid URI");
        } catch (IOException e) {
            LOG.error("IOException was thrown", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ElectricityConsolidatorRuntimeException(e);
        }
        throw new ElectricityConsolidatorRuntimeException("Could not retrieve data access token.");
    }
    private boolean shouldUseCachedDataAccessToken() {
        Optional<String> lastAccessDate = configLoader.getProperty(ConfigurationKeys.LAST_DATA_ACCESS_REFRESH);
        if (lastAccessDate.isEmpty()) return false;
        LocalDate currentTime = LocalDate.now();
        LocalDate convertedLastAccessDate = DateConverter.toLocalDate(lastAccessDate.get());
        if (convertedLastAccessDate.getYear() != currentTime.getYear()) return false;
        return convertedLastAccessDate.getDayOfYear() == currentTime.getDayOfYear();
    }
}
