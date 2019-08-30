/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.test;

import javax.ws.rs.core.Response.Status;

import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.resource.Patient;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.util.FHIRUtil;
import com.ibm.watson.health.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watson.health.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.watson.health.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;

/**
 * This class servces as a test implementation of the FHIRPersistenceInterceptor interface.
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
        possiblyThrowException(event.getFhirResource(), IssueType.ValueSet.FORBIDDEN);
    }

    @Override
    public void afterCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterCreateCount++;
        possiblyThrowException(event.getFhirResource(), IssueType.ValueSet.CODE_INVALID);
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResource() != null && event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeUpdateCount++;
        }
        possiblyThrowException(event.getFhirResource(), IssueType.ValueSet.CONFLICT);
    }

    @Override
    public void afterUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterUpdateCount++;
        possiblyThrowException(event.getFhirResource(), IssueType.ValueSet.EXPIRED);
    }
    
    @Override
    public void beforeRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeReadCount++;
        }
        possiblyThrowException(event.getFhirResourceType(), null);
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterReadCount++;
        possiblyThrowException(event.getFhirResourceType(), null);
    }
    
    @Override
    public void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null && event.getFhirVersionId() != null) {
            beforeVreadCount++;
        }
        possiblyThrowException(event.getFhirResourceType(), null);
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterVreadCount++;
        possiblyThrowException(event.getFhirResourceType(), null);
    }
    
    @Override
    public void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null && event.getFhirResourceId() != null) {
            beforeHistoryCount++;
        }
        possiblyThrowException(event.getFhirResourceType(), null);
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterHistoryCount++;
        possiblyThrowException(event.getFhirResourceType(), null);
    }
    
    @Override
    public void beforeSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if (event.getFhirResourceType() != null) {
            beforeSearchCount++;
        }
        possiblyThrowException(event.getFhirResourceType(), null);
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        afterSearchCount++;
        possiblyThrowException(event.getFhirResourceType(), null);
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
    
    private void possiblyThrowException(Resource resource, IssueType.ValueSet issueType) throws FHIRPersistenceInterceptorException {
        if (resource instanceof Patient) {
            Patient patient = (Patient) resource;
            if (patient.getName().get(0).getFamily().getValue().equals("Exception")) {
                String msg = "Detected invalid patient";
                OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
                throw new FHIRPersistenceInterceptorException(msg).withIssue(ooi);
            }
        }
        
    }
    
    private void possiblyThrowException(String resourceType, Status status) throws FHIRPersistenceInterceptorException {
        if ("Exception".equals(resourceType)) {
            throw new FHIRPersistenceInterceptorException("Detected invalid resource type");
        }
        
    }
}
