/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query;

/**
 * A column reference, expression or sub-query item used in the selection list
 */
public class SelectItem {
    // The alias to use for this item. Can be null
    private Alias alias;

    /**
     * Default constructor
     */
    protected SelectItem() {
    }

    /**
     * Public constructor
     * @param alias
     */
    protected SelectItem(Alias alias) {
        this.alias = alias;
    }

    public Alias getAlias() {
        return this.alias;
    }

    public void setAlias(Alias alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return alias.toString();
    }
}