/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import org.linuxforhealth.fhir.registry.util.PackageRegistryResourceProvider;

public class FHIRPathTestResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "fhir.path.test";
    }
}
