/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * Represents a table referenced in the from list
 * @author rarnold
 *
 */
public class FromItemSelect extends FromItem {
    
    // The sub-query
    private final Select select;

    /**
     * Protected constructor
     * 
     * @param subQuery
     * @param alias
     */
    protected FromItemSelect(Select subQuery, Alias alias) {
        super(alias);
        this.select = subQuery;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(select.toString());
        result.append(")");
        
        Alias alias = getAlias();
        if (alias != null) {    
            result.append(" AS ").append(alias.toString());
        }
        return result.toString();
    }
}
