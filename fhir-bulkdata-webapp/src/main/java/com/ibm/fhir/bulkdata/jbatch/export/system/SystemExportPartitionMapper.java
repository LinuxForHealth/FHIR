/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.system;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * Generates the {@link PartitionPlan} describing how the system export work is
 * broken into pieces. Each resource type extracted from the fhirResourceType
 * property is allocated to a partition.
 */
@Dependent
public class SystemExportPartitionMapper implements PartitionMapper {

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    public SystemExportPartitionMapper() {
        // No Operation
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(jobCtx.getExecutionId());

        BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

        BulkDataContext ctx = ctxAdapter.getStepContextForExportPartitionMapper();

        // We know these are real resource types.
        List<String> resourceTypes = Arrays.asList(ctx.getFhirResourceTypes().split("\\s*,\\s*"));

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(resourceTypes.size());
        pp.setThreads(Math.min(ConfigurationFactory.getInstance().getCoreMaxPartitions(), resourceTypes.size()));
        Properties[] partitionProps = new Properties[resourceTypes.size()];

        int propCount = 0;
        for (String resourceType : resourceTypes) {
            Properties p = new Properties();
            p.setProperty(OperationFields.PARTITION_RESOURCETYPE, resourceType);
            partitionProps[propCount++] = p;
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }
}