/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static com.ibm.fhir.schema.control.FhirSchemaConstants.TENANT_SEQUENCE;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.database.utils.tenant.AddTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.GetTenantDAO;
import com.ibm.fhir.schema.app.processor.SchemaUtil;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * <pre>
 * --prop-file db2.properties
 * --schema-name FHIRDATA
 * --pool-size 2
 * --add-tenant-key TENANT1
 * --dry-run
 * </pre>
 */
public class AddTenantKeyAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(AddTenantKeyAction.class.getName());

    public AddTenantKeyAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = SchemaUtil.getRandomKey();
        final String tenantKey = SchemaUtil.getRandomKey();

        GetTenantDAO tid = new GetTenantDAO(actionBean.getAdminSchemaName(), actionBean.getAddKeyForTenant());
        Tenant tenant = adapter.runStatement(tid);

        String addKeyForTenant = actionBean.getAddKeyForTenant();
        checkValidTenant(tenant, addKeyForTenant);

        // Attach the new tenant key to the tenant:
        String adminSchemaName = actionBean.getAdminSchemaName();
        int tenantId = tenant.getTenantId();
        AddTenantKeyDAO adder = new AddTenantKeyDAO(adminSchemaName, tenantId, tenantKey, tenantSalt, TENANT_SEQUENCE);
        adapter.runStatement(adder);

        logger.info("New tenant key: [" + addKeyForTenant + "] [key=" + tenantKey + "]");
        actionBean.setTenantKey(tenantKey);
    }

    public void checkValidTenant(Tenant tenant, String addKeyForTenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant does not exist: [" + addKeyForTenant + "]");
        }
    }
}