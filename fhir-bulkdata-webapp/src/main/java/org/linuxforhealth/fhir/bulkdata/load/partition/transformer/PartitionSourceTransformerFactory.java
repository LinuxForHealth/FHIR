/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bulkdata.load.partition.transformer;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import org.linuxforhealth.fhir.bulkdata.common.BulkDataUtils;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl.AzureTransformer;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl.FileTransformer;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl.HttpsTransformer;
import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl.S3Transformer;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataSource;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.OperationFields;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;

/**
 *  Converts the Source from a DataSource to Partitioned Data Source
 */
public class PartitionSourceTransformerFactory {
    private static final FileTransformer fileTransformer = new FileTransformer();
    private static final HttpsTransformer httpsTransformer = new HttpsTransformer();
    private static final S3Transformer s3Transformer = new S3Transformer();
    private static final AzureTransformer azureTransformer = new AzureTransformer();

    private PartitionSourceTransformerFactory() {
        // No Operation
    }

    /**
     * Controls the Transformation and wraps the complicated logic in type specific implementations.
     * @param source
     * @param dataSourcesInfo
     * @return
     */
    public static List<BulkDataSource> transformToSources(String source, String dataSourcesInfo) throws FHIRException {
        JsonArray dataSourceArray = BulkDataUtils.getDataSourcesFromJobInput(dataSourcesInfo);

        List<BulkDataSource> sources = new ArrayList<>();

        for (JsonValue jsonValue : dataSourceArray) {
            JsonObject dataSourceInfo = jsonValue.asJsonObject();

            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            StorageType storageType = adapter.getStorageProviderStorageType(source);
            String type = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_TYPE);
            String location = dataSourceInfo.getString(OperationFields.IMPORT_INPUT_RESOURCE_URL);

            // Convert the types to data sources
            switch (storageType) {
            case HTTPS:
                sources.addAll(httpsTransformer.transformToDataSources(source, type, location));
                break;
            case FILE:
                sources.addAll(fileTransformer.transformToDataSources(source, type, location));
                break;
            case AWSS3:
            case IBMCOS:
                sources.addAll(s3Transformer.transformToDataSources(source, type, location));
                break;
            case AZURE:
                sources.addAll(azureTransformer.transformToDataSources(source, type, location));
                break;
            default:
                break;
            }
        }

        return sources;
    }
}