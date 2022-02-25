/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

// Class for tracking the partition import progress and for Batch job check points.
// Also used as data carrier for collecting and aggregation of import metrics.
public class ImportCheckPointData implements Serializable {
    private static final long serialVersionUID = 2189917861035732241L;
    // URL or COS/S3 object name.
    protected String importPartitionWorkitem;
    protected String matrixWorkItem;

    // Values for metrics calculation.
    protected long numOfProcessedResources = 0;
    protected long numOfImportedResources = 0;
    protected long numOfImportFailures = 0;
    protected long numOfSkipped = 0;
    protected long totalReadMilliSeconds = 0;
    protected long totalWriteMilliSeconds = 0;
    protected long totalValidationMilliSeconds = 0;
    protected long importFileSize = 0;

    // Track the Azure Progress
    protected long currentBytes = 0;

    protected long inFlyRateBeginMilliSeconds = 0;

    // Value used to sign the successful ending of the import.
    protected long numOfToBeImported = 0;

    // Parsing failures in current batch.
    protected long numOfParseFailures = 0;
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

    // Used to track last resource count when in fly rate was logged
    protected long lastChecked = 0;

    protected ImportCheckPointData() {
        super();
    }

    public String getImportPartitionWorkitem() {
        return importPartitionWorkitem;
    }

    public long getNumOfProcessedResources() {
        return numOfProcessedResources;
    }

    public void setNumOfProcessedResources(long numOfProcessedResources) {
        this.numOfProcessedResources = numOfProcessedResources;
    }

    public void addToNumOfProcessedResources(long numOfProcessedResources) {
        this.numOfProcessedResources += numOfProcessedResources;
    }

    public long getNumOfImportedResources() {
        return numOfImportedResources;
    }

    public void setNumOfImportedResources(long numOfImportedResources) {
        this.numOfImportedResources = numOfImportedResources;
    }

    public void addToNumOfImportedResources(long numOfImportedResources) {
        this.numOfImportedResources += numOfImportedResources;
    }

    public long getNumOfSkippedResources() {
        return numOfSkipped;
    }

    public void setNumOfSkippedResources(long skipped) {
        this.numOfSkipped = skipped;
    }

    public void addToNumOfSkippedResources(long skipped) {
        this.numOfSkipped += skipped;
    }

    public long getNumOfImportFailures() {
        return numOfImportFailures;
    }

    public void setNumOfImportFailures(long numOfImportFailures) {
        this.numOfImportFailures = numOfImportFailures;
    }

    public void addToNumOfImportFailures(long numOfImportFailures) {
        this.numOfImportFailures += numOfImportFailures;
    }

    public long getNumOfToBeImported() {
        return numOfToBeImported;
    }

    public void setNumOfToBeImported(long numOfToBeImported) {
        this.numOfToBeImported = numOfToBeImported;
    }

    public String getImportPartitionResourceType() {
        return importPartitionResourceType;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public void setCurrentBytes(long currentBytes) {
        this.currentBytes = currentBytes;
    }

    public long getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(long lastChecked) {
        this.lastChecked = lastChecked;
    }

    public static ImportCheckPointData fromImportTransientUserData(ImportTransientUserData userData) {
        return ImportCheckPointData.Builder.builder()
                .importPartitionWorkitem(userData.getImportPartitionWorkitem())
                .matrixWorkItem(userData.matrixWorkItem)
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
                .numOfSkippedResources(userData.getNumOfSkippedResources())
                .currentBytes(userData.getCurrentBytes())
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

    public long getNumOfParseFailures() {
        return numOfParseFailures;
    }

    public void setNumOfParseFailures(long numOfParseFailures) {
        this.numOfParseFailures = numOfParseFailures;
    }

    public void addToNumOfParseFailures(long numOfParseFailures) {
        this.numOfParseFailures += numOfParseFailures;
    }

    public long getTotalReadMilliSeconds() {
        return totalReadMilliSeconds;
    }

    public void setTotalReadMilliSeconds(long totalReadMilliSeconds) {
        this.totalReadMilliSeconds = totalReadMilliSeconds;
    }

    public void addToTotalReadMilliSeconds(long totalReadMilliSeconds) {
        this.totalReadMilliSeconds += totalReadMilliSeconds;
    }

    public long getTotalWriteMilliSeconds() {
        return totalWriteMilliSeconds;
    }

    public void setTotalWriteMilliSeconds(long totalWriteMilliSeconds) {
        this.totalWriteMilliSeconds = totalWriteMilliSeconds;
    }

    public void addToTotalWriteMilliSeconds(long totalWriteMilliSeconds) {
        this.totalWriteMilliSeconds += totalWriteMilliSeconds;
    }

    public long getTotalValidationMilliSeconds() {
        return totalValidationMilliSeconds;
    }

    public void setTotalValidationMilliSeconds(long totalValidationMilliSeconds) {
        this.totalValidationMilliSeconds = totalValidationMilliSeconds;
    }

    public void addTotalValidationMilliSeconds(long validationMilliSeconds) {
        this.totalValidationMilliSeconds += totalValidationMilliSeconds;
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

    public void setMatrixWorkItem(String matrixWorkItem) {
        this.matrixWorkItem = matrixWorkItem;
    }

    public String getMatrixWorkItem() {
        return matrixWorkItem;
    }

    public static class Builder {

        protected String importPartitionWorkitem;
        protected String matrixWorkItem;
        protected long numOfProcessedResources;
        protected String importPartitionResourceType;
        protected long numOfImportedResources;
        protected long numOfImportFailures;
        protected String uniqueIDForImportFailureOperationOutcomes;
        protected String uniqueIDForImportOperationOutcomes;
        protected String uploadIdForOperationOutcomes;
        protected Set<PartETag> dataPacksForOperationOutcomes = new LinkedHashSet<>();
        protected int partNumForOperationOutcomes;
        protected String uploadIdForFailureOperationOutcomes;
        protected Set<PartETag> dataPacksForFailureOperationOutcomes = new LinkedHashSet<>();
        protected int partNumForFailureOperationOutcomes;
        protected long totalReadMilliSeconds;
        protected long totalValidationMilliSeconds;
        protected long totalWriteMilliSeconds;
        protected long importFileSize;
        protected long inFlyRateBeginMilliSeconds;
        protected long currentBytes;
        protected long numOfSkippedResources;

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

        public Builder matrixWorkItem(String matrixWorkItem) {
            this.matrixWorkItem = matrixWorkItem;
            return this;
        }

        public Builder numOfProcessedResources(long numOfProcessedResources) {
            this.numOfProcessedResources = numOfProcessedResources;
            return this;
        }

        public Builder importPartitionResourceType(String importPartitionResourceType) {
            this.importPartitionResourceType = importPartitionResourceType;
            return this;
        }

        public Builder numOfImportedResources(long numOfImportedResources) {
            this.numOfImportedResources = numOfImportedResources;
            return this;
        }

        public Builder numOfSkippedResources(long numOfSkippedResources) {
            this.numOfSkippedResources = numOfSkippedResources;
            return this;
        }

        public Builder numOfImportFailures(long numOfImportFailures) {
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
            if (dataPacksForOperationOutcomes != null) {
                this.dataPacksForOperationOutcomes.addAll(dataPacksForOperationOutcomes);
            }
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
            if (dataPacksForFailureOperationOutcomes != null) {
                this.dataPacksForFailureOperationOutcomes.addAll(dataPacksForFailureOperationOutcomes);
            }
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

        public Builder currentBytes(long currentBytes) {
            this.currentBytes = currentBytes;
            return this;
        }

        public ImportCheckPointData build() {
            ImportCheckPointData importCheckPointData = new ImportCheckPointData();
            importCheckPointData.importPartitionWorkitem = this.importPartitionWorkitem;
            importCheckPointData.matrixWorkItem = this.matrixWorkItem;
            importCheckPointData.numOfProcessedResources = this.numOfProcessedResources;
            importCheckPointData.importPartitionResourceType = this.importPartitionResourceType;
            importCheckPointData.numOfImportedResources = this.numOfImportedResources;
            importCheckPointData.numOfImportFailures = this.numOfImportFailures;
            importCheckPointData.uniqueIDForImportFailureOperationOutcomes = this.uniqueIDForImportFailureOperationOutcomes;
            importCheckPointData.uniqueIDForImportOperationOutcomes = this.uniqueIDForImportOperationOutcomes;
            importCheckPointData.uploadIdForOperationOutcomes = this.uploadIdForOperationOutcomes;
            importCheckPointData.dataPacksForOperationOutcomes = new ArrayList<>(this.dataPacksForOperationOutcomes);
            importCheckPointData.partNumForOperationOutcomes = this.partNumForOperationOutcomes;
            importCheckPointData.uploadIdForFailureOperationOutcomes = this.uploadIdForFailureOperationOutcomes;
            importCheckPointData.dataPacksForFailureOperationOutcomes = new ArrayList<>(
                    this.dataPacksForFailureOperationOutcomes);
            importCheckPointData.partNumForFailureOperationOutcomes = this.partNumForFailureOperationOutcomes;
            importCheckPointData.totalReadMilliSeconds = this.totalReadMilliSeconds;
            importCheckPointData.totalValidationMilliSeconds = this.totalValidationMilliSeconds;
            importCheckPointData.totalWriteMilliSeconds = this.totalWriteMilliSeconds;
            importCheckPointData.importFileSize = this.importFileSize;
            importCheckPointData.inFlyRateBeginMilliSeconds = this.inFlyRateBeginMilliSeconds;
            importCheckPointData.numOfSkipped = this.numOfSkippedResources;
            importCheckPointData.currentBytes = this.currentBytes;
            return importCheckPointData;
        }
    }

    @Override
    public String toString() {
        return "ImportCheckPointData [importPartitionWorkitem=" + importPartitionWorkitem + ", numOfProcessedResources=" + numOfProcessedResources
                + ", matrixWorkItem=" + matrixWorkItem
                + ", numOfImportedResources=" + numOfImportedResources + ", numOfImportFailures=" + numOfImportFailures + ", totalReadMilliSeconds="
                + totalReadMilliSeconds + ", totalWriteMilliSeconds=" + totalWriteMilliSeconds + ", totalValidationMilliSeconds=" + totalValidationMilliSeconds
                + ", importFileSize=" + importFileSize + ", currentBytes=" + currentBytes + ", inFlyRateBeginMilliSeconds=" + inFlyRateBeginMilliSeconds
                + ", numOfToBeImported=" + numOfToBeImported + ", numOfParseFailures=" + numOfParseFailures + ", importPartitionResourceType="
                + importPartitionResourceType + ", uniqueIDForImportOperationOutcomes=" + uniqueIDForImportOperationOutcomes + ", partNumForOperationOutcomes="
                + partNumForOperationOutcomes + ", uploadIdForOperationOutcomes=" + uploadIdForOperationOutcomes + ", dataPacksForOperationOutcomes="
                + dataPacksForOperationOutcomes + ", uniqueIDForImportFailureOperationOutcomes=" + uniqueIDForImportFailureOperationOutcomes
                + ", partNumForFailureOperationOutcomes=" + partNumForFailureOperationOutcomes + ", uploadIdForFailureOperationOutcomes="
                + uploadIdForFailureOperationOutcomes + ", dataPacksForFailureOperationOutcomes=" + dataPacksForFailureOperationOutcomes + "]";
    }
}
