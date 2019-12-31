/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.database.utils.tenant.AddTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.GetTenantDAO;
import com.ibm.fhir.schema.app.processor.TenantUtil;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

public class AddTenantKeyAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(AddTenantKeyAction.class.getName());

    private String adminSchemaName;
    private String addKeyForTenant; 

    public AddTenantKeyAction(String adminSchemaName, String addKeyForTenant) {
        this.adminSchemaName = adminSchemaName;
        this.addKeyForTenant = addKeyForTenant;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {

        final String tenantKey = TenantUtil.getRandomKey();

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = TenantUtil.getRandomKey();

        GetTenantDAO tid = new GetTenantDAO(adminSchemaName, addKeyForTenant);
        Tenant tenant = adapter.runStatement(tid);

        if (tenant != null) {
            // Attach the new tenant key to the tenant:
            AddTenantKeyDAO adder =
                    new AddTenantKeyDAO(adminSchemaName, tenant.getTenantId(), tenantKey, tenantSalt,
                            FhirSchemaConstants.TENANT_SEQUENCE);
            adapter.runStatement(adder);
        } else {
            throw new IllegalArgumentException("Tenant does not exist: " + addKeyForTenant);
        }
        logger.info("New tenant key: " + addKeyForTenant + " [key=" + tenantKey + "]");
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {

        final String tenantKey = TenantUtil.getRandomKey();

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = TenantUtil.getRandomKey();

        GetTenantDAO tid = new GetTenantDAO(adminSchemaName, addKeyForTenant);
        Tenant tenant = adapter.runStatement(tid);

        if (tenant != null) {
            // Attach the new tenant key to the tenant:
            AddTenantKeyDAO adder =
                    new AddTenantKeyDAO(adminSchemaName, tenant.getTenantId(), tenantKey, tenantSalt,
                            FhirSchemaConstants.TENANT_SEQUENCE);
            adapter.runStatement(adder);
        } else {
            throw new IllegalArgumentException("Tenant does not exist: " + addKeyForTenant);
        }
        logger.info("New tenant key: " + addKeyForTenant + " [key=" + tenantKey + "]");
    }
}