/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.export.group.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import com.ibm.fhir.bulkdata.audit.BulkAuditLogger;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * GroupHandler handles each Page of Members in a Group (enabling paging of the members of the Group)
 *
 * @implNote we support nested Groups.
 */
public class GroupHandler {

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    protected FHIRPersistence fhirPersistence;
    private int pageSize = ConfigurationFactory.getInstance().getCorePageSize();

    // List for the patients
    private List<Member> patientMembers = null;
    private String provider = null;

    public GroupHandler() {
        // No Operation
    }

    /**
     * register the fhir persistence from the calling class.
     *
     * @param fhirPersistence
     * @param provider
     */
    public void register(FHIRPersistence fhirPersistence, String provider) {
        this.fhirPersistence = fhirPersistence;
        this.provider = provider;
    }

    /**
     * resolve the groupId into a set of patients and add them to this handler
     *
     * @param groupId
     * @throws Exception
     */
    public void process(String groupId) throws Exception {
        if (patientMembers == null) {
            Group group = findGroupByID(groupId);
            patientMembers = new ArrayList<>();
            // List for the group and sub groups in the expansion paths, this is used to avoid dead loop caused by circle reference of the groups.
            Set<String> groupsInPath = new HashSet<>();
            expandGroupToPatients(group, patientMembers, groupsInPath);
        }
    }

    /**
     * get a page of members from this handler
     *
     * @param pageNum
     * @param pageSize
     * @return
     * @implNote {@link GroupHandler#process(String)} must be called first
     */
    public List<Member> getPageOfMembers(int pageNum, int pageSize){
        return patientMembers.subList((pageNum - 1) * pageSize, Math.min(pageNum * pageSize, patientMembers.size()));
    }

    /**
     * recursively expands a group into a set of members
     * @param group
     * @param patients
     * @param groupsInPath empty, or prior Groups scanned
     * @throws Exception
     */
    private void expandGroupToPatients(Group group, List<Member> patients, Set<String> groupsInPath)
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
                // Only expand if NOT previously found
                if (!groupsInPath.contains(group2.getId())) {
                    expandGroupToPatients(group2, patients, groupsInPath);
                }
            }
        }
    }

    private Group findGroupByID(String groupId) throws Exception{
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put(SearchConstants.ID, Arrays.asList(groupId));
        searchContext = SearchUtil.parseQueryParameters(Group.class, queryParameters);
        List<Resource> resources = null;
        Date startTime = new Date(System.currentTimeMillis());
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        try {
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            resources = fhirPersistence.search(persistenceContext, Group.class).getResource();
        } finally {
            txn.end();
            if (auditLogger.shouldLog() && resources != null) {
                Date endTime = new Date(System.currentTimeMillis());
                auditLogger.logSearchOnExport(queryParameters, resources.size(), startTime, endTime, Response.Status.OK, "StorageProvider@" + provider, "BulkDataOperator");
            }
        }

        if (resources != null) {
            return (Group) resources.get(0);
        } else {
            return null;
        }
    }

    public List<Resource> patientIdsToPatients(List<String> patientIds) throws Exception {
        List<Resource> patients = null;
        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put(SearchConstants.ID, patientIds);

        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(Patient.class, queryParameters);
        searchContext.setPageSize(pageSize);

        Date startTime = new Date(System.currentTimeMillis());
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        try {
            FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, searchContext);
            patients = fhirPersistence.search(persistenceContext, Patient.class).getResource();
        } finally {
            txn.end();
            if (auditLogger.shouldLog() && patients != null) {
                Date endTime = new Date(System.currentTimeMillis());
                auditLogger.logSearchOnExport(queryParameters, patients.size(), startTime, endTime, Response.Status.OK, "StorageProvider@" + provider, "BulkDataOperator");
            }
        }
        return patients;
    }
}