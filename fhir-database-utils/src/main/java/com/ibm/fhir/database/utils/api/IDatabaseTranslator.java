/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.sql.SQLException;
import java.util.Properties;

/**
 * Lets us adjust DDL/DML/SQL statements to match the target database. This
 * is needed because DB2 and Derby have a few differences, and we need to
 * tweak the SQL in order to support all the unit tests we want/need
 * @author rarnold
 *
 */
public interface IDatabaseTranslator {

    /**
     * Are we working with a Derby database
     * @return
     */
    boolean isDerby();
    
    /**
     * Append FOR UPDATE/FOR UPDATE WITH RS depending on the target DB type
     * @param sql
     * @return
     */
    String addForUpdate(String sql);

    /**
     * Get the proper table name based on the type of database we
     * are connected to. Derby has its own handling of temp tables
     * @param string
     * @return
     */
    String globalTempTableName(String tableName);

    /**
     * @param ddl
     * @return
     */
    String createGlobalTempTable(String ddl);

    /**
     * Check the exception to see if it is reporting a duplicate value constraint violation
     * @param x
     * @return
     */
    boolean isDuplicate(SQLException x);
    
    /**
     * Database timed out waiting to get a lock. This is not the same as a deadlock, of course
     * @param x
     * @return
     */
    boolean isLockTimeout(SQLException x);
    
    /**
     * Was this statement the victim of a deadlock
     * @param x
     * @return
     */
    boolean isDeadlock(SQLException x);
        
    /**
     * Returns true if the exception represents a connection error
     * @param x
     * @return
     */
    boolean isConnectionError(SQLException x);
    
    /**
     * Get an appropriate instance of ReplicatorException to throw
     * depending on the details of SQLException
     * @param x
     * @return
     */
    DataAccessException translate(SQLException x);

    /**
     * Returns true if the SQLException is indicating an object is undefined
     * (e.g. "DROP TABLE foo.bar", where table "foo.bar" doesn't exist)
     * @param x
     * @return
     */
    boolean isUndefinedName(SQLException x);

    /**
     * Configure the properties using information from the ConnectionDetails
     * @param p
     * @param cd
     */
    void fillProperties(Properties p, ConnectionDetails cd);
    
    /**
     * Returns an expression which computes the timestamp difference
     * between left and right in seconds
     * @param left
     * @param right
     * @param alias adds " AS alias " if alias is not null
     * @return
     */
    String timestampDiff(String left, String right, String alias);

    /**
     * Craft the DDL for a CREATE SEQUENCE statement
     * @param name
     * @param cache the number of sequence values to cache, if supported by the database
     * @return
     */
    String createSequence(String name, int cache);

    /**
     * Return the REORG TABLE command if supported, or null otherwise
     * @param tableName
     * @return
     */
    String reorgTableCommand(String tableName);

    /**
     * Get the driver class to use for connections
     * @return
     */
    String getDriverClassName();
    
    /**
     * Get the JDBC connection URL based on the properties
     * @param connectionProperties
     * @return
     */
    String getUrl(Properties connectionProperties);
    
    /**
     * Does the database support inlining for clobs
     * @return
     */
    boolean clobSupportsInline();
}
