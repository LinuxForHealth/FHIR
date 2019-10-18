/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.core.context.impl.FHIRPagingContextImpl;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;

public class FHIRHistoryContextImpl extends FHIRPagingContextImpl implements FHIRHistoryContext {
    private Instant since = null;
    private Map<String,List<Integer>> deletedResources = new HashMap<>();
    
    public FHIRHistoryContextImpl() {
    }

    @Override
    public Instant getSince() {
        return since;
    }

    @Override
    public void setSince(Instant since) {
        this.since = since;
    }

    @Override
    public Map<String, List<Integer>> getDeletedResources() {
        return this.deletedResources;
        
    }

    @Override
    public void setDeletedResources(Map<String, List<Integer>> deletedResources) {
        this.deletedResources = deletedResources;
        
    }
}
