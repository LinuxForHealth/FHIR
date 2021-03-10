/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.load.partition.transformer.impl;

import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.bulkdata.load.partition.transformer.PartitionSourceTransformer;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataSource;

/**
 * Converts the Https Type to a BulkData Source
 * There are no sophisticated operations.
 *
 * Usually the list of generated data sources is size 1.
 */
public class HttpsTransformer implements PartitionSourceTransformer {
    @Override
    public List<BulkDataSource> transformToDataSources(String source, String type, String location) throws FHIRException{
        return Arrays.asList(new BulkDataSource(type, location));
    }
}