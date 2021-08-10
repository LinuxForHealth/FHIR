/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.AbortMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.CompleteMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadRequest;
import com.ibm.cloud.objectstorage.services.s3.model.InitiateMultipartUploadResult;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartRequest;
import com.ibm.cloud.objectstorage.services.s3.model.UploadPartResult;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.core.util.URLSupport;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;

/**
 * Utility functions for IBM COS.
 */
public class BulkDataUtils {
    private final static Logger logger = Logger.getLogger(BulkDataUtils.class.getName());

    private static final ConfigurationAdapter adapter = ConfigurationFactory.getInstance();

    // Retry times when https or amazon s3 client timeout or other error happens, e.g, timeout can happen if the batch write to DB takes
    // longer than the socket timeout, set to retry once for now.
    public static final int IMPORT_RETRY_TIMES = 1;

    private static void log(String method, Object msg) {
        logger.info(method + ": " + String.valueOf(msg));
    }

    public static String startPartUpload(AmazonS3 cosClient, String bucketName, String itemName) throws Exception {
        try {
            log("startPartUpload", "Start multi-part upload for " + itemName + " to bucket - " + bucketName);

            InitiateMultipartUploadRequest initMultipartUploadReq = new InitiateMultipartUploadRequest(bucketName, itemName);
            InitiateMultipartUploadResult mpResult = cosClient.initiateMultipartUpload(initMultipartUploadReq);
            return mpResult.getUploadId();
        } catch (Exception sdke) {
            log("startPartUpload", "Upload start Error - " + sdke.getMessage());
            throw sdke;
        }
    }

    /**
     * Use the passed cosClient to upload part of a multi-part object
     * @param cosClient
     * @param bucketName
     * @param itemName
     * @param uploadID
     * @param dataStream
     * @param partSize
     * @param partNum
     * @return
     * @throws Exception
     * @implSpec This method does not close the passed dataStream
     */
    public static PartETag multiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID,
            InputStream dataStream, int partSize, int partNum) throws Exception {
        try {
            log("multiPartUpload", "Upload part " + partNum + " to " + itemName);

            UploadPartRequest upRequest = new UploadPartRequest().withBucketName(bucketName).withKey(itemName)
                    .withUploadId(uploadID).withPartNumber(partNum).withInputStream(dataStream).withPartSize(partSize);

            UploadPartResult upResult = cosClient.uploadPart(upRequest);
            return upResult.getPartETag();
        } catch (Exception sdke) {
            log("multiPartUpload", "Upload part Error - " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }

    public static void finishMultiPartUpload(AmazonS3 cosClient, String bucketName, String itemName, String uploadID,
            List<PartETag> dataPacks) throws Exception {
        try {
            cosClient.completeMultipartUpload(
                    new CompleteMultipartUploadRequest(bucketName, itemName, uploadID, dataPacks));
            log("finishMultiPartUpload", "Upload finished for " + itemName);
        } catch (Exception sdke) {
            log("finishMultiPartUpload", "Upload finish Error: " + sdke.getMessage());
            cosClient.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, itemName, uploadID));
            throw sdke;
        }
    }

    /**
     * @param resReader - the buffer reader to read FHIR resource from.
     * @param numOfProcessedLines - number of the already processed lines.
     * @param fhirResources - List holds the FHIR resources.
     * @param isSkipProcessed - if need to skip the processed lines before read.
     * @return - the number of parsing failures.
     * @throws Exception
     */
    private static int getFhirResourceFromBufferReader(BufferedReader resReader, int numOfProcessedLines, List<Resource> fhirResources,
            boolean isSkipProcessed, String dataSource) throws Exception {
        int exported = 0;
        int lineRed = 0;
        int parseFailures = 0;

        String resLine = null;
        do {
            resLine = resReader.readLine();
            if (resLine != null) {
                lineRed++;
                if (isSkipProcessed && lineRed <= numOfProcessedLines) {
                    continue;
                }
                try {
                    fhirResources.add(FHIRParser.parser(Format.JSON).parse(new StringReader(resLine)));
                    exported++;

                    // Per Code Review, this could be a trap if always compare ==.
                    if (exported >= adapter.getImportNumberOfFhirResourcesPerRead(null)) {
                        break;
                    }
                } catch (FHIRParserException e) {
                    // Log and skip the invalid FHIR resource.
                    parseFailures++;
                    logger.log(Level.INFO, "getFhirResourceFromBufferReader: " + "Failed to parse line "
                            + (numOfProcessedLines + exported + parseFailures) + " of [" + dataSource + "].", e);
                    continue;
                }
            }
        } while (resLine != null);
        return parseFailures;
    }

    public static void cleanupTransientUserData(ImportTransientUserData transientUserData, boolean isAbort) throws Exception {
        if (transientUserData.getInputStream() != null) {
            if (isAbort && transientUserData.getInputStream() instanceof S3ObjectInputStream) {
                // For S3 input stream, if the read is not finished successfully, we have to abort it first.
                ((S3ObjectInputStream)transientUserData.getInputStream()).abort();
            }
            transientUserData.getInputStream().close();
            transientUserData.setInputStream(null);
        }

        if (transientUserData.getBufferReader() != null) {
            transientUserData.getBufferReader().close();
            transientUserData.setBufferReader(null);
        }
    }

    /**
     * @param cosClient - COS/S3 client.
     * @param bucketName - COS/S3 bucket name to read from.
     * @param itemName - COS/S3 object name to read from.
     * @param numOfLinesToSkip - number of lines to skip before read.
     * @param fhirResources - List holds the FHIR resources.
     * @param transientUserData - transient user data for the chunk.
     * @return - number of parsing failures.
     * @throws Exception
     */
    public static int readFhirResourceFromObjectStore(AmazonS3 cosClient, String bucketName, String itemName,
           int numOfLinesToSkip, List<Resource> fhirResources, ImportTransientUserData transientUserData) throws Exception {
        int parseFailures = 0;
        int retryTimes = IMPORT_RETRY_TIMES;
        do {
            try {
                if (transientUserData.getBufferReader() == null) {
                    S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));
                    S3ObjectInputStream s3InStream = item.getObjectContent();
                    transientUserData.setInputStream(s3InStream);
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream));
                    transientUserData.setBufferReader(resReader);
                    // Skip the already processed lines after opening the input stream for first read.
                    parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, true, itemName);
                } else {
                    parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, false, itemName);
                }
                break;
            } catch (Exception ex) {
                // Prepare for retry, skip all the processed lines in previous batches and this batch.
                numOfLinesToSkip = numOfLinesToSkip + fhirResources.size() + parseFailures;
                cleanupTransientUserData(transientUserData, true);
                logger.warning("readFhirResourceFromObjectStore: Error proccesing file [" + itemName + "] - " + ex.getMessage());
                if ((retryTimes--) > 0) {
                    logger.warning("readFhirResourceFromObjectStore: Retry ...");
                } else {
                    // Throw exception to fail the job, the job can be continued from the current checkpoint after the problem is solved.
                    throw ex;
                }
            }
        } while (retryTimes > 0);

        return parseFailures;
    }


    public static long getCosFileSize(AmazonS3 cosClient, String bucketName, String itemName) throws Exception {
            S3Object item = cosClient.getObject(new GetObjectRequest(bucketName, itemName));
            return item.getObjectMetadata().getContentLength();
      }

    /**
     * @param filePath - file path to the ndjson file.
     * @param numOfLinesToSkip - number of lines to skip before read.
     * @param fhirResources - List holds the FHIR resources.
     * @param transientUserData - transient user data for the chunk.
     * @return - number of parsing failures.
     * @throws Exception
     */
    public static int readFhirResourceFromLocalFile(String filePath, int numOfLinesToSkip, List<Resource> fhirResources,
            ImportTransientUserData transientUserData) throws Exception {
        int parseFailures = 0;

        try {
            if (transientUserData.getBufferReader() == null) {
                BufferedReader resReader = Files.newBufferedReader(Paths.get(filePath));
                transientUserData.setBufferReader(resReader);
                // Skip the already processed lines after opening the input stream for first read.
                parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, true, filePath);
            } else {
                parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, false, filePath);
            }
        } catch (Exception ex) {
            // Clean up.
            fhirResources.clear();
            cleanupTransientUserData(transientUserData, true);
            // Log the error and throw exception to fail the job, the job can be continued from the current checkpoint after the problem is solved.
            logger.warning("readFhirResourceFromLocalFile: Error proccesing file [" + filePath + "] - " + ex.getMessage());
            throw ex;
        }

        return parseFailures;
    }

    /**
     * @param dataUrl - URL to the ndjson file.
     * @param numOfLinesToSkip - number of lines to skip before read.
     * @param fhirResources - List holds the FHIR resources.
     * @param transientUserData - transient user data for the chunk.
     * @return - number of parsing failures.
     * @throws Exception
     */
    public static int readFhirResourceFromHttps(String dataUrl, int numOfLinesToSkip, List<Resource> fhirResources,
            ImportTransientUserData transientUserData) throws Exception {
        int parseFailures = 0;
        int retryTimes = IMPORT_RETRY_TIMES;
        do {
            try {
                if (transientUserData.getBufferReader() == null) {
                    InputStream inputStream = new URL(dataUrl).openConnection().getInputStream();
                    transientUserData.setInputStream(inputStream);
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(inputStream));
                    transientUserData.setBufferReader(resReader);
                    // Skip the already processed lines after opening the input stream for first read.
                    parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, true, dataUrl);
                } else {
                    parseFailures = getFhirResourceFromBufferReader(transientUserData.getBufferReader(), numOfLinesToSkip, fhirResources, false, dataUrl);
                }
                break;
            } catch (Exception ex) {
                // Prepare for retry, skip all the processed lines in previous batches and this batch.
                numOfLinesToSkip = numOfLinesToSkip + fhirResources.size() + parseFailures;
                cleanupTransientUserData(transientUserData, true);
                logger.warning("readFhirResourceFromHttps: Error proccesing file [" + dataUrl + "] - " + ex.getMessage());
                if ((retryTimes--) > 0) {
                    logger.warning("readFhirResourceFromHttps: Retry ...");
                } else {
                    // Throw exception to fail the job, the job can be continued from the current checkpoint after the problem is solved.
                    throw ex;
                }
            }
        } while (retryTimes > 0);

        return parseFailures;
    }

    /**
     * Validate the input resource and throw if there are validation errors
     *
     * @param resource
     * @throws FHIRValidationException
     * @throws FHIROperationException
     */
    public static List<OperationOutcome.Issue> validateInput(Resource resource)
            throws FHIRValidationException, FHIROperationException {
        List<OperationOutcome.Issue> issues = FHIRValidator.validator().validate(resource);
        if (!issues.isEmpty()) {
            boolean includesFailure = false;
            for (OperationOutcome.Issue issue : issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    includesFailure = true;
                }
            }

            if (includesFailure) {
                throw new FHIROperationException("Input resource failed validation.").withIssue(issues);
            } else if (logger.isLoggable(Level.FINE)) {
                    String info = issues.stream()
                                .flatMap(issue -> Stream.of(issue.getDetails()))
                                .flatMap(details -> Stream.of(details.getText()))
                                .flatMap(text -> Stream.of(text.getValue()))
                                .collect(Collectors.joining(", "));
                    logger.fine("Validation warnings for input resource: [" + info + "]");
            }
        }
        return issues;
    }

    /**
     * converts the type filter into a series of search parameters which are used to filter the bulk data export results.
     *
     * @param typeFilters
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static Map<Class<? extends Resource>, List<Map<String, List<String>>>> getSearchParametersFromTypeFilters(String typeFilters) throws UnsupportedEncodingException, URISyntaxException {
        HashMap<Class<? extends Resource>, List<Map<String, List<String>>>> searchParametersForResoureTypes = new HashMap<>();
        if (typeFilters != null) {
            List<String> typeFilterList = Arrays.asList(typeFilters.split("\\s*,\\s*"));

            for (String typeFilter : typeFilterList) {
                String typeFilterDecoded = URLDecoder.decode(typeFilter.trim(), StandardCharsets.UTF_8.toString());
                if (typeFilterDecoded.contains("?")) {
                    // Need to account for Systems and re-encode.
                    URI uri = new URI(typeFilterDecoded.trim().replaceAll("\\|", "%7C"));

                    if (uri.getPath() == null || uri.getQuery() == null) {
                        logger.log(Level.WARNING, "Bad type filter: {0}", typeFilterDecoded);
                        continue;
                    }

                    Map<String, List<String>> queryParameters = URLSupport.parseQuery(uri.getQuery(), false);
                    Class<? extends Resource> resourceType = ModelSupport.getResourceType(uri.getPath());
                    if (!queryParameters.isEmpty() && resourceType != null) {
                        searchParametersForResoureTypes
                            .computeIfAbsent(resourceType, k -> new ArrayList<>())
                            .add(queryParameters);
                    }
                }
            }
        }
        return searchParametersForResoureTypes;
    }

    public static JsonArray getDataSourcesFromJobInput(String dataSourcesInfo) {
        try (JsonReader reader =
                Json.createReader(new StringReader(
                        new String(Base64.getDecoder().decode(dataSourcesInfo), StandardCharsets.UTF_8)))) {
            JsonArray dataSourceArray = reader.readArray();
            return dataSourceArray;
        }
    }

    /**
     * Update the chunkData with the stats from the newly finished upload.
     * The data is added to the summary from the chunkData's currentUploadResourceNum.
     *
     * @param fhirResourceType
     * @param chunkData
     */
    public static void updateSummary(String fhirResourceType, ExportTransientUserData chunkData) {
        if (chunkData.getResourceTypeSummary() == null) {
            chunkData.setResourceTypeSummary(fhirResourceType + "[" + chunkData.getCurrentUploadResourceNum());
        } else {
            chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "," + chunkData.getCurrentUploadResourceNum());
        }

        if (chunkData.getPageNum() >= chunkData.getLastPageNum()) {
            chunkData.setResourceTypeSummary(chunkData.getResourceTypeSummary() + "]");
        }
    }
}