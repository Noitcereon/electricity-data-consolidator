package me.noitcereon.external.api.orchestration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class ApiCallOrchestratorTest {

    @Test
    @Disabled("This test is disabled because it is only intended for manual testing.")
    void executeApiCall() throws InterruptedException {
        String arbitraryEndpointThatCanBeReached = "https://google.com";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(arbitraryEndpointThatCanBeReached))
                .GET()
                .build();

        ApiCallResult<String> result = ApiCallOrchestrator.executeApiCall(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(result);
        System.out.println(result);
    }
}