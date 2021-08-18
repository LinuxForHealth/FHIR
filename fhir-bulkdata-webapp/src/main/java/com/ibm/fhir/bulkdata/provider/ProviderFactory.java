/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.provider;

import com.ibm.fhir.bulkdata.provider.impl.AzureProvider;
import com.ibm.fhir.bulkdata.provider.impl.FileProvider;
import com.ibm.fhir.bulkdata.provider.impl.HttpsProvider;
import com.ibm.fhir.bulkdata.provider.impl.S3Provider;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Provider Factory picks the ideal provider based on the type.
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
        Provider provider = null;
        switch (storageType) {
        case HTTPS:
            provider = new HttpsProvider(source);
            break;
        case FILE:
            provider = new FileProvider(source);
            break;
        case AWSS3:
        case IBMCOS:
            provider = new S3Provider(source);
            break;
        case AZURE:
            provider = new AzureProvider(source);
            break;
        default:
            throw new IllegalStateException ("Doesn't support data source storage type '" + type + "'!");
        }
        return provider;
    }
}