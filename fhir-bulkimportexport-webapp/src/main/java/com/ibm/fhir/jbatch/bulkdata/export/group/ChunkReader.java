/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.jbatch.bulkdata.export.group;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.jbatch.bulkdata.context.BatchContextAdapter;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.export.group.resource.GroupHandler;
import com.ibm.fhir.jbatch.bulkdata.export.patient.resource.PatientResourceHandler;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.BulkDataContext;

/**
 * BulkData Group Export ChunkReader
 */
@Dependent
public class ChunkReader extends com.ibm.fhir.jbatch.bulkdata.export.patient.ChunkReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());

    private GroupHandler groupHandler = new GroupHandler();
    private PatientResourceHandler patientHandler = new PatientResourceHandler();
    private BulkDataContext ctx = null;

    @Inject
    StepContext stepCtx;

    public ChunkReader() {
        super();
    }

    @Override
    public Object readItem() throws Exception {
        BatchContextAdapter stepContextAdapter = new BatchContextAdapter(stepCtx);
        ctx = stepContextAdapter.getStepContextForGroupChunkReader();

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

        if (!pageOfMembers.isEmpty()) {
            List<String> patientIds = pageOfMembers.stream().filter(patientRef -> patientRef != null).map(patientRef
                    -> patientRef.getEntity().getReference().getValue().substring(8)).collect(Collectors.toList());
            if (patientIds != null && patientIds.size() > 0) {
                patientHandler.register(chunkData, ctx, getPersistence(), pageSize, resourceType, searchParametersForResoureTypes);
                patientHandler.fillChunkData(groupHandler.patientIdsToPatients(patientIds), patientIds);
            }
        } else {
            logger.fine("readItem: End of reading!");
        }
        return pageOfMembers;
    }
}