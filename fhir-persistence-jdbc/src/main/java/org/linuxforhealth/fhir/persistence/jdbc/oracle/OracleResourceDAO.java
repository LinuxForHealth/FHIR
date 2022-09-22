/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.oracle;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.MetricHandle;
import org.linuxforhealth.fhir.database.utils.common.CalendarHelper;
import org.linuxforhealth.fhir.persistence.InteractionStatus;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.FHIRDAOConstants;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.Resource;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import org.linuxforhealth.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import org.linuxforhealth.fhir.persistence.jdbc.util.FHIRPersistenceJDBCMetric;
import org.linuxforhealth.fhir.persistence.params.api.FhirRefSequenceDAO;

/**
 * Data access object for writing FHIR resources to an postgresql database using
 * the stored procedure (or function, in this case)
 */
public class OracleResourceDAO extends ResourceDAOImpl {
    private static final String CLASSNAME = OracleResourceDAO.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private static final String SQL_READ_RESOURCE_TYPE = "{CALL %s.add_resource_type(?, ?)}";
    
    // 13 args (9 in, 4 out)
    private static final String SQL_INSERT_WITH_PARAMETERS = "{CALL %s.add_any_resource(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    // DAO used to obtain sequence values from FHIR_REF_SEQUENCE
    private FhirRefSequenceDAO fhirRefSequenceDAO;

    /**
     * Public constructor used in runtimes without UserTransaction support
     * @param connection
     * @param schemaName
     * @param flavor
     * @param cache
     */
    public OracleResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache) {
        super(connection, schemaName, flavor, cache);
    }

    /**
     * Public constructor used when UserTransaction is available
     * @param connection
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     * @param cache
     * @param ptdi
     * @param shardKey
     */
    public OracleResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache,
        ParameterTransactionDataImpl ptdi) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, ptdi);
    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, 
            ParameterDAO parameterDao, Integer ifNoneMatch)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert(Resource, List<ExtractedParameterValue, ParameterDAO>";
        logger.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        String stmtString = null;
        Timestamp lastUpdated;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            // Just make sure this resource type is known to the database before we
            // hit the procedure
            Objects.requireNonNull(getResourceTypeId(resource.getResourceType()));

            stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, getSchemaName());

            try (CallableStatement stmt = connection.prepareCall(stmtString)) {
                int arg = 1;
                stmt.setString(arg++, resource.getResourceType());
                stmt.setString(arg++, resource.getLogicalId());
                
                if (resource.getDataStream() != null) {
                    stmt.setBinaryStream(arg++, resource.getDataStream().inputStream());
                } else {
                    // payload was offloaded to another data store
                    stmt.setNull(arg++, Types.BINARY);
                }
    
                lastUpdated = resource.getLastUpdated();
                stmt.setTimestamp(arg++, lastUpdated, CalendarHelper.getCalendarForUTC());
                stmt.setString(arg++, resource.isDeleted() ? "Y": "N");
                stmt.setInt(arg++, resource.getVersionId());
                stmt.setString(arg++, parameterHashB64);
                setInt(stmt, arg++, ifNoneMatch);
                setString(stmt, arg++, resource.getResourcePayloadKey());
                
                // TODO use a helper function which can return the arg index to help clean up the syntax
                stmt.registerOutParameter(arg, Types.BIGINT);  final int logicalResourceIdIndex  = arg++;
                stmt.registerOutParameter(arg, Types.VARCHAR); final int oldParameterHashIndex   = arg++;
                stmt.registerOutParameter(arg, Types.INTEGER); final int interactionStatusIndex  = arg++;
                stmt.registerOutParameter(arg, Types.INTEGER); final int ifNoneMatchVersionIndex = arg++;
    
                dbCallStartTime = System.nanoTime();
                try (MetricHandle m = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_ADD_ANY_RESOURCE.name())) {
                    stmt.execute();
                }
                dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
    
                resource.setLogicalResourceId(stmt.getLong(logicalResourceIdIndex));
                if (stmt.getInt(interactionStatusIndex) == 1) { // interaction status
                    // no change, so skip parameter updates
                    resource.setInteractionStatus(InteractionStatus.IF_NONE_MATCH_EXISTED);
                    resource.setIfNoneMatchVersion(stmt.getInt(ifNoneMatchVersionIndex)); // current version
                } else {
                    resource.setInteractionStatus(InteractionStatus.MODIFIED);
                    resource.setCurrentParameterHash(stmt.getString(oldParameterHashIndex));
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Successfully inserted Resource. logicalResourceId=" + resource.getLogicalResourceId() + " executionTime=" + dbCallDuration + "ms");
                }
            }
        } catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        } catch(SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(logger, fx, e);
        } catch(SQLException e) {
            if (FHIRDAOConstants.SQLSTATE_WRONG_VERSION.equals(e.getSQLState())) {
                // this is just a concurrency update, so there's no need to log the SQLException here
                throw new FHIRPersistenceVersionIdMismatchException("Encountered version id mismatch while inserting Resource");
            } else {
                FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.");
                throw severe(logger, fx, e);
            }
        } catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Resource.");
            throw severe(logger, fx, e);
        } finally {
            logger.exiting(CLASSNAME, METHODNAME);
        }

        return resource;
    }

    /**
     * Read the id for the named type
     * @param resourceTypeName
     * @param conn
     * @return the database id, or null if the named record is not found
     * @throws SQLException
     */
    protected Integer getResourceTypeId(String resourceTypeName, Connection conn) throws SQLException {
        Integer result = null;

        final String sql = "SELECT resource_type_id FROM resource_types WHERE resource_type = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceTypeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        }

        return result;
    }

    /**
     * stored-procedure-less implementation for managing the resource_types table
     * @param resourceTypeName
     * @param conn
     * @throw SQLException
     */
    public int getOrCreateResourceType(String resourceTypeName, Connection conn) throws SQLException {
        // As the system is concurrent, we have to handle cases where another thread
        // might create the entry after we selected and found nothing
        Integer result = getResourceTypeId(resourceTypeName, conn);

        // Create the resource if we don't have it already (set by the continue handler)
        if (result == null) {
            try {
                result = fhirRefSequenceDAO.nextValue();
                final String INS = "INSERT INTO resource_types (resource_type_id, resource_type) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(INS)) {
                    // bind parameters
                    stmt.setInt(1, result);
                    stmt.setString(2, resourceTypeName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                throw e;
            }
        }

        return result;
    }

    @Override
    public Integer readResourceTypeId(String resourceType) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException  {
        final String METHODNAME = "readResourceTypeId";
        logger.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        CallableStatement stmt = null;
        Integer resourceTypeId = null;
        String stmtString;
        long dbCallStartTime;
        double dbCallDuration;

        try {
            stmtString = String.format(SQL_READ_RESOURCE_TYPE, getSchemaName());
            stmt = connection.prepareCall(stmtString);
            stmt.setString(1, resourceType);
            stmt.registerOutParameter(2, Types.INTEGER);
            dbCallStartTime = System.nanoTime();
            stmt.execute();
            dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
            if (logger.isLoggable(Level.FINER)) {
                logger.finer("DB read resource type id complete. executionTime=" + dbCallDuration + "ms");
            }
            resourceTypeId = stmt.getInt(2);
        } catch (Throwable e) {
            final String errMsg = "Failure storing Resource type name id: name=" + resourceType;
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException(errMsg);
            throw severe(logger, fx, e);
        } finally {
            cleanup(stmt);
            logger.exiting(CLASSNAME, METHODNAME);
        }
        return resourceTypeId;
    }
}