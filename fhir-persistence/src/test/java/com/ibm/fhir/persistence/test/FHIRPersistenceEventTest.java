/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.FHIRModelTestBase;
import com.ibm.fhir.persistence.context.FHIRReplicationContext;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;

/**
 * Tests associated with the FHIRPersistenceContextImpl class.
 */
public class FHIRPersistenceEventTest extends FHIRModelTestBase {
    @Test
    public void test1() {
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent();
        assertNull(pe.getFhirResource());
        assertNull(pe.getFhirResourceType());
        assertNull(pe.getFhirResourceId());
        assertNull(pe.getFhirVersionId());
        assertFalse(pe.isStandardResourceType());
        assertNull(pe.getHttpHeaders());
        assertNull(pe.getRequestProperties());
        assertNull(pe.getSecurityContext());
        assertNull(pe.getUriInfo());
        assertNull(pe.getTransactionCorrelationId());
        assertNull(pe.getRequestCorrelationId());
        assertNull(pe.getReplicationContext());
        assertNull(pe.getPersistenceImpl());
    }
    
    @Test
    public void test2() throws Exception {
        Patient patient = readExampleResource("json/ibm/minimal/Patient-1.json");
        Map<String, Object> properties = new HashMap<>();
        
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "id1");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "v1");
        
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent(patient, properties);
        assertNotNull(pe.getFhirResource());
        assertEquals("Patient", pe.getFhirResourceType());
        assertEquals("id1", pe.getFhirResourceId());
        assertEquals("v1", pe.getFhirVersionId());
        assertNull(pe.getReplicationContext());
    }
    
    @Test
    public void test3() throws Exception {
        Patient patient = readExampleResource("json/ibm/minimal/Patient-1.json");
        Map<String, Object> properties = new HashMap<>();
        
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "id1");
        properties.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, "v1");
        
        java.time.Instant lastUpdated = Instant.now();
        FHIRReplicationContext expectedRC = new FHIRReplicationContext("999999", lastUpdated);
        properties.put(FHIRPersistenceEvent.PROPNAME_REPLICATION_CONTEXT, expectedRC);
        
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent(patient, properties);
        assertNotNull(pe.getFhirResource());
        assertEquals("Patient", pe.getFhirResourceType());
        assertEquals("id1", pe.getFhirResourceId());
        assertEquals("v1", pe.getFhirVersionId());
        FHIRReplicationContext actualRC = pe.getReplicationContext();
        assertNotNull(actualRC);
        assertEquals(expectedRC.getVersionId(), actualRC.getVersionId());
        assertEquals(expectedRC.getLastUpdated(), actualRC.getLastUpdated());
    }
    
    @Test
    public void testGetHeaderString() throws Exception {
        Patient patient = readExampleResource("json/ibm/minimal/Patient-1.json");
        Map<String, Object> properties = new HashMap<>();
        
        Map<String, String> reqProps = new HashMap<String, String>();
        reqProps.put("X-Custom-Header", "value1");
        
        properties.put(FHIRPersistenceEvent.PROPNAME_REQUEST_PROPERTIES, reqProps);
        
        FHIRPersistenceEvent pe = new FHIRPersistenceEvent(patient, properties);
        
        assertEquals("value1", pe.getHeaderString("X-Custom-Header"));
        assertNull(pe.getHeaderString("X-Fake-Header"));
    }
}
