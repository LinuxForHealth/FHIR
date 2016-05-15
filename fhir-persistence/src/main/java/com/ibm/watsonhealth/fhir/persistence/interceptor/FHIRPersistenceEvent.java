/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.interceptor;

import com.ibm.watsonhealth.fhir.model.Resource;

/**
 * This class represents an event fired by the FHIR persistence interceptor framework.
 */
public class FHIRPersistenceEvent {
    Resource fhirResource;
    
    public FHIRPersistenceEvent() {
    }
    
    public FHIRPersistenceEvent(Resource fhirResource) {
        setFhirResource(fhirResource);
    }

    public Resource getFhirResource() {
        return fhirResource;
    }

    public void setFhirResource(Resource fhirResource) {
        this.fhirResource = fhirResource;
    }
}
