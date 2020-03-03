/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulktools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

/**
 * Tool to break large COS file into multiple ones.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    // number of nanoseconds in a second
    private static final double NANOS = 1e9;

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
     * The number of files to break into.
     */
    private static int numberOfFiles;

    /**
     * Parse the command line arguments
     *   --cosApiKey
     *   --cosSrvinstId
     *   --cosEndpintUrl
     *   --cosLocation
     *   --cosBucketName
     *   --cosCredentialIbm
     *   --cosFile2Break
     *   --numberOfFiles
     * @param args
     */
    public void parseArgs(String[] args) {
        // really simple args, so nothing fancy needed here
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
                    throw new IllegalArgumentException("Missing value for --cosCredentialIbm argument at posn: " + i);
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
            case "--numberOfFiles":
                if (++i < args.length) {
                    numberOfFiles = Integer.parseInt(args[i]);
                }
                else {
                    throw new IllegalArgumentException("Missing value for --numberOfFiles argument at posn: " + i);
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
                    System.out.println("\n Count finished!!!!!");
                    break;
                } else {
                    System.out.print(".");
                }
        }
        return lineRed;
    }



    private void writeFhirResourceFromBufferReader(BufferedReader resReader,  AmazonS3 cosClient, ByteArrayOutputStream bufferStream, int numOfRes4Seg) throws Exception {
        int lineRed = 0;
        int segNum = 0;
        List<PartETag> dataPackTags = new ArrayList<>();
        String uploadId = null;
        int partNum = 1;
        boolean isMore2Read = true;
        while (isMore2Read) {
                String resLine = resReader.readLine();
                lineRed++;
                if (resLine == null) {
                    System.out.println("\n Read finished!!!!!");
                    isMore2Read = false;
                } else {
                    System.out.print(".");
                    bufferStream.write(resLine.getBytes());
                    bufferStream.write(Constants.NDJSON_LINESEPERATOR);
                }

                if (bufferStream.size() > Constants.COS_PART_MINIMALSIZE
                        || (segNum < numberOfFiles - 1 && lineRed == numOfRes4Seg)
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

                    if (segNum < numberOfFiles - 1 && lineRed == numOfRes4Seg
                            || !isMore2Read) {
                        BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, segName, uploadId, dataPackTags);
                        System.out.println("Finished writting for " + segName);
                        lineRed = 0;
                        segNum++;
                        uploadId = null;
                        partNum = 1;
                        dataPackTags.clear();
                    }
                }
        }

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
                logger.warning("Failed to get CosClient!");
                System.exit(1);
            }

            S3Object item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFile2Break));
            try (S3ObjectInputStream s3InStream = item.getObjectContent();
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
                   totalNum = m.getFhirResourceNumberFromBufferReader(resReader);
               } catch (Exception ioe) {
                   logger.warning("Error proccesing file " + cosFile2Break + " - " + ioe.getMessage());
                   System.exit(1);
               }

            ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
            int numOfRes4Seg = totalNum/numberOfFiles;

            item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFile2Break));
            try (S3ObjectInputStream s3InStream = item.getObjectContent();
                    BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
                m.writeFhirResourceFromBufferReader(resReader, cosClient, bufferStream, numOfRes4Seg);
               } catch (Exception ioe) {
                   logger.warning("Error proccesing file " + cosFile2Break + " - " + ioe.getMessage());
                   System.exit(1);
               }

            long end = System.nanoTime();
            logger.info(String.format("Total Resources: %d, Took: %6.3f seconds", totalNum, (end-start)/NANOS));
        }
        catch (Exception x) {
            logger.log(Level.SEVERE, "Failed to run", x);
        }
        finally {
            System.exit(0);
        }
    }
}
