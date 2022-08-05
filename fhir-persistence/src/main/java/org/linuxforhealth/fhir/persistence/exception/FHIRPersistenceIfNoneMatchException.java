/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.exception;

import static org.linuxforhealth.fhir.core.FHIRConstants.EXT_BASE;
import static org.linuxforhealth.fhir.model.type.String.string;

import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;

/**
 * This exception is thrown when an IfNoneMatch precondition check
 * fails and the server is configured to treat this as an error
 * (412 Precondition Failed).
 * See FHIRConfiguration.PROPERTY_IF_NONE_MATCH_RETURNS_NOT_MODIFIED.
 */
public class FHIRPersistenceIfNoneMatchException extends FHIRPersistenceException {

    // Generated serialVersionUID
    private static final long serialVersionUID = -6078163237462666409L;

    public FHIRPersistenceIfNoneMatchException(String message) {
        super(message);
        withIssue(FHIRUtil.buildOperationOutcomeIssue(getMessage(), IssueType.CONFLICT.toBuilder()
                .extension(Extension.builder()
                    .url(EXT_BASE + "http-failed-precondition")
                    .value(string("If-None-Match"))
                    .build())
                .build()));
    }
}