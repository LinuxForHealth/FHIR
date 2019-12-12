/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

public enum Generated {
    ALWAYS, BY_DEFAULT;
    
    @Override
    public String toString() {
        switch (this) {
        case BY_DEFAULT: return "BY DEFAULT";
        default: return this.name();
        }
    }
}
