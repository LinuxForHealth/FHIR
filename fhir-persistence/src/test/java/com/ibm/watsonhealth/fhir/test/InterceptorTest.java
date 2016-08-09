/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.test;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.HashMap;
import java.util.Map;

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
    public void testBeforeRead() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeReadEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeReadCount());
    }
    
    @Test
    public void testAfterRead() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        
        assertTrue(event.isStandardResourceType() == false);
        assertEquals(4, MyInterceptor.getAfterReadCount());
    }
    
    @Test
    public void testBeforeVread() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeVreadEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeVreadCount());
    }
    
    @Test
    public void testAfterVread() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterVreadEvent(event);
        mgr.fireAfterVreadEvent(event);
        
        assertTrue(event.isStandardResourceType() == false);
        assertEquals(2, MyInterceptor.getAfterVreadCount());
    }
    
    @Test
    public void testBeforeHistory() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeHistoryEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeHistoryCount());
    }
    
    @Test
    public void testAfterHistory() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterHistoryEvent(event);
        mgr.fireAfterHistoryEvent(event);
        mgr.fireAfterHistoryEvent(event);
        
        assertTrue(event.isStandardResourceType() == false);
        assertEquals(3, MyInterceptor.getAfterHistoryCount());
    }
    
    @Test
    public void testBeforeSearch() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeSearchEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeSearchCount());
    }
    
    @Test
    public void testAfterSearch() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterSearchEvent(event);
        mgr.fireAfterSearchEvent(event);
        mgr.fireAfterSearchEvent(event);
        
        assertTrue(event.isStandardResourceType() == false);
        assertEquals(3, MyInterceptor.getAfterSearchCount());
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
