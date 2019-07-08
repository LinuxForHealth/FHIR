/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.schema.control;

import java.sql.Connection;
import java.sql.SQLException;

import com.ibm.watsonhealth.database.utils.api.IConnectionProvider;
import com.ibm.watsonhealth.database.utils.api.IDatabaseTranslator;
import com.ibm.watsonhealth.database.utils.jdbc.ConnectionStub;

/**
 * @author rarnold
 *
 */
public class TestConnectionProvider implements IConnectionProvider {

    // Just use a single connection stub
    private final ConnectionStub connection;

    private final IDatabaseTranslator translator;

    public TestConnectionProvider(IDatabaseTranslator t) {
        this.connection = new ConnectionStub();
        this.translator = t;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IConnectionProvider#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
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
        // TODO Auto-generated method stub

    }

}
