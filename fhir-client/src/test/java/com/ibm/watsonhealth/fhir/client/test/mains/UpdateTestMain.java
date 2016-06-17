/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test.mains;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.quantity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;
import com.ibm.watsonhealth.fhir.provider.FHIRProvider;

public class UpdateTestMain {
	public static void main(String[] args) throws Exception {
		Client client = ClientBuilder.newBuilder()
		        .register(new FHIRProvider())
		        .build();
		
		WebTarget target = client.target("http://localhost:9080/fhir-server/api/v1");
		Response response = target.path("Observation/14").request(MediaType.APPLICATION_JSON_FHIR).get();
		
		Observation observation = response.readEntity(Observation.class);
		FHIRUtil.write(observation, Format.JSON, System.out);
		
		observation.getComponent().get(0).setValueQuantity(quantity(120.0, "mmHg"));
		
		Entity<Observation> observationEntity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
		response = target.path("Observation/14").request().put(observationEntity);
		System.out.println(response.getStatusInfo().getReasonPhrase());
		System.out.println("location: " + response.getLocation());
	}
}
