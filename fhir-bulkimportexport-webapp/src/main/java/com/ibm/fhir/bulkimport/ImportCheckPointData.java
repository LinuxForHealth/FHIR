/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.model.util.FHIRUtil;

// Class for tracking the partition import progress and for Batch job check points.
// Also used as data carrier for collecting and aggregation of import metrics.
public class ImportCheckPointData implements Serializable {
    private static final long serialVersionUID = 2189917861035732241L;
    // URL or COS/S3 object name.
    private String importPartitionWorkitem;

    // Values for metrics calculation.
    private int numOfProcessedResources = 0;
    private int numOfImportedResources = 0;
    private int numOfImportFailures = 0;

    // Value used to sign the successful ending of the import.
    private int numOfToBeImported = 0;

    // Parsing failures in current batch.
    private int numOfParseFailures = 0;
    // Fhir resource type processed in this partition.
    private String importPartitionResourceType;

    // COS/S3 object name for import OperationOutcomes.
    private String uniqueID4ImportOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    private int partNum4OperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    private String uploadId4OperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    private List<PartETag> dataPacks4OperationOutcomes = new ArrayList<>();

    // COS/S3 object name for import failure OperationOutcomes;
    private String uniqueID4ImportFailureOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    private int partNum4FailureOperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    private String uploadId4FailureOperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    private List<PartETag> dataPacks4FailureOperationOutcomes = new ArrayList<>();

    public ImportCheckPointData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType) {
        super();
        this.importPartitionWorkitem = importPartitionWorkitem;
        this.numOfProcessedResources = numOfProcessedResources;
        this.importPartitionResourceType = importPartitionResourceType;
        String ramdomID = FHIRUtil.getRandomKey("AES");
        this.setUniqueID4ImportOperationOutcomes(ramdomID + "_ok_file.ndjson");
        this.setUniqueID4ImportFailureOperationOutcomes(ramdomID + "_error_file.ndjson");
    }

    public ImportCheckPointData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType,
            int numOfImportedResources, int numOfImportFailures, String uniqueID4ImportFailureOperationOutcomes, String uniqueID4ImportOperationOutcomes,
            String uploadId4OperationOutcomes, List<PartETag> dataPacks4OperationOutcomes, int partNum4OperationOutcomes,
            String uploadId4FailureOperationOutcomes, List<PartETag> dataPacks4FailureOperationOutcomes, int partNum4FailureOperationOutcomes) {
        super();
        this.importPartitionWorkitem = importPartitionWorkitem;
        this.numOfProcessedResources = numOfProcessedResources;
        this.importPartitionResourceType = importPartitionResourceType;
        this.numOfImportedResources = numOfImportedResources;
        this.numOfImportFailures = numOfImportFailures;
        this.uniqueID4ImportFailureOperationOutcomes = uniqueID4ImportFailureOperationOutcomes;
        this.uniqueID4ImportOperationOutcomes = uniqueID4ImportOperationOutcomes;
        this.uploadId4OperationOutcomes = uploadId4OperationOutcomes;
        this.dataPacks4OperationOutcomes = dataPacks4OperationOutcomes;
        this.partNum4OperationOutcomes = partNum4OperationOutcomes;
        this.uploadId4FailureOperationOutcomes = uploadId4FailureOperationOutcomes;
        this.dataPacks4FailureOperationOutcomes = dataPacks4FailureOperationOutcomes;
        this.partNum4FailureOperationOutcomes = partNum4FailureOperationOutcomes;
    }

    public ImportCheckPointData(int numOfProcessedResources, int numOfImportedResources, int numOfImportFailures, String importPartitionResourceType) {
        super();
        this.numOfProcessedResources = numOfProcessedResources;
        this.numOfImportedResources = numOfImportedResources;
        this.numOfImportFailures = numOfImportFailures;
        this.importPartitionResourceType = importPartitionResourceType;
    }

    public String getImportPartitionWorkitem() {
        return importPartitionWorkitem;
    }

    public void setImportPartitionWorkitem(String importPartitionWorkitem) {
        this.importPartitionWorkitem = importPartitionWorkitem;
    }

    public int getNumOfProcessedResources() {
        return numOfProcessedResources;
    }

    public void setNumOfProcessedResources(int numOfProcessedResources) {
        this.numOfProcessedResources = numOfProcessedResources;
    }

    public int getNumOfImportedResources() {
        return numOfImportedResources;
    }

    public void setNumOfImportedResources(int numOfImportedResources) {
        this.numOfImportedResources = numOfImportedResources;
    }

    public int getNumOfImportFailures() {
        return numOfImportFailures;
    }

    public void setNumOfImportFailures(int numOfImportFailures) {
        this.numOfImportFailures = numOfImportFailures;
    }

    public int getNumOfToBeImported() {
        return numOfToBeImported;
    }

    public void setNumOfToBeImported(int numOfToBeImported) {
        this.numOfToBeImported = numOfToBeImported;
    }

    public String getImportPartitionResourceType() {
        return importPartitionResourceType;
    }

    public void setImportPartitionResourceType(String importPartitionResourceType) {
        this.importPartitionResourceType = importPartitionResourceType;
    }

    public static ImportCheckPointData fromImportTransientUserData(ImportTransientUserData userData) {
        return new ImportCheckPointData(userData.getImportPartitionWorkitem(), userData.getNumOfProcessedResources(),
                userData.getImportPartitionResourceType(), userData.getNumOfImportedResources(), userData.getNumOfImportFailures(),
                userData.getUniqueID4ImportFailureOperationOutcomes(), userData.getUniqueID4ImportOperationOutcomes(),
                userData.getUploadId4OperationOutcomes(), userData.getDataPacks4OperationOutcomes(), userData.getPartNum4OperationOutcomes(),
                userData.getUploadId4FailureOperationOutcomes(), userData.getDataPacks4FailureOperationOutcomes(), userData.getPartNum4FailureOperationOutcomes());
    }

    public String getUniqueID4ImportOperationOutcomes() {
        return uniqueID4ImportOperationOutcomes;
    }

    public void setUniqueID4ImportOperationOutcomes(String uniqueID4ImportOKOperationOutcomes) {
        this.uniqueID4ImportOperationOutcomes = uniqueID4ImportOKOperationOutcomes;
    }

    public String getUniqueID4ImportFailureOperationOutcomes() {
        return uniqueID4ImportFailureOperationOutcomes;
    }

    public void setUniqueID4ImportFailureOperationOutcomes(String uniqueID4ImportFailureOperationOutcomes) {
        this.uniqueID4ImportFailureOperationOutcomes = uniqueID4ImportFailureOperationOutcomes;
    }

    public int getPartNum4OperationOutcomes() {
        return partNum4OperationOutcomes;
    }

    public void setPartNum4OperationOutcomes(int partNum4OperationOutcomes) {
        this.partNum4OperationOutcomes = partNum4OperationOutcomes;
    }

    public String getUploadId4OperationOutcomes() {
        return uploadId4OperationOutcomes;
    }

    public void setUploadId4OperationOutcomes(String uploadId4OperationOutcomes) {
        this.uploadId4OperationOutcomes = uploadId4OperationOutcomes;
    }

    public List<PartETag> getDataPacks4OperationOutcomes() {
        return dataPacks4OperationOutcomes;
    }

    public void setDataPacks4OperationOutcomes(List<PartETag> dataPacks4OperationOutcomes) {
        this.dataPacks4OperationOutcomes = dataPacks4OperationOutcomes;
    }

    public int getPartNum4FailureOperationOutcomes() {
        return partNum4FailureOperationOutcomes;
    }

    public void setPartNum4FailureOperationOutcomes(int partNum4FailureOperationOutcomes) {
        this.partNum4FailureOperationOutcomes = partNum4FailureOperationOutcomes;
    }

    public String getUploadId4FailureOperationOutcomes() {
        return uploadId4FailureOperationOutcomes;
    }

    public void setUploadId4FailureOperationOutcomes(String uploadId4FailureOperationOutcomes) {
        this.uploadId4FailureOperationOutcomes = uploadId4FailureOperationOutcomes;
    }

    public List<PartETag> getDataPacks4FailureOperationOutcomes() {
        return dataPacks4FailureOperationOutcomes;
    }

    public void setDataPacks4FailureOperationOutcomes(List<PartETag> dataPacks4FailureOperationOutcomes) {
        this.dataPacks4FailureOperationOutcomes = dataPacks4FailureOperationOutcomes;
    }

    public int getNumOfParseFailures() {
        return numOfParseFailures;
    }

    public void setNumOfParseFailures(int numOfParseFailures) {
        this.numOfParseFailures = numOfParseFailures;
    }
}
