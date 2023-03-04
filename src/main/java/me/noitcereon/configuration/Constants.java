package me.noitcereon.configuration;

public class Constants {
    private Constants(){
        // not intended to be instantiated.
    }

    /**
     * Should contain default values.
     */
    public static final String HARDCODED_CONFIG_FILE = "configuration.conf";

    /**
     * Should contain custom values that would override the default values.
     */
    public static final String CONFIG_FILE = "personal-configuration.conf";
}
