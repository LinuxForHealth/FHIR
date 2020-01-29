/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * VersionHistoryServiceActionTest Test
 */
public class VersionHistoryServiceActionTest {
    @Test
    public void testVersionHistoryServiceAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        actionBean.setTenantId(1);
        actionBean.setAdminSchemaName("ADMIN_SCHEMA");
        actionBean.setSchemaName("FHIR_SCHEMA");
        IDatabaseAdapter adapter = DatabaseSupport.generateMapAdapter(null); //DatabaseSupport.generateUndefinedName()
        ITransactionProvider transactionProvider = DatabaseSupport.generateTransactionProvider();
        VersionHistoryServiceAction action = new VersionHistoryServiceAction();
        action.run(actionBean, null, adapter, transactionProvider);
        assertNotNull(actionBean.getAdminSchemaName());
    }

    @Test(expectedExceptions = {SchemaActionException.class})
    public void testVersionHistoryServiceActionUndefined() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        actionBean.setTenantId(1);
        actionBean.setAdminSchemaName("ADMIN_SCHEMA");
        actionBean.setSchemaName("FHIR_SCHEMA");
        IDatabaseAdapter adapter = DatabaseSupport.generateMapAdapter(DatabaseSupport.generateUndefinedName());
        ITransactionProvider transactionProvider = DatabaseSupport.generateTransactionProvider();
        VersionHistoryServiceAction action = new VersionHistoryServiceAction();
        action.run(actionBean, null, adapter, transactionProvider);
    }
}