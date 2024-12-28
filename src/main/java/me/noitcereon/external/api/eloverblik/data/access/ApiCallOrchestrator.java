package me.noitcereon.external.api.eloverblik.data.access;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;

/**
 * A class for making HTTP calls, logging them and returning a custom record ({@link ApiCallResult}) with info from the response.
 * @apiNote The intent behind this class is for all API calls to go through this orchestrator, so it can get logged in the same manner (and any other potential "always do this" for API calls)
 */
public class ApiCallOrchestrator {

    private static final Logger LOG = LoggerFactory.getLogger(ApiCallOrchestrator.class);
    private ApiCallOrchestrator() {
        // No need to instantiate a helper class.
    }

    public static <T> ApiCallResult<T> executeApiCallAndLogIt(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws InterruptedException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            UUID requestId = UUID.randomUUID(); // To help identify which response belongs to the request in case of many requests in the logs.

            LOG.info("ApiCall({}): Calling {}", requestId, request.uri());

            HttpResponse<T> response = httpClient.send(request, bodyHandler);
            ApiCallResult<T> apiCallResult = new ApiCallResult<>(request.uri(), response.statusCode(), Optional.ofNullable(response.body()));
            String isBodyPresent = apiCallResult.responseBody.isPresent() ? "Yes" : "No";

            LOG.info("ApiCall({}): Response info regarding call to {}: httpStatus={} hasBody={}", requestId, request.uri(), apiCallResult.httpStatusCode, isBodyPresent);

            return apiCallResult;
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }
    }

    /**
     * Temporary main method for manual testing.
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://google.com"))
                .GET()
                .build();

        ApiCallResult<String> result = executeApiCallAndLogIt(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(result);
    }

    public record ApiCallResult<B>(URI endpointCalled, int httpStatusCode,
                                   Optional<B> responseBody) {
        public boolean isSuccess() {
            return httpStatusCode >= 200 && httpStatusCode > 300;
        }
    }
}
