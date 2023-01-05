/**
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.bulkdata.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import org.linuxforhealth.fhir.persistence.helper.FHIRTransactionHelper;

/**
 * Resource class for the health check of bulkdata web application.
 */
@Path("/")
public class HealthCheck {
    
    
    public HealthCheck() throws Exception {
        super();
    }

    private static final Logger log = java.util.logging.Logger.getLogger(HealthCheck.class.getName());
    
    FHIRPersistence fhirPersistence;
    
    /**
     * This method validates the health check of bulkdata web application.
     * @return Response - The health check response status
     * @throws FHIROperationException
     */
    @GET
    @Path("healthcheck")
    public Response healthCheck() throws FHIROperationException {
        log.entering(this.getClass().getName(), "healthCheck()");
        try {
            FHIRPersistenceHelper fhirPersistenceHelper = new FHIRPersistenceHelper(null);
            fhirPersistence = fhirPersistenceHelper.getFHIRPersistenceImplementation();
            FHIRTransactionHelper txn = new FHIRTransactionHelper(fhirPersistence.getTransaction());
            txn.begin();
            try {
                OperationOutcome operationOutcome = fhirPersistence.getHealth();
                checkOperationOutcome(operationOutcome);
                return Response.status(Response.Status.OK).build();
            } catch (Throwable t) {
                txn.setRollbackOnly();
                throw t;
            } finally {
                txn.end();
            }
        } catch (FHIROperationException e) {
            throw e;
        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for bulkdata healthcheck operation : " + getCausedByMessage(t), t);
        } finally {
            log.exiting(this.getClass().getName(), "healthCheck()");
        }
    }

    private void checkOperationOutcome(OperationOutcome oo) throws FHIROperationException {
        List<Issue> issues = oo.getIssue();
        for (Issue issue : issues) {
            IssueSeverity severity = issue.getSeverity();
            if (severity != null && (IssueSeverity.ERROR.getValue().equals(severity.getValue())
                    || IssueSeverity.FATAL.getValue().equals(severity.getValue()))) {
                throw new FHIROperationException("The persistence layer reported one or more issues").withIssue(issues);
            }
        }
    }

    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
    
    
}
