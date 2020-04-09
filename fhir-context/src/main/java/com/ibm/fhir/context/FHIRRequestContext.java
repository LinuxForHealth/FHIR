/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.context;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;

/**
 * This class is used to hold FHIR REST API context information.
 *
 * <p>There are non-static methods for maintaining individual instances, as
 * well as static methods for setting instances on and getting instances from thread local.
 *
 * <p>The overall strategy is for the REST API servlet filter to retrieve the request context
 * information, create an instance of this class and set it on the current thread for use
 * by the FHIR Server as it processes the request.
 */
public class FHIRRequestContext {
    private static final Logger log = Logger.getLogger(FHIRRequestContext.class.getName());

    private String tenantId;
    private String tenantKey;
    private String dataStoreId;
    private String requestUniqueId;
    private String originalRequestUri;
    private Map<String, List<String>> httpHeaders;
    private List<OperationOutcome.Issue> issues;

    // Default to the "strict" handling which means the server will reject unrecognized search parameters and elements
    private HTTPHandlingPreference handlingPreference = HTTPHandlingPreference.STRICT;

    // Default to the "minimal" representation which means create/update responses won't return the resource body
    private HTTPReturnPreference returnPreference = HTTPReturnPreference.MINIMAL;

    private Pattern validChars = Pattern.compile("[a-zA-Z0-9_\\-]+");
    private String errorMsg = "Only [a-z], [A-Z], [0-9], '_', and '-' characters are allowed.";

    private static ThreadLocal<FHIRRequestContext> contexts = new ThreadLocal<FHIRRequestContext>() {
        @Override
        public FHIRRequestContext initialValue() {
            try {
                return new FHIRRequestContext(FHIRConfiguration.DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_DATASTORE_ID);
            } catch (FHIRException e) {
                throw new IllegalStateException("Unexpected error while initializing FHIRRequestContext");
            }
        }
    };

    public FHIRRequestContext() {
        this.requestUniqueId = UUID.randomUUID().toString();
        this.issues = new ArrayList<>();
    }

    public FHIRRequestContext(String tenantId) throws FHIRException {
        this();
        setTenantId(tenantId);
    }

    public FHIRRequestContext(String tenantId, String dataStoreId) throws FHIRException {
        this(tenantId);
        setDataStoreId(dataStoreId);
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getTenantKey() {
        return this.tenantKey;
    }

    public void setTenantId(String tenantId) throws FHIRException {
        Matcher matcher = validChars.matcher(tenantId);
        if (matcher.matches()) {
            this.tenantId = tenantId;
        } else {
            throw new FHIRException("Invalid tenantId. " + errorMsg);
        }
    }

    /**
     * Setter for the tenant key
     * @param b64
     * @throws FHIRException if the given value is not a valid Base64 string
     */
    public void setTenantKey(String b64) throws FHIRException {
        try {
            Base64.getDecoder().decode(b64);
            this.tenantKey = b64;
        }
        catch (IllegalArgumentException x) {
            // Tenant key is a secret, so don't include it in any error message
            throw new FHIRException("Invalid tenantKey.");
        }
    }

    public String getDataStoreId() {
        return dataStoreId;
    }

    public void setDataStoreId(String dataStoreId) throws FHIRException {
        Matcher matcher = validChars.matcher(dataStoreId);
        if (matcher.matches()) {
            this.dataStoreId = dataStoreId;
        } else {
            throw new FHIRException("Invalid dataStoreId. " + errorMsg);
        }
    }

    /**
     * Sets the specified FHIRRequestContext instance on the current thread,
     * so that it can be used by FHIR Server downstream processing.
     * This method is called when the FHIR Server starts processing a request.
     */
    public static void set(FHIRRequestContext context) {
        contexts.set(context);
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.set: " + context.toString());
        }
    }

    /**
     * Returns the FHIRRequestContext on the current thread.
     */
    public static FHIRRequestContext get() {
        FHIRRequestContext result = contexts.get();
        if (log.isLoggable(Level.FINEST)) {
            log.finest("FHIRRequestContext.get: " + result.toString());
        }
        return result;
    }

    /**
     * Removes the FHIRRequestContext that's set on the current thread.
     * This method is called when the FHIR Server is finished processing a request.
     */
    public static void remove() {
        contexts.remove();
        log.finest("FHIRRequestContext.remove invoked.");
    }

    @Override
    public String toString() {
        return "FHIRRequestContext [tenantId=" + tenantId + ", dataStoreId=" + dataStoreId
                + ", this="+ objectHandle(this) + "]";
    }

    public String getRequestUniqueId() {
        return this.requestUniqueId;
    }

    private static String objectHandle(Object obj) {
        return '@' + Integer.toHexString(System.identityHashCode(obj));
    }

    /**
     * @return the handlingPreference
     */
    public HTTPHandlingPreference getHandlingPreference() {
        return handlingPreference;
    }

    /**
     * @param handlingPreference the handlingPreference to set
     */
    public void setHandlingPreference(HTTPHandlingPreference handlingPreference) {
        this.handlingPreference = handlingPreference;
    }

    /**
     * @return the returnPreference
     */
    public HTTPReturnPreference getReturnPreference() {
        return returnPreference;
    }

    /**
     * @param returnPreference the returnPreference to set
     */
    public void setReturnPreference(HTTPReturnPreference returnPreference) {
        this.returnPreference = returnPreference;
    }

    /**
     * @return the originalRequestUri
     */
    public String getOriginalRequestUri() {
        return originalRequestUri;
    }

    /**
     * @param originalRequestUri the originalRequestUri to set
     */
    public void setOriginalRequestUri(String originalRequestUri) {
        this.originalRequestUri = originalRequestUri;
    }

    /**
     * @return the httpHeaders
     */
    public Map<String, List<String>> getHttpHeaders() {
        return httpHeaders;
    }

    /**
     * @param httpHeaders the httpHeaders to set
     */
    public void setHttpHeaders(Map<String, List<String>> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    /**
     * @return the issues
     */
    public List<OperationOutcome.Issue> getIssues() {
        return issues;
    }

    /**
     * Associate a supplemental warning with the current request
     */
    public void addWarning(IssueType issueType, com.ibm.fhir.model.type.String message, com.ibm.fhir.model.type.String... expression) {
        this.issues.add(OperationOutcome.Issue.builder()
                .severity(IssueSeverity.WARNING)
                .code(issueType)
                .details(CodeableConcept.builder()
                    .text(message)
                    .build())
                .expression(expression)
                .build());
    }

    /**
     * Associate a supplemental informational message with the current request
     */
    public void addInfo(IssueType issueType, com.ibm.fhir.model.type.String message, com.ibm.fhir.model.type.String... expression) {
        this.issues.add(OperationOutcome.Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(issueType)
                .details(CodeableConcept.builder()
                    .text(message)
                    .build())
                .expression(expression)
                .build());
    }

    /**
     * Associate a list of supplemental issues with the current request
     *
     * @implSpec issues will be appended in-order to the resulting OperationOutcome
     * @implNote these issues are supplemental and therefor it is NOT recommended to use this method
     *           to communicate issues of SEVERITY greater than warning (i.e. do not be use for issues
     *           of severity IssueSeverity.ValueSet.ERROR or IssueSeverity.ValueSet.FATAL)
     */
    public void addIssue(List<OperationOutcome.Issue> issues) {
        this.issues.addAll(issues);
    }
}
