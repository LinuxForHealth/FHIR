/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.valuetypes;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.test.BaseSearchTest;

/**
 * Multi-Tenant Value Types Test
 * 
 * @author pbastide
 *
 */
public class MultiTenantValueTypesTest extends BaseSearchTest {

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @Test
    public void testDefault() throws Exception {
        runTest("default", Patient.class, "favorite-color", com.ibm.fhir.model.type.String.class);

        runTest("default", Patient.class, "favorite-number", com.ibm.fhir.model.type.Integer.class);

        runTest("default", Patient.class, "favorite-code", com.ibm.fhir.model.type.CodeableConcept.class);

        runTest("default", Patient.class, "favorite-uri", com.ibm.fhir.model.type.Uri.class);

        runTest("default", Patient.class, "favorite-quantity", com.ibm.fhir.model.type.SampledData.class, com.ibm.fhir.model.type.Quantity.class);

        runTest("default", Patient.class, "favorite-date", com.ibm.fhir.model.type.DateTime.class);

        runTestNotContain("default", Patient.class, "favorite-date", com.ibm.fhir.model.type.Uri.class);
    }
    
    @Test
    public void testTenant1() throws Exception {
        runTest("tenant1", Basic.class, "measurement-type", com.ibm.fhir.model.type.String.class);

        runTestNotContain("tenant1", Basic.class, "measurement-type", com.ibm.fhir.model.type.Uri.class);
        
        runTestNotContain("tenant1", Basic.class, "measurement-type-junk", com.ibm.fhir.model.type.Uri.class);
        
        runTestNotContain("tenant1", Observation.class, "value-range", com.ibm.fhir.model.type.String.class);
        
        runTest("tenant1", Observation.class, "value-range", com.ibm.fhir.model.type.Integer.class);

    }    
    
    @Test
    public void testTenant2() throws Exception {
        runTestNotContain("tenant2", Patient.class, "favorite-nfl-team", com.ibm.fhir.model.type.String.class);
        runTestNotContain("tenant2", Patient.class, "favorite-mlb-team", com.ibm.fhir.model.type.String.class);

    } 

    /*
     * runs the Positive test.
     */
    private void runTest(String tenantId, Class<?> cls, String code, Class<?>... classesThatMustExist) throws FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId));

        Set<Class<?>> clzs = ValueTypesFactory.getValueTypesProcessor().getValueTypes(cls, code);
        assertNotNull(clzs);

        assertFalse(clzs.isEmpty());

        // Iterate over the classes that must exist
        boolean done = false;
        for (Class<?> tmpCls : classesThatMustExist) {
            assertTrue(clzs.contains(tmpCls));
            done = true;
        }
        assertTrue(done);

    }

    /*
     * runs the Negative test.
     */
    private void runTestNotContain(String tenantId, Class<?> cls, String code, Class<?>... classesThatMustNotExist) throws FHIRException {
        FHIRRequestContext.set(new FHIRRequestContext(tenantId));

        Set<Class<?>> clzs = ValueTypesFactory.getValueTypesProcessor().getValueTypes(cls, code);
        assertNotNull(clzs);

        // Iterate over the classes that must exist
        boolean done = false;
        for (Class<?> tmpCls : classesThatMustNotExist) {
            assertFalse(clzs.contains(tmpCls));
            done = true;
        }
        assertTrue(done);

    }

}
