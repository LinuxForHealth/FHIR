/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;

import com.ibm.fhir.model.resource.Resource;

/**
 * Carrier for a Resource read from a BucketLoaderJob
 */
public class ResourceEntry {

    private final BucketLoaderJob job;
    
    private final Resource resource;
        
    private final int lineNumber;
    
    /**
     * Public constructor
     * @param job
     * @param resource
     */
    public ResourceEntry(BucketLoaderJob job, Resource resource, int lineNumber) {
        this.job = job;
        this.resource = resource;
        this.lineNumber = lineNumber;
    }
    
    @Override
    public String toString() {
        return job.toString() + ", resourceType=" + resource.getClass().getSimpleName();
    }
    
    /**
     * @return the job
     */
    public BucketLoaderJob getJob() {
        return job;
    }
    
    /**
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }
}