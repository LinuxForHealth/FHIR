/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Represents a column definition in a table
 */
public class ColumnDef implements Comparable<ColumnDef> {
    private final String name;
    private boolean nullable;
    private ColumnType columnType;
    private long size;
    private Integer precision;
    private int inlineSize;
    private String defaultVal;

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
    public Integer getPrecision() {
        return precision;
    }
    public void setPrecision(int precision) {
        this.precision = precision;
    }
    public String getDefaultVal() {
        return defaultVal;
    }
    /**
     * @param defaultVal this value is NOT auto-quoted, you must pass the single-quote (') within the string value for literal strings
     * @implNote the reason the defaultVal SQL string literal value must be explicitly quoted is because PostgreSQL
     *           doesn't allow numeric literals to be quoted
     */
    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    @Override
    public int compareTo(ColumnDef that) {
        return this.name.compareTo(that.name);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ColumnDef) {
            ColumnDef that = (ColumnDef)other;
            return this.name.equals(that.name);
        } else {
            return false;
        }
    }

    public int getInlineSize() {
        return inlineSize;
    }

    public void setInlineSize(int inlineSize) {
        this.inlineSize = inlineSize;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}