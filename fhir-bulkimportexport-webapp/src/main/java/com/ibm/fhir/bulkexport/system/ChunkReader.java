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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
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
@Dependent
public class ChunkReader extends AbstractItemReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    boolean isSingleCosObject = false;
    int pageNum = 1;
    // Control the number of records to read in each "item".
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;
    // Search parameters for resource types gotten from fhir.typeFilters job parameter.
    Map<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = null;
    int indexOfCurrentTypeFilter = 0;

    // Used to prevent the same resource from being exported multiple times when multiple _typeFilter for the same
    // resource type are used, which leads to multiple search requests which can have overlaps of resources,
    // loadedResourceIds and isDoDuplicationCheck are always reset when moving to the next resource type.
    Set<String> loadedResourceIds = new HashSet<>();
    boolean isDoDuplicationCheck = false;

    FHIRPersistence fhirPersistence;
    Class<? extends Resource> resourceType;

    /**
     * Fhir tenant id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_TENANT)
    String fhirTenant;

    /**
     * Fhir data store id.
     */
    @Inject
    @BatchProperty(name = Constants.FHIR_DATASTORE_ID)
    String fhirDatastoreId;

    /**
     * Fhir resource type to process.
     */
    @Inject
    @BatchProperty(name = Constants.PARTITION_RESOURCE_TYPE)
    String fhirResourceType;

    /**
     * Fhir Search from date.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_FROMDATE)
    String fhirSearchFromDate;

    /**
     * Fhir search to date.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_TODATE)
    String fhirSearchToDate;

    /**
     * Fhir export type filters.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_TYPEFILTERS)
    String fhirTypeFilters;

    /**
     * Fhir search page size.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_PAGESIZE)
    String fhirSearchPageSize;

    @Inject
    StepContext stepCtx;

    public ChunkReader() {
        super();
    }

    private void fillChunkDataBuffer(List<Resource> resources) throws Exception {
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
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
            chunkData.setCurrentUploadResourceNum(chunkData.getCurrentUploadResourceNum() + resSubTotal);
            chunkData.setCurrentUploadSize(chunkData.getCurrentUploadSize() + chunkData.getBufferStream().size());
            chunkData.setTotalResourcesNum(chunkData.getTotalResourcesNum() + resSubTotal);
            logger.fine("fillChunkDataBuffer: Processed resources - " + resSubTotal + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        } else {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }

    }

    @Override
    public Object readItem() throws Exception {
        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        // If the search already reaches the last page, then check if need to move to the next typeFilter.
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            if (searchParametersForResoureTypes.get(resourceType) == null || searchParametersForResoureTypes.get(resourceType).size() <= indexOfCurrentTypeFilter + 1) {
                chunkData.setMoreToExport(false);
                return null;
            } else {
             // If there is more typeFilter to process for current resource type, then reset pageNum only and move to the next typeFilter.
                pageNum = 1;
                indexOfCurrentTypeFilter++;
            }
        }

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

        List<String> searchCriteria = new ArrayList<>();

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
        txn.unenroll();
        pageNum++;

        if (chunkData == null) {
            chunkData = new TransientUserData(pageNum, null, new ArrayList<PartETag>(), 1, 0, null, 0, 0, 0, 1);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
            stepCtx.setTransientUserData(chunkData);
        } else {
            chunkData.setPageNum(pageNum);
            chunkData.setIndexOfCurrentTypeFilter(indexOfCurrentTypeFilter);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
        }

        if (resources != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("readItem: loaded resources number - " + resources.size());
            }
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
            indexOfCurrentTypeFilter = checkPointData.getIndexOfCurrentTypeFilter();
            stepCtx.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
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
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("open: Set page size to " + pageSize + ".");
                }
            } catch (Exception e) {
                logger.warning("open: Set page size to default(" + Constants.DEFAULT_SEARCH_PAGE_SIZE + ").");
            }
        }

        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();

        searchParametersForResoureTypes = BulkDataUtils.getSearchParemetersFromTypeFilters(fhirTypeFilters);
        resourceType = ModelSupport.getResourceType(fhirResourceType);
    }

    @Override
    public void close() throws Exception {
        // do nothing.
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) stepCtx.getTransientUserData());
    }

}
