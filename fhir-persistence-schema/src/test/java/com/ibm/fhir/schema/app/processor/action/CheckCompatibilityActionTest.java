/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.derby.DerbyAdapter;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Check Compatibility Action
 */
public class CheckCompatibilityActionTest {
    @Test
    public void testAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        Db2Adapter adapter = new Db2Adapter(target);
        CheckCompatibilityAction action = new CheckCompatibilityAction();
        action.run(actionBean, target, adapter, null);
        assertTrue(actionBean.isCheckCompatibility());
    }

    @Test
    public void testActionSkipped() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        DerbyAdapter adapter = new DerbyAdapter(target);
        CheckCompatibilityAction action = new CheckCompatibilityAction();
        action.run(actionBean, target, adapter, null);
        assertFalse(actionBean.isCheckCompatibility());
    }

    @Test
    public void testActionFailed() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target =
                new JdbcTarget(DatabaseSupport.generateConnection(false, DatabaseSupport.generateUndefinedName()));
        Db2Adapter adapter = new Db2Adapter(target);
        CheckCompatibilityAction action = new CheckCompatibilityAction();
        action.run(actionBean, target, adapter, null);
        assertFalse(actionBean.isCheckCompatibility());
    }
}