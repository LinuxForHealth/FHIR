/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.CreateBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.ObjectMetadata;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.PutObjectRequest;
import com.ibm.fhir.bulkcommon.COSUtils;
import com.ibm.fhir.bulkcommon.Constants;
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
 * Bulk export Batchlet implementation.
 * 
 * @author Albert Wang
 */
public class BulkExportBatchLet implements Batchlet {
    private final static Logger logger = Logger.getLogger(BulkExportBatchLet.class.getName());
    private AmazonS3 cosClient = null;
    int pageSize = Constants.DEFAULT_SEARCH_PAGE_SIZE;
    int cosMaxFileSize = 0;
    boolean isSingleCosObject = false;
    String uploadId;
    List<PartETag> cosDataPacks = new ArrayList<PartETag>();
    int partNum = 1;
    String exportFileNames = null;

    /**
     * The IBM COS API key or S3 access key.
     */
    @Inject
    @BatchProperty(name = "cos.api.key")
    String cosApiKeyProperty;

    /**
     * The IBM COS service instance id or s3 secret key.
     */
    @Inject
    @BatchProperty(name = "cos.srvinst.id")
    String cosSrvinstId;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.endpointurl")
    String cosEndpintUrl;

    /**
     * The Cos End point URL.
     */
    @Inject
    @BatchProperty(name = "cos.location")
    String cosLocation;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.name")
    String cosBucketName;

    /**
     * The Cos bucket name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.pathprefix")
    String cosBucketPathPrefix;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    /**
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;

    /**
     * The file size limit when exporting to multiple COS files.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.maxfilesize")
    String cosBucketMaxFileSize;

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

    @Inject
    JobContext jobContext;

    /**
     * Default constructor.
     */
    public BulkExportBatchLet() {

    }

    /**
     * This flag gets set if the batchlet is stopped. This will stop the export.
     */
    private boolean stopRequested = false;

    private void pushFhirJsons2Cos(InputStream in, int dataLength) throws Exception {
        if (cosClient == null) {
            logger.warning("pushFhirJsons2Cos: no cosClient!");
            throw new Exception("pushFhirJsons2Cos: no cosClient!");
        }
        if (isSingleCosObject) {
            cosDataPacks.add(COSUtils.multiPartUpload(cosClient, cosBucketName, cosBucketObjectName, uploadId, in,
                    dataLength, partNum));
            partNum++;
            logger.info(
                    "pushFhirJsons2Cos: Contents were successfully appended to COS object - " + cosBucketObjectName);
        } else {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(dataLength);
            String itemName;
            if (cosBucketPathPrefix != null && cosBucketPathPrefix.trim().length() > 0) {
                itemName = cosBucketPathPrefix + "/" + fhirResourceType + "_" + partNum + ".ndjson";
            } else {
                itemName = "job" + jobContext.getExecutionId() + "/" + fhirResourceType + "_" + partNum + ".ndjson";
            }
            PutObjectRequest req = new PutObjectRequest(cosBucketName, itemName, in, metadata);
            cosClient.putObject(req);
            partNum++;
            logger.info("pushFhirJsons2Cos: " + itemName + " was successfully written to COS");
            if (exportFileNames == null) {
                exportFileNames = itemName;
            } else {
                exportFileNames += " : " + itemName;
            }

        }
    }

    /**
     * Main entry point.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String process() throws Exception {
        String exitStatus;
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);
        if (cosClient == null) {
            logger.warning("process: Failed to get CosClient!");
            throw new Exception("process: Failed to get CosClient!");
        } else {
            logger.finer("process: Got CosClient successfully!");
        }
        if (cosBucketName == null) {
            cosBucketName = Constants.DEFAULT_COS_BUCKETNAME;
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
            isSingleCosObject = true;
        }
        if (!cosClient.doesBucketExist(cosBucketName)) {
            CreateBucketRequest req = new CreateBucketRequest(cosBucketName);
            cosClient.createBucket(req);
        }
        if (fhirTenant == null) {
            fhirTenant = Constants.DEFAULT_FHIR_TENANT;
            logger.info("process: Set tenant to default!");
        }
        if (fhirDatastoreId == null) {
            fhirDatastoreId = Constants.DEFAULT_FHIR_TENANT;
            logger.info("process: Set DatastoreId to default!");
        }
        if (fhirSearchPageSize != null) {
            try {
                pageSize = Integer.parseInt(fhirSearchPageSize);
                logger.info("process: Set page size to " + pageSize + ".");
            } catch (Exception e) {
                logger.warning("process: Set page size to default(" + Constants.DEFAULT_SEARCH_PAGE_SIZE + ").");
            }
        }
        if (!isSingleCosObject && cosBucketMaxFileSize != null) {
            try {
                cosMaxFileSize = Integer.parseInt(cosBucketMaxFileSize);
                logger.info("process: Set max COS file size to " + cosMaxFileSize + ".");
            } catch (Exception e) {
                cosMaxFileSize = Constants.DEFAULT_MAXCOSFILE_SIZE;
                logger.warning("process: Set max COS file size to default(" + Constants.DEFAULT_MAXCOSFILE_SIZE + ").");
            }
        }

        FHIRRequestContext.set(new FHIRRequestContext(fhirTenant, fhirDatastoreId));
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper();
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        Class<? extends Resource> resourceType = (Class<? extends Resource>) ModelSupport
                .getResourceType(fhirResourceType);
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString = "&_sort=" + Constants.FHIR_SEARCH_LASTUPDATED;

        if (fhirSearchFromDate != null) {
            queryString += ("&" + Constants.FHIR_SEARCH_LASTUPDATED + "=ge" + fhirSearchFromDate);
            queryParameters.put(Constants.FHIR_SEARCH_LASTUPDATED,
                    Collections.singletonList("ge" + fhirSearchFromDate));
        }
        if (fhirSearchToDate != null) {
            queryString += ("&" + Constants.FHIR_SEARCH_LASTUPDATED + "=lt" + fhirSearchToDate);
            queryParameters.put(Constants.FHIR_SEARCH_LASTUPDATED, Collections.singletonList("lt" + fhirSearchToDate));
        }

        queryParameters.put("_sort", Arrays.asList(new String[] { Constants.FHIR_SEARCH_LASTUPDATED }));
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        int pageNum = 1, exported = 0, totalExported = 0;
        searchContext.setPageSize(pageSize);

        if (isSingleCosObject) {
            uploadId = COSUtils.startPartUpload(cosClient, cosBucketName, cosBucketObjectName);
        }

        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        while (!stopRequested) {
            searchContext.setPageNumber(pageNum);
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            List<Resource> resources = fhirPersistence.search(persistenceContext, resourceType);
            txn.commit();

            if (resources != null) {
                for (Resource res : resources) {
                    if (stopRequested) {
                        break;
                    }
                    if (res == null) {
                        continue;
                    }
                    try {
                        FHIRGenerator.generator(Format.JSON).generate(res, bufferStream);
                        bufferStream.write(Constants.NDJSON_LINESEPERATOR.getBytes());
                        exported++;
                    } catch (FHIRGeneratorException e) {
                        if (res.getId() != null) {
                            logger.log(Level.WARNING,
                                    "Error while writing resources with id '" + res.getId().getValue() + "'", e);
                        } else {
                            logger.log(Level.WARNING, "Error while writing resources with unknown id", e);
                        }
                    }
                }
            }

            if (exported > 0) {
                if ((!isSingleCosObject && cosMaxFileSize == 0)
                        || (!isSingleCosObject && cosMaxFileSize > 0 && bufferStream.size() > cosMaxFileSize)
                        || (isSingleCosObject && bufferStream.size() > Constants.COS_PART_MINIMALSIZE) || stopRequested
                        || (pageNum + 1) > searchContext.getLastPageNumber()) {
                    pushFhirJsons2Cos(new ByteArrayInputStream(bufferStream.toByteArray()), bufferStream.size());
                    bufferStream.reset();
                    totalExported += exported;
                    exported = 0;
                }
            }
            if (!stopRequested) {
                pageNum++;
            }
            // No more to export, so stop.
            if (pageNum > searchContext.getLastPageNumber()) {
                stopRequested = true;
            }
        }

        // more to export but was stopped/canceled.
        if (pageNum <= searchContext.getLastPageNumber()) {
            if (isSingleCosObject) {
                cosClient.abortMultipartUpload(
                        new AbortMultipartUploadRequest(cosBucketName, cosBucketObjectName, uploadId));
                exitStatus = "Export was canceled! Nothing was exported to COS!";
            } else {
                exitStatus = "Export was stoped! Total exported - " + totalExported;
            }
        } else {
            if (isSingleCosObject) {
                if (totalExported > 0) {
                    COSUtils.finishMultiPartUpload(cosClient, cosBucketName, cosBucketObjectName, uploadId,
                            cosDataPacks);
                    exportFileNames = cosBucketObjectName;
                } else {
                    cosClient.abortMultipartUpload(
                            new AbortMultipartUploadRequest(cosBucketName, cosBucketObjectName, uploadId));
                }
            }
            exitStatus = "Export was finished! Total exported - " + totalExported;
        }

        if (exportFileNames != null) {
            exitStatus += "; " + exportFileNames;
        }
        logger.info("process: " + exitStatus);
        jobContext.setExitStatus(exitStatus);
        return exitStatus;
    }

    /**
     * Called if the batchlet is stopped by the container.
     */
    @Override
    public void stop() throws Exception {
        logger.info("stop: Stop request accepted!");
        stopRequested = true;
    }
}
