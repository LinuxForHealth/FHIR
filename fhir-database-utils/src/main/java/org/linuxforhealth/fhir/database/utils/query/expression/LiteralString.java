/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.expression;

/**
 * Simple container of a String value to make overloading of some methods simpler
 * and more natural
 */
public class LiteralString {
    private final String value;

    public LiteralString(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}