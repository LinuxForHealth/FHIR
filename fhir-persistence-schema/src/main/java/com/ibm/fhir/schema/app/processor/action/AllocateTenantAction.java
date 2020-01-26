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
import com.ibm.fhir.schema.app.processor.SchemaUtil;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

public class AllocateTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(AllocateTenantAction.class.getName());

    public AllocateTenantAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // The key we'll use for this tenant. This key should be used in subsequent
        // activities related to this tenant, such as setting the tenant context.
        final String tenantKey = SchemaUtil.getRandomKey();

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = SchemaUtil.getRandomKey();

        // Open a new transaction and associate it with our connection pool. Remember
        // that we don't support distributed transactions, so all connections within
        // this transaction must come from the same pool
        logger.info("Allocating new tenant: " + actionBean.getTenantName() + " [key=" + tenantKey + "]");

        int tenantId =
                adapter.allocateTenant(actionBean.getAdminSchemaName(), actionBean.getSchemaName(),
                        actionBean.getTenantName(),
                        tenantKey, tenantSalt,
                        FhirSchemaConstants.TENANT_SEQUENCE);

        // The tenant-id is important because this is also used to identify the partition number
        logger.info("Tenant Id[" + actionBean.getTenantName() + "] = " + tenantId);
        actionBean.setTenantId(tenantId);
    }
}