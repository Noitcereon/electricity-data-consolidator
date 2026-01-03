package me.noitcereon.external.api.eloverblik;

import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.external.api.auth.AuthProvider;

import java.net.http.HttpRequest;

/**
 * Adds Authorization header using the static API key (for obtaining Data Access Token).
 */
public class ElOverblikApiKeyAuthProvider implements AuthProvider {
    @Override
    public HttpRequest.Builder authenticate(HttpRequest.Builder builder) {
        String apiKey = SimpleConfigLoader.getInstance().getApiKey();
        return builder.header("Authorization", "Bearer " + apiKey);
    }
}
