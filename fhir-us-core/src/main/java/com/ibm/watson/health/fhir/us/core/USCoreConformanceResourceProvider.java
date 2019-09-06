/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.us.core;

import java.util.Collection;

import com.ibm.watson.health.fhir.conformance.ConformanceResource;
import com.ibm.watson.health.fhir.conformance.spi.ConformanceResourceProvider;
import com.ibm.watson.health.fhir.conformance.util.ConformanceUtil;
import com.ibm.watson.health.fhir.model.format.Format;

public class USCoreConformanceResourceProvider implements ConformanceResourceProvider {
    @Override
    public Collection<ConformanceResource> getConformanceResources() {
        return ConformanceUtil.getConformanceResources(Format.JSON, getClass().getClassLoader());
    }
}