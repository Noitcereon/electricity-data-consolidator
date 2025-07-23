package me.noitcereon.external.api.orchestration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class LogRequestResponseStrategy implements ApiCallStrategy {

    private final UUID apiCallId = UUID.randomUUID();

    private static final Logger LOG = LoggerFactory.getLogger(LogRequestResponseStrategy.class);

    @Override
    public void beforeApiCall(HttpRequest request) {
        LOG.info("ApiCall({}): Calling {}", apiCallId, request.uri());
    }

    @Override
    public <T> void afterApiCall(HttpRequest request, HttpResponse<T> response) {
        boolean isSuccess = response.statusCode() > 199 && response.statusCode() < 300;

        boolean hasBody = response.body() != null;
        String isBodyPresentMsg = hasBody ? "Yes" : "No";

        LOG.info("ApiCall({}): Response info regarding call to {}: httpStatus={} hasBody={}", apiCallId, request.uri(), response.statusCode(), isBodyPresentMsg);
        LOG.info("ApiCall({}): Response body: {}", apiCallId, response.body());
        if (!isSuccess) {
            LOG.warn("ApiCall({}): Returned a non-success status code.", apiCallId);
        }
    }
}
