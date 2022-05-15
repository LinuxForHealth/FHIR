/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterNameDAOImpl;

public class PostgresParameterNamesDAO extends ParameterNameDAOImpl {
    private static final String CLASSNAME = PostgresParameterNamesDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String SQL_CALL_ADD_PARAMETER_NAME = "{CALL %s.add_parameter_name(?, ?)}";

    public PostgresParameterNamesDAO(Connection c, String schemaName) {
        super(c, schemaName);
    }

    /**
     * Calls a stored procedure to read the name contained in the passed Parameter in the Parameter_Names table.
     * If it's not in the DB, it will be stored and a unique id will be returned.
     * @param parameterName
     * @return The generated id of the stored system.
     * @throws FHIRPersistenceDataAccessException
     */
    @Override
    public int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDataAccessException  {
        final String METHODNAME = "readOrAddParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        int parameterNameId;
        String stmtString;
        String errMsg = "Failure storing search parameter name id: name=" + parameterName;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmtString = String.format(SQL_CALL_ADD_PARAMETER_NAME, getSchemaName());
            try (CallableStatement stmt = getConnection().prepareCall(stmtString)) {
                stmt.setString(1, parameterName);
                stmt.registerOutParameter(2, Types.INTEGER);
                dbCallStartTime = System.nanoTime();
                stmt.execute();
                dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
                if (log.isLoggable(Level.FINE)) {
                        log.fine("DB read/store parameter name id complete. executionTime=" + dbCallDuration + "ms");
                }
                parameterNameId = stmt.getInt(2);
            }
        } catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return parameterNameId;
    }
}
