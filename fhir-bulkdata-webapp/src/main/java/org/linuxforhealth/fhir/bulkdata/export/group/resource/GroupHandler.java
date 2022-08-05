/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bulkdata.export.group.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response.Status;

import org.linuxforhealth.fhir.bulkdata.audit.BulkAuditLogger;
import org.linuxforhealth.fhir.model.resource.Group;
import org.linuxforhealth.fhir.model.resource.Group.Member;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * GroupHandler handles each Page of Members in a Group (enabling paging of the members of the Group)
 *
 * @implNote we support nested Groups.
 */
public class GroupHandler {

    private static final Logger logger = Logger.getLogger(GroupHandler.class.getName());

    private BulkAuditLogger auditLogger = new BulkAuditLogger();

    protected FHIRPersistence fhirPersistence;


    private Set<String> uniquenessGuard = null;
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
            uniquenessGuard = new HashSet<>();
            patientMembers = new ArrayList<>();
            // List for the group and sub groups in the expansion paths, this is used to avoid dead loop caused by circle reference of the groups.
            Set<String> groupsInPath = new HashSet<>();
            expandGroupToPatients(group, groupsInPath);
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
    private void expandGroupToPatients(Group group, Set<String> groupsInPath)
            throws Exception{
        if (group == null) {
            return;
        }
        groupsInPath.add(group.getId());
        for (Member member : group.getMember()) {
            String refValue = member.getEntity().getReference().getValue();
            if (refValue.startsWith("Patient")) {
                if (uniquenessGuard.add(refValue)) {
                    patientMembers.add(member);
                }
            } else if (refValue.startsWith("Group")) {
                Group group2 = findGroupByID(refValue.substring(6));
                // Only expand if NOT previously found
                if (!groupsInPath.contains(group2.getId())) {
                    expandGroupToPatients(group2, groupsInPath);
                }
            } else if (logger.isLoggable(Level.FINE)){
                logger.fine("Skipping group member '" + refValue + "'. "
                        + "Only literal relative references to patients will be used for export.");
            }
        }
    }

    private Group findGroupByID(String groupId) throws Exception{
        FHIRPersistenceContext persistenceContext;
        SingleResourceResult<Group> result = null;

        Date startTime = new Date(System.currentTimeMillis());
        FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
        txn.begin();
        try {
            persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
            return fhirPersistence.read(persistenceContext, Group.class, groupId).getResource();
        } finally {
            txn.end();
            if (auditLogger.shouldLog() && result != null) {
                Date endTime = new Date(System.currentTimeMillis());
                auditLogger.logReadOnExport(result.getResource(), startTime, endTime,
                        result.isSuccess() ? Status.OK : Status.BAD_REQUEST, "StorageProvider@" + provider, "BulkDataOperator");
            }
        }
    }
}