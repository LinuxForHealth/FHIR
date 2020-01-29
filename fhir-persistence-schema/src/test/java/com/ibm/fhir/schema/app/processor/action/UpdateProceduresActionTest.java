/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * UpdateProceduresAction Test
 */
public class UpdateProceduresActionTest {
    @Test
    public void testAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        IDatabaseAdapter adapter = DatabaseSupport.generateTenantAdapter();
        UpdateProceduresAction action = new UpdateProceduresAction();

        String adminSchema = "ADMIN_SCHEMA";
        String schema = "SCHEMA";
        actionBean.setAdminSchemaName(adminSchema);
        actionBean.setSchemaName(schema);
        action.run(actionBean, target, adapter, null);
        assertNotNull(actionBean);
    }
}