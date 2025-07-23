package me.noitcereon.external.api.orchestration;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface ApiCallStrategy {
    void beforeApiCall(HttpRequest request);

    <T> void afterApiCall(HttpRequest request, HttpResponse<T> response);
}
