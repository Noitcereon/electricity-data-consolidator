package me.noitcereon.external.api.eloverblik;


import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.*;
import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import me.noitcereon.external.api.eloverblik.data.access.DataAccessTokenManager;
import me.noitcereon.external.api.eloverblik.data.access.MeterDataManager;
import me.noitcereon.external.api.eloverblik.data.access.MeteringPointManager;
import me.noitcereon.external.api.eloverblik.models.MeterDataFormatted;
import me.noitcereon.external.api.eloverblik.models.MeteringPointApiDto;
import me.noitcereon.external.api.eloverblik.models.MeteringPointsRequest;
import me.noitcereon.utilities.FileNameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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
    private final MeterDataManager meterDataManager;
    private static final Logger LOG = LoggerFactory.getLogger(ElOverblikApiController.class);
    private final ConfigurationLoader configLoader;
    private final ConfigurationSaver configSaver;

    public ElOverblikApiController() {
        configSaver = SimpleConfigSaver.getInstance();
        configLoader = SimpleConfigLoader.getInstance();
        dataAccessTokenManager = new DataAccessTokenManager();
        meteringPointManager = new MeteringPointManager();
        meterDataManager = new MeterDataManager();
    }

    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        dataAccessTokenManager = new DataAccessTokenManager(configSaver, configLoader);
        meteringPointManager = new MeteringPointManager();
        meterDataManager = new MeterDataManager();
    }

    // Constructor intended for testing (make everything mockable)
    public ElOverblikApiController(ConfigurationSaver configSaver, ConfigurationLoader configLoader,
                                   DataAccessTokenManager dataAccessTokenManager, MeteringPointManager meteringPointManager,
                                   MeterDataManager meterDataManager) {
        this.configSaver = configSaver;
        this.configLoader = configLoader;
        this.dataAccessTokenManager = dataAccessTokenManager;
        this.meteringPointManager = meteringPointManager;
        this.meterDataManager = meterDataManager;
    }

    public String retrieveDataAccessToken() {
        return dataAccessTokenManager.retrieveDataAccessToken();
    }

    public Optional<List<MeteringPointApiDto>> getMeteringPoints(boolean includeAll) {
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

    /**
     * Retrieves a csv file containing MeterData and then tries to format it to {@link MeterDataFormatted} and creates it as a csv file in dataFromApi.
     *
     * @param meteringPointsRequest Http request body containing which meteringPoints to fetch MeterData from.
     * @param dateFrom              Start date to fetch data from.
     * @param dateTo                End date to fetch data from.
     * @param timeAggregation       Most likely {@link TimeAggregation#HOUR} (at the time of writing nothing else has been used).
     * @return {@link MethodOutcome#SUCCESS} if file is created with data or already exists with data. Otherwise, {@link MethodOutcome#FAILURE}
     * @throws IOException On failure to create necessary files.
     */
    public MethodOutcome fetchMeterDataCsvCustomFormat(MeteringPointsRequest meteringPointsRequest, LocalDate dateFrom, LocalDate dateTo, TimeAggregation timeAggregation) throws IOException {
        // Prepare a file for response data.
        Path fileDirectory = Path.of(System.getProperty("user.dir"), "dataFromApi");
        if (!fileDirectory.toFile().exists()) {
            Files.createDirectory(fileDirectory);
        }
        Path filePath = Path.of(fileDirectory.toString(), FileNameGenerator.meterDataCustomFormatCsvFile(dateFrom, dateTo));
        if (filePath.toFile().exists() && filePath.toFile().length() != 0) {
            // We already have the data, so no need to fetch it a second time.
            return MethodOutcome.SUCCESS;
        }

        Optional<String> originalCsvFromApi = meterDataManager.fetchMeterDataInPeriodAsCsvString(meteringPointsRequest, dateFrom, dateTo, timeAggregation);
        if (originalCsvFromApi.isEmpty()) return MethodOutcome.FAILURE;
        List<MeterDataFormatted> formattedMeterData = MeterDataFormatted.parseFrom(originalCsvFromApi.get());
        Path csvFilePath = Files.createFile(filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath.toFile()))) {
            boolean shouldAddHeader = true;
            for (MeterDataFormatted meterDataEntry : formattedMeterData) {
                writer.write(meterDataEntry.asCsv(shouldAddHeader));
                if (shouldAddHeader) shouldAddHeader = false;
            }
        }
        return MethodOutcome.SUCCESS;
    }

    public MethodOutcome fetchMeterDataCsvFile(MeteringPointsRequest meteringPointsRequest, LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregationUnit) {
        MethodOutcome result = meterDataManager.fetchMeterDataInPeriodAsCsvFile(meteringPointsRequest, dateFrom, dateTo, aggregationUnit);
        if (result == MethodOutcome.SUCCESS) {
            LOG.info("""
                    MeterData from the following MeteringPoints: {}
                    in the following period:
                    From date: {}
                    To date: {}
                    was saved in CSV file based on hourly data.
                    """, meteringPointsRequest, dateFrom, dateTo);
        }
        return result;
    }
}
