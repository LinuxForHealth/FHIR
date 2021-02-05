/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.system;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.jbatch.bulkdata.common.Constants;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * Generates the {@link PartitionPlan} describing how the system export work is
 * broken into pieces. Each resource type extracted from the fhirResourceType
 * property is allocated to a partition.
 */
@Dependent
public class SystemExportPartitionMapper implements PartitionMapper {

    // Comma-separated list of FHIR resource type names.
    @Inject
    @BatchProperty(name = Constants.FHIR_RESOURCETYPES)
    String fhirResourceType;


    public SystemExportPartitionMapper() {
        // No Operation
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        List<String> resourceTypes = Arrays.asList(fhirResourceType.split("\\s*,\\s*"));
        resourceTypes = resourceTypes.stream().filter(item-> ModelSupport.isResourceType(item)).collect(Collectors.toList());
        if (resourceTypes == null || resourceTypes.isEmpty()) {
            throw new Exception("open: None of the input resource types is valid!");
        }

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(resourceTypes.size());
        pp.setThreads(Math.min(Constants.IMPORT_MAX_PARTITIONPROCESSING_THREADNUMBER, resourceTypes.size()));
        Properties[] partitionProps = new Properties[resourceTypes.size()];

        int propCount = 0;
        for (String resourceType : resourceTypes) {
            Properties p = new Properties();
            p.setProperty(Constants.PARTITION_RESOURCE_TYPE, resourceType);
            partitionProps[propCount++] = p;
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }
}