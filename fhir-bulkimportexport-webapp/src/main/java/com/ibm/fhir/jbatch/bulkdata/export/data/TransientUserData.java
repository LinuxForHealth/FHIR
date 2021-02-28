/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.data;

import java.io.ByteArrayOutputStream;

/**
 * Bulk export Chunk implementation - job cache data.
 *
 */
public class TransientUserData extends CheckPointUserData {
    private static final long serialVersionUID = -5892726731783560418L;

    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream(2 ^ 16); // 2 ^ 20 = 1 MiB

    protected TransientUserData() {
        super();
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

    @Override
    public String toString() {
        return "TransientUserData [bufferStream=" + bufferStream + ", pageNum=" + pageNum + ", lastPageNum=" + lastPageNum + ", partNum=" + partNum
                + ", uploadId=" + uploadId + ", uploadCount=" + uploadCount + ", cosDataPacks=" + cosDataPacks + ", currentUploadResourceNum="
                + currentUploadResourceNum + ", currentUploadSize=" + currentUploadSize + ", totalResourcesNum=" + totalResourcesNum
                + ", indexOfCurrentTypeFilter=" + indexOfCurrentTypeFilter + ", resourceTypeSummary=" + resourceTypeSummary + ", lastWritePageNum="
                + lastWritePageNum + "]";
    }
}