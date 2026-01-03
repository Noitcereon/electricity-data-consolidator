package me.noitcereon.external.api.auth;

import java.net.http.HttpRequest;

/**
 * Provides authentication for outbound HTTP requests by decorating a HttpRequest.Builder.
 */
public interface AuthProvider {
    HttpRequest.Builder authenticate(HttpRequest.Builder builder);
}
