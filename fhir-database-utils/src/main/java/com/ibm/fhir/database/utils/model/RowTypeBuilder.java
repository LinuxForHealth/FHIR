/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Builder pattern to make it easy to define row types
 */
public class RowTypeBuilder extends ColumnDefBuilder {
    
    private String schemaName;
    
    private String typeName;
    
    /**
     * Setter for the schema name
     * @param schemaName
     * @return
     */
    public RowTypeBuilder setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    /**
     * Setter for the name of the table being built
     * @param typeName
     * @return
     */
    public RowTypeBuilder setTypeName(String typeName) {
        this.typeName = typeName;
        return this;
    }
    
    /**
     * Build the immutable table object based on the current configuration
     * @return
     */
    public RowType build() {
        if (this.typeName == null) {
            throw new IllegalStateException("No type name provided");
        }
        
        // Our schema objects are immutable by design, so all initialization takes place
        // through the constructor
        return new RowType(this.schemaName, this.typeName, 1, buildColumns());
    }
}
