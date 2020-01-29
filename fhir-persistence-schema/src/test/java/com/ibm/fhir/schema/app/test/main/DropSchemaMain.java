/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.test.main;

import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.test.main.helper.TestHelper;

/**
 * Runs the Drop Schema locally. 
 */
public class DropSchemaMain {
    public static void main(String[] args) {
        String[] arguments = {
                "--prop-file", TestHelper.absolutePathToProperties(),
                "--pool-size", "2",
                "--schema-name", "FHIRDATA",
                "--drop-schema",
                "--confirm-drop"
        };
        Main.main(arguments);
        
        String[] arguments2 = {
                "--prop-file", TestHelper.absolutePathToProperties(),
                "--pool-size", "2",
                "--schema-name", "FHIR_ADMIN",
                "--drop-admin",
                "--confirm-drop"
        };
        Main.main(arguments2);
    }
}