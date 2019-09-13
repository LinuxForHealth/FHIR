/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.bulkexport;

import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

/**
 * Bulk export Chunk implementation - job progress data.
 * 
 * @author Albert Wang
 */
public class TransientUserData implements java.io.Serializable{   
    private static final long serialVersionUID = 5722923276076940517L;
    private int pageNum;
    private int lastPageNum;
    private int currentPartSize;
    private int partNum;
    private String uploadId;
    private boolean isSingleCosObject = false;    
    private List<PartETag> cosDataPacks;
    
    public TransientUserData(int pageNum, int currentPartSize, String uploadId, List<PartETag> cosDataPacks, int partNum) {
        super();
        this.pageNum = pageNum;
        this.currentPartSize = currentPartSize;
        this.uploadId = uploadId;
        this.cosDataPacks = cosDataPacks;
        this.partNum = partNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getCurrentPartSize() {
        return currentPartSize;
    }

    public void setCurrentPartSize(int currentPartSize) {
        this.currentPartSize = currentPartSize;
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
     
}
