/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bulkdata.load.partition.transformer.impl;

import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.bulkdata.load.partition.transformer.PartitionSourceTransformer;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.BulkDataSource;

/**
 * Converts the File Type to a BulkData Source
 * There are no sophisticated operations.
 *
 * Usually the list of generated datasources is size 1.
 */
public class FileTransformer implements PartitionSourceTransformer {

    @Override
    public List<BulkDataSource> transformToDataSources(String source, String type, String location) throws FHIRException {
        return Arrays.asList(new BulkDataSource(type, location));
    }
}
