/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.system;

import java.util.ArrayList;
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

import com.ibm.fhir.bulkdata.export.system.resource.SystemExportResourceHandler;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.HistorySortOrder;
import com.ibm.fhir.persistence.ResourceChangeLogRecord;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;

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

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        // We know these are real resource types.
        List<String> resourceTypes = Arrays.asList(ctx.getFhirResourceTypes().split("\\s*,\\s*"));

        // Note we're already running inside a transaction (started by the JavaBatch framework)
        // so this txn will just wrap it...the commit won't happen until the checkpoint
        SystemExportResourceHandler handler = new SystemExportResourceHandler();
        FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper(handler.getSearchHelper());
        FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();

        // Check resourceType needs to be processed
        List<String> target = new ArrayList<>();
        try {
            for (String resourceType : resourceTypes) {
                List<ResourceChangeLogRecord> resourceResults = fhirPersistence.changes(1, null, null, null, Arrays.asList(resourceType), false, HistorySortOrder.NONE);

                // Early Exit Logic
                if (!resourceResults.isEmpty()) {
                    target.add(resourceType);
                }
            }
        } finally {
            txn.end();
        }

        PartitionPlanImpl pp = new PartitionPlanImpl();
        pp.setPartitions(target.size());
        pp.setThreads(Math.min(ConfigurationFactory.getInstance().getCoreMaxPartitions(), target.size()));
        Properties[] partitionProps = new Properties[target.size()];

        int propCount = 0;
        for (String resourceType : target) {
            Properties p = new Properties();
            p.setProperty(OperationFields.PARTITION_RESOURCETYPE, resourceType);
            partitionProps[propCount++] = p;
        }
        pp.setPartitionProperties(partitionProps);

        return pp;
    }
}