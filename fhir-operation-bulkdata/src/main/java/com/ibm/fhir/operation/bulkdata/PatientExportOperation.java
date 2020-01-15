/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;

/**
 * Creates an Patient Export of FHIR Data to NDJSON format
 *
 * @link https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-patient-export.json.html At the time of building this...
 *       BulkDataAccess V1.0.0: STU1
 *
 * patient export operation definition for <code>$export</code>:
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
