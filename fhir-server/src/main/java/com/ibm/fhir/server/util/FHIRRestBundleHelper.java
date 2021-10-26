/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.core.FHIRConstants.EXT_BASE;
import static com.ibm.fhir.model.type.String.string;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.patch.FHIRPatch;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.patch.FHIRPathPatch;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.server.exception.FHIRRestBundledRequestException;
import com.ibm.fhir.server.rest.FHIRRestInteraction;
import com.ibm.fhir.server.rest.FHIRRestInteractionCreate;
import com.ibm.fhir.server.rest.FHIRRestInteractionDelete;
import com.ibm.fhir.server.rest.FHIRRestInteractionHistory;
import com.ibm.fhir.server.rest.FHIRRestInteractionInvoke;
import com.ibm.fhir.server.rest.FHIRRestInteractionIssue;
import com.ibm.fhir.server.rest.FHIRRestInteractionPatch;
import com.ibm.fhir.server.rest.FHIRRestInteractionRead;
import com.ibm.fhir.server.rest.FHIRRestInteractionSearch;
import com.ibm.fhir.server.rest.FHIRRestInteractionUpdate;
import com.ibm.fhir.server.rest.FHIRRestInteractionVRead;
import com.ibm.fhir.server.rest.FHIRRestInteractionValidationResponse;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * Helper for processing bundle entries. Does not perform any persistence operations,
 * but instead helps to translate the request into a {@link FHIRRestInteraction} implementation
 * which can be executed at a later time.
 */
public class FHIRRestBundleHelper {
    private static final Logger log =
            java.util.logging.Logger.getLogger(FHIRRestBundleHelper.class.getName());

