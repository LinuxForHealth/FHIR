/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.database.utils.common.DatabaseTranslatorFactory;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.query.QueryUtil;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBCleanupException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class is a root Data Access Object for managing JDBC access to the FHIR database.
 * As of 4.3.0, connection handling is factored out of the DAO and is instead the
 * responsibility of the FHIRPersistenceJDBCImpl. Each DAO therefore requires a
 * connection to be passed to it. This is good for separation of concerns, because the
 * DAO code shouldn't care where the connection comes from. As each DAO is stateless,
 * and lightweight, they can be created on-the-fly and handed a connection in the
 * constructor. This pattern avoids polluting every method call with a connection
 * parameter.
 */
public class FHIRDbDAOImpl implements FHIRDbDAO {
    private static final Logger log = Logger.getLogger(FHIRDbDAOImpl.class.getName());
    private static final String CLASSNAME = FHIRDbDAOImpl.class.getName();
    private static final String NEWLINE = System.getProperty("line.separator");

    // The connection the DAO operates against
    private final Connection connection;

    // The name of the FHIR data schema (containing the FHIR resource tables).
    private final String schemaName;

    // The type of database we are connected to. Perhaps should use translator instead?
    private final FHIRDbFlavor flavor;

    /**
     * Constructs a DAO instance suitable for acquiring DB connections via JNDI from the app server.
     */
    public FHIRDbDAOImpl(Connection connection, String schemaName, FHIRDbFlavor flavor) {
        super();
        this.connection = connection;
        this.schemaName = schemaName;
        this.flavor = flavor;
    }

    /**
     * Getter for the connection being used by this DAO.
     * @return
     */
    @Override
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Getter for the schema name we've been configured for
     * @return
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    @Override
    public FHIRDbFlavor getFlavor() {
        return this.flavor;
    }

    /**
     * Convenience function to log the cause of an exception about to be thrown. This
     * is useful when avoiding chaining the cause with the persistence exception, which
     * could inadvertently leak sensitive information (details of the schema, for example)
     *
     * @param logger
     * @param fx
     * @param cause
     * @return
     */
    protected <XT extends FHIRPersistenceException> XT severe(Logger logger, XT fx, Throwable cause) {
        logger.log(Level.SEVERE, fx.getMessage(), cause);
        return fx;
    }

    /**
     * Log the exception message here along with the cause stack. Return the
     * exception fx to the caller so that it can be thrown easily.
     *
     * @param logger
     * @param fx
     * @param errorMessage
     * @param cause
     * @return
     */
    protected <XT extends FHIRPersistenceException> XT severe(Logger logger, XT fx, String errorMessage,
            Throwable cause) {
        if (cause != null) {
            logger.log(Level.SEVERE, fx.addProbeId(errorMessage), cause);
        } else {
            logger.log(Level.SEVERE, fx.addProbeId(errorMessage));
        }
        return fx;
    }

