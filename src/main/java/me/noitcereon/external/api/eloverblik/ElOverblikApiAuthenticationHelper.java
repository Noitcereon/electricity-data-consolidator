package me.noitcereon.external.api.eloverblik;

import me.noitcereon.external.api.eloverblik.data.access.DataAccessTokenManager;

import java.net.http.HttpRequest;

public class ElOverblikApiAuthenticationHelper {
    private ElOverblikApiAuthenticationHelper() {
        // Don't instantiate static helper class.
    }

    /**
     * Adds ElOverblikApi Authorization Bearer token to the HttpRequest being built and returns the built HttpRequest.
     *
     * @param httpRequestBuilder An HTTP Request Builder that is only missing auth header to be complete.
     * @return A {@link HttpRequest} with HTTP "Authorization" header.
     */
    public static HttpRequest addAuthHeader(HttpRequest.Builder httpRequestBuilder) {
        DataAccessTokenManager dataAccessTokenManager = new DataAccessTokenManager();

        httpRequestBuilder.header("Authorization", "Bearer " + dataAccessTokenManager.retrieveDataAccessToken());
        return httpRequestBuilder.build();
    }

}
