/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.build;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.PlainSchemaAdapter;

/**
 * Represents an adapter used to build the FHIR schema when
 * used with a distributed like Citus
 */
public class DistributedSchemaAdapter extends PlainSchemaAdapter {

    /**
     * Public constructor
     * 
     * @param databaseAdapter
     */
    public DistributedSchemaAdapter(IDatabaseAdapter databaseAdapter) {
        super(databaseAdapter);
    }
}
