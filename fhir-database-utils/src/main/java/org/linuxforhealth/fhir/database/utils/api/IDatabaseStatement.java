/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

import java.sql.Connection;

/**
 * Definition of the Database Statement
 */
public interface IDatabaseStatement {

    /**
     * Execute the statement using the connection.
     * @param translator to translate any exceptions
     * @param c
     */
    public void run(IDatabaseTranslator translator, Connection c);
}
