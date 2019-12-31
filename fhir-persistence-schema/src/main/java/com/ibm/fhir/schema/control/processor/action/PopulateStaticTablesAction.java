/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.Db2AddResourceType;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class PopulateStaticTablesAction implements ISchemaAction {
    private String adminSchemaName;
    private String tenantName;
    private String schemaName;
    private String tenantKey;

    public PopulateStaticTablesAction(String adminSchemaName, String schemaName, String tenantName, String tenantKey) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantName      = tenantName;
        this.tenantKey       = tenantKey;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Very important. Establish the value of the session variable so that we
        // pass the row permission predicate
        Db2SetTenantVariable cmd = new Db2SetTenantVariable(adminSchemaName, tenantName, tenantKey);
        adapter.runStatement(cmd);

        // Now get all the resource types and insert them into the tenant-based table
        gen.applyResourceTypes(c -> {
            Db2AddResourceType art = new Db2AddResourceType(schemaName, c);
            adapter.runStatement(art);
        });
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Very important. Establish the value of the session variable so that we
        // pass the row permission predicate
        Db2SetTenantVariable cmd = new Db2SetTenantVariable(adminSchemaName, tenantName, tenantKey);
        adapter.runStatement(cmd);

        // Now get all the resource types and insert them into the tenant-based table
        gen.applyResourceTypes(c -> {
            Db2AddResourceType art = new Db2AddResourceType(schemaName, c);
            adapter.runStatement(art);
        });
    }
}