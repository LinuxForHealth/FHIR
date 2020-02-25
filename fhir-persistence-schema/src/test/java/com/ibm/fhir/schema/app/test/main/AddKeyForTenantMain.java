/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.test.main;

import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.test.main.helper.TestHelper;

public class AddKeyForTenantMain {
    public static void main(String[] args) {
        String[] arguments = {
                "--prop-file", TestHelper.absolutePathToProperties(),
                "--pool-size", "5",
                "--schema-name", "FHIRDATA",
                "--add-tenant-key", "default"
        };
        Main.main(arguments);
    }
}