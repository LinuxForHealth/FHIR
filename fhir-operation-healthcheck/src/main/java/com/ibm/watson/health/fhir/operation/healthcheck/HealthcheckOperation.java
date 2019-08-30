/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.healthcheck;

import java.io.InputStream;
import java.util.List;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.operation.AbstractOperation;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;
import com.ibm.watson.health.fhir.operation.util.FHIROperationUtil;
import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;

public class HealthcheckOperation extends AbstractOperation {
    public HealthcheckOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("healthcheck.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);            
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            FHIRPersistence pl = (FHIRPersistence) operationContext.getProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL);
            OperationOutcome operationOutcome = pl.getHealth();
            checkOperationOutcome(operationOutcome);
            return FHIROperationUtil.getOutputParameters(operationOutcome);
        } catch (FHIROperationException e) {
            throw e;
        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for operation '" +
                    getName() + "': " + getCausedByMessage(t), t);
        }
    }
    
    private void checkOperationOutcome(OperationOutcome oo) throws FHIROperationException {
        List<Issue> issues = oo.getIssue();
        for (Issue issue : issues) {
            IssueSeverity severity = issue.getSeverity();
            if (severity != null) {
                if(severity.getValue()==IssueSeverity.ERROR.getValue() || severity.getValue()==IssueSeverity.FATAL.getValue())
                    throw new FHIROperationException("The persistence layer reported one or more issues").withIssue(issues);
            }
        }
    }
    
    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}
