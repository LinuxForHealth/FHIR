/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.exception;

import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

/**
 * This exception class represents an occurrence of a mismatch between the version id in the resource json vs.
 * the next expected version. This can happen due to a race condition when a particular resource is being updated concurrently
 * on different threads.
 * @author markd
 *
 */
public class FHIRPersistenceVersionIdMismatchException extends FHIRPersistenceException {

    private static final long serialVersionUID = -8350452448890342596L;

    public FHIRPersistenceVersionIdMismatchException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(getMessage(), IssueType.CONFLICT));
    }

    public FHIRPersistenceVersionIdMismatchException(String message, Throwable cause) {
        super(message, cause);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(getMessage(), IssueType.CONFLICT));
    }

}
