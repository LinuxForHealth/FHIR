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
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;
import com.ibm.fhir.task.api.ITaskCollector;

/**
 * ApplyModelAction Test
 */
public class ApplyModelActionTest {
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testCheckValidNullVersionHistoryService() {
        ApplyModelAction action = new ApplyModelAction();
        action.checkValid(null, null, null);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testCheckValidNullITaskCollector() {
        ApplyModelAction action = new ApplyModelAction();
        String adminSchema = "ADMIN_SCHEMA";
        String schema = "SCHEMA";
        VersionHistoryService vhs = new VersionHistoryService(adminSchema, schema);
        action.checkValid(vhs, null, null);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testCheckValidNullPhysicalDataModel() {
        ApplyModelAction action = new ApplyModelAction();
        String adminSchema = "ADMIN_SCHEMA";
        String schema = "SCHEMA";
        VersionHistoryService vhs = new VersionHistoryService(adminSchema, schema);
        ITaskCollector collector = DatabaseSupport.generateTaskCollector(true);
        action.checkValid(vhs, collector, null);
    }

    @Test
    public void testAction() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        IDatabaseAdapter adapter = DatabaseSupport.generateTenantAdapter();
        ApplyModelAction action = new ApplyModelAction();

        PhysicalDataModel pdm = new PhysicalDataModel();
        actionBean.setPhysicalDataModel(pdm);

        String adminSchema = "ADMIN_SCHEMA";
        String schema = "SCHEMA";
        VersionHistoryService vhs = new VersionHistoryService(adminSchema, schema);
        actionBean.setVersionHistoryService(vhs);
        actionBean.setCollector(DatabaseSupport.generateTaskCollector(true));
        action.run(actionBean, target, adapter, null);
        assertNotNull(actionBean);
    }

    @Test
    public void testActionFailedTaskGroups() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();
        JdbcTarget target = new JdbcTarget(DatabaseSupport.generateConnection(true, null));
        IDatabaseAdapter adapter = DatabaseSupport.generateTenantAdapter();
        ApplyModelAction action = new ApplyModelAction();

        PhysicalDataModel pdm = new PhysicalDataModel();
        actionBean.setPhysicalDataModel(pdm);

        String adminSchema = "ADMIN_SCHEMA";
        String schema = "SCHEMA";
        VersionHistoryService vhs = new VersionHistoryService(adminSchema, schema);
        actionBean.setVersionHistoryService(vhs);
        actionBean.setCollector(DatabaseSupport.generateTaskCollector(false));
        action.run(actionBean, target, adapter, null);
        assertNotNull(actionBean);
    }
}