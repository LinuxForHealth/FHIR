/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control.processor.action;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;

public class UpdateTenantStatusAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(UpdateTenantStatusAction.class.getName());

    private String adminSchemaName;
    private int tenantId;
    private String tenantName;
    private String tenantKey;
    private TenantStatus status;

    public UpdateTenantStatusAction(String adminSchemaName, String tenantKey, String tenantName, int tenantId, TenantStatus status) {
        this.adminSchemaName = adminSchemaName;
        this.tenantId        = tenantId;
        this.tenantName      = tenantName;
        this.tenantKey       = tenantKey;
        this.status = status;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        adapter.updateTenantStatus(adminSchemaName, tenantId, status);
        logger.info("Allocated tenant: " + tenantName + " [key=" + tenantKey + "] with Id = " + tenantId);
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        adapter.updateTenantStatus(adminSchemaName, tenantId, status);
        logger.info("Allocated tenant: " + tenantName + " [key=" + tenantKey + "] with Id = " + tenantId);
    }
}