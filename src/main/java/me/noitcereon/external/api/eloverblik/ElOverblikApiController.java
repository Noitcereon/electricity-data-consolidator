package me.noitcereon.external.api.eloverblik;


import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.data.access.DataAccessTokenManager;
import me.noitcereon.external.api.eloverblik.data.access.MeteringPointManager;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Handles all calls to the "El Overblik" API.
 *
 * @author Noitcereon
 * @see <a href="https://api.eloverblik.dk/CustomerApi/index.html">El Overblik API Swagger</a>
 * @since 0.0.1
 */
public class ElOverblikApiController {
    private final DataAccessTokenManager dataAccessTokenManager;
    private final MeteringPointManager meteringPointManager;
    private static final Logger LOG = LoggerFactory.getLogger(ElOverblikApiController.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;

    public ElOverblikApiController() {
        configSaver = SimpleConfigSaver.getInstance();
        configLoader = SimpleConfigLoader.getInstance();
        dataAccessTokenManager = new DataAccessTokenManager();
        meteringPointManager = new MeteringPointManager();
    }
    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        dataAccessTokenManager = new DataAccessTokenManager(configSaver, configLoader);
        meteringPointManager = new MeteringPointManager();
    }

    // Constructor intended for testing (make everything mockable)
    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader, DataAccessTokenManager dataAccessTokenManager, MeteringPointManager meteringPointManager) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.dataAccessTokenManager = dataAccessTokenManager;
        this.meteringPointManager = meteringPointManager;
    }

    public String retrieveDataAccessToken(){
        return dataAccessTokenManager.retrieveDataAccessToken();
    }
    public Optional<List<MeteringPointApiDto>> getMeteringPoints(boolean includeAll){
        try {
            return meteringPointManager.getMeteringPoints(includeAll);
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        } catch (InterruptedException e) {
            LOG.warn("Call to getMeteringPoints was interrupted", e);
            Thread.currentThread().interrupt();
        }
        throw new ElectricityConsolidatorRuntimeException("Something went wrong during call to getMeteringPoints. Args: includeAll=" + includeAll);
    }
}
