/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rarnold
 *
 */
public class FromClause {
    private final List<FromItem> items = new ArrayList<>();

    /**
     * @param schemaName
     * @param tableName
     */
    public void addTable(String schemaName, String tableName) {
        FromItemTable fit = new FromItemTable(schemaName, tableName);
        items.add(fit);
    }

    public void addTable(String schemaName, String tableName, Alias alias) {
        FromItemTable fit = new FromItemTable(schemaName, tableName, alias);
        items.add(fit);
    }

    public void addTable(String tableName, Alias alias) {
        FromItemTable fit = new FromItemTable(null, tableName, alias);
        items.add(fit);
    }

    /**
     * Add the sub-query as an item in the from list
     * @param sub
     * @param alias
     */
    public void addFrom(Select sub, Alias alias) {
        FromItemSelect fis = new FromItemSelect(sub, alias);
        items.add(fis);
    }

    @Override
    public String toString() {
        return items.stream().map(FromItem::toString).collect(Collectors.joining(", "));
    }

}
