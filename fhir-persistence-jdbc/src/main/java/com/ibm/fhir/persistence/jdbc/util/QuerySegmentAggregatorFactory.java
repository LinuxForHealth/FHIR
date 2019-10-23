/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.logging.Logger;

import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * Encapsulates the logic for creating QuerySegmentAggegator and subclass instances.
 */
public class QuerySegmentAggregatorFactory {
    private static final String CLASSNAME = QuerySegmentAggregatorFactory.class.getName();
    private static final Logger log = java.util.logging.Logger.getLogger(CLASSNAME);

    /**
     * Instantiates and returns a QuerySegmentAggregator instance based on the passed parameters.    
     *
     */
    public static QuerySegmentAggregator buildQuerySegmentAggregator(Class<?> resourceType, int offset, int pageSize, 
                                    ParameterDAO parameterDao, ResourceDAO resourceDao, FHIRSearchContext searchContext) {
        final String METHODNAME = "buildQuerySegmentAggregator";
        log.entering(CLASSNAME, METHODNAME);
        
        QuerySegmentAggregator qsa;
        
        if (searchContext.hasIncludeParameters() || searchContext.hasRevIncludeParameters()) {
            qsa = new InclusionQuerySegmentAggregator(resourceType, offset, pageSize, parameterDao, resourceDao, 
                                                      searchContext.getIncludeParameters(), searchContext.getRevIncludeParameters());
        }
        else if (searchContext.hasSortParameters()) {
            qsa = new SortedQuerySegmentAggregator(resourceType, offset, pageSize, parameterDao, resourceDao, searchContext.getSortParameters());
        }
        else {
            qsa = new QuerySegmentAggregator(resourceType, offset, pageSize, parameterDao, resourceDao);
        }
        
        log.exiting(CLASSNAME, METHODNAME);
        return qsa;
        
    }
    
}
