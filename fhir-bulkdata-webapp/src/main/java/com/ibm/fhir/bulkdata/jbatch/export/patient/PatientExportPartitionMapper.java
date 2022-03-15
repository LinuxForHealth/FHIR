/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

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
import com.ibm.fhir.search.compartment.CompartmentHelper;


@Dependent
public class PatientExportPartitionMapper implements PartitionMapper {

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    private static final CompartmentHelper compartmentHelper = new CompartmentHelper();

    public PatientExportPartitionMapper() {
        // No Operation
    }

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        JobExecution jobExecution = BatchRuntime.getJobOperator().getJobExecution(jobCtx.getExecutionId());

        BatchContextAdapter ctxAdapter = new BatchContextAdapter(jobExecution.getJobParameters());

        BulkDataContext ctx = ctxAdapter.getStepContextForExportPartitionMapper();

        // By default we're in the Patient Compartment, if we have a valid context
        // which has a resourceType specified, it's valid as the operation has already checked.
        Set<String> resourceTypes = compartmentHelper.getCompartmentResourceTypes("Patient");
        if (ctx.getFhirResourceTypes() != null ) {
            resourceTypes = Set.of(ctx.getFhirResourceTypes().split("\\s*,\\s*"));
        }

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

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
        pp.setThreads(Math.min(adapter.getCoreMaxPartitions(), target.size()));
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