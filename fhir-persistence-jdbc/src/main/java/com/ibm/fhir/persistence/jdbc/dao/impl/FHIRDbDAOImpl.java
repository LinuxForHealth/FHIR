/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.database.utils.api.BadTenantFrozenException;
import com.ibm.fhir.database.utils.api.BadTenantKeyException;
import com.ibm.fhir.database.utils.api.BadTenantNameException;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBCleanupException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This class is a root Data Access Object for managing JDBC access to the FHIR database. It contains common functions
 * for managing connections, closing used
 * JDBC resources, and running database queries.
 */
public class FHIRDbDAOImpl implements FHIRDbDAO {
    private static final Logger log = Logger.getLogger(FHIRDbDAOImpl.class.getName());
    private static final String CLASSNAME = FHIRDbDAOImpl.class.getName();
    private static final String NEWLINE = System.getProperty("line.separator");

    // Used to indicate the default behavior of a datastore as multitenant.
    public static final List<String> DATASTORE_REQUIRES_ROW_PERMISSIONS = Arrays.asList("db2");

    private static DataSource fhirDb = null;
    private static String datasourceJndiName = null;

    private Properties dbProps = null;
    private Connection externalConnection = null;
    private static boolean dbDriverLoaded = false;

    // Abstract source of configured connections
    private final IConnectionProvider connectionProvider;

    /**
     * Constructs a DAO instance suitable for acquiring DB connections via JNDI from the app server.
     */
    public FHIRDbDAOImpl() {
        super();
        this.connectionProvider = null;
    }

    /**
     * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific
     * properties.
     *
     * @param dbProperties
     */
    public FHIRDbDAOImpl(Properties dbProperties) {
        this.setDbProps(dbProperties);
        this.connectionProvider = null;
    }

    /**
     * Constructs a DAO using the passed externally managed database connection.
     * The connection used by this instance for all DB operations will be the passed connection.
     *
     * @param Connection - A database connection that will be managed by the caller.
     */
    public FHIRDbDAOImpl(Connection conn) {
        this();
        this.setExternalConnection(conn);
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

    @Override
    public Connection getConnection() throws FHIRPersistenceDBConnectException {
        final String METHODNAME = "getConnection";

        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, METHODNAME);
        }

        try {
            Connection connection = null;
            String dbDriverName = null;
            String dbUrl;

            if (this.getExternalConnection() != null) {
                connection = this.getExternalConnection();
            } else if (this.connectionProvider != null) {
                try {
                    connection = connectionProvider.getConnection();
                } catch (SQLException x) {
                    FHIRPersistenceDBConnectException fx =
                            new FHIRPersistenceDBConnectException(
                                    "Failed to acquire database connection from provider");
                    throw severe(log, fx, x);
                }
            } else if (this.getDbProps() == null) {
                try {
                    String tenantId = FHIRRequestContext.get().getTenantId();
                    String dsId = FHIRRequestContext.get().getDataStoreId();
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Getting connection for tenantId/dsId: [" + tenantId + "/" + dsId + "]...");
                    }

                    // As this connection is part of a pool, we don't make this try-catch-close.
                    connection = getFhirDatasource().getConnection(tenantId, dsId);

                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Got the connection for [" + tenantId + "/" + dsId + "]!");
                    }
                } catch (Throwable e) {
                    // Don't emit secrets in case they are returned to a client
                    FHIRPersistenceDBConnectException fx =
                            new FHIRPersistenceDBConnectException("Failure acquiring connection for datasource");
                    throw severe(log, fx, "Failure acquiring connection for datasource: " + getDataSourceJndiName(), e);
                }
            } else {
                if (!dbDriverLoaded) {
                    try {
                        dbDriverName = this.getDbProps().getProperty(PROPERTY_DB_DRIVER);
                        Class.forName(dbDriverName);
                        dbDriverLoaded = true;
                    } catch (ClassNotFoundException e) {
                        // Not concerned about revealing a classname in the exception
                        throw new FHIRPersistenceDBConnectException("Failed to load driver: " + dbDriverName, e);
                    }
                }

                dbUrl = this.getDbProps().getProperty(PROPERTY_DB_URL);
                try {
                    connection = DriverManager.getConnection(dbUrl, this.getDbProps());

                    // Most queries assume the current schema is set up properly
                    String schemaName = this.getDbProps().getProperty("schemaName", "FHIRDATA");
                    connection.setSchema(schemaName);
                } catch (Throwable e) {
                    // Don't emit secrets like the dbUrl in case they are returned to a client
                    FHIRPersistenceDBConnectException fx =
                            new FHIRPersistenceDBConnectException("Failed to acquire DB connection");
                    throw severe(log, fx, "Failed to acquire DB connection. dbUrl=" + dbUrl, e);
                }
            }

            executeRowAccessFeature(connection);

            return connection;
        } catch (FHIRPersistenceDBConnectException e) {
            throw e;
        } catch (Throwable t) {
            FHIRPersistenceDBConnectException fx =
                    new FHIRPersistenceDBConnectException(
                            "An unexpected error occurred while connecting to the database.");
            throw severe(log, fx, t);
        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
    }

    /**
     * this feature is executes row access control
     *
     * @param connection
     * @throws Exception
     */
    public void executeRowAccessFeature(Connection connection) throws Exception {
        // For the multi-tenant feature, configure the connection for the tenant by setting the sv_tenant_id.
        String tenantName = FHIRRequestContext.get().getTenantId();
        String tenantKey = null;
        String dsId = FHIRRequestContext.get().getDataStoreId();
        boolean multiTenantFeature = false;

        // Retrieve the property group pertaining to the specified datastore.
        // Find and set the tenantKey for the request, otherwise subsequent pulls from the pool
        // miss the tenantKey.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + dsId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
        if (dsPG != null) {
            tenantKey = dsPG.getStringProperty("tenantKey", null);
            if (log.isLoggable(Level.FINE)) {
                log.finer("tenantKey is null? = [" + Objects.isNull(tenantKey) + "]");
            }

            // Specific to Db2 right now, we want to switch behavior if multitenant row level permission is required.
            String type = dsPG.getStringProperty("type", null);
            if (type != null) {
                // Based on the default for the database type, the code.
                multiTenantFeature =
                        dsPG.getBooleanProperty("multitenant", DATASTORE_REQUIRES_ROW_PERMISSIONS.contains(type));
            }
        } else {
            log.fine("there are no datasource properties found for : [" + dsPropertyName + "]");
        }

        if (multiTenantFeature) {
            if (Objects.isNull(tenantKey)) {
                // Should have been set.
                throw buildFHIRPersistenceDBConnectException("MISSING TENANT KEY [" + tenantName + "]",
                        IssueType.EXCEPTION);
            }
            if (log.isLoggable(Level.FINE)) {
                log.fine("Setting tenant access on connection for: [" + tenantName + "]");
            }

            // At this point, tenantName and tenantKey should be non-null.
            Db2SetTenantVariable cmd = new Db2SetTenantVariable("FHIR_ADMIN", tenantName, tenantKey);
            JdbcTarget target = new JdbcTarget(connection);
            Db2Adapter adapter = new Db2Adapter(target);
            try {
                adapter.runStatement(cmd);
            } catch (BadTenantKeyException x) {
                throw buildFHIRPersistenceDBConnectException("MISSING OR INVALID TENANT KEY [" + tenantName + "]",
                        IssueType.EXCEPTION);
            } catch (BadTenantNameException x) {
                throw buildFHIRPersistenceDBConnectException("MISSING OR INVALID TENANT NAME [" + tenantName + "]",
                        IssueType.EXCEPTION);
            } catch (BadTenantFrozenException x) {
                throw buildFHIRPersistenceDBConnectException("TENANT FROZEN [" + tenantName + "]", IssueType.EXCEPTION);
            }
        }
    }

    /**
     * Retrieves the datasource JNDI name to be used from the fhir server configuration.
     *
     * @return the datasource JNDI name
     * @throws Exception
     */
    private static String getDataSourceJndiName() throws Exception {
        if (datasourceJndiName == null) {
            datasourceJndiName =
                    FHIRConfiguration.getInstance().loadConfiguration().getStringProperty(
                            FHIRConfiguration.PROPERTY_JDBC_DATASOURCE_JNDINAME, FHIRDbDAO.FHIRDB_JNDI_NAME_DEFAULT);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Using datasource JNDI name: " + datasourceJndiName);
            }
        }
        return datasourceJndiName;
    }

    /**
     * Looks up and returns a Datasource JDBC object representing the FHIR database via JNDI.
     *
     * @return
     * @throws Exception
     */
    private static DataSource getFhirDatasource() throws Exception {
        final String METHODNAME = "getFhirDatasource";
        log.entering(CLASSNAME, METHODNAME);
        try {
            if (fhirDb == null) {
                acquireFhirDb();
            }
            return fhirDb;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    private static synchronized void acquireFhirDb() throws Exception {
        final String METHODNAME = "acquireFhirDb";
        if (log.isLoggable(Level.FINEST)) {
            log.entering(CLASSNAME, METHODNAME);
        }

        try {
            InitialContext ctxt;

            if (fhirDb == null) {
                try {
                    ctxt = new InitialContext();
                    fhirDb = (DataSource) ctxt.lookup(getDataSourceJndiName());
                } catch (Throwable e) {
                    FHIRException fx = new FHIRPersistenceDBConnectException("Failure acquiring datasource");
                    log.log(Level.SEVERE, fx.addProbeId("Failure acquiring datasource: " + getDataSourceJndiName()), e);
                    throw fx;
                }
            }

        } finally {
            if (log.isLoggable(Level.FINEST)) {
                log.exiting(CLASSNAME, METHODNAME);
            }
        }
    }

    /**
     * Closes the passed PreparedStatement and Connection objects.
     *
     * @param stmt
     * @param connection
     */
    protected void cleanup(PreparedStatement stmt, Connection connection) {
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
        if (connection != null && this.getExternalConnection() == null) {
            try {
                connection.close();
            } catch (Throwable e) {
                ce = new FHIRPersistenceDBCleanupException("Failure closing Connection.", e);
                log.log(Level.SEVERE, ce.getMessage(), ce);
            }
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Closes the passed ResultSet, PreparedStatement, and Connection objects.
     *
     * @param resultSet
     * @param stmt
     * @param connection
     */
    protected void cleanup(ResultSet resultSet, PreparedStatement stmt, Connection connection) {
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
        this.cleanup(stmt, connection);
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
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], JDBCConstants.UTC);
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
                log.fine("Successfully retrieved FHIR objects. SQL=" + sql + "  searchArgs="
                        + Arrays.toString(searchArgs) + " executionTime=" + dbCallDuration + "ms");
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
            this.cleanup(resultSet, stmt, connection);
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
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg =
                "Failure retrieving count. SQL=" + sql + NEWLINE + "  searchArgs=" + Arrays.toString(searchArgs);
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], JDBCConstants.UTC);
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
                    log.fine("Successfully retrieved count. SQL=" + sql + NEWLINE + "  searchArgs="
                            + Arrays.toString(searchArgs) + NEWLINE + "  count=" + rowCount + " executionTime="
                            + dbCallDuration + "ms");
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
            this.cleanup(resultSet, stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }

        return rowCount;
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

    private Properties getDbProps() {
        return dbProps;
    }

    private void setDbProps(Properties dbProps) {
        this.dbProps = dbProps;
    }

    @Override
    public Connection getExternalConnection() {
        return externalConnection;
    }

    @Override
    public void setExternalConnection(Connection externalConnection) {
        this.externalConnection = externalConnection;
    }

    @Override
    public boolean isDb2Database() throws FHIRPersistenceDBConnectException, SQLException {
        String dbUrl = this.getConnection().getMetaData().getURL();
        dbUrl = dbUrl.toLowerCase();
        return dbUrl.contains("db2");
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
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        String errMsg;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(sql);
            // Inject arguments into the prepared stmt.
            for (int i = 0; i < searchArgs.length; i++) {
                if (searchArgs[i] instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) searchArgs[i], JDBCConstants.UTC);
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
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            // avoid leaking SQL because the exception message might be returned to a client
            FHIRPersistenceDataAccessException fx =
                    new FHIRPersistenceDataAccessException("Failure retrieving string values");
            errMsg = "Failure retrieving string values. SQL=" + sql + "  searchArgs=" + Arrays.toString(searchArgs);
            throw severe(log, fx, errMsg, e);
        } finally {
            this.cleanup(resultSet, stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        return strValues;
    }
}
