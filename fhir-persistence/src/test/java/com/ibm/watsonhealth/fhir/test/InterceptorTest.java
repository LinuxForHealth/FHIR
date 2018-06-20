/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.IssueTypeList;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.test.FHIRModelTestBase;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.watsonhealth.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;

/**
 * This class tests the persistence interceptor feature. The MyInterceptor class is our test interceptor implementation
 * and is registered with the interceptor framework.
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
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeCreateEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeCreateCount());
    }

    @Test
    public void testAfterCreate() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
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
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeReadEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeReadCount());
    }
    
    @Test
    public void testAfterRead() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        mgr.fireAfterReadEvent(event);
        
        assertFalse(event.isStandardResourceType());
        assertEquals(4, MyInterceptor.getAfterReadCount());
    }
    
    @Test
    public void testBeforeVread() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeVreadEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeVreadCount());
    }
    
    @Test
    public void testAfterVread() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterVreadEvent(event);
        mgr.fireAfterVreadEvent(event);
        
        assertFalse(event.isStandardResourceType());
        assertEquals(2, MyInterceptor.getAfterVreadCount());
    }
    
    @Test
    public void testBeforeHistory() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeHistoryEvent(event);
        
        assertTrue(event.isStandardResourceType());
        assertEquals(1, MyInterceptor.getBeforeHistoryCount());
    }
    
    @Test
    public void testAfterHistory() throws Exception {
    	Map<String, Object> properties = new HashMap<String, Object>();
    	properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient1");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterHistoryEvent(event);
        mgr.fireAfterHistoryEvent(event);
        mgr.fireAfterHistoryEvent(event);
        
        assertFalse(event.isStandardResourceType());
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
        
        assertFalse(event.isStandardResourceType());
        assertEquals(3, MyInterceptor.getAfterSearchCount());
    }

    @Test
    public void testBeforeUpdate() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireBeforeUpdateEvent(event);
        mgr.fireBeforeUpdateEvent(event);

        assertEquals(2, MyInterceptor.getBeforeUpdateCount());
    }

    @Test
    public void testAfterUpdate() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
        
        mgr.fireAfterUpdateEvent(event);

        assertEquals(1, MyInterceptor.getAfterUpdateCount());
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeCreate"})
    public void testBeforeCreateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);

        try {
            mgr.fireBeforeCreateEvent(event);
        } catch (FHIRPersistenceInterceptorException e) {
            assertEquals(IssueTypeList.FORBIDDEN, e.getIssues().get(0).getCode().getValue());
            throw e;
        }
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterCreate"})
    public void testAfterCreateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);

        try {
            mgr.fireAfterCreateEvent(event);
        } catch (FHIRPersistenceInterceptorException e) {
            assertEquals(IssueTypeList.CODE_INVALID, e.getIssues().get(0).getCode().getValue());
            throw e;
        }
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeUpdate"})
    public void testBeforeUpdateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);

        try {
            mgr.fireBeforeUpdateEvent(event);
        } catch (FHIRPersistenceInterceptorException e) {
            assertEquals(IssueTypeList.CONFLICT, e.getIssues().get(0).getCode().getValue());
            throw e;
        }
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterUpdate"})
    public void testAfterUpdateException() throws Exception {
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, null);

        try {
            mgr.fireAfterUpdateEvent(event);
        } catch (FHIRPersistenceInterceptorException e) {
            assertEquals(IssueTypeList.EXPIRED, e.getIssues().get(0).getCode().getValue());
            throw e;
        }
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeRead"})
    public void testBeforeReadException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
        
        mgr.fireBeforeReadEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterRead"})
    public void testAfterReadException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, properties);
        
        mgr.fireAfterReadEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeVread"})
    public void testBeforeVreadException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
        
        mgr.fireBeforeVreadEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterVread"})
    public void testAfterVreadException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "1");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, properties);
        
        mgr.fireAfterVreadEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeHistory"})
    public void testBeforeHistoryException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
        
        mgr.fireBeforeHistoryEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterHistory"})
    public void testAfterHistoryException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "123");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, properties);
        
        mgr.fireAfterHistoryEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testBeforeSearch"})
    public void testBeforeSearchException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
        
        mgr.fireBeforeSearchEvent(event);
    }

    @Test(expectedExceptions = {FHIRPersistenceInterceptorException.class}, dependsOnMethods={"testAfterSearch"})
    public void testAfterSearchException() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Exception");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(badPatient, properties);
        
        mgr.fireAfterSearchEvent(event);
    }
}