    /**
     * Closes the passed PreparedStatement.
     *
     * @param stmt
     * @param connection
     */
    protected void cleanup(PreparedStatement stmt) {
        final String METHODNAME = "cleanup(PreparedStatement, Connection)";
        log.entering(CLASSNAME, METHODNAME);

        FHIRPersistenceDBCleanupException ce;

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Throwable e) {
                ce = new FHIRPersistenceDBCleanupException("Failure closing PreparedStatement.", e);
                log.log(Level.SEVERE, ce.getMessage(), ce);
            }
        }

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Closes the passed ResultSet, PreparedStatement. We are no longer responsible
     * for Connections so don't close them
     *
     * @param resultSet
     * @param stmt
     */
    protected void cleanup(ResultSet resultSet, PreparedStatement stmt) {
        final String METHODNAME = "cleanup(PreparedStatement, Connection)";
        log.entering(CLASSNAME, METHODNAME);

        FHIRPersistenceDBCleanupException ce;

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Throwable e) {
                ce = new FHIRPersistenceDBCleanupException("Failure closing ResultSet.", e);
                log.log(Level.SEVERE, ce.getMessage(), ce);
            }
        }
        this.cleanup(stmt);
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Creates and executes a PreparedStatement using the passed parameters that returns a collection of FHIR Data
     * Transfer Objects of type T.
     *
     * @param sql        - The SQL template to execute.
     * @param searchArgs - An array of arguments to be substituted into the SQL template.
     * @return List<T> - A List of FHIR Data Transfer Objects resulting from the executed query.
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    protected List<Resource> runQuery(String sql, Object... searchArgs)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "runQuery";
        log.entering(CLASSNAME, METHODNAME);

        List<Resource> fhirObjects = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], CalendarHelper.getCalendarForUTC());
                } else {
                    stmt.setObject(i + 1, searchArgs[i]);
                }
            }
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            // Transform the resultSet into a collection of Data Transfer Objects
            fhirObjects = this.createDTOs(resultSet);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully retrieved FHIR objects; SQL=" + sql + NEWLINE + "searchArgs="
                        + Arrays.toString(searchArgs) + " [took " + dbCallDuration + " ms]");
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            // avoid leaking SQL because the exception message might be returned to a client
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Failure retrieving FHIR objects");
            errMsg = "Failure retrieving FHIR objects. SQL=" + sql + "  searchArgs=" + Arrays.toString(searchArgs);
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return fhirObjects;
    }

    /**
     * Creates and executes a PreparedStatement for the passed sql containing a 'SELECT COUNT...'.
     * The count value is extracted from the ResultSet and returned as an int.
     *
     * @param sql        - The SQL SELECT COUNT template to execute.
     * @param searchArgs - An array of arguments to be substituted into the SQL template.
     * @return int - The count of results returned by the SQL query.
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    protected int runCountQuery(String sql, Object... searchArgs)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "runCountQuery";
        log.entering(CLASSNAME, METHODNAME);

        int rowCount = 0;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg =
                "Failure retrieving count. SQL=" + sql + NEWLINE + "  searchArgs=" + Arrays.toString(searchArgs);
        long dbCallStartTime;
        double dbCallDuration;

        try {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Count SQL = " + sql + NEWLINE + "; searchArgs="
                        + Arrays.toString(searchArgs));
            }
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], CalendarHelper.getCalendarForUTC());
                } else {
                    stmt.setObject(i + 1, searchArgs[i]);
                }
            }
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Successfully retrieved count; count=" + rowCount + " [took "
                            + dbCallDuration + " ms]");
                }
            } else {
                // Don't emit the SQL text in an exception - it risks returning it to the client in a response
                FHIRPersistenceDataAccessException fx =
                        new FHIRPersistenceDataAccessException("Server error: failure retrieving count");
                throw severe(log, fx, errMsg, null);
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            // Don't emit the SQL text in an exception - it risks returning it to the client in a response
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Server error: failure retrieving count");
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return rowCount;
    }

    /**
     * Creates and executes a PreparedStatement for the passed sql containing a 'SELECT COUNT...'.
     * The count value is extracted from the ResultSet and returned as an int.
     *
     * @param countQuery - The SQL SELECT COUNT statement and bind variables to execute.
     * @return int - The count of results returned by the SQL query.
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    protected int runCountQuery(Select countQuery)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "runCountQuery";
        log.entering(CLASSNAME, METHODNAME);

        int rowCount = 0;
        long dbCallStartTime;
        double dbCallDuration;

        // Query string is FINE logged inside prepareSelect, so no need to log again here
        try (PreparedStatement stmt = QueryUtil.prepareSelect(connection, countQuery, getTranslator())) {
            dbCallStartTime = System.nanoTime();
            ResultSet resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Successfully retrieved count; count=" + rowCount + " [took "
                            + dbCallDuration + " ms]");
                }
            } else {
                // Don't emit the SQL text in an exception - it risks returning it to the client in a response
                FHIRPersistenceDataAccessException fx =
                        new FHIRPersistenceDataAccessException("Server error: failure retrieving count");
                throw severe(log, fx, countQuery.toDebugString(), null);
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            // Don't emit the SQL text in an exception - it risks returning it to the client in a response
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Server error: failure retrieving count");
            throw severe(log, fx, countQuery.toDebugString(), e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return rowCount;
    }

    /**
     * Retrieve the FHIR objects by executing the given {@link Select} statement
     * @param select
     * @return
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    protected List<Resource> runQuery(Select select)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "runQuery";
        log.entering(CLASSNAME, METHODNAME);

        List<Resource> fhirObjects = new ArrayList<>();
        ResultSet resultSet = null;
        long dbCallStartTime;
        double dbCallDuration;

        try (PreparedStatement stmt = QueryUtil.prepareSelect(connection, select, getTranslator())) {
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;
            // Transform the resultSet into a collection of Data Transfer Objects
            fhirObjects = this.createDTOs(resultSet);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully retrieved FHIR objects [took " + dbCallDuration + " ms]");
            }
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            // avoid leaking SQL because the exception message might be returned to a client
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Failure retrieving FHIR objects");
            throw severe(log, fx, select.toDebugString(), e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return fhirObjects;
    }


    /**
     * An method for creating a collection of Data Transfer Objects of type T from the contents of the passed ResultSet.
     *
     * @param resultSet A ResultSet containing FHIR persistent object data.
     * @return List<Resource> - A collection of FHIR Data Transfer objects of the same type.
     * @throws FHIRPersistenceDataAccessException
     */
    protected List<Resource> createDTOs(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
        final String METHODNAME = "createDTOs";
        log.entering(CLASSNAME, METHODNAME);

        Resource dto;
        List<Resource> dtoList = new ArrayList<Resource>();

        try {
            while (resultSet.next()) {
                dto = this.createDTO(resultSet);
                if (dto != null) {
                    dtoList.add(dto);
                }
            }
        } catch (Throwable e) {
            // Don't chain the cause, because we might leak secrets
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure creating DTOs.");
            throw severe(log, fx, e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return dtoList;
    }

    /**
     * A method for creating a Data Transfer Object of type T from the contents of the passed ResultSet.
     *
     * @param resultSet A ResultSet containing FHIR persistent object data.
     * @return T - An instance of type T, which is a FHIR Data Transfer Object.
     * @throws FHIRPersistenceDataAccessException
     */
    protected Resource createDTO(ResultSet resultSet) throws FHIRPersistenceDataAccessException {
        // Can be overridden by subclasses that need to return DTOs.
        return null;
    }

    @Override
    public boolean isDb2Database() {
        return this.flavor.getType() == DbType.DB2;
    }

    protected FHIRPersistenceDataAccessException buildExceptionWithIssue(String msg, IssueType issueType)
            throws FHIRPersistenceDataAccessException {
        Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIRPersistenceDataAccessException(msg).withIssue(ooi);
    }

    protected FHIRPersistenceDBConnectException buildFHIRPersistenceDBConnectException(String msg, IssueType issueType)
            throws FHIRPersistenceDBConnectException {
        Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIRPersistenceDBConnectException(msg).withIssue(ooi);
    }

    /**
     * Creates and executes a PreparedStatement using the passed parameters that returns a collection of String values.
     *
     * @param sql        - The SQL template to execute.
     * @param searchArgs - An array of arguments to be substituted into the SQL template.
     * @return List<String> - A List of strings resulting from the executed query.
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    protected List<String> runQuery_STR_VALUES(String sql, Object... searchArgs)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "runQuery_STR_VALUES";
        log.entering(CLASSNAME, METHODNAME);
        List<String> strValues = new ArrayList<String>();
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], CalendarHelper.getCalendarForUTC());
                } else {
                    stmt.setObject(i + 1, searchArgs[i]);
                }
            }
            dbCallStartTime = System.nanoTime();
            resultSet = stmt.executeQuery();
            dbCallDuration = (System.nanoTime() - dbCallStartTime) / 1e6;

            while (resultSet.next()) {
                strValues.add(resultSet.getString(1));
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully retrieved string values. SQL=" + sql + "  searchArgs="
                        + Arrays.toString(searchArgs) + " executionTime=" + dbCallDuration + "ms");
            }
        } catch (Throwable e) {
            // avoid leaking SQL because the exception message might be returned to a client
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Failure retrieving string values");
            errMsg = "Failure retrieving string values. SQL=" + sql + "  searchArgs=" + Arrays.toString(searchArgs);
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return strValues;
    }

    /**
     * Get the translator appropriate for the flavor of database we are using
     * @return
     */
    protected IDatabaseTranslator getTranslator() {
        return DatabaseTranslatorFactory.getTranslator(this.flavor.getType());
    }
}
