package me.noitcereon.external.api.orchestration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * A class for making HTTP calls, logging them and returning a custom record ({@link ApiCallResult}) with info from the response.
 *
 * @apiNote The intent behind this class is for all API calls to go through this orchestrator, so it can get logged in the same manner (and any other potential "always do this" for API calls)
 */
public class ApiCallOrchestrator {

    private ApiCallOrchestrator() {
        // No need to instantiate helper class.
    }

    /**
     * Executes an api call with the {@link LogRequestResponseStrategy}.
     *
     * @param request
     * @param bodyHandler
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public static <T> ApiCallResult<T> executeApiCall(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws InterruptedException {
        return executeApiCall(request, bodyHandler, new LogRequestResponseStrategy());
    }

    /**
     * Executes an api call with the given {@link ApiCallStrategy}.
     *
     * @param request
     * @param bodyHandler
     * @param apiCallStrategy
     * @param <T>
     * @return
     * @throws InterruptedException
     */
    public static <T> ApiCallResult<T> executeApiCall(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler, ApiCallStrategy apiCallStrategy) throws InterruptedException {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();

            apiCallStrategy.beforeApiCall(request);
            HttpResponse<T> response = httpClient.send(request, bodyHandler);
            apiCallStrategy.afterApiCall(request, response);

            return new ApiCallResult<>(response, Optional.ofNullable(response.body()), request.uri(), response.statusCode());
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }
    }
}
