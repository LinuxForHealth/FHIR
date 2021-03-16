/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.provider;

import com.ibm.fhir.bulkdata.provider.impl.FileProvider;
import com.ibm.fhir.bulkdata.provider.impl.HttpsProvider;
import com.ibm.fhir.bulkdata.provider.impl.S3Provider;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Wrapper
 */
public class ProviderFactory {

    private ProviderFactory() {
        // No Operation
    }

    /**
     * get the SourceWrapper based on the type
     *
     * @return
     * @throws Exception
     */
    public static Provider getSourceWrapper(String source, String type) throws Exception {
        StorageType storageType = StorageType.from(type);
        Provider wrapper = null;
        switch (storageType) {
        case HTTPS:
            wrapper = new HttpsProvider(source);
            break;
        case FILE:
            wrapper = new FileProvider(source);
            break;
        case AWSS3:
        case IBMCOS:
            wrapper = new S3Provider(source);
            break;
        default:
            throw new IllegalStateException ("Doesn't support data source storage type '" + type + "'!");
        }
        return wrapper;
    }
}