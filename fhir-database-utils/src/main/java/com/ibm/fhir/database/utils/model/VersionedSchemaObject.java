/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

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
    private int version = 1;

    public VersionedSchemaObject(String schemaName, String objectName) {
        DataDefinitionUtil.assertValidNames(schemaName, objectName);
        this.schemaName = schemaName;
        this.objectName = objectName;
    }

    /**
     * Setter to override the default version number for this object
     * (usually used for alter statements, or new tables and indexes)
     * 
     * @param v
     */
    protected void setVersionValue(int v) {
        this.version = v;
    }

    /**
     * Getter for the object's version (change) number
     * 
     * @return
     */
    public int getVersion() {
        return version;
    }

    /**
     * Getter for the object's schema name
     * 
     * @return
     */
    public String getSchemaName() {
        return this.schemaName;
    }

    /**
     * Getter for the object's name
     * 
     * @return
     */
    public String getObjectName() {
        return this.objectName;
    }

    public String getQualifiedName() {
        return DataDefinitionUtil.getQualifiedName(schemaName, objectName);
    }
}