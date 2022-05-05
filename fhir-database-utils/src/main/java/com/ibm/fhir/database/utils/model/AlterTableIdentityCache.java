/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;

import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;

/**
 * Modify the CACHE property of an AS IDENTITY column (changes
 * the CACHE property of the underlying SEQUENCE).
 */
public class AlterTableIdentityCache extends BaseObject {
    private final String columnName;
    
    // caching sequence values for tuning
    private final int cache;

    /**
     * Public constructor
     * 
     * @param schemaName
     * @param sequenceName
     * @param version
     * @param startWith
     * @param cache
     */
    public AlterTableIdentityCache(String schemaName, String tableName, String columnName, int cache, int version) {
        super(schemaName, tableName, DatabaseObjectType.SEQUENCE, version);
        this.columnName = columnName;
        this.cache = cache;
    }

    @Override
    public String getTypeNameVersion() {
        // There's typically only one identity column on a table, but we still
        // want to qualify the name with the column just to make sure it's unique
        return getObjectType().name() + ":" + getQualifiedName() + ":" + this.columnName + ":" + this.version;
        
    }

    @Override
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        target.alterTableColumnIdentityCache(getSchemaName(), getObjectName(), this.columnName, this.cache);
    }

    @Override
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context) {
        apply(target, context);
    }

    @Override
    public void drop(ISchemaAdapter target) {
        // NOP
    }

    @Override
    protected void grantGroupPrivileges(ISchemaAdapter target, Set<Privilege> group, String toUser) {
        // NOP
    }

    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }
}