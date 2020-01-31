/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.schema.app.processor.ActionProcessor;
import com.ibm.fhir.schema.app.processor.SchemaUtil;
import com.ibm.fhir.schema.app.processor.action.AddTenantKeyAction;
import com.ibm.fhir.schema.app.processor.action.AddTenantPartitionsAction;
import com.ibm.fhir.schema.app.processor.action.AllocateTenantAction;
import com.ibm.fhir.schema.app.processor.action.ApplyModelAction;
import com.ibm.fhir.schema.app.processor.action.CheckCompatibilityAction;
import com.ibm.fhir.schema.app.processor.action.CreateSchemaAction;
import com.ibm.fhir.schema.app.processor.action.CreateVersionHistoryAction;
import com.ibm.fhir.schema.app.processor.action.DropSchemaAction;
import com.ibm.fhir.schema.app.processor.action.DropTenantAction;
import com.ibm.fhir.schema.app.processor.action.GrantPrivilegesAction;
import com.ibm.fhir.schema.app.processor.action.MigrateSchemaAction;
import com.ibm.fhir.schema.app.processor.action.PopulateStaticTablesAction;
import com.ibm.fhir.schema.app.processor.action.TestTenantAction;
import com.ibm.fhir.schema.app.processor.action.UpdateProceduresAction;
import com.ibm.fhir.schema.app.processor.action.UpdateSchemaAction;
import com.ibm.fhir.schema.app.processor.action.UpdateTenantStatusAction;
import com.ibm.fhir.schema.app.processor.action.VersionHistoryServiceAction;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * Converts the Arguments into an ActionBean
 */
public class ProcessFeature {
    private static final double NANOS = 1e9;

    private static final Logger logger = Logger.getLogger(ProcessFeature.class.getName());

    // To support ISchemaAction
    private ActionProcessor processor;

    public ProcessFeature() {
        // No Operation
    }

    /**
     * Process the requested operation
     * 
     * @parma actionBean
     * @throws Exception
     */
    public void process(ActionBean actionBean) throws Exception {
        long start = System.nanoTime();
        processor = new ActionProcessor(actionBean);
        SchemaUtil.loadDriver(actionBean.getTranslator());

        if (actionBean.isCheckCompatibility()) {
            logger.info("Check Compatibility -> " + checkCompatibility(actionBean));
        }

        if (actionBean.getAddKeyForTenant() != null) {
            addTenantKey();
        } else if (actionBean.isDropSchema() || actionBean.isDropAdmin()) {
            // only proceed with the drop if the user has provided additional confirmation
            checkDropConfirmed(actionBean);
            dropSchema();
        } else if (actionBean.isUpdateSchema()) {
            updateSchema();
        } else if (actionBean.isCreateFhirSchemas()) {
            createFhirSchemas();
        } else if (actionBean.isUpdateProc()) {
            updateProcedures();
        } else if (actionBean.isAllocateTenant()) {
            allocateTenant(actionBean);
        } else if (actionBean.isTestTenant()) {
            testTenant();
        } else if (actionBean.isDropTenant()) {
            dropTenant(actionBean);
        }

        if (actionBean.getGrantTo() != null) {
            grantPrivileges();
        }

        processor.close();

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    public void checkDropConfirmed(ActionBean actionBean) {
        if (!actionBean.isConfirmDrop()) {
            throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
        }
    }

    /**
     * Create fhir data and admin schema
     * 
     * @throws Exception
     */
    protected void createFhirSchemas() throws Exception {
        CreateSchemaAction action = new CreateSchemaAction();
        processor.process(action);
    }

    /**
     * Drop all the objects in the admin and data schemas. Typically used
     * during development.
     * 
     * @throws Exception
     */
    protected void dropSchema() throws Exception {
        DropSchemaAction action = new DropSchemaAction();
        processor.process(action);
    }

    /**
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     * 
     * @throws Exception
     */
    protected void updateProcedures() throws Exception {
        UpdateProceduresAction action = new UpdateProceduresAction();
        processor.process(action);
    }

    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     * 
     * @throws SchemaActionException
     */
    protected void addTenantKey() throws SchemaActionException {
        AddTenantKeyAction action = new AddTenantKeyAction();
        processor.processTransaction(action);
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     * 
     * @throws Exception
     */
    protected void allocateTenant(ActionBean actionBean) throws Exception {
        // Starts the Allocate Tenant Action by getting the tenantId
        AllocateTenantAction action = new AllocateTenantAction();
        processor.processTransaction(action);

        // Add Tenant Partition Action.
        AddTenantPartitionsAction addTenantPartitionsAction = new AddTenantPartitionsAction();
        processor.processTransaction(addTenantPartitionsAction);

        // Fill any static data tables (which are also partitioned by tenant)
        PopulateStaticTablesAction actionStatic = new PopulateStaticTablesAction();
        processor.processTransaction(actionStatic);

        // Now all the table partitions have been allocated, we can mark the tenant as ready
        actionBean.setStatus(TenantStatus.ALLOCATED);
        UpdateTenantStatusAction updateTenantStatusAction = new UpdateTenantStatusAction();
        processor.processTransaction(updateTenantStatusAction);
    }

    /**
     * checks the compatibility action
     * @throws Exception 
     */
    protected boolean checkCompatibility(ActionBean actionBean) throws Exception {
        CheckCompatibilityAction action = new CheckCompatibilityAction();
        processor.process(action);
        return actionBean.isCompatible();
    }

    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     * 
     * @throws SchemaActionException
     */
    protected void grantPrivileges() throws SchemaActionException {
        GrantPrivilegesAction action = new GrantPrivilegesAction();
        processor.processTransaction(action);
    }

    /**
     * Update the schema
     * 
     * @throws SchemaActionException
     */
    protected void updateSchema() throws Exception {
        // Create a Version History Table 
        CreateVersionHistoryAction createVersionHistoryAction = new CreateVersionHistoryAction();
        processor.process(createVersionHistoryAction);

        // Current version history for the data schema
        VersionHistoryServiceAction versionHistoryServiceAction = new VersionHistoryServiceAction();
        processor.processWithoutTransaction(versionHistoryServiceAction);

        UpdateSchemaAction action = new UpdateSchemaAction();
        processor.processTransaction(action);

        MigrateSchemaAction migrateServiceAction = new MigrateSchemaAction();
        processor.process(migrateServiceAction);

        ApplyModelAction applyModelAction = new ApplyModelAction();
        processor.processTransaction(applyModelAction);
    }

    /**
     * Deallocate this tenant, dropping all the related partitions
     * 
     * @throws SchemaActionException
     */
    protected void dropTenant(ActionBean actionBean) throws SchemaActionException {
        DropTenantAction action = new DropTenantAction();
        processor.processTransaction(action);

        // Now all the table partitions have been allocated, we can mark the tenant as dropped
        actionBean.setStatus(TenantStatus.DROPPED);
        UpdateTenantStatusAction updateTenantStatusAction = new UpdateTenantStatusAction();
        processor.processTransaction(updateTenantStatusAction);
    }

    /**
     * Check that we can call the set_tenant procedure successfully (which means
     * that the
     * tenant record exists in the tenants table)
     * 
     * @throws SchemaActionException
     */
    protected void testTenant() throws SchemaActionException {
        // Fill any static data tables (which are also partitioned by tenant)
        PopulateStaticTablesAction actionStatic = new PopulateStaticTablesAction();
        processor.processTransaction(actionStatic);

        TestTenantAction testTenantAction = new TestTenantAction();
        processor.processTransaction(testTenantAction);
    }
}