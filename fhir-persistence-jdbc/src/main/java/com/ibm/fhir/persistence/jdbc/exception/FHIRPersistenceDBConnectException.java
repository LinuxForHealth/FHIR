/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.exception;

import java.util.Collection;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * This exception class represents failures encountered while attempting to connect to a JDBC database or datasource.
 */
public class FHIRPersistenceDBConnectException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890342596L;
    

    public FHIRPersistenceDBConnectException(String message) {
        super(message);
    }

    public FHIRPersistenceDBConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public FHIRPersistenceDBConnectException withIssue(OperationOutcome.Issue... issues) {
        super.withIssue(issues);
        return this;
    }
    
    @Override
    public FHIRPersistenceDBConnectException withIssue(Collection<OperationOutcome.Issue> issues) {
        super.withIssue(issues);
        return this;
    }

}
