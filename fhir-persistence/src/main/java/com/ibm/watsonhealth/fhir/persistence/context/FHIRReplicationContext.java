/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.context;

/**
 * This class provides context information related to a persistence request
 * associated with a replicated resource.
 */
public class FHIRReplicationContext {
    private String versionId;
    private String lastUpdated;
    
    public FHIRReplicationContext() {
    }
    
    public FHIRReplicationContext(String versionId, String lastUpdated) {
        setVersionId(versionId);
        setLastUpdated(lastUpdated);
    }
    
    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
