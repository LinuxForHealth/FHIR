/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.common;

import java.io.ByteArrayOutputStream;

/**
 * Bulk export Chunk implementation - job cache data.
 *
 */
public class TransientUserData extends CheckPointUserData {
    private static final long serialVersionUID = -5892726731783560418L;
    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();

    public TransientUserData() {
        super();
    }

    public static TransientUserData fromCheckPointUserData(CheckPointUserData checkPointData) {
        return (TransientUserData)TransientUserData.Builder.builder()
            .pageNum(checkPointData.getPageNum())
            .uploadId(checkPointData.getUploadId())
            .cosDataPacks(checkPointData.getCosDataPacks())
            .partNum(checkPointData.getPartNum())
            .indexOfCurrentTypeFilter(checkPointData.getIndexOfCurrentTypeFilter())
            .resourceTypeSummary(checkPointData.getResourceTypeSummary())
            .totalResourcesNum(checkPointData.getTotalResourcesNum())
            .currentUploadResourceNum(checkPointData.getCurrentUploadResourceNum())
            .currentUploadSize(checkPointData.getCurrentUploadSize())
            .uploadCount(checkPointData.getUploadCount())
            .lastPageNum(checkPointData.getLastPageNum())
            .lastWritePageNum(checkPointData.getLastWritePageNum())
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

}
