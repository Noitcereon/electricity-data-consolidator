package me.noitcereon.external.api.orchestration;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * A class for making HTTP calls, logging them and returning a custom record ({@link ApiCallResult}) with info from the response.
 *
 * @apiNote Kept for backward compatibility. Internally delegates to {@link DefaultHttpOrchestrator}.
 * @deprecated Use {@link HttpOrchestrator} and {@link DefaultHttpOrchestrator} instead.
 */
@Deprecated(since = "0.3.1")
public class ApiCallOrchestrator {

    private ApiCallOrchestrator() {
        // No need to instantiate helper class.
    }

    /**
     * Executes an api call with the {@link LogRequestResponseStrategy}.
     */
    public static <T> ApiCallResult<T> executeApiCall(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws InterruptedException {
        return DefaultHttpOrchestrator.getInstance().send(request, bodyHandler);
    }

    /**
     * Executes an api call with the given {@link ApiCallStrategy}.
     */
    public static <T> ApiCallResult<T> executeApiCall(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler, ApiCallStrategy apiCallStrategy) throws InterruptedException {
        return DefaultHttpOrchestrator.withStrategies(List.of(apiCallStrategy)).send(request, bodyHandler);
    }
}
