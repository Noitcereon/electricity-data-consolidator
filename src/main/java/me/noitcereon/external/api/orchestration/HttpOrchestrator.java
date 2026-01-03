package me.noitcereon.external.api.orchestration;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Central HTTP orchestrator interface. All outbound HTTP calls should go through this.
 */
public interface HttpOrchestrator {
    <T> ApiCallResult<T> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws InterruptedException;
}
