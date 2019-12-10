/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.test.TestUtil;

/**
 * <a href="https://www.hl7.org/fhir/r4/location.html#positional">FHIR Specification: Positional Searching for Location Resource</a>
 */
public abstract class AbstractSearchNearTest extends AbstractPLSearchTest {

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/spec/location-example.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    public void testSearchPositionSearch() throws Exception {
        assertSearchReturnsSavedResource("near", "-83.694810|42.256500|11.20|km");
        assertSearchDoesntReturnSavedResource("near", "-83.694810|42.256500|11.20|km");
    }
}