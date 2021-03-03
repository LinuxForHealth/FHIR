/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.load.data;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

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
        return (ImportTransientUserData) ImportTransientUserData.Builder.builder().importPartitionWorkitem(importCheckPointData.importPartitionWorkitem).numOfProcessedResources(importCheckPointData.numOfProcessedResources).importPartitionResourceType(importCheckPointData.importPartitionResourceType).numOfImportedResources(importCheckPointData.numOfImportedResources).numOfImportFailures(importCheckPointData.numOfImportFailures).uniqueIDForImportFailureOperationOutcomes(importCheckPointData.uniqueIDForImportFailureOperationOutcomes).uniqueIDForImportOperationOutcomes(importCheckPointData.uniqueIDForImportOperationOutcomes).uploadIdForOperationOutcomes(importCheckPointData.uploadIdForOperationOutcomes).dataPacksForOperationOutcomes(importCheckPointData.dataPacksForOperationOutcomes).partNumForOperationOutcomes(importCheckPointData.partNumForOperationOutcomes).uploadIdForFailureOperationOutcomes(importCheckPointData.uploadIdForFailureOperationOutcomes).dataPacksForFailureOperationOutcomes(importCheckPointData.dataPacksForFailureOperationOutcomes).partNumForFailureOperationOutcomes(importCheckPointData.partNumForFailureOperationOutcomes).totalReadMilliSeconds(importCheckPointData.totalReadMilliSeconds).totalValidationMilliSeconds(importCheckPointData.totalValidationMilliSeconds).totalWriteMilliSeconds(importCheckPointData.totalWriteMilliSeconds).importFileSize(importCheckPointData.importFileSize).inFlyRateBeginMilliSeconds(importCheckPointData.inFlyRateBeginMilliSeconds).build();
    }

    public static class Builder extends ImportCheckPointData.Builder {

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public ImportCheckPointData build() {
            ImportTransientUserData importTransientUserData = new ImportTransientUserData();
            importTransientUserData.importPartitionWorkitem = this.importPartitionWorkitem;
            importTransientUserData.numOfProcessedResources = this.numOfProcessedResources;
            importTransientUserData.importPartitionResourceType = this.importPartitionResourceType;
            importTransientUserData.numOfImportedResources = this.numOfImportedResources;
            importTransientUserData.numOfImportFailures = this.numOfImportFailures;
            importTransientUserData.uniqueIDForImportFailureOperationOutcomes = this.uniqueIDForImportFailureOperationOutcomes;
            importTransientUserData.uniqueIDForImportOperationOutcomes = this.uniqueIDForImportOperationOutcomes;
            importTransientUserData.uploadIdForOperationOutcomes = this.uploadIdForOperationOutcomes;
            importTransientUserData.dataPacksForOperationOutcomes = this.dataPacksForOperationOutcomes;
            importTransientUserData.partNumForOperationOutcomes = this.partNumForOperationOutcomes;
            importTransientUserData.uploadIdForFailureOperationOutcomes = this.uploadIdForFailureOperationOutcomes;
            importTransientUserData.dataPacksForFailureOperationOutcomes = this.dataPacksForFailureOperationOutcomes;
            importTransientUserData.partNumForFailureOperationOutcomes = this.partNumForFailureOperationOutcomes;
            importTransientUserData.totalReadMilliSeconds = this.totalReadMilliSeconds;
            importTransientUserData.totalValidationMilliSeconds = this.totalValidationMilliSeconds;
            importTransientUserData.totalWriteMilliSeconds = this.totalWriteMilliSeconds;
            importTransientUserData.importFileSize = this.importFileSize;
            importTransientUserData.inFlyRateBeginMilliSeconds = this.inFlyRateBeginMilliSeconds;
            return importTransientUserData;
        }
    }

    @Override
    public String toString() {
        return "ImportTransientUserData [bufferStreamForImportError=" + bufferStreamForImportError + ", bufferStreamForImport=" + bufferStreamForImport
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