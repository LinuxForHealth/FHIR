/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.jbatch.export.group;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

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
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;
import com.ibm.fhir.operation.bulkdata.model.type.OperationFields;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;

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

        // Move the page number forward. On the first readItem call, this will increment from 0 to 1.
        pageNum++;

        ExportTransientUserData chunkData = (ExportTransientUserData) stepCtx.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            chunkData.setMoreToExport(false);
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

        // We don't want to recreate the persistence layer, we want to reuse it.
        groupHandler.register(getPersistence(), ctx.getSource());
        groupHandler.process(ctx.getGroupId());

        // Get a Page of Patients
        List<Member> pageOfMembers = groupHandler.getPageOfMembers(pageNum, pageSize);

        if (chunkData == null) {
            chunkData = ExportTransientUserData.Builder.builder()
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
                .lastPageNum((pageOfMembers.size() + pageSize -1)/pageSize)
                .lastWrittenPageNum(1)
                .build();
        } else {
            chunkData.setPageNum(pageNum);
            // do we want to support extending the last page number mid-export?
//            chunkData.setLastPageNum((pageOfMembers.size() + pageSize -1)/pageSize);
        }

        ReadResultDTO dto = new ReadResultDTO();

        if (!pageOfMembers.isEmpty()) {
            Set<String> patientIds = new LinkedHashSet<>();
            String baseUrl = ReferenceUtil.getBaseUrl(null);
            for (Member member : pageOfMembers) {
                ReferenceValue refVal = ReferenceUtil.createReferenceValueFrom(member.getEntity(), baseUrl);
                if (refVal.getType() != ReferenceType.LITERAL_RELATIVE ||
                        !"Patient".equals(refVal.getTargetResourceType())) {
                    logger.info("Skipping group member '" + refVal.getValue() + "'. "
                            + "Only literal references to patients on this server will be exported.");
                    continue;
                }
                if (refVal.getVersion() != null) {
                    logger.info("Skipping group member '" + refVal.getValue() + "'. "
                            + "Versioned references are not supported by Group export at this time.");
                    continue;
                }
                patientIds.add(refVal.getValue());
            }
            if (!patientIds.isEmpty()) {
                patientHandler.register(chunkData, ctx, getPersistence(), pageSize, resourceType, searchParametersForResoureTypes, ctx.getSource());

                List<Resource> resources = patientHandler.executeSearch(patientIds);
                if (FHIRMediaType.APPLICATION_PARQUET.equals(ctx.getFhirExportFormat())) {
                    dto.setResources(resources);
                }
                patientHandler.fillChunkData(ctx.getFhirExportFormat(), chunkData, resources);
            }
        } else {
            logger.fine("readItem: End of reading!");
        }

        stepCtx.setTransientUserData(chunkData);
        return dto;
    }
}
