/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.common;

import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

/**
 * Bulk export Chunk implementation - job check point data.
 *
 */
public class CheckPointUserData implements java.io.Serializable {
    private static final long serialVersionUID = 5722923276076940517L;
    private int pageNum;
    private int lastPageNum;
    private int partNum;
    private String uploadId;
    private boolean isSingleCosObject = false;
    private List<PartETag> cosDataPacks;
    private int currentPartResourceNum = 0;
    private int totalResourcesNum = 0;
    // One resource type can have 0 to multiple typeFilters, indexOfCurrentTypeFilter is used to tell the currently processed typeFilter.
    private int indexOfCurrentTypeFilter;
    // Partition status for the exported resources, e.g, Patient[1000,1000,200]
    private String resourceTypeSummary = null;
    // Used to mark the complete of the partition.
    private boolean isMoreToExport = true;

    public CheckPointUserData(int pageNum, String uploadId, List<PartETag> cosDataPacks, int partNum, int indexOfCurrentTypeFilter,
            String resourceTypeSummary, int totalResourcesNum, int currentPartResourceNum) {
        super();
        this.pageNum = pageNum;
        this.uploadId = uploadId;
        this.cosDataPacks = cosDataPacks;
        this.partNum = partNum;
        this.indexOfCurrentTypeFilter = indexOfCurrentTypeFilter;
        this.resourceTypeSummary = resourceTypeSummary;
        this.totalResourcesNum = totalResourcesNum;
        this.currentPartResourceNum = currentPartResourceNum;
    }

    public static CheckPointUserData fromTransientUserData(TransientUserData userData) {
        return new CheckPointUserData(userData.getPageNum(), userData.getUploadId(), userData.getCosDataPacks(),
                userData.getPartNum(), userData.getIndexOfCurrentTypeFilter(), userData.getResourceTypeSummary(),
                userData.getTotalResourcesNum(), userData.getCurrentPartResourceNum());
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

    public void setCosDataPacks(List<PartETag> cosDataPacks) {
        this.cosDataPacks = cosDataPacks;
    }

    public boolean isSingleCosObject() {
        return isSingleCosObject;
    }

    public void setSingleCosObject(boolean isSingleCosObject) {
        this.isSingleCosObject = isSingleCosObject;
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

    public int getCurrentPartResourceNum() {
        return currentPartResourceNum;
    }

    public void setCurrentPartResourceNum(int currentPartResourceNum) {
        this.currentPartResourceNum = currentPartResourceNum;
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

}
