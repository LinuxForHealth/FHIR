/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test.v110;

import static com.ibm.fhir.path.util.FHIRPathUtil.compile;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.ig.carin.bb.C4BB110ResourceProvider;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.spi.FHIRRegistryResourceProvider;
import com.ibm.fhir.validation.FHIRValidator;

public class ProviderTest {

    @Test
    public void testBBResourceProvider() {
        FHIRRegistryResourceProvider provider = new C4BB110ResourceProvider();
        Collection<FHIRRegistryResource> registryResources = provider.getRegistryResources();
        assertNotNull(registryResources);
        assertTrue(!registryResources.isEmpty());
        for (FHIRRegistryResource fhirRegistryResource : registryResources) {
            assertNotNull(fhirRegistryResource.getResource());
        }
        assertEquals(provider.getRegistryResources().size(), 97);
    }

    @Test
    public static void testConstraintGenerator() throws Exception {
        FHIRRegistryResourceProvider provider = new C4BB110ResourceProvider();
        for (FHIRRegistryResource registryResource : provider.getRegistryResources()) {
            if (StructureDefinition.class.equals(registryResource.getResourceType())) {
                String url = registryResource.getUrl();
                System.out.println(url);
                Class<?> type =
                        ModelSupport.isResourceType(registryResource.getType()) ? ModelSupport.getResourceType(registryResource.getType()) : Extension.class;
                for (Constraint constraint : ProfileSupport.getConstraints(url, type)) {
                    System.out.println("    " + constraint);
                    if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                        compile(constraint.location());
                    }
                    compile(constraint.expression());
                }
            }
        }
    }

    @Test
    public void testRegistry() {
        StructureDefinition definition =
                FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/us/carin-bb/StructureDefinition/C4BB-Coverage", StructureDefinition.class);
        Assert.assertNotNull(definition);
    }

    @Test
    public void testValidateResources() throws Exception {
        FHIRRegistryResourceProvider provider = new C4BB110ResourceProvider();

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