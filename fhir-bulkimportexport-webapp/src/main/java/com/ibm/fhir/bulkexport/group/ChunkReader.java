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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.batch.api.BatchProperty;
import javax.batch.runtime.context.StepContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.ibm.cloud.objectstorage.services.s3.model.PartETag;
import com.ibm.fhir.bulkcommon.Constants;
import com.ibm.fhir.bulkexport.common.TransientUserData;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Bulk patient group export Chunk implementation - the Reader.
 */
@Dependent
public class ChunkReader extends com.ibm.fhir.bulkexport.patient.ChunkReader {
    private final static Logger logger = Logger.getLogger(ChunkReader.class.getName());
    // List for the patients
    List<Member> patientMembers = null;

    /**
     * Fhir search patient group id.
     */
    @Inject
    @BatchProperty(name = Constants.EXPORT_FHIR_SEARCH_PATIENTGROUPID)
    String fhirSearchPatientGroupId;

    @Inject
    StepContext stepCtx;

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
                Group group2 = findGroupByID(refValue.substring(6));
                if (!groupsInPath.contains(group2.getId())) {
                    expandGroup2Patients(fhirTenant, fhirDatastoreId, group2, patients, groupsInPath);
                }
            }
        }
    }

    private Group findGroupByID(String groupId) throws Exception{
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

    private List<Resource> patientIdsToPatients(List<String> patientIds) throws Exception {
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        List<Resource> patients;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_id", patientIds);
        searchContext = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        searchContext.setPageSize(pageSize);
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());

        txn.enroll();
        persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
        patients = fhirPersistence.search(persistenceContext, Patient.class).getResource();
        txn.unenroll();

        return patients;
    }

    @Override
    public Object readItem() throws Exception {
        if (fhirSearchPatientGroupId == null) {
            throw new Exception("readItem: missing group id for this group export job!");
        }

        TransientUserData chunkData = (TransientUserData) stepCtx.getTransientUserData();
        if (chunkData != null && pageNum > chunkData.getLastPageNum()) {
            chunkData.setMoreToExport(false);
            return null;
        }

        if (patientMembers == null) {
            Group group = findGroupByID(fhirSearchPatientGroupId);
            patientMembers = new ArrayList<>();
            // List for the group and sub groups in the expansion paths, this is used to avoid dead loop caused by circle reference of the groups.
            HashSet<String> groupsInPath = new HashSet<>();
            expandGroup2Patients(fhirTenant, fhirDatastoreId, group, patientMembers, groupsInPath);
        }
        List<Member> patientPageMembers = patientMembers.subList((pageNum - 1) * pageSize,
                pageNum * pageSize <= patientMembers.size() ? pageNum * pageSize : patientMembers.size());
        pageNum++;

        if (chunkData == null) {
            chunkData = new TransientUserData(pageNum, null, new ArrayList<PartETag>(), 1, 0, null, 0, 0, 0, 1);
            stepCtx.setTransientUserData(chunkData);
        } else {
            chunkData.setPageNum(pageNum);
        }
        chunkData.setLastPageNum((patientMembers.size() + pageSize -1)/pageSize );

        if (!patientPageMembers.isEmpty()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("readItem: loaded patients number - " + patientMembers.size());
            }

            List<String> patientIds = patientPageMembers.stream().filter(patientRef -> patientRef != null).map(patientRef
                    -> patientRef.getEntity().getReference().getValue().substring(8)).collect(Collectors.toList());
            if (patientIds != null && patientIds.size() > 0) {
                List<Resource> resources = null;
                if (fhirResourceType.equalsIgnoreCase("patient")) {
                    resources = patientIdsToPatients(patientIds);
                }
                fillChunkData(resources, patientIds);
            }
        } else {
            logger.fine("readItem: End of reading!");
        }

        return patientPageMembers;
    }

}
