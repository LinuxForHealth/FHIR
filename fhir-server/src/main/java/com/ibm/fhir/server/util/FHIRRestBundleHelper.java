/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.core.FHIRConstants.EXT_BASE;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.ModelSupport.getResourceType;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.owasp.encoder.Encode;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.core.context.FHIRPagingContext;
import com.ibm.fhir.database.utils.api.LockException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.Bundle.Entry.Search;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.model.util.CollectingVisitor;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ReferenceMappingVisitor;
import com.ibm.fhir.model.util.SaltHash;
import com.ibm.fhir.model.visitor.ResourceFingerprintVisitor;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.patch.FHIRPathPatch;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.fhir.persistence.ResourceChangeLogRecord;
import com.ibm.fhir.persistence.ResourceEraseRecord;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.context.FHIRSystemHistoryContext;
import com.ibm.fhir.persistence.erase.EraseDTO;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.helper.FHIRTransactionHelper;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.impl.FHIRPersistenceInterceptorMgr;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.ReferenceValue.ReferenceType;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.operation.FHIROperationRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.rest.FHIRRestOperation;
import com.ibm.fhir.server.rest.FHIRRestOperationCreate;
import com.ibm.fhir.server.rest.FHIRRestOperationDelete;
import com.ibm.fhir.server.rest.FHIRRestOperationHistory;
import com.ibm.fhir.server.rest.FHIRRestOperationInvoke;
import com.ibm.fhir.server.rest.FHIRRestOperationIssue;
import com.ibm.fhir.server.rest.FHIRRestOperationPatch;
import com.ibm.fhir.server.rest.FHIRRestOperationRead;
import com.ibm.fhir.server.rest.FHIRRestOperationSearch;
import com.ibm.fhir.server.rest.FHIRRestOperationUpdate;
import com.ibm.fhir.server.rest.FHIRRestOperationVRead;
import com.ibm.fhir.server.rest.FHIRRestOperationValidationResponse;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Helper for processing bundle entries. Does not perform any persistence operations,
 * but instead helps to translate the request into a {@link FHIRRestOperation} implementation
 * which can be executed at a later time.
 */
