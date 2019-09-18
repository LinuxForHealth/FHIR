/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.waston.health.fhir.bulkcommon.Constants;
import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.generator.FHIRGenerator;
import com.ibm.watson.health.fhir.model.generator.exception.FHIRGeneratorException;
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
    boolean isSingleCosObject = false;
    int pageNum = 1;
    // Control the number of records to read in each "item".
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;

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

    /**
     * @see AbstractItemReader#AbstractItemReader()
     */
    public ChunkReader() {
        super();
    }

    private void fillChunkDataBuffer(List<Resource> resources) throws Exception {
        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData != null) {
            for (Resource res : resources) {
                if (res == null) {
                    continue;
                }

                try {
                    FHIRGenerator.generator(Format.JSON).generate(res, chunkData.getBufferStream());
                    chunkData.getBufferStream().write("\r\n".getBytes());
                } catch (FHIRGeneratorException e) {
                    if (res.getId() != null) {
                        logger.log(Level.WARNING, "updateChunkDataSizeInfo: Error while writing resources with id '"
                                + res.getId().getValue() + "'", e);
                    } else {
                        logger.log(Level.WARNING,
                                "updateChunkDataSizeInfo: Error while writing resources with unknown id", e);
                    }
                } catch (IOException e) {
                    logger.warning("fillChunkDataBuffer: chunkDataBuffer written error!");
                    throw e;
                }
            }
            logger.info("fillChunkDataBuffer: Processed resources - " + resources.size() + "; Bufferred data size - "
                    + chunkData.getBufferStream().size());
        } else {
            logger.warning("fillChunkDataBuffer: chunkData is null, this should never happen!");
            throw new Exception("fillChunkDataBuffer: chunkData is null, this should never happen!");
        }

    }

    /**
     * @see AbstractItemReader#readItem()
     */
    @SuppressWarnings("unchecked")
    public Object readItem() throws Exception {
        // no more page to read, so return null to end the reading.
        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            return null;
        }
        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
            logger.info("readItem: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("readItem: Set DatastoreId to default!");
        }
        if (fhirSearchPageSize != null) {
            try {
                pageSize = Integer.parseInt(fhirSearchPageSize);
                logger.info("readItem: Set page size to " + pageSize + ".");
            } catch (Exception e) {
                logger.warning("readItem: Set page size to default(" + Constants.DEFAULT_SEARCH_PAGE_SIZE + ").");
            }
        }
        if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
            isSingleCosObject = true;
            logger.info("readItem: Use single COS object for uploading!");
        }

        FHIRConfiguration.setConfigHome("./");
        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        Class<? extends Resource> resourceType = (Class<? extends Resource>) ModelSupport
                .getResourceType(fhirResourceType);
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString = "&_sort=_lastUpdated";

        if (fhirSearchFromDate != null) {
            queryString += ("&_lastUpdated=ge" + fhirSearchFromDate);
            queryParameters.put("_lastUpdated", Collections.singletonList("ge" + fhirSearchFromDate));
        }
        if (fhirSearchToDate != null) {
            queryString += ("&_lastUpdated=lt" + fhirSearchToDate);
            queryParameters.put("_lastUpdated", Collections.singletonList("lt" + fhirSearchToDate));
        }

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

        if (chunkData == null) {
            chunkData = new TransientUserData(pageNum, null, new ArrayList<PartETag>(), 1);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
            if (isSingleCosObject) {
                chunkData.setSingleCosObject(true);
            }
            jobContext.setTransientUserData(chunkData);
        } else {
            chunkData = (TransientUserData) jobContext.getTransientUserData();
            chunkData.setPageNum(pageNum);
            chunkData.setLastPageNum(searchContext.getLastPageNumber());
        }

        if (resources != null) {
            logger.info("readItem: loaded resources number - " + resources.size());
            fillChunkDataBuffer(resources);
        } else {
            logger.info("readItem: End of reading!");
        }

        return resources;
    }

    @Override
    public void open(Serializable checkpoint) throws Exception {
        if (checkpoint != null) {
            CheckPointUserData checkPointData = (CheckPointUserData) checkpoint;
            pageNum = checkPointData.getPageNum();
            jobContext.setTransientUserData(TransientUserData.fromCheckPointUserData(checkPointData));
        }
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return CheckPointUserData.fromTransientUserData((TransientUserData) jobContext.getTransientUserData());
    }

}
