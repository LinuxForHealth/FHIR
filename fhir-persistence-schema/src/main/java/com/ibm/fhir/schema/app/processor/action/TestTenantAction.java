/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.db2.Db2GetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.schema.control.Db2GetResourceTypeList;
import com.ibm.fhir.schema.model.ResourceType;

public class TestTenantAction implements ISchemaAction {
    private static final Logger logger = Logger.getLogger(TestTenantAction.class.getName());

    public TestTenantAction() {
        // No Operation
    }

    @Override
    public void run(ActionBean actionBean, IDatabaseTarget target, IDatabaseAdapter adapter,
            ITransactionProvider transactionProvider) throws SchemaActionException {
        // The tenants table, variable and set_tenant procedure are all located in
        // the admin schema. The data access user only has execute privileges on the
        // set_tenant procedure and read access to the variable. The variable can
        // only be set by calling the stored procedure
        Db2SetTenantVariable cmd =
                new Db2SetTenantVariable(actionBean.getAdminSchemaName(), actionBean.getTenantName(),
                        actionBean.getTenantKey());
        adapter.runStatement(cmd);

        Db2GetTenantVariable getter = new Db2GetTenantVariable(actionBean.getAdminSchemaName());
        Integer tid = adapter.runStatement(getter);
        if (tid == null) {
            throw new IllegalStateException("SV_TENANT_ID not set!");
        }

        // Print the id from the session variable (used for access control)
        logger.info("tenantName='" + actionBean.getTenantName() + "', tenantId=" + tid);

        // Now let's check we can run a select against one our tenant-based
        // tables
        Db2GetResourceTypeList rtListGetter = new Db2GetResourceTypeList(actionBean.getSchemaName());
        List<ResourceType> rtList = adapter.runStatement(rtListGetter);
        rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
    }
}