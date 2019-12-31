/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2GetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.schema.control.Db2GetResourceTypeList;
import com.ibm.fhir.schema.model.ResourceType;

public class TestTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(TestTenantAction.class.getName());

    private String adminSchemaName;
    private String tenantName;
    private String schemaName;
    private String tenantKey;

    public TestTenantAction(String adminSchemaName, String schemaName, String tenantName, String tenantKey) {
        this.adminSchemaName = adminSchemaName;
        this.schemaName      = schemaName;
        this.tenantName      = tenantName;
        this.tenantKey       = tenantKey;
    }

    @Override
    public void run(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // The tenants table, variable and set_tenant procedure are all located in
        // the admin schema. The data access user only has execute privileges on the
        // set_tenant procedure and read access to the variable. The variable can
        // only be set by calling the stored procedure
        Db2SetTenantVariable cmd = new Db2SetTenantVariable(adminSchemaName, tenantName, tenantKey);
        adapter.runStatement(cmd);

        Db2GetTenantVariable getter = new Db2GetTenantVariable(adminSchemaName);
        Integer tid = adapter.runStatement(getter);
        if (tid == null) {
            throw new IllegalStateException("SV_TENANT_ID not set!");
        }

        // Print the id from the session variable (used for access control)
        logger.info("tenantName='" + tenantName + "', tenantId=" + tid);

        // Now let's check we can run a select against one our tenant-based
        // tables
        Db2GetResourceTypeList rtListGetter = new Db2GetResourceTypeList(schemaName);
        List<ResourceType> rtList = adapter.runStatement(rtListGetter);
        rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
    }

    @Override
    public void dryRun(JdbcTarget target, Db2Adapter adapter, ITransactionProvider transactionProvider) {
        // The tenants table, variable and set_tenant procedure are all located in
        // the admin schema. The data access user only has execute privileges on the
        // set_tenant procedure and read access to the variable. The variable can
        // only be set by calling the stored procedure
        Db2SetTenantVariable cmd = new Db2SetTenantVariable(adminSchemaName, tenantName, tenantKey);
        adapter.runStatement(cmd);

        Db2GetTenantVariable getter = new Db2GetTenantVariable(adminSchemaName);
        Integer tid = adapter.runStatement(getter);
        if (tid == null) {
            throw new IllegalStateException("SV_TENANT_ID not set!");
        }

        // Print the id from the session variable (used for access control)
        logger.info("tenantName='" + tenantName + "', tenantId=" + tid);

        // Now let's check we can run a select against one our tenant-based
        // tables
        Db2GetResourceTypeList rtListGetter = new Db2GetResourceTypeList(schemaName);
        List<ResourceType> rtList = adapter.runStatement(rtListGetter);
        rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
    }
}