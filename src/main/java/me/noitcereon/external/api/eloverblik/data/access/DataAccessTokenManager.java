package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.exceptions.MissingApiKeyException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.models.StringApiResponse;
import me.noitcereon.external.api.orchestration.ApiCallResult;
import me.noitcereon.external.api.eloverblik.ElOverblikApiKeyAuthProvider;
import me.noitcereon.external.api.orchestration.DefaultHttpOrchestrator;
import me.noitcereon.external.api.orchestration.HttpOrchestrator;
import me.noitcereon.external.api.orchestration.RequestBuilderFactory;
import me.noitcereon.utilities.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Optional;

public class DataAccessTokenManager {
    private static final Logger LOG = LoggerFactory.getLogger(DataAccessTokenManager.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;
    private final HttpOrchestrator httpOrchestrator;
    private final RequestBuilderFactory requestFactory;

    public DataAccessTokenManager() {
        configSaver = SimpleConfigSaver.getInstance();
        configLoader = SimpleConfigLoader.getInstance();
        this.httpOrchestrator = DefaultHttpOrchestrator.getInstance();
        this.requestFactory = new RequestBuilderFactory(new ElOverblikApiKeyAuthProvider());
    }

    public DataAccessTokenManager(ConfigurationSaver configSaver, ConfigurationLoader configLoader) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.httpOrchestrator = DefaultHttpOrchestrator.getInstance();
        this.requestFactory = new RequestBuilderFactory(new ElOverblikApiKeyAuthProvider());
    }

    public DataAccessTokenManager(ConfigurationSaver configSaver, ConfigurationLoader configLoader,
                                  HttpOrchestrator orchestrator, RequestBuilderFactory requestFactory) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.httpOrchestrator = orchestrator;
        this.requestFactory = requestFactory;
    }

    public String retrieveDataAccessToken() {
        String dataAccessToken;
        if (shouldUseCachedDataAccessToken()) {
            dataAccessToken = configLoader.getDataAccessToken();
            return dataAccessToken;
        }
        try {
            URI getDataAccessTokenEndPoint = new URI(ElOverblikApiEndpoint.DATA_ACCESS_TOKEN);

            String apiKey = configLoader.getApiKey();
            if (apiKey.isEmpty()) {
                throw new MissingApiKeyException("Could not retrieve El Overblik API key. Have you updated /config/api-key.conf? See README.md if in doubt about what it needs.");
            }
            HttpRequest httpRequest = requestFactory.jsonGet(getDataAccessTokenEndPoint);

            ApiCallResult<String> response = httpOrchestrator.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (!response.isSuccess()) {
                switch (response.httpStatusCode()) {
                    case 429:
                        System.out.println("El Overblik API denies further request for data due to too users requesting data. Please try again later.");
                        break;
                    case 509:
                        System.out.println("El Overblik API cannot keep up with demand and is unavailable at this time ('DataHub unavailable'). Please try again later.");
                        break;
                    default:
                        ElectricityConsolidatorRuntimeException e = new ElectricityConsolidatorRuntimeException("Could not retrieve data access token.");
                        String message = e.getMessage() + "Response from eloverblik API is: " + response;
                        LOG.error(message, e);
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            StringApiResponse responseObject = mapper.readValue(response.responseBody().orElseThrow(), StringApiResponse.class);
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
