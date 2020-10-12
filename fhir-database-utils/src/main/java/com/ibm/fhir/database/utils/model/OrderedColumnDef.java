/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;


/**
 * Defines a column with ASC or DESC order and NULLS FIRST/LAST.
 */
public class OrderedColumnDef {

    public static enum Direction {
        ASC, DESC
    }
    
    public static enum NullOrder {
        NULLS_FIRST("NULLS FIRST"), 
        NULLS_LAST("NULLS LAST");
        
        private final String value;
        
        private NullOrder(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
 
    // The column name
    private final String columnName;

    // collation order
    private final Direction direction;

    // collation behavior for nulls
    private final NullOrder nullOrder;
    
    public OrderedColumnDef(String columnName, Direction direction, NullOrder nullOrder) {
        this.columnName = columnName;
        this.direction = direction;
        this.nullOrder = nullOrder;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(columnName);
        
        if (direction != null) {
            result.append(" ");
            result.append(direction.name());
        }
        
        if (nullOrder != null) {
            result.append(" ");
            result.append(nullOrder.toString());
        }
        
        return result.toString();
    }
    
    public String getColumnName() {
        return this.columnName;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public NullOrder getNullOrder() {
        return this.nullOrder;
    }
}