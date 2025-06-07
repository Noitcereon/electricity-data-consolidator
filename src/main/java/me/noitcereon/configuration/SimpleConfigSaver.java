package me.noitcereon.configuration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

public class SimpleConfigSaver implements ConfigurationSaver {
    private static SimpleConfigSaver instance;

    private static final Logger LOG = LoggerFactory.getLogger(SimpleConfigSaver.class);

    private SimpleConfigSaver() {
        // Prevent accidental instantiation.
    }

    @Override
    public String saveProperty(String key, String value) {
        Path configFile = Path.of("./config/" + key + ".conf");
        ensureFileExists(configFile);

        try (Writer outputWriter = Files.newBufferedWriter(configFile)) {
            String lineToSave = key + "=" + value;
            outputWriter.write(lineToSave);
            outputWriter.flush();
            SimpleConfigLoader.getInstance().forceRefreshCache();
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }

        return "Updated " + key + " to the value " + value;
    }

    private void ensureFileExists(Path configFile) {
        try {
            Files.createFile(configFile);
        } catch (FileAlreadyExistsException e) {
            // Ignore.
        } catch (NoSuchFileException e) {
            LOG.error("Error when trying to create file at {}", configFile.toAbsolutePath());
            throw new ElectricityConsolidatorRuntimeException(e);
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }
    }

    public static SimpleConfigSaver getInstance() {
        if (instance == null) {
            instance = new SimpleConfigSaver();
        }
        return instance;
    }
}
