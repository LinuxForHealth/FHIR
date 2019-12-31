/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class DropSchemaAction implements ISchemaAction {

    private String schemaName;
    private String adminSchemaName;

    private boolean dropSchema = false;
    private boolean dropAdmin = false;

    public DropSchemaAction(String schemaName, String adminSchemaName, boolean dropSchema, boolean dropAdmin) {
        this.schemaName      = schemaName;
        this.adminSchemaName = adminSchemaName;
        this.dropSchema      = dropSchema;
        this.dropAdmin = dropAdmin;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        if (this.dropSchema) {
            // Just drop the objects associated with the FHIRDATA schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
        }

        if (dropAdmin) {
            // Just drop the objects associated with the ADMIN schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
        }
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        if (this.dropSchema) {
            // Just drop the objects associated with the FHIRDATA schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
        }

        if (dropAdmin) {
            // Just drop the objects associated with the ADMIN schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
        }
    }
}
