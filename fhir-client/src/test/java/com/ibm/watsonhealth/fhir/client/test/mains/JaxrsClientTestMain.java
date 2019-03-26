/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test.mains;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.codeableConcept;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.contactPoint;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.date;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.humanName;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.observationStatus;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.quantity;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.reference;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.ObservationStatusList;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.provider.FHIRProvider;

public class JaxrsClientTestMain {
    private static final ObjectFactory f = new ObjectFactory();
    
    public static void main(String[] args) throws Exception {
        Patient patient = buildPatient();
        System.out.println("\nJSON:");
        FHIRUtil.write(patient, Format.JSON, System.out);
        System.out.println("\nXML:");
        FHIRUtil.write(patient, Format.XML, System.out);

        Client client = ClientBuilder.newBuilder()
                .register(new FHIRProvider())
                .build();
        
        WebTarget target = client.target("http://localhost:9080/fhir-server/api/v1");
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_XML_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        
        if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
            System.out.println("");
            System.out.println(response.getStatus());
            System.out.println(response.getStatusInfo().getReasonPhrase());
            String location = response.getLocation().toString();
            System.out.println("location: " + location);
            
            String id = location.substring(location.lastIndexOf("/") + 1);
            response = target.path("Patient/" + id).request(MediaType.APPLICATION_JSON_FHIR).get();
            patient = response.readEntity(Patient.class);
            System.out.println("\nJSON:");
            FHIRUtil.write(patient, Format.JSON, System.out);
            System.out.println("\nXML:");
            FHIRUtil.write(patient, Format.XML, System.out);
            
            Observation observation = buildObservation(id);
            System.out.println("\nJSON:");
            FHIRUtil.write(observation, Format.JSON, System.out);
            System.out.println("\nXML:");
            FHIRUtil.write(observation, Format.XML, System.out);
            
            Entity<Observation> observationEntity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
            response = target.path("Observation").request().post(observationEntity, Response.class);
            
            if (Response.Status.CREATED.getStatusCode() == response.getStatus()) {
                System.out.println("");
                System.out.println(response.getStatus());
                System.out.println(response.getStatusInfo().getReasonPhrase());
                location = response.getLocation().toString();
                System.out.println("location: " + location);
                
                response = target.path("Observation").queryParam("subject", "Patient/" + id).request(MediaType.APPLICATION_JSON_FHIR).get();
                Bundle bundle = response.readEntity(Bundle.class);
                System.out.println("\nJSON:");
                FHIRUtil.write(bundle, Format.JSON, System.out);
                System.out.println("\nXML:");
                FHIRUtil.write(bundle, Format.XML, System.out);
            } else {
                System.out.println("");
                System.out.println(response.getStatus());
                System.out.println(response.getStatusInfo().getReasonPhrase());
                System.out.println(response.readEntity(String.class));
            }
        } else {
            System.out.println("");
            System.out.println(response.getStatus());
            System.out.println(response.getStatusInfo().getReasonPhrase());
            System.out.println(response.readEntity(String.class));
        }
    }
    
    public static Patient buildPatient() {
        Patient patient = f.createPatient()
            .withName(
                humanName("John", "Doe")
            )
            .withBirthDate(date("1950-08-15"))
            .withTelecom(
                contactPoint(ContactPointSystemList.PHONE, "555-1234", ContactPointUseList.HOME)
            )
            .withExtension(f.createExtension()
                .withUrl("http://ibm.com/watsonhealth/fhir/extension/Patient/favorite-color")
                .withValueString(string("blue"))
            );
        return patient;
    }
    
    public static Observation buildObservation(String patientId) {
        Observation observation = f.createObservation()
            .withStatus(
                observationStatus(ObservationStatusList.FINAL)
            )
            .withCategory(
                codeableConcept("http://hl7.org/fhir/observation-category", "vital-signs", "Vital Signs")
            )
            .withCode(
                codeableConcept("http://loinc.org", "55284-4", "Blood pressure systolic & diastolic")
            )
            .withSubject(
                reference("Patient/" + patientId)
            )
            .withComponent(
                f.createObservationComponent().withCode(
                    codeableConcept("http://loinc.org", "8459-0", "Systolic")
                )
                .withValueQuantity(
                    quantity(124.9, "mmHg")
                )
            )
            .withComponent(
                f.createObservationComponent().withCode(
                    codeableConcept("http://loinc.org", "8453-3", "Diastolic")
                )
                .withValueQuantity(
                    quantity(93.7, "mmHg")
                )
            );
        return observation;
    } 
}
