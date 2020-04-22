/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.sample;

import java.util.Properties;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRClientFactory;
import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRParameters.ValuePrefix;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.type.code.BundleType;

/**
 * This class is sample code that demonstrates the use of the FHIR Server's Client API.
 * This sample is not necessarily expected to run correctly as certain details are omitted so that
 * we can focus on the highlights of the FHIRClient interface.
 */
public class FHIRClientSample {
    public final static int OK = 200;
    public final static int CREATED = 201;

    public static void main(String[] args) throws Exception {

        // Create properties to be used to configure the client.
        Properties clientProperties = new Properties();
        clientProperties.setProperty(FHIRClient.PROPNAME_BASE_URL, "https://localhost:9443/fhir-server/api/v4");

        // Retrieve an instance of the FHIRClient interface.
        FHIRClient client = FHIRClientFactory.getClient(clientProperties);

        // Create and initialize patient.
        Patient patient = Patient.builder().id("test").build();
        // patient.set...

        // 1) Persist the patient and then display its location URI (e.g. "Patient/123/_history/1").
        FHIRResponse response = client.create(patient);
        if (response.getStatus() == CREATED) {
            System.out.println("Patient resource was persisted, location = " + response.getLocation());
        } else {
            System.out.println("Error persisting patient, status code = " + response.getStatus());
        }

        // 2) Retrieve an Observation.
        response = client.read("Observation", "123");
        if (response.getStatus() == OK) {
            System.out.println("Retrieved an Observation!");
            Observation observation = response.getResource(Observation.class);
            // Do something with the Observation...
        } else {
            System.out.println("Error reading Observation!");
            OperationOutcome operationOutcome = response.getResource(OperationOutcome.class);
            // display operationOutcome message, etc.
        }

        // 3) Retrieve a specific version of a MedicationAdministration.
        response = client.vread("MedicationAdministration", "12345", "2");
        // Check status code, retrieve resource, etc.

        // 4) Retrieve the most recent 5 versions of a Patient.
        FHIRParameters parameters = new FHIRParameters().count(5);
        response = client.history("Patient", "123", parameters);
        // Check status code, retrieve response bundle, etc.

        // 5) Search for patients born on 1/1/1970.
        parameters = new FHIRParameters().searchParam("birthdate", ValuePrefix.EQ, "1970-01-01");
        response = client.search("Patient", parameters);

        // 6) Validate a Patient resource.
        response = client.validate(patient);
        // Check status code, retrieve OperationOutcome response resource, etc.

        // 7) Invoke a batch request.
        Bundle bundle = Bundle.builder().type(BundleType.BATCH).build();
        // Initialize bundle by adding individual operations (read, vread, create, update, etc.)

        response = client.batch(bundle);
        // Check status code, retrieve Bundle response resource, etc.
    }
}
