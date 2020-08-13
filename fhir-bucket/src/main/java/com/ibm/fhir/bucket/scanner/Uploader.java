/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.scanner;

import java.io.IOException;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;


import com.ibm.fhir.model.type.Id;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Resource;

import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.validation.FHIRValidator;

/**
 * Reads synthea data and uploads to the IBM FHIR Server via a threadpool, attempting
 * to load data as quickly as possible. Several instances can run at the same time
 * and will coordinate their work so two instances will not try to upload the same
 * source data
 */
public class Uploader {
//    public static void main(String[] args) throws Exception {
//        Path path;
//        if (args.length > 0) {
//            path = Paths.get(args[0]);
//        } else {
//            path = Paths.get("src/test/resources");
//        }
//        
//        if (!Files.exists(path)) {
//            System.err.println("Path " + path.toString() + " does not exist.");
//            System.exit(-1);
//        }
//        
//        final ObjectFactory factory = new ObjectFactory();
//        final FHIRClient client = createClient();
//        
////        ExecutorService executor = Executors.newCachedThreadPool();
//        ExecutorService executor = Executors.newFixedThreadPool(64);
//        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
//        Set<Future<Integer>> results = new HashSet<>();
//        long start = System.currentTimeMillis();
//
//        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//            @Override
//            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                String fileName = file.getFileName().toString();
//                if (fileName.endsWith(".json")) {
//                    try {
//                        // Skip files over 1 MB
//                        // TODO split these into multiple requests instead
//                        if (Files.size(file) > 1e6) {
//                            Path bucketOfBigResources = file.getParent().resolveSibling("large");
//                            if (!Files.exists(bucketOfBigResources)) {
//                                System.out.println("Creating " + bucketOfBigResources);
//                                Files.createDirectory(bucketOfBigResources);
//                            }
//                            System.out.println("Moving " + fileName + " to " + bucketOfBigResources);
//                            Files.move(file, bucketOfBigResources.resolve(fileName));
//                            return FileVisitResult.CONTINUE;
//                        }
//                        Reader reader = Files.newBufferedReader(file);
//                        Bundle bundle = FHIRUtil.read(Bundle.class, Format.JSON, reader);
//                        convertToBatchRequest(bundle);
//                        if (isValidFHIR(bundle)) {
//                            Future<Integer> result = completionService.submit(() -> uploadToFHIR(fileName, client, bundle));
//                            results.add(result);
//                        } else {
//                            Path bucketOfInvalidResources = file.getParent().resolveSibling("invalid");
//                            if (!Files.exists(bucketOfInvalidResources)) {
//                                System.out.println("Creating " + bucketOfInvalidResources);
//                                Files.createDirectory(bucketOfInvalidResources);
//                            }
//                            System.out.println("Moving " + fileName + " to " + bucketOfInvalidResources);
//                            Files.move(file, bucketOfInvalidResources.resolve(fileName));
//                        }
//                    } catch (Exception e) {
//                        throw new RuntimeException("Unexpected error while processing " + file, e);
//                    }
//                }
//                return FileVisitResult.CONTINUE;
//            }
//
//            private void convertToBatchRequest(Bundle bundle) throws Exception {
//                BundleType type = bundle.getType();
//                if (type != null) {
//                    switch (type.getValue()) {
//                    case BATCH:
//                        return;
//                    case TRANSACTION:
//                        // convert to batch to improve speed
////                        bundle.setType(factory.createBundleType().withValue(BundleTypeList.BATCH));
//                        return;
//                    case COLLECTION:
//                        transformCollectionToBatchRequest(bundle);
//                        break;
//                    case DOCUMENT:
//                    case HISTORY:
//                    case MESSAGE:
//                    case SEARCHSET:
//                    case BATCH_RESPONSE:
//                    case TRANSACTION_RESPONSE:
//                    default:
//                        throw new Exception("Bundle of type " + type.getValue() + " cannot be uploaded.");
//                    }
//                }
//            }
//
//            private void transformCollectionToBatchRequest(Bundle bundle) throws Exception {
//                bundle.setType(factory.createBundleType().withValue(BundleTypeList.BATCH));
//                for (BundleEntry entry : bundle.getEntry()) {
//                    BundleRequest request = null;
//
//                    ResourceContainer resourceContainer = entry.getResource();
//                    Resource resource = FHIRUtil.getResourceContainerResource(resourceContainer);
//                    String typeName = FHIRUtil.getResourceTypeName(resource);
//                    Id id = resource.getId();
//                    if (id == null) {
//                        request =
//                                factory.createBundleRequest().withMethod(factory.createHTTPVerb().withValue(HTTPVerbList.POST)).withUrl(FHIRUtil.uri(typeName));
//                    } else {
//                        String idStr = resource.getId().getValue();
//                        request = factory.createBundleRequest().withMethod(factory.createHTTPVerb().withValue(HTTPVerbList.PUT)).withUrl(FHIRUtil.uri(typeName
//                                + "/" + idStr));
//                    }
//                    entry.setRequest(request);
//                }
//            }
//
//            private int uploadToFHIR(String name, FHIRClient client, Bundle bundle) throws Exception {
//                long before = System.currentTimeMillis();
//                FHIRResponse response = client.batch(bundle);
//                long after = System.currentTimeMillis();
//                if (response.getStatus() != 200) {
////                    System.err.println("Response body: \n" + response.getResponse().readEntity(String.class));
//                    throw new Exception(name + ": Bundle with " + bundle.getEntry().size() + " entries failed in " 
//                            + (after - before) / 1000 + " seconds with HTTP error code " + response.getStatus());
//                }
//                Bundle responseBundle = response.getResource(Bundle.class);
//                if (responseBundle == null) {
//                    throw new Exception(name + ": Empty response in " + (after - before) / 1000 + " seconds");
//                }
//                int createCount = 0;
//                int updateCount = 0;
//                int failureCount = 0;
//                List<BundleEntry> entries = responseBundle.getEntry();
//                for (BundleEntry entry : entries) {
//                    String returnCode = entry.getResponse().getStatus().getValue();
//                    switch (returnCode) {
//                    case "201": createCount++; break;
//                    case "200": updateCount++; break;
//                    default:
//                        // use the index of the entry if there is no id
//                        String id = entry.getResponse().getId() == null ? "[" + entries.indexOf(entry) + "]" : entry.getResponse().getId();
//                        System.err.println(name + ": Entry " + id + " failed with HTTP error code " + returnCode);
//                        StringWriter writer = new StringWriter();
//                        FHIRUtil.write(FHIRUtil.getResourceContainerResource(entry.getResource()), Format.JSON, writer, false);
//                        System.err.println("OperationOutcome: " + writer.toString());
//                        failureCount++;
//                        break;
//                    }
//                }
//                System.out.println(name + ": Created " + createCount + " and updated " + updateCount + " entries in " + (after - before) / 1000 + " seconds");
//                if (failureCount > 0) {
//                    System.err.println(name + ": " + failureCount + " resources failed to upload.");
//                }
//                return createCount + updateCount;
//            }
//        });
//        
//        System.out.println("done walking");
//        int totalEntries = 0;
//        int errorCount = 0;
//        for (int i = 0; i < results.size(); i++) {
//            try {
//                totalEntries += completionService.take().get();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//                errorCount++;
//            }
//        }
//        long end = System.currentTimeMillis();
//        long seconds = (end - start) / 1000;
//        System.out.print("Completed uploading " + totalEntries + " resources across " + results.size() + " patients in " + (end - start) / 1000 + " seconds ");
//        System.out.println("with " + errorCount + " exceptions.");
//        System.out.println("Resources/second: " + (totalEntries / seconds) );
//        executor.shutdown();
//        System.exit(errorCount);
//    }
}
