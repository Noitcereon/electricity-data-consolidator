package me.noitcereon.external.api.eloverblik;

import me.noitcereon.external.api.eloverblik.data.access.DataAccessTokenManager;
import me.noitcereon.external.api.auth.AuthProvider;

import java.net.http.HttpRequest;

/**
 * Adds Authorization header using the ElOverblik Data Access Token.
 */
public class ElOverblikTokenAuthProvider implements AuthProvider {
    private final DataAccessTokenManager dataAccessTokenManager;

    public ElOverblikTokenAuthProvider() {
        this(new DataAccessTokenManager());
    }

    public ElOverblikTokenAuthProvider(DataAccessTokenManager dataAccessTokenManager) {
        this.dataAccessTokenManager = dataAccessTokenManager;
    }

    @Override
    public HttpRequest.Builder authenticate(HttpRequest.Builder builder) {
        String token = dataAccessTokenManager.retrieveDataAccessToken();
        return builder.header("Authorization", "Bearer " + token);
    }
}
