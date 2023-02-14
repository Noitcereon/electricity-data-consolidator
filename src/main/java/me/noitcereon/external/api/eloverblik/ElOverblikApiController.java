package me.noitcereon.external.api.eloverblik;


import me.noitcereon.configuration.ConfigLoader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Handles all calls to the "El Overblik" API.
 *
 * @author Noitcereon
 * @see <a href="https://api.eloverblik.dk/CustomerApi/index.html">El Overblik API Swagger</a>
 * @since 0.0.1
 */
public class ElOverblikApiController {
    // TODO: look into how Java can call API via HttpClient
    // TODO: Call El Overblik to get electricity data for a given household.
    private ConfigLoader configLoader = ConfigLoader.getInstance();

    public void retrieveDataAccessToken() {
        try {
            // TODO: refactor this method
            // TODO: restrict access to this method and cache the data access key for the remainder of the day.
            URI getDataAccessTokenEndPoint = new URI("https://api.eloverblik.dk/customerapi/api/token");
            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest httpRequest =
                    HttpRequest.newBuilder(getDataAccessTokenEndPoint)
                    .header("Authorization", "Bearer " + configLoader.getProperty("eloverblik-api-key"))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (URISyntaxException e) {
            System.err.println("Invalid URI");
        } catch (IOException e) {
            System.err.println(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
