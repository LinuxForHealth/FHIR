/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.common;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTarget;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;

/**
 * An {@link IDatabaseTarget} which just acts as a sink and doesn't actually do
 * anything. Useful for tests, just to exercise the code.
 * 
 *
 */
public class NopTarget implements IDatabaseTarget {

    /**
     * Public constructor
     */
    public NopTarget() {
        // No operation
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, String ddl) {
        // No operation
    }

    @Override
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value) {
        // No operation
    }

    @Override
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement) {
        // No operation
    }

    @Override
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier) {
        // No operation
        return null;
    }
}
