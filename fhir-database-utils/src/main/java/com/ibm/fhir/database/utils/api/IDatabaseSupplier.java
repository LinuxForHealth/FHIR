/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

import java.sql.Connection;

/**
 * Represents a statement runnable by {@link IDatabaseAdapter} returning a result
 * of type T.
 */
public interface IDatabaseSupplier<T> {

    /**
     * Execute the statement using the connection and return the value
     * @param translator to translate any exceptions
     * @param c
     */
    public T run(IDatabaseTranslator translator, Connection c);
}
