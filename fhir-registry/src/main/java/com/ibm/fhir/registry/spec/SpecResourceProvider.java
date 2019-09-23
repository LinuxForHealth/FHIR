/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spec;

import java.util.Collection;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;

public class SpecResourceProvider implements FHIRRegistryResourceProvider {
    @Override
    public Collection<FHIRRegistryResource> getResources() {
        return FHIRRegistryUtil.getResources(Format.JSON, getClass().getClassLoader(), "spec.index");
    }
}