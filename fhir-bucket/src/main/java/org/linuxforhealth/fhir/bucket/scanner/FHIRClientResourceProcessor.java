/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.scanner;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import org.linuxforhealth.fhir.bucket.api.IResourceEntryProcessor;
import org.linuxforhealth.fhir.bucket.api.ResourceBundleError;
import org.linuxforhealth.fhir.bucket.api.ResourceEntry;
import org.linuxforhealth.fhir.bucket.api.ResourceIdValue;
import org.linuxforhealth.fhir.bucket.client.FHIRBucketClient;
import org.linuxforhealth.fhir.bucket.client.FhirServerResponse;
import org.linuxforhealth.fhir.bucket.client.PostResource;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Bundle.Entry.Response;
import org.linuxforhealth.fhir.model.resource.Resource;

/**
 * Calls the FHIR REST API to create resources
 */
public class FHIRClientResourceProcessor implements IResourceEntryProcessor {
    private static final Logger logger = Logger.getLogger(FHIRClientResourceProcessor.class.getName());
    private static final int BATCH_SIZE = 200;
    
    // Nanos in a millisecond
    private static final long NANOS_MS = 1000000;

    // Client for making FHIR server requests
    private final FHIRBucketClient fhirClient;

    // Access to the FHIR bucket persistence layer to record logical ids
    private final DataAccess dataAccess;
    
    /**
     * Public constructor
     * @param poolSize
     */
    public FHIRClientResourceProcessor(FHIRBucketClient fc, DataAccess dataAccess) {
        this.fhirClient = fc;
        this.dataAccess = dataAccess;
    }

    @Override
    public void process(ResourceEntry re) {
        
        boolean success = false;
        try {
            Resource resource = re.getResource();
            final String resourceType = resource.getClass().getSimpleName();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Processing resource: " + resourceType);
            }
            
            // Build a post request for the resource and send to FHIR
            long start = System.nanoTime();
            PostResource post = new PostResource(resource);
            FhirServerResponse response = post.run(fhirClient);
            long end = System.nanoTime();
            switch (response.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_CREATED:
                String locn = response.getLocationHeader();
                if (response.getResource() != null) {
                    // Process the response bundle
                    success = processResponseResource(re, response.getResource());
                    
                    // Update the job with the response time for this entry (for local logging)
                    int responseTimeMs = (int)((end - start) / NANOS_MS);
                    re.getJob().setLastCallResponseTime(responseTimeMs);
                } else if (locn != null) {
                    if (locn.startsWith("https://")) {
                        // the response was empty, so in this case we need to extract the id from
                        // the location header
                        int responseTimeMs = (int)((end - start) / NANOS_MS);
                        success = processLocation(re, locn, responseTimeMs);
                    } else {
                        logger.warning("FHIR bad location format [" + re.toString() + "]: " + 
                                locn);
                    }
                    
                } else {
                    logger.warning("FHIR request id not found [" + re.toString() + "]: " + 
                            response.getStatusCode() + " " + response.getStatusMessage());
                }
                break;
            default:
                logger.warning("FHIR request failed [" + re.toString() + "]: " + 
                        response.getStatusCode() + " " + response.getStatusMessage());
                processBadRequest(re, response);
                
                // Still set the response time so we can see if it failed because the transaction took too long
                int responseTimeMs = (int)((end - start) / NANOS_MS);
                re.getJob().setLastCallResponseTime(responseTimeMs);
                break;
            }
        } catch (Throwable x) {
            // don't let any exceptions propagate into the thread pool
            logger.log(Level.SEVERE, re.toString(), x);
        } finally {
            // Signal the processing is complete for this entry
            re.getJob().operationComplete(success);
        }
    }

    /**
     * Process the bundle we received in the FHIR POST response to extract all the ids
     * Synthetic example:
        {           
            "entry": [
                {
                    "response": {
                        "etag": "W/\"1\"",
                        "id": "1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309",
                        "lastModified": "2020-08-20T17:22:12.554128Z",
                        "location": "Patient/1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309/_history/1",
                        "status": "201"
                    }
                },
                {
                    "response": {
                        "etag": "W/\"1\"",
                        "id": "1740ce47574-fb9b6b7e-15a4-4abc-bc33-f6b4fdb3d1e3",
                        "lastModified": "2020-08-20T17:22:12.980788Z",
                        "location": "Organization/1740ce47574-fb9b6b7e-15a4-4abc-bc33-f6b4fdb3d1e3/_history/1",
                        "status": "201"
                    }
                },
                ...
            ],  
            "resourceType": "Bundle",
            "type": "transaction-response"
        }
     * 
     * @param bundle
     * @return
     */
    private boolean processResponseResource(ResourceEntry re, Resource resource) {
        boolean result;
        
        if (Bundle.class.isAssignableFrom(resource.getClass())) {
            Bundle bundle = resource.as(Bundle.class);
            result = processResponseBundle(re, bundle);
        } else {
            logger.severe("Resource is not a bundle. Skipping: " + resource.getClass().getSimpleName());
            result = false;
        }
        
        
        return result;
    }
    
    private boolean processResponseBundle(ResourceEntry re, Bundle bundle) {
        
        // Extract the location from every entry in the bundle. Collect them
        // together so that we can make a single batch insert into the database
        // which is going to be a lot more efficient than individual inserts
        List<ResourceIdValue> idValues = new ArrayList<>();
        re.getJob().addTotalResourceCount(bundle.getEntry().size());
        for (Bundle.Entry entry: bundle.getEntry()) {
            Response response = entry.getResponse();
            if (response != null) {
                if (response.getLocation() != null && response.getLocation().getValue() != null) {
                    String locn = response.getLocation().getValue();
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("New resource: " + locn);
                    }
                    ResourceIdValue rid = getResourceIdValue(locn);
                    if (rid != null) {
                        idValues.add(rid);
                    }
                }
            }
        }

        processResourceIdValues(re, idValues);
        return idValues.size() > 0;
    }

    /**
     * Process the list of resource ids as a batch
     * @param re
     * @param idValues
     * @return
     */
    private void processResourceIdValues(ResourceEntry re, List<ResourceIdValue> idValues) {
        if (dataAccess != null) {
            dataAccess.recordLogicalIds(re.getJob().getResourceBundleLoadId(), re.getLineNumber(), idValues, BATCH_SIZE);
        }
    }
    /**
     * Parse the location to create a {@link ResourceIdValue} DTO object.
     * The location can take one of two forms:
     *   "Patient/1740ce473c9-aecca6ca-6824-44a0-a8d8-4cfd230e0309/_history/1"
     *   "https://localhost:9443/fhir-server/api/v4/DiagnosticReport/173eed87a99-605de23b-266d-4b4d-b64f-31e769fda112/_history/1"
     * @param location
     * @return
     */
    private ResourceIdValue getResourceIdValue(String location) {
        ResourceIdValue result;
    
        String[] parts = location.split("/");
        if (parts.length == 10) {
            String resourceType = parts[6];
            String id = parts[7];
            result = new ResourceIdValue(resourceType, id);
        } else if (parts.length == 4) {
            String resourceType = parts[0];
            String id = parts[1];
            result = new ResourceIdValue(resourceType, id);
        } else {
            result = null;
        }
        
        return result;
    }
    
    private boolean processLocation(ResourceEntry re, String location, int responseTimeMs) {
        boolean result = false;
        // the response was empty, so in this case we need to extract the id from
        // the location header, which means cracking the string into parts:
        // https://localhost:9443/fhir-server/api/v4/DiagnosticReport/173eed87a99-605de23b-266d-4b4d-b64f-31e769fda112/_history/1
        re.getJob().addTotalResourceCount(1);
        String[] parts = location.split("/");
        if (parts.length == 10) {
            String resourceType = parts[6];
            String id = parts[7];
            logger.info("[" +re.toString() + "] new " + resourceType + "/" + id + " [took " + responseTimeMs + " ms]");
            if (dataAccess != null) {
                dataAccess.recordLogicalId(resourceType, id, re.getJob().getResourceBundleLoadId(), re.getLineNumber(), responseTimeMs);
            }
            result = true;
        }
        
        return result;
    }

    /**
     * Record the error in the database
     * @param re
     * @param response
     */
    protected void processBadRequest(ResourceEntry re, FhirServerResponse response) {
        
        if (logger.isLoggable(Level.FINE)) {
            // dump the resource and full operational outcome to the log
            logger.fine(re.getJob().getObjectKey() + "[" + re.getLineNumber() + "]: "
                + resourceToString(re.getResource()));
            logger.fine(re.getJob().getObjectKey() + "[" + re.getLineNumber() + "]: "
                + response.getOperationalOutcomeText());
        }
        
        List<ResourceBundleError> errors = new ArrayList<>();
        errors.add(new ResourceBundleError(re.getLineNumber(), response.getOperationalOutcomeText(), 
            response.getResponseTime(), response.getStatusCode(), response.getStatusMessage()));
        
        if (dataAccess != null) {
            dataAccess.recordErrors(re.getJob().getResourceBundleLoadId(), re.getLineNumber(), errors);
        }
    }

    /**
     * Render the resource as a string (for logging)
     * @param resource
     * @return
     */
    private String resourceToString(Resource resource) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        try {
            FHIRGenerator.generator(Format.JSON, false).generate(resource, os);
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (FHIRGeneratorException e) {
            throw new IllegalStateException(e);
        }
    }
}
