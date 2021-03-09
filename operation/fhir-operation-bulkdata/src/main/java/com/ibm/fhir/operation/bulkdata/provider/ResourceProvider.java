/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.provider;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

/**
 * Resource Provider for the IG Capability Statement for Bulk Data
 */
public class ResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.uv.bulkdata";
    }
}