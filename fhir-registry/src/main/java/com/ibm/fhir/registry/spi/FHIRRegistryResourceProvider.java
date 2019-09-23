/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spi;

import java.util.Collection;

import com.ibm.fhir.registry.resource.FHIRRegistryResource;

public interface FHIRRegistryResourceProvider {
    Collection<FHIRRegistryResource> getResources();
}