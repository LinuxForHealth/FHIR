/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.client.test.mains;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.watson.health.fhir.core.FHIRMediaType;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.FHIRGenerator;
import com.ibm.watson.health.fhir.model.resource.Patient;
import com.ibm.watson.health.fhir.provider.FHIRProvider;

public class Main {
    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newBuilder()
                .register(new FHIRProvider())
                .build();
        WebTarget target = client.target("http://fhirtest.uhn.ca/baseDstu2");
        Response response = target.path("Patient/5149").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        Patient patient = response.readEntity(Patient.class);
        
        FHIRGenerator.generator( Format.JSON, false).generate(patient, System.out);
        System.out.println("");
        FHIRGenerator.generator( Format.XML, false).generate(patient, System.out);
    }
}
