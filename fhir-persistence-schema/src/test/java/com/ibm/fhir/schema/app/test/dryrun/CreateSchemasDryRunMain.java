/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.test.dryrun;

import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.test.TestHelper;

public class CreateSchemasDryRunMain {

    public static void main(String[] args) {
        String[] arguments = {
                "--prop-file", TestHelper.absolutePathToProperties(),
                "--pool-size", "2",
                "--schema-name", "FHIRDATA",
                "--create-schemas",
                "--dry-run"
        };
        Main.main(arguments);
    }
}