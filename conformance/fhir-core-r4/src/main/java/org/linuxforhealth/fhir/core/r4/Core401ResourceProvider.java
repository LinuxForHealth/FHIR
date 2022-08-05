/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.r4;

import org.linuxforhealth.fhir.registry.util.PackageRegistryResourceProvider;

public class Core401ResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.core.401";
    }
}
