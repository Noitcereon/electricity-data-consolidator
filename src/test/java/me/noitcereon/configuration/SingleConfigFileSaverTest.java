package me.noitcereon.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleConfigFileSaverTest {

    @TempDir
    Path tempDir;

    private Path testFile(String name) {
        return tempDir.resolve(name);
    }

    @Test
    void givenMissingFileAndParentDirectory_WhenSavingProperty_ThenFileIsCreatedAndReadable() throws IOException {
        Path conf = tempDir.resolve("nested").resolve("app.conf");
        assertFalse(Files.exists(conf));

        SingleConfigFileSaver saver = new SingleConfigFileSaver(conf);
        String msg = saver.saveProperty("alpha", "A");
        assertTrue(msg.contains("alpha") && msg.contains("A"));

        assertTrue(Files.exists(conf));
        // Use loader to verify readability
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);
        assertEquals("A", loader.getProperty("alpha").orElseThrow());
    }

    @Test
    void givenInvalidKeys_WhenSavingProperty_ThenThrowsIllegalArgumentException() {
        Path conf = testFile("invalid-keys.conf");
        SingleConfigFileSaver saver = new SingleConfigFileSaver(conf);
        assertThrows(IllegalArgumentException.class, () -> saver.saveProperty("bad key", "x"));
        assertThrows(IllegalArgumentException.class, () -> saver.saveProperty("bad=key", "x"));
        assertThrows(IllegalArgumentException.class, () -> saver.saveProperty("", "x"));
    }

    @Test
    void givenExistingFileWithMixedContent_WhenOverwritingAndAddingKeys_ThenKeepsOtherEntriesAndIsParsable() throws IOException {
        Path conf = testFile("overwrite.conf");
        String originalConfigFileContent = """
                # comment
                a = 1
                
                b = two""";
        Files.writeString(conf, originalConfigFileContent);

        SingleConfigFileSaver saver = new SingleConfigFileSaver(conf);
        // Overwrite existing key 'a' and add a new key 'c'
        saver.saveProperty("a", "UPDATED");
        saver.saveProperty("c", "3");

        // Validate by loading with loader
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);
        assertEquals("UPDATED", loader.getProperty("a").orElseThrow());
        assertEquals("two", loader.getProperty("b").orElseThrow());
        assertEquals("3", loader.getProperty("c").orElseThrow());

        // Assert only 1 line was added (and none are removed)
        List<String> lines = Files.readAllLines(conf);
        assertEquals(5, lines.size());
    }

    @Test
    void givenSavedProperties_WhenUsingLoaderCacheAndRefreshing_ThenValuesUpdateAfterRefresh() {
        Path conf = testFile("roundtrip.conf");
        SingleConfigFileSaver saver = new SingleConfigFileSaver(conf);
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);

        saver.saveProperty(ConfigurationKeys.API_KEY, "K-123");
        saver.saveProperty(ConfigurationKeys.DATA_ACCESS_TOKEN, "T-456");

        // Lazy load then explicit refresh shouldn't break anything
        assertEquals("K-123", loader.getApiKey());
        assertEquals("T-456", loader.getDataAccessToken());

        // Update externally via saver and ensure loader reflects after refresh
        saver.saveProperty(ConfigurationKeys.API_KEY, "K-789");
        assertEquals("K-123", loader.getApiKey()); // still cached
        loader.forceRefreshCache();
        assertEquals("K-789", loader.getApiKey());
    }
}
