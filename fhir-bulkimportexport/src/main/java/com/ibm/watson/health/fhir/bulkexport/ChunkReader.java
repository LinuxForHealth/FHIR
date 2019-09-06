/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Inject;

import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.util.ModelSupport;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watson.health.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watson.health.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.watson.health.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.watson.health.fhir.search.context.FHIRSearchContext;
import com.ibm.watson.health.fhir.search.util.SearchUtil;

/**
 * Bulk export Chunk implementation - the Reader.
 * 
 * @author Albert Wang
 */
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    /**
     * Fhir ResourceType.
     */
    @Inject
    @BatchProperty(name = "fhir.resourcetype")
    String fhirResourceType;

    /**
     * Fhir Search from date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.fromdate")
    String fhirSearchFromDate;

    /**
     * Fhir search to date.
     */
    @Inject
    @BatchProperty(name = "fhir.search.todate")
    String fhirSearchToDate;

    int pageNum = 1;
    // Control the number of records to read in each "item".
    int pageSize = 100;

    /**
     * @see AbstractItemReader#AbstractItemReader()
     */
    public ChunkReader() {
        super();
        if (fhirTenant == null) {
            fhirTenant = "default";
            log("readItem", "Set tenant to default!");
        }
    }

    /**
     * Logging helper.
     */
    private void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    /**
     * @see AbstractItemReader#readItem()
     */
    @SuppressWarnings("unchecked")
    public Object readItem() throws Exception {
        // no more page to read, so return null to end the reading.
        if (pageNum == -1) {
            return null;
        }

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirTenant));

        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        Class<? extends Resource> resourceType = (Class<? extends Resource>) ModelSupport
                .getResourceType(fhirResourceType);
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;

        queryString = "&_lastUpdated=ge" + fhirSearchFromDate + "&_lastUpdated=lt" + fhirSearchToDate
                + "&_sort=_lastUpdated";
        queryParameters.put("_lastUpdated", Collections.singletonList("ge" + fhirSearchFromDate));
        queryParameters.put("_lastUpdated", Collections.singletonList("lt" + fhirSearchToDate));
        queryParameters.put("_sort", Arrays.asList(new String[] { "_lastUpdated" }));

        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);
        List<Resource> resources = null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
        resources = fhirPersistence.search(persistenceContext, resourceType);
        txn.commit();
        pageNum++;
        
        if (pageNum > searchContext.getLastPageNumber()) {
            // Use -1 as pageNum to tell the end of the reading.
            pageNum = -1;
        }

        if (resources != null) {
            log("readItem", "loaded resources number: " + resources.size());
        } else {
            log("readItem", "End of reading!");
        }

        return resources;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            pageNum = (Integer) checkpoint;
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return pageNum;
    }

}
