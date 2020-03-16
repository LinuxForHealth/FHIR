/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Base class for the schema object builders which also carry a version (change) number
 */
public class VersionedSchemaObject {

    // The database schema in which the object will be created
    private final String schemaName;

    // The name of the object (e.g. table, index, constraint etc)
    private final String objectName;

    // The version, which can be changed during building
    protected int version = 1;

    // Conditional migration steps to perform before or after updating to the current version
    protected List<Migration> preSteps;
    protected List<Migration> postSteps;

    public VersionedSchemaObject(String schemaName, String objectName) {
        DataDefinitionUtil.assertValidNames(schemaName, objectName);
        this.schemaName = schemaName;
        this.objectName = objectName;
    }

    /**
     * Setter to override the default version number for this object
     * (usually used for alter statements, or new tables and indexes)
     * @param v
     */
    protected void setVersionValue(int v) {
        this.version = v;
    }

    /**
     * Getter for the object's version (change) number
     * @return
     */
    public int getVersion() {
        return this.version;
    }

    /**
     * Getter for the object's schema name
     * @return
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    /**
     * Getter for the object's name
     * @return
     */
    public String getObjectName() {
        return this.objectName;
    }

    public String getQualifiedName() {
        return DataDefinitionUtil.getQualifiedName(schemaName, objectName);
    }

    /**
     * Add migration steps to perform before applying this object
     * @param migration
     * @return
     */
    public VersionedSchemaObject addPreStep(Migration... migration) {
        preSteps.addAll(Arrays.asList(migration));
        return this;
    }

    /**
     * Add migration steps to perform after applying this object
     * @param migration
     * @return
     */
    public VersionedSchemaObject addPostStep(Migration... migration) {
        postSteps.addAll(Arrays.asList(migration));
        return this;
    }
}
