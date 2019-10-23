/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

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
}
