/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.schema.build;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.PlainSchemaAdapter;

/**
 * Represents an adapter used to build the standard FHIR schema
 */
public class FhirSchemaAdapter extends PlainSchemaAdapter {

    /**
     * Public constructor
     * 
     * @param databaseAdapter
     */
    public FhirSchemaAdapter(IDatabaseAdapter databaseAdapter) {
        super(databaseAdapter);
    }
}
