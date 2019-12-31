/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class DropTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(DropTenantAction.class.getName());

    private String adminSchemaName;
    private String schemaName;
    private String tenantName;
    private int tenantId;

    public DropTenantAction(String schemaName, String adminSchemaName, String tenantName) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantName      = tenantName;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way
        logger.info("Marking tenant for drop: " + tenantName);
        tenantId = adapter.findTenantId(schemaName, tenantName);
        if (tenantId < 1) {
            throw new IllegalArgumentException("Tenant '" + tenantName + "' not found in schema " + schemaName);
        }

        // Mark the tenant as frozen before we proceed with dropping anything
        adapter.updateTenantStatus(schemaName, tenantId, TenantStatus.FROZEN);

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Remove all the tenant-based data
        pdm.removeTenantPartitions(adapter, schemaName, tenantId);
        pdm.dropOldTenantTables();
        pdm.dropTenantTablespace();
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way
        logger.info("Marking tenant for drop: " + tenantName);
        tenantId = adapter.findTenantId(schemaName, tenantName);
        if (tenantId < 1) {
            throw new IllegalArgumentException("Tenant '" + tenantName + "' not found in schema " + schemaName);
        }

        // Mark the tenant as frozen before we proceed with dropping anything
        adapter.updateTenantStatus(schemaName, tenantId, TenantStatus.FROZEN);

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Remove all the tenant-based data
        pdm.removeTenantPartitions(adapter, schemaName, tenantId);
        pdm.dropOldTenantTables();
        pdm.dropTenantTablespace();
    }

    public int getTenantId() {
        return tenantId;
    }
}