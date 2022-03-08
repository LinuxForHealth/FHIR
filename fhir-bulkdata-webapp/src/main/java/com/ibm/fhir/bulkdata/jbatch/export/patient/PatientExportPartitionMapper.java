/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.patient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.ResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRPersistenceHelper;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.compartment.CompartmentHelper;
import com.ibm.fhir.search.context.FHIRSearchContext;

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
        List<String> resourceTypes = compartmentHelper.getCompartmentResourceTypes("Patient");
        if (ctx.getFhirResourceTypes() != null ) {
            resourceTypes = Arrays.asList(ctx.getFhirResourceTypes().split("\\s*,\\s*"));
        }

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        // Check resourceType needs to be processed
        List<String> target = new ArrayList<>();
        SystemExportResourceHandler handler = new SystemExportResourceHandler();
        for (String resourceType : resourceTypes) {
            FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper(handler.getSearchHelper());
            FHIRPersistence fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
            Map<String, List<String>> queryParameters = new HashMap<>();
            Class<? extends Resource> rt = ModelSupport.getResourceType(resourceType);
            FHIRSearchContext searchContext = handler.getSearchHelper().parseQueryParameters(rt, queryParameters);
            searchContext.setPageSize(1);
            searchContext.setPageNumber(1);

            // Note we're already running inside a transaction (started by the JavaBatch framework)
            // so this txn will just wrap it...the commit won't happen until the checkpoint
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            try {
                
                // Execute the search query to obtain the page of resources
                FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
                @SuppressWarnings("unused")
                List<ResourceResult<? extends Resource>> resourceResults = fhirPersistence.search(persistenceContext, rt).getResourceResults();
                resourceResults = null;

                // Early Exit Logic
                if (searchContext.getTotalCount() != 0) {
                    target.add(resourceType);
                }
            } finally {
                txn.end();
            }
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