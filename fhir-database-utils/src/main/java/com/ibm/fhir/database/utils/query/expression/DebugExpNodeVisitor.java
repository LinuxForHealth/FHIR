/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.time.Instant;

import com.ibm.fhir.database.utils.query.Select;

/**
 * Debug version of the {@link StringExpNodeVisitor} which is used to
 * render the string with the bind variable values written in-place,
 * which is handy for debug.
 */
public class DebugExpNodeVisitor extends StringExpNodeVisitor {
    private final static String NULL = "NULL";

    /**
     * Simple rendering of the expression tree to a string, ignoring
     * the bind marker values
     */
    public DebugExpNodeVisitor() {
        super(true); // pretty-print the expression
    }

    @Override
    public String bindMarker(Double value) {
        return value != null ? value.toString() : NULL;
    }

    @Override
    public String bindMarker(Long value) {
        return value != null ? value.toString() : NULL;
    }

    @Override
    public String bindMarker(String value) {
        if (value != null) {
            return "'" + value + "'";
        } else {
            return NULL;
        }
    }

    @Override
    public String bindMarker(Instant value) {
        return value != null ? value.toString() : NULL;
    }

    @Override
    public String bindMarker(Integer value) {
        return value != null ? value.toString() : NULL;
    }

    @Override
    public String select(Select select) {
        super.select(select);

        // but the string we want to render is the debug string so we get the bind variables
        // substituted inline
        return select.toDebugString();
    }

}