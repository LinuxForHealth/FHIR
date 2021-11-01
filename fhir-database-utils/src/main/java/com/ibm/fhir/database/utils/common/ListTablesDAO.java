/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.common;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;

/**
 * A DAO to list the tables in a given schema
 */
public interface ListTablesDAO extends IDatabaseSupplier<SchemaInfoObject>{

}
