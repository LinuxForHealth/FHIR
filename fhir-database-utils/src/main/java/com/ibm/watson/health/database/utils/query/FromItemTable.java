/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.database.utils.query;

/**
 * Represents a table referenced in the from list
 * @author rarnold
 *
 */
public class FromItemTable extends FromItem {
    // optional schema name
    private final String schemaName;
    
    // The name of the table
    private final String tableName;

    /**
     * Protected constructor
     * @param tableName
     */
    protected FromItemTable(String tableName) {
        // the alias is the same as the name of the table
        super(new Alias(tableName));
        this.schemaName = null;
        this.tableName = tableName;
    }

    /**
     * Protected constructor
     * @param tableName
     */
    protected FromItemTable(String tableName, Alias alias) {
        super(alias);
        this.schemaName = null;
        this.tableName = tableName;
    }
    
    /**
     * Protected constructor
     * @param tableName
     */
    protected FromItemTable(String schemaName, String tableName, Alias alias) {
        super(alias);
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    /**
     * Protected constructor
     * @param tableName
     */
    protected FromItemTable(String schemaName, String tableName) {
        super(null);
        this.schemaName = schemaName;
        this.tableName = tableName;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (schemaName != null) {
            result.append(schemaName).append(".");
        }
        
        result.append(tableName);
        
        Alias alias = getAlias();
        if (alias != null) {
            result.append(" AS ");
            result.append(alias.toString());
        }
        
        return result.toString();
    }

}
