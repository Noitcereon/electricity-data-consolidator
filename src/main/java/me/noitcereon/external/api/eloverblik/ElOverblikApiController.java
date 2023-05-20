package me.noitcereon.external.api.eloverblik;


import me.noitcereon.configuration.*;
import me.noitcereon.external.api.eloverblik.data.access.DataAccessTokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final DataAccessTokenManager dataAccessTokenManager;
    private static final Logger LOG = LoggerFactory.getLogger(ElOverblikApiController.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;

    public ElOverblikApiController() {
        configSaver = SimpleConfigSaver.getInstance();
        configLoader = SimpleConfigLoader.getInstance();
        dataAccessTokenManager = new DataAccessTokenManager();
    }
    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        dataAccessTokenManager = new DataAccessTokenManager(configSaver, configLoader);
    }

    // Constructor intended for testing (make everything mockable)
    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader, DataAccessTokenManager dataAccessTokenManager) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.dataAccessTokenManager = dataAccessTokenManager;
    }

    public String retrieveDataAccessToken(){
        return dataAccessTokenManager.retrieveDataAccessToken();
    }
}
