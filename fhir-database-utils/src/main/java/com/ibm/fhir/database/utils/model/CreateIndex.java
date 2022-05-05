/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DistributionType;
import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;
import com.ibm.fhir.database.utils.common.CreateIndexStatement;

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

    // The table name the index will be created on
    private final String tableName;

    // Distribution rules if the associated table is distributed
    private final DistributionType distributionType;

    /**
     * Protected constructor. Use the Builder to create instance.
     * @param schemaName
     * @param indexName
     * @param version
     * @param distributionType
     */
    protected CreateIndex(String schemaName, String versionTrackingName, String tableName, int version, IndexDef indexDef, String tenantColumnName,
            DistributionType distributionType) {
        super(schemaName, versionTrackingName, DatabaseObjectType.INDEX, version);
        this.tableName = tableName;
        this.indexDef = indexDef;
        this.tenantColumnName = tenantColumnName;
        this.distributionType = distributionType;
    }
    
    /**
     * Return a new Builder instance for creating instances of CreateIndex
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Build a CreateIndexStatement from this CreateIndex object
     * @return
     */
    public CreateIndexStatement createStatement() {
        return indexDef.createStatement(getSchemaName(), this.tableName, this.tenantColumnName);
    }
    

    /**
     * Get the name of the table this index is built on
     * @return
     */
    public String getTableName() {
        return this.tableName;
    }
    
    @Override
    public String getTypeNameVersion() {
        // for cases where this CreateIndex object is defined standalone (as opposed to
        // being defined inside a Table definition), we need to make sure it gets a
        // unique name for the task collector. Note that the index name needs to be
        // unique, so we don't need to qualify with the table name.
        StringBuilder result = new StringBuilder();
        result.append(getObjectType().name());
        result.append(":");
        result.append(getQualifiedName());
        result.append(":");
        result.append(this.version);
        return result.toString();
    }

    
    @Override
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        long start = System.nanoTime();
        indexDef.apply(getSchemaName(), getTableName(), tenantColumnName, target, distributionType);
        
        if (logger.isLoggable(Level.FINE)) {
            long end = System.nanoTime();
            long elapsedMillis = (end - start) / 1000000;
            logger.fine(String.format("Created index %s [took=%dms]", indexDef.toString(), elapsedMillis));
        }
    }

    @Override
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context) {
        apply(target, context);
    }

    @Override
    public void drop(ISchemaAdapter target) {
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
        private final List<OrderedColumnDef> indexCols = new ArrayList<>();
        
        // Is this a unique index?
        private boolean unique;
        
        // Special case to handle a previous defect where indexes were tracked using tableName in version_history
        private String versionTrackingName;

        // Set if the table is distributed
        private DistributionType distributionType = DistributionType.NONE;

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
        
        public Builder setVersionTrackingName(String name) {
            this.versionTrackingName = name;
            return this;
        }

        /**
         * Setter for distributionType
         * @param dt
         * @return
         */
        public Builder setDistributionType(DistributionType dt) {
            this.distributionType = dt;
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
            this.indexCols.add(new OrderedColumnDef(column, null, null));
            return this;
        }

        /**
         * Add the named column to the index, with specific order and null value
         * collation rules
         * @param column
         * @param direction
         * @param nullOrder
         * @return
         */
        public Builder addColumn(String column, OrderedColumnDef.Direction direction, OrderedColumnDef.NullOrder nullOrder) {
            this.indexCols.add(new OrderedColumnDef(column, direction, nullOrder));
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
            
            String versionTrackingName = this.versionTrackingName;
            if (versionTrackingName == null) {
                versionTrackingName = this.indexName;
            }
            
            return new CreateIndex(schemaName, versionTrackingName, tableName, version,
                new IndexDef(indexName, indexCols, unique), tenantColumnName, distributionType);
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