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

    public TransientUserData(int pageNum, String uploadId, List<PartETag> cosDataPacks, int partNum) {
        super(pageNum, uploadId, cosDataPacks, partNum);
    }

    public static TransientUserData fromCheckPointUserData(CheckPointUserData checkPointData) {
        return new TransientUserData(checkPointData.getPageNum(), checkPointData.getUploadId(),
                checkPointData.getCosDataPacks(), checkPointData.getPartNum());
    }

    public ByteArrayOutputStream getBufferStream() {
        return bufferStream;
    }

}
