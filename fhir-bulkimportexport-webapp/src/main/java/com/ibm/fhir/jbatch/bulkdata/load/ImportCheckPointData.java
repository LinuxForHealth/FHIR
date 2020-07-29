/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.load;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

// Class for tracking the partition import progress and for Batch job check points.
// Also used as data carrier for collecting and aggregation of import metrics.
public class ImportCheckPointData implements Serializable {
    private static final long serialVersionUID = 2189917861035732241L;
    // URL or COS/S3 object name.
    protected String importPartitionWorkitem;

    // Values for metrics calculation.
    protected int numOfProcessedResources = 0;
    protected int numOfImportedResources = 0;
    protected int numOfImportFailures = 0;
    protected long totalReadMilliSeconds = 0;
    protected long totalWriteMilliSeconds = 0;
    protected long totalValidationMilliSeconds = 0;
    protected long importFileSize = 0;

    protected long inFlyRateBeginMilliSeconds = 0;

    // Value used to sign the successful ending of the import.
    protected int numOfToBeImported = 0;

    // Parsing failures in current batch.
    protected int numOfParseFailures = 0;
    // Fhir resource type processed in this partition.
    protected String importPartitionResourceType;

    // COS/S3 object name for import OperationOutcomes.
    protected String uniqueIDForImportOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    protected int partNumForOperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    protected String uploadIdForOperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    protected List<PartETag> dataPacksForOperationOutcomes = new ArrayList<>();

    // COS/S3 object name for import failure OperationOutcomes;
    protected String uniqueIDForImportFailureOperationOutcomes = null;
    // Part number for COS/S3 multiple-parts upload.
    protected int partNumForFailureOperationOutcomes = 1;
    // Upload id for COS/S3 multiple-parts upload.
    protected String uploadIdForFailureOperationOutcomes = null;
    // ETags for COS/S3 multiple-parts upload.
    protected List<PartETag> dataPacksForFailureOperationOutcomes = new ArrayList<>();

    protected ImportCheckPointData() {
        super();
    }

    public static class Builder {
        protected String importPartitionWorkitem;
        protected int numOfProcessedResources;
        protected String importPartitionResourceType;
        protected int numOfImportedResources;
        protected int numOfImportFailures;
        protected String uniqueIDForImportFailureOperationOutcomes;
        protected String uniqueIDForImportOperationOutcomes;
        protected String uploadIdForOperationOutcomes;
        protected List<PartETag> dataPacksForOperationOutcomes;
        protected int partNumForOperationOutcomes;
        protected String uploadIdForFailureOperationOutcomes;
        protected List<PartETag> dataPacksForFailureOperationOutcomes;
        protected int partNumForFailureOperationOutcomes;
        protected long totalReadMilliSeconds;
        protected long totalValidationMilliSeconds;
        protected long totalWriteMilliSeconds;
        protected long importFileSize;
        protected long inFlyRateBeginMilliSeconds;

        public Builder() {
            super();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder importPartitionWorkitem(String importPartitionWorkitem) {
            this.importPartitionWorkitem = importPartitionWorkitem;
            return this;
        }

        public Builder numOfProcessedResources(int numOfProcessedResources) {
            this.numOfProcessedResources = numOfProcessedResources;
            return this;
        }

        public Builder importPartitionResourceType(String importPartitionResourceType) {
            this.importPartitionResourceType = importPartitionResourceType;
            return this;
        }

        public Builder numOfImportedResources(int numOfImportedResources) {
            this.numOfImportedResources = numOfImportedResources;
            return this;
        }

        public Builder numOfImportFailures(int numOfImportFailures) {
            this.numOfImportFailures = numOfImportFailures;
            return this;
        }

        public Builder uniqueIDForImportFailureOperationOutcomes(String uniqueIDForImportFailureOperationOutcomes) {
            this.uniqueIDForImportFailureOperationOutcomes = uniqueIDForImportFailureOperationOutcomes;
            return this;
        }

        public Builder uniqueIDForImportOperationOutcomes(String uniqueIDForImportOperationOutcomes) {
            this.uniqueIDForImportOperationOutcomes = uniqueIDForImportOperationOutcomes;
            return this;
        }

        public Builder uploadIdForOperationOutcomes(String uploadIdForOperationOutcomes) {
            this.uploadIdForOperationOutcomes = uploadIdForOperationOutcomes;
            return this;
        }

        public Builder dataPacksForOperationOutcomes(List<PartETag> dataPacksForOperationOutcomes) {
            this.dataPacksForOperationOutcomes = dataPacksForOperationOutcomes;
            return this;
        }

        public Builder partNumForOperationOutcomes(int partNumForOperationOutcomes) {
            this.partNumForOperationOutcomes = partNumForOperationOutcomes;
            return this;
        }

        public Builder uploadIdForFailureOperationOutcomes(String uploadIdForFailureOperationOutcomes) {
            this.uploadIdForFailureOperationOutcomes = uploadIdForFailureOperationOutcomes;
            return this;
        }

        public Builder dataPacksForFailureOperationOutcomes(List<PartETag> dataPacksForFailureOperationOutcomes) {
            this.dataPacksForFailureOperationOutcomes = dataPacksForFailureOperationOutcomes;
            return this;
        }

        public Builder partNumForFailureOperationOutcomes(int partNumForFailureOperationOutcomes) {
            this.partNumForFailureOperationOutcomes = partNumForFailureOperationOutcomes;
            return this;
        }

        public Builder totalReadMilliSeconds(long totalReadMilliSeconds) {
            this.totalReadMilliSeconds = totalReadMilliSeconds;
            return this;
        }

        public Builder totalValidationMilliSeconds(long totalValidationMilliSeconds) {
            this.totalValidationMilliSeconds = totalValidationMilliSeconds;
            return this;
        }

        public Builder importFileSize(long importFileSize) {
            this.importFileSize = importFileSize;
            return this;
        }

        public Builder totalWriteMilliSeconds(long totalWriteMilliSeconds) {
            this.totalWriteMilliSeconds = totalWriteMilliSeconds;
            return this;
        }

        public Builder inFlyRateBeginMilliSeconds(long inFlyRateBeginMilliSeconds) {
            this.inFlyRateBeginMilliSeconds = inFlyRateBeginMilliSeconds;
            return this;
        }

        public ImportCheckPointData build(){
            ImportCheckPointData importCheckPointData = new ImportCheckPointData();
            importCheckPointData.importPartitionWorkitem = this.importPartitionWorkitem;
            importCheckPointData.numOfProcessedResources = this.numOfProcessedResources;
            importCheckPointData.importPartitionResourceType = this.importPartitionResourceType;
            importCheckPointData.numOfImportedResources = this.numOfImportedResources;
            importCheckPointData.numOfImportFailures = this.numOfImportFailures;
            importCheckPointData.uniqueIDForImportFailureOperationOutcomes = this.uniqueIDForImportFailureOperationOutcomes;
            importCheckPointData.uniqueIDForImportOperationOutcomes = this.uniqueIDForImportOperationOutcomes;
            importCheckPointData.uploadIdForOperationOutcomes = this.uploadIdForOperationOutcomes;
            importCheckPointData.dataPacksForOperationOutcomes = this.dataPacksForOperationOutcomes;
            importCheckPointData.partNumForOperationOutcomes = this.partNumForOperationOutcomes;
            importCheckPointData.uploadIdForFailureOperationOutcomes = this.uploadIdForFailureOperationOutcomes;
            importCheckPointData.dataPacksForFailureOperationOutcomes = this.dataPacksForFailureOperationOutcomes;
            importCheckPointData.partNumForFailureOperationOutcomes = this.partNumForFailureOperationOutcomes;
            importCheckPointData.totalReadMilliSeconds = this.totalReadMilliSeconds;
            importCheckPointData.totalValidationMilliSeconds = this.totalValidationMilliSeconds;
            importCheckPointData.totalWriteMilliSeconds = this.totalWriteMilliSeconds;
            importCheckPointData.importFileSize = this.importFileSize;
            importCheckPointData.inFlyRateBeginMilliSeconds = this.inFlyRateBeginMilliSeconds;

            return importCheckPointData;
        }
    }


    public String getImportPartitionWorkitem() {
        return importPartitionWorkitem;
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

    public static ImportCheckPointData fromImportTransientUserData(ImportTransientUserData userData) {
        return ImportCheckPointData.Builder.builder()
                .importPartitionWorkitem(userData.getImportPartitionWorkitem())
                .numOfProcessedResources(userData.getNumOfProcessedResources())
                .importPartitionResourceType(userData.getImportPartitionResourceType())
                .numOfImportedResources(userData.getNumOfImportedResources())
                .numOfImportFailures(userData.getNumOfImportFailures())
                .uniqueIDForImportFailureOperationOutcomes(userData.getUniqueIDForImportFailureOperationOutcomes())
                .uniqueIDForImportOperationOutcomes(userData.getUniqueIDForImportOperationOutcomes())
                .uploadIdForOperationOutcomes(userData.getUploadIdForOperationOutcomes())
                .dataPacksForOperationOutcomes(userData.getDataPacksForOperationOutcomes())
                .partNumForOperationOutcomes(userData.getPartNumForOperationOutcomes())
                .uploadIdForFailureOperationOutcomes(userData.getUploadIdForFailureOperationOutcomes())
                .dataPacksForFailureOperationOutcomes(userData.getDataPacksForFailureOperationOutcomes())
                .partNumForFailureOperationOutcomes(userData.getPartNumForFailureOperationOutcomes())
                .totalReadMilliSeconds(userData.getTotalReadMilliSeconds())
                .totalValidationMilliSeconds(userData.getTotalValidationMilliSeconds())
                .totalWriteMilliSeconds(userData.getTotalWriteMilliSeconds())
                .importFileSize(userData.getImportFileSize())
                .inFlyRateBeginMilliSeconds(userData.getInFlyRateBeginMilliSeconds())
                .build();
    }

    public String getUniqueIDForImportOperationOutcomes() {
        return uniqueIDForImportOperationOutcomes;
    }

    public String getUniqueIDForImportFailureOperationOutcomes() {
        return uniqueIDForImportFailureOperationOutcomes;
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

    public int getNumOfParseFailures() {
        return numOfParseFailures;
    }

    public void setNumOfParseFailures(int numOfParseFailures) {
        this.numOfParseFailures = numOfParseFailures;
    }

    public long getTotalReadMilliSeconds() {
        return totalReadMilliSeconds;
    }

    public void setTotalReadMilliSeconds(long totalReadMilliSeconds) {
        this.totalReadMilliSeconds = totalReadMilliSeconds;
    }

    public long getTotalWriteMilliSeconds() {
        return totalWriteMilliSeconds;
    }

    public void setTotalWriteMilliSeconds(long totalWriteMilliSeconds) {
        this.totalWriteMilliSeconds = totalWriteMilliSeconds;
    }

    public long getTotalValidationMilliSeconds() {
        return totalValidationMilliSeconds;
    }

    public void setTotalValidationMilliSeconds(long totalValidationMilliSeconds) {
        this.totalValidationMilliSeconds = totalValidationMilliSeconds;
    }

    public long getImportFileSize() {
        return importFileSize;
    }

    public void setImportFileSize(long importFileSize) {
        this.importFileSize = importFileSize;
    }

    public long getInFlyRateBeginMilliSeconds() {
        return inFlyRateBeginMilliSeconds;
    }

    public void setInFlyRateBeginMilliSeconds(long inFlyRateBeginMilliSeconds) {
        this.inFlyRateBeginMilliSeconds = inFlyRateBeginMilliSeconds;
    }

    @Override
    public String toString() {
        return "ImportCheckPointData [importPartitionWorkitem=" + importPartitionWorkitem + ", numOfProcessedResources=" + numOfProcessedResources
                + ", numOfImportedResources=" + numOfImportedResources + ", numOfImportFailures=" + numOfImportFailures + ", totalReadMilliSeconds="
                + totalReadMilliSeconds + ", totalWriteMilliSeconds=" + totalWriteMilliSeconds + ", totalValidationMilliSeconds=" + totalValidationMilliSeconds
                + ", importFileSize=" + importFileSize + ", inFlyRateBeginMilliSeconds=" + inFlyRateBeginMilliSeconds + ", numOfToBeImported="
                + numOfToBeImported + ", numOfParseFailures=" + numOfParseFailures + ", importPartitionResourceType=" + importPartitionResourceType
                + ", uniqueIDForImportOperationOutcomes=" + uniqueIDForImportOperationOutcomes + ", partNumForOperationOutcomes=" + partNumForOperationOutcomes
                + ", uploadIdForOperationOutcomes=" + uploadIdForOperationOutcomes + ", dataPacksForOperationOutcomes=" + dataPacksForOperationOutcomes
                + ", uniqueIDForImportFailureOperationOutcomes=" + uniqueIDForImportFailureOperationOutcomes + ", partNumForFailureOperationOutcomes="
                + partNumForFailureOperationOutcomes + ", uploadIdForFailureOperationOutcomes=" + uploadIdForFailureOperationOutcomes
                + ", dataPacksForFailureOperationOutcomes=" + dataPacksForFailureOperationOutcomes + "]";
    }


}
