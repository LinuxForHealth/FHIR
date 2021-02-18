/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.source.type;

import com.ibm.fhir.jbatch.bulkdata.source.type.impl.FileWrapper;
import com.ibm.fhir.jbatch.bulkdata.source.type.impl.HttpsWrapper;
import com.ibm.fhir.jbatch.bulkdata.source.type.impl.S3Wrapper;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Wrapper
 */
public class SourceWrapperFactory {

    private SourceWrapperFactory() {
        // No Operation
    }

    /**
     * get the SourceWrapper based on the type
     *
     * @return
     * @throws Exception
     */
    public static SourceWrapper getSourceWrapper(String source, String type) throws Exception {
        StorageType storageType = StorageType.from(type);
        SourceWrapper wrapper = null;
        switch (storageType) {
        case HTTPS:
            wrapper = new HttpsWrapper(source);
            break;
        case FILE:
            wrapper = new FileWrapper(source);
            break;
        case AWSS3:
        case IBMCOS:
            wrapper = new S3Wrapper(source);
            break;
        default:
            throw new IllegalStateException ("Doesn't support data source storage type '" + type + "'!");
        }
        return wrapper;
    }
}