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
 * Creates an Group Export of FHIR Data to NDJSON format
 *
 * <a href="https://hl7.org/Fhir/uv/bulkdata/OperationDefinition-group-export.json.html">BulkDataAccess V1.0.0: STU1</a>
 *
 * group export operation definition for <code>$export</code>:
 *
 */
public class GroupExportOperation extends ExportOperation {

    private static final String FILE = "OperationDefinition-group-export.json";

    public GroupExportOperation() {
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
