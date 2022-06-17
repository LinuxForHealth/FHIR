/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.citus;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.database.utils.common.PreparedStatementHelper;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceVersionIdMismatchException;
import com.ibm.fhir.persistence.index.FHIRRemoteIndexService;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDAOConstants;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.JDBCIdentityCacheImpl;
import com.ibm.fhir.persistence.jdbc.dao.impl.ParameterVisitorBatchDAO;
import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresResourceDAO;

/**
 * Data access object for writing FHIR resources to Citus database using
 * the stored procedure (or function, in this case)
 */
public class CitusResourceDAO extends PostgresResourceDAO {
    private static final String CLASSNAME = CitusResourceDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    // @formatter:off
    //                                                                                   0                 1
    //                                                                                   1 2 3 4 5 6 7 8 9 0 1 2 3 4 5
    // @formatter:on
    // Don't forget that we must account for IN and OUT parameters.
    private static final String SQL_INSERT_WITH_PARAMETERS = "{ CALL %s.add_any_resource(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";
    private static final String SQL_LOGICAL_RESOURCE_IDENT = "{ CALL %s.add_logical_resource_ident(?,?,?) }";

    // Read the current version of the resource (even if the resource has been deleted)
    private static final String SQL_READ = ""
            + "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID, R.RESOURCE_PAYLOAD_KEY "
            + "  FROM %s_RESOURCES R, "
            + "       %s_LOGICAL_RESOURCES LR "
            + " WHERE R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID "
            + "   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID " // join must use common Citus distribution column
            + "   AND LR.LOGICAL_RESOURCE_ID = ? "; // lookup using logical_resource_id

    // Read a specific version of the resource
    private static final String SQL_VERSION_READ = ""
            + "SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID, R.RESOURCE_PAYLOAD_KEY " 
            + "  FROM %s_RESOURCES R, "
            + "       %s_LOGICAL_RESOURCES LR "
            + " WHERE LR.LOGICAL_RESOURCE_ID = ? "
            + "   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID "
            + "   AND R.VERSION_ID = ?";

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public CitusResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd, Short shardKey) {
        super(connection, schemaName, flavor, cache, rrd, shardKey);
    }

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param flavor
     * @param trxSynchRegistry
     * @param cache
     * @param rrd
     * @param ptdi
     */
    public CitusResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, TransactionSynchronizationRegistry trxSynchRegistry, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd,
        ParameterTransactionDataImpl ptdi, Short shardKey) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi, shardKey);
    }

    /**
     * Read the logical_resource_id value from logical_resource_ident
     * @param resourceType
     * @param logicalId
     * @return
     */
    private Long getLogicalResourceIdentId(String resourceType, String logicalId) throws FHIRPersistenceDataAccessException {
        final int resourceTypeId = getCache().getResourceTypeCache().getId(resourceType);
        final Long logicalResourceId;
        final String selectLogicalResourceIdent = ""
                + "SELECT logical_resource_id "
                + "  FROM logical_resource_ident "
                + " WHERE resource_type_id = ? "
                + "   AND logical_id = ? "; // distribution key
        try (PreparedStatement ps = getConnection().prepareStatement(selectLogicalResourceIdent)) {
            ps.setInt(1, resourceTypeId);
            ps.setString(2, logicalId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                logicalResourceId = rs.getLong(1);
            } else {
                logicalResourceId = null;
            }
        } catch (SQLException x) {
            log.log(Level.SEVERE, "read '" + resourceType + "/" + logicalId + "'", x);
            throw new FHIRPersistenceDataAccessException("read failed for logical resource ident record");
        }
        return logicalResourceId;
    }

    @Override
    public Resource read(String logicalId, String resourceType) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "read";
        log.entering(CLASSNAME, METHODNAME);

        // For Citus we want to first query the logical_resource_ident table because it is
        // distributed by the logicalId. This gets us the logical_resource_id value which
        // we can then use to access the logical_resource tables which are distributed by
        // logical_resource_id
        Long logicalResourceId = getLogicalResourceIdentId(resourceType, logicalId);
        if (logicalResourceId == null) {
            return null;
        }

        Resource resource = null;
        List<Resource> resources;
        String stmtString = null;

        try {
            stmtString = String.format(SQL_READ, resourceType, resourceType);
            resources = this.runQuery(stmtString, logicalResourceId);
            if (!resources.isEmpty()) {
                resource = resources.get(0);
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resource;
    }

    @Override
    public Resource versionRead(String logicalId, String resourceType, int versionId) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "versionRead";
        log.entering(CLASSNAME, METHODNAME);

        // For Citus we want to first query the logical_resource_ident table because it is
        // distributed by the logicalId. This gets us the logical_resource_id value which
        // we can then use to access the logical_resource tables which are distributed by
        // logical_resource_id
        Long logicalResourceId = getLogicalResourceIdentId(resourceType, logicalId);
        if (logicalResourceId == null) {
            return null;
        }

        Resource resource = null;
        List<Resource> resources;
        String stmtString = null;

        try {
            stmtString = String.format(SQL_VERSION_READ, resourceType, resourceType);
            resources = this.runQuery(stmtString, logicalResourceId, versionId);
            if (!resources.isEmpty()) {
                resource = resources.get(0);
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resource;

    }

    @Override
    public Resource insert(Resource resource, List<ExtractedParameterValue> parameters, String parameterHashB64, 
            ParameterDAO parameterDao, Integer ifNoneMatch)
            throws FHIRPersistenceException {
        final String METHODNAME = "insert(Resource, List<ExtractedParameterValue, ParameterDAO>";
        log.entering(CLASSNAME, METHODNAME);

        final Connection connection = getConnection(); // do not close
        long dbCallStartTime;
        double dbCallDuration;

        try {
            // Just make sure this resource type is known to the database before we
            // hit the procedure
            Integer resourceTypeId = getResourceTypeId(resource.getResourceType());
            Objects.requireNonNull(resourceTypeId);

            // For Citus, we first make a call to establish the logical_resource_ident record
            long logicalResourceId = createOrLockLogicalResourceIdent(resourceTypeId, resource.getLogicalId());

            final String stmtString = String.format(SQL_INSERT_WITH_PARAMETERS, getSchemaName());
            try (CallableStatement stmt = connection.prepareCall(stmtString)) {
                PreparedStatementHelper psh = new PreparedStatementHelper(stmt);
    
                psh.setLong(logicalResourceId);
                psh.setInt(resourceTypeId);
                psh.setString(resource.getResourceType());
                psh.setString(resource.getLogicalId());
                psh.setBinaryStream(resource.getDataStream() != null ? resource.getDataStream().inputStream() : null);
                psh.setTimestamp(resource.getLastUpdated());
                psh.setString(resource.isDeleted() ? "Y": "N");
                psh.setString(UUID.randomUUID().toString());
                psh.setInt(resource.getVersionId());
                psh.setString(parameterHashB64);
                psh.setInt(ifNoneMatch);
                psh.setString(resource.getResourcePayloadKey());
                
                final int oldParameterHashIndex   = psh.registerOutParameter(Types.VARCHAR);
                final int interactionStatusIndex  = psh.registerOutParameter(Types.INTEGER);
                final int ifNoneMatchVersionIndex = psh.registerOutParameter(Types.INTEGER);
    
                dbCallStartTime = System.nanoTime();
                stmt.execute();
                dbCallDuration = (System.nanoTime()-dbCallStartTime)/1e6;
    
                resource.setLogicalResourceId(logicalResourceId);
                if (stmt.getInt(interactionStatusIndex) == 1) { // interaction status
                    // no change, so skip parameter updates
                    resource.setInteractionStatus(InteractionStatus.IF_NONE_MATCH_EXISTED);
                    resource.setIfNoneMatchVersion(stmt.getInt(ifNoneMatchVersionIndex)); // current version
                } else {
                    resource.setInteractionStatus(InteractionStatus.MODIFIED);
        
                    // Parameter time
                    // To keep things simple for the postgresql use-case, we just use a visitor to
                    // handle inserts of parameters directly in the resource parameter tables.
                    // Note we don't get any parameters for the resource soft-delete operation
                    // Bypass the parameter insert here if we have the remoteIndexService configured
                    FHIRRemoteIndexService remoteIndexService = FHIRRemoteIndexService.getServiceInstance();
                    final String currentParameterHash = stmt.getString(oldParameterHashIndex);
                    if (remoteIndexService == null
                            && parameters != null && (parameterHashB64 == null || parameterHashB64.isEmpty()
                            || !parameterHashB64.equals(currentParameterHash))) {
                        // postgresql doesn't support partitioned multi-tenancy, so we disable it on the DAO:
                        JDBCIdentityCache identityCache = new JDBCIdentityCacheImpl(getCache(), this, parameterDao, getResourceReferenceDAO());
                        try (ParameterVisitorBatchDAO pvd = new ParameterVisitorBatchDAO(connection, null, resource.getResourceType(), false, resource.getLogicalResourceId(), 100,
                            identityCache, getResourceReferenceDAO(), getTransactionData())) {
                            for (ExtractedParameterValue p: parameters) {
                                p.accept(pvd);
                            }
                        }
                    }
                }
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Successfully inserted Resource. logicalResourceId=" + resource.getLogicalResourceId() + " executionTime=" + dbCallDuration + "ms");
                }
            }
        } catch(FHIRPersistenceDBConnectException | FHIRPersistenceDataAccessException e) {
            throw e;
        } catch(SQLIntegrityConstraintViolationException e) {
            FHIRPersistenceFKVException fx = new FHIRPersistenceFKVException("Encountered FK violation while inserting Resource.");
            throw severe(log, fx, e);
        } catch(SQLException e) {
            if (FHIRDAOConstants.SQLSTATE_WRONG_VERSION.equals(e.getSQLState())) {
                // this is just a concurrency update, so there's no need to log the SQLException here
                throw new FHIRPersistenceVersionIdMismatchException("Encountered version id mismatch while inserting Resource");
            } else {
                FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("SQLException encountered while inserting Resource.");
                throw severe(log, fx, e);
            }
        } catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Resource.");
            throw severe(log, fx, e);
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return resource;
    }

    /**
     * Call the ADD_LOGICAL_RESOURCE_IDENT procedure to create or lock (select for update)
     * the logical_resource_ident record. For Citus we run this step first because this
     * function is distributed by the logical_id parameter.
     * @param resourceTypeId
     * @param logicalId
     * @return
     * @throws SQLException
     */
    protected long createOrLockLogicalResourceIdent(int resourceTypeId, String logicalId) throws SQLException {
        long logicalResourceId;

        final String stmtString = String.format(SQL_LOGICAL_RESOURCE_IDENT, getSchemaName());
        try (CallableStatement cs = getConnection().prepareCall(stmtString)) {
            PreparedStatementHelper psh = new PreparedStatementHelper(cs);
            psh.setInt(resourceTypeId);
            psh.setString(logicalId);
            int idxLogicalResourceId = psh.registerOutParameter(Types.BIGINT);
            cs.execute();
            logicalResourceId = cs.getLong(idxLogicalResourceId);
        }

        // At this point the logical_resource_ident record will be locked for update
        return logicalResourceId;
    }
}