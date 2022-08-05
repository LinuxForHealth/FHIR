/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Simple modeling of an insert statement. Handles parameter markers
 * as well as simple constant expressions (as strings)
 * 
 * We make the statement itself immutable, relying on a Builder to create it
 */
public class InsertStatement {

    private final String schemaName;
    private final String tableName;
    private final List<Column> columns = new ArrayList<>();

    private InsertStatement(String schemaName, String tableName, List<Column> cols) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columns.addAll(cols);
    }
    
    @Override
    public String toString() {
        final String tbl = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String cols = columns.stream().map(c -> c.getName()).collect(Collectors.joining(","));
        final String vals = columns.stream().map(c -> c.getValue()).collect(Collectors.joining(","));
        
        StringBuilder result = new StringBuilder();
        result.append("INSERT INTO ");
        result.append(tbl);
        result.append("(");
        result.append(cols);
        result.append(") VALUES (");
        result.append(vals);
        result.append(")");
        return result.toString();
    }

    /**
     * Factory method for creating a build instance for an {@link InsertStatement}
     * @param schemaName
     * @param tableName
     * @return
     */
    public static Builder builder(String schemaName, String tableName) {
        return new Builder(schemaName, tableName);
    }
    
    public static class Builder {
        private final String schemaName;
        private final String tableName;
        private final List<Column> columns = new ArrayList<>();
        
        /**
         * Private constructor to force creation via the {@link InsertStatement#builder(String, String)}
         * factory method
         * @param schemaName
         * @param tableName
         */
        private Builder(String schemaName, String tableName) {
            this.schemaName = schemaName;
            this.tableName = tableName;
        }

        public InsertStatement build() {
            return new InsertStatement(schemaName, tableName, columns);
        }

        /**
         * Add a column name to the insert statement
         * @param columnName
         */
        public Builder addColumn(String columnName) {
            this.columns.add(new Column(columnName, "?"));
            return this;
        }

        /**
         * Add a column with a literal value (e.g. "CURRENT TIMESTAMP")
         * @param columnName
         * @param value
         * @return
         */
        public Builder addColumn(String columnName, String value) {
            this.columns.add(new Column(columnName, value));
            return this;
        }

    }
    
    /**
     * A definition of a column in the insert statement

     *
     */
    private static class Column {
        final String name;
        final String value;
        
        private Column(String name, String value) {
            this.name = name;
            this.value = value;
        }
        
        private String getName() {
            return this.name;
        }
        
        private String getValue() {
            return this.value;
        }
    }
}
