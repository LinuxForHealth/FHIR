/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.model;

/**
 * Represents a column definition in a table
 * @author rarnold
 *
 */
public class ColumnDef implements Comparable<ColumnDef> {
    private final String name;
    private boolean nullable;
    private ColumnType columnType;
    private long size;
    private int precision;
    private int inlineSize;
    
    /**
     * Public constructor
     * @param name
     */
    public ColumnDef(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    public ColumnType getColumnType() {
        return columnType;
    }
    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public int getPrecision() {
        return precision;
    }
    public void setPrecision(int precision) {
        this.precision = precision;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ColumnDef that) {
        return this.name.compareTo(that.name);
    }
    
    @Override 
    public boolean equals(Object other) {
        if (other instanceof ColumnDef) {
            ColumnDef that = (ColumnDef)other;
            return this.name.equals(that.name);
        }
        else {
            return false;
        }
    }
    
    @Override 
    public int hashCode() {
        return this.name.hashCode();
    }

    public int getInlineSize() {
        return inlineSize;
    }

    public void setInlineSize(int inlineSize) {
        this.inlineSize = inlineSize;
    }
    
}
