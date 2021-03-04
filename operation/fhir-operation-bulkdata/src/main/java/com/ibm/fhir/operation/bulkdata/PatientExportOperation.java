/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;

/**
 * <a href="https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-patient-export.json.html">BulkDataAccess V1.0.0: STU1 -
 * ExportOperation</a> Creates an Patient Export of FHIR Data to NDJSON format patient export operation definition for
 * <code>$export</code>
 */
public class PatientExportOperation extends ExportOperation {
    private static final String FILE = "export-patient.json";

    public PatientExportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}