/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class BulkDataResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.uv.bulkdata";
    }
}
