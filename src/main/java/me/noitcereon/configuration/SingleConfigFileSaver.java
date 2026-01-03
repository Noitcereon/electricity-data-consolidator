package me.noitcereon.configuration;

import me.noitcereon.exceptions.ElectricityConsolidatorRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * A {@link ConfigurationSaver} implementation that writes configuration key-value pairs to a single file
 * in a format readable by {@link SingleConfigFileLoader}.
 * <p>
 * Format per line: {@code key = value}
 * <ul>
 *     <li>Preserves existing comments (lines starting with '#'), blank lines, and any non-conforming lines.</li>
 *     <li>When saving a property:
 *         <ul>
 *             <li>If the key already exists on one or more lines, those line(s) are replaced with a normalized
 *             {@code key = value} entry while all other content is kept intact.</li>
 *             <li>If the key does not exist, a new {@code key = value} line is appended at the end of the file.</li>
 *         </ul>
 *     </li>
 *     <li>Keys must not contain spaces or the {@code =} character</li>
 * </ul>
 *
 * @implNote Made with Junie AI agent
 */
public class SingleConfigFileSaver implements ConfigurationSaver {
    private static final Logger LOG = LoggerFactory.getLogger(SingleConfigFileSaver.class);

    private final Path configFilePath;

    /**
     * Creates a saver using the default path: {@code ./config/application.conf}.
     */
    public SingleConfigFileSaver() {
        this(Path.of("./config/application.conf"));
    }

    /**
     * Creates a saver targeting the specified config file. The file and its parent directory
     * will be created if missing.
     *
     * @param configFilePath the target configuration file
     */
    public SingleConfigFileSaver(Path configFilePath) {
        Objects.requireNonNull(configFilePath, "configFilePath");
        this.configFilePath = configFilePath;
        ensureFileExists();
    }

    @Override
    public String saveProperty(String key, String value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");
        validateKey(key);

        ensureFileExists();
        List<String> lines;
        try {
            lines = Files.readAllLines(configFilePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }

        boolean replacedAny = false;
        List<String> out = new ArrayList<>(Math.max(lines.size(), 1));
        for (String raw : lines) {
            if (raw == null) {
                out.add("");
                continue;
            }
            String trimmed = raw.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                int idx = trimmed.indexOf('=');
                if (idx >= 0) {
                    String existingKey = trimmed.substring(0, idx).trim();
                    if (isValidKey(existingKey) && existingKey.equals(key)) {
                        out.add(existingKey + " = " + value);
                        replacedAny = true;
                        continue;
                    }
                }
            }
            // keep line as-is
            out.add(raw);
        }

        if (!replacedAny) {
            out.add(key + " = " + value);
        }

        try {
            Files.write(configFilePath, out, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ElectricityConsolidatorRuntimeException(e);
        }

        return "Updated " + key + " to the value " + value;
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

    private void validateKey(String key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid key. Keys must not be empty, contain whitespace, or '='.");
        }
    }

    private boolean isValidKey(String key) {
        if (key == null || key.isEmpty()) return false;
        if (key.indexOf('=') >= 0) return false;
        for (int i = 0; i < key.length(); i++) {
            if (Character.isWhitespace(key.charAt(i))) return false;
        }
        return true;
    }
}
