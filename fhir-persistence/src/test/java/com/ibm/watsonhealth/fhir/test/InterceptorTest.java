/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.test.FHIRModelTestBase;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;

/**
 * This class tests the persistence interceptor feature.
 * The MyInterceptor class is our test interceptor implementation and is registered with
 * the interceptor framework.
 */
public class InterceptorTest extends FHIRModelTestBase {
    protected static FHIRPersistenceInterceptorMgr mgr = null;
    protected static Patient patient = null;
    protected static Patient badPatient = null;

    public InterceptorTest() {
    }
    
    @BeforeClass
    public void setUp() throws Exception {
        mgr = FHIRPersistenceInterceptorMgr.getInstance();
        assertNotNull(mgr);
        
        patient = readResource(Patient.class, "Patient_DavidOrtiz.json");
        badPatient = readResource(Patient.class, "Patient_JohnException.json");
    }
    
    @Test
    public void testBeforeCreate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, null);
        
        mgr.fireBeforeCreateEvent(event);
        
        assertEquals(1, MyInterceptor.getBeforeCreateCount());
    }
    
    @Test
    public void testAfterCreate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, null);
        
        mgr.fireAfterCreateEvent(event);
        mgr.fireAfterCreateEvent(event);
        mgr.fireAfterCreateEvent(event);
        mgr.fireAfterCreateEvent(event);
        
        assertEquals(4, MyInterceptor.getAfterCreateCount());
    }

    @Test
    public void testBeforeUpdate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, null);
        
        mgr.fireBeforeUpdateEvent(event);
        mgr.fireBeforeUpdateEvent(event);
        
        assertEquals(2, MyInterceptor.getBeforeUpdateCount());
    }

    @Test
    public void testAfterUpdate() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, null);
        
        mgr.fireAfterUpdateEvent(event);
        
        assertEquals(1, MyInterceptor.getAfterUpdateCount());
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class})
    public void testBeforeCreateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);
        
        mgr.fireBeforeCreateEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class})
    public void testAfterCreateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);
        
        mgr.fireAfterCreateEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class})
    public void testBeforeUpdateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);
        
        mgr.fireBeforeUpdateEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class})
    public void testAfterUpdateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);
        
        mgr.fireAfterUpdateEvent(event);
    }
}
