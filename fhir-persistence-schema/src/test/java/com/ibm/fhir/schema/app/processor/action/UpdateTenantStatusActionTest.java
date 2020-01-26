/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * AddTenantKeyAction Test
 */
public class UpdateTenantStatusActionTest {
    @Test
    public void testUpdateTenantStatusActionTest() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        actionBean.setTenantId(1);
        actionBean.setAdminSchemaName("ADMIN_SCHEMA");
        actionBean.setStatus(TenantStatus.ALLOCATED);
        IDatabaseAdapter adapter = DatabaseSupport.generateTenantAdapter();

        UpdateTenantStatusAction action = new UpdateTenantStatusAction();
        action.run(actionBean, null, adapter, null);
        assertNotNull(actionBean.getAdminSchemaName());
    }
}