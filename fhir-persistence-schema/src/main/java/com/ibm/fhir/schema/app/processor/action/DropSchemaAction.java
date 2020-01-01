/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class DropSchemaAction implements ISchemaAction {

    public DropSchemaAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        if (actionBean.isDropSchema()) {
            // Just drop the objects associated with the FHIRDATA schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
        }

        if (actionBean.isDropAdmin()) {
            // Just drop the objects associated with the ADMIN schema group
            pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
        }
    }

    @Override
    public void dryRun(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) {
        run(actionBean, target, adapter, transactionProvider);
    }
}