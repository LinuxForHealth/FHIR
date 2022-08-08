/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.jbatch.export.data;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

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
        return ExportTransientUserData.Builder.builder()
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
            .lastWrittenPageNum(checkPointData.lastWrittenPageNum)
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
        public Builder pageNum(int pageNum) {
            return (Builder) super.pageNum(pageNum);
        }

        @Override
        public Builder lastPageNum(int lastPageNum) {
            return (Builder) super.lastPageNum(lastPageNum);
        }

        @Override
        public Builder partNum(int partNum) {
            return (Builder) super.partNum(partNum);
        }

        @Override
        public Builder uploadId(String uploadId) {
            return (Builder) super.uploadId(uploadId);
        }

        @Override
        public Builder uploadCount(long uploadCount) {
            return (Builder) super.uploadCount(uploadCount);
        }

        @Override
        public Builder cosDataPacks(List<PartETag> cosDataPacks) {
            return (Builder) super.cosDataPacks(cosDataPacks);
        }

        @Override
        public Builder currentUploadResourceNum(long currentUploadResourceNum) {
            return (Builder) super.currentUploadResourceNum(currentUploadResourceNum);
        }

        @Override
        public Builder currentUploadSize(long currentUploadSize) {
            return (Builder) super.currentUploadSize(currentUploadSize);
        }

        @Override
        public Builder totalResourcesNum(long totalResourcesNum) {
            return (Builder) super.totalResourcesNum(totalResourcesNum);
        }

        @Override
        public Builder indexOfCurrentTypeFilter(int indexOfCurrentTypeFilter) {
            return (Builder) super.indexOfCurrentTypeFilter(indexOfCurrentTypeFilter);
        }

        @Override
        public Builder resourceTypeSummary(String resourceTypeSummary) {
            return (Builder) super.resourceTypeSummary(resourceTypeSummary);
        }

        @Override
        public Builder lastWrittenPageNum(int lastWritePageNum) {
            return (Builder) super.lastWrittenPageNum(lastWritePageNum);
        }

        @Override
        public ExportTransientUserData build(){
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
            transientUserData.lastWrittenPageNum = this.lastWritePageNum;
            return transientUserData;
        }
    }

    @Override
    public String toString() {
        return "ExportTransientUserData [bufferStream=" + bufferStream + ", pageNum=" + pageNum + ", lastPageNum=" + lastPageNum + ", partNum=" + partNum
                + ", uploadId=" + uploadId + ", uploadCount=" + uploadCount + ", cosDataPacks=" + cosDataPacks + ", currentUploadResourceNum="
                + currentUploadResourceNum + ", currentUploadSize=" + currentUploadSize + ", totalResourcesNum=" + totalResourcesNum
                + ", indexOfCurrentTypeFilter=" + indexOfCurrentTypeFilter + ", resourceTypeSummary=" + resourceTypeSummary + ", lastWritePageNum="
                + lastWrittenPageNum + "]";
    }
}