/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.data;

import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

/**
 * Bulk Export Job Checkpoint data.
 */
public class CheckPointUserData implements java.io.Serializable {
    private static final long serialVersionUID = 5722923276076940517L;
    protected int pageNum;
    protected int lastPageNum;
    protected int partNum;
    protected String uploadId;
    protected int uploadCount = 1;
    protected List<PartETag> cosDataPacks;
    protected int currentUploadResourceNum = 0;
    protected int currentUploadSize = 0;
    private boolean isFinishCurrentUpload = false;
    protected int totalResourcesNum = 0;
    // One resource type can have 0 to multiple typeFilters, indexOfCurrentTypeFilter is used to tell the currently processed typeFilter.
    protected int indexOfCurrentTypeFilter;
    // Partition status for the exported resources, e.g, Patient[1000,1000,200]
    protected String resourceTypeSummary = null;
    // Used to mark the complete of the partition.
    private boolean isMoreToExport = true;
    protected int lastWritePageNum;

    protected CheckPointUserData() {
        super();
    }

    public static CheckPointUserData fromTransientUserData(TransientUserData userData) {
        return CheckPointUserData.Builder.builder()
            .pageNum(userData.pageNum)
            .uploadId(userData.uploadId)
            .cosDataPacks(userData.cosDataPacks)
            .partNum(userData.partNum)
            .indexOfCurrentTypeFilter(userData.indexOfCurrentTypeFilter)
            .resourceTypeSummary(userData.resourceTypeSummary)
            .totalResourcesNum(userData.totalResourcesNum)
            .currentUploadResourceNum(userData.currentUploadResourceNum)
            .currentUploadSize(userData.currentUploadSize)
            .uploadCount(userData.uploadCount)
            .lastPageNum(userData.lastPageNum)
            .lastWritePageNum(userData.lastWritePageNum)
            .build();
    }

    public static class Builder {
        protected int pageNum;
        protected int lastPageNum;
        protected int partNum;
        protected String uploadId;
        protected int uploadCount = 1;
        protected List<PartETag> cosDataPacks;
        protected int currentUploadResourceNum = 0;
        protected int currentUploadSize = 0;
        protected int totalResourcesNum = 0;
        protected int indexOfCurrentTypeFilter;
        protected String resourceTypeSummary = null;
        protected int lastWritePageNum;

        public Builder() {
            super();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder pageNum(int pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public Builder lastPageNum(int lastPageNum) {
            this.lastPageNum = lastPageNum;
            return this;
        }

        public Builder partNum(int partNum) {
            this.partNum = partNum;
            return this;
        }

        public Builder uploadId(String uploadId) {
            this.uploadId = uploadId;
            return this;
        }

        public Builder uploadCount(int uploadCount) {
            this.uploadCount = uploadCount;
            return this;
        }

        public Builder cosDataPacks(List<PartETag> cosDataPacks) {
            this.cosDataPacks = cosDataPacks;
            return this;
        }

        public Builder currentUploadResourceNum(int currentUploadResourceNum) {
            this.currentUploadResourceNum = currentUploadResourceNum;
            return this;
        }

        public Builder currentUploadSize(int currentUploadSize) {
            this.currentUploadSize = currentUploadSize;
            return this;
        }

        public Builder totalResourcesNum(int totalResourcesNum) {
            this.totalResourcesNum = totalResourcesNum;
            return this;
        }

        public Builder indexOfCurrentTypeFilter(int indexOfCurrentTypeFilter) {
            this.indexOfCurrentTypeFilter = indexOfCurrentTypeFilter;
            return this;
        }

        public Builder resourceTypeSummary(String resourceTypeSummary) {
            this.resourceTypeSummary = resourceTypeSummary;
            return this;
        }

        public Builder lastWritePageNum(int lastWritePageNum) {
            this.lastWritePageNum = lastWritePageNum;
            return this;
        }

        public CheckPointUserData build(){
            CheckPointUserData checkPointUserData = new CheckPointUserData();
            checkPointUserData.pageNum  = this.pageNum;
            checkPointUserData.lastPageNum = this.lastPageNum;
            checkPointUserData.partNum = this.partNum;
            checkPointUserData.uploadId = this.uploadId;
            checkPointUserData.uploadCount = this.uploadCount;
            checkPointUserData.cosDataPacks = this.cosDataPacks;
            checkPointUserData.currentUploadResourceNum = this.currentUploadResourceNum;
            checkPointUserData.currentUploadSize = this.currentUploadSize;
            checkPointUserData.totalResourcesNum = this.totalResourcesNum;
            checkPointUserData.indexOfCurrentTypeFilter = this.indexOfCurrentTypeFilter;
            checkPointUserData.resourceTypeSummary = this.resourceTypeSummary;
            checkPointUserData.lastWritePageNum = this.lastWritePageNum;

            return checkPointUserData;
        }

    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<PartETag> getCosDataPacks() {
        return cosDataPacks;
    }

    public int getPartNum() {
        return partNum;
    }

    public void setPartNum(int partNum) {
        this.partNum = partNum;
    }

    public int getLastPageNum() {
        return lastPageNum;
    }

    public void setLastPageNum(int lastPageNum) {
        this.lastPageNum = lastPageNum;
    }

    public int getCurrentUploadResourceNum() {
        return currentUploadResourceNum;
    }

    public void setCurrentUploadResourceNum(int currentUploadResourceNum) {
        this.currentUploadResourceNum = currentUploadResourceNum;
    }

    public void addCurrentUploadResourceNum(int currentUploadResourceNum) {
        this.currentUploadResourceNum += currentUploadResourceNum;
    }

    public int getIndexOfCurrentTypeFilter() {
        return indexOfCurrentTypeFilter;
    }

    public void setIndexOfCurrentTypeFilter(int indexOfCurrentTypeFilter) {
        this.indexOfCurrentTypeFilter = indexOfCurrentTypeFilter;
    }

    public String getResourceTypeSummary() {
        return resourceTypeSummary;
    }

    public void setResourceTypeSummary(String resourceTypeSummary) {
        this.resourceTypeSummary = resourceTypeSummary;
    }

    public boolean isMoreToExport() {
        return isMoreToExport;
    }

    public void setMoreToExport(boolean isMoreToExport) {
        this.isMoreToExport = isMoreToExport;
    }

    public int getTotalResourcesNum() {
        return totalResourcesNum;
    }

    public void setTotalResourcesNum(int totalResourcesNum) {
        this.totalResourcesNum = totalResourcesNum;
    }

    public void addTotalResourcesNum(int totalResourcesNum) {
        this.totalResourcesNum += totalResourcesNum;
    }

    public int getCurrentUploadSize() {
        return currentUploadSize;
    }

    public void setCurrentUploadSize(int currentUploadSize) {
        this.currentUploadSize = currentUploadSize;
    }

    public void addCurrentUploadSize(int currentUploadSize) {
        this.currentUploadSize += currentUploadSize;
    }

    public boolean isFinishCurrentUpload() {
        return isFinishCurrentUpload;
    }

    public void setFinishCurrentUpload(boolean isFinishCurrentUpload) {
        this.isFinishCurrentUpload = isFinishCurrentUpload;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public int getLastWritePageNum() {
        return lastWritePageNum;
    }

    /**
     * @param lastWritePageNum the last page of search results that was exported
     */
    public void setLastWritePageNum(int lastWritePageNum) {
        this.lastWritePageNum = lastWritePageNum;
    }

    @Override
    public String toString() {
        return "CheckPointUserData [pageNum=" + pageNum + ", lastPageNum=" + lastPageNum + ", partNum=" + partNum + ", uploadId=" + uploadId + ", uploadCount="
                + uploadCount + ", cosDataPacks=" + cosDataPacks + ", currentUploadResourceNum=" + currentUploadResourceNum + ", currentUploadSize="
                + currentUploadSize + ", isFinishCurrentUpload=" + isFinishCurrentUpload + ", totalResourcesNum=" + totalResourcesNum
                + ", indexOfCurrentTypeFilter=" + indexOfCurrentTypeFilter + ", resourceTypeSummary=" + resourceTypeSummary + ", isMoreToExport="
                + isMoreToExport + ", lastWritePageNum=" + lastWritePageNum + "]";
    }

}
