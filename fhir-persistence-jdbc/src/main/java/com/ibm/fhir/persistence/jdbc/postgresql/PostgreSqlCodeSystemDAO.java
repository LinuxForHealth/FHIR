/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.dao.impl.CodeSystemDAOImpl;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * PostgreSql variant DAO used to manage code_systems records. Uses
 * plain old JDBC statements instead of a stored procedure.
 *
 */
public class PostgreSqlCodeSystemDAO extends CodeSystemDAOImpl {
    private static final Logger log = Logger.getLogger(PostgreSqlCodeSystemDAO.class.getName());
    private static final String CLASSNAME = PostgreSqlCodeSystemDAO.class.getName();
    private static final String SQL_CALL_ADD_CODE_SYSTEM_ID = "{CALL %s.add_code_system(?, ?)}";

    /**
     * Public constructor
     * @param c
     * @param fsd
     */
    public PostgreSqlCodeSystemDAO(Connection c) {
        super(c);
    }

    /**
     * Calls a stored procedure to read the system contained in the passed Parameter in the Code_Systems table.
     * If it's not in the DB, it will be stored and a unique id will be returned.
     * @param systemName
     *
     * @return The generated id of the stored system.
     * @throws FHIRPersistenceDataAccessException
     */
    @Override
    public int readOrAddCodeSystem(String systemName) throws FHIRPersistenceDataAccessException   {
        final String METHODNAME = "readOrAddCodeSystem";
        log.entering(CLASSNAME, METHODNAME);

        int systemId;
        String currentSchema;
        String stmtString;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            currentSchema = getConnection().getSchema().trim();
            stmtString = String.format(SQL_CALL_ADD_CODE_SYSTEM_ID, currentSchema);
            try (CallableStatement stmt = getConnection().prepareCall(stmtString)) {
                stmt.setString(1, systemName);
                stmt.registerOutParameter(2, Types.INTEGER);
                dbCallStartTime = System.nanoTime();
                stmt.execute();
                dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
                if (log.isLoggable(Level.FINE)) {
                        log.fine("DB read code system id complete. executionTime=" + dbCallDuration + "ms");
                }
                systemId = stmt.getInt(2);
            }
        } catch (Throwable e) {
            String errMsg = "Failure storing code system id: name=" + systemName;
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return systemId;
    }

}
