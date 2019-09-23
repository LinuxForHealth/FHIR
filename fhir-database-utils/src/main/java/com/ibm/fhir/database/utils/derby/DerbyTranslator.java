/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.derby;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.ConnectionDetails;
import com.ibm.fhir.database.utils.api.ConnectionException;
import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DuplicateValueException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.LockException;
import com.ibm.fhir.database.utils.api.UndefinedNameException;

/**
 * @author rarnold
 *
 */
public class DerbyTranslator implements IDatabaseTranslator {
    private static final Logger logger = Logger.getLogger(DerbyTranslator.class.getName());

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.stream.rdbms.IStatementWriter#addForUpdate(java.lang.String)
     */
    @Override
    public String addForUpdate(String sql) {
        return sql + " FOR UPDATE";
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isDerby()
     */
    @Override
    public boolean isDerby() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#globalTempTableName(java.lang.String)
     */
    @Override
    public String globalTempTableName(String tableName) {
        return "SYSTEM." + tableName;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#createGlobalTempTable(java.lang.String)
     */
    @Override
    public String createGlobalTempTable(String ddl) {
        return "DECLARE " + ddl;
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
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#isLockTimeout(java.sql.SQLException)
     */
    @Override
    public boolean isDeadlock(SQLException x) {
        final String sqlState = x.getSQLState();
        return "40XL1".equals(sqlState) || "40XL2".equals(sqlState);
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
        return "42X05".equals(x.getSQLState());
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#fillProperties(java.util.Properties, com.ibm.watson.platform.health.replicator.api.ConnectionDetails)
     */
    @Override
    public void fillProperties(Properties p, ConnectionDetails cd) {
        p.put("user", cd.getUser());
        p.put("password", cd.getPassword());
        
        if (cd.isSsl()) {
            p.put("sslConnection", "true");
        }
        
        if (cd.isHA()) {
            logger.warning("No HA support for Derby");
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#timestampDiff(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String timestampDiff(String left, String right, String alias) {
        if (alias == null || alias.isEmpty()) {
            return String.format("{fn timestampdiff(SQL_TSI_SECOND, %s, %s)}", left, right);
        }
        else {
            return String.format("{fn timestampdiff(SQL_TSI_SECOND, %s, %s)} AS %s", left, right, alias);            
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#createSequence(java.lang.String, int)
     */
    @Override
    public String createSequence(String name, int cache) {
        // cache isn't supported by Derby
        return "CREATE SEQUENCE " + name;
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.platform.health.replicator.api.IStatementWriter#reorgTableCommand(java.lang.String)
     */
    @Override
    public String reorgTableCommand(String tableName) {
        // REORG TABLE not supported by Derby, so return null
        return null;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#getDriverClassName()
     */
    @Override
    public String getDriverClassName() {
        return "org.apache.derby.jdbc.EmbeddedDriver";
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#getUrl(java.util.Properties)
     */
    @Override
    public String getUrl(Properties connectionProperties) {
        DerbyPropertyAdapter adapter = new DerbyPropertyAdapter(connectionProperties);
        if (adapter.isMemory()) {
            return "jdbc:derby:memory:" + adapter.getDatabase();
        }
        else {
            return "jdbc:derby:" + adapter.getDatabase();            
        }
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IDatabaseTranslator#clobSupportsInline()
     */
    @Override
    public boolean clobSupportsInline() {
        return false;
    }

}
