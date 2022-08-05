/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.app.menu;

import java.io.PrintStream;

/**
 * Menu System for Persistence Schema Tool
 */
public class Menu {
    public static final String PROP_FILE = "--prop-file";
    public static final String SCHEMA_NAME = "--schema-name";
    public static final String GRANT_TO = "--grant-to";
    public static final String GRANT_READ_TO = "--grant-read-to";
    public static final String TARGET = "--target";
    public static final String UPDATE_PROC = "--update-proc";
    public static final String CHECK_COMPATIBILITY = "--check-compatibility";
    public static final String DROP_ADMIN = "--drop-admin";
    public static final String DB_TYPE = "--db-type";
    public static final String DROP_SPLIT_TRANSACTION = "--drop-split-transaction";
    public static final String CONFIRM_DROP = "--confirm-drop";
    public static final String VACUUM_TABLE_NAME = "--vacuum-table-name";
    public static final String VACUUM_SCALE_FACTOR = "--vacuum-scale-factor";
    public static final String VACUUM_TRESHOLD = "--vacuum-threshold";
    public static final String UPDATE_VACUUM = "--update-vacuum";
    public static final String FORCE_UNUSED_TABLE_REMOVAL = "--force-unused-table-removal";
    public static final String VACUUM_COST_LIMIT = "--vacuum-cost-limit";
    public static final String PROP = "--prop";
    public static final String POOL_SIZE = "--pool-size";
    public static final String THREAD_POOL_SIZE = "--thread-pool-size";
    public static final String DROP_SCHEMA_OAUTH = "--drop-schema-oauth";
    public static final String DROP_SCHEMA_BATCH = "--drop-schema-batch";
    public static final String DROP_SCHEMA_FHIR = "--drop-schema-fhir";
    public static final String DROP_SCHEMA = "--drop-schema";
    public static final String UPDATE_SCHEMA = "--update-schema";
    public static final String UPDATE_SCHEMA_FHIR = "--update-schema-fhir";
    public static final String UPDATE_SCHEMA_BATCH = "--update-schema-batch";
    public static final String UPDATE_SCHEMA_OAUTH = "--update-schema-oauth";
    public static final String CREATE_SCHEMAS = "--create-schemas";
    public static final String CREATE_SCHEMA_FHIR = "--create-schema-fhir";
    public static final String CREATE_SCHEMA_BATCH = "--create-schema-batch";
    public static final String CREATE_SCHEMA_OAUTH = "--create-schema-oauth";
    public static final String FORCE = "--force";
    public static final String HELP = "--help";
    public static final String SHOW_DB_SIZE = "--show-db-size";
    public static final String SHOW_DB_SIZE_DETAIL = "--show-db-size-detail";
    public static final String SCHEMA_TYPE = "--schema-type";

    public Menu() {
        // NOP
    }

