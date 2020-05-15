/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.test;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class FHIRTermTestResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "fhir.term.test";
    }
}
