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

public class UpdateProceduresAction implements ISchemaAction {

    private String schemaName;
    private String adminSchemaName;

    public UpdateProceduresAction(String schemaName, String adminSchemaName) {
        this.schemaName      = schemaName;
        this.adminSchemaName = adminSchemaName;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {

        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        pdm.applyProcedures(adapter);
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        FhirSchemaGenerator gen = new FhirSchemaGenerator(adminSchemaName, schemaName);
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        pdm.applyProcedures(adapter);
    }
}
