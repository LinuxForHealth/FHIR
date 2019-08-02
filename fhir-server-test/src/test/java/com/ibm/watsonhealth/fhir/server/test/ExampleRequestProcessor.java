/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.spec.test.IExampleProcessor;
import com.ibm.watsonhealth.fhir.model.spec.test.ResourceComparatorVisitor;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.persistence.util.ResourceFingerprintVisitor;
import com.ibm.watsonhealth.fhir.persistence.util.SaltHash;

/**
 * @author rarnold
 *
 */
public class ExampleRequestProcessor implements IExampleProcessor {
    private final FHIRServerTestBase base;
    
    public ExampleRequestProcessor(FHIRServerTestBase base) {
        this.base = base;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.model.spec.test.IExampleProcessor#process(java.lang.String, com.ibm.watsonhealth.fhir.model.resource.Resource)
     */
    @Override
    public void process(String jsonFile, Resource resource) throws Exception {
        
        // clear the id value from the example so we can assign our own
        resource = resource.toBuilder().id(null).build();
        
        // Compute a fingerprint which can be used to check the consistency of the
        // resource we read back from FHIR
        ResourceFingerprintVisitor v = new ResourceFingerprintVisitor();
        resource.accept(resource.getClass().getSimpleName(), v);

        WebTarget target = base.getWebTarget();

        String resourceTypeName = FHIRUtil.getResourceTypeName(resource);

        // Build a new Patient and then call the 'create' API.
        Entity<Resource> entity = Entity.entity(resource, MediaType.APPLICATION_FHIR_JSON);
        Response response = target.path(resourceTypeName).request().post(entity, Response.class);
        base.assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the logical id value.
        String logicalId = base.getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path(resourceTypeName + "/" + logicalId).request(MediaType.APPLICATION_FHIR_JSON).get();
        base.assertResponse(response, Response.Status.OK.getStatusCode());

        // Now...do we need some reflection here?
        Resource responseResource = response.readEntity(resource.getClass());

        // Now we can check what we sent equals what we got back (minus the meta changes)
        // making sure to seed the visitor with the same salt we used above
        SaltHash baseline = v.getSaltAndHash();
        ResourceFingerprintVisitor v2 = new ResourceFingerprintVisitor(baseline);
        responseResource.accept(responseResource.getClass().getSimpleName(), v2);

        SaltHash responseHash = v2.getSaltAndHash();
        
        if (!responseHash.equals(baseline)) {
            // Use the ResourceComparatorVisitor to provide some detail about what's different
            ResourceComparatorVisitor originals = new ResourceComparatorVisitor();
            resource.accept(resource.getClass().getSimpleName(), originals);
            
            ResourceComparatorVisitor others = new ResourceComparatorVisitor();
            responseResource.accept(responseResource.getClass().getSimpleName(), others);
            
            // Perform a bi-directional comparison of values in the maps
            ResourceComparatorVisitor.compare(originals.getValues(), others.getValues());

            // throw the error so it is handled by the test framework
            assertEquals(responseHash, baseline);
        }
    }
}