    public enum HelpMenu {
        MI_HELP(HELP, "", "This menu"),
        MI_PROP_FILE(PROP_FILE, "path-to-property-file", "loads the properties from a file"),
        MI_SCHEMA_NAME(SCHEMA_NAME, "schema-name" , "uses the schema as specified, must be valid."),
        MI_GRANT_TO(GRANT_TO, "username", "uses the user as specified, must be valid.\nand grants permission to the username"),
        MI_GRANT_READ_TO(GRANT_READ_TO, "username", "uses the user as specified, must be valid.\nand grants read (select) permission on FHIR data tables to the username"),
        MI_TARGET(TARGET, "TYPE schemaName", "The schemaName and type [BATCH,OAUTH,DATA]"),
        MI_UPDATE_PROC(UPDATE_PROC, "", "updates the stored procedure for a specific tenant"),
        MI_CHECK_COMPATIBILITY(CHECK_COMPATIBILITY, "", "checks feature compatibility"),
        MI_DROP_ADMIN(DROP_ADMIN, "", "drops the admin schema"),
        MI_DB_TYPE(DB_TYPE, "dbType" , "Either derby, postgresql, citus"),
        MI_CONFIRM_DROP(CONFIRM_DROP, "", "confirms the dropping of a schema"),
        MI_UPDATE_VACUUM(UPDATE_VACUUM, "", "Update the Vacuum settings for PostgreSQL"),
        MI_VACUUM_TABLE_NAME(VACUUM_TABLE_NAME, "tableName", "Table Name to update vacuum settings"),
        MI_VACUUM_SCALE_FACTOR(VACUUM_SCALE_FACTOR, "scaleFactor", "The scale factor to alter to 'scaleFactor'"),
        MI_VACUUM_TRESHOLD(VACUUM_TRESHOLD, "threshold", "The threshold value to alter to 'threshold'"),
        MI_VACUUM_COST_LIMIT(VACUUM_COST_LIMIT, "costLimit", "The Vacuum cost limit to set"),
        MI_FORCE(FORCE, "", "Do not skip schema update process when the whole-schema-version matches."),
        MI_FORCE_UNUSED_TABLE_REMOVAL(FORCE_UNUSED_TABLE_REMOVAL, "", "Forces the removal of unused tables - DomainResource, Resource"),
        MI_PROP(PROP, "name=value", "name=value that is passed in on the commandline"),
        MI_POOL_SIZE(POOL_SIZE, "poolSize", "poolsize used with the database actions"),
        MI_THREAD_POOL_SIZE(THREAD_POOL_SIZE, "threadPoolSize", "pool size used with parallel actions"),
        MI_DROP_SCHEMA_OAUTH(DROP_SCHEMA_OAUTH, "", "drop the db schema used by liberty's oauth/openid connect features"),
        MI_DROP_SCHEMA_BATCH(DROP_SCHEMA_BATCH, "", "drop the db schema used by liberty's java-batch feature\""),
        MI_DROP_SCHEMA_FHIR(DROP_SCHEMA_FHIR, "", "drop the schema set by '--schema-name'"),
        // UNUSED, left here intentionally
        // MI_DROP_SCHEMA(DROP_SCHEMA, "", ""),
        MI_UPDATE_SCHEMA(UPDATE_SCHEMA, "", "deploy or update the schema set by '--schema-name' *deprecated*"),
        MI_UPDATE_SCHEMA_FHIR(UPDATE_SCHEMA_FHIR, "schemaName", "Updates the FHIR Data Schema"),
        MI_UPDATE_SCHEMA_BATCH(UPDATE_SCHEMA_BATCH, "schemaName", "Updates the Batch Schema"),
        MI_UPDATE_SCHEMA_OAUTH(UPDATE_SCHEMA_OAUTH, "schemaName", "Updates the OAuth Schema"),
        MI_CREATE_SCHEMAS(CREATE_SCHEMAS, "", "create the database schemas for batch, oauth, and the fhir schema set by '--schema-name'"),
        MI_CREATE_SCHEMA_FHIR(CREATE_SCHEMA_FHIR, "schemaName", "Create the FHIR Data Schema"),
        MI_CREATE_SCHEMA_BATCH(CREATE_SCHEMA_BATCH, "schemaName", "Create the Batch Schema"),
        MI_CREATE_SCHEMA_OAUTH(CREATE_SCHEMA_OAUTH, "schemaName", "Create the OAuth Schema"),
        MI_SCHEMA_TYPE(SCHEMA_TYPE, "", "Which variant of the FHIR data schema to use"),
        MI_SHOW_DB_SIZE(SHOW_DB_SIZE, "", "Generate report with a breakdown of database size"),
        MI_SHOW_DB_SIZE_DETAIL(SHOW_DB_SIZE_DETAIL, "", "Include detailed table and index info in size report");

        // Variables for Enum:
        private final String flag;
        private final String variable;
        private final String description;

        private HelpMenu(String flag, String variable, String description) {
            this.flag = flag;
            this.variable = variable;
            this.description = description;
        }

        public String flag() {
            return flag;
        }

        public String variable() {
            return variable;
        }

        public String description() {
            return description;
        }
    }

    /**
     * Generates the Markdown Table
     * @param ps
     */
    public void generateMarkdownTable(PrintStream ps) {
        ps.println("|Flag|Variable|Description|");
        ps.println("|----------------|----------------|----------------|");
        for (HelpMenu item : HelpMenu.values()) {
            StringBuilder builder = new StringBuilder();
            builder.append("|");
            builder.append(item.flag);
            builder.append("|");
            builder.append(item.variable);
            builder.append("|");
            builder.append(item.description);
            builder.append("|");
            ps.println(builder.toString());
        }
        ps.println("");
    }

    /**
     * prints a brief menu to the standard out showing the usage.
     */
    public void generateHelpMenu() {
        PrintStream ps = System.err;
        ps.println("Usage: ");
        for (HelpMenu item : HelpMenu.values()) {
            ps.print(item.flag);
            if (!item.variable.isEmpty()) {
                ps.print(" " + item.variable);
            }
            ps.println();
            ps.println("  *  " + item.description);
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.generateMarkdownTable(System.out);
    }
}
