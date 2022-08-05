/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.load.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

public class ImportTransientUserData extends ImportCheckPointData {

    private static final long serialVersionUID = -2642411992044844735L;
    // Used for import OperationOutcomes, Bulk data import API defines optional links to the OperationOutcomes for each
    // import data source,
    // Data in this buffer stream will be uploaded to storage like IBM COS and allow user to download via the link
    // mentioned above.
    private ByteArrayOutputStream bufferStreamForImportError = new ByteArrayOutputStream();
    private ByteArrayOutputStream bufferStreamForImport = new ByteArrayOutputStream();

    private InputStream inputStream = null;
    private BufferedReader bufferReader = null;

    protected ImportTransientUserData() {
        super();
    }

    public ByteArrayOutputStream getBufferStreamForImportError() {
        return bufferStreamForImportError;
    }

    public ByteArrayOutputStream getBufferStreamForImport() {
        return bufferStreamForImport;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public BufferedReader getBufferReader() {
        return bufferReader;
    }

    public void setBufferReader(BufferedReader bufferReader) {
        this.bufferReader = bufferReader;
    }

    /**
     * Translate from CheckPoint to Transient User Data
     *
     * @param importCheckPointData
     * @return
     */
    public static ImportTransientUserData fromImportCheckPointData(ImportCheckPointData importCheckPointData) {
        return ImportTransientUserData.Builder.builder()
                .importPartitionWorkitem(importCheckPointData.importPartitionWorkitem)
                .matrixWorkItem(importCheckPointData.matrixWorkItem)
                .numOfProcessedResources(importCheckPointData.numOfProcessedResources)
                .importPartitionResourceType(importCheckPointData.importPartitionResourceType)
                .numOfImportedResources(importCheckPointData.numOfImportedResources)
                .numOfImportFailures(importCheckPointData.numOfImportFailures)
                .uniqueIDForImportFailureOperationOutcomes(importCheckPointData.uniqueIDForImportFailureOperationOutcomes)
                .uniqueIDForImportOperationOutcomes(importCheckPointData.uniqueIDForImportOperationOutcomes)
                .uploadIdForOperationOutcomes(importCheckPointData.uploadIdForOperationOutcomes)
                .dataPacksForOperationOutcomes(importCheckPointData.dataPacksForOperationOutcomes)
                .partNumForOperationOutcomes(importCheckPointData.partNumForOperationOutcomes)
                .uploadIdForFailureOperationOutcomes(importCheckPointData.uploadIdForFailureOperationOutcomes)
                .dataPacksForFailureOperationOutcomes(importCheckPointData.dataPacksForFailureOperationOutcomes)
                .partNumForFailureOperationOutcomes(importCheckPointData.partNumForFailureOperationOutcomes)
                .totalReadMilliSeconds(importCheckPointData.totalReadMilliSeconds)
                .totalValidationMilliSeconds(importCheckPointData.totalValidationMilliSeconds)
                .totalWriteMilliSeconds(importCheckPointData.totalWriteMilliSeconds)
                .importFileSize(importCheckPointData.importFileSize)
                .inFlyRateBeginMilliSeconds(importCheckPointData.inFlyRateBeginMilliSeconds)
                .currentBytes(importCheckPointData.currentBytes)
                .build();
    }

    public static class Builder extends ImportCheckPointData.Builder {

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public Builder importPartitionWorkitem(String importPartitionWorkitem) {
            return (Builder) super.importPartitionWorkitem(importPartitionWorkitem);
        }

        @Override
        public Builder matrixWorkItem(String matrixWorkItem) {
            return (Builder) super.matrixWorkItem(matrixWorkItem);
        }

        @Override
        public Builder numOfProcessedResources(long numOfProcessedResources) {
            return (Builder) super.numOfProcessedResources(numOfProcessedResources);
        }

        @Override
        public Builder importPartitionResourceType(String importPartitionResourceType) {
            return (Builder) super.importPartitionResourceType(importPartitionResourceType);
        }

        @Override
        public Builder numOfImportedResources(long numOfImportedResources) {
            return (Builder) super.numOfImportedResources(numOfImportedResources);
        }

        @Override
        public Builder numOfImportFailures(long numOfImportFailures) {
            return (Builder) super.numOfImportFailures(numOfImportFailures);
        }

        @Override
        public Builder uniqueIDForImportFailureOperationOutcomes(String uniqueIDForImportFailureOperationOutcomes) {
            return (Builder) super.uniqueIDForImportFailureOperationOutcomes(uniqueIDForImportFailureOperationOutcomes);
        }

        @Override
        public Builder uniqueIDForImportOperationOutcomes(String uniqueIDForImportOperationOutcomes) {
            return (Builder) super.uniqueIDForImportOperationOutcomes(uniqueIDForImportOperationOutcomes);
        }

        @Override
        public Builder uploadIdForOperationOutcomes(String uploadIdForOperationOutcomes) {
            return (Builder) super.uploadIdForOperationOutcomes(uploadIdForOperationOutcomes);
        }

        @Override
        public Builder dataPacksForOperationOutcomes(List<PartETag> dataPacksForOperationOutcomes) {
            return (Builder) super.dataPacksForOperationOutcomes(dataPacksForOperationOutcomes);
        }

        @Override
        public Builder partNumForOperationOutcomes(int partNumForOperationOutcomes) {
            return (Builder) super.partNumForOperationOutcomes(partNumForOperationOutcomes);
        }

        @Override
        public Builder uploadIdForFailureOperationOutcomes(String uploadIdForFailureOperationOutcomes) {
            return (Builder) super.uploadIdForFailureOperationOutcomes(uploadIdForFailureOperationOutcomes);
        }

        @Override
        public Builder dataPacksForFailureOperationOutcomes(List<PartETag> dataPacksForFailureOperationOutcomes) {
            return (Builder) super.dataPacksForFailureOperationOutcomes(dataPacksForFailureOperationOutcomes);
        }

        @Override
        public Builder partNumForFailureOperationOutcomes(int partNumForFailureOperationOutcomes) {
            return (Builder) super.partNumForFailureOperationOutcomes(partNumForFailureOperationOutcomes);
        }

        @Override
        public Builder totalReadMilliSeconds(long totalReadMilliSeconds) {
            return (Builder) super.totalReadMilliSeconds(totalReadMilliSeconds);
        }

        @Override
        public Builder totalValidationMilliSeconds(long totalValidationMilliSeconds) {
            return (Builder) super.totalValidationMilliSeconds(totalValidationMilliSeconds);
        }

        @Override
        public Builder importFileSize(long importFileSize) {
            return (Builder) super.importFileSize(importFileSize);
        }

        @Override
        public Builder totalWriteMilliSeconds(long totalWriteMilliSeconds) {
            return (Builder) super.totalWriteMilliSeconds(totalWriteMilliSeconds);
        }

        @Override
        public Builder inFlyRateBeginMilliSeconds(long inFlyRateBeginMilliSeconds) {
            return (Builder) super.inFlyRateBeginMilliSeconds(inFlyRateBeginMilliSeconds);
        }

        @Override
        public Builder currentBytes(long currentBytes) {
            return (Builder) super.currentBytes(currentBytes);
        }

        @Override
        public ImportTransientUserData build() {
            ImportTransientUserData importTransientUserData = new ImportTransientUserData();
            importTransientUserData.importPartitionWorkitem = this.importPartitionWorkitem;
            importTransientUserData.matrixWorkItem = this.matrixWorkItem;
            importTransientUserData.numOfProcessedResources = this.numOfProcessedResources;
            importTransientUserData.importPartitionResourceType = this.importPartitionResourceType;
            importTransientUserData.numOfImportedResources = this.numOfImportedResources;
            importTransientUserData.numOfImportFailures = this.numOfImportFailures;
            importTransientUserData.uniqueIDForImportFailureOperationOutcomes = this.uniqueIDForImportFailureOperationOutcomes;
            importTransientUserData.uniqueIDForImportOperationOutcomes = this.uniqueIDForImportOperationOutcomes;
            importTransientUserData.uploadIdForOperationOutcomes = this.uploadIdForOperationOutcomes;
            importTransientUserData.dataPacksForOperationOutcomes = new ArrayList<>(this.dataPacksForOperationOutcomes);
            importTransientUserData.partNumForOperationOutcomes = this.partNumForOperationOutcomes;
            importTransientUserData.uploadIdForFailureOperationOutcomes = this.uploadIdForFailureOperationOutcomes;
            importTransientUserData.dataPacksForFailureOperationOutcomes = new ArrayList<>(
                    this.dataPacksForFailureOperationOutcomes);
            importTransientUserData.partNumForFailureOperationOutcomes = this.partNumForFailureOperationOutcomes;
            importTransientUserData.totalReadMilliSeconds = this.totalReadMilliSeconds;
            importTransientUserData.totalValidationMilliSeconds = this.totalValidationMilliSeconds;
            importTransientUserData.totalWriteMilliSeconds = this.totalWriteMilliSeconds;
            importTransientUserData.importFileSize = this.importFileSize;
            importTransientUserData.inFlyRateBeginMilliSeconds = this.inFlyRateBeginMilliSeconds;
            importTransientUserData.currentBytes = this.currentBytes;
            return importTransientUserData;
        }
    }

    @Override
    public String toString() {
        return "ImportTransientUserData [bufferStreamForImportError=" + bufferStreamForImportError + ", bufferStreamForImport=" + bufferStreamForImport
                + ", matrix=" + matrixWorkItem
                + ", inputStream=" + inputStream + ", bufferReader=" + bufferReader + ", importPartitionWorkitem=" + importPartitionWorkitem
                + ", numOfProcessedResources=" + numOfProcessedResources + ", numOfImportedResources=" + numOfImportedResources + ", numOfImportFailures="
                + numOfImportFailures + ", totalReadMilliSeconds=" + totalReadMilliSeconds + ", totalWriteMilliSeconds=" + totalWriteMilliSeconds
                + ", totalValidationMilliSeconds=" + totalValidationMilliSeconds + ", importFileSize=" + importFileSize + ", inFlyRateBeginMilliSeconds="
                + inFlyRateBeginMilliSeconds + ", numOfToBeImported=" + numOfToBeImported + ", numOfParseFailures=" + numOfParseFailures
                + ", importPartitionResourceType=" + importPartitionResourceType + ", uniqueIDForImportOperationOutcomes=" + uniqueIDForImportOperationOutcomes
                + ", partNumForOperationOutcomes=" + partNumForOperationOutcomes + ", uploadIdForOperationOutcomes=" + uploadIdForOperationOutcomes
                + ", dataPacksForOperationOutcomes=" + dataPacksForOperationOutcomes + ", uniqueIDForImportFailureOperationOutcomes="
                + uniqueIDForImportFailureOperationOutcomes + ", partNumForFailureOperationOutcomes=" + partNumForFailureOperationOutcomes
                + ", uploadIdForFailureOperationOutcomes=" + uploadIdForFailureOperationOutcomes + ", dataPacksForFailureOperationOutcomes="
                + dataPacksForFailureOperationOutcomes + "]";
    }
}