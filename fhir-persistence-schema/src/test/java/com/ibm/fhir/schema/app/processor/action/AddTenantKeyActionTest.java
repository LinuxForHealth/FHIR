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
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * AddTenantKeyAction Test
 */
public class AddTenantKeyActionTest {
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testCheckValidTenantBadTenant() {
        AddTenantKeyAction action = new AddTenantKeyAction();
        String addKeyForTenant = "tenantId";
        action.checkValidTenant(null, addKeyForTenant);
    }

    @Test
    public void testCheckValidTenant() {
        AddTenantKeyAction action = new AddTenantKeyAction();
        String addKeyForTenant = "tenantId";
        Tenant tenant = new Tenant();
        action.checkValidTenant(tenant, addKeyForTenant);
        assert true;
    }

    @Test
    public void testAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        IDatabaseAdapter adapter = DatabaseSupport.generateTenantAdapter();
        AddTenantKeyAction action = new AddTenantKeyAction();
        action.run(actionBean, target, adapter, null);
        assertNotNull(actionBean.getTenantKey());
    }
}