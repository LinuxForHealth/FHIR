/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Builder pattern to make it easy to add column definitions to an object (e.g. table, type etc)
 */
public class ColumnDefBuilder {
    // LinkedHashSet so we can remember order
    protected LinkedHashSet<ColumnDef> columns = new LinkedHashSet<>();

    public void checkColumnAlreadyExists(ColumnDef cd, String columnName) {
        if (columns.contains(cd)) {
            throw new IllegalArgumentException("Duplicate column: " + columnName);
        }
    }

    public ColumnDefBuilder addIntColumn(String columnName, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);
        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.INT);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addSmallIntColumn(String columnName, Integer defaultValue, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);
        cd.setNullable(nullable);

        if (defaultValue != null) {
            cd.setDefaultVal(Integer.toString(defaultValue));
        }

        cd.setColumnType(ColumnType.SMALLINT);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addBigIntColumn(String columnName, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.BIGINT);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addDoubleColumn(String columnName, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.DOUBLE);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addTimestampColumn(String columnName, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.TIMESTAMP);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addVarcharColumn(String columnName, int size, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.VARCHAR);
        cd.setSize(size);
        columns.add(cd);
        return this;
    }

    /**
     * Add char (fixed-width) column
     * @param columnName
     * @param size
     * @param nullable
     * @return
     */
    public ColumnDefBuilder addCharColumn(String columnName, int size, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.CHAR);
        cd.setSize(size);
        columns.add(cd);
        return this;
    }

    public ColumnDefBuilder addBlobColumn(String columnName, long size, int inlineSize, boolean nullable) {
        ColumnDef cd = new ColumnDef(columnName);
        checkColumnAlreadyExists(cd, columnName);

        cd.setNullable(nullable);
        cd.setColumnType(ColumnType.BLOB);
        cd.setSize(size);
        cd.setInlineSize(inlineSize);
        columns.add(cd);
        return this;
    }

    /**
     * Check each of the columns in the given array are valid column names
     * @param columns
     */
    protected void checkColumns(String[] columns) {
        checkColumns(Arrays.asList(columns));
    }

    /**
     * Check each of the columns in the given array are valid column names
     * @param columns
     */
    protected void checkColumns(Collection<String> columns) {
        for (String columnName: columns) {
            ColumnDef cd = new ColumnDef(columnName);
            if (!this.columns.contains(cd)) {
                throw new IllegalArgumentException("Invalid column name: " + columnName);
            }
        }
    }

    /**
     * Create the columns for the table based on the definitions that have been added
     * @return
     */
    public List<ColumnBase> buildColumns() {
        List<ColumnBase> result = new ArrayList<>();

        for (ColumnDef cd: this.columns) {
            ColumnBase column;
            switch (cd.getColumnType()) {
            case BIGINT:
                column = new BigIntColumn(cd.getName(), cd.isNullable());
                break;
            case INT:
                column = new IntColumn(cd.getName(), cd.isNullable());
                break;
            case SMALLINT:
                column = new SmallIntColumn(cd.getName(), cd.isNullable(), cd.getDefaultVal());
                break;
            case DOUBLE:
                column = new DoubleColumn(cd.getName(), cd.isNullable());
                break;
            case TIMESTAMP:
                column = new TimestampColumn(cd.getName(), cd.isNullable(), cd.getPrecision());
                break;
            case VARCHAR:
                if (cd.getSize() > Integer.MAX_VALUE) {
                    throw new IllegalStateException("Invalid size for column: " + cd.getName());
                }
                column = new VarcharColumn(cd.getName(), (int)cd.getSize(), cd.isNullable());
                break;
            case CHAR:
                if (cd.getSize() > Integer.MAX_VALUE) {
                    throw new IllegalStateException("Invalid size for column: " + cd.getName());
                }
                column = new CharColumn(cd.getName(), (int)cd.getSize(), cd.isNullable());
                break;
            case BLOB:
                column = new BlobColumn(cd.getName(), cd.getSize(), cd.getInlineSize(), cd.isNullable());
                break;
            case CLOB:
                column = new ClobColumn(cd.getName(), cd.isNullable(), cd.getDefaultVal());
                break;
            default:
                throw new IllegalStateException("Unsupported column type: " + cd.getColumnType().name());
            }
            result.add(column);
        }
        return result;
    }
}