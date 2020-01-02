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
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

public class DropTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(DropTenantAction.class.getName());

    public DropTenantAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way
        logger.info("Marking tenant for drop: " + actionBean.getTenantName());
        int tenantId = adapter.findTenantId(actionBean.getSchemaName(), actionBean.getTenantName());
        if (tenantId < 1) {
            throw new IllegalArgumentException(
                    "Tenant '" + actionBean.getTenantName() + "' not found in schema " + actionBean.getSchemaName());
        }

        // Mark the tenant as frozen before we proceed with dropping anything
        adapter.updateTenantStatus(actionBean.getSchemaName(), tenantId, TenantStatus.FROZEN);

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(actionBean.getAdminSchemaName(), actionBean.getSchemaName());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Remove all the tenant-based data
        pdm.removeTenantPartitions(adapter, actionBean.getSchemaName(), tenantId);
        pdm.dropOldTenantTables();
        pdm.dropTenantTablespace();

        actionBean.setTenantId(tenantId);
    }
}