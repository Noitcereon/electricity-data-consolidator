package me.noitcereon.external.api.orchestration;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * The result of an api call.
 * @param endpointCalled
 * @param httpStatusCode
 * @param responseBody
 * @param <B> Type of the response body.
 */
public record ApiCallResult<B>(HttpResponse<B> httpResponse, Optional<B> responseBody, URI endpointCalled, int httpStatusCode) {
    public boolean isSuccess() {
        return httpStatusCode >= 200 && httpStatusCode < 300;
    }
}
