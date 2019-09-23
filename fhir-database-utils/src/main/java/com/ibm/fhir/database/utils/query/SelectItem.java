/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * A column reference, expression or sub-query item used in the selection list
 * @author rarnold
 *
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
}
