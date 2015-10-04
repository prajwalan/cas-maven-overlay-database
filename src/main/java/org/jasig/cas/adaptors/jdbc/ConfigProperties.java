package org.jasig.cas.adaptors.jdbc;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigProperties {
    private static ConfigProperties instance;
    private static final String PROPERTIES_FILE = "config.properties";

    private static Configuration configProperties;

    static {
        if (instance == null) {
            instance = new ConfigProperties();
        }
    }

    private ConfigProperties() {
        try {
            configProperties = new PropertiesConfiguration(PROPERTIES_FILE);
        }
        catch (ConfigurationException e) {
            // TODO: Log it
            e.printStackTrace();
        }
    }

    public static Configuration getConfigProperties() {
        return configProperties;
    }

}