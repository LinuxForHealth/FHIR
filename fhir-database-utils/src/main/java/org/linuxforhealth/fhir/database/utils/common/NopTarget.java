/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseSupplier;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTarget;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

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
