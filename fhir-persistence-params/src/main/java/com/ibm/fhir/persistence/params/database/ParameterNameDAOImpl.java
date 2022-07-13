/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.params.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.params.api.ParameterNameDAO;

/**
 * Database interaction for parameter_names. Caching etc is handled
 * elsewhere...we're just doing JDBC stuff here.
 *
 * @implNote this class references NAME but in fact uses `code` from SearchParameter.
 */
public class ParameterNameDAOImpl implements ParameterNameDAO {
    private static final Logger log = Logger.getLogger(ParameterNameDAOImpl.class.getName());
    private static final String CLASSNAME = ParameterNameDAOImpl.class.getName();

    private static final String SQL_SELECT_ALL_SEARCH_PARAMETER_NAMES = "SELECT PARAMETER_NAME_ID, PARAMETER_NAME FROM PARAMETER_NAMES";

    private static final String SQL_SELECT_PARAMETER_NAME_ID = "SELECT PARAMETER_NAME_ID FROM PARAMETER_NAMES WHERE PARAMETER_NAME = ?";

    private static final String SQL_CALL_ADD_PARAMETER_NAME = "CALL %s.add_parameter_name(?, ?)";

    // The JDBC connection to be used by this instance of the DAO
    private final Connection connection;
    
    // The FHIR data schema
    private final String schemaName;

    /**
     * Public constructor
     */
    public ParameterNameDAOImpl(Connection connection, String schemaName) {
        this.connection = connection;
        this.schemaName = schemaName;
    }

    /**
     * Provide the connection to subclasses
     * @return
     */
    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * Get the name of the FHIR data schema we are using
     * @return
     */
    protected String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public Map<String, Integer> readAllSearchParameterNames()
                                         throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllSearchParameterNames";
        log.entering(CLASSNAME, METHODNAME);

        ResultSet resultSet = null;
        String parameterName;
        int parameterId;
        Map<String, Integer> parameterMap = new HashMap<>();
        String errMsg = "Failure retrieving all Search Parameter names.";
        long dbCallStartTime;
        double dbCallDuration;

        dbCallStartTime = System.nanoTime();
        try (PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_ALL_SEARCH_PARAMETER_NAMES)) {
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                    log.fine("DB read all search parameter names complete. executionTime=" + dbCallDuration + "ms");
            }

            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                parameterId = resultSet.getInt(1);
                parameterName = resultSet.getString(2);
                parameterMap.put(parameterName, parameterId);
            }
        } catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return parameterMap;
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
            stmtString = String.format(SQL_CALL_ADD_PARAMETER_NAME, schemaName);
            try (CallableStatement stmt = connection.prepareCall(stmtString)) {
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

    @Override
    public Integer readParameterNameId(String parameterName) throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "readParameterNameId";
        log.entering(CLASSNAME, METHODNAME);

        Integer result;
        ResultSet resultSet = null;
        String errMsg = "Failure retrieving parameter name. name=" + parameterName;
        long dbCallStartTime;
        double dbCallDuration;

        try (PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_PARAMETER_NAME_ID)) {
            stmt.setString(1, parameterName);
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB select parameter_name_id. executionTime=" + dbCallDuration + "ms");
            }

            if (resultSet.next()) {
                result = resultSet.getInt(1);
            } else {
                result = null;
            }
        } catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return result;
    }
}
