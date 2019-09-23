/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

/**
 * An entry in the FROM clause
 * @author rarnold
 *
 */
public class FromItem {
    private final Alias alias;
    
    protected FromItem(Alias alias) {
        this.alias = alias;
    }
    
    public Alias getAlias() {
        return this.alias;
    }
}
