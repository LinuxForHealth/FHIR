/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.client.test.mains;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Claim;
import org.linuxforhealth.fhir.provider.FHIRProvider;

public class ReadClaimTestMain {
    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newBuilder()
                .register(new FHIRProvider(RuntimeType.CLIENT))
                .build();
        WebTarget target = client.target("http://fhirtest.uhn.ca/baseDstu2");
        Response response = target.path("Claim/14105").request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        Claim claim = response.readEntity(Claim.class);
        FHIRGenerator.generator( Format.JSON, false).generate(claim, System.out);
        System.out.println("");
        FHIRGenerator.generator( Format.XML, false).generate(claim, System.out);
        
    }
}
