package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.ConfigurationLoader;
import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.ElOverblikApiAuthenticationHelper;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.*;
import me.noitcereon.utilities.FileNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This class is used to fetch MeterData from the ElOverblikApi, in one form or another.
 */
public class MeterDataManager {
    private final ConfigurationLoader configLoader;
    private static final Logger LOG = LoggerFactory.getLogger(MeterDataManager.class);
    private HttpClient httpClient;

    public MeterDataManager() {
        configLoader = SimpleConfigLoader.getInstance();
        httpClient = HttpClient.newHttpClient();
    }

    public MeterDataManager(ConfigurationLoader configLoader) {
        this.configLoader = configLoader;
    }

    public Optional<List<MeterDataReadingsDto>> getMeterDataInPeriod(LocalDate dateFrom, LocalDate dateTo) {
        // TODO implement getMeterDataInPeriod
        LOG.warn("Method not implemented.");

        return Optional.empty();
    }

    /**
     * This fetches the raw API response for usage in app if needed.
     * @param meteringPointsRequestBody
     * @param dateFrom
     * @param dateTo
     * @param aggregationUnit
     * @return Raw api response encapsualted in an Optional.
     */
    public Optional<MyEnergyDataMarketDocumentResponseListApiResponse> fetchEnergyDataMarketDocument(MeteringPointsRequest meteringPointsRequestBody, LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit) {
        try{
            String endpoint = ElOverblikApiEndpoint.getMeterDataRawEndPoint(dateFrom, dateTo, aggregationUnit);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyJson = objectMapper.writeValueAsString(meteringPointsRequestBody);
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
            requestBuilder.header("Content-Type", "application/json");
            HttpRequest request = ElOverblikApiAuthenticationHelper.addAuthHeader(requestBuilder);
            LOG.info("Sending request to ElOverblikApi. Request: {}", request);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200 || response.statusCode() < 300){
                LOG.info("Successful request to {}", request.uri());
                LOG.info(response.body());
                MyEnergyDataMarketDocumentResponseListApiResponse responseBody = objectMapper.readValue(response.body(), MyEnergyDataMarketDocumentResponseListApiResponse.class);

                return Optional.of(responseBody);
            }
            else{
                throw new ElectricityConsolidatorRuntimeException("Failed to fetch meterdata... API response code: " + response.statusCode());
            }
        }
        catch (InterruptedException intEx){
            Thread.currentThread().interrupt();
        }
        catch (Exception e){
            LOG.error("Exception occurred when sending request to ElOverblikApi", e);
        }
        return Optional.empty(); // Something went wrong.
    }

    public MethodOutcome getMeterDataInPeriodAsCsv(MeteringPointsRequest requestBody, LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit) {
        try {
            String endpoint = ElOverblikApiEndpoint.getMeterDataCsvEndPoint(dateFrom, dateTo, aggregationUnit);

            // Build request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
            requestBuilder.header("accept", "text/csv");
            requestBuilder.header("Content-Type", "application/json");
            HttpRequest request = ElOverblikApiAuthenticationHelper.addAuthHeader(requestBuilder);

            // Prepare for file for response
            Path fileDirectory = Path.of(System.getProperty("user.dir"), "dataFromApi");
            if(!fileDirectory.toFile().exists()){
                Files.createDirectory(fileDirectory);
            }

            Path filePath = Path.of(fileDirectory.toString(), FileNameGenerator.meterDataCsvFile(dateFrom, dateTo));
            if(filePath.toFile().exists()){
                // We already have the data, so no need to fetch it a second time.
                return MethodOutcome.SUCCESS;
            }
            Path csvFilePath = Files.createFile(filePath);

            HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(csvFilePath, StandardOpenOption.WRITE));
            if (response.statusCode() == 200) {
                LOG.info("Success!");
                return MethodOutcome.SUCCESS;
            }
            LOG.warn("The request {} failed to get a successful response. Request body: {}", request, requestBodyJson);
            LOG.warn("Response to request: {}", response);
        }
        catch (FileAlreadyExistsException ex){
            LOG.info("You've already fetched data for this period (see file: '%s'".formatted(ex.getFile()));
        }
        catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return MethodOutcome.FAILURE;
    }
    public Optional<String> fetchMeterDataInPeriodAsCsvString(MeteringPointsRequest httpRequestBody, LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit) {
        try {
            String endpoint = ElOverblikApiEndpoint.getMeterDataCsvEndPoint(dateFrom, dateTo, aggregationUnit);

            // Build request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBodyJson = objectMapper.writeValueAsString(httpRequestBody);
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
            requestBuilder.header("accept", "text/csv");
            requestBuilder.header("Content-Type", "application/json");
            HttpRequest request = ElOverblikApiAuthenticationHelper.addAuthHeader(requestBuilder);

            LOG.info("Sending ElOverblikApi HTTP request to {} ", request.uri());
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            LOG.info("Response from ElOverblikApi HTTP request to {} resulted in status code {}", request.uri(), response.statusCode());
            if (response.statusCode() == 200) {
                LOG.info("Success!");
                return Optional.of(response.body());
            }
            LOG.warn("The request {} failed to get a successful response. Request body: {}", request, requestBodyJson);
            LOG.warn("Response to request: {}", response);
        }
        catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return Optional.empty(); // Something went wrong when fetching data.
    }
}
