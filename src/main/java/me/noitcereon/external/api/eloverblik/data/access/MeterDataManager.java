package me.noitcereon.external.api.eloverblik.data.access;

import me.noitcereon.MethodOutcome;
import me.noitcereon.configuration.ConfigurationLoader;
import me.noitcereon.configuration.SimpleConfigLoader;
import me.noitcereon.external.api.eloverblik.TimeAggregation;
import me.noitcereon.external.api.eloverblik.models.MeterDataReadingsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MeterDataManager {
    private final ConfigurationLoader configLoader;
    private static final Logger LOG = LoggerFactory.getLogger(MeterDataManager.class);

    public MeterDataManager() {
        configLoader = SimpleConfigLoader.getInstance();
    }

    public MeterDataManager(ConfigurationLoader configLoader) {
        this.configLoader = configLoader;
    }
    public Optional<List<MeterDataReadingsDto>> getMeterDataInPeriod(LocalDate dateFrom, LocalDate dateTo){
        // TODO implement getMeterDataInPeriod


        return Optional.empty();
    }
    public Optional<List<MeterDataReadingsDto>> getMeterDataInPeriod(LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregation){
        // TODO implement getMeterDataInPeriod


        return Optional.empty();
    }
    public MethodOutcome getMeterDataInPeriodAsCsv(LocalDate dateFrom, LocalDate dateTo, TimeAggregation aggregation){
        // TODO implement getMeterDataInPeriodAsCsv
        return MethodOutcome.FAILURE;
    }
}
