package com.ibm.fhir.bulkimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Request;
import com.ibm.cloud.objectstorage.services.s3.model.ListObjectsV2Result;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectSummary;
import com.ibm.fhir.bulkcommon.COSUtils;
import com.ibm.fhir.bulkcommon.Constants;

public class ImportPartitionMapper implements PartitionMapper {
    private final static Logger logger = Logger.getLogger(ImportPartitionMapper.class.getName());
    private AmazonS3 cosClient = null;

    @Inject
    StepContext stepCtx;

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
     * The Cos object name.
     */
    @Inject
    @BatchProperty(name = "cos.bucket.objectname")
    String cosBucketObjectName;

    /**
     * If use IBM credential.
     */
    @Inject
    @BatchProperty(name = "cos.credential.ibm")
    String cosCredentialIbm;

    public ImportPartitionMapper() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see PartitionMapper#mapPartitions()
     */
    @Override
    public PartitionPlan mapPartitions() {
        String nextToken = null;
        List<String> resCosObjectNameList = new ArrayList<String>();
        cosClient = COSUtils.getCosClient(cosCredentialIbm, cosApiKeyProperty, cosSrvinstId, cosEndpintUrl,
                cosLocation);

        if (cosClient == null) {
            logger.warning("mapPartitions: Failed to get CosClient!");
            return null;
        } else {
            logger.finer("mapPartitions: Succeed get CosClient!");
        }
        if (cosBucketName == null) {
            logger.warning("mapPartitions: Failed to get cosBucketName!");
            return null;
        }else {
            logger.finer("mapPartitions: Succeed get cosBucketName!");
        }
        cosBucketName = cosBucketName.toLowerCase();
        if (!cosClient.doesBucketExist(cosBucketName)) {
            logger.warning("mapPartitions: Bucket '" + cosBucketName + "' not found!");
            COSUtils.listBuckets(cosClient);
            return null;
        }

        ListObjectsV2Result result = null;
        do {
            if (result != null) {
                nextToken = result.getNextContinuationToken();
            }
            ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(cosBucketName).withMaxKeys(1000)
                    .withContinuationToken(nextToken);
            result = cosClient.listObjectsV2(request);
            for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                boolean isToBeProccessed = false;
                if (cosBucketObjectName != null && cosBucketObjectName.trim().length() > 0) {
                    if (objectSummary.getKey().startsWith(cosBucketObjectName.trim())) {
                        isToBeProccessed = true;
                    }
                } else {
                    isToBeProccessed = true;
                }
                if (isToBeProccessed) {
                    logger.info("mapPartitions: COS Object(" + objectSummary.getKey() + ") - " + objectSummary.getSize()
                            + " bytes.");
                    if (objectSummary.getSize() > 0) {
                        resCosObjectNameList.add(objectSummary.getKey());
                    }
                }
            }
        } while (result != null && result.isTruncated());

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(resCosObjectNameList.size());
        pp.setThreads(Math.min(Constants.IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER, resCosObjectNameList.size()));
        Properties[] partitionProps = new Properties[resCosObjectNameList.size()];
        int propCount = 0;
        for (String cosObjectName : resCosObjectNameList) {
            Properties p = new Properties();
            p.setProperty(Constants.IMPORT_PARTITTION_WORKITEM, cosObjectName);
            partitionProps[ propCount++ ] = p;
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }

}
