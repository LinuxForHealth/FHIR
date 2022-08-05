/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

/**
 * Data Model Interface
 */
public interface IDataModel {

    /**
     * Look up the table definition
     * @param schemaName
     * @param tableName
     * @return
     */
    public Table findTable(String schemaName, String tableName);

    /**
     * Is the target database distributed (e.g. with sharding)?
     * @return
     */
    public boolean isDistributed();
}
