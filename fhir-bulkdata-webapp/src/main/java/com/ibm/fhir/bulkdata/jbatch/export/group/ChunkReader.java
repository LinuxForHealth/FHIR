/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.export.group.resource.GroupHandler;
import com.ibm.fhir.bulkdata.export.patient.resource.PatientResourceHandler;
import com.ibm.fhir.bulkdata.jbatch.context.BatchContextAdapter;
import com.ibm.fhir.bulkdata.jbatch.export.data.TransientUserData;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;

/**
 * BulkData Group Export ChunkReader
 */
@Dependent
public class ChunkReader extends com.ibm.fhir.bulkdata.jbatch.export.patient.ChunkReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private GroupHandler groupHandler = new GroupHandler();
    private PatientResourceHandler patientHandler = new PatientResourceHandler();
    private BulkDataContext ctx = null;

    @Inject
    @Any
    @BatchProperty(name = OperationFields.PARTITION_RESOURCETYPE)
    private String partResourceType;

    @Inject
    StepContext stepCtx;

    @Inject
    JobContext jobCtx;

    public ChunkReader() {
        super();
    }

    @Override
    public Object readItem() throws Exception {
        if (!BatchStatus.STARTED.equals(jobCtx.getBatchStatus())) {
            // short-circuit
            return null;
        }

        long executionId = jobCtx.getExecutionId();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchContextAdapter contextAdapter = new BatchContextAdapter(jobExecution.getJobParameters());
        ctx = contextAdapter.getStepContextForGroupChunkReader();
        ctx.setPartitionResourceType(partResourceType);

        resourceType = ModelSupport.getResourceType(ctx.getPartitionResourceType());

        // Register the context to get the right configuration.
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        adapter.registerRequestContext(ctx.getTenantId(), ctx.getDatastoreId(), ctx.getIncomingUrl());

        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            chunkData.setMoreToExport(false);
            return null;
        }

        // We don't want to recreated the persistence layer, we want to reuse it.
        groupHandler.register(getPersistence());
        groupHandler.process(ctx.getGroupId());

        // Get a Page of Patients
        List<Member> pageOfMembers = groupHandler.getPageOfMembers(pageNum, pageSize);

        // Move the Page Number Forward
        pageNum++;

        if (chunkData == null) {
            chunkData = (TransientUserData) TransientUserData.Builder.builder()
                .pageNum(pageNum)
                .uploadId(null)
                .cosDataPacks(new ArrayList<PartETag>())
                .partNum(1)
                .indexOfCurrentTypeFilter(0)
                .resourceTypeSummary(null)
                .totalResourcesNum(0)
                .currentUploadResourceNum(0)
                .currentUploadSize(0)
                .uploadCount(1)
                .lastPageNum(0)
                .lastWritePageNum(1)
                .build();

            stepCtx.setTransientUserData(chunkData);
        } else {
            chunkData.setPageNum(pageNum);
        }
        chunkData.setLastPageNum((pageOfMembers.size() + pageSize -1)/pageSize );

        ReadResultDTO dto = new ReadResultDTO();
        if (!pageOfMembers.isEmpty()) {
            List<String> patientIds = pageOfMembers.stream()
                    .filter(patientRef -> patientRef != null)
                    .map(patientRef -> patientRef.getEntity().getReference().getValue().replace("Patient/", ""))
                    .collect(Collectors.toList());
            if (patientIds != null && !patientIds.isEmpty()) {
                patientHandler.register(chunkData, ctx, getPersistence(), pageSize, resourceType, searchParametersForResoureTypes);

                if ("Patient".equals(ctx.getPartitionResourceType())) {
                    dto.setResources(groupHandler.patientIdsToPatients(patientIds));
                } else {
                    patientHandler.fillChunkDataBuffer(patientIds, dto);
                }
            }
        } else {
            logger.fine("readItem: End of reading!");
        }

        stepCtx.setTransientUserData(chunkData);
        return dto;
    }
}