/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.mcode;

import java.util.Collection;

import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;
import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class MCODEResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public Collection<FHIRRegistryResource> getResources() {
        return FHIRRegistryUtil.getResources("hl7.fhir.us.mcode");
    }
}
