/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.schema.size;

import java.sql.Connection;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Interface for a command to fetch schema size information from a database
 */
public interface ISizeCollector {

    /**
     * Collect database size metrics for the given schema using the 
     * given database connection
     * @param schemaName
     * @param connection
     */
    public void run(String schemaName, Connection connection, IDatabaseTranslator translator);
}
