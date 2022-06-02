/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.r4b.test;

import java.util.Collection;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.util.FHIRRegistryUtil;

public class FHIRRegistryTest {
    @Test
    public void testRegistry() {
        StructureDefinition structureDefinition = FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/StructureDefinition/Account", StructureDefinition.class);
        Assert.assertNotNull(structureDefinition);
    }

    @Test
    public void testGetResourcesByResourceType() {
        Collection<SearchParameter> searchParameters = FHIRRegistry.getInstance().getResources(SearchParameter.class);
        Assert.assertEquals(searchParameters.size(), 1414);
    }

    @Test
    public void testGetProfilesByType() {
        Collection<Canonical> observationProfiles = FHIRRegistry.getInstance().getProfiles("Observation");
        Assert.assertEquals(observationProfiles.size(), 17);
    }

    @Test
    public void testGetSearchParametersByType() {
        Collection<SearchParameter> tokenSearchParameters = FHIRRegistry.getInstance().getSearchParameters("token");
        Assert.assertEquals(tokenSearchParameters.size(), 566);
    }

    @Test
    public void testLoadAllResources() {
        // FHIRRegistryUtil has a private set of all definitional resources,
        // so an alternative would be to mark that public and iterate through that instead
        for (Class<? extends Resource> resourceType : ModelSupport.getResourceTypes()) {
            if (FHIRRegistryUtil.isDefinitionalResourceType(resourceType)) {
                FHIRRegistry.getInstance().getResources(resourceType);
            }
        }
    }
}
