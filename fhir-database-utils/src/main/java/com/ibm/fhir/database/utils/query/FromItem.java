/*
 * (C) Copyright IBM Corp. 2019,2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * An element in the FROM clause
 */
public class FromItem {

    // The source we are joining to
    private final RowSource rowSource;

    // Optional alias...but recommended to avoid nasty surprises
    private final Alias alias;

    /**
     * @param rowSource
     * @param alias
     */
    protected FromItem(RowSource rowSource, Alias alias) {
        this.rowSource = rowSource;
        this.alias = alias;
    }

    /**
     * Get the alias associated with this item in the from clause.
     * @return the alias for this item. Can be null
     */
    public Alias getAlias() {
        Alias result = this.alias;

        if (result == null) {
            // Use the alias from the row source if we don't have one already
            result = rowSource.getImpliedAlias();
        }

        // can be null
        return this.alias;
    }

    /**
     * Is this an ANSI-style join?
     * @return
     */
    public boolean isAnsiJoin() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(rowSource.toString());

        // the alias can be null - it's not always needed, but recommended
        Alias aliasName = getAlias();
        if (aliasName != null) {
            result.append(" AS ").append(aliasName.toString());
        }

        return result.toString();
    }
}