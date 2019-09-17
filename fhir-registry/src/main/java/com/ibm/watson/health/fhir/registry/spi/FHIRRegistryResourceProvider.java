/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.registry.spi;

import java.util.Collection;

import com.ibm.watson.health.fhir.registry.resource.FHIRRegistryResource;

public interface FHIRRegistryResourceProvider {
    Collection<FHIRRegistryResource> getResources();
}