/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class FHIRTermServiceTestResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "fhir.term.service.test";
    }
}