    private static final String LOCAL_REF_PREFIX = "urn:";
    private static final com.ibm.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));

    // Constant for indicating the need to validate a resource
    public static final boolean DO_VALIDATION = true;
    // Constant for indicating whether an update can be skipped when the requested update resource matches the existing one
    public static final boolean SKIPPABLE_UPDATE = true;

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

    // Access to helper functions for creating event objects
    private final FHIRResourceHelpers helpers;

    /**
     * Public constructor
     * @param helpers
     */
    public FHIRRestBundleHelper(FHIRResourceHelpers helpers) {
        this.helpers = helpers;
    }

    /**
     * Construct a FHIROperationException indicating the given resourceTypeName
     * is not supported
     * @param resourceTypeName
     * @return
     */
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

    /**
     * Construct a FHIROperationException with an IssueSeverity of FATAL
     * @param msg
     * @param issueType
     * @return
     */
    private FHIROperationException buildRestException(String msg, IssueType issueType) {
        return buildRestException(msg, issueType, IssueSeverity.FATAL);
    }

    /**
     * Construct a FHIROperationException configured with an issue defined
     * using the severity, issueType and msg parameters
     * @param msg
     * @param issueType
     * @param severity
     * @return
     */
    private FHIROperationException buildRestException(String msg, IssueType issueType, IssueSeverity severity) {
        return new FHIROperationException(msg).withIssue(buildOperationOutcomeIssue(severity, issueType, msg));
    }

    /**
     * Build an OperationOutcomeIssue with the respective values for some of the fields.
     * @param severity
     * @param type
     * @param msg
     * @return
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
     * @param bundleRequestCorrelationId
     *            the bundle request correlation ID
     * @param skippableUpdates
     *            if true, and the bundle contains an update for which the resource content in the update matches the existing
     *            resource on the server, then skip the update; if false, then always attempt the updates specified in the bundle
     * @return a list of FHIRRestInteraction objects to be processed in order
     * @throws Exception
     */
    public List<FHIRRestInteraction> translateBundleEntries(Bundle requestBundle, Map<Integer, Entry> validationResponseEntries,
            boolean failFast, String bundleRequestCorrelationId, boolean skippableUpdates) throws Exception {
        log.entering(this.getClass().getName(), "translateBundleEntries");

        // The list of operations to execute, in the order we want to execute them
        List<FHIRRestInteraction> result = new ArrayList<>(requestBundle.getEntry().size());
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
                    // validation marked this entry as invalid, so wrap the validation response entry and skip it, but provide a description
                    // so it gets logged properly
                    final Request request = requestBundle.getEntry().get(i).getRequest();
                    final long initialTime = System.currentTimeMillis();
                    final String method = request != null && request.getMethod() != null ? request.getMethod().getValue() : "null";
                    final String requestURL = request != null && request.getUrl() != null ? request.getUrl().getValue() : "null";
                    final StringBuilder requestDescription = new StringBuilder();
                    requestDescription.append("entryIndex:[");
                    requestDescription.append(i);
                    requestDescription.append("] correlationId:[");
                    requestDescription.append(bundleRequestCorrelationId);
                    requestDescription.append("] method:[");
                    requestDescription.append(method);
                    requestDescription.append("] uri:[");
                    requestDescription.append(requestURL);
                    requestDescription.append("]");

                    result.add(new FHIRRestInteractionValidationResponse(i, validationResponseEntries.get(i), requestDescription.toString(), initialTime));
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

                        final FHIRRestInteraction operation;
                        if (request.getMethod().equals(HTTPVerb.GET)) {
                            operation = processEntryForGet(entryIndex, request, requestURL, absoluteUri, requestDescription.toString(), initialTime);
                        } else if (request.getMethod().equals(HTTPVerb.POST)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            operation = processEntryForPost(requestEntry, validationResponseEntry,
                                    entryIndex, requestURL, absoluteUri, requestDescription.toString(), initialTime, (bundleType == BundleType.Value.TRANSACTION));
                        } else if (request.getMethod().equals(HTTPVerb.PUT)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            operation = processEntryForPut(requestEntry, validationResponseEntry,
                                    entryIndex, requestURL, absoluteUri, requestDescription.toString(), initialTime, skippableUpdates, (bundleType == BundleType.Value.TRANSACTION));
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
                        result.add(new FHIRRestInteractionIssue(entryIndex, initialTime, status, issue));
                    }
                } // end foreach method entry
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Finished translation for method: " + httpMethod);
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
     * @return a FHIRRestInteraction representing the intended interaction
     * @throws Exception
     */
    private FHIRRestInteraction processEntryForPatch(Entry requestEntry, FHIRUrlParser requestURL, Integer entryIndex, String requestDescription,
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

        // Extract the local identifier which may be used by other resources in the bundle to reference this resource
        String localIdentifier = retrieveLocalIdentifier(requestEntry);

        // Build the event we'll use when executing the interaction command
        // - the resource gets injected later when we have it
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, helpers.buildPersistenceEventProperties(resourceType, resourceId, null, null));

        // We don't perform the actual operation here, just generate the command
        // we want to execute later
        return new FHIRRestInteractionPatch(entryIndex, event, requestDescription, requestURL, initialTime, resourceType, resourceId, patch, null, null, skippableUpdate, localIdentifier);
    }

    /**
     * Processes a request entry with a request method of GET.
     * @param entryIndex
     *            the index of the entry in the request bundle
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
     * @return a FHIRRestInteraction representing the intended interaction
     * @throws Exception
     */
    private FHIRRestInteraction processEntryForGet(int entryIndex, Entry.Request entryRequest, FHIRUrlParser requestURL, String absoluteUri,
            String requestDescription, long initialTime) throws Exception {

        final FHIRRestInteraction result;
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
                operationContext = FHIROperationContext.createSystemOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, GET, null, null, null, null, queryParams);
                break;
            case 2:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createResourceTypeOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, GET, pathTokens[0], null, null, null, queryParams);
                break;
            case 3:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createInstanceOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, GET, pathTokens[0], pathTokens[1], null, null, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }
        } else if (pathTokens.length == 1) {
            // This is a 'search' request.
            if ("_search".equals(pathTokens[0])) {
                result = new FHIRRestInteractionSearch(entryIndex, requestDescription, requestURL, initialTime, "Resource", null, null, queryParams, absoluteUri, null, true);
            } else {
                checkResourceType(pathTokens[0]);
                result = new FHIRRestInteractionSearch(entryIndex, requestDescription, requestURL, initialTime, pathTokens[0], null, null, queryParams, absoluteUri, null, true);
            }
        } else if (pathTokens.length == 2) {
            // This is a 'read' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestInteractionRead(entryIndex, requestDescription, requestURL, initialTime, pathTokens[0], pathTokens[1], true, false, null, null, true);
        } else if (pathTokens.length == 3) {
            if ("_history".equals(pathTokens[2])) {
                // This is a 'history' request.
                checkResourceType(pathTokens[0]);
                result = new FHIRRestInteractionHistory(entryIndex, requestDescription, requestURL, initialTime, pathTokens[0], pathTokens[1], queryParams, absoluteUri);
            } else {
                // This is a compartment based search
                checkResourceType(pathTokens[2]);
                result = new FHIRRestInteractionSearch(entryIndex, requestDescription, requestURL, initialTime, pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri, null, true);
            }
        } else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
            // This is a 'vread' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestInteractionVRead(entryIndex, requestDescription, requestURL, initialTime, pathTokens[0], pathTokens[1], pathTokens[3], null);
        } else {
            String msg = "Unrecognized path in request URL: " + requestURL.getPath();
            throw buildRestException(msg, IssueType.NOT_FOUND);
        }

        return result;
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
     * @param requestURL
     *            the request URL
     * @param absoluteUri
     *            the absolute URI
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @param transaction
     *            a flag indicating whether or not this is a transaction bundle type
     * @return a FHIRRestInteraction representing the intended interaction
     * @throws Exception
     */
    private FHIRRestInteraction processEntryForPost(Entry requestEntry, Entry validationResponseEntry,
            Integer entryIndex, FHIRUrlParser requestURL, String absoluteUri, String requestDescription, long initialTime, boolean transaction)
            throws Exception {

        final FHIRRestInteraction result;
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
                operationContext = FHIROperationContext.createSystemOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, POST, null, null, null, resource, queryParams);
                break;
            case 2:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createResourceTypeOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, POST, pathTokens[0], null, null, resource, queryParams);
                break;
            case 3:
                checkResourceType(pathTokens[0]);
                operationContext = FHIROperationContext.createInstanceOperationContext(operationName);
                result = new FHIRRestInteractionInvoke(entryIndex, validationResponseEntry, requestDescription, requestURL, initialTime, operationContext, POST, pathTokens[0], pathTokens[1], null, resource, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }
        } else if (pathTokens.length == 2 && "_search".equals(pathTokens[1])) {
            // This is a 'search' request.
            checkResourceType(pathTokens[0]);
            result = new FHIRRestInteractionSearch(entryIndex, requestDescription, requestURL, initialTime, pathTokens[0], null, null, queryParams, absoluteUri, null, true);
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

            // Since 1869 we no longer use pre-generated identifiers. Identifiers are always
            // assigned during the meta-processing loop.
            // String resourceId = retrieveGeneratedIdentifier(localRefMap, localIdentifier);

            // Build the CREATE interaction
            Entry.Request request = requestEntry.getRequest();
            String ifNoneExist = request.getIfNoneExist() != null
                    && request.getIfNoneExist().getValue() != null
                    && !request.getIfNoneExist().getValue().isEmpty() ? request.getIfNoneExist().getValue() : null;


            if (log.isLoggable(Level.FINE)) {
                log.fine("Creating CREATE interaction for bundle entry[" + entryIndex + "]: " + requestDescription + "; validationResponseEntry: "
                    + validationResponseEntry);
            }

            // Create the event
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(resource, helpers.buildPersistenceEventProperties(resource.getClass().getSimpleName(), null, null, null));

            result = new FHIRRestInteractionCreate(entryIndex, event, validationResponseEntry, requestDescription, requestURL, initialTime, pathTokens[0], resource, ifNoneExist, localIdentifier);
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
     * @param transaction
     *            a flag indicating whether or not this is a transaction bundle type
     * @return a FHIRRestInteraction representing the intended interaction
     * @throws Exception
     */
    private FHIRRestInteraction processEntryForPut(Entry requestEntry, Entry validationResponseEntry,
            Integer entryIndex, FHIRUrlParser requestURL, String absoluteUri, String requestDescription,
            long initialTime, boolean skippableUpdate, boolean transaction) throws Exception {

        final FHIRRestInteraction result;
        String[] pathTokens = requestURL.getPathTokens();
        final String type;
        final String id;

        // Process a PUT (update).
        if (pathTokens.length == 1) {
            // A single-part url would be a conditional update: <type>?<query>
            type = pathTokens[0];
            id = null;
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

        // Build the 'update' interaction.
        String ifMatchBundleValue = null;
        if (requestEntry.getRequest().getIfMatch() != null) {
            ifMatchBundleValue = requestEntry.getRequest().getIfMatch().getValue();
        }

        // Conditional create-on-update
        Integer ifNoneMatch = null;
        if (requestEntry.getRequest().getIfNoneMatch() != null) {
            String ifNoneMatchValue = requestEntry.getRequest().getIfNoneMatch().getValue();
            if ("*".equals(ifNoneMatchValue)) {
                ifNoneMatch = Integer.valueOf(0);
            }
        }

        // Extract the local identifier which may be used by other resources in the bundle to reference this resource
        String localIdentifier = retrieveLocalIdentifier(requestEntry);

        // Build the UPDATE interaction command
        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating UPDATE interaction for bundle entry[" + entryIndex + "]: " + requestDescription + "; validationResponseEntry: "
                + validationResponseEntry);
        }

        // Create the event we'll use for this resource interaction
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(resource, helpers.buildPersistenceEventProperties(type, id, null, null));
        result = new FHIRRestInteractionUpdate(entryIndex, event, validationResponseEntry, requestDescription, requestURL, initialTime,
            type, id, resource, ifMatchBundleValue, requestURL.getQuery(), skippableUpdate, localIdentifier, ifNoneMatch);

        return result;
    }

    /**
     * Processes a request entry with a request method of DELETE.
     * @param entryIndex
     *            the index of the entry in the request bundle
     * @param requestURL
     *            the request URL
     * @param requestDescription
     *            a description of the request
     * @param initialTime
     *            the time the bundle entry processing started
     * @return a FHIRRestInteraction representing the intended interaction
     * @throws Exception
     */
    private FHIRRestInteraction processEntryForDelete(int entryIndex, FHIRUrlParser requestURL, String requestDescription, long initialTime) throws Exception {

        final FHIRRestInteraction result;
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
        if (log.isLoggable(Level.FINE)) {
            log.fine("Creating DELETE interaction for bundle entry[" + entryIndex + "]: " + requestDescription);
        }
        result = new FHIRRestInteractionDelete(entryIndex, requestDescription, requestURL, initialTime, type, id, requestURL.getQuery());
        return result;
    }

    /**
     * Check that the resource type is a valid type
     * @param type the resource type name
     * @throws FHIROperationException
     */
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
}