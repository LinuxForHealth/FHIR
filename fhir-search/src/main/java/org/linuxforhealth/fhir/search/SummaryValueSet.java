/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search;

/**
 * Search _summary Constants
 */
public enum SummaryValueSet {
        TRUE("true"),
        TEXT("text"),
        DATA("data"),
        COUNT("count"),
        FALSE("false");
     
        private final String value;
     
        SummaryValueSet(String value) {
            this.value = value;
        }
     
        public String value() {
            return value;
        }
        
        public static SummaryValueSet from(String value) {
            for (SummaryValueSet c : SummaryValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
}
