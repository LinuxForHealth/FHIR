/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.watsonhealth.database.utils.api.IConnectionProvider;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;

/**
 * @author rarnold
 *
 */
public class JdbcConnectionProvider implements IConnectionProvider {
    private static final Logger logger = Logger.getLogger(JdbcConnectionProvider.class.getName());
    
    private final JdbcPropertyAdapter properties;
    private final IDatabaseTranslator translator;
    
    public JdbcConnectionProvider(IDatabaseTranslator translator, JdbcPropertyAdapter properties) {
        this.translator = translator;
        this.properties = properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.watsonhealth.database.utils.api.IConnectionProvider#getConnection()
     */
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
                connection.setSchema(schema);
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return connection;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IConnectionProvider#getTranslator()
     */
    @Override
    public IDatabaseTranslator getTranslator() {
        return this.translator;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IConnectionProvider#commitTransaction()
     */
    @Override
    public void commitTransaction() throws SQLException {
        // TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IConnectionProvider#rollbackTransaction()
     */
    @Override
    public void rollbackTransaction() throws SQLException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IConnectionProvider#describe(java.lang.String, java.lang.StringBuilder, java.lang.String)
     */
    @Override
    public void describe(String prefix, StringBuilder cfg, String key) {

    }

}
