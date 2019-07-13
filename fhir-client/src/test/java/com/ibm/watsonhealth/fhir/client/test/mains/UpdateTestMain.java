/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.test.mains;

import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Quantity;

import static com.ibm.watsonhealth.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.Observation;
import com.ibm.watsonhealth.fhir.model.resource.Observation.Component;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.format.Format;
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
        
                
        List <Component> newCompList = new ArrayList<Component>();
        int i = 0;
        for (Component component: observation.getComponent()) {
            // change first component only
            if ( i == 0 ) {
                component = component.toBuilder()
                        .value(Quantity.builder().value(Decimal.of(120)).unit(string("mmHg")).build()).build(); 
            } 
            newCompList.add(component);
            i++;
        }
        
        observation = observation.toBuilder().component(newCompList).build();
        
        Entity<Observation> observationEntity = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Observation/14").request().put(observationEntity);
        System.out.println(response.getStatusInfo().getReasonPhrase());
        System.out.println("location: " + response.getLocation());
    }
}
