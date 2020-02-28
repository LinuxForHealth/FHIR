/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import java.util.List;
import java.util.Map;

import com.ibm.fhir.core.context.FHIRPagingContext;
import com.ibm.fhir.model.type.Instant;

public interface FHIRHistoryContext extends FHIRPagingContext {
    
    Instant getSince();
    void setSince(Instant since);
    
    /**
     * Returns a Map indicating the deletion history of a resource.
     * The map key is the logical resource id. The value is a List of deleted versions of the resource. 
     * Note there can be more than one deleted version, since a deleted resource can be brought back to life by a subsequent update.
     * @return deleted resources Map
     */
    Map<String,List<Integer>> getDeletedResources();
    
    /**
     * Sets a Map indicating the deletion history of a resource.
     * The map key is the logical resource id. The value is a List of deleted versions of the resource. 
     * Note there can be more than one deleted version, since a deleted resource can be brought back to life by a subsequent update.
     * @param deletedResources
     */
    void setDeletedResources(Map<String,List<Integer>> deletedResources);
}
