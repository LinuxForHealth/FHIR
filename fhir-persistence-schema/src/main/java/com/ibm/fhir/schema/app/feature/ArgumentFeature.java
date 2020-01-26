/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import java.io.PrintStream;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

/**
 * ArgumentFeature
 */
public class ArgumentFeature {
    private static final Logger logger = Logger.getLogger(ArgumentFeature.class.getName());

    public ArgumentFeature() {
        //No Operation
    }

    /**
     * Parse the command-line arguments, building up the environment and
     * establishing
     * the run-list
     * 
     * @param args
     */
    public ActionBean parseArgs(String[] args) {
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        ActionBean actionBean = new ActionBean();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                checkValidityOfIndex(++i, args.length);
                actionBean.loadPropertyFile(args[i]);
                break;
            case "--schema-name":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);
                DataDefinitionUtil.assertValidName(args[i]);

                // Force upper-case to avoid tricky-to-catch errors related to quoting names
                actionBean.setSchemaName(args[i].toUpperCase());
                if (!actionBean.getSchemaName().equals(args[i])) {
                    logger.info("Schema name forced to upper case: " + actionBean.getSchemaName());
                }
                break;
            case "--grant-to":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);
                DataDefinitionUtil.assertValidName(args[i]);

                // Force upper-case because user names are case-insensitive
                actionBean.setGrantTo(args[i].toUpperCase());
                break;
            case "--add-tenant-key":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);
                actionBean.setAddKeyForTenant(args[i]);
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
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);
                actionBean.setTenantName(args[i]);
                actionBean.setTestTenant(true);
                break;
            case "--tenant-key":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);
                actionBean.setTenantKey(args[i]);
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
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);

                actionBean.setMaxConnectionPoolSize(Integer.parseInt(args[i]));
                break;
            case "--prop":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);

                // properties are given as name=value
                actionBean.addProperty(args[i]);
                break;
            case "--confirm-drop":
                actionBean.setConfirmDrop(true);
                break;
            case "--allocate-tenant":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);

                actionBean.setTenantName(args[i]);
                actionBean.setAllocateTenant(true);
                actionBean.setDropTenant(false);
                break;
            case "--drop-tenant":
                checkValidityOfIndex(++i, args.length);
                checkForMissingPropertyValue(args[i]);

                actionBean.setTenantName(args[i]);
                actionBean.setAllocateTenant(false);
                actionBean.setDropTenant(true);
                break;
            case "--dry-run":
                // The presence of dry-run automatically flips it on.
                actionBean.setDryRun(Boolean.TRUE);
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
        return actionBean;
    }

    /**
     * checks that the constraint is not invalid currentIndex must not be more than length.
     * 
     * @param currentIndex
     * @param length
     */
    public void checkValidityOfIndex(int currentIndex, int length) {
        if (currentIndex >= length) {
            throw new IllegalArgumentException("Missing value for argument at posn: " + currentIndex);
        }
    }

    /**
     * @param argumentValue
     */
    public void checkForMissingPropertyValue(String argumentValue) {
        // At this point '--' will never be passed in, meaning
        // a property missing a value never gets passed assertValidName
        if (argumentValue == null || argumentValue.isEmpty() || argumentValue.startsWith("--")) {
            throw new IllegalArgumentException("The value for argument value is incorrect [" + argumentValue + "]");
        }
    }

    /***
     * prints a brief menu to the standard out showing the usage.
     */
    public void printUsage(PrintStream ps) {
        ps.println("Usage: ");

        // Properties File
        ps.println("--prop-file path-to-property-file");
        ps.println(" * loads the properties from a file");

        // Schema Name
        ps.println("--schema-name schema-name");
        ps.println(" * uses the schema as specified, must be valid.");

        // Grant Permissions to a valid username
        ps.println("--grant-to username");
        ps.println(" * uses the user as specified, must be valid.");
        ps.println(" * and grants permission to the username");

        // Add Tenant Key
        ps.println("--add-tenant-key tenant-key");
        ps.println(" * adds a tenant-key");

        // Updates the Sotred Procedure for a Tenant
        ps.println("--update-proc");
        ps.println(" * updates the stored procedure for a specific tenant");

        // Checks feature compatiblility
        ps.println("--check-compatibility");
        ps.println(" * checks feature compatibility ");

        // Drop the Admin Schema
        ps.println("--drop-admin");
        ps.println(" * drops the admin schema ");

        // Test Tenant
        ps.println("--test-tenant tenantName");
        ps.println(" * used to test with tenantName ");

        // Tenant Key
        ps.println("--tenant-key tenantKey");
        ps.println(" * uses the tenant-key in the queries ");

        // Update Schema action
        ps.println("--update-schema");
        ps.println(" * action to update the schema ");

        // Create Schema action
        ps.println("--create-schemas");
        ps.println(" * action to create the schema, used in conjunction with --schema-name schemaname");
        ps.println(" * automatically creates the corresponding FHIR_ADMIN schema. ");

        // Drop Schema action 
        ps.println("--drop-schema");
        ps.println(" * action to drop the schema ");
        ps.println(" * must be used with --confirm-drop");
        ps.println(" * note: drops tables, sequences if found in the schema.");

        // Uses a specified poolsize
        ps.println("--pool-size poolSize");
        ps.println(" * poolsize used with the database actions ");

        // Property used to connect
        ps.println("--prop name=value");
        ps.println(" * name=value that is passed in on the commandline  ");

        // Confirms dropping of the schema
        ps.println("--confirm-drop");
        ps.println(" * confirms the dropping of a schema");

        // Allocates Tenant
        ps.println("--allocate-tenant");
        ps.println(" * allocates a tenant");

        // Drops a Tenant
        ps.println("--drop-tenant tenantName");
        ps.println(" * drops the tenant given the tenantName");

        // Dry Run functionality
        ps.println("--dry-run ");
        ps.println(" * simulates the actions of the actions that change the datastore");
    }
}