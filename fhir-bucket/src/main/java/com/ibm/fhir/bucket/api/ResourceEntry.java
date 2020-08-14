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
    
    /**
     * Public constructor
     * @param job
     * @param resource
     */
    public ResourceEntry(BucketLoaderJob job, Resource resource) {
        this.job = job;
        this.resource = resource;
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
}