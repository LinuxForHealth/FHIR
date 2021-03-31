/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This DAO uses a connection provided to its constructor. It's therefore
 * assumed to be a short-lived object, created on-the-fly. Caching etc is handled
 * elsewhere...we're just doing JDBC stuff here.
 */
public class CodeSystemDAOImpl implements CodeSystemDAO {
    private static final Logger log = Logger.getLogger(CodeSystemDAOImpl.class.getName());
    private static final String CLASSNAME = CodeSystemDAOImpl.class.getName();

    private static final String SQL_CALL_ADD_CODE_SYSTEM_ID = "CALL %s.add_code_system(?, ?)";

    private static final String SQL_SELECT_ALL_CODE_SYSTEMS = "SELECT CODE_SYSTEM_ID, CODE_SYSTEM_NAME FROM CODE_SYSTEMS";

    private static final String SQL_SELECT_CODE_SYSTEM_ID = "SELECT CODE_SYSTEM_ID FROM CODE_SYSTEMS WHERE CODE_SYSTEM_NAME = ?";

    // The JDBC connection used by this DAO instance
    private final Connection connection;
    
    // name of the FHIR data schema
    private final String schemaName;

    /**
     * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
     */
    public CodeSystemDAOImpl(Connection c, String schemaName) {
        this.connection = c;
        this.schemaName = schemaName;
    }

    /**
     * Provide subclasses with access to the {@link Connection}
     * @return
     */
    protected Connection getConnection() {
        return this.connection;
    }

    /**
     * Getter for the FHIR data schema
     * @return
     */
    protected String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public Map<String, Integer> readAllCodeSystems() throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "readAllCodeSystems";
        log.entering(CLASSNAME, METHODNAME);

        ResultSet resultSet = null;
        String systemName;
        int systemId;
        Map<String, Integer> systemMap = new HashMap<>();
        String errMsg = "Failure retrieving all code systems.";
        long dbCallStartTime;
        double dbCallDuration;

        dbCallStartTime = System.nanoTime();
        try (PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_ALL_CODE_SYSTEMS)) {
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB read all code systems complete. executionTime=" + dbCallDuration + "ms");
            }
            while (resultSet.next()) {
                systemId = resultSet.getInt(1);
                systemName = resultSet.getString(2);
                systemMap.put(systemName, systemId);
            }
        }
        catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return systemMap;
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
        String stmtString;
        String errMsg = "Failure storing code system id: name=" + systemName;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmtString = String.format(SQL_CALL_ADD_CODE_SYSTEM_ID, this.schemaName);
            try (CallableStatement stmt = connection.prepareCall(stmtString)) {
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
        }
        catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return systemId;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.dao.api.CodeSystemDAO#readCodeSystemId(java.lang.String)
     */
    @Override
    public Integer readCodeSystemId(String codeSystem) throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "readCodeSystemId";
        log.entering(CLASSNAME, METHODNAME);

        Integer result;
        ResultSet resultSet = null;
        String errMsg = "Failure retrieving code system. name=" + codeSystem;
        long dbCallStartTime;
        double dbCallDuration;

        try (PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_CODE_SYSTEM_ID)) {
            stmt.setString(1, codeSystem);
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (log.isLoggable(Level.FINE)) {
                log.fine("DB read code system complete. executionTime=" + dbCallDuration + "ms");
            }

            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            else {
                result = null;
            }
        }
        catch (Throwable e) {
            throw new FHIRPersistenceDataAccessException(errMsg,e);
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return result;
    }
}
