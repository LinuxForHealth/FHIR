/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.schema.model;

import org.linuxforhealth.fhir.schema.app.Main;

/**
 * Schema is holding and manipulating the schema to UPPERCASE
 */
public class Schema {
    // The schema used for administration of tenants
    private String adminSchemaName = Main.ADMIN_SCHEMANAME;

    // The schema used for administration of OAuth 2.0 clients
    private String oauthSchemaName = Main.OAUTH_SCHEMANAME;

    // The schema used for Java Batch
    private String javaBatchSchemaName = Main.BATCH_SCHEMANAME;

    // The schema we will use for all the FHIR data tables
    private String schemaName = Main.DATA_SCHEMANAME;

    // Set to true when a data schema name has been provided, overriding the default
    private boolean overrideDataSchema = false;

    /**
     * @return the adminSchemaName
     */
    public String getAdminSchemaName() {
        return adminSchemaName.toUpperCase();
    }

    /**
     * @param adminSchemaName
     *            the adminSchemaName to set
     */
    public void setAdminSchemaName(String adminSchemaName) {
        this.adminSchemaName = adminSchemaName;
    }

    /**
     * @return the oauthSchemaName
     */
    public String getOauthSchemaName() {
        return oauthSchemaName.toUpperCase();
    }

    /**
     * @param oauthSchemaName
     *            the oauthSchemaName to set
     */
    public void setOauthSchemaName(String oauthSchemaName) {
        this.oauthSchemaName = oauthSchemaName;
    }

    /**
     * @return the javaBatchSchemaName
     */
    public String getJavaBatchSchemaName() {
        return javaBatchSchemaName.toUpperCase();
    }

    /**
     * @param javaBatchSchemaName
     *            the javaBatchSchemaName to set
     */
    public void setJavaBatchSchemaName(String javaBatchSchemaName) {
        this.javaBatchSchemaName = javaBatchSchemaName;
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName.toUpperCase();
    }

    /**
     * @param schemaName
     *            the schemaName to set
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        this.overrideDataSchema = true; // the data schema has been specified
    }

    /**
     * @return the withDataSchema
     */
    public boolean isOverrideDataSchema() {
        return overrideDataSchema;
    }

    /**
     * Returns true if the given schemaName parameter matches the configured schemaName held
     * by this.
     * @param schemaSchema
     * @return
     */
    public boolean matchesDataSchema(String schemaName) {
        return this.schemaName.equalsIgnoreCase(schemaName);
    }
}