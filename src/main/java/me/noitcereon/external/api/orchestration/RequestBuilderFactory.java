package me.noitcereon.external.api.orchestration;

import me.noitcereon.external.api.auth.AuthProvider;

import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

/**
 * Factory for building standardized HTTP requests with common headers and authentication.
 */
public class RequestBuilderFactory {
    private final AuthProvider authProvider;

    public RequestBuilderFactory(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public HttpRequest jsonGet(URI uri) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("accept", "application/json");
        builder = authProvider.authenticate(builder);
        return builder.GET().build();
    }

    public HttpRequest jsonPost(URI uri, String bodyJson) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("accept", "application/json")
                .header("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8)
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson));
        builder = authProvider.authenticate(builder);
        return builder.build();
    }

    public HttpRequest csvPost(URI uri, String bodyJson) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("accept", "text/csv")
                .header("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8)
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson));
        builder = authProvider.authenticate(builder);
        return builder.build();
    }
}
