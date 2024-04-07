package me.noitcereon.external.api.eloverblik.data.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.ConfigurationLoader;
import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.external.api.eloverblik.ElOverblikApiAuthenticationHelper;
import me.noitcereon.external.api.eloverblik.ElOverblikApiEndpoint;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeterDataReadingsDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    public Optional<List<MeterDataReadingsDto>> getMeterDataInPeriod(LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregation) {
        // TODO implement getMeterDataInPeriod
        LOG.warn("Method not implemented.");

        return Optional.empty();
    }

    public MethodOutcome getMeterDataInPeriodAsCsv(List<MeteringPointApiDto> meteringPointApiDtos, LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit) {
        try {
            String endpoint = ElOverblikApiEndpoint.getMeterDataCsvEndPoint(dateFrom, dateTo, aggregationUnit);

            // Build request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(endpoint));
            ObjectMapper objectMapper = new ObjectMapper();
            MeteringPointsRequest requestBody = MeteringPointsRequest.from(meteringPointApiDtos);
            String requestBodyJson = objectMapper.writeValueAsString(requestBody);
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
            requestBuilder.header("accept", "text/csv");
            requestBuilder.header("Content-Type", "application/json");
            HttpRequest request = ElOverblikApiAuthenticationHelper.addAuthHeader(requestBuilder);

            // Prepare for file for response
            Path fileDirectory = Path.of(System.getProperty("user.dir"), "dataFromApi");
            Files.createDirectory(fileDirectory);
            String uniqueFileName = "meterdata" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hhmmss", Locale.ROOT)) + ".csv";
            Path csvFilePath = Files.createFile(Path.of(fileDirectory.toString(), uniqueFileName));

            HttpResponse<Path> response = httpClient.send(request, HttpResponse.BodyHandlers.ofFile(csvFilePath, StandardOpenOption.WRITE));
            if (response.statusCode() == 200) {
                LOG.info("Success!");
                return MethodOutcome.SUCCESS;
            }
            LOG.warn("The request {} failed to get a successful response. Request body: {}", request, requestBodyJson);
            LOG.warn("Response to request: {}", response);
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return MethodOutcome.FAILURE;
    }
}
