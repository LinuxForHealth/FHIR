/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.test.main.dryrun;

import com.ibm.fhir.schema.app.Main;
import com.ibm.fhir.schema.app.test.main.helper.TestHelper;

public class AllocateTenantDryRunMain {
    public static void main(String[] args) {
        String[] arguments = {
                "--prop-file", TestHelper.absolutePathToProperties(),
                "--pool-size", "5",
                "--schema-name", "FHIRDATA",
                "--allocate-tenant", "default10",
                "--dry-run"
        };
        Main.main(arguments);
    }
}