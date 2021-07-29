/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class USCoreSTU3ResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.us.core.stu3";
    }
}
