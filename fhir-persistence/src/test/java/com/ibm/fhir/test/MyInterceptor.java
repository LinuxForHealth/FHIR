/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.test;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;

/**
 * This class serves as a test implementation of the FHIRPersistenceInterceptor interface.
 */
public class MyInterceptor implements FHIRPersistenceInterceptor {
    private static int beforeCreateCount = 0;
    private static int afterCreateCount = 0;
    private static int beforeUpdateCount = 0;
    private static int afterUpdateCount = 0;
    private static int beforeReadCount = 0;
    private static int afterReadCount = 0;
    private static int beforeVreadCount = 0;
    private static int afterVreadCount = 0;
    private static int beforeHistoryCount = 0;
    private static int afterHistoryCount = 0;
    private static int beforeSearchCount = 0;
    private static int afterSearchCount = 0;

    public MyInterceptor() {
    }

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResource() != null && event.getFhirResourceType() != null) {
            beforeCreateCount++;
        }
        possiblyThrowException(event.getFhirResource(), IssueType.FORBIDDEN);
    }

    @Override
    public void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterCreateCount++;
        possiblyThrowException(event.getFhirResource(), IssueType.CODE_INVALID);
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResource() != null && event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeUpdateCount++;
        }
        possiblyThrowException(event.getFhirResource(), IssueType.CONFLICT);
    }

    @Override
    public void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterUpdateCount++;
        possiblyThrowException(event.getFhirResource(), IssueType.EXPIRED);
    }

    @Override
    public void beforeRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeReadCount++;
        }
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterReadCount++;
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null && event.getFhirVersionId() != null) {
            beforeVreadCount++;
        }
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterVreadCount++;
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeHistoryCount++;
        }
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterHistoryCount++;
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void beforeSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null) {
            beforeSearchCount++;
        }
        possiblyThrowException(event.getFhirResourceType());
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterSearchCount++;
        possiblyThrowException(event.getFhirResourceType());
    }

    public static int getBeforeCreateCount() {
        return beforeCreateCount;
    }

    public static int getAfterCreateCount() {
        return afterCreateCount;
    }

    public static int getBeforeUpdateCount() {
        return beforeUpdateCount;
    }

    public static int getAfterUpdateCount() {
        return afterUpdateCount;
    }

    public static int getBeforeReadCount() {
        return beforeReadCount;
    }

    public static int getAfterReadCount() {
        return afterReadCount;
    }

    public static int getBeforeVreadCount() {
        return beforeVreadCount;
    }

    public static int getAfterVreadCount() {
        return afterVreadCount;
    }

    public static int getBeforeHistoryCount() {
        return beforeHistoryCount;
    }

    public static int getAfterHistoryCount() {
        return afterHistoryCount;
    }

    public static int getBeforeSearchCount() {
        return beforeSearchCount;
    }

    public static int getAfterSearchCount() {
        return afterSearchCount;
    }

    private void possiblyThrowException(Resource resource, IssueType issueType) throws FHIRPersistenceInterceptorException {
        if (resource instanceof Patient) {
            Patient patient = (Patient) resource;
            for (HumanName name : patient.getName()) {
                if (name.getFamily().getValue().equals("Exception")) {
                    String msg = "Detected invalid patient";
                    OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(ooi);
                }
            }
        }

    }

    private void possiblyThrowException(String resourceType) throws FHIRPersistenceInterceptorException {
        if ("Exception".equals(resourceType)) {
            throw new FHIRPersistenceInterceptorException("Detected invalid resource type");
        }
    }
}
