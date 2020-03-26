/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkexport.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk patient group export Chunk implementation - the Reader.
 */
public class ChunkReader extends com.ibm.fhir.bulkexport.patient.ChunkReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    // List for the patients
    List<Member> patientMembers = null;

    /**
     * Fhir search patient group id.
     */
    @Inject
    @BatchProperty(name = "fhir.search.patientgroupid")
    String fhirSearchPatientGroupId;

    @Inject
    JobContext jobContext;

    public ChunkReader() {
        super();
    }

    private void expandGroup2Patients(String fhirTenant, String fhirDatastoreId, Group group, List<Member> patients, HashSet<String> groupsInPath)
            throws Exception{
        if (group == null) {
            return;
        }
        groupsInPath.add(group.getId());
        for (Member member : group.getMember()) {
            String refValue = member.getEntity().getReference().getValue();
            if (refValue.startsWith("Patient")) {
                patients.add(member);
            } else if (refValue.startsWith("Group")) {
                Group group2 = findGroupByID(fhirTenant, fhirDatastoreId, refValue.substring(6));
                if (!groupsInPath.contains(group2.getId())) {
                    expandGroup2Patients(fhirTenant, fhirDatastoreId, group2, patients, groupsInPath);
                }
            }
        }
    }

    private Group findGroupByID(String fhirTenant, String fhirDatastoreId, String groupId) throws Exception{
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_id", Arrays.asList(new String[] { groupId }));
        searchContext = SearchUtil.parseQueryParameters(Group.class, queryParameters);
        List<Resource> resources = null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.enroll();
        persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
        resources = fhirPersistence.search(persistenceContext, Group.class).getResource();
        txn.unenroll();

        if (resources != null) {
            return (Group) resources.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Object readItem() throws Exception {
        if (fhirSearchPatientGroupId == null) {
            throw new Exception("readItem: missing group id for this group export job!");
        }

        TransientUserData chunkData = (TransientUserData) jobContext.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            if (resourceTypes.size() == indexOfCurrentResourceType + 1) {
                // No more resource type and page to read, so return null to end the reading.
                return null;
            } else {
                // More resource types to read, so reset pageNum, partNum and move resource type index to the next.
                pageNum = 1;
                chunkData.setPartNum(1);
                indexOfCurrentResourceType++;
            }
        }

        if (patientMembers == null) {
            Group group = findGroupByID(fhirTenant, fhirDatastoreId, fhirSearchPatientGroupId);
            patientMembers = new ArrayList<>();
            // List for the group and sub groups in the expansion paths, this is used to avoid dead loop caused by circle reference of the groups.
            HashSet<String> groupsInPath = new HashSet<>();
            expandGroup2Patients(fhirTenant, fhirDatastoreId, group, patientMembers, groupsInPath);
        }
        List<Member> patientPageMembers = patientMembers.subList((pageNum - 1) * pageSize,
                pageNum * pageSize <= patientMembers.size() ? pageNum * pageSize : patientMembers.size());
        pageNum++;

        if (chunkData == null) {
            chunkData = new TransientUserData(pageNum, null, new ArrayList<PartETag>(), 1, 0, 0);
            jobContext.setTransientUserData(chunkData);
        } else {
            chunkData.setIndexOfCurrentResourceType(indexOfCurrentResourceType);
            chunkData.setPageNum(pageNum);
        }
        chunkData.setLastPageNum((patientMembers.size() + pageSize -1)/pageSize );

        if (!patientPageMembers.isEmpty()) {
            logger.fine("readItem: loaded patients number - " + patientMembers.size());

            List<String> patientIds = patientPageMembers.stream().filter(patientRef -> patientRef != null).map(patientRef
                    -> patientRef.getEntity().getReference().getValue().substring(8)).collect(Collectors.toList());
            if (patientIds != null && patientIds.size() > 0) {
                fillChunkDataBuffer(patientIds);
            }
        } else {
            logger.fine("readItem: End of reading!");
        }

        return patientPageMembers;
    }

}
