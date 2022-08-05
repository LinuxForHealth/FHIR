/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.CodeSystemDAOImpl;

/**
 * PostgreSql variant DAO used to manage code_systems records. Uses
 * plain old JDBC statements instead of a stored procedure.
 */
public class PostgresCodeSystemDAO extends CodeSystemDAOImpl {
    private static final String CLASSNAME = PostgresCodeSystemDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String SQL_CALL_ADD_CODE_SYSTEM_ID = "{CALL %s.add_code_system(?, ?)}";

    /**
     * Public constructor
     * @param c connection to the database
     * @param schemaName the schema containing the FHIR data tables
     */
    public PostgresCodeSystemDAO(Connection c, String schemaName) {
        super(c, schemaName);
    }

    @Override
    public int readOrAddCodeSystem(String systemName) throws FHIRPersistenceDataAccessException   {
        final String METHODNAME = "readOrAddCodeSystem";
        log.entering(CLASSNAME, METHODNAME);

        int systemId;
        String stmtString;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmtString = String.format(SQL_CALL_ADD_CODE_SYSTEM_ID, getSchemaName());
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
