/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * JdbcConnectionProvider
 */
public class JdbcConnectionProvider implements IConnectionProvider {
    private static final Logger logger = Logger.getLogger(JdbcConnectionProvider.class.getName());

    private final JdbcPropertyAdapter properties;
    private final IDatabaseTranslator translator;

    public JdbcConnectionProvider(IDatabaseTranslator translator, JdbcPropertyAdapter properties) {
        this.translator = translator;
        this.properties = properties;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties connectionProperties = new Properties();
        properties.getExtraProperties(connectionProperties);
        String url = translator.getUrl(properties.getProperties());

        logger.info("Opening connection to database: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);

            // Configure the default schema to use on the connection
            String schema = properties.getDefaultSchema();
            if (schema != null && schema.length() > 0) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Setting schema: " + schema);
                }
                connection.setSchema(schema);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return connection;
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return this.translator;
    }

    @Override
    public void commitTransaction() throws SQLException {
        // Intentionally blank

    }

    @Override
    public void rollbackTransaction() throws SQLException {
        // Intentionally blank
    }

    @Override
    public void describe(String prefix, StringBuilder cfg, String key) {
        // Intentionally blank
    }

}
