/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulktools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.GetObjectRequest;
import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.cloud.objectstorage.services.s3.model.S3Object;
import com.ibm.cloud.objectstorage.services.s3.model.S3ObjectInputStream;
import com.ibm.fhir.bulkcommon.BulkDataUtils;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Tool to break large COS file into multiple ones.
 * This allows us to use multiple partitions (one for each generated COS file) for the Bulkdata import job to
 * import the same type of FHIR resources in parallel to achieve better performance.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    /**
     * The number of nanoseconds in a second.
     */
    private static final double NANOS = 1000 * Constants.NANOMS;

    /**
     * The number of files to break into.
     */
    private static int numberOfFiles;

    /**
     * The IBM COS API key or S3 access key.
     */
    private static String cosApiKey;

    /**
     * The IBM COS service instance id or S3 secret key.
     */
    private static String cosSrvinstId;

    /**
     * The IBM COS or S3 End point URL.
     */
    private static String cosEndpintUrl;

    /**
     * The IBM COS or S3 location.
     */
    private static String cosLocation;

    /**
     * The IBM COS or S3 bucket name to import from.
     */
    private static String cosBucketName;

    /**
     * If use IBM credential.
     */
    private static String cosCredentialIbm;

    /**
     * The COS file to break.
     */
    private static String cosFile2Break;


    /**
     * Do we want to get some FHIR resources with distinct IDs.
     */
    private static boolean isGetDistinceResources = false;


    /**
     * The total number of distinct FHIR resources we want to include in the target files.
     */
    private static int numberOfTotalResources = 0;

    /**
     * Break the file into same size or with same number of lines.
     */
    private static boolean isSegBySize = true;

    /**
     * Parse the command line arguments
     *   --cosApiKey:       the IBM COS API key or S3 access key.
     *   --cosSrvinstId:    the IBM COS service instance id or S3 secret key.
     *   --cosEndpintUrl:   the IBM COS or S3 End point URL.
     *   --cosLocation:     the IBM COS or S3 location.
     *   --cosBucketName:   the IBM COS or S3 bucket name to import from.
     *   --cosCredentialIbm: if use IBM credential(Y/N), default(Y).
     *   --cosFile2Break:    the file(COS/S3 object) to break.
     *   --numberOfFiles:    how many pieces (COS/S3 objects) to break into.
     *   --numberOfTotalResources: the total number of distinct FHIR resources we want to include in the target files.
     *                             used only when segBySize is 'N'(by number of records).
     *   --segBySize:        break the file by file size(default) or by number of records("N").
     *                       (1) if by file size, then we get the total file size from the object meta directly,
     *                           and each piece with size close to (total-file-size/numberOfFiles).
     *                       (2) if by number of records
     *                           (a) with numberOfTotalResources, then each piece has (numberOfTotalResources/numberOfFiles) distinct FHIR resources.
     *                           (b) without numberOfTotalResources, then we go thought the COS/S3 once to get the total record number, then each piece
     *                               will have (total-record-number/numberOfFiles) lines in it.
     * @param args
     */
    public void parseArgs(String[] args) {
        for (int i=0; i<args.length; i++) {
            String arg = args[i];
            switch (arg) {
            case "--cosApiKey":
                if (++i < args.length) {
                    cosApiKey = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosApiKey argument at posn: " + i);
                }
                break;
            case "--cosSrvinstId":
                if (++i < args.length) {
                    cosSrvinstId = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosSrvinstId argument at posn: " + i);
                }
                break;
            case "--cosEndpintUrl":
                if (++i < args.length) {
                    cosEndpintUrl = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosEndpintUrl argument at posn: " + i);
                }
                break;
            case "--cosLocation":
                if (++i < args.length) {
                    cosLocation = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosLocation argument at posn: " + i);
                }
                break;
            case "--cosBucketName":
                if (++i < args.length) {
                    cosBucketName = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosBucketName argument at posn: " + i);
                }
                break;
            case "--cosCredentialIbm":
                if (++i < args.length) {
                    cosCredentialIbm = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosCredentialIbm(Y/N) argument at posn: " + i);
                }
                break;
            case "--cosFile2Break":
                if (++i < args.length) {
                    cosFile2Break = args[i];
                }
                else {
                    throw new IllegalArgumentException("Missing value for --cosFile2Break argument at posn: " + i);
                }
                break;
            case "--segBySize":
                if (++i < args.length) {
                    isSegBySize = args[i].equalsIgnoreCase("N")? false : true;
                }
                else {
                    throw new IllegalArgumentException("Missing value for --segBySize(Y/N) argument at posn: " + i);
                }
                break;
            case "--numberOfFiles":
                if (++i < args.length) {
                    numberOfFiles = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --numberOfFiles argument at posn: " + i);
                }
                break;
            case "--numberOfTotalResources":
                if (++i < args.length) {
                    numberOfTotalResources = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --numberOfTotalResources argument at posn: " + i);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
    }


    private int getFhirResourceNumberFromBufferReader(BufferedReader resReader) throws Exception {
        int lineRed = 0;
        while (true) {
                String resLine = resReader.readLine();
                lineRed++;
                if (resLine == null) {
                    break;
                }
        }
        return lineRed;
    }



    private boolean writeFhirResourceFromBufferReader(BufferedReader resReader,  AmazonS3 cosClient, ByteArrayOutputStream bufferStream, long num4Seg, boolean isSegBySize) throws Exception {
        int lineRed = 0;
        int segNum = 0;
        long segSize = 0;
        HashSet<String> UniqueResourceIds= new HashSet<String>();

        List<PartETag> dataPackTags = new ArrayList<>();
        String uploadId = null;
        int partNum = 1;
        boolean isMore2Read = true;
        boolean isAbortStream = false;
        int totalRead = 0;
        while (isMore2Read) {
            String resLine = resReader.readLine();
            lineRed++;
            totalRead++;
            if (resLine == null) {
                isMore2Read = false;
            } else {
                boolean isToAdd = true;
                if (!isSegBySize && numberOfTotalResources > 1000) {
                    try {
                        Resource res = FHIRParser.parser(Format.JSON).parse(new StringReader(resLine));
                        isToAdd = UniqueResourceIds.add(res.getId());
                    } catch (FHIRParserException ex) {
                        isToAdd = false;
                    }
                }

                if (isToAdd) {
                    bufferStream.write(resLine.getBytes());
                    bufferStream.write(Constants.NDJSON_LINESEPERATOR);
                    segSize += (resLine.getBytes().length + Constants.NDJSON_LINESEPERATOR.length);
                } else {
                    lineRed--;
                }
            }

            if (bufferStream.size() > Constants.COS_PART_MINIMALSIZE
                    || (segNum < numberOfFiles - 1 && ((isSegBySize && segSize >= num4Seg) || (!isSegBySize && lineRed == num4Seg)))
                    || (segNum == numberOfFiles -1 && isGetDistinceResources && lineRed == num4Seg)
                    || !isMore2Read) {
                String segName = cosFile2Break + "_seg" + segNum;
                if (uploadId == null) {
                    uploadId = BulkDataUtils.startPartUpload(cosClient, cosBucketName, segName, true);
                }

                if (bufferStream.size() > 0) {
                    dataPackTags.add(BulkDataUtils.multiPartUpload(cosClient, cosBucketName, segName,uploadId,
                            new ByteArrayInputStream(bufferStream.toByteArray()), bufferStream.size(), partNum++));
                    bufferStream.reset();
                }

                if ((segNum < numberOfFiles - 1 && ((isSegBySize && segSize >= num4Seg) || (!isSegBySize && lineRed == num4Seg)))
                        || (segNum == numberOfFiles -1 && isGetDistinceResources && lineRed == num4Seg)
                        || !isMore2Read) {
                    BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, segName, uploadId, dataPackTags);
                    logger.info("Finished writting for " + segName);
                    lineRed = 0;
                    segNum++;
                    if (segNum == numberOfFiles && isMore2Read) {
                        isMore2Read = false;
                        isAbortStream = true;
                    }
                    segSize = 0;
                    uploadId = null;
                    partNum = 1;
                    dataPackTags.clear();
                }
            }
        }
        if (isGetDistinceResources) {
            logger.info("TotalReads: " + totalRead + " DistinctResources: " + UniqueResourceIds.size());
        }
        return isAbortStream;
    }


    /**
     * Main entry
     * @param args
     */
    public static void main(String[] args) {
        Main m = new Main();
        try {
            m.parseArgs(args);

            long start = System.nanoTime();
            int totalNum = 0;

            AmazonS3 cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKey, cosSrvinstId, cosEndpintUrl,
                    cosLocation);
            if (cosClient == null) {
                throw new Exception("Failed to get CosClient!");
            }

            long num4Seg;
            S3Object item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFile2Break));
            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();

            if (isSegBySize) {
                long totalSize = item.getObjectMetadata().getContentLength();
                num4Seg = totalSize / numberOfFiles;
            } else {
                if (numberOfTotalResources > numberOfFiles) {
                    num4Seg = numberOfTotalResources/numberOfFiles;
                    isGetDistinceResources = true;
                } else {
                    try (S3ObjectInputStream s3InStream = item.getObjectContent();
                            BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
                           totalNum = m.getFhirResourceNumberFromBufferReader(resReader);
                       } catch (Exception ioe) {
                           throw ioe;
                       }
                    num4Seg = totalNum/numberOfFiles;
                }
            }

            item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFile2Break));
            try (S3ObjectInputStream s3InStream = item.getObjectContent();
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
                if (m.writeFhirResourceFromBufferReader(resReader, cosClient, bufferStream, num4Seg, isSegBySize)) {
                    s3InStream.abort();
                }
               } catch (Exception ioe) {
                   throw ioe;
               }

            long end = System.nanoTime();
            logger.info(String.format("Total Resources: %d, Took: %6.3f seconds", totalNum, (end-start)/NANOS));
            System.exit(0);
        }
        catch (Exception x) {
            logger.log(Level.SEVERE, "Failed to run", x);
            System.exit(1);
        }
    }
}
