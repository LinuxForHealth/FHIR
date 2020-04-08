/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.core;

import java.util.Collection;

import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;
import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class CoreResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public Collection<FHIRRegistryResource> getResources() {
        return FHIRRegistryUtil.getResources("hl7.fhir.core");
    }
}