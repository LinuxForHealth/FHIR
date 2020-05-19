/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.db2;

import java.sql.SQLException;
import java.util.Properties;

import com.ibm.fhir.database.utils.api.BadTenantFrozenException;
import com.ibm.fhir.database.utils.api.BadTenantKeyException;
import com.ibm.fhir.database.utils.api.BadTenantNameException;
import com.ibm.fhir.database.utils.api.ConnectionDetails;
import com.ibm.fhir.database.utils.api.ConnectionException;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DatabaseNotReadyException;
import com.ibm.fhir.database.utils.api.DuplicateNameException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.LockException;
import com.ibm.fhir.database.utils.api.UndefinedNameException;
import com.ibm.fhir.database.utils.api.UniqueConstraintViolationException;


/**
 * Handles translation of statements/fragments etc specific to DB2
 */
public class Db2Translator implements IDatabaseTranslator {

    @Override
    public String addForUpdate(String sql) {
        return sql + " FOR UPDATE WITH RS";
    }

    @Override
    public boolean isDerby() {
        return false;
    }

    @Override
    public String globalTempTableName(String tableName) {
        return tableName;
    }

    @Override
    public String createGlobalTempTable(String ddl) {
        return "CREATE " + ddl;
    }

    @Override
    public boolean isDuplicate(SQLException x) {
        // Class Code 23: Constraint Violation
        return "23505".equals(x.getSQLState());
    }

    @Override
    public boolean isAlreadyExists(SQLException x) {
        // SQL-Error: -601, SQL State 42710
        return "42710".equals(x.getSQLState());
    }

    @Override
    public boolean isLockTimeout(SQLException x) {
        // lock timeout (not deadlock)
        final String sqlState = x.getSQLState();
        final String msg = x.getMessage();

        return "40001".equals(sqlState) 
                && msg != null 
                && msg.contains("SQLERRMC=68");
    }

    @Override
    public boolean isDeadlock(SQLException x) {
        // deadlock is 40001 reason code 2
        final String sqlState = x.getSQLState();
        final String msg = x.getMessage();
        return "40001".equals(sqlState) 
            && msg != null
            && msg.contains("SQLERRMC=2");
    }

    @Override
    public boolean isConnectionError(SQLException x) {
        String sqlState = x.getSQLState();
        return sqlState != null && sqlState.startsWith("08");
    }

    @Override
    public DataAccessException translate(SQLException x) {
        if (isBadTenantKeyError(x)){
            return new BadTenantKeyException(x);
        } else if (isBadTenantNameError(x)) {
            return new BadTenantNameException(x);
        } else if (isTenantFrozenError(x)) {
            return new BadTenantFrozenException(x);
        } else if (isDeadlock(x)) {
            return new LockException(x, true);
        } else if (isLockTimeout(x)) {
            return new LockException(x, false);
        } else if (isConnectionError(x)) {
            return new ConnectionException(x);
        } else if (isDuplicate(x)) {
            return new UniqueConstraintViolationException(x);
        } else if (isAlreadyExists(x)) {
            return new DuplicateNameException(x);
        } else if (isUndefinedName(x)) {
            return new UndefinedNameException(x);
        } else if(isDatabaseNotReady(x)) {
            return new DatabaseNotReadyException(x);
        } else {
            return new DataAccessException(x);
        }
    }

    /*
     * This is specific to Db2 when the database is not yet up and ready.
     * For instance, when it's offline, but the connection/port is active.
     * Or in the automation world, it's still being created.
     */
    public boolean isDatabaseNotReady(SQLException x) {
        //<code>SQLCODE=-1035, SQLSTATE=57019</code>
        String sqlState = x.getSQLState();
        Integer sqlCode = x.getErrorCode();
        return sqlState != null && sqlState.equals("57019") 
                && sqlCode != null && sqlCode == -1035;
    }

    /*
     * This is specific to Db2 schema's multi-tenant feature.
     */
    public boolean isTenantFrozenError(SQLException x) {
        String sqlState = x.getSQLState();
        String sqlMsg = x.getMessage();
        return sqlState != null && sqlState.equals("99401") && sqlMsg != null
                && sqlMsg.contains("NOT AUTHORIZED: TENANT IS FROZEN");
    }

    /*
     * This is specific to Db2 schema's multi-tenant feature.
     */
    public boolean isBadTenantKeyError(SQLException x) {
        String sqlState = x.getSQLState();
        String sqlMsg = x.getMessage();
        return sqlState != null && sqlState.equals("99401") && sqlMsg != null
                && sqlMsg.contains("NOT AUTHORIZED: MISSING OR INVALID TENANT KEY");
    }

    /*
     * This is specific to Db2 schema's multi-tenant feature.
     */
    public boolean isBadTenantNameError(SQLException x) {
        String sqlState = x.getSQLState();
        String sqlMsg = x.getMessage();
        return sqlState != null && sqlState.equals("99401") && sqlMsg != null
                && sqlMsg.contains("NOT AUTHORIZED: MISSING OR INVALID TENANT NAME");
    }

    @Override
    public boolean isUndefinedName(SQLException x) {
        return "42704".equals(x.getSQLState());
    }

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

    @Override
    public String timestampDiff(String left, String right, String alias) {
        if (alias == null || alias.isEmpty()) {
            return String.format("timestampdiff(2, %s, %s)", left, right);
        }
        else {
            return String.format("timestampdiff(2, %s, %s) AS %s", left, right, alias);
        }
    }

    @Override
    public String createSequence(String name, int cache) {
        return "CREATE SEQUENCE " + name + " CACHE " + cache;
    }

    @Override
    public String reorgTableCommand(String tableName) {
        return "REORG TABLE " + tableName;
    }

    @Override
    public String getDriverClassName() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    @Override
    public String getUrl(Properties connectionProperties) {
        Db2PropertyAdapter adapter = new Db2PropertyAdapter(connectionProperties);
        return "jdbc:db2://" + adapter.getHost() + ":" + adapter.getPort() + "/" + adapter.getDatabase();
    }

    @Override
    public boolean clobSupportsInline() {
        return true;
    }
}
