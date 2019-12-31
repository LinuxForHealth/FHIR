/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.dryrun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;

/**
 * DryRunJdbcConnectionProvider
 * <br>
 * Wraps the DryRunJdbcConnetion into a the provider.
 */
public class DryRunJdbcConnectionProvider extends JdbcConnectionProvider {
    private static final Logger logger = Logger.getLogger(DryRunJdbcConnectionProvider.class.getName());

    private final JdbcPropertyAdapter propertiesDryRun;
    private final IDatabaseTranslator translatorDryRun;

    public DryRunJdbcConnectionProvider(IDatabaseTranslator translator, JdbcPropertyAdapter properties) {
        super(translator, properties);
        this.propertiesDryRun = properties;
        this.translatorDryRun = translator;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties connectionProperties = new Properties();
        propertiesDryRun.getExtraProperties(connectionProperties);
        String url = translatorDryRun.getUrl(propertiesDryRun.getProperties());
        logger.info("Opening connection to database: " + url);
        Connection connection;
        try {
            Connection wrappedConnection = DriverManager.getConnection(url, connectionProperties);
            connection = new DryRunConnection(wrappedConnection);
            connection.setAutoCommit(false);

            // Configure the default schema to use on the connection
            String schema = propertiesDryRun.getDefaultSchema();
            if (schema != null && schema.length() > 0) {
                connection.setSchema(schema);
            }
        } catch (SQLException x) {
            throw translatorDryRun.translate(x);
        }
        return connection;
    }

    @Override
    public IDatabaseTranslator getTranslator() {
        return this.translatorDryRun;
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