/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.impl.ParameterTransactionDataImpl;
import com.ibm.fhir.persistence.jdbc.postgres.PostgresResourceDAO;

/**
 * Data access object for writing FHIR resources to Citus database using
 * the stored procedure (or function, in this case)
 */
public class CitusResourceDAO extends PostgresResourceDAO {
    private static final String CLASSNAME = CitusResourceDAO.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

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

}