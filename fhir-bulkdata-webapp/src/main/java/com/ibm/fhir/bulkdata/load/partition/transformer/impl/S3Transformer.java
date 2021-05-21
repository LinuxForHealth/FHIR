/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.load.partition.transformer.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.bulkdata.load.partition.transformer.PartitionSourceTransformer;
import com.ibm.fhir.bulkdata.provider.impl.S3Provider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataSource;

/**
 * S3Transformer takes the object path, such that GET fhir-import maps to fhir-import.ndjson and fhir-import and
 * anything that matches 'fhir-import*'.
 *
 * Think of this as a matrix of the input to multiple inputs (such as segments in a file).
 */
public class S3Transformer implements PartitionSourceTransformer {

    private static final Logger logger = Logger.getLogger(S3Transformer.class.getName());

    @Override
    public List<BulkDataSource> transformToDataSources(String source, String type, String location) throws FHIRException {
        S3Provider wrapper = new S3Provider(source);
        wrapper.listBuckets();
        // Checks and return empty list.
        if (!wrapper.exists()) {
            wrapper.listBuckets();
            return Collections.emptyList();
        }

        List<BulkDataSource> sources = new ArrayList<>();
        String continuationToken = null;
        ListObjectsV2Result result = null;
        boolean shouldContinue = true;
        while (shouldContinue) {
            result = wrapper.getListObject(continuationToken);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                boolean isToBeProccessed = false;
                if (location != null && !location.trim().isEmpty()) {
                    if (objectSummary.getKey().startsWith(location.trim())) {
                        isToBeProccessed = true;
                    }
                } else {
                    isToBeProccessed = true;
                }
                if (isToBeProccessed) {
                    logger.info("ObjectStorge Object -'" + objectSummary.getKey()
                            + "' - '" + objectSummary.getSize() + "' bytes.");
                    if (objectSummary.getSize() >= 0) {
                        // We  want these to line up when we align with the output of the Job Listener
                        sources.add(new BulkDataSource(type, objectSummary.getKey()));
                    }
                }
            }

            shouldContinue = result != null && result.isTruncated();
            continuationToken = result.getNextContinuationToken();
        }

        return sources;
    }
}