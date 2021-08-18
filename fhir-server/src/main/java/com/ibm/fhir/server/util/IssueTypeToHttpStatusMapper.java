/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.core.FHIRConstants.EXT_BASE;

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
    private static final String EXTENSION_URL_HTTP_FAILED_PRECONDITION = EXT_BASE + "http-failed-precondition";

    /**
     * Custom extension used by the IBM FHIR Server for marking what it was that wasn't supported:
     * resource | interaction
     */
    private static final String EXTENSION_URL_NOT_SUPPORTED_DETAIL = EXT_BASE + "not-supported-detail";

    /**
     * Custom extension used by the IBM FHIR Server for marking which code was not found
     */
    private static final String EXTENSION_URL_NOT_FOUND_DETAIL = EXT_BASE + "not-found-detail";

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
                    IssueType.Value issueType = code.getValueAsEnum();
                    // Special case for IssueType CONFLICT which can be either an HTTP 409 (Conflict) or HTTP 412 (Precondition failed)
                    if (issueType == IssueType.Value.CONFLICT &&
                            FHIRUtil.getExtensionStringValue(code, EXTENSION_URL_HTTP_FAILED_PRECONDITION) != null) {
                        return Status.PRECONDITION_FAILED;
                    } else if (issueType == IssueType.Value.NOT_SUPPORTED &&
                            "resource".equals(FHIRUtil.getExtensionStringValue(code, EXTENSION_URL_NOT_SUPPORTED_DETAIL))) {
                        return Status.NOT_FOUND;
                    } else if (issueType == IssueType.Value.NOT_FOUND &&
                            FHIRUtil.getExtensionStringValue(code, EXTENSION_URL_NOT_FOUND_DETAIL ) != null ) {
                        return Status.BAD_REQUEST;
                    }
                    return issueTypeToResponseCode(issueType);
                }
            }
        }
        return Status.INTERNAL_SERVER_ERROR;
    }

    private static Status issueTypeToResponseCode(IssueType.Value value) {
        switch (value) {
        case INFORMATIONAL:
            return Status.OK;
        case FORBIDDEN:
        case SUPPRESSED:
        case SECURITY:
        case THROTTLED:     // Consider HTTP 429?
            return Status.FORBIDDEN;
        case PROCESSING:
        case NOT_SUPPORTED:
        case BUSINESS_RULE: // Consider HTTP 422?
        case CODE_INVALID:  // Consider HTTP 422?
        case EXTENSION:     // Consider HTTP 422?
        case INVALID:       // Consider HTTP 422?
        case INVARIANT:     // Consider HTTP 422?
        case REQUIRED:      // Consider HTTP 422?
        case STRUCTURE:     // Consider HTTP 422?
        case VALUE:         // Consider HTTP 422?
        case TOO_COSTLY:    // Consider HTTP 403?
        case DUPLICATE:     // Consider HTTP 409?
            return Status.BAD_REQUEST;
        case DELETED:
            return Status.GONE;
        case CONFLICT:
            return Status.CONFLICT;
        case MULTIPLE_MATCHES:
            return Status.PRECONDITION_FAILED;
        case EXPIRED:
        case LOGIN:
        case UNKNOWN:
            return Status.UNAUTHORIZED;
        case NOT_FOUND:
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
