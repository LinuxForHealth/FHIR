/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.loader.util;

import java.util.logging.Logger;

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * This class will load a Configuration from a property file, then override parameters based on environment variables.
 */
public class ConfigLoader {
    public static final String STORAGE_DATACENTER = "storage.cql.local-datacenter";
    public static final String STORAGE_HOSTNAME = "storage.hostname";
    public static final String STORAGE_PORT = "storage.port";
    public static final String STORAGE_USERNAME = "storage.username";
    public static final String STORAGE_PASSWORD = "storage.password";
    public static final String INDEX_SEARCH_HOSTNAME = "index.search.hostname";
    public static final String INDEX_SEARCH_PORT = "index.search.port";
    public static final String STORAGE_DATACENTER_ENV = "TERM_STORAGE_DATACENTER";
    public static final String STORAGE_HOSTNAME_ENV = "TERM_" + STORAGE_HOSTNAME.toUpperCase().replaceAll("\\.", "_");
    public static final String STORAGE_PORT_ENV = "TERM_" + STORAGE_PORT.toUpperCase().replaceAll("\\.", "_");
    public static final String STORAGE_USERNAME_ENV = "TERM_" + STORAGE_USERNAME.toUpperCase().replaceAll("\\.", "_");
    public static final String STORAGE_PASSWORD_ENV = "TERM_" + STORAGE_PASSWORD.toUpperCase().replaceAll("\\.", "_");
    public static final String INDEX_SEARCH_HOSTNAME_ENV = "TERM_" + INDEX_SEARCH_HOSTNAME.toUpperCase().replaceAll("\\.", "_");
    public static final String INDEX_SEARCH_PORT_ENV = "TERM_" + INDEX_SEARCH_PORT.toUpperCase().replaceAll("\\.", "_");

    private static final Logger LOG = Logger.getLogger(ConfigLoader.class.getName());

    /*
     * Load Configuration from properties file at given location, then override using environment variables
     */
    public static final Configuration load(String propFileName) throws ConfigurationException {
        Configuration configuration = null;
        if (propFileName == null) {
            LOG.info("Could not load configuration from property file. ");
            configuration = new BaseConfiguration();

            configuration.setProperty("storage.backend", "cql");
            configuration.setProperty("storage.batch-loading", "true");
            configuration.setProperty(STORAGE_HOSTNAME, "127.0.0.1");
            configuration.setProperty("index.search.backend", "elasticsearch");
            configuration.setProperty(INDEX_SEARCH_HOSTNAME, "127.0.0.1");
            configuration.setProperty(INDEX_SEARCH_PORT, "9200");
            configuration.setProperty("cache.tx-cache-size", "100000");
            configuration.setProperty("cache.tx-dirty-size", "10000");
            configuration.setProperty("ids.block-size", "500000");
            configuration.setProperty("storage.cql.local-datacenter", "datacenter1");
        } else {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<FileBasedConfiguration> builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                    PropertiesConfiguration.class).configure(params.properties().setFileName(propFileName));
            configuration = builder.getConfiguration();
        }

        String storageHostname = System.getenv(STORAGE_HOSTNAME_ENV);
        if (storageHostname != null) {
            configuration.setProperty(STORAGE_HOSTNAME, storageHostname);
        }

        String storagePort = System.getenv(STORAGE_PORT_ENV);
        if (storagePort != null) {
            configuration.setProperty(STORAGE_PORT, storagePort);
        }

        String storageUsername = System.getenv(STORAGE_USERNAME_ENV);
        if (storageUsername != null) {
            configuration.setProperty(STORAGE_USERNAME, storageUsername);
        }

        String storagePassword = System.getenv(STORAGE_PASSWORD_ENV);
        if (storagePassword != null) {
            configuration.setProperty(STORAGE_PASSWORD, storagePassword);
        }

        String indexSearchHostname = System.getenv(INDEX_SEARCH_HOSTNAME_ENV);
        if (indexSearchHostname != null) {
            configuration.setProperty(INDEX_SEARCH_HOSTNAME, indexSearchHostname);
        }

        String indexSearchPort = System.getenv(INDEX_SEARCH_PORT_ENV);
        if (indexSearchPort != null) {
            configuration.setProperty(INDEX_SEARCH_PORT, indexSearchPort);
        }

        String datacenter = System.getenv(STORAGE_DATACENTER_ENV);
        if (datacenter != null) {
            configuration.setProperty(STORAGE_DATACENTER, datacenter);
        }

        return configuration;
    }
}