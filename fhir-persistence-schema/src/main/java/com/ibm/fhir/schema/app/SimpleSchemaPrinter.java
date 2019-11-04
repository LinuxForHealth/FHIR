/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import com.ibm.fhir.database.utils.common.PrintTarget;

import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;

/**
 * A simple main method for printing the DDL of the FHIR Server.
 * For real work, use {@link Main}.
 */
public class SimpleSchemaPrinter {
    private static final String SCHEMA_NAME = "FHIRAPP";
    private static final String ADMIN_SCHEMA_NAME = "FHIRADMIN";
    
    public static void main(String[] args) {
        // Create an instance of the service and use it to test creation
        // of the FHIR schema
        FhirSchemaGenerator gen = new FhirSchemaGenerator(ADMIN_SCHEMA_NAME, SCHEMA_NAME);
        PhysicalDataModel model = new PhysicalDataModel();
        gen.buildSchema(model);
        
        // Print the statements instead of executing them against a database
        PrintTarget tgt = new PrintTarget(null, true);
        
        // Pretend that our target is a DB2 database
        Db2Adapter adapter = new Db2Adapter(tgt);
        model.apply(adapter);
    }
}