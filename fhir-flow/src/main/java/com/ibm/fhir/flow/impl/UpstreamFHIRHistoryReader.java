/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.impl;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import org.apache.http.HttpStatus;

import com.ibm.fhir.bucket.client.FHIRBucketClient;
import com.ibm.fhir.bucket.client.FhirServerResponse;
import com.ibm.fhir.database.utils.thread.ThreadHandler;
import com.ibm.fhir.flow.api.IFlowPool;
import com.ibm.fhir.flow.api.IFlowWriter;
import com.ibm.fhir.flow.api.ResourceIdentifier;
import com.ibm.fhir.flow.api.ResourceIdentifierVersion;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Resource;

/**
 * Reads the change sequence from the upstream system using the
 * whole-system history API. Dispatches vread requests to the flowPool, 
 * and sends the future results to the downstream flowWriter.
 */
public class UpstreamFHIRHistoryReader {
    private static final Logger logger = Logger.getLogger(UpstreamFHIRHistoryReader.class.getName());

    // The pool used to read resource payloads from the upstream system
    private IFlowPool flowPool;

    // The writer used to push resources to the downstream system
    private IFlowWriter flowWriter;

    // The number we use to identify where we are in the upstream change sequence
    private long changeIdMarker;

    private final int count;

    // The client we use to call the upstream whole-system history API
    private FHIRBucketClient client;

    /**
     * Public constructor
     * 
     * @param count
     * @param changeIdMarkerStart
     */
    public UpstreamFHIRHistoryReader(int count, long changeIdMarkerStart) {
        this.count = count;
        this.changeIdMarker = changeIdMarkerStart;
    }

    /**
     * Setter for the flowPool
     * @param flowPool
     */
    public void setFlowPool(IFlowPool flowPool) {
        this.flowPool = flowPool;
    }

    /**
     * Setter for the client
     * @param client
     */
    public void setClient(FHIRBucketClient client) {
        this.client = client;
    }

    /**
     * Setter for the flowWriter
     * @param flowWriter
     */
    public void setFlowWriter(IFlowWriter flowWriter) {
        this.flowWriter = flowWriter;
    }

    /**
     * Keep asking for changes until duration expires, at which point we return
     * with the current changeIdMarker
     * @param duration the length of time to run for; run forever if null
     * @return the last changeIdMarker
     */
    public long fetch(Duration duration) {
        // Keep calling the whole-system history API. Run for the given
        // duration, or forever if duration is null
        long endTime = duration != null ? System.nanoTime() + duration.getSeconds() * 1000000000l : -1l; 
        long lastChangeIdMarker = changeIdMarker;
        do {
            fetch();
            if (this.changeIdMarker == lastChangeIdMarker) {
                // If no change was detected, then take a breather so we don't keep spamming
                // the upstream system looking for data when the system is quiet
                ThreadHandler.safeSleep(1000);
            }
        } while (duration == null || System.nanoTime() < endTime);
        return this.changeIdMarker;
    }

    /**
     * Perform one whole-system history call
     */
    private void fetch() {
        StringBuilder request = new StringBuilder();
        request.append("_history?");
        request.append("_sort=none");
        request.append("&_count=").append(this.count);
        
        if (this.changeIdMarker >= 0) {
            request.append("&_changeIdMarker=").append(this.changeIdMarker);
        }
        request.append("&_excludeTransactionTimeoutWindow=true");
        
        // Make the request and process the result
        final Map<String,String> headers = new HashMap<>();
        headers.put("Prefer", "return=minimal");
        FhirServerResponse response = client.get(request.toString(), headers);
        if (response.getStatusCode() == HttpStatus.SC_OK) {
            Resource r = response.getResource();
            if (r instanceof Bundle) {
                Bundle bundle = r.as(Bundle.class);
                processBundle(bundle);
                updateChangeIdMarker(bundle);
            } else {
                logger.warning("Expected a Bundle, but got this: " + r.toString());
            }
        }
    }

    /**
     * Update the changeIdMarker value from the bundle next link
     * @param bundle
     */
    private void updateChangeIdMarker(Bundle bundle) {
        for (Bundle.Link link: bundle.getLink()) {
            if (link.getRelation() != null && "next".equals(link.getRelation().getValue())) {
                if (link.getUrl() != null && link.getUrl().getValue() != null) {
                    updateChangeIdMarker(link.getUrl().getValue());
                } else {
                    throw new IllegalArgumentException("Bundle next link did not contain a url");
                }
                break;
            }
        }
    }

    /**
     * Extract the changeIdMarker value from the url, which might look something like this:
     *   - https://localhost:9443/fhir-server/api/v4/_history?_count=1000&_sort=none&_changeIdMarker=2048&_excludeTransactionTimeoutWindow=true
     *   - https://localhost:9443/fhir-server/api/v4/_history?_count=1000&_changeIdMarker=2048&_sort=none&_excludeTransactionTimeoutWindow=true
     * @param url
     */
    private void updateChangeIdMarker(String url) {
        // discard the location part, keeping only the params
        int index = url.indexOf('?');
        if (index >= 0 && index < url.length()-1) {
            // break the params up, and find the _changeIdMarker
            String[] params = url.substring(index+1).split("&");
            for (int i=0; i<params.length; i++) {
                String[] nameval = params[i].split("=");
                if (nameval.length == 2 && "_changeIdMarker".equals(nameval[0])) {
                    this.changeIdMarker = Long.parseLong(nameval[1]);
                    break;
                }
            }
        }
    }

    /**
     * Process the response bundle from the whole-system history call
     * @param bundle
     */
    private void processBundle(Bundle bundle) {
        for (Bundle.Entry entry: bundle.getEntry()) {
            switch (entry.getRequest().getMethod().getValueAsEnum()) {
            case DELETE:
                // no value to fetch, so just submit the interaction
                flowWriter.submit(new Delete(ResourceIdentifier.from(entry.getFullUrl())));
                break;
            case PUT:
            case POST:
                // initiate the request to read the resource. Because we need the version, we have to
                // use the response.location field
                Entry.Response response = entry.getResponse();
                if (response != null) {
                    ResourceIdentifierVersion riv = ResourceIdentifierVersion.from(response.getLocation());
                    CompletableFuture<FlowFetchResult> flowFetchFuture = flowPool.requestResource(riv);
    
                    // and submit the interaction using the future which can be resolved later
                    flowWriter.submit(new CreateOrUpdate(riv, flowFetchFuture));
                } else {
                    throw new IllegalStateException("entry does not include a response: " + entry.toString());
                }
                break;
            default:
                break;
            }
        }
    }

    
}