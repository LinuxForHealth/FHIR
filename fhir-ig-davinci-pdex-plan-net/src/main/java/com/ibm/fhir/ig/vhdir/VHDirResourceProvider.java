/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.vhdir;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class VHDirResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.uv.vhdir";
    }
}
