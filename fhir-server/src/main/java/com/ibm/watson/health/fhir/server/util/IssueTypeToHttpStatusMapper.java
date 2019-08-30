/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server.util;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.type.IssueType;

public class IssueTypeToHttpStatusMapper {
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
                    return issueTypeToResponseCode(IssueType.ValueSet.from(code.getValue()));
                }
            }
        }
        
        return Status.INTERNAL_SERVER_ERROR;
    }
    
    public static Status issueTypeToResponseCode(IssueType.ValueSet value) {
        switch (value) {
        case INFORMATIONAL:
            return Status.OK;
        case FORBIDDEN:
        case SUPPRESSED:
        case SECURITY:
        case THROTTLED:     // Should this one be an HTTP 429 instead?
            return Status.FORBIDDEN;
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
        case CONFLICT:
        case DUPLICATE:
            return Status.CONFLICT;
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
        default:
            return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
