/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Represents the list of columns, expressions or sub-queries being selected
 * from one or more tables (or subqueries)
 */
public class SelectList {
    private final List<SelectItem> items = new ArrayList<>();

    public SelectItemColumn addColumn(String c) {
        SelectItemColumn column = new SelectItemColumn(null, c, null);
        items.add(column);
        return column;
    }

    public SelectItemColumn addColumn(String source, String name) {
        SelectItemColumn column = new SelectItemColumn(source, name, null);
        items.add(column);
        return column;
    }

    public SelectItemColumn addColumn(String source, String name, Alias alias) {
        SelectItemColumn column = new SelectItemColumn(source, name, alias);
        items.add(column);
        return column;
    }

    public SelectItemSubQuery addSubQuery(Select subQuery, Alias alias) {
        SelectItemSubQuery column = new SelectItemSubQuery(subQuery, alias);
        items.add(column);
        return column;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for(SelectItem selectItem : items) {
            joiner.add(selectItem.toString());
        }
        return joiner.toString();
    }
}