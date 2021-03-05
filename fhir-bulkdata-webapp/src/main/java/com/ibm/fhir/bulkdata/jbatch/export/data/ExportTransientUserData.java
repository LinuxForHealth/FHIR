/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.data;

import java.io.ByteArrayOutputStream;

/**
 * Bulk Export Job Transient data
 * @implNote instances of this class are very unlikely to be persisted to disk.
 */
public class ExportTransientUserData extends ExportCheckpointUserData {
    private static final long serialVersionUID = -5892726731783560418L;

    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(2 ^ 16); // 2 ^ 20 = 1 MiB

    protected ExportTransientUserData() {
        super();
    }

    public static ExportTransientUserData fromCheckPointUserData(ExportCheckpointUserData checkPointData) {
        return (ExportTransientUserData)ExportTransientUserData.Builder.builder()
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

    public static class Builder extends ExportCheckpointUserData.Builder {

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public ExportCheckpointUserData build(){
            ExportTransientUserData transientUserData = new ExportTransientUserData();
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

    @Override
    public String toString() {
        return "TransientUserData [bufferStream=" + bufferStream + ", pageNum=" + pageNum + ", lastPageNum=" + lastPageNum + ", partNum=" + partNum
                + ", uploadId=" + uploadId + ", uploadCount=" + uploadCount + ", cosDataPacks=" + cosDataPacks + ", currentUploadResourceNum="
                + currentUploadResourceNum + ", currentUploadSize=" + currentUploadSize + ", totalResourcesNum=" + totalResourcesNum
                + ", indexOfCurrentTypeFilter=" + indexOfCurrentTypeFilter + ", resourceTypeSummary=" + resourceTypeSummary + ", lastWritePageNum="
                + lastWritePageNum + "]";
    }
}