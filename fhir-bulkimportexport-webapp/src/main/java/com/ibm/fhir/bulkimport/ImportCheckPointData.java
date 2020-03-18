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
    private String uniqueIDForImportOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    private int partNumForOperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    private String uploadIdForOperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    private List<PartETag> dataPacksForOperationOutcomes = new ArrayList<>();

    // COS/S3 object name for import failure OperationOutcomes;
    private String uniqueIDForImportFailureOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    private int partNumForFailureOperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    private String uploadIdForFailureOperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    private List<PartETag> dataPacksForFailureOperationOutcomes = new ArrayList<>();

    public ImportCheckPointData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType) {
        super();
        this.importPartitionWorkitem = importPartitionWorkitem;
        this.numOfProcessedResources = numOfProcessedResources;
        this.importPartitionResourceType = importPartitionResourceType;
        String ramdomID = FHIRUtil.getRandomKey("AES");
        this.setUniqueIDForImportOperationOutcomes(ramdomID + "_ok_file.ndjson");
        this.setUniqueIDForImportFailureOperationOutcomes(ramdomID + "_error_file.ndjson");
    }

    public ImportCheckPointData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType,
            int numOfImportedResources, int numOfImportFailures, String uniqueIDForImportFailureOperationOutcomes, String uniqueIDForImportOperationOutcomes,
            String uploadIdForOperationOutcomes, List<PartETag> dataPacksForOperationOutcomes, int partNumForOperationOutcomes,
            String uploadIdForFailureOperationOutcomes, List<PartETag> dataPacksForFailureOperationOutcomes, int partNumForFailureOperationOutcomes) {
        super();
        this.importPartitionWorkitem = importPartitionWorkitem;
        this.numOfProcessedResources = numOfProcessedResources;
        this.importPartitionResourceType = importPartitionResourceType;
        this.numOfImportedResources = numOfImportedResources;
        this.numOfImportFailures = numOfImportFailures;
        this.uniqueIDForImportFailureOperationOutcomes = uniqueIDForImportFailureOperationOutcomes;
        this.uniqueIDForImportOperationOutcomes = uniqueIDForImportOperationOutcomes;
        this.uploadIdForOperationOutcomes = uploadIdForOperationOutcomes;
        this.dataPacksForOperationOutcomes = dataPacksForOperationOutcomes;
        this.partNumForOperationOutcomes = partNumForOperationOutcomes;
        this.uploadIdForFailureOperationOutcomes = uploadIdForFailureOperationOutcomes;
        this.dataPacksForFailureOperationOutcomes = dataPacksForFailureOperationOutcomes;
        this.partNumForFailureOperationOutcomes = partNumForFailureOperationOutcomes;
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
                userData.getUniqueIDForImportFailureOperationOutcomes(), userData.getUniqueIDForImportOperationOutcomes(),
                userData.getUploadIdForOperationOutcomes(), userData.getDataPacksForOperationOutcomes(), userData.getPartNumForOperationOutcomes(),
                userData.getUploadIdForFailureOperationOutcomes(), userData.getDataPacksForFailureOperationOutcomes(), userData.getPartNumForFailureOperationOutcomes());
    }

    public String getUniqueIDForImportOperationOutcomes() {
        return uniqueIDForImportOperationOutcomes;
    }

    public void setUniqueIDForImportOperationOutcomes(String uniqueIDForImportOKOperationOutcomes) {
        this.uniqueIDForImportOperationOutcomes = uniqueIDForImportOKOperationOutcomes;
    }

    public String getUniqueIDForImportFailureOperationOutcomes() {
        return uniqueIDForImportFailureOperationOutcomes;
    }

    public void setUniqueIDForImportFailureOperationOutcomes(String uniqueIDForImportFailureOperationOutcomes) {
        this.uniqueIDForImportFailureOperationOutcomes = uniqueIDForImportFailureOperationOutcomes;
    }

    public int getPartNumForOperationOutcomes() {
        return partNumForOperationOutcomes;
    }

    public void setPartNumForOperationOutcomes(int partNumForOperationOutcomes) {
        this.partNumForOperationOutcomes = partNumForOperationOutcomes;
    }

    public String getUploadIdForOperationOutcomes() {
        return uploadIdForOperationOutcomes;
    }

    public void setUploadIdForOperationOutcomes(String uploadIdForOperationOutcomes) {
        this.uploadIdForOperationOutcomes = uploadIdForOperationOutcomes;
    }

    public List<PartETag> getDataPacksForOperationOutcomes() {
        return dataPacksForOperationOutcomes;
    }

    public void setDataPacksForOperationOutcomes(List<PartETag> dataPacksForOperationOutcomes) {
        this.dataPacksForOperationOutcomes = dataPacksForOperationOutcomes;
    }

    public int getPartNumForFailureOperationOutcomes() {
        return partNumForFailureOperationOutcomes;
    }

    public void setPartNumForFailureOperationOutcomes(int partNumForFailureOperationOutcomes) {
        this.partNumForFailureOperationOutcomes = partNumForFailureOperationOutcomes;
    }

    public String getUploadIdForFailureOperationOutcomes() {
        return uploadIdForFailureOperationOutcomes;
    }

    public void setUploadIdForFailureOperationOutcomes(String uploadIdForFailureOperationOutcomes) {
        this.uploadIdForFailureOperationOutcomes = uploadIdForFailureOperationOutcomes;
    }

    public List<PartETag> getDataPacksForFailureOperationOutcomes() {
        return dataPacksForFailureOperationOutcomes;
    }

    public void setDataPacksForFailureOperationOutcomes(List<PartETag> dataPacksForFailureOperationOutcomes) {
        this.dataPacksForFailureOperationOutcomes = dataPacksForFailureOperationOutcomes;
    }

    public int getNumOfParseFailures() {
        return numOfParseFailures;
    }

    public void setNumOfParseFailures(int numOfParseFailures) {
        this.numOfParseFailures = numOfParseFailures;
    }
}
