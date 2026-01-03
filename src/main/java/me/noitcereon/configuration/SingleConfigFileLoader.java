package me.noitcereon.configuration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ConfigurationLoader} implementation that reads all configuration key-value pairs from a single file.
 * <p>
 * Format rules:
 * <ul>
 *     <li>Each non-empty line contains a single key-value pair in the form: {@code key = value}</li>
 *     <li>Lines starting with {@code #} are treated as comments and ignored</li>
 *     <li>Blank lines are ignored</li>
 *     <li>Keys must not contain spaces or the {@code =} character</li>
 * </ul>
 *
 * @implNote Made with Junie AI agent
 */
public class SingleConfigFileLoader implements ConfigurationLoader {
    private static final Logger LOG = LoggerFactory.getLogger(SingleConfigFileLoader.class);

    private final Path configFilePath;

    // Lazy cache
    private Map<String, String> cache; // null until first use

    /**
     * Creates a loader using the default path: {@code ./config/application.conf}.
     */
    public SingleConfigFileLoader() {
        this(Path.of("./config/application.conf"));
    }

    /**
     * Creates a loader that reads from the specified path. The file and its parent directory
     * will be created if missing.
     *
     * @param configFilePath the path to the configuration file
     */
    public SingleConfigFileLoader(Path configFilePath) {
        Objects.requireNonNull(configFilePath, "configFilePath");
        this.configFilePath = configFilePath;
        ensureFileExists();
    }

    @Override
    public Optional<String> getProperty(String key) {
        Objects.requireNonNull(key, "key");
        ensureCacheLoaded();
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Optional<String> getProperty(String key, String fallbackValue) {
        Objects.requireNonNull(key, "key");
        ensureCacheLoaded();
        String value = cache.get(key);
        if (value != null) return Optional.of(value);
        return Optional.ofNullable(fallbackValue);
    }

    @Override
    public void forceRefreshCache() {
        this.cache = readAllProperties();
    }

    @Override
    public String getApiKey() {
        String key = ConfigurationKeys.API_KEY;
        Optional<String> value = getProperty(key);
        if (value.isEmpty()) {
            LOG.error("Api key has not been set in the configuration file ({}). Expected format per line: 'key = value'", configFilePath.toAbsolutePath());
        }
        return value.orElse("");
    }

    @Override
    public String getDataAccessToken() {
        String key = ConfigurationKeys.DATA_ACCESS_TOKEN;
        Optional<String> value = getProperty(key);
        if (value.isEmpty()) {
            LOG.error("Data access token has not been set in the configuration file ({}). Expected format per line: 'key = value'", configFilePath.toAbsolutePath());
            return "";
        }
        return value.orElse("");
    }

    private void ensureCacheLoaded() {
        if (cache == null) {
            cache = readAllProperties();
        }
    }

    private Map<String, String> readAllProperties() {
        ensureFileExists();
        Map<String, String> map = new LinkedHashMap<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(configFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }

        for (String rawLine : lines) {
            String line = rawLine == null ? "" : rawLine.trim();
            if (line.isBlank()) continue; // blank lines
            if (line.startsWith("#")) continue; // comments

            int separatorIndex = line.indexOf('=');
            if (separatorIndex < 0) {
                LOG.warn("Ignoring invalid config line (missing '='): {}", line);
                continue;
            }

            String key = line.substring(0, separatorIndex).trim();
            String value = line.substring(separatorIndex + 1).trim();

            if (!isValidKey(key)) {
                LOG.warn("Ignoring invalid key '{}': keys must not contain spaces or '='.", key);
                continue;
            }

            map.put(key, value);
        }
        return map;
    }

    private boolean isValidKey(String key) {
        if (key == null || key.isEmpty()) return false;
        if (key.indexOf('=') >= 0) return false;
        // No whitespace allowed in key
        for (int i = 0; i < key.length(); i++) {
            if (Character.isWhitespace(key.charAt(i))) return false;
        }
        return true;
    }

    private void ensureFileExists() {
        try {
            Path parent = configFilePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try {
                Files.createFile(configFilePath);
            } catch (FileAlreadyExistsException ignore) {
                // ok
            }
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }
    }
}
