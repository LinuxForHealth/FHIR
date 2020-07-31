/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * Index creation definition for creating new indexes after the table has been defined
 * (e.g. in a subsequent version)
 */
public class CreateIndex extends BaseObject {
    private static final Logger logger = Logger.getLogger(CreateIndex.class.getName());
    
    // The index definition to apply to the data model
    private final IndexDef indexDef;
    
    // The name of the tenant column when used for multi-tenant databases
    private final String tenantColumnName;

    /**
     * Protected constructor. Use the Builder to create instance.
     * @param schemaName
     * @param indexName
     * @param version
     */
    protected CreateIndex(String schemaName, String indexName, int version, IndexDef indexDef, String tenantColumnName) {
        super(schemaName, indexName, DatabaseObjectType.INDEX, version);
        this.indexDef = indexDef;
        this.tenantColumnName = tenantColumnName;
    }
    
    /**
     * Return a new Builder instance for creating instances of CreateIndex
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }
    
    @Override
    public void apply(IDatabaseAdapter target) {
        long start = System.nanoTime();
        indexDef.apply(getSchemaName(), getObjectName(), tenantColumnName, target);
        
        if (logger.isLoggable(Level.FINE)) {
            long end = System.nanoTime();
            long elapsedMillis = (end - start) / 1000000;
            logger.fine(String.format("Created index %s [took=%dms]", indexDef.toString(), elapsedMillis));
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        apply(target);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        long start = System.nanoTime();
        indexDef.drop(getSchemaName(), target);
        
        if (logger.isLoggable(Level.FINE)) {
            long end = System.nanoTime();
            long elapsedMillis = (end - start) / 1000000;
            logger.fine(String.format("Dropped index %s [took=%dms]", indexDef.toString(), elapsedMillis));
        }
    }

    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }
    
    /**
     * Builder to implement fluent construction of {@link CreateIndex} objects.
     */
    public static class Builder {
        
        // The schema name of the table to which we're adding the index
        private String schemaName;
        
        // The name of the table to which we're adding the index
        private String tableName;
        
        // The name of the index
        private String indexName;
        
        // The name of the tenant column, if this is a multi-tenant database configuration
        private String tenantColumnName;
        
        // The version number associated with this change
        private int version;
        
        // The list of columns to index
        private final List<String> indexCols = new ArrayList<>();
        
        // Is this a unique index?
        private boolean unique;

        
        /**
         * @param schemaName the schemaName to set
         */
        public Builder setSchemaName(String schemaName) {
            this.schemaName = schemaName;
            return this;
        }

        
        /**
         * @param tableName the tableName to set
         */
        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        
        /**
         * @param indexName the indexName to set
         */
        public Builder setIndexName(String indexName) {
            this.indexName = indexName;
            return this;
        }
        
        /**
         * @param version the version to set
         */
        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        
        /**
         * @param unique the unique to set
         */
        public Builder setUnique(boolean unique) {
            this.unique = unique;
            return this;
        }

        /**
         * Add the named column to the list of columns
         * @param column
         * @return
         */
        public Builder addColumn(String column) {
            this.indexCols.add(column);
            return this;
        }

        /**
         * Build an instance of {@link CreateIndex} using the current state of this Builder.
         * @return
         */
        public CreateIndex build() {
            if (this.indexCols.isEmpty()) {
                throw new IllegalStateException("no index columns defined for index '" + indexName + "'");
            }
            return new CreateIndex(schemaName, tableName, version,
                new IndexDef(indexName, indexCols, unique), tenantColumnName);
        }
        
        /**
         * Sets the tenantColumnName property
         * @param name
         * @return
         */
        public Builder setTenantColumnName(String name) {
            this.tenantColumnName = name;
            return this;
        }
    }
}