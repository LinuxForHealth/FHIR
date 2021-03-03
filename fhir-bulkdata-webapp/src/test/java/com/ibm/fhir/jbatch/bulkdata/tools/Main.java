/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.tools;

/**
 * Tool to break large COS file into multiple ones.
 * This allows us to use multiple partitions (one for each generated COS file) for the Bulkdata import job to
 * import the same type of FHIR resources in parallel to achieve better performance.
 */
public class Main {
//    private static final Logger logger = Logger.getLogger(Main.class.getName());
//    /**
//     * The number of files to break into.
//     */
//    private static int numberOfFiles;
//
//    /**
//     * The IBM COS API key or S3 access key.
//     */
//    private static String cosApiKey;
//
//    /**
//     * The IBM COS service instance id or S3 secret key.
//     */
//    private static String cosSrvinstId;
//
//    /**
//     * The IBM COS or S3 End point URL.
//     */
//    private static String cosEndpointUrl;
//
//    /**
//     * The IBM COS or S3 location.
//     */
//    private static String cosLocation;
//
//    /**
//     * The IBM COS or S3 bucket name to import from.
//     */
//    private static String cosBucketName;
//
//    /**
//     * If use IBM credential.
//     */
//    private static String cosCredentialIbm;
//
//    /**
//     * The COS file to break.
//     */
//    private static String cosFileToBreak;
//
//
//    /**
//     * Do we want to get some FHIR resources with distinct IDs.
//     */
//    private static boolean isGetDistinceResources = false;
//
//
//    /**
//     * The total number of distinct FHIR resources we want to include in the target files.
//     */
//    private static int numberOfTotalResources = 0;
//
//    /**
//     * Break the file into same size or with same number of lines.
//     */
//    private static boolean isSegBySize = true;
//
//    /**
//     * Parse the command line arguments
//     * --cosapikey: the IBM COS API key or S3 access key.
//     * --cossrvinstid: the IBM COS service instance id or S3 secret key.
//     * --cosendpointurl: the IBM COS or S3 End point URL.
//     * --coslocation: the IBM COS or S3 location.
//     * --cosbucketname: the IBM COS or S3 bucket name to import from.
//     * --coscredentialibm: if use IBM credential(Y/N), default(Y).
//     * --cosfiletobreak: the file(COS/S3 object) to break.
//     * --numberoffiles: how many pieces (COS/S3 objects) to break into.
//     * --numberoftotalresources: the total number of distinct FHIR resources we want to include in the target files.
//     * used only when segBySize is 'N'(by number of records).
//     * --segbysize: break the file by file size(default) or by number of records("N").
//     * (1) if by file size, then we get the total file size from the object meta directly,
//     * and each piece with size close to (total-file-size/numberOfFiles).
//     * (2) if by number of records
//     * (a) with numberOfTotalResources, then each piece has (numberOfTotalResources/numberOfFiles) distinct FHIR
//     * resources.
//     * (b) without numberOfTotalResources, then we go thought the COS/S3 once to get the total record number, then each
//     * piece
//     * will have (total-record-number/numberOfFiles) lines in it.
//     *
//     * @param args
//     */
//    public void parseArgs(String[] args) {
//        for (int i=0; i<args.length; i++) {
//            String arg = args[i];
//            switch (arg) {
//            case "--cosapikey":
//                if (++i < args.length) {
//                    cosApiKey = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosApiKey argument at posn: " + i);
//                }
//                break;
//            case "--cossrvinstid":
//                if (++i < args.length) {
//                    cosSrvinstId = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosSrvinstId argument at posn: " + i);
//                }
//                break;
//            case "--cosendpointurl":
//                if (++i < args.length) {
//                    cosEndpointUrl = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosEndpointUrl argument at posn: " + i);
//                }
//                break;
//            case "--coslocation":
//                if (++i < args.length) {
//                    cosLocation = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosLocation argument at posn: " + i);
//                }
//                break;
//            case "--cosbucketname":
//                if (++i < args.length) {
//                    cosBucketName = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosBucketName argument at posn: " + i);
//                }
//                break;
//            case "--coscredentialibm":
//                if (++i < args.length) {
//                    cosCredentialIbm = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosCredentialIbm(Y/N) argument at posn: " + i);
//                }
//                break;
//            case "--cosfiletobreak":
//                if (++i < args.length) {
//                    cosFileToBreak = args[i];
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --cosFileToBreak argument at posn: " + i);
//                }
//                break;
//            case "--segbysize":
//                if (++i < args.length) {
//                    isSegBySize = args[i].equalsIgnoreCase("N")? false : true;
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --segBySize(Y/N) argument at posn: " + i);
//                }
//                break;
//            case "--numberoffiles":
//                if (++i < args.length) {
//                    numberOfFiles = Integer.parseInt(args[i]);
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --numberOfFiles argument at posn: " + i);
//                }
//                break;
//            case "--numberoftotalresources":
//                if (++i < args.length) {
//                    numberOfTotalResources = Integer.parseInt(args[i]);
//                }
//                else {
//                    throw new IllegalArgumentException("Missing value for --numberOfTotalResources argument at posn: " + i);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid argument: " + arg);
//            }
//        }
//    }
//
//
//    private int getFhirResourceNumberFromBufferReader(BufferedReader resReader) throws Exception {
//        int lineRed = 0;
//        while (true) {
//                String resLine = resReader.readLine();
//                lineRed++;
//                if (resLine == null) {
//                    break;
//                }
//        }
//        return lineRed;
//    }
//
//
//
//    private boolean writeFhirResourceFromBufferReader(BufferedReader resReader,  AmazonS3 cosClient, long numForSeg, boolean isSegBySize) throws Exception {
//        int lineRed = 0;
//        int segNum = 0;
//        long segSize = 0;
//        HashSet<String> UniqueResourceIds= new HashSet<>();
//
//        List<PartETag> dataPackTags = new ArrayList<>();
//        String uploadId = null;
//        int partNum = 1;
//        boolean isMoreToRead = true;
//        boolean isAbortStream = false;
//        int totalRead = 0;
//        try (ByteArrayOutputStream bufferStream = new ByteArrayOutputStream()) {
//            while (isMoreToRead) {
//                String resLine = resReader.readLine();
//                lineRed++;
//                totalRead++;
//                if (resLine == null) {
//                    isMoreToRead = false;
//                } else {
//                    boolean isToAdd = true;
//                    if (!isSegBySize && numberOfTotalResources > 1000) {
//                        try {
//                            Resource res = FHIRParser.parser(Format.JSON).parse(new StringReader(resLine));
//                            isToAdd = UniqueResourceIds.add(res.getId());
//                        } catch (FHIRParserException ex) {
//                            isToAdd = false;
//                        }
//                    }
//
//                    if (isToAdd) {
//                        bufferStream.write(resLine.getBytes());
//                        bufferStream.write(Constants.NDJSON_LINESEPERATOR);
//                        segSize += (resLine.getBytes().length + Constants.NDJSON_LINESEPERATOR.length);
//                    } else {
//                        lineRed--;
//                    }
//                }
//
//                if (bufferStream.size() > Constants.COS_PART_MINIMALSIZE
//                        || (segNum < numberOfFiles - 1 && ((isSegBySize && segSize >= numForSeg) || (!isSegBySize && lineRed == numForSeg)))
//                        || (segNum == numberOfFiles -1 && isGetDistinceResources && lineRed == numForSeg)
//                        || !isMoreToRead) {
//                    String segName = cosFileToBreak + "_seg" + segNum;
//                    if (uploadId == null) {
//                        uploadId = BulkDataUtils.startPartUpload(cosClient, cosBucketName, segName, true);
//                    }
//
//                    if (bufferStream.size() > 0) {
//                        dataPackTags.add(BulkDataUtils.multiPartUpload(cosClient, cosBucketName, segName,uploadId,
//                                new ByteArrayInputStream(bufferStream.toByteArray()), bufferStream.size(), partNum++));
//                        bufferStream.reset();
//                    }
//
//                    if ((segNum < numberOfFiles - 1 && ((isSegBySize && segSize >= numForSeg) || (!isSegBySize && lineRed == numForSeg)))
//                            || (segNum == numberOfFiles -1 && isGetDistinceResources && lineRed == numForSeg)
//                            || !isMoreToRead) {
//                        BulkDataUtils.finishMultiPartUpload(cosClient, cosBucketName, segName, uploadId, dataPackTags);
//                        logger.info("Finished writting for " + segName);
//                        lineRed = 0;
//                        segNum++;
//                        if (segNum == numberOfFiles && isMoreToRead) {
//                            isMoreToRead = false;
//                            isAbortStream = true;
//                        }
//                        segSize = 0;
//                        uploadId = null;
//                        partNum = 1;
//                        dataPackTags.clear();
//                    }
//                }
//            }
//        }
//        if (isGetDistinceResources) {
//            logger.info("TotalReads: " + totalRead + " DistinctResources: " + UniqueResourceIds.size());
//        }
//        return isAbortStream;
//    }


    /**
     * Main entry
     * @param args
     */
    public static void main(String[] args) {
//        Main m = new Main();
//        try {
//            m.parseArgs(args);
//
//            long start = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
//            int totalNum = 0;
//            AmazonS3 cosClient = BulkDataUtils.getCosClient(cosCredentialIbm, cosApiKey, cosSrvinstId, cosEndpointUrl,
//                    cosLocation, false);
//            if (cosClient == null) {
//                throw new Exception("Failed to get CosClient!");
//            }
//
//            long numForSeg;
//            S3Object item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFileToBreak));
//
//            if (isSegBySize) {
//                long totalSize = item.getObjectMetadata().getContentLength();
//                numForSeg = totalSize / numberOfFiles;
//            } else {
//                if (numberOfTotalResources > numberOfFiles) {
//                    numForSeg = numberOfTotalResources/numberOfFiles;
//                    isGetDistinceResources = true;
//                    totalNum = numberOfTotalResources;
//                } else {
//                    try (S3ObjectInputStream s3InStream = item.getObjectContent();
//                           BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
//                           totalNum = m.getFhirResourceNumberFromBufferReader(resReader);
//                       } catch (Exception ioe) {
//                           throw ioe;
//                       }
//                    numForSeg = totalNum/numberOfFiles;
//                }
//            }
//
//            item = cosClient.getObject(new GetObjectRequest(cosBucketName, cosFileToBreak));
//            try (S3ObjectInputStream s3InStream = item.getObjectContent();
//                    BufferedReader resReader = new BufferedReader(new InputStreamReader(s3InStream))) {
//                if (m.writeFhirResourceFromBufferReader(resReader, cosClient, numForSeg, isSegBySize)) {
//                    s3InStream.abort();
//                }
//               } catch (Exception ioe) {
//                   throw ioe;
//               }
//
//            long end = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());
//            logger.info(String.format("Total Resources: %d, Took: %d seconds", totalNum, end-start));
//            System.exit(0);
//        }
//        catch (Exception x) {
//            logger.log(Level.SEVERE, "Failed to run", x);
//            System.exit(1);
//        }
    }
}
