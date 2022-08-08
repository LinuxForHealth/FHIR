/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * Item in a SELECT clause which is itself a sub-query
 */
public class SelectItemSubQuery extends SelectItem {
    private final Select subQuery;

    /**
     * @param subQuery
     * @param alias
     */
    public SelectItemSubQuery(Select subQuery, Alias alias) {
        super(alias);
        this.subQuery = subQuery;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("(");
        result.append(subQuery.toString());
        result.append(")");

        Alias alias = getAlias();
        if (alias != null) {
            result.append(" AS ").append(alias.toString());
        }
        return result.toString();
    }
}