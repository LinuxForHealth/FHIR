/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import org.linuxforhealth.fhir.bulkdata.jbatch.load.exception.FHIRLoadException;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.PartitionSourceTransformer;
import org.linuxforhealth.fhir.bulkdata.provider.impl.S3Provider;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataSource;

/**
 * S3Transformer takes the object path, such that GET fhir-import maps to fhir-import.ndjson and fhir-import and
 * anything that matches 'fhir-import*'.
 *
 * Think of this as a matrix of the input to multiple inputs (such as segments in a file).
 */
public class S3Transformer implements PartitionSourceTransformer {

    private static final Logger LOG = Logger.getLogger(S3Transformer.class.getName());

    @Override
    public List<BulkDataSource> transformToDataSources(String source, String type, String location) throws FHIRException {
        List<BulkDataSource> sources = new ArrayList<>();

        S3Provider provider = new S3Provider(source);
        if (!provider.exists()) {
            provider.listBuckets();
        }

        String loc = location.trim();
        LOG.fine(() -> "Location being verified is '" + loc + "'");

        String continuationToken = null;
        ListObjectsV2Result result = null;
        boolean shouldContinue = true;
        while (shouldContinue) {
            result = provider.getListObject(loc, continuationToken);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                LOG.fine(() -> "Object to be added [" + objectSummary.getKey() + "]");

                if (!loc.isEmpty() && objectSummary.getKey().startsWith(loc)) {
                    LOG.fine(() -> "Object Summary -'" + objectSummary.getKey() + "' - '" + objectSummary.getSize() + "' bytes.");

                    BulkDataSource ds = new BulkDataSource(type, objectSummary.getKey());
                    ds.setOriginalLocation(location);
                    sources.add(ds);
                }
            }

            shouldContinue = result != null && result.isTruncated();
            continuationToken = result.getNextContinuationToken();
        }

        // We shouldn't hit this situation.
        if (sources.isEmpty()) {
            throw new FHIRLoadException("The source is not found '" + location + "'");
        }

        return sources;
    }
}