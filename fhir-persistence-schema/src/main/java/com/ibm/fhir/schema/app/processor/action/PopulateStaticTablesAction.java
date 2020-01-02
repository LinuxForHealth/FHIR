/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.Db2AddResourceType;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class PopulateStaticTablesAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(PopulateStaticTablesAction.class.getName());

    public PopulateStaticTablesAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        if (actionBean.getTenantName() == null || actionBean.getTenantName().isEmpty()) {
            throw new IllegalStateException("Missing tenant name");
        }

        if (actionBean.getTenantKey() == null || actionBean.getTenantKey().isEmpty()) {
            throw new IllegalArgumentException("No tenant-key value provided");
        }

        logger.info("Testing tenant: " + actionBean.getTenantName());

        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Very important. Establish the value of the session variable so that we
        // pass the row permission predicate
        Db2SetTenantVariable cmd =
                new Db2SetTenantVariable(actionBean.getAdminSchemaName(), actionBean.getTenantName(),
                        actionBean.getTenantKey());
        adapter.runStatement(cmd);

        // Now get all the resource types and insert them into the tenant-based table
        gen.applyResourceTypes(c -> {
            Db2AddResourceType art = new Db2AddResourceType(actionBean.getSchemaName(), c);
            adapter.runStatement(art);
        });
    }
}