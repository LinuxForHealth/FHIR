/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.us.core.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.ig.us.core.USCoreResourceProvider;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;
import com.ibm.fhir.validation.FHIRValidator;

public class USCoreResourceProviderTest {
    @Test
    public void testUSCoreResourceProvider() {
        FHIRRegistryResourceProvider provider = new USCoreResourceProvider();
        assertEquals(provider.getRegistryResources().size(), 147);
    }

    @Test
    public void testValidateResources() throws Exception {
        FHIRRegistryResourceProvider provider = new USCoreResourceProvider();

        List<Exception> exceptions = new ArrayList<>();
        List<Issue> issues = new ArrayList<>();

        FHIRValidator validator = FHIRValidator.validator();

        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            try {
                Resource resource = registryResource.getResource();
                issues.addAll(validator.validate(resource));
            } catch (Exception e) {
                exceptions.add(e);
            }
        }

        assertEquals(exceptions.size(), 0);
        assertFalse(issues.stream().anyMatch(issue -> IssueSeverity.ERROR.equals(issue.getSeverity())));
    }
}
