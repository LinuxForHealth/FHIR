/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;

/**
 * Represents the ROW type used to pass parameters to the add_resource stored procedures
 *         CREATE OR REPLACE TYPE <schema>.t_str_values AS ROW (parameter_name_id INTEGER, str_value VARCHAR(511 OCTETS), str_value_lcase   VARCHAR(511 OCTETS))
 */
public class RowType extends BaseObject {
    private final List<ColumnBase> columns = new ArrayList<>();

    public RowType(String schemaName, String typeName, int version, Collection<ColumnBase> cols) {
        super(schemaName, typeName, DatabaseObjectType.TYPE, version);
        this.columns.addAll(cols);
    }

    @Override
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        target.createRowType(getSchemaName(), getObjectName(), columns);
    }

    @Override
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context) {
        if (priorVersion != null && priorVersion > 0 && this.version > priorVersion) {
            throw new UnsupportedOperationException("Upgrading row types is not supported");
        }
        apply(target, context);
    }

    @Override
    public void drop(ISchemaAdapter target) {
        target.dropType(getSchemaName(), getObjectName());
    }

    /**
     * Builder pattern
     *
     */
    public static class Builder extends ColumnDefBuilder {
        private String schemaName;

        private String typeName;

        private int version = 1;

        /**
         * Setter for the schema name
         * @param schemaName
         * @return
         */
        public Builder setSchemaName(String schemaName) {
            this.schemaName = schemaName;
            return this;
        }

        /**
         * Setter for the version of this particular change
         * @param v
         * @return
         */
        public Builder setVersion(int v) {
            this.version = v;
            return this;
        }

        /**
         * Setter for the name of the table being built
         * @param typeName
         * @return
         */
        public Builder setTypeName(String typeName) {
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
            return new RowType(this.schemaName, this.typeName, version, buildColumns());
        }

    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visit(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.model.IDatabaseObject#visitReverse(com.ibm.fhir.database.utils.model.DataModelVisitor)
     */
    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }
}
