/*
 * (C) Copyright IBM Corp. 2019,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;

/**
 * Creates an Export of FHIR Data to NDJSON format
 *
 * @link https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-export.html At the time of building this...
 *       BulkDataAccess IG: STU1
 *
 * These three operation definitions are for <code>$export</code>:
 * @link export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-export.json.html
 * @link patient-export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-patient-export.json.html
 * @link group-export https://build.fhir.org/ig/HL7/bulk-data/OperationDefinition-group-export.json.html
 *
 */
public class PatientExportOperation extends ExportOperation {

    private static final String FILE = "OperationDefinition-patient-export.json";

    public PatientExportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        /**
         * Loads the operation definition file. In this case, there are three files, and only one is loaded.
         */
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
