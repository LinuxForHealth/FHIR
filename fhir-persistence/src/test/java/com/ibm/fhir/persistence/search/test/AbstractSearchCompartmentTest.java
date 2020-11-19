/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.AssertJUnit.assertEquals;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Unit tests for compartment-based searches
 * @see https://hl7.org/fhir/r4/search.html
 * GET [base]/Patient/[id]/[type]?parameter(s)
 */
public abstract class AbstractSearchCompartmentTest extends AbstractPLSearchTest {

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicCompartment.json");
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("compartment");

        // Need to set reference before storing the resource. The server-url
        // is now used to determine if an absolute reference is local (can be served
        // from this FHIR server).
        createReference();
    }

    @BeforeClass
    public void createReference() throws FHIRException {
        String originalRequestUri = "https://example.com/Patient/123";
        FHIRRequestContext context = FHIRRequestContext.get();
        context.setOriginalRequestUri(originalRequestUri);
    }

    @Test
    public void testSearchCompartment() throws Exception {
        // The saved Basic resource is a member of the compartment Patient/123
        // Check that we can find the resource with additional query parameters
        // Note that "Reference-relative just happens to be another searchable
        // parameter in the BasicCompartment.json resource
        List<Resource> results = runQueryTest("Patient", "123",
            Basic.class, "Reference-relative", "Patient/123");
        assertEquals(1, results.size());
    }
}