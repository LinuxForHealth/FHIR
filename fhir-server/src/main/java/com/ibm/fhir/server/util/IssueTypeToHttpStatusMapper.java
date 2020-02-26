/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;

public class IssueTypeToHttpStatusMapper {
    /**
     * Custom extension used by the IBM FHIR Server for marking which precondition has failed (if any)
     */
    private static final String EXTENSION_URL_HTTP_FAILED_PRECONDITION = "http://ibm.com/fhir/extension/http-failed-precondition";

    /**
     * @return an HTTP response status based on the first issue contained within the OperationOutcome with a code;
     *         Response.Status.INTERNAL_SERVER_ERROR if it is null or empty
     */
    public static Response.Status ooToStatus(OperationOutcome oo) {
        if (oo == null) {
            return Status.INTERNAL_SERVER_ERROR;
        }
        return issueListToStatus(oo.getIssue());
    }
    
    /**
     * @return an HTTP response status based on the first issue contained within the OperationOutcomeIssue list with a code;
     *         Response.Status.INTERNAL_SERVER_ERROR if it is null or empty
     */
    public static Response.Status issueListToStatus(List<Issue> issues) {
        if (issues != null) {
            for (Issue issue : issues) {
                IssueType code = issue.getCode();
                if (code != null && code.getValue() != null) {
                    IssueType.ValueSet issueType = code.getValueAsEnumConstant();
                    // Special case for IssueType CONFLICT which can be either an HTTP 409 (Conflict) or HTTP 412 (Precondition failed)
                    if (issueType == IssueType.ValueSet.CONFLICT &&
                            FHIRUtil.getExtensionStringValue(code, EXTENSION_URL_HTTP_FAILED_PRECONDITION) != null) {
                        return Status.PRECONDITION_FAILED;
                    }
                    return issueTypeToResponseCode(issueType);
                }
            }
        }
        return Status.INTERNAL_SERVER_ERROR;
    }
    
    private static Status issueTypeToResponseCode(IssueType.ValueSet value) {
        switch (value) {
        case INFORMATIONAL:
            return Status.OK;
        case FORBIDDEN:
        case SUPPRESSED:
        case SECURITY:
        case THROTTLED:     // Should this one be an HTTP 429 instead?
            return Status.FORBIDDEN;
        case PROCESSING:
        case BUSINESS_RULE: // Should we consider HTTP 422 for these?
        case CODE_INVALID:  // Should we consider HTTP 422 for these?
        case EXTENSION:     // Should we consider HTTP 422 for these?
        case INVALID:       // Should we consider HTTP 422 for these?
        case INVARIANT:     // Should we consider HTTP 422 for these?
        case REQUIRED:      // Should we consider HTTP 422 for these?
        case STRUCTURE:     // Should we consider HTTP 422 for these?
        case VALUE:         // Should we consider HTTP 422 for these?
        case TOO_COSTLY:    // Should we consider HTTP 403 for this?
            return Status.BAD_REQUEST;
        case DELETED:
            return Status.GONE;
        case CONFLICT:
        case DUPLICATE:
            return Status.CONFLICT;
        case MULTIPLE_MATCHES:
            return Status.PRECONDITION_FAILED;
        case EXPIRED:
        case LOGIN:
        case UNKNOWN:
            return Status.UNAUTHORIZED;
        case NOT_FOUND:
        case NOT_SUPPORTED:
            return Status.NOT_FOUND;
        case TOO_LONG:
            return Status.REQUEST_ENTITY_TOO_LARGE;
        case EXCEPTION:
        case LOCK_ERROR:
        case NO_STORE:
        case TIMEOUT:
        case TRANSIENT:
        case INCOMPLETE:
        default:
            return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
