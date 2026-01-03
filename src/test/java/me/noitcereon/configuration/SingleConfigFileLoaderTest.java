package me.noitcereon.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SingleConfigFileLoaderTest {

    @TempDir
    Path tempDir;

    private Path testFile(String name) {
        return tempDir.resolve(name);
    }

    @Test
    void givenConfigWithMixedLines_WhenLoading_ThenReadsValidAndIgnoresInvalidCommentsAndBlanks() throws IOException {
        Path conf = testFile("app.conf");
        Files.createDirectories(conf.getParent());
        Files.write(conf, (
                "# comment\n" +
                "\n" +
                "a = 1\n" +
                "b=two\n" +
                "c = some = value with equals\n" +
                "bad key = nope\n" +
                "novalue-line-without-equals\n" +
                "# another comment\n" +
                "d=   spaced\n"
        ).getBytes(StandardCharsets.UTF_8));

        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);

        assertEquals(Optional.of("1"), loader.getProperty("a"));
        assertEquals(Optional.of("two"), loader.getProperty("b"));
        assertEquals(Optional.of("some = value with equals"), loader.getProperty("c"));
        assertEquals(Optional.of("spaced"), loader.getProperty("d"));

        // invalids ignored
        assertEquals(Optional.empty(), loader.getProperty("bad key"));
        assertEquals(Optional.empty(), loader.getProperty("novalue-line-without-equals"));
    }

    @Test
    void givenCachedConfig_WhenFileChangesWithoutRefresh_ThenReturnsOldValueUntilRefreshed() throws IOException {
        Path conf = testFile("cache.conf");
        Files.write(conf, "x = 1\n".getBytes(StandardCharsets.UTF_8));
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);

        // First read loads cache lazily
        assertEquals(Optional.of("1"), loader.getProperty("x"));

        // External change to the file should not reflect without refresh
        Files.write(conf, "x = 2\n".getBytes(StandardCharsets.UTF_8));
        assertEquals(Optional.of("1"), loader.getProperty("x")); // still cached

        // After force refresh, it should pick up new value
        loader.forceRefreshCache();
        assertEquals(Optional.of("2"), loader.getProperty("x"));
    }

    @Test
    void givenNoCredentialsInFile_WhenQueryingApiKeyAndToken_ThenEmptyStringsThenValuesAfterRefresh() throws IOException {
        Path conf = testFile("creds.conf");
        // start empty; ctor will create file
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);

        assertEquals("", loader.getApiKey());
        assertEquals("", loader.getDataAccessToken());

        Files.write(conf, (
                ConfigurationKeys.API_KEY + " = ABC\n" +
                ConfigurationKeys.DATA_ACCESS_TOKEN + " = XYZ\n"
        ).getBytes(StandardCharsets.UTF_8));

        loader.forceRefreshCache();

        assertEquals("ABC", loader.getApiKey());
        assertEquals("XYZ", loader.getDataAccessToken());
    }

    @Test
    void givenMissingFileAndParentDirectory_WhenInitializingLoader_ThenCreatesEmptyConfigFile() {
        Path conf = tempDir.resolve("nested").resolve("app.conf");
        assertFalse(Files.exists(conf));
        SingleConfigFileLoader loader = new SingleConfigFileLoader(conf);
        assertTrue(Files.exists(conf), "Loader should create the config file if it doesn't exist");
        // Also ensure getProperty works with newly created empty file
        assertEquals(Optional.empty(), loader.getProperty("anything"));
    }
}