public class FHIRRestBundleHelper {
    private static final Logger log =
            java.util.logging.Logger.getLogger(FHIRRestHelper.class.getName());

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String LOCAL_REF_PREFIX = "urn:";
    private static final com.ibm.fhir.model.type.String SC_BAD_REQUEST_STRING = string(Integer.toString(SC_BAD_REQUEST));
    private static final com.ibm.fhir.model.type.String SC_GONE_STRING = string(Integer.toString(SC_GONE));
    private static final com.ibm.fhir.model.type.String SC_NOT_FOUND_STRING = string(Integer.toString(SC_NOT_FOUND));
    private static final com.ibm.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));
    private static final com.ibm.fhir.model.type.String SC_OK_STRING = string(Integer.toString(SC_OK));
    private static final ZoneId UTC = ZoneId.of("UTC");
    
    // Constant for indicating the need to validate a resource
    public static final boolean DO_VALIDATION = true;
    // Constant for indicating whether an update can be skipped when the requested update resource matches the existing one
    public static final boolean SKIPPABLE_UPDATE = true;

    // default number of entries in system history if no _count is given
    private static final int DEFAULT_HISTORY_ENTRIES = 100;

    // clamp the number of entries in system history to 1000
    private static final int MAX_HISTORY_ENTRIES = 1000;

    public static final DateTimeFormatter PARSER_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("EEE")
            .optionalStart()
            // ANSIC date time format for If-Modified-Since
            .appendPattern(" MMM dd HH:mm:ss yyyy")
            .optionalEnd()
            .optionalStart()
            // Touchstone date time format for If-Modified-Since
            .appendPattern(", dd-MMM-yy HH:mm:ss")
            .optionalEnd().toFormatter();

    // Used for correlating requests within a bundle.
    private String bundleRequestCorrelationId = null;

    private final FHIRValidator validator = FHIRValidator.validator(FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_VALIDATION_FAIL_FAST, Boolean.FALSE));

    public FHIRRestBundleHelper() {
    }

    private FHIROperationException buildUnsupportedResourceTypeException(String resourceTypeName) {
        String msg = "'" + resourceTypeName + "' is not a valid resource type.";
        Issue issue = OperationOutcome.Issue.builder()
                .severity(IssueSeverity.FATAL)
                .code(IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(EXT_BASE +  "not-supported-detail")
                            .value(Code.of("resource"))
                            .build())
                        .build())
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
        return new FHIROperationException(msg).withIssue(issue);
    }

    private FHIROperationException buildRestException(String msg, IssueType issueType) {
        return buildRestException(msg, issueType, IssueSeverity.FATAL);
    }

    private FHIROperationException buildRestException(String msg, IssueType issueType, IssueSeverity severity) {
        return new FHIROperationException(msg).withIssue(buildOperationOutcomeIssue(severity, issueType, msg));
    }

    /**
     * Builds an OperationOutcomeIssue with the respective values for some of the fields.
     */
    private OperationOutcome.Issue buildOperationOutcomeIssue(IssueSeverity severity, IssueType type, String msg) {
        return OperationOutcome.Issue.builder()
                .severity(severity)
                .code(type)
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build();
    }

    /**
     * Translate each bundle entry into a FHIRRestOperation implementation which can then
     * be executed in a particular order. No persistance operations are performed at this
     * stage.
     * @param requestBundle
     *            the bundle containing the request entries
     * @param validationResponseEntries
     *            the response entries with errors/warnings constructed during validation
     * @param failFast
     *            a boolean value indicating if processing should stop on first failure
     * @param localRefMap
     *            the map of local references to external references
     * @param bundleRequestProperties
     *            the bundle request properties
     * @param bundleRequestCorrelationId
     *            the bundle request correlation ID
     * @param skippableUpdates
     *            if true, and the bundle contains an update for which the resource content in the update matches the existing
     *            resource on the server, then skip the update; if false, then always attempt the updates specified in the bundle
     * @return a list of entries for the response bundle
     * @throws Exception
     */
    public List<FHIRRestOperation> translateBundleEntries(Bundle requestBundle, Map<Integer, Entry> validationResponseEntries,
            boolean failFast, Map<String, String> localRefMap, String bundleRequestCorrelationId, boolean skippableUpdates) throws Exception {
        log.entering(this.getClass().getName(), "translateBundleEntries");
        
        // The list of operations to execute, in the order we want to execute them
        List<FHIRRestOperation> result = new ArrayList<>(requestBundle.getEntry().size());
        try {

            // Group the request entries by request method; LinkedHashMap because order is important
            Map<HTTPVerb.Value, List<Integer>> requestEntriesByMethod = new LinkedHashMap<>(6);
            requestEntriesByMethod.put(HTTPVerb.Value.DELETE, new ArrayList<>());
            requestEntriesByMethod.put(HTTPVerb.Value.POST, new ArrayList<>());
            requestEntriesByMethod.put(HTTPVerb.Value.PUT, new ArrayList<>());
            requestEntriesByMethod.put(HTTPVerb.Value.GET, new ArrayList<>());
            requestEntriesByMethod.put(HTTPVerb.Value.PATCH, new ArrayList<>());
            requestEntriesByMethod.put(HTTPVerb.Value.HEAD, new ArrayList<>());
            for (int i = 0; i < requestBundle.getEntry().size(); i++) {
                if (validationResponseEntries.containsKey(i) &&
                        !validationResponseEntries.get(i).getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
                    // validation marked this entry as invalid, so wrap the validation response entry and skip it
                    result.add(new FHIRRestOperationValidationResponse(i, validationResponseEntries.get(i)));
                    continue;
                }
                Entry entry = requestBundle.getEntry().get(i);
                requestEntriesByMethod.get(entry.getRequest().getMethod().getValueAsEnum()).add(i);
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Bundle request indices to be processed: " +
                        "DELETE" + requestEntriesByMethod.get(HTTPVerb.Value.DELETE) + ", " +
                        "POST" + requestEntriesByMethod.get(HTTPVerb.Value.POST) + ", " +
                        "PUT" + requestEntriesByMethod.get(HTTPVerb.Value.PUT) + ", " +
                        "GET" + requestEntriesByMethod.get(HTTPVerb.Value.GET) + ", " +
                        "PATCH" + requestEntriesByMethod.get(HTTPVerb.Value.PATCH) + ", " +
                        "HEAD" + requestEntriesByMethod.get(HTTPVerb.Value.HEAD));
            }
            
            // Translate the individual bundle entry requests into corresponding FHIRRestOperation implementations
            for (Map.Entry<HTTPVerb.Value, List<Integer>> methodIndices : requestEntriesByMethod.entrySet()) {
                HTTPVerb.Value httpMethod = methodIndices.getKey();
                List<Integer> entryIndices = methodIndices.getValue();

                if (log.isLoggable(Level.FINER)) {
                    log.finer("Beginning processing for method: " + httpMethod);
                }

                // For PUT and DELETE requests, we need to sort the indices by the request url path value.
                if (httpMethod == HTTPVerb.Value.PUT || httpMethod == HTTPVerb.Value.DELETE) {
                    sortBundleRequestEntries(requestBundle, entryIndices);
                    if (log.isLoggable(Level.FINER)) {
                        log.finer("Sorted bundle request indices to be processed: "
                                + entryIndices.toString());
                    }
                }

                // Now visit each of the request entries using the list of indices obtained above.
                for (Integer entryIndex : entryIndices) {
                    Entry requestEntry = requestBundle.getEntry().get(entryIndex);
                    Entry.Request request = requestEntry.getRequest();

                    StringBuilder requestDescription = new StringBuilder();
                    long initialTime = System.currentTimeMillis();

                    try {
                        final FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                        final BundleType.Value bundleType = requestBundle.getType().getValueAsEnum();

                        // Build the description of the request for use in logging start/end later
                        requestDescription.append("entryIndex:[");
                        requestDescription.append(entryIndex);
                        requestDescription.append("] correlationId:[");
                        requestDescription.append(bundleRequestCorrelationId);
                        requestDescription.append("] method:[");
                        requestDescription.append(request.getMethod().getValue());
                        requestDescription.append("] uri:[");
                        requestDescription.append(request.getUrl().getValue());
                        requestDescription.append("]");

                        // Construct the absolute requestUri to be used for any response bundles associated
                        // with history and search requests.
                        final String absoluteUri = getAbsoluteUri(getRequestUri(), request.getUrl().getValue());

                        final FHIRRestOperation operation;
                        if (request.getMethod().equals(HTTPVerb.GET)) {
                            operation = processEntryForGet(entryIndex, request, requestURL, absoluteUri, requestDescription.toString(), initialTime);
                        } else if (request.getMethod().equals(HTTPVerb.POST)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            operation = processEntryForPost(requestEntry, validationResponseEntry,
                                    entryIndex, localRefMap, requestURL, absoluteUri, requestDescription.toString(), initialTime, (bundleType == BundleType.Value.TRANSACTION));
                        } else if (request.getMethod().equals(HTTPVerb.PUT)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            operation = processEntryForPut(requestEntry, validationResponseEntry,
                                    entryIndex, localRefMap, requestURL, absoluteUri, requestDescription.toString(), initialTime, skippableUpdates, (bundleType == BundleType.Value.TRANSACTION));
                        } else if (request.getMethod().equals(HTTPVerb.PATCH)) {
                            operation = processEntryForPatch(requestEntry, requestURL, entryIndex,
                                    requestDescription.toString(), initialTime, skippableUpdates);
                        } else if (request.getMethod().equals(HTTPVerb.DELETE)) {
                            operation = processEntryForDelete(entryIndex, requestURL, requestDescription.toString(), initialTime);
                        } else {
                            // Internal error, should not get here!
                            throw new IllegalStateException("Internal Server Error: reached an unexpected code location.");
                        }
                        
                        result.add(operation);
                    } catch (FHIROperationException e) {
                        if (failFast) {
                            String msg = "Error while processing request bundle.";
                            throw new FHIRRestBundledRequestException(msg, e).withIssue(e.getIssues());
                        }

                        // otherwise, record the processing error by adding a FHIRRestOperationIssue to
                        // the list. When executed, this will add the error to the response bundle
                        Status status;
                        if (e instanceof FHIRSearchException) {
                            status = Status.BAD_REQUEST;
                        } else {
                            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
                        }
                        

                        Entry issue = Entry.builder()
                                .resource(FHIRUtil.buildOperationOutcome(e, false))
                                .response(Entry.Response.builder()
                                    .status(string(Integer.toString(status.getStatusCode())))
                                    .build())
                                .build();
                        
                        // Record the issue so that it can be added to the response bundle later
                        result.add(new FHIRRestOperationIssue(entryIndex, requestDescription.toString(), initialTime, status, issue));
                    }
                } // end foreach method entry
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Finished processing for method: " + httpMethod);
                }
            } // end foreach method
            
        } finally {
            log.exiting(this.getClass().getName(), "processEntriesForMethod");
        }
        
        return result;
    }

    /**
     * Processes a request entry with a request method of Patch.
     *
     * @param requestEntry
     *            the request bundle entry
     * @param requestURL
     *            the request URL
     * @param entryIndex
     *            the bundle entry index of the bundle entry being processed
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @param skippableUpdate
     *            if true, and the resource content in the update matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @return the bundle entry response
     * @throws Exception
     */
    private FHIRRestOperation processEntryForPatch(Entry requestEntry, FHIRUrlParser requestURL, Integer entryIndex, String requestDescription,
            long initialTime, boolean skippableUpdate) throws Exception {
        String[] pathTokens = requestURL.getPathTokens();
        String resourceType = null;
        String resourceId = null;

        // Process a PATCH.
        if (pathTokens.length == 1) {
            // A single-part url would be a conditional update: <type>?<query>
            // This is not yet supported for PATCH requests.
            String msg = "Conditional update operation is not supported for PATCH requests.";
            throw buildRestException(msg, IssueType.NOT_SUPPORTED);
        } else if (pathTokens.length == 2) {
            // A two-part url would be a normal patch: <type>/<id>.
            resourceType = pathTokens[0];
            resourceId = pathTokens[1];
        } else {
            // A url with any other pattern is an error.
            String msg = "Request URL for bundled PATCH request should have path part with two tokens (<resourceType>/<id>).";
            throw buildRestException(msg, IssueType.INVALID);
        }

        checkResourceType(resourceType);

        if (!requestEntry.getResource().is(Parameters.class)) {
            String msg="Request resource type for PATCH request must be type 'Parameters'";
            throw buildRestException(msg, IssueType.INVALID);
        }

        Parameters parameters = requestEntry.getResource().as(Parameters.class);
        FHIRPatch patch = FHIRPathPatch.from(parameters);

        // We don't perform the actual operation here, just generate the command
        // we want to execute later
        return new FHIRRestOperationPatch(entryIndex, requestDescription, initialTime, resourceType, resourceId, patch, null, null, skippableUpdate);
    }

    /**
     * Processes a request entry with a request method of GET.
     *
     * @param entryRequest
     *            the request portion of the corresponding request bundle entry
     * @param requestURL
     *            the request URL
     * @param absoluteUri
     *            the absolute URI
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @return the bundle entry response
     * @throws Exception
     */
    private FHIRRestOperation processEntryForGet(int entryIndex, Entry.Request entryRequest, FHIRUrlParser requestURL, String absoluteUri,
            String requestDescription, long initialTime) throws Exception {

        final FHIRRestOperation result;
        String[] pathTokens = requestURL.getPathTokens();
        MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();

        // Process a GET (read, vread, history, search, etc.).
        // Determine the type of request from the path tokens.
        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
            // This is a custom operation request.

            // Chop off the '$' and save the name
            String operationName = pathTokens[pathTokens.length - 1].substring(1);
            final String GET = "GET";
            final Entry validationResponseEntry = null; // No validation for GET operations
            FHIROperationContext operationContext;
            switch (pathTokens.length) {
            case 1:
                operationContext = FHIROperationContext.createSystemOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, GET, null, null, null, operationName, null, queryParams);
                break;
            case 2:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createResourceTypeOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, GET, pathTokens[0], null, null, operationName, null, queryParams);
                break;
            case 3:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createInstanceOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, GET, pathTokens[0], pathTokens[1], null, operationName, null, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }
        } else if (pathTokens.length == 1) {
            // This is a 'search' request.
            if ("_search".equals(pathTokens[0])) {
                result = new FHIRRestOperationSearch(entryIndex, requestDescription, initialTime, "Resource", null, null, queryParams, absoluteUri, null, true);
            } else {
                checkResourceType(pathTokens[0]);
                result = new FHIRRestOperationSearch(entryIndex, requestDescription, initialTime, pathTokens[0], null, null, queryParams, absoluteUri, null, true);
            }
        } else if (pathTokens.length == 2) {
            // This is a 'read' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestOperationRead(entryIndex, requestDescription, initialTime, pathTokens[0], pathTokens[1], true, false, null, null, true);
        } else if (pathTokens.length == 3) {
            if ("_history".equals(pathTokens[2])) {
                // This is a 'history' request.
                checkResourceType(pathTokens[0]);
                result = new FHIRRestOperationHistory(entryIndex, requestDescription, initialTime, pathTokens[0], pathTokens[1], queryParams, absoluteUri);
            } else {
                // This is a compartment based search
                checkResourceType(pathTokens[2]);
                result = new FHIRRestOperationSearch(entryIndex, requestDescription, initialTime, pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri, null, true);
            }
        } else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
            // This is a 'vread' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestOperationVRead(entryIndex, requestDescription, initialTime, pathTokens[0], pathTokens[1], pathTokens[3], null);
        } else {
            String msg = "Unrecognized path in request URL: " + requestURL.getPath();
            throw buildRestException(msg, IssueType.NOT_FOUND);
        }

        return result;
    }

    /**
     * TODO this shouldn't be part of the command build, but instead only called during command execution
     * common update to the operationContext
     * @param operationContext
     * @param method
     */
    private void updateOperationContext(FHIROperationContext operationContext, String method) {
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
        operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
        operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
        operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
        operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, method);
    }

    /**
     * Processes a request entry with a request method of POST.
     *
     * @param requestEntry
     *            the request bundle entry
     * @param validationResponseEntry
     *            the response bundle entry created during validation, possibly null
     * @param entryIndex
     *            the bundle entry index of the bundle entry being processed
     * @param localRefMap
     *            the map of local references to external references
     * @param requestURL
     *            the request URL
     * @param absoluteUri
     *            the absolute URI
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @return the bundle entry response
     * @throws Exception
     */
    private FHIRRestOperation processEntryForPost(Entry requestEntry, Entry validationResponseEntry,
            Integer entryIndex, Map<String, String> localRefMap, FHIRUrlParser requestURL, String absoluteUri, String requestDescription, long initialTime, boolean transaction)
            throws Exception {

        final FHIRRestOperation result;
        String[] pathTokens = requestURL.getPathTokens();
        MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();
        Resource resource = null;

        // Process a POST (create or search, or custom operation).
        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
            // This is a custom operation request.

            // Chop off the '$' and save the name.
            String operationName = pathTokens[pathTokens.length - 1].substring(1);

            // Retrieve the resource from the request entry.
            resource = requestEntry.getResource();

            FHIROperationContext operationContext;
            final String POST = "POST";
            switch (pathTokens.length) {
            case 1:
                operationContext = FHIROperationContext.createSystemOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, POST, null, null, null, operationName, resource, queryParams);
                break;
            case 2:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createResourceTypeOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, POST, pathTokens[0], null, null, operationName, resource, queryParams);
                break;
            case 3:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createInstanceOperationContext();
                result = new FHIRRestOperationInvoke(entryIndex, validationResponseEntry, requestDescription, initialTime, operationContext, POST, pathTokens[0], pathTokens[1], null, operationName, resource, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }
        } else if (pathTokens.length == 2 && "_search".equals(pathTokens[1])) {
            // This is a 'search' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestOperationSearch(entryIndex, requestDescription, initialTime, pathTokens[0], null, null, queryParams, absoluteUri, null, true);
        } else if (pathTokens.length == 1) {
            // This is a 'create' request.
            checkResourceType(pathTokens[0]);

            // Retrieve the local identifier from the request entry (if present).
            String localIdentifier = retrieveLocalIdentifier(requestEntry);

            // Retrieve the resource from the request entry.
            resource = requestEntry.getResource();
            if (resource == null) {
                String msg = "BundleEntry.resource is required for bundled create requests.";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }

            // Determine if we have a pre-generated resource ID
            String resourceId = retrieveGeneratedIdentifier(localRefMap, localIdentifier);

            // Perform the 'create' or 'update' operation.
            Entry.Request request = requestEntry.getRequest();
            String ifNoneExist = request.getIfNoneExist() != null
                    && request.getIfNoneExist().getValue() != null
                    && !request.getIfNoneExist().getValue().isEmpty() ? request.getIfNoneExist().getValue() : null;
            if (ifNoneExist != null || resourceId == null) {
                result = new FHIRRestOperationCreate(entryIndex, validationResponseEntry, requestDescription, initialTime, pathTokens[0], resource, ifNoneExist, !DO_VALIDATION, localIdentifier);
            } else {
                resource = resource.toBuilder().id(resourceId).build();
                // Skip validation because its already been performed.
                result = new FHIRRestOperationUpdate(entryIndex, validationResponseEntry, requestDescription, initialTime, pathTokens[0], resourceId, resource, null, null, !SKIPPABLE_UPDATE, !DO_VALIDATION, localIdentifier);
            }

            // If a local identifier was present and not already mapped to its external identifier, add mapping.
            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                // TODO can we use resource here instead of ior.getResource(). This must be related to id generation, which
                // the persistence layer used to manage. Now we have to handle id generation/assignment here, which also helps
                // to improve separation of concerns because this is business logic which the persistence
                // layer shouldn't be involved with.
                addLocalRefMapping(localRefMap, localIdentifier, null, resource);
            }

        } else {
            String msg = "Request URL for bundled create requests should have a path with exactly one token (<resourceType>).";
            throw buildRestException(msg, IssueType.NOT_FOUND);
        }
        
        return result;
    }

    /**
     * Processes a request entry with a request method of PUT.
     *
     * @param requestEntry
     *            the request bundle entry
     * @param validationResponseEntry
     *            the response bundle entry created during validation, possibly null
     * @param entryIndex
     *            the bundle entry index of the bundle entry being processed
     * @param localRefMap
     *            the map of local references to external references
     * @param requestURL
     *            the request URL
     * @param absoluteUri
     *            the absolute URI
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @param skippableUpdate
     *            if true, and the resource content in the update matches the existing resource on the server, then skip the update;
     *            if false, then always attempt the update
     * @return the bundle entry response
     * @throws Exception
     */
    private FHIRRestOperation processEntryForPut(Entry requestEntry, Entry validationResponseEntry,
            Integer entryIndex, Map<String, String> localRefMap, FHIRUrlParser requestURL, String absoluteUri, String requestDescription,
            long initialTime, boolean skippableUpdate, boolean transaction) throws Exception {

        final FHIRRestOperation result;
        String[] pathTokens = requestURL.getPathTokens();
        String type = null;
        String id = null;

        // Process a PUT (update).
        if (pathTokens.length == 1) {
            // A single-part url would be a conditional update: <type>?<query>
            type = pathTokens[0];
            if (requestURL.getQuery() == null || requestURL.getQuery().isEmpty()) {
                String msg = "A search query string is required for a conditional update operation.";
                throw buildRestException(msg, IssueType.INVALID);
            }
        } else if (pathTokens.length == 2) {
            // A two-part url would be a normal update: <type>/<id>.
            type = pathTokens[0];
            id = pathTokens[1];
        } else {
            // A url with any other pattern is an error.
            String msg = "Request URL for bundled PUT request should have path part with either one or two tokens (<resourceType> or <resourceType>/<id>).";
            throw buildRestException(msg, IssueType.INVALID);
        }

        checkResourceType(type);

        // Retrieve the resource from the request entry.
        Resource resource = requestEntry.getResource();

        // Perform the 'update' operation.
        String ifMatchBundleValue = null;
        if (requestEntry.getRequest().getIfMatch() != null) {
            ifMatchBundleValue = requestEntry.getRequest().getIfMatch().getValue();
        }
        
        // If this was a conditional update, and if a local identifier was present and not already mapped to its external identifier, add mapping.
        String localIdentifier = null;
        if (pathTokens.length == 1) {
            localIdentifier = retrieveLocalIdentifier(requestEntry);
        }
        result = new FHIRRestOperationUpdate(entryIndex, validationResponseEntry, requestDescription, initialTime, type, id, resource, ifMatchBundleValue, requestURL.getQuery(), skippableUpdate, !DO_VALIDATION, localIdentifier);


        return result;
    }

    /**
     * Processes a request entry with a request method of DELETE.
     *
     * @param requestURL
     *            the request URL
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @return the bundle entry response
     * @throws Exception
     */
    private FHIRRestOperation processEntryForDelete(int entryIndex, FHIRUrlParser requestURL, String requestDescription, long initialTime) throws Exception {

        final FHIRRestOperation result;
        String[] pathTokens = requestURL.getPathTokens();
        String type = null;
        String id = null;

        // Process a DELETE.
        if (pathTokens.length == 1) {
            // A single-part url would be a conditional delete: <type>?<query>
            type = pathTokens[0];
            if (requestURL.getQuery() == null || requestURL.getQuery().isEmpty()) {
                String msg = "A search query string is required for a conditional delete operation.";
                throw buildRestException(msg, IssueType.INVALID);
            }
        } else if (pathTokens.length == 2) {
            type = pathTokens[0];
            id = pathTokens[1];
        } else {
            String msg = "Request URL for bundled DELETE request should have path part with one or two tokens (<resourceType> or <resourceType>/<id>).";
            throw buildRestException(msg, IssueType.INVALID);
        }

        checkResourceType(type);

        // Perform the 'delete' operation.
        result = new FHIRRestOperationDelete(entryIndex, requestDescription, initialTime, type, id, requestURL.getQuery());
        return result;
    }
    
    public void checkResourceType(String type) throws FHIROperationException {
        if (!ModelSupport.isResourceType(type)) {
            throw buildUnsupportedResourceTypeException(type);
        }
        if (!ModelSupport.isConcreteResourceType(type)) {
            log.warning("Use of abstract resource types like '" + type + "' in FHIR URLs is deprecated and will be removed in a future release");
        }
    }

    /**
     * This function sorts the request entries in the specified bundle, based on the path part of the entry's 'url'
     * field.
     *
     * @param bundle
     *            the bundle containing the request entries to be sorted.
     * @return an array of Integer which provides the "sorted" ordering of request entry index values.
     */
    private void sortBundleRequestEntries(Bundle bundle, List<Integer> indices) {
        // Sort the list of indices based on the contents of their entries in the bundle.
        Collections.sort(indices, new BundleEntryComparator(bundle.getEntry()));
    }

    private static class BundleEntryComparator implements Comparator<Integer> {
        private List<Entry> entries;

        public BundleEntryComparator(List<Entry> entries) {
            this.entries = entries;
        }

        @Override
        public int compare(Integer indexA, Integer indexB) {
            Entry a = entries.get(indexA);
            Entry b = entries.get(indexB);
            String pathA = getUrlPath(a);
            String pathB = getUrlPath(b);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Comparing request entry URL paths: " + pathA + ", " + pathB);
            }
            if (pathA != null && pathB != null) {
                return pathA.compareTo(pathB);
            } else if (pathA != null) {
                return 1;
            } else if (pathB != null) {
                return -1;
            }
            return 0;
        }
    }

    /**
     * Returns the specified BundleEntry's path component of the 'url' field.
     *
     * @param entry
     *            the bundle entry
     * @return the bundle entry's 'url' field's path component
     */
    private static String getUrlPath(Entry entry) {
        String path = null;
        Entry.Request request = entry.getRequest();
        if (request != null) {
            if (request.getUrl() != null && request.getUrl().getValue() != null) {
                FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                path = requestURL.getPath();
            }
        }

        return path;
    }

    /**
     * This method will add a mapping to the local-to-external identifier map if the specified localIdentifier is
     * non-null.
     *
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @param externalIdentifier
     *            the externalIdentifier previously obtained for the resource (may be null
     *            if resource is not null)
     * @param resource
     *            the resource for which an external identifier will be built (may be null
     *            if externalIdentifier is not null)
     */
    private void addLocalRefMapping(Map<String, String> localRefMap, String localIdentifier, String externalIdentifier, Resource resource) {
        if (localIdentifier != null) {
            if (externalIdentifier == null) {
                externalIdentifier = ModelSupport.getTypeName(resource.getClass()) + "/" + resource.getId();
            }
            localRefMap.put(localIdentifier, externalIdentifier);
            if (log.isLoggable(Level.FINER)) {
                log.finer("Added local/ext identifier mapping: " + localIdentifier + " --> " + externalIdentifier);
            }
        }
    }

    /**
     * This method will retrieve the local identifier associated with the specified bundle request entry, or return null
     * if the fullUrl field is not specified or doesn't contain a local identifier.
     *
     * @param requestEntry
     *            the bundle request entry
     * @return the local identifier
     */
    private String retrieveLocalIdentifier(Entry requestEntry) {
        String localIdentifier = null;
        if (requestEntry.getFullUrl() != null) {
            String fullUrl = requestEntry.getFullUrl().getValue();
            if (fullUrl != null && fullUrl.startsWith(LOCAL_REF_PREFIX)) {
                localIdentifier = fullUrl;
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Request entry contains local identifier: " + localIdentifier);
                }
            }
        }
        return localIdentifier;
    }

    /**
     * This method will retrieve the generated identifier associated with the specified local identifier from the
     * local ref map, or return null if there is no mapping for the local identifier.
     *
     * @param localRefMap
     *            the map containing the local-to-external identifier mappings
     * @param localIdentifier
     *            the localIdentifier previously obtained for the resource
     * @return the generated identifier
     */
    private String retrieveGeneratedIdentifier(Map<String, String> localRefMap, String localIdentifier) {
        String generatedIdentifier = null;
        String externalIdentifier = localRefMap.get(localIdentifier);
        if (externalIdentifier != null) {
            int index = externalIdentifier.indexOf("/");
            if (index > -1) {
                generatedIdentifier = externalIdentifier.substring(index+1);
            }
        }
        return generatedIdentifier;
    }

    /**
     * This function will build an absolute URI from the specified base URI and relative URI.
     *
     * @param baseUri
     *            the base URI to be used; this will be of the form <scheme>://<host>:<port>/<context-root>
     * @param relativeUri
     *            the path and query parts
     * @return the full URI value as a String
     */
    private String getAbsoluteUri(String baseUri, String relativeUri) {
        StringBuilder fullUri = new StringBuilder();
        fullUri.append(baseUri);
        if (!baseUri.endsWith("/")) {
            fullUri.append("/");
        }
        fullUri.append((relativeUri.startsWith("/") ? relativeUri.substring(1) : relativeUri));
        return fullUri.toString();
    }

    /**
     * Creates a bundle that will hold results for a search operation.
     *
     * @param resources
     *            the list of resources to include in the bundle
     * @param searchContext
     *            the FHIRSearchContext object associated with the search
     * @param type
     *            the name of the resource type being searched
     * @return the bundle
     * @throws Exception
     */
    Bundle createSearchBundle(List<Resource> resources, FHIRSearchContext searchContext, String type) throws Exception {

        // throws if we have a count of more than 2,147,483,647 resources
        UnsignedInt totalCount = searchContext.getTotalCount() != null ? UnsignedInt.of(searchContext.getTotalCount()) : null;
        // generate ID for this bundle and set total
        Bundle.Builder bundleBuilder = Bundle.builder()
                                            .type(BundleType.SEARCHSET)
                                            .id(UUID.randomUUID().toString())
                                            .total(totalCount);

        if (resources.size() > 0) {
            // Calculate how many resources are 'match' mode
            int matchResourceCount = searchContext.getMatchCount();
            List<Resource> matchResources = resources.subList(0,  matchResourceCount);

            // Check if too many included resources
            if (resources.size() > matchResourceCount + searchContext.getMaxPageIncludeCount()) {
                throw buildRestException("Number of returned 'include' resources exceeds allowable limit of " + searchContext.getMaxPageIncludeCount(),
                    IssueType.BUSINESS_RULE, IssueSeverity.ERROR);
            }

            // Find chained search parameters and find reference search parameters containing only a logical ID
            List<QueryParameter> chainedSearchParameters = new ArrayList<>();
            List<QueryParameter> logicalIdReferenceSearchParameters = new ArrayList<>();
            for (QueryParameter queryParameter : searchContext.getSearchParameters()) {
                if (!queryParameter.isReverseChained()) {
                    if (queryParameter.isChained()) {
                        chainedSearchParameters.add(queryParameter);
                    } else if (SearchConstants.Type.REFERENCE == queryParameter.getType()) {
                        // Look for logical ID-only value
                        for (QueryParameterValue value : queryParameter.getValues()) {
                            ReferenceValue refVal = ReferenceUtil.createReferenceValueFrom(value.getValueString(), null, ReferenceUtil.getBaseUrl(null));
                            if (refVal.getType() == ReferenceType.LITERAL_RELATIVE && refVal.getTargetResourceType() == null) {
                                logicalIdReferenceSearchParameters.add(queryParameter);
                                break;
                            }
                        }
                    }
                }
            }
            List<Issue> issues = new ArrayList<>();
            if (searchContext.getOutcomeIssues() != null) {
                issues.addAll(searchContext.getOutcomeIssues());
            }
            if (!chainedSearchParameters.isEmpty() || !logicalIdReferenceSearchParameters.isEmpty()) {
                // Check 'match' resources for versioned references in chain search parameter fields and
                // multiple resource types with matching logical ID in reference search parameter fields.
                issues = performSearchReferenceChecks(type, chainedSearchParameters, logicalIdReferenceSearchParameters, matchResources);
            }

            for (Resource resource : resources) {
                Entry.Builder entryBuilder = Entry.builder();
                if (resource != null) {
                    if (resource.getId() != null) {
                        entryBuilder.id(resource.getId());
                        entryBuilder.fullUrl(Uri.of(getRequestBaseUri(type) + "/" + resource.getClass().getSimpleName() + "/" + resource.getId()));
                    } else {
                        String msg = "A resource with no id was found.";
                        log.warning(msg);
                        issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, msg));
                    }
                    entryBuilder.resource(resource);
                } else {
                    String msg = "A resource with no data was found.";
                    log.warning(msg);
                    issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, msg));
                }
                // Search mode is determined by the matchResourceCount, which will be decremented each time through the loop.
                // If the count is greater than 0, the mode is MATCH. If less than or equal to 0, the mode is INCLUDE.
                Entry entry = entryBuilder
                    .search(Search.builder()
                        .mode(matchResourceCount-- > 0 ? SearchEntryMode.MATCH : SearchEntryMode.INCLUDE)
                        .score(Decimal.of("1"))
                        .build())
                    .build();
                bundleBuilder.entry(entry);
            }

            if (!issues.isEmpty()) {
                // Add OperationOutcome resource containing issues
                bundleBuilder.entry(
                    Entry.builder()
                    .search(Search.builder().mode(SearchEntryMode.OUTCOME).build())
                    .resource(FHIRUtil.buildOperationOutcome(issues))
                    .build());
            }
        }

        Bundle bundle = bundleBuilder.build();

        // Add the SUBSETTED tag, if the _elements search result parameter was applied to limit elements included in
        // returned resources or _summary is required.
        if (searchContext.hasElementsParameters()
                || (searchContext.hasSummaryParameter() && !searchContext.getSummaryParameter().equals(SummaryValueSet.FALSE))) {
            bundle = FHIRUtil.addTag(bundle, SearchConstants.SUBSETTED_TAG);
        }

        return bundle;
    }

    /**
     * For chained search, check 'match' resources for existence of a versioned reference in the field
     * associated with the chain search parameter.
     *
     * For reference search specifying logical ID only, check 'match' resources for existence of multiple
     * resource types containing the same logical ID in the field associated with the reference search parameter.
     *
     * @param resourceType
     *            The search resource type.
     * @param chainQueryParameters
     *            The chained query parameters. These will be mutually exclusive of the logicalIdReferenceQueryParameters.
     * @param logicalIdReferenceQueryParameters
     *            The list of reference query parameters that only specified a logical ID.
     * @param matchResources
     *            The list of 'match' resources to check.
     * @return
     *            A list of Issues, one per resource in which a versioned reference is found.
     * @throws Exception if multiple resource types containing the same logical ID are found
     */
    private List<Issue> performSearchReferenceChecks(String resourceType, List<QueryParameter> chainQueryParameters,
            List<QueryParameter> logicalIdReferenceQueryParameters, List<Resource> matchResources) throws Exception {
        List<Issue> issues = new ArrayList<>();

        if (!chainQueryParameters.isEmpty() || !logicalIdReferenceQueryParameters.isEmpty()) {
            // Build a map of parameter name to SearchParameter for all queryParameters.
            // Since the search was successful, we can assume search parameters exist, are valid, and of type Reference.
            // However, if this is a whole-system search, we will need to get the SearchParameters based on
            // the resource type returned.
            Map<QueryParameter, SearchParameter> searchParameterMap = new HashMap<>();
            if (!Resource.class.getSimpleName().equals(resourceType)) {
                Class<? extends Resource> resourceTypeClass = ModelSupport.getResourceType(resourceType);
                for (QueryParameter queryParameter : chainQueryParameters) {
                    searchParameterMap.put(queryParameter, SearchUtil.getSearchParameter(resourceTypeClass, queryParameter.getCode()));
                }
                for (QueryParameter queryParameter : logicalIdReferenceQueryParameters) {
                    searchParameterMap.put(queryParameter, SearchUtil.getSearchParameter(resourceTypeClass, queryParameter.getCode()));
                }
            }

            List<QueryParameter> queryParameters = new ArrayList<>(chainQueryParameters);
            queryParameters.addAll(logicalIdReferenceQueryParameters);
            Map<String, String> logicalIdToTypeMap = new HashMap<>();

            // Loop through the resources, looking for versioned references and references to multiple resource types for the same logical ID
            for (Resource resource : matchResources) {
                FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
                EvaluationContext evaluationContext = new EvaluationContext(resource);
                for (QueryParameter queryParameter : queryParameters) {
                    SearchParameter searchParameter = searchParameterMap.get(queryParameter);
                    if (searchParameter == null) {
                        searchParameter = SearchUtil.getSearchParameter(resource.getClass(), queryParameter.getCode());
                    }

                    // For logical ID check, only need to look at search parameters with more than one target resource type
                    if (logicalIdReferenceQueryParameters.contains(queryParameter) && searchParameter.getTarget().size() == 1) {
                        continue;
                    }

                    Collection<FHIRPathNode> nodes = evaluator.evaluate(evaluationContext, searchParameter.getExpression().getValue());
                    for (FHIRPathNode node : nodes) {
                        Reference reference = node.asElementNode().element().as(Reference.class);
                        ReferenceValue rv = ReferenceUtil.createReferenceValueFrom(reference, ReferenceUtil.getBaseUrl(null));
                        if (chainQueryParameters.contains(queryParameter) && rv.getVersion() != null &&
                                (rv.getTargetResourceType() == null || rv.getTargetResourceType().equals(queryParameter.getModifierResourceTypeName()))) {
                            // Found versioned reference value
                            String msg = "Resource with id '" + resource.getId() +
                                    "' contains a versioned reference in an element used for chained search, but chained search does not act on versioned references.";
                            issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, msg, node.path()));
                        } else if (logicalIdReferenceQueryParameters.contains(queryParameter) && rv.getTargetResourceType() != null &&
                                !rv.getTargetResourceType().equals(logicalIdToTypeMap.computeIfAbsent(queryParameter.getCode() + "|" + rv.getValue(), v -> rv.getTargetResourceType()))) {
                            // Found multiple resource types this logical ID
                            String msg = "Multiple resource type matches found for logical ID '" + rv.getValue() +
                                    "' for search parameter '" + queryParameter.getCode() + "'.";
                            throw buildRestException(msg, IssueType.INVALID, IssueSeverity.ERROR);
                        }
                    }
                }
            }
        }

        return issues;
    }

    /**
     * Get the original request URI from either the HttpServletRequest or a configured Header (in case of re-writing proxies).
     *
     * <p>When the 'fhirServer/core/originalRequestUriHeaderName' property is empty, this method returns the equivalent of
     * uriInfo.getRequestUri().toString(), except that uriInfo.getRequestUri() will throw an IllegalArgumentException
     * when the query string portion contains a vertical bar | character. The vertical bar is one known case of a special character
     * causing the exception. There could be others.
     *
     * @return String The complete request URI
     * @throws Exception if an error occurs while reading the config
     */
    private String getRequestUri() throws Exception {
        return FHIRRequestContext.get().getOriginalRequestUri();
    }

    /**
     * This method returns the "base URI" associated with the current request. For example, if a client invoked POST
     * https://myhost:9443/fhir-server/api/v4/Patient to create a Patient resource, this method would return
     * "https://myhost:9443/fhir-server/api/v4".
     *
     * @return The base endpoint URI associated with the current request.
     * @throws Exception if an error occurs while reading the config
     * @implNote This method uses {@link #getRequestUri()} to get the original request URI and then strips it to the
     *           <a href="https://www.hl7.org/fhir/http.html#general">Service Base URL</a>
     */
    private String getRequestBaseUri(String type) throws Exception {
        String baseUri = null;

        String requestUri = getRequestUri();

        // Strip off everything after the path
        int queryPathSeparatorLoc = requestUri.indexOf("?");
        if (queryPathSeparatorLoc != -1) {
            baseUri = requestUri.substring(0, queryPathSeparatorLoc);
        } else {
            baseUri = requestUri;
        }

        // Strip off any path elements after the base
        if (type != null && !type.isEmpty()) {
            int resourceNamePathLocation = baseUri.lastIndexOf("/" + type);
            if (resourceNamePathLocation != -1) {
                baseUri = requestUri.substring(0, resourceNamePathLocation);
            } else {
                // Assume the request was a batch/transaction; nothing to strip
            }
        }

        // Strip any path segments for whole-system interactions (in case of whole-system search, "Resource" is passed as the type, or $everything-based search)
        if (type == null || type.isEmpty() || "Resource".equals(type) || baseUri.contains("$everything")) {
            if (baseUri.endsWith("/_search")) {
                baseUri = baseUri.substring(0, baseUri.length() - "/_search".length());
            } else if (baseUri.endsWith("/_history")) {
                baseUri = baseUri.substring(0, baseUri.length() - "/_history".length());
            } else if (baseUri.contains("/$")) {
                baseUri = baseUri.substring(0, baseUri.lastIndexOf("/$"));
            }
        }

        return baseUri;
    }
}