/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * CreateSchemaAction Test
 */
public class CreateFhirSchemasActionTest {
    @Test
    public void testAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        Db2Adapter adapter = new Db2Adapter(target);
        CreateSchemaAction action = new CreateSchemaAction();
        action.run(actionBean, target, adapter, null);
        assertNotNull(action);
    }

    @Test
    public void testActionSkipped() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        DerbyAdapter adapter = new DerbyAdapter(target);
        CreateSchemaAction action = new CreateSchemaAction();
        action.run(actionBean, target, adapter, null);
        assertNotNull(action);
    }

    @Test(expectedExceptions = { DataAccessException.class })
    public void testActionFailed() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target =
                new JdbcTarget(DatabaseSupport.generateConnection(false, DatabaseSupport.generateUndefinedName()));
        Db2Adapter adapter = new Db2Adapter(target);
        CreateSchemaAction action = new CreateSchemaAction();
        action.run(actionBean, target, adapter, null);
        assertNotNull(action);
    }
}