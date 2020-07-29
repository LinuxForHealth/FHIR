/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bulk export Chunk implementation - job cache data.
 *
 */
public class TransientUserData extends CheckPointUserData {
    private final static Logger logger = Logger.getLogger(TransientUserData.class.getName());
    private static final long serialVersionUID = -5892726731783560418L;

    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(2 ^ 16); // 2 ^ 20 = 1 MiB
    private Path tmpFile;

    protected TransientUserData() {
        super();
        try {
            tmpFile = Files.createTempDirectory("fhir");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to create temporary file", e);
            throw new RuntimeException(e);
        }
    }

    public static TransientUserData fromCheckPointUserData(CheckPointUserData checkPointData) {
        return (TransientUserData)TransientUserData.Builder.builder()
            .pageNum(checkPointData.pageNum)
            .uploadId(checkPointData.uploadId)
            .cosDataPacks(checkPointData.cosDataPacks)
            .partNum(checkPointData.partNum)
            .indexOfCurrentTypeFilter(checkPointData.indexOfCurrentTypeFilter)
            .resourceTypeSummary(checkPointData.resourceTypeSummary)
            .totalResourcesNum(checkPointData.totalResourcesNum)
            .currentUploadResourceNum(checkPointData.currentUploadResourceNum)
            .currentUploadSize(checkPointData.currentUploadSize)
            .uploadCount(checkPointData.uploadCount)
            .lastPageNum(checkPointData.lastPageNum)
            .lastWritePageNum(checkPointData.lastWritePageNum)
            .build();
    }

    public ByteArrayOutputStream getBufferStream() {
        return bufferStream;
    }

    public Path getTempFile() {
        return tmpFile;
    }

    public static class Builder extends CheckPointUserData.Builder {

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public CheckPointUserData build(){
            TransientUserData transientUserData = new TransientUserData();
            transientUserData.pageNum  = this.pageNum;
            transientUserData.lastPageNum = this.lastPageNum;
            transientUserData.partNum = this.partNum;
            transientUserData.uploadId = this.uploadId;
            transientUserData.uploadCount = this.uploadCount;
            transientUserData.cosDataPacks = this.cosDataPacks;
            transientUserData.currentUploadResourceNum = this.currentUploadResourceNum;
            transientUserData.currentUploadSize = this.currentUploadSize;
            transientUserData.totalResourcesNum = this.totalResourcesNum;
            transientUserData.indexOfCurrentTypeFilter = this.indexOfCurrentTypeFilter;
            transientUserData.resourceTypeSummary = this.resourceTypeSummary;
            transientUserData.lastWritePageNum = this.lastWritePageNum;
            return transientUserData;
        }
    }
}
