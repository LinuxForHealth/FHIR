/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

public class ImportTransientUserData extends ImportCheckPointData {
    private static final long serialVersionUID = -2642411992044844735L;
    // Used for import OperationOutcomes, Bulk data import API defines optional links to the OperationOutcomes for each import data source,
    // Data in this buffer stream will be uploaded to storage like IBM COS and allow user to download via the link mentioned above.
    private ByteArrayOutputStream bufferStreamForImportError = new ByteArrayOutputStream();
    private ByteArrayOutputStream bufferStreamForImport = new ByteArrayOutputStream();

    private InputStream inputStream = null;
    private BufferedReader bufferReader = null;

    public ImportTransientUserData(String importPartitionWorkitem, int numOfProcessedResources,
            String importPartitionResourceType, int numOfImportedResource, int numOfImportFailures,
            String uniqueIDForImportFailureOperationOutcomes, String uniqueIDForImportOperationOutcome,
            String uploadIdForOperationOutcomes, List<PartETag> dataPacksForOperationOutcomes, int partNumForOperationOutcomes,
            String uploadIdForFailureOperationOutcomes, List<PartETag> dataPacksForFailureOperationOutcomes, int partNumForFailureOperationOutcomes) {
        super(importPartitionWorkitem, numOfProcessedResources, importPartitionResourceType,
                numOfImportedResource, numOfImportFailures, uniqueIDForImportFailureOperationOutcomes, uniqueIDForImportOperationOutcome,
                uploadIdForOperationOutcomes, dataPacksForOperationOutcomes, partNumForOperationOutcomes,
                uploadIdForFailureOperationOutcomes, dataPacksForFailureOperationOutcomes, partNumForFailureOperationOutcomes);
    }

    public ImportTransientUserData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType) {
        super(importPartitionWorkitem, numOfProcessedResources, importPartitionResourceType);
    }

    public ByteArrayOutputStream getBufferStreamForImportError() {
        return bufferStreamForImportError;
    }

    public ByteArrayOutputStream getBufferStreamForImport() {
        return bufferStreamForImport;
    }

    public static ImportTransientUserData fromImportCheckPointData(ImportCheckPointData importCheckPointData) {
        return new ImportTransientUserData(importCheckPointData.getImportPartitionWorkitem(),
                importCheckPointData.getNumOfProcessedResources(), importCheckPointData.getImportPartitionResourceType(),
                importCheckPointData.getNumOfImportedResources(), importCheckPointData.getNumOfImportFailures(),
                importCheckPointData.getUniqueIDForImportFailureOperationOutcomes(), importCheckPointData.getUniqueIDForImportOperationOutcomes(),
                importCheckPointData.getUploadIdForOperationOutcomes(), importCheckPointData.getDataPacksForOperationOutcomes(), importCheckPointData.getPartNumForOperationOutcomes(),
                importCheckPointData.getUploadIdForFailureOperationOutcomes(), importCheckPointData.getDataPacksForFailureOperationOutcomes(), importCheckPointData.getPartNumForFailureOperationOutcomes());
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

}
