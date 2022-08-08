/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.ig.mcode;

import org.linuxforhealth.fhir.registry.util.PackageRegistryResourceProvider;

public class MCODEResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "hl7.fhir.us.mcode";
    }
}
