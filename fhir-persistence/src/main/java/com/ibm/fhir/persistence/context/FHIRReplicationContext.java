/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import java.time.Instant;

/**
 * This class provides context information related to a persistence request
 * associated with a replicated resource.
 */
public class FHIRReplicationContext {
    private String versionId;
    private Instant lastUpdated;
    
    public FHIRReplicationContext() {
    }
    
    /**
     * Since R4 we correctly use an Instant to represent lastUpdated
     * @param versionId
     * @param lastUpdated
     */
    public FHIRReplicationContext(String versionId, Instant lastUpdated) {
        setVersionId(versionId);
        setLastUpdated(lastUpdated);
    }

    public String getVersionId() {
        return versionId;
    }
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }
    public Instant getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
