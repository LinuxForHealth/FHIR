/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.MetricHandle;
import com.ibm.fhir.database.utils.api.SchemaType;
import com.ibm.fhir.database.utils.common.CalendarHelper;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDAOConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.FhirRefSequenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.util.FHIRPersistenceJDBCMetric;

/**
 * Data access object for writing FHIR resources to an postgresql database using
 * the stored procedure (or function, in this case)
 */
public class PostgresResourceDAO extends ResourceDAOImpl {
    private static final String CLASSNAME = PostgresResourceDAO.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private static final String SQL_READ_RESOURCE_TYPE = "{CALL %s.add_resource_type(?, ?)}";
    
    // 13 args (9 in, 4 out)
    private static final String SQL_INSERT_WITH_PARAMETERS = "{CALL %s.add_any_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    // 14 args (10 in, 4 out)
    private static final String SQL_SHARDED_INSERT_WITH_PARAMETERS = "{CALL %s.add_any_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    private static final String SQL_SHARDED_READ = ""
            + "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, "
            + "       R.DATA, LR.LOGICAL_ID, R.RESOURCE_PAYLOAD_KEY " 
            + "  FROM %s_RESOURCES R, "
            + "       %s_LOGICAL_RESOURCES LR "
            + " WHERE LR.SHARD_KEY = ? "
            + "   AND LR.LOGICAL_ID = ? "
            + "   AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID "
            + "   AND R.SHARD_KEY = LR.SHARD_KEY ";

    // Read a specific version of the resource
    private static final String SQL_SHARDED_VERSION_READ = ""
            + "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, "
            + "       R.DATA, LR.LOGICAL_ID, R.RESOURCE_PAYLOAD_KEY "
            + "  FROM %s_RESOURCES R, "
            + "       %s_LOGICAL_RESOURCES LR "
            + " WHERE LR.SHARD_KEY = ? "
            + "   AND LR.LOGICAL_ID = ? "
            + "   AND R.VERSION_ID = ? "
            + "   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID "
            + "   AND R.SHARD_KEY = LR.SHARD_KEY ";

    // DAO used to obtain sequence values from FHIR_REF_SEQUENCE
    private FhirRefSequenceDAO fhirRefSequenceDAO;

    // The (optional) shard key used with sharded databases
    private final Short shardKey;

    public PostgresResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd, Short shardKey) {
        super(connection, schemaName, flavor, cache, rrd);
        this.shardKey = shardKey;
    }

    public PostgresResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd,
        ParameterTransactionDataImpl ptdi, Short shardKey) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
        this.shardKey = shardKey;
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

            if (getFlavor().getSchemaType() == SchemaType.SHARDED) {
                if (this.shardKey == null) {
                    throw new FHIRPersistenceException("Shard key value required when schema type is SHARDED");
                }
                stmtString = String.format(SQL_SHARDED_INSERT_WITH_PARAMETERS, getSchemaName());
            } else {
                stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, getSchemaName());
            }

            try (CallableStatement stmt = connection.prepareCall(stmtString)) {
                int arg = 1;
                if (getFlavor().getSchemaType() == SchemaType.SHARDED) {
                    stmt.setShort(arg++, shardKey);
                }
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
                stmt.setString(arg++, UUID.randomUUID().toString());
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
        
                    // Parameter time
                    // To keep things simple for the postgresql use-case, we just use a visitor to
                    // handle inserts of parameters directly in the resource parameter tables.
                    // Note we don't get any parameters for the resource soft-delete operation
                    // Bypass the parameter insert here if we have the remoteIndexService configured
//                    FHIRRemoteIndexService remoteIndexService = FHIRRemoteIndexService.getServiceInstance();
//                    final String currentParameterHash = stmt.getString(oldParameterHashIndex);
//                    if (remoteIndexService == null
//                            && parameters != null && (parameterHashB64 == null || parameterHashB64.isEmpty()
//                            || !parameterHashB64.equals(currentParameterHash))) {
//                        // postgresql doesn't support partitioned multi-tenancy, so we disable it on the DAO:
//                        JDBCIdentityCache identityCache = new JDBCIdentityCacheImpl(getCache(), this, parameterDao, getResourceReferenceDAO());
//                        try (MetricHandle m = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_INSERT_PARAMS.name())) {
//                            try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(connection, null, resource.getResourceType(), false, resource.getLogicalResourceId(), 100,
//                                identityCache, getResourceReferenceDAO(), getTransactionData())) {
//                                for (ExtractedParameterValue p: parameters) {
//                                    p.accept(pvd);
//                                }
//                            }
//                        }
//                    }
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

    @Override
    public Resource read(String logicalId, String resourceType) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "read";
        logger.entering(CLASSNAME, METHODNAME);

        Resource resource = null;
        if (getFlavor().getSchemaType() == SchemaType.SHARDED) {
            List<Resource> resources;
            String stmtString = null;
            
            try {
                stmtString = String.format(SQL_SHARDED_READ, resourceType, resourceType);
                resources = this.runQuery(stmtString, shardKey, logicalId);
                if (!resources.isEmpty()) {
                    resource = resources.get(0);
                }
            } finally {
                logger.exiting(CLASSNAME, METHODNAME);
            }
        } else {
            resource = super.read(logicalId, resourceType);
        }
        return resource;
    }

    @Override
    public Resource versionRead(String logicalId, String resourceType, int versionId) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "versionRead";
        logger.entering(CLASSNAME, METHODNAME);

        Resource resource = null;
        if (getFlavor().getSchemaType() == SchemaType.SHARDED) {
            String stmtString = null;

            try {
                stmtString = String.format(SQL_SHARDED_VERSION_READ, resourceType, resourceType);
                List<Resource> resources = this.runQuery(stmtString, shardKey, logicalId, versionId);
                if (!resources.isEmpty()) {
                    resource = resources.get(0);
                }
            } finally {
                logger.exiting(CLASSNAME, METHODNAME);
            }
        } else {
            resource = super.versionRead(logicalId, resourceType, versionId);
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