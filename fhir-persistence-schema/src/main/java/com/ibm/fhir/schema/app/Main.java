/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.schema.app.processor.ActionProcessor;
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
import com.ibm.fhir.schema.app.processor.action.PopulateStaticTablesAction;
import com.ibm.fhir.schema.app.processor.action.TestTenantAction;
import com.ibm.fhir.schema.app.processor.action.UpdateProceduresAction;
import com.ibm.fhir.schema.app.processor.action.UpdateSchemaAction;
import com.ibm.fhir.schema.app.processor.action.UpdateTenantStatusAction;
import com.ibm.fhir.schema.app.processor.action.VersionHistoryServiceAction;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.util.SchemaUtil;

/**
 * Utility app to connect to a DB2 database and create/update the FHIR schema.
 * The DDL processing is idempotent, with only the necessary changes applied.
 * <br>
 * This utility also includes an option to exercise the tenant partitioning
 * code.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static final int EXIT_OK = 0; // validation was successful
    public static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    public static final int EXIT_RUNTIME_ERROR = 2; // programming error
    public static final int EXIT_VALIDATION_FAILED = 3; // validation test failed
    private static final double NANOS = 1e9;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    // To support ISchemaAction
    private ActionProcessor processor;
    private ActionBean actionBean = new ActionBean();

    /**
     * Get the program exit status from the environment
     * 
     * @return
     */
    protected int getExitStatus() {
        return this.exitStatus;
    }

    /**
     * Write a final status message - useful for QA to review when checking the
     * output
     */
    protected void logStatusMessage(int status) {
        switch (status) {
        case EXIT_OK:
            logger.info("RESULT: OK");
            break;
        case EXIT_BAD_ARGS:
            logger.severe("RESULT: BAD ARGS");
            break;
        case EXIT_RUNTIME_ERROR:
            logger.severe("RESULT: RUNTIME ERROR");
            break;
        case EXIT_VALIDATION_FAILED:
            logger.warning("RESULT: FAILED");
            break;
        default:
            logger.severe("RESULT: RUNTIME ERROR");
            break;
        }
    }

    // **************************************************************************************************************
    // Parse the commandline arguments

    /**
     * Parse the command-line arguments, building up the environment and
     * establishing
     * the run-list
     * 
     * @param args
     */
    protected void parseArgs(String[] args) {
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    actionBean.loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case to avoid tricky-to-catch errors related to quoting names
                    actionBean.setSchemaName(args[i].toUpperCase());
                    if (!actionBean.getSchemaName().equals(args[i])) {
                        logger.info("Schema name forced to upper case: " + actionBean.getSchemaName());
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--grant-to":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    actionBean.setGrantTo(args[i].toUpperCase());
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--add-tenant-key":
                if (++i < args.length) {
                    actionBean.setAddKeyForTenant(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-proc":
                actionBean.setUpdateProc(true);
                break;
            case "--check-compatibility":
                actionBean.setCheckCompatibility(true);
                break;
            case "--drop-admin":
                actionBean.setDropAdmin(true);
                break;
            case "--test-tenant":
                if (++i < args.length) {
                    actionBean.setTenantName(args[i]);
                    actionBean.setTestTenant(true);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key":
                if (++i < args.length) {
                    actionBean.setTenantKey(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-schema":
                actionBean.setUpdateSchema(true);
                actionBean.setDropSchema(false);
                break;
            case "--create-schemas":
                actionBean.setCreateFhirSchemas(true);
                break;
            case "--drop-schema":
                actionBean.setUpdateSchema(false);
                actionBean.setDropSchema(true);
                break;
            case "--pool-size":
                if (++i < args.length) {
                    actionBean.setMaxConnectionPoolSize(Integer.parseInt(args[i]));
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--prop":
                if (++i < args.length) {
                    // properties are given as name=value
                    actionBean.addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--confirm-drop":
                actionBean.setConfirmDrop(true);
                break;
            case "--allocate-tenant":
                if (++i < args.length) {
                    actionBean.setTenantName(args[i]);
                    actionBean.setAllocateTenant(true);
                    actionBean.setDropTenant(false);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--drop-tenant":
                if (++i < args.length) {
                    actionBean.setTenantName(args[i]);
                    actionBean.setAllocateTenant(false);
                    actionBean.setDropTenant(true);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--dry-run":
                // The presence of dry-run automatically flips it on.
                actionBean.setDryRun(Boolean.TRUE);
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
    }

    /**
     * Process the requested operation
     */
    protected void process() {
        long start = System.nanoTime();
        processor = new ActionProcessor(actionBean);
        SchemaUtil.loadDriver(actionBean.getTranslator());

        if (actionBean.isCheckCompatibility()) {
            logger.info("Check Compatibility -> " + checkCompatibility());
        }

        if (actionBean.getAddKeyForTenant() != null) {
            addTenantKey();
        } else if (actionBean.isDropSchema()) {
            // only proceed with the drop if the user has provided additional confirmation
            if (actionBean.isConfirmDrop()) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (actionBean.isDropAdmin()) {
            // only try to drop the admin schema
            if (actionBean.isConfirmDrop()) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (actionBean.isUpdateSchema()) {
            updateSchema();
        } else if (actionBean.isCreateFhirSchemas()) {
            createFhirSchemas();
        } else if (actionBean.isUpdateProc()) {
            updateProcedures();
        } else if (actionBean.isAllocateTenant()) {
            allocateTenant();
        } else if (actionBean.isTestTenant()) {
            testTenant();
        } else if (actionBean.isDropTenant()) {
            dropTenant();
        }

        if (actionBean.getGrantTo() != null) {
            grantPrivileges();
        }

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    //**************************************************************************************************************
    // The following methods are ACTIONs that can be taken in this application.

    /**
     * Create fhir data and admin schema
     */
    protected void createFhirSchemas() {
        CreateSchemaAction action = new CreateSchemaAction();
        processor.process(action);
    }

    /**
     * Drop all the objects in the admin and data schemas. Typically used
     * during development.
     */
    protected void dropSchema() {
        DropSchemaAction action = new DropSchemaAction();
        processor.process(action);
    }

    /**
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     */
    protected void updateProcedures() {
        UpdateProceduresAction action = new UpdateProceduresAction();
        processor.process(action);
    }

    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     */
    protected void addTenantKey() {
        AddTenantKeyAction action = new AddTenantKeyAction();
        processor.processTransaction(action);
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     */
    protected void allocateTenant() {
        // Starts the Allocate Tenant Action by getting the tenantId
        AllocateTenantAction action = new AllocateTenantAction();
        processor.processTransaction(action);

        // Add Tenant Partition Action.
        AddTenantPartitionsAction addTenantPartitionsAction = new AddTenantPartitionsAction();
        processor.process(addTenantPartitionsAction);

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
     */
    protected boolean checkCompatibility() {
        CheckCompatibilityAction action = new CheckCompatibilityAction();
        processor.processTransaction(action);
        return actionBean.isCompatible();
    }

    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     */
    protected void grantPrivileges() {
        GrantPrivilegesAction action = new GrantPrivilegesAction();
        processor.processTransaction(action);
    }

    /**
     * Update the schema
     */
    protected void updateSchema() {
        UpdateSchemaAction action = new UpdateSchemaAction();
        processor.processTransaction(action);

        // Create a Version History Table 
        CreateVersionHistoryAction createVersionHistoryAction = new CreateVersionHistoryAction();
        processor.process(createVersionHistoryAction);

        // Current version history for the data schema
        VersionHistoryServiceAction versionHistoryServiceAction = new VersionHistoryServiceAction();
        processor.process(versionHistoryServiceAction);

        ApplyModelAction applyModelAction = new ApplyModelAction();
        processor.process(applyModelAction);
    }

    /**
     * Deallocate this tenant, dropping all the related partitions
     */
    protected void dropTenant() {
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
     */
    protected void testTenant() {
        // Fill any static data tables (which are also partitioned by tenant)
        PopulateStaticTablesAction actionStatic = new PopulateStaticTablesAction();
        processor.processTransaction(actionStatic);

        TestTenantAction testTenantAction = new TestTenantAction();
        processor.processTransaction(testTenantAction);
    }

    //**************************************************************************************************************
    /**
     * Main entry point
     * 
     * @param args
     */
    public static void main(String[] args) {
        SchemaUtil.logClasspath();

        int exitStatus;
        Main m = new Main();
        try {
            SchemaUtil.configureLogger();
            m.parseArgs(args);
            m.process();
            exitStatus = m.getExitStatus();
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            SchemaUtil.printUsage();
            exitStatus = EXIT_BAD_ARGS;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "schema tool failed", x);
            exitStatus = EXIT_RUNTIME_ERROR;
        }

        // Write out a final status message to make it easy to see validation success/failure
        m.logStatusMessage(exitStatus);

        // almost certainly will get flagged during code-scan, but this is intentional,
        // as we genuinely want to exit with the correct status here. The code-scan tool
        // really ought to be able to see that this is a main function in a J2SE environment
        System.exit(exitStatus);
    }
}