/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.citus;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import javax.transaction.TransactionSynchronizationRegistry;

import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dto.Resource;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
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
            + " WHERE LR.LOGICAL_ID = ? "
            + "   AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID "
            + "   AND R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID"; // additional predicate using common Citus distribution column

    /**
     * Public constructor
     * 
     * @param connection
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public CitusResourceDAO(Connection connection, String schemaName, FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(connection, schemaName, flavor, cache, rrd);
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
        ParameterTransactionDataImpl ptdi) {
        super(connection, schemaName, flavor, trxSynchRegistry, cache, rrd, ptdi);
    }

    @Override
    public Resource read(String logicalId, String resourceType) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "read";
        log.entering(CLASSNAME, METHODNAME);

        Resource resource = null;
        List<Resource> resources;
        String stmtString = null;

        try {
            stmtString = String.format(SQL_READ, resourceType, resourceType);
            resources = this.runQuery(stmtString, logicalId);
            if (!resources.isEmpty()) {
                resource = resources.get(0);
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resource;
    }
}