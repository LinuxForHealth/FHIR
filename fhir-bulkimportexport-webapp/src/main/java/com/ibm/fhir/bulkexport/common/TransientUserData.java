/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.common;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;

/**
 * Bulk export Chunk implementation - job cache data.
 *
 */
public class TransientUserData extends CheckPointUserData {
    private static final long serialVersionUID = -5892726731783560418L;
    private ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();

    public TransientUserData(int pageNum, String uploadId, List<PartETag> cosDataPacks, int partNum, int indexOfCurrentTypeFilter,
            String resourceTypeSummary, int totalResourcesNum, int currentPartResourceNum, int currentPartSize, int uploadCount) {
        super(pageNum, uploadId, cosDataPacks, partNum, indexOfCurrentTypeFilter, resourceTypeSummary,
                totalResourcesNum, currentPartResourceNum, currentPartSize, uploadCount);
    }

    public static TransientUserData fromCheckPointUserData(CheckPointUserData checkPointData) {
        return new TransientUserData(checkPointData.getPageNum(), checkPointData.getUploadId(),
                checkPointData.getCosDataPacks(), checkPointData.getPartNum(), checkPointData.getIndexOfCurrentTypeFilter(),
                checkPointData.getResourceTypeSummary(), checkPointData.getTotalResourcesNum(),
                checkPointData.getCurrentUploadResourceNum(), checkPointData.getCurrentUploadSize(),
                checkPointData.getUploadCount());
    }

    public ByteArrayOutputStream getBufferStream() {
        return bufferStream;
    }

}
