/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

/**
 * When to generate a value for an identity column.
 */
public enum Generated {
    ALWAYS, BY_DEFAULT;
    
    /**
     * @return the SQL keyword or phrase for this Generated option
     */
    @Override
    public String toString() {
        switch (this) {
        case BY_DEFAULT:
            return "BY DEFAULT";
        default:
            return this.name();
        }
    }
}
