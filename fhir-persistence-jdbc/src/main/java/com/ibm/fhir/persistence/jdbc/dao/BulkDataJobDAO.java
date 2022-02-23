/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.persistence.bulkdata.InputDTO;
import com.ibm.fhir.persistence.bulkdata.JobManager;
import com.ibm.fhir.persistence.bulkdata.JobStatusDTO;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.connection.FHIRDbFlavor;
import com.ibm.fhir.persistence.jdbc.dao.api.IResourceReferenceDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.ResourceDAOImpl;

/**
 * BulkDataJobDAO is the data access layer of the erase operation which executes
 * directly against the database using SQL statements to:
 * <li>Create a Job</li>
 * <li>Update a Job</li>
 * <li>Delete a Job</li>
 */
public class BulkDataJobDAO extends ResourceDAOImpl implements JobManager {

    private static final String CLASSNAME = BulkDataJobDAO.class.getSimpleName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    // The translator specific to the database type we're working with
    private final IDatabaseTranslator translator;

    // The name of the admin schema where we find the SV_TENANT_ID variable
    private final String adminSchemaName;

    /**
     * Public constructor
     *
     * @param conn
     * @param adminSchemaName
     * @param translator
     * @param schemaName
     * @param flavor
     * @param cache
     * @param rrd
     */
    public BulkDataJobDAO(Connection conn, String adminSchemaName, IDatabaseTranslator translator, String schemaName,
            FHIRDbFlavor flavor, FHIRPersistenceJDBCCache cache, IResourceReferenceDAO rrd) {
        super(conn, schemaName, flavor, cache, rrd);
        this.adminSchemaName = adminSchemaName;
        this.translator = translator;
    }

    @Override
    public void createImportJob(String tenant, String datastore, String source, String outcome, String incomingUrl,
            String extJobId, List<InputDTO> inputs) {
        // Set the default status to DISPATCHED (D)
        // Set the Type to ImportChunk = 5
        // The fields intJobId and output are not set.

    }

    @Override
    public void updateInternalJobId(String extJobId, String intJobId) {

    }

    @Override
    public void createExportJob(String tenant, String datastore, String source, String outcome, String incomingUrl,
            String extJobId, String since, String types, String exportType, String outputFormatStr,
            String typeFiltersStr, String groupId) {
        // Set the default status to DISPATCHED (D)
        // Set the Type to ImportChunk = 5
        // The fields intJobId and output are not set.

    }

    @Override
    public String getInternalJobId(String extJobId) {
        return null;
    }

    @Override
    public JobStatusDTO getStatus(String extJobId) {
        return null;
    }

    @Override
    public void updateJobStatus(String extJobId) {
        // Update the Job Status to DELETED

    }

}