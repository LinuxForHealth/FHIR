/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.system;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.CheckPointUserData;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk system export Chunk implementation - the Reader.
 *
 */
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    boolean isSingleCosObject = false;
    int pageNum = 1;
    int indexOfCurrentResourceType = 0;
    // Control the number of records to read in each "item".
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;
    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;
    int indexOfCurrentTypeFilter = 0;

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources.
    HashSet<String> loadedResourceIds = new HashSet<>();
    boolean isDoDuplicationCheck = false;

    FHIRPersistence fhirPersistence;
    List<String> resourceTypes;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = "fhir.tenant")
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = "fhir.datastoreid")
    String fhirDatastoreId;

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

    /**
     * Fhir export type filters.
     */
    @Inject
    @BatchProperty(name = "fhir.typeFilters")
    String fhirTypeFilters;

    /**
     * Fhir search page size.
     */
    @Inject
    @BatchProperty(name = "fhir.search.pagesize")
    String fhirSearchPageSize;

    /**
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;

    @Inject
    JobContext jobContext;

    public ChunkReader() {
        super();
    }

    private void fillChunkDataBuffer(List<Resource> resources) throws Exception {
        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        int resSubTotal = 0;
        if (chunkData != null) {
            for (Resource res : resources) {
                if (res == null || (isDoDuplicationCheck && loadedResourceIds.contains(res.getId()))) {
                    continue;
                }

                try {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write(Constants.NDJSON_LINESEPERATOR);
                    resSubTotal++;
                    if (isDoDuplicationCheck) {
                        loadedResourceIds.add(res.getId());
                    }
                } catch (FHIRGeneratorException e) {
                    if (res.getId() != null) {
                        logger.log(Level.WARNING, "fillChunkDataBuffer: Error while writing resources with id '"
                                + res.getId() + "'", e);
                    } else {
                        logger.log(Level.WARNING,
                                "fillChunkDataBuffer: Error while writing resources with unknown id", e);
                    }
                } catch (IOException e) {
                    logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                    throw e;
                }
            }
            chunkData.setCurrentPartResourceNum(chunkData.getCurrentPartResourceNum() + resSubTotal);
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        } else {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }

    }

    @Override
    public Object readItem() throws Exception {

        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        // If the search already reaches the last page, then check if need to move to the next typeFilter or next resource type.
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            Class<? extends Resource> resourceType = ModelSupport.getResourceType(resourceTypes.get(indexOfCurrentResourceType));
            if (searchParametersForResoureTypes.get(resourceType) == null || searchParametersForResoureTypes.get(resourceType).size() <= indexOfCurrentTypeFilter + 1) {
                // If there is no more typeFilter to process for current resource type, then check if there is any more resource type to process.
                if (resourceTypes.size() == indexOfCurrentResourceType + 1) {
                    // No more resource type and page to read, so return null to end the reading.
                    return null;
                } else {
                    // More resource types to read, so reset pageNum, partNum and move resource type index to the next and reset indexOfCurrentTypeFilter.
                    pageNum = 1;
                    chunkData.setPartNum(1);
                    indexOfCurrentResourceType++;
                    indexOfCurrentTypeFilter = 0;
                    isDoDuplicationCheck = false;
                    loadedResourceIds.clear();
                }
            } else {
             // If there is more typeFilter to process for current resource type, then reset pageNum only and move to the next typeFilter.
                pageNum = 1;
                indexOfCurrentTypeFilter++;
            }
        }

        Class<? extends Resource> resourceType = ModelSupport
                .getResourceType(resourceTypes.get(indexOfCurrentResourceType));
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        // Add the search parameters from the current typeFilter for current resource type.
        if (searchParametersForResoureTypes.get(resourceType) != null) {
            queryParameters.putAll(searchParametersForResoureTypes.get(resourceType).get(indexOfCurrentTypeFilter));
            if (searchParametersForResoureTypes.get(resourceType).size() > 1) {
                isDoDuplicationCheck = true;
            }
        }

        List<String> searchCriteria = new ArrayList<String>();

        if (fhirSearchFromDate != null) {
            searchCriteria.add("ge" + fhirSearchFromDate);
        }
        if (fhirSearchToDate != null) {
            searchCriteria.add("lt" + fhirSearchToDate);
        }

        if (!searchCriteria.isEmpty()) {
            queryParameters.put(Constants.FHIR_SEARCH_LASTUPDATED, searchCriteria);
        }

        queryParameters.put("_sort", Arrays.asList(new String[] { Constants.FHIR_SEARCH_LASTUPDATED }));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(pageSize);
        searchContext.setPageNumber(pageNum);
        List<Resource> resources = null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.enroll();
        persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
        resources = fhirPersistence.search(persistenceContext, resourceType).getResource();
        txn.unenroll();;
        pageNum++;

        if (chunkData == null) {
            chunkData = new TransientUserData(pageNum, null, new ArrayList<PartETag>(), 1, 0, 0);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
            if (isSingleCosObject) {
                chunkData.setSingleCosObject(true);
            }
            jobContext.setTransientUserData(chunkData);
        } else {
            chunkData.setPageNum(pageNum);
            chunkData.setIndexOfCurrentResourceType(indexOfCurrentResourceType);
            chunkData.setIndexOfCurrentTypeFilter(indexOfCurrentTypeFilter);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
        }

        if (resources != null) {
            logger.fine("readItem: loaded resources number - " + resources.size());
            fillChunkDataBuffer(resources);
        } else {
            logger.fine("readItem: End of reading!");
        }

        return resources;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
            pageNum = checkPointData.getPageNum();
            indexOfCurrentResourceType = checkPointData.getIndexOfCurrentResourceType();
            indexOfCurrentTypeFilter = checkPointData.getIndexOfCurrentTypeFilter();
            jobContext.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
        }

        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
            logger.fine("open: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.fine("open: Set DatastoreId to default!");
        }
        if (fhirSearchPageSize != null) {
            try {
                pageSize = Integer.parseInt(fhirSearchPageSize);
                logger.fine("open: Set page size to " + pageSize + ".");
            } catch (Exception e) {
                logger.warning("open: Set page size to default(" + Constants.DEFAULT_SEARCH_PAGE_SIZE + ").");
            }
        }

        if (cosBucketObjectName != null
                && cosBucketObjectName.trim().length() > 0
                // Single COS object uploading is for single resource type export only.
                && resourceTypes.size() == 1) {
            isSingleCosObject = true;
            logger.info("open: Use single COS object for uploading!");
        }

        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        resourceTypes = Arrays.asList(fhirResourceType.split("\\s*,\\s*"));
        searchParametersForResoureTypes = BulkDataUtils.getSearchParemetersFromTypeFilters(fhirTypeFilters);
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) jobContext.getTransientUserData());
    }

}
