/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.conformance.spi;

import java.util.Collection;

import com.ibm.watson.health.fhir.conformance.ConformanceResource;

public interface ConformanceResourceProvider {
    Collection<ConformanceResource> getConformanceResources();
}