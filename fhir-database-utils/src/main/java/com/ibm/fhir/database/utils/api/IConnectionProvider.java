/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides an abstract way to obtain a JDBC connection. Hides the implementation,
 * making it easier to switch between in-memory instances of Derby used for unit-tests
 * and connections to actual DB2 instances either in pure Java or JEE environments like
 * Liberty Profile.
 * 
 * One {@link IConnectionProvider} instance is used per data source. If you need to
 * connect to more than one database, you'll need multiple providers. This is just
 * simple wrapper stuff...allowing us to switch out underlying pool implementations should
 * we want/need to. No support for distributed transactions or anything fancy like that.
 * 
 */
public interface IConnectionProvider {
    /**
     * Obtain a new connection to a data-source which this provider has
     * been configured for.
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException;
    
    /**
     * Get the translator associated with this connection provider. Supports
     * interpretation of SQLExceptions and the ability to tweak
     * SQL statements to handle differences between DB2 and Derby
     * @return
     */
    public IDatabaseTranslator getTranslator();
    
    /**
     * Commit the current transaction if there's an actual connection associated
     * with this thread (i.e. if getConnection() has been called at least once),
     * otherwise do nothing.
     */
    public void commitTransaction() throws SQLException;

    /**
     * Roll back the transaction for the connection associated with this thread, if one exists
     */
    public void rollbackTransaction() throws SQLException;

    /**
     * Describe self, for writing configuration information to log file for test record purposes
     * @param cfg
     * @param key
     */
    public void describe(String prefix, StringBuilder cfg, String key);

    /**
     * used to control threading size.
     */
    public default int getPoolSize() {
        return -1;
    }
}