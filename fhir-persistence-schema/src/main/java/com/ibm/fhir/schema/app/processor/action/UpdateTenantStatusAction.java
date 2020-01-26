/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * The UpdateTenantStatusAction changes the status for given Tenant. 
 * <br>
 * The valid statuses are:
 * <pre>
 * PROVISIONING
 * FREE
 * ALLOCATED
 * FROZEN
 * DROPPED
 * </pre>
 */
public class UpdateTenantStatusAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(UpdateTenantStatusAction.class.getName());

    public UpdateTenantStatusAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        adapter.updateTenantStatus(actionBean.getAdminSchemaName(), actionBean.getTenantId(), actionBean.getStatus());
        logger.info("Update Tenant Status: " + actionBean.getTenantName() + "] " + actionBean.getStatus());
    }
}