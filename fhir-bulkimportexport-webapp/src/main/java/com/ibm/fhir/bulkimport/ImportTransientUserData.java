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
    private ByteArrayOutputStream bufferStream4ImportError = new ByteArrayOutputStream();
    private ByteArrayOutputStream bufferStream4Import = new ByteArrayOutputStream();

    private InputStream inputStream = null;
    private BufferedReader bufferReader = null;

    public ImportTransientUserData(String importPartitionWorkitem, int numOfProcessedResources,
            String importPartitionResourceType, int numOfImportedResource, int numOfImportFailures,
            String uniqueID4ImportFailureOperationOutcomes, String uniqueID4ImportOperationOutcome,
            String uploadId4OperationOutcomes, List<PartETag> dataPacks4OperationOutcomes, int partNum4OperationOutcomes,
            String uploadId4FailureOperationOutcomes, List<PartETag> dataPacks4FailureOperationOutcomes, int partNum4FailureOperationOutcomes) {
        super(importPartitionWorkitem, numOfProcessedResources, importPartitionResourceType,
                numOfImportedResource, numOfImportFailures, uniqueID4ImportFailureOperationOutcomes, uniqueID4ImportOperationOutcome,
                uploadId4OperationOutcomes, dataPacks4OperationOutcomes, partNum4OperationOutcomes,
                uploadId4FailureOperationOutcomes, dataPacks4FailureOperationOutcomes, partNum4FailureOperationOutcomes);
    }

    public ImportTransientUserData(String importPartitionWorkitem, int numOfProcessedResources, String importPartitionResourceType) {
        super(importPartitionWorkitem, numOfProcessedResources, importPartitionResourceType);
    }

    public ByteArrayOutputStream getBufferStream4ImportError() {
        return bufferStream4ImportError;
    }

    public ByteArrayOutputStream getBufferStream4Import() {
        return bufferStream4Import;
    }

    public static ImportTransientUserData fromImportCheckPointData(ImportCheckPointData importCheckPointData) {
        return new ImportTransientUserData(importCheckPointData.getImportPartitionWorkitem(),
                importCheckPointData.getNumOfProcessedResources(), importCheckPointData.getImportPartitionResourceType(),
                importCheckPointData.getNumOfImportedResources(), importCheckPointData.getNumOfImportFailures(),
                importCheckPointData.getUniqueID4ImportFailureOperationOutcomes(), importCheckPointData.getUniqueID4ImportOperationOutcomes(),
                importCheckPointData.getUploadId4OperationOutcomes(), importCheckPointData.getDataPacks4OperationOutcomes(), importCheckPointData.getPartNum4OperationOutcomes(),
                importCheckPointData.getUploadId4FailureOperationOutcomes(), importCheckPointData.getDataPacks4FailureOperationOutcomes(), importCheckPointData.getPartNum4FailureOperationOutcomes());
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
