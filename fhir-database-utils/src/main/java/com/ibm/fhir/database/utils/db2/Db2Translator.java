/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.ConnectionDetails;
import com.ibm.fhir.database.utils.api.ConnectionException;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DuplicateValueException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.LockException;
import com.ibm.fhir.database.utils.api.UndefinedNameException;


/**
 * Handles translation of statements/fragments etc specific to DB2
 * @author rarnold
 *
 */
public class Db2Translator implements IDatabaseTranslator {

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.stream.rdbms.IStatementWriter#addForUpdate(java.lang.String)
     */
    @Override
    public String addForUpdate(String sql) {
        return sql + " FOR UPDATE WITH RS";
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isDerby()
     */
    @Override
    public boolean isDerby() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#globalTempTableName(java.lang.String)
     */
    @Override
    public String globalTempTableName(String tableName) {
        return tableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#createGlobalTempTable(java.lang.String)
     */
    @Override
    public String createGlobalTempTable(String ddl) {
        return "CREATE " + ddl;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isDuplicate(java.sql.SQLException)
     */
    @Override
    public boolean isDuplicate(SQLException x) {
        // Class Code 23: Constraint Violation
        return "23505".equals(x.getSQLState());
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isLockTimeout(java.sql.SQLException)
     */
    @Override
    public boolean isLockTimeout(SQLException x) {
        // lock timeout (not deadlock)
        final String sqlState = x.getSQLState();
        final String msg = x.getMessage();

        return "40001".equals(sqlState) 
                && msg != null 
                && msg.contains("SQLERRMC=68");
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isLockTimeout(java.sql.SQLException)
     */
    @Override
    public boolean isDeadlock(SQLException x) {
        // deadlock is 40001 reason code 2
        final String sqlState = x.getSQLState();
        final String msg = x.getMessage();
        return "40001".equals(sqlState) 
            && msg != null
            && msg.contains("SQLERRMC=2");
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isConnectionError(java.sql.SQLException)
     */
    @Override
    public boolean isConnectionError(SQLException x) {
        String sqlState = x.getSQLState();
        return sqlState != null && sqlState.startsWith("08");
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#translate(java.sql.SQLException)
     */
    @Override
    public DataAccessException translate(SQLException x) {
        if (isDeadlock(x)) {
            return new LockException(x, true);
        }
        else if (isLockTimeout(x)) {
            return new LockException(x, false);
        }
        else if (isConnectionError(x)) {
            return new ConnectionException(x);
        }
        else if (isDuplicate(x)) {
            return new DuplicateValueException(x);
        }
        else if (isUndefinedName(x)) {
            return new UndefinedNameException(x);
        }
        else {
            return new DataAccessException(x);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isUndefinedName(java.sql.SQLException)
     */
    @Override
    public boolean isUndefinedName(SQLException x) {
        return "42704".equals(x.getSQLState());
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#fillProperties(java.util.Properties, com.ibm.watson.platform.health.replicator.api.ConnectionDetails)
     */
    @Override
    public void fillProperties(Properties p, ConnectionDetails cd) {
        // Configure the properties as required by the DB2 driver
        p.put("user", cd.getUser());
        p.put("password", cd.getPassword());
                
        if (cd.isSsl()) {
            p.put("sslConnection", "true");
            p.put("sslTrustStoreLocation", cd.getTrustStoreLocation());
            p.put("sslTrustStorePassword", cd.getTrustStorePassword());
        }

        // Let's see if we have HA enabled...if so we need to populate
        // the properties with some more info
        if (cd.isHA()) {
            // failback only works if enableSeamlessFailover and enableClientAffinitiesList are yes
            // p.put("affinityFailbackInterval", cd.getAffinityFailbackInterval());
            p.put("clientRerouteAlternateServerName", cd.getClientRerouteAlternateServerName());
            
            // RTC 257857 need to pass in port number as a string property
            p.put("clientRerouteAlternatePortNumber", cd.getClientRerouteAlternatePortNumber());

            // only inject the seamless failover property if we think it has been enabled
            if (cd.getEnableSeamlessFailover() > 0) {
                p.put("enableSeamlessFailover", cd.getEnableSeamlessFailover());
            }
            
            p.put("maxRetriesForClientReroute", "" + cd.getMaxRetriesForClientReroute());
            p.put("retryIntervalForClientReroute", "" + cd.getRetryIntervalForClientReroute());
            p.put("enableClientAffinitiesList", "" + cd.getEnableClientAffinitiesList());
            p.put("connectionTimeout", "" + cd.getConnectionTimeout());
            p.put("loginTimeout", "" + cd.getLoginTimeout());
            p.put("blockingReadConnectionTimeout", "" + 60);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#timestampDiff(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String timestampDiff(String left, String right, String alias) {
        if (alias == null || alias.isEmpty()) {
            return String.format("timestampdiff(2, %s, %s)", left, right);
        }
        else {
            return String.format("timestampdiff(2, %s, %s) AS %s", left, right, alias);            
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#createSequence(java.lang.String, int)
     */
    @Override
    public String createSequence(String name, int cache) {
        return "CREATE SEQUENCE " + name + " CACHE " + cache;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#reorgTableCommand(java.lang.String)
     */
    @Override
    public String reorgTableCommand(String tableName) {
        return "REORG TABLE " + tableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#getDriverClassName()
     */
    @Override
    public String getDriverClassName() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#getUrl(java.util.Properties)
     */
    @Override
    public String getUrl(Properties connectionProperties) {
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(connectionProperties);
        return "jdbc:db2://" + adapter.getHost() + ":" + adapter.getPort() + "/" + adapter.getDatabase();    
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#clobSupportsInline()
     */
    @Override
    public boolean clobSupportsInline() {
        return true;
    }
}
