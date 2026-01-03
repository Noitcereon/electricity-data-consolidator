package me.noitcereon.external.api.orchestration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Default implementation of the central HTTP orchestrator.
 * Uses a shared HttpClient and an ordered chain of ApiCallStrategy filters.
 */
public class DefaultHttpOrchestrator implements HttpOrchestrator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpOrchestrator.class);

    private final HttpClient httpClient;
    private final List<ApiCallStrategy> strategies;

    public DefaultHttpOrchestrator(HttpClient httpClient, List<ApiCallStrategy> strategies) {
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.strategies = new ArrayList<>(Objects.requireNonNull(strategies, "strategies"));
    }

    public static DefaultHttpOrchestrator getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DefaultHttpOrchestrator INSTANCE = new DefaultHttpOrchestrator(
                HttpClient.newHttpClient(),
                List.of(new LogRequestResponseStrategy())
        );
    }

    @Override
    public <T> ApiCallResult<T> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws InterruptedException {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(bodyHandler, "bodyHandler");

        // before hooks
        for (ApiCallStrategy s : strategies) {
            try {
                s.beforeApiCall(request);
            } catch (Exception e) {
                LOG.warn("ApiCallStrategy.beforeApiCall threw exception: {}", e.toString());
            }
        }


        try {
            HttpResponse<T> response = httpClient.send(request, bodyHandler);

            // after hooks
            for (ApiCallStrategy s : strategies) {
                executeAfterApiCallStrategy(request, s, response);
            }

            return new ApiCallResult<>(response, Optional.ofNullable(response.body()), request.uri(), response.statusCode());
        } catch (IOException ioe) {
            for (ApiCallStrategy s : strategies) {
                try {
                    s.onError(request, ioe);
                } catch (Exception e) {
                    LOG.warn("ApiCallStrategy.onError threw exception: {}", e.toString());
                }
            }
            throw new ElectricityConsolidatorRuntimeException(ioe);
        }
    }

    private static <T> void executeAfterApiCallStrategy(HttpRequest request, ApiCallStrategy s, HttpResponse<T> response) {
        try {
            s.afterApiCall(request, response);
        } catch (Exception e) {
            LOG.warn("ApiCallStrategy.afterApiCall threw exception: {}", e.toString());
        }
    }

    /**
     * Convenience factory to create a temporary orchestrator using a provided strategy chain.
     */
    public static DefaultHttpOrchestrator withStrategies(List<ApiCallStrategy> strategies) {
        return new DefaultHttpOrchestrator(HttpClient.newHttpClient(), strategies);
    }
}
