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
import com.ibm.fhir.schema.app.processor.TenantUtil;
import com.ibm.fhir.schema.control.FhirSchemaConstants;

public class AllocateTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(AllocateTenantAction.class.getName());

    private String adminSchemaName;
    private String tenantName;
    private String schemaName;
    private int tenantId;

    public AllocateTenantAction(String adminSchemaName, String schemaName, String tenantName) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantName      = tenantName;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // The key we'll use for this tenant. This key should be used in subsequent
        // activities related to this tenant, such as setting the tenant context.
        final String tenantKey = TenantUtil.getRandomKey();

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = TenantUtil.getRandomKey();

        // Open a new transaction and associate it with our connection pool. Remember
        // that we don't support distributed transactions, so all connections within
        // this transaction must come from the same pool
        logger.info("Allocating new tenant: " + tenantName + " [key=" + tenantKey + "]");

        tenantId =
                adapter.allocateTenant(adminSchemaName, schemaName, tenantName, tenantKey, tenantSalt,
                        FhirSchemaConstants.TENANT_SEQUENCE);

        // The tenant-id is important because this is also used to identify the partition number
        logger.info("Tenant Id[" + tenantName + "] = " + tenantId);
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // The key we'll use for this tenant. This key should be used in subsequent
        // activities related to this tenant, such as setting the tenant context.
        final String tenantKey = TenantUtil.getRandomKey();

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = TenantUtil.getRandomKey();

        // Open a new transaction and associate it with our connection pool. Remember
        // that we don't support distributed transactions, so all connections within
        // this transaction must come from the same pool
        logger.info("Allocating new tenant: " + tenantName + " [key=" + tenantKey + "]");
        tenantId =
                adapter.allocateTenant(adminSchemaName, schemaName, tenantName, tenantKey, tenantSalt,
                        FhirSchemaConstants.TENANT_SEQUENCE);

        // The tenant-id is important because this is also used to identify the partition number
        logger.info("Tenant Id[" + tenantName + "] = " + tenantId);
    }

    public int getTenantId() {
        return tenantId;
    }
}