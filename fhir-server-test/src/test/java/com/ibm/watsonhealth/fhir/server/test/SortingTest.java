/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Ordering;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Patient;

public class SortingTest extends FHIRServerTestBase {
	class Sorting{
		
	}
	
	private String patientId;
	
	@BeforeMethod
    public void shouldSkipTests() throws Exception {
        if (!this.isSortingSupported()) {
        	throw new SkipException("Sorting feature not supported; skipping tests");
        }
    }
    
    @Test
    public void testCreatePatient1() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        
        patient.setGender(objFactory.createCode().withValue("male"));
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
    }
    
    @Test
    public void testCreatePatient2() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_DavidOrtiz.json");
        
        patient.setGender(objFactory.createCode().withValue("male"));
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
    }
    
    @Test
    public void testCreatePatient3() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "patient-example-a.json");
        
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
    }
    
    @Test
    public void testCreatePatient4() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "patient-example-c.json");
        
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
    }
    
    @Test
    public void testCreatePatient5() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "patient-example-a1.json");
        
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Patient responsePatient = response.readEntity(Patient.class);
        assertResourceEquals(patient, responsePatient);
    }
    
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1" })
    public void testCreateObservation1() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation = buildObservation(patientId, "Observation1.json");
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation = response.readEntity(Observation.class);

        // use it for search
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }
    
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1" })
    public void testCreateObservation2() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation = buildObservation(patientId, "Observation2.json");
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation = response.readEntity(Observation.class);

        // use it for search
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }
    
    
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1" })
    public void testCreateObservation3() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation = buildObservation(patientId, "Observation3.json");
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation = response.readEntity(Observation.class);

        // use it for search
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }
    
    @Test(groups = { "server-search" })
    public void testCreateObservation5() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation = buildObservation("1", "Observation5.json");
        Entity<Observation> entity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Observation").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        String observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation responseObservation = response.readEntity(Observation.class);

        // use it for search
        observationId = responseObservation.getId().getValue();
        assertResourceEquals(observation, responseObservation);
    }

    
    //Patient?gender=male&_sort:asc=family
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortAscending() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("gender", "male").queryParam("_count", "50").queryParam("_sort:asc", "family").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getName()!=null&&bundle.getEntry().get(i).getResource().getPatient().getName().size()>0){
        		list.add(bundle.getEntry().get(i).getResource().getPatient().getName().get(0).getFamily().get(0).getValue());
        	}
    	}
        assertTrue(Ordering.natural().isOrdered(list));
    }
    
    
    
    //Patient?gender=male&_sort:desc=family
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortDescending() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("gender", "male").queryParam("_count", "50").queryParam("_sort:desc", "family").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getName()!=null&&bundle.getEntry().get(i).getResource().getPatient().getName().size()>0){
        		list.add(bundle.getEntry().get(i).getResource().getPatient().getName().get(0).getFamily().get(0).getValue());
        	}
    	}
        assertTrue(Ordering.natural().reverse().isOrdered(list));
    }
    
    //Patient?gender=male&_sort=telecom
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortTelecom() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("gender", "male").queryParam("_count", "50").queryParam("_sort", "telecom").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getTelecom()!=null&&bundle.getEntry().get(i).getResource().getPatient().getTelecom().size()>0){
        		list.add(bundle.getEntry().get(i).getResource().getPatient().getTelecom().get(0).getValue().getValue());
        	}
        }
        assertTrue(Ordering.natural().isOrdered(list));
    }
    
    //Patient?gender=male&_sort:desc=birthDate
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortBirthDate() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("gender", "male").queryParam("_count", "50").queryParam("_sort:desc", "birthdate").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getBirthDate()!=null){
        		list.add(bundle.getEntry().get(i).getResource().getPatient().getBirthDate().getValue());
        	}
    	}
        assertTrue(Ordering.natural().reverse().isOrdered(list));
    }
    
    //Patient?gender=male&_sort:desc=family&_sort:asc=birthdate
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortTwoParameters() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("_count", "50").queryParam("_sort:desc", "family").queryParam("_sort:asc", "birthdate").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        String previousBirthDate=null, previousFamily=null;
        //Check birthDate order first, and then check family name order.
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getName()!=null&&bundle.getEntry().get(i).getResource().getPatient().getName().size()>0){
        		String currentFamily = bundle.getEntry().get(i).getResource().getPatient().getName().get(0).getFamily().get(0).getValue();
        		String currentBirthDate=null;
        		if(bundle.getEntry().get(i).getResource().getPatient().getBirthDate()!=null){
        			currentBirthDate = bundle.getEntry().get(i).getResource().getPatient().getBirthDate().getValue();
        		}
        		else{
        			currentBirthDate = null;
        		}
        		if(previousFamily != null && previousFamily.equals(currentFamily)){
        			assertTrue(previousBirthDate == null || previousBirthDate.compareTo(currentBirthDate) <= 0);
        		}
        		list.add(currentFamily);
        		
        		previousFamily = currentFamily;
        		previousBirthDate = currentBirthDate;
        	}
    	}
        assertTrue(Ordering.natural().reverse().isOrdered(list));
    }
    
  //Patient?gender=male&_sort:desc=family&_sort:desc=birthdate
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testSortTwoParametersDescending() {
        WebTarget target = getWebTarget();
        Response response = target.path("Patient").queryParam("_count", "50").queryParam("_sort:desc", "family").queryParam("_sort:desc", "birthdate").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<String> list = new ArrayList<String>();
        String previousBirthDate=null, previousFamily=null;
        //Check birthDate order first, and then check family name order.
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getPatient().getName()!=null&&bundle.getEntry().get(i).getResource().getPatient().getName().size()>0){
        		String currentFamily = bundle.getEntry().get(i).getResource().getPatient().getName().get(0).getFamily().get(0).getValue();
        		String currentBirthDate=null;
        		if(bundle.getEntry().get(i).getResource().getPatient().getBirthDate()!=null){
        			currentBirthDate = bundle.getEntry().get(i).getResource().getPatient().getBirthDate().getValue();
        		}
        		else{
        			currentBirthDate = null;
        		}
        		if(previousFamily != null && previousFamily.equals(currentFamily)){
        			assertTrue(previousBirthDate == null || previousBirthDate.compareTo(currentBirthDate) >= 0);
        		}
        		list.add(currentFamily);
        		
        		previousFamily = currentFamily;
        		previousBirthDate = currentBirthDate;
        	}
    	}
        assertTrue(Ordering.natural().reverse().isOrdered(list));
    }
    
    //Observation?status=final&_sort:asc=value-quantity
    @Test(groups = { "server-search" }, dependsOnMethods = { "testCreateObservation1", "testCreateObservation2", "testCreateObservation3", "testCreateObservation5" })
    public void testSortValueQuantityAscending() {
        WebTarget target = getWebTarget();
        Response response = target.path("Observation").queryParam("status", "final").queryParam("_count", "50").queryParam("_sort:asc", "component-value-quantity").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.readEntity(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() > 1);
        List<BigDecimal> list = new ArrayList<BigDecimal>();
        for(int i=0;i<bundle.getEntry().size();i++){
        	if(bundle.getEntry().get(i).getResource().getObservation().getComponent().size()>0){
        		list.add(bundle.getEntry().get(i).getResource().getObservation().getComponent().get(1).getValueQuantity().getValue().getValue());
        	}
        }
        assertTrue(Ordering.natural().isOrdered(list));
    }
    
    
}
