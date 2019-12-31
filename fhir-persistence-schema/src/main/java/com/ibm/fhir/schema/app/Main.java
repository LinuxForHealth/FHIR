/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.schema.app.util.SchemaUtil;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.processor.ActionProcessor;
import com.ibm.fhir.schema.control.processor.action.AddTenantKeyAction;
import com.ibm.fhir.schema.control.processor.action.AddTenantPartitionsAction;
import com.ibm.fhir.schema.control.processor.action.AllocateTenantAction;
import com.ibm.fhir.schema.control.processor.action.ApplyModelAction;
import com.ibm.fhir.schema.control.processor.action.CheckCompatibilityAction;
import com.ibm.fhir.schema.control.processor.action.CreateSchemaAction;
import com.ibm.fhir.schema.control.processor.action.CreateVersionHistoryAction;
import com.ibm.fhir.schema.control.processor.action.DropSchemaAction;
import com.ibm.fhir.schema.control.processor.action.DropTenantAction;
import com.ibm.fhir.schema.control.processor.action.GrantPrivilegesAction;
import com.ibm.fhir.schema.control.processor.action.PopulateStaticTablesAction;
import com.ibm.fhir.schema.control.processor.action.TestTenantAction;
import com.ibm.fhir.schema.control.processor.action.UpdateProceduresAction;
import com.ibm.fhir.schema.control.processor.action.UpdateSchemaAction;
import com.ibm.fhir.schema.control.processor.action.UpdateTenantStatusAction;
import com.ibm.fhir.schema.control.processor.action.VersionHistoryServiceAction;
import com.ibm.fhir.task.api.ITaskCollector;

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

    // Properties accumulated as we parse args and read configuration files
    private final Properties properties = new Properties();

    // The schema we will use for all the FHIR data tables
    private String schemaName;

    // The schema used for administration of tenants
    private String adminSchemaName = "FHIR_ADMIN";

    // Arguments requesting we drop the objects from the schema
    private boolean dropSchema = false;
    private boolean dropAdmin = false;
    private boolean confirmDrop = false;
    private boolean updateSchema = false;
    private boolean updateProc = false;
    private boolean checkCompatibility = false;
    private boolean createFhirSchemas = false;

    // By default, the dryRun option is OFF, and FALSE
    // When overridden, it simulates the actions. 
    private Boolean dryRun = Boolean.FALSE;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // Tenant management
    private boolean allocateTenant;
    private boolean dropTenant;
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;

    // The tenant name for when we want to add a new tenant key
    private String addKeyForTenant;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    // This utility is designed to work with a DB2 database
    private IDatabaseTranslator translator = new Db2Translator();

    // The connection pool and transaction provider to support concurrent operations
    private int maxConnectionPoolSize = FhirSchemaConstants.DEFAULT_POOL_SIZE / 2;

    private ActionProcessor processor;

    /**
     * Read the properties from the given file
     * 
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IllegalArgumentException("The properties file does not exist [" + filename + "]");
        }

        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Parse the given key=value string and add to the properties being collected
     * 
     * @param pair
     */
    public void addProperty(String pair) {
        String[] kv = pair.split("=");
        if (kv.length == 2) {
            properties.put(kv[0], kv[1]);
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

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
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case to avoid tricky-to-catch errors related to quoting names
                    this.schemaName = args[i].toUpperCase();

                    if (!schemaName.equals(args[i])) {
                        logger.info("Schema name forced to upper case: " + schemaName);
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--grant-to":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    this.grantTo = args[i].toUpperCase();
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--add-tenant-key":
                if (++i < args.length) {
                    this.addKeyForTenant = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-proc":
                this.updateProc = true;
                break;
            case "--check-compatibility":
                this.checkCompatibility = true;
                break;
            case "--drop-admin":
                this.dropAdmin = true;
                break;
            case "--test-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.testTenant = true;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key":
                if (++i < args.length) {
                    this.tenantKey = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-schema":
                this.updateSchema = true;
                this.dropSchema = false;
                break;
            case "--create-schemas":
                this.createFhirSchemas = true;
                break;
            case "--drop-schema":
                this.updateSchema = false;
                this.dropSchema = true;
                break;
            case "--pool-size":
                if (++i < args.length) {
                    this.maxConnectionPoolSize = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--prop":
                if (++i < args.length) {
                    // properties are given as name=value
                    addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--confirm-drop":
                this.confirmDrop = true;
                break;
            case "--allocate-tenant":
                if (++i < args.length) {
                    this.tenantName     = args[i];
                    this.allocateTenant = true;
                    this.dropTenant     = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--drop-tenant":
                if (++i < args.length) {
                    this.tenantName     = args[i];
                    this.dropTenant     = true;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--dry-run":
                // The presence of dry-run automatically flips it on.
                this.dryRun = Boolean.TRUE;
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
        processor = new ActionProcessor(properties, translator, dryRun, maxConnectionPoolSize);

        if (this.checkCompatibility) {
            checkCompatibility();
        }

        if (addKeyForTenant != null) {
            addTenantKey();
        } else if (this.dropSchema) {
            // only proceed with the drop if the user has provided additional confirmation
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (this.dropAdmin) {
            // only try to drop the admin schema
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (updateSchema) {
            updateSchema();
        } else if (createFhirSchemas) {
            createFhirSchemas();
        } else if (updateProc) {
            updateProcedures();
        } else if (this.allocateTenant) {
            allocateTenant();
        } else if (this.testTenant) {
            testTenant();
        } else if (this.dropTenant) {
            dropTenant();
        }

        if (this.grantTo != null) {
            grantPrivileges(FhirSchemaConstants.FHIR_USER_GRANT_GROUP);
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
        CreateSchemaAction action = new CreateSchemaAction(schemaName, adminSchemaName);
        processor.process(action);
    }

    /**
     * Drop all the objects in the admin and data schemas. Typically used
     * during development.
     */
    protected void dropSchema() {
        DropSchemaAction action = new DropSchemaAction(schemaName, adminSchemaName, dropSchema, dropAdmin);
        processor.process(action);
    }

    /**
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     */
    protected void updateProcedures() {
        UpdateProceduresAction action = new UpdateProceduresAction(schemaName, adminSchemaName);
        processor.process(action);
    }

    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     */
    protected void addTenantKey() {
        AddTenantKeyAction action = new AddTenantKeyAction(adminSchemaName, addKeyForTenant);
        processor.processTransaction(action);
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     */
    protected void allocateTenant() {
        // Starts the Allocate Tenant Action by getting the tenantId
        AllocateTenantAction action = new AllocateTenantAction(adminSchemaName, schemaName, tenantName);
        processor.processTransaction(action);
        int tenantId = action.getTenantId();

        // Add Tenant Partition Action.
        AddTenantPartitionsAction addTenantPartitionsAction =
                new AddTenantPartitionsAction(schemaName, adminSchemaName, tenantId);
        processor.process(addTenantPartitionsAction);

        // Fill any static data tables (which are also partitioned by tenant)
        PopulateStaticTablesAction actionStatic =
                new PopulateStaticTablesAction(adminSchemaName, schemaName, tenantName, tenantKey);
        processor.processTransaction(actionStatic);

        // Now all the table partitions have been allocated, we can mark the tenant as ready
        UpdateTenantStatusAction updateTenantStatusAction =
                new UpdateTenantStatusAction(adminSchemaName, tenantKey, tenantName, tenantId, TenantStatus.ALLOCATED);
        processor.processTransaction(updateTenantStatusAction);
    }

    /**
     * checks the compatibility action
     */
    protected boolean checkCompatibility() {
        CheckCompatibilityAction action = new CheckCompatibilityAction(adminSchemaName);
        processor.processTransaction(action);
        return action.getCompatible();
    }

    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     *
     * @param groupName
     */
    protected void grantPrivileges(String groupName) {
        GrantPrivilegesAction action = new GrantPrivilegesAction(adminSchemaName, schemaName, groupName, grantTo);
        processor.processTransaction(action);
    }

    /**
     * Update the schema
     */
    protected void updateSchema() {
        UpdateSchemaAction action = new UpdateSchemaAction(schemaName, adminSchemaName, maxConnectionPoolSize);
        processor.processTransaction(action);
        ITaskCollector collector = action.getCollector();
        PhysicalDataModel pdm = action.getPhysicalDataModel();

        // Create a Version History Table 
        CreateVersionHistoryAction createVersionHistoryAction = new CreateVersionHistoryAction(adminSchemaName);
        processor.process(createVersionHistoryAction);

        // Current version history for the data schema
        VersionHistoryServiceAction versionHistoryServiceAction =
                new VersionHistoryServiceAction(schemaName, adminSchemaName);
        processor.process(versionHistoryServiceAction);
        VersionHistoryService vhs = versionHistoryServiceAction.getVersionHistoryService();

        ApplyModelAction applyModelAction = new ApplyModelAction(pdm, vhs, collector);
        processor.process(applyModelAction);
        exitStatus = applyModelAction.getExitStatus();
    }

    /**
     * Deallocate this tenant, dropping all the related partitions
     */
    protected void dropTenant() {
        DropTenantAction action = new DropTenantAction(schemaName, adminSchemaName, tenantName);
        processor.processTransaction(action);

        // Now all the table partitions have been allocated, we can mark the tenant as dropped
        int tenantId = action.getTenantId();
        UpdateTenantStatusAction updateTenantStatusAction =
                new UpdateTenantStatusAction(adminSchemaName, tenantKey, tenantName, tenantId, TenantStatus.DROPPED);
        processor.processTransaction(updateTenantStatusAction);
    }

    /**
     * Check that we can call the set_tenant procedure successfully (which means
     * that the
     * tenant record exists in the tenants table)
     */
    protected void testTenant() {
        if (this.tenantName == null || this.tenantName.isEmpty()) {
            throw new IllegalStateException("Missing tenant name");
        }

        if (this.tenantKey == null || this.tenantKey.isEmpty()) {
            throw new IllegalArgumentException("No tenant-key value provided");
        }

        logger.info("Testing tenant: " + tenantName);

        // Fill any static data tables (which are also partitioned by tenant)
        PopulateStaticTablesAction actionStatic =
                new PopulateStaticTablesAction(adminSchemaName, schemaName, tenantName, tenantKey);
        processor.processTransaction(actionStatic);

        TestTenantAction testTenantAction = new TestTenantAction(adminSchemaName, schemaName, tenantName, tenantKey);
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
            SchemaUtil.loadDriver(m.translator);
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