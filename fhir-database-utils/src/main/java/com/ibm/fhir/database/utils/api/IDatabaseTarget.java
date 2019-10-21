/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Abstraction of the ability to execute statements against a database. This allows
 * for better unit testing, especially those functions which are not supported by
 * Derby.
 */
public interface IDatabaseTarget {

    /**
     * Run the given DDL statement against the connection managed by the implementation
     * of this interface
     * @param ddl
     */
    public void runStatement(IDatabaseTranslator translator, final String ddl);

    /**
     * Convenience method for running a SQL statement requiring a single int parameter
     * @param translator
     * @param sql
     * @param value
     */
    public void runStatementWithInt(IDatabaseTranslator translator, String sql, int value);
    
    
    /**
     * Run the statement using the resources (e.g. connection) held by the implementation
     * of this interface
     * @param statement
     */
    public void runStatement(IDatabaseTranslator translator, IDatabaseStatement statement);

    /**
     * Run a statement returning a value of type T.
     * @param supplier
     * @return
     */
    public <T> T runStatement(IDatabaseTranslator translator, IDatabaseSupplier<T> supplier);

}
