/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.util;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.ModelSupport.getResourceType;
import static javax.servlet.http.HttpServletResponse.SC_ACCEPTED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_GONE;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.net.URI;
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
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.exception.FHIRValidationException;

/**
 * Helper methods for performing the "heavy lifting" with respect to implementing
 * FHIR interactions.
 */
public class FHIRRestHelper implements FHIRResourceHelpers {
    private static final Logger log =
            java.util.logging.Logger.getLogger(FHIRRestHelper.class.getName());

    public static final String EXTENSION_URL = "http://ibm.com/fhir/extension";
    private static final String LOCAL_REF_PREFIX = "urn:";
    private static final com.ibm.fhir.model.type.String SC_BAD_REQUEST_STRING = string(Integer.toString(SC_BAD_REQUEST));
    private static final com.ibm.fhir.model.type.String SC_GONE_STRING = string(Integer.toString(SC_GONE));
    private static final com.ibm.fhir.model.type.String SC_NOT_FOUND_STRING = string(Integer.toString(SC_NOT_FOUND));
    private static final com.ibm.fhir.model.type.String SC_ACCEPTED_STRING = string(Integer.toString(SC_ACCEPTED));
    private static final com.ibm.fhir.model.type.String SC_OK_STRING = string(Integer.toString(SC_OK));
    private static final String TOO_MANY_INCLUDE_RESOURCES = "Number of returned 'include' resources exceeds allowable limit of " + SearchConstants.MAX_PAGE_SIZE;
    private static final ZoneId UTC = ZoneId.of("UTC");

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

    private FHIRPersistence persistence = null;

    // Used for correlating requests within a bundle.
    private String bundleRequestCorrelationId = null;

    public FHIRRestHelper(FHIRPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public FHIRRestOperationResponse doCreate(String type, Resource resource, String ifNoneExist,
            boolean doValidation) throws Exception {
        log.entering(this.getClass().getName(), "doCreate");

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.CREATE.value(), type);

        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Get the transaction started before there's any chance of a rollback
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        txn.begin();

        try {

            // Make sure the expected type (specified in the URL string) is congruent with the actual type
            // of the resource.
            String resourceType = ModelSupport.getTypeName(resource.getClass());
            if (!resourceType.equals(type)) {
                String msg = "Resource type '" + resourceType
                        + "' does not match type specified in request URI: " + type;
                throw buildRestException(msg, IssueType.INVALID);
            }

            // Check to see if we're supposed to perform a conditional 'create'.
            if (ifNoneExist != null && !ifNoneExist.isEmpty()) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing conditional create with search criteria: " + ifNoneExist);
                }
                Bundle responseBundle = null;

                // Perform the search using the "If-None-Exist" header value.
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(ifNoneExist);
                    responseBundle = doSearch(type, null, null, searchParameters, null, resource, false);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg =
                            "An error occurred while performing the search for a conditional create operation.";
                    log.log(Level.WARNING, msg, t);
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the create operation.
                int resultCount = responseBundle.getEntry().size();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Conditional create search yielded " + resultCount + " results.");
                }

                if (resultCount == 0) {
                    // Do nothing and fall through to process the 'create' request.
                } else if (resultCount == 1) {
                    // If we found a single match, bypass the 'create' request and return information
                    // for the matched resource.
                    Resource matchedResource = responseBundle.getEntry().get(0).getResource();
                    ior.setLocationURI(FHIRUtil.buildLocationURI(type, matchedResource));
                    ior.setStatus(Response.Status.OK);
                    ior.setResource(matchedResource);
                    ior.setOperationOutcome(FHIRUtil.buildOperationOutcome("Found a single match; check the Location header",
                            IssueType.INFORMATIONAL, IssueSeverity.INFORMATION));
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Returning location URI of matched resource: " + ior.getLocationURI());
                    }
                    return ior;
                } else {
                    String msg =
                            "The search criteria specified for a conditional create operation returned multiple matches.";
                    throw buildRestException(msg, IssueType.MULTIPLE_MATCHES);
                }
            }

            // Validate the input and, if valid, start collecting supplemental warnings
            List<Issue> warnings = doValidation ? new ArrayList<>(validateInput(resource)) : new ArrayList<>();

            // For R4, resources may contain an id. For create, this should be ignored and
            // we no longer reject the request.
            if (resource.getId() != null) {
                String msg = "The create request resource included id: '" + resource.getId() + "'; this id has been replaced";
                warnings.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.INFORMATION, IssueType.INFORMATIONAL, msg));
                if (log.isLoggable(Level.FINE)) {
                    log.fine(msg);
                }
            }

            // If there were no validation errors, then create the resource and return the location header.

            // First, invoke the 'beforeCreate' interceptor methods.
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(resource, buildPersistenceEventProperties(type, null, null, null));
            getInterceptorMgr().fireBeforeCreateEvent(event);

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event);

            // R4: remember model objects are immutable, so we get back a new resource with the id/meta stuff
            SingleResourceResult<Resource> result = persistence.create(persistenceContext, resource);
            resource = result.getResource();
            if (result.isSuccess() && result.getOutcome() != null) {
                warnings.addAll(result.getOutcome().getIssue());
            }
            event.setFhirResource(resource); // update event with latest
            ior.setStatus(Response.Status.CREATED);
            ior.setResource(resource);
            ior.setOperationOutcome(FHIRUtil.buildOperationOutcome(warnings));

            // Build our location URI and add it to the interceptor event structure since it is now known.
            ior.setLocationURI(FHIRUtil.buildLocationURI(ModelSupport.getTypeName(resource.getClass()), resource));
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, ior.getLocationURI().toString());

            // Invoke the 'afterCreate' interceptor methods.
            getInterceptorMgr().fireAfterCreateEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            return ior;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doCreate");
        }
    }

    @Override
    public FHIRRestOperationResponse doPatch(String type, String id, FHIRPatch patch, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate) throws Exception {

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.PATCH.value(), type);

        return doPatchOrUpdate(type, id, patch, null, ifMatchValue, searchQueryString, skippableUpdate, DO_VALIDATION);
    }

    @Override
    public FHIRRestOperationResponse doUpdate(String type, String id, Resource newResource, String ifMatchValue,
            String searchQueryString, boolean skippableUpdate, boolean doValidation) throws Exception {

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.UPDATE.value(), type);

        return doPatchOrUpdate(type, id, null, newResource, ifMatchValue, searchQueryString, skippableUpdate, doValidation);
    }

    private FHIRRestOperationResponse doPatchOrUpdate(String type, String id, FHIRPatch patch,
            Resource newResource, String ifMatchValue, String searchQueryString,
            boolean skippableUpdate, boolean doValidation) throws Exception {
        log.entering(this.getClass().getName(), "doPatchOrUpdate");

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        txn.begin();

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        try {
            // Make sure the type specified in the URL string matches the resource type obtained from the new resource.
            if (patch == null) {
                String resourceType =  ModelSupport.getTypeName(newResource.getClass());
                if (!resourceType.equals(type)) {
                    String msg = "Resource type '" + resourceType
                            + "' does not match type specified in request URI: " + type;
                    throw buildRestException(msg, IssueType.INVALID);
                }
            }

            // Next, if a conditional update was invoked then use the search criteria to find the
            // resource to be updated. Otherwise, we'll use the id value to retrieve the current
            // version of the resource.
            if (searchQueryString != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing conditional update/patch with search criteria: "
                            + Encode.forHtml(searchQueryString));
                }
                Bundle responseBundle = null;
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    responseBundle = doSearch(type, null, null, searchParameters, null, newResource, false);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg =
                            "An error occurred while performing the search for a conditional update/patch operation.";
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the update operation.
                int resultCount = responseBundle.getEntry().size();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Conditional update/patch search yielded " + resultCount + " results.");
                }

                if (resultCount == 0) {
                    if (patch != null) {
                        String msg =
                                "The search criteria specified for a conditional patch operation did not return any results.";
                        throw buildRestException(msg, IssueType.NOT_FOUND);
                    }
                    // Search yielded no matches, so we'll do an update/create operation below.
                    ior.setPrevResource(null);

                    // if no id provided, then generate an id for the input resource
                    if (newResource.getId() == null) {
                        id = persistence.generateResourceId();
                        newResource = newResource.toBuilder().id(id).build();
                    } else {
                        id = newResource.getId();
                    }
                } else if (resultCount == 1) {
                    // If we found a single match, then we'll perform a normal update on the matched resource.
                    ior.setPrevResource(responseBundle.getEntry().get(0).getResource());
                    id = ior.getPrevResource().getId();

                    // If the id of the input resource is different from the id of the search result,
                    // then throw exception.
                    if (newResource.getId() != null && id != null && !newResource.getId().equals(id)) {
                        String msg = "Input resource 'id' attribute must match the id of the search result resource.";
                        throw buildRestException(msg, IssueType.VALUE);
                    }

                    // Make sure the id of the newResource is not null and is the same as the id of the found resource.
                    newResource = newResource.toBuilder().id(id).build();
                } else {
                    String msg =
                            "The search criteria specified for a conditional update/patch operation returned multiple matches.";
                    throw buildRestException(msg, IssueType.MULTIPLE_MATCHES);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    String msg = "The 'id' parameter is required for an update/pach operation.";
                    throw buildRestException(msg, IssueType.REQUIRED);
                }

                // If an id value was passed in (i.e. the id specified in the REST API URL string),
                // then make sure it's the same as the value in the resource.
                if (patch == null) {
                    // Make sure the resource has an 'id' attribute.
                    if (newResource.getId() == null) {
                        String msg = "Input resource must contain an 'id' attribute.";
                        throw buildRestException(msg, IssueType.INVALID);
                    }

                    if (!newResource.getId().equals(id)) {
                        String msg = "Input resource 'id' attribute must match 'id' parameter.";
                        throw buildRestException(msg, IssueType.VALUE);
                    }
                }

                // Retrieve the resource to be updated using the type and id values.
                ior.setPrevResource(doRead(type, id, (patch != null), true, newResource, null, false));
            }

            if (patch != null) {
                try {
                    newResource = patch.apply(ior.getPrevResource());
                } catch (FHIRPatchException e) {
                    String msg = "Invalid patch: " + e.getMessage();
                    throw new FHIROperationException(msg, e).withIssue(Issue.builder()
                            .severity(IssueSeverity.ERROR)
                            .code(IssueType.INVALID)
                            .details(CodeableConcept.builder()
                                    .text(string(msg))
                                    .build())
                            .expression(string(e.getPath()))
                            .build());
                }
            }

            // Validate the input and, if valid, start collecting supplemental warnings
            List<Issue> warnings = doValidation ? new ArrayList<>(validateInput(newResource)) : new ArrayList<>() ;

            // Perform the "version-aware" update check, and also find out if the resource was deleted.
            boolean isDeleted = false;
            if (ior.getPrevResource() != null) {
                performVersionAwareUpdateCheck(ior.getPrevResource(), ifMatchValue);

                try {
                    doRead(type, id, (patch != null), false, newResource, null, false);
                } catch (FHIRPersistenceResourceDeletedException e) {
                    isDeleted = true;
                }

                if (skippableUpdate && !isDeleted) {
                    ResourceFingerprintVisitor fingerprinter = new ResourceFingerprintVisitor();
                    ior.getPrevResource().accept(fingerprinter);
                    SaltHash baseline = fingerprinter.getSaltAndHash();

                    fingerprinter = new ResourceFingerprintVisitor(baseline);
                    newResource.accept(fingerprinter);
                    if (fingerprinter.getSaltAndHash().equals(baseline)) {
                        txn.commit();
                        txn = null;

                        ior.setResource(ior.getPrevResource());
                        ior.setStatus(Status.OK);
                        ior.setLocationURI(FHIRUtil.buildLocationURI(type, ior.getPrevResource()));
                        ior.setOperationOutcome(OperationOutcome.builder()
                                .issue(Issue.builder()
                                    .severity(IssueSeverity.INFORMATION)
                                    .code(IssueType.INFORMATIONAL)
                                    .details(CodeableConcept.builder()
                                        .text(string("Update resource matches the existing resource; skipping the update"))
                                        .build())
                                    .build())
                                .build());

                        return ior; // early exit
                    }
                }
            }

            // First, create the persistence event.
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(newResource,
                    buildPersistenceEventProperties(type, newResource.getId(), null, null));

            // Next, set the "previous resource" in the persistence event.
            event.setPrevFhirResource(ior.getPrevResource());

            // Next, invoke the 'beforeUpdate' or 'beforeCreate' interceptor methods as appropriate.
            boolean updateCreate = (ior.getPrevResource() == null);
            if (updateCreate) {
                getInterceptorMgr().fireBeforeCreateEvent(event);
            } else {
                if (patch != null) {
                    event.getProperties().put(FHIRPersistenceEvent.PROPNAME_PATCH, patch);
                    getInterceptorMgr().fireBeforePatchEvent(event);
                } else {
                    getInterceptorMgr().fireBeforeUpdateEvent(event);
                }
            }

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event);
            SingleResourceResult<Resource> result = persistence.update(persistenceContext, id, newResource);
            if (result.isSuccess() && result.getOutcome() != null) {
                warnings.addAll(result.getOutcome().getIssue());
            }
            newResource = result.getResource();
            event.setFhirResource(newResource); // update event with latest
            ior.setResource(newResource);
            ior.setOperationOutcome(FHIRUtil.buildOperationOutcome(warnings));

            // Build our location URI and add it to the interceptor event structure since it is now known.
            ior.setLocationURI(FHIRUtil.buildLocationURI(type, newResource));
            event.getProperties().put(FHIRPersistenceEvent.PROPNAME_RESOURCE_LOCATION_URI, ior.getLocationURI().toString());

            // Invoke the 'afterUpdate' interceptor methods.
            if (updateCreate) {
                ior.setStatus(Response.Status.CREATED);
                getInterceptorMgr().fireAfterCreateEvent(event);
            } else {
                ior.setStatus(Response.Status.OK);
                if (patch != null) {
                    getInterceptorMgr().fireAfterPatchEvent(event);
                } else {
                    getInterceptorMgr().fireAfterUpdateEvent(event);
                }
            }

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            // If the deleted resource is updated, then simply return 201 instead of 200 to pass Touchstone test.
            // We don't set the previous resource to null in above codes if the resource was deleted, otherwise
            // it will break the code logic of the resource versioning.
            if (isDeleted && ior.getStatus() == Response.Status.OK) {
                ior.setStatus(Response.Status.CREATED);
            }

            return ior;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we still have a transaction at this point, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doPatchOrUpdate");
        }
    }

    /**
     * Performs a 'delete' operation on the specified resource.
     *
     * @param type
     *            the resource type associated with the Resource to be deleted
     * @param id
     *            the id of the Resource to be deleted
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a FHIRRestOperationResponse that contains the results of the operation
     * @throws Exception
     */
    @Override
    public FHIRRestOperationResponse doDelete(String type, String id, String searchQueryString) throws Exception {
        log.entering(this.getClass().getName(), "doDelete");

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.DELETE.value(), type);

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        FHIRRestOperationResponse ior = new FHIRRestOperationResponse();

        // Make sure we get a transaction started before there's any chance
        // it could be marked for rollback
        txn.begin();

        // A list of supplemental warnings to include in the response
        List<Issue> warnings = new ArrayList<>();

        try {
            String resourceTypeName = type;
            if (!ModelSupport.isResourceType(type)) {
                throw buildUnsupportedResourceTypeException(type);
            }

            Class<? extends Resource> resourceType =
                    getResourceType(resourceTypeName);

            // Next, if a conditional delete was invoked then use the search criteria to find the
            // resource to be deleted. Otherwise, we'll use the id value to identify the resource
            // to be deleted.
            Resource resourceToDelete = null;
            Bundle responseBundle = null;

            if (searchQueryString != null) {
                int searchPageSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_CONDITIONAL_DELETE_MAX_NUMBER,
                        FHIRConstants.FHIR_CONDITIONAL_DELETE_MAX_NUMBER_DEFAULT);

                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing conditional delete with search criteria: "
                            + Encode.forHtml(searchQueryString));
                }
                try {
                    MultivaluedMap<String, String> searchParameters = getQueryParameterMap(searchQueryString);
                    searchParameters.putSingle(SearchConstants.COUNT, Integer.toString(searchPageSize));
                    // TODO add support for collecting the warnings from the search
                    responseBundle = doSearch(type, null, null, searchParameters, null, null, false);
                } catch (FHIROperationException e) {
                    throw e;
                } catch (Throwable t) {
                    String msg = "An error occurred while performing the search for a conditional delete operation.";
                    throw new FHIROperationException(msg, t);
                }

                // Check the search results to determine whether or not to perform the update operation.

                int resultCount = responseBundle.getEntry().size();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Conditional delete search yielded " + resultCount + " results.");
                }

                if (resultCount == 0) {
                    // Search yielded no matches
                    String msg = "Search criteria for a conditional delete operation yielded no matches.";
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(msg);
                    }
                    ior.setOperationOutcome(FHIRUtil.buildOperationOutcome(msg, IssueType.NOT_FOUND, IssueSeverity.WARNING));
                    ior.setStatus(Status.OK);
                    return ior;
                } else if (responseBundle.getTotal().getValue() > searchPageSize) {
                    String msg = "The search criteria specified for a conditional delete operation returned too many matches ( > " + searchPageSize + " ).";
                    throw buildRestException(msg, IssueType.MULTIPLE_MATCHES);
                }
            } else {
                // Make sure an id value was passed in.
                if (id == null) {
                    String msg = "The 'id' parameter is required for a delete operation.";
                    throw buildRestException(msg, IssueType.REQUIRED);
                }

                // Read the resource so it will be available to the beforeDelete interceptor methods.
                try {
                    resourceToDelete = doRead(type, id, false, false, null, null, false);
                    if (resourceToDelete != null) {
                        responseBundle = Bundle.builder().type(BundleType.SEARCHSET)
                                .id(UUID.randomUUID().toString())
                                .entry(Entry.builder().id(id).resource(resourceToDelete).build())
                                .total(UnsignedInt.of(1))
                                .build();
                    } else {
                        warnings.add(buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.NOT_FOUND, "Cannot find "
                                + type + " with id '" + id + "'."));
                    }
                } catch (FHIRPersistenceResourceDeletedException e) {
                    // Absorb this exception.
                    ior.setResource(doRead(type, id, false, true, null, null, false));
                    warnings.add(buildOperationOutcomeIssue(IssueSeverity.WARNING, IssueType.DELETED, "Resource of type '"
                        + type + "' with id '" + id + "' is already deleted."));
                }
            }

            if (responseBundle != null) {

                for (Entry entry: responseBundle.getEntry()) {
                    id = entry.getResource().getId();
                    resourceToDelete = entry.getResource();
                    // First, invoke the 'beforeDelete' interceptor methods.
                    FHIRPersistenceEvent event =
                            new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null, null));
                    event.setFhirResource(resourceToDelete);
                    getInterceptorMgr().fireBeforeDeleteEvent(event);

                    FHIRPersistenceContext persistenceContext =
                            FHIRPersistenceContextFactory.createPersistenceContext(event);

                    SingleResourceResult<? extends Resource> result = persistence.delete(persistenceContext, resourceType, id);
                    if (result.getOutcome() != null) {
                        warnings.addAll(result.getOutcome().getIssue());
                    }
                    Resource resource = result.getResource();
                    event.setFhirResource(resource);

                    if (responseBundle.getEntry().size() == 1) {
                        ior.setResource(resource);
                    }

                    // Invoke the 'afterDelete' interceptor methods.
                    getInterceptorMgr().fireAfterDeleteEvent(event);
                }

                warnings.add(Issue.builder()
                        .severity(IssueSeverity.INFORMATION)
                        .code(IssueType.INFORMATIONAL)
                        .details(CodeableConcept.builder()
                            .text(string("Deleted " + responseBundle.getEntry().size() + " " + type + " resource(s) " +
                                "with the following id(s): " +
                                responseBundle.getEntry().stream().map(Bundle.Entry::getId).collect(Collectors.joining(","))))
                            .build())
                        .build());

                // Commit our transaction if we started one before.
                txn.commit();
                txn = null;
            }

            // The server should return either a 200 OK if the response contains a payload, or a 204 No Content with no response payload
            if (!warnings.isEmpty()) {
                ior.setOperationOutcome(FHIRUtil.buildOperationOutcome(warnings));
                ior.setStatus(Status.OK);
            } else {
                ior.setStatus(Status.NO_CONTENT);
            }

            return ior;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doDelete");
        }
    }

    @Override
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource) throws Exception {
        return doRead(type, id, throwExcOnNull, includeDeleted, contextResource, null);
    }

    @Override
    public Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource, MultivaluedMap<String, String> queryParameters) throws Exception {
        return doRead(type, id, throwExcOnNull, includeDeleted, contextResource, queryParameters, true);
    }

    /**
     * Performs a 'read' operation to retrieve a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param throwExcOnNull
     *            if true, throw an exception if returned resource is null
     * @param includeDeleted
     *            if true, return resource even if deleted
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @param contextResource
     *            a FHIR resource associated with this request
     * @param queryParameters
     *            for supporting _elements and _summary for resource read
     * @param checkInteractionAllowed
     *            if true, check if this interaction is allowed per the tenant configuration; if false, assume interaction is allowed
     * @return the Resource
     * @throws Exception
     */
    private Resource doRead(String type, String id, boolean throwExcOnNull, boolean includeDeleted,
            Resource contextResource, MultivaluedMap<String, String> queryParameters, boolean checkInteractionAllowed)
            throws Exception {
        log.entering(this.getClass().getName(), "doRead");

        // Validate that interaction is allowed for given resource type
        if (checkInteractionAllowed) {
            validateInteraction(Interaction.READ.value(), type);
        }

        // Start a new txn in the persistence layer if one is not already active.
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        txn.begin();

        Resource resource = null;

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!ModelSupport.isResourceType(type)) {
                throw buildUnsupportedResourceTypeException(type);
            }

            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);

            FHIRSearchContext searchContext = null;
            if (queryParameters != null) {
                searchContext = SearchUtil.parseReadQueryParameters(resourceType, queryParameters, Interaction.READ.value(),
                    HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference()));
            }

            // First, invoke the 'beforeRead' interceptor methods.
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(contextResource, buildPersistenceEventProperties(type, id, null, searchContext));
            getInterceptorMgr().fireBeforeReadEvent(event);

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event, includeDeleted, searchContext);
            resource = persistence.read(persistenceContext, resourceType, id).getResource();
            if (resource == null && throwExcOnNull) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '" + type + "/" + id + "' not found.");
            }

            event.setFhirResource(resource);

            // Invoke the 'afterRead' interceptor methods.
            getInterceptorMgr().fireAfterReadEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            return resource;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doRead");
        }
    }

    @Override
    public Resource doVRead(String type, String id, String versionId, MultivaluedMap<String, String> queryParameters)
            throws Exception {
        log.entering(this.getClass().getName(), "doVRead");

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.VREAD.value(), type);

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        // Start a new txn in the persistence layer if one is not already active.
        txn.begin();

        Resource resource = null;

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!ModelSupport.isResourceType(type)) {
                throw buildUnsupportedResourceTypeException(type);
            }

            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);

            FHIRSearchContext searchContext = null;
            if (queryParameters != null) {
                searchContext = SearchUtil.parseReadQueryParameters(resourceType, queryParameters, Interaction.VREAD.value(),
                    HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference()));
            }

            // First, invoke the 'beforeVread' interceptor methods.
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, versionId, searchContext));
            getInterceptorMgr().fireBeforeVreadEvent(event);

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event, searchContext);
            resource = persistence.vread(persistenceContext, resourceType, id, versionId).getResource();
            if (resource == null) {
                throw new FHIRPersistenceResourceNotFoundException("Resource '"
                        + resourceType.getSimpleName() + "/" + id + "' version " + versionId + " not found.");
            }

            event.setFhirResource(resource);

            // Invoke the 'afterVread' interceptor methods.
            getInterceptorMgr().fireAfterVreadEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            return resource;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doVRead");
        }
    }

    /**
     * Performs the work of retrieving versions of a Resource.
     *
     * @param type
     *            the resource type associated with the Resource to be retrieved
     * @param id
     *            the id of the Resource to be retrieved
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri the URI from the request
     * @param requestProperties
     *            additional request properties which supplement the HTTP headers associated with this request
     * @return a Bundle containing the history of the specified Resource
     * @throws Exception
     */
    @Override
    public Bundle doHistory(String type, String id, MultivaluedMap<String, String> queryParameters, String requestUri)
            throws Exception {
        log.entering(this.getClass().getName(), "doHistory");

        // Validate that interaction is allowed for given resource type
        validateInteraction(Interaction.HISTORY.value(), type);

        // Start a new txn in the persistence layer if one is not already active.
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        txn.begin();

        Bundle bundle = null;

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;
            if (!ModelSupport.isResourceType(type)) {
                throw buildUnsupportedResourceTypeException(type);
            }

            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);
            FHIRHistoryContext historyContext = FHIRPersistenceUtil.parseHistoryParameters(queryParameters,
                    HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference()));

            // First, invoke the 'beforeHistory' interceptor methods.
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(null, buildPersistenceEventProperties(type, id, null, null));
            getInterceptorMgr().fireBeforeHistoryEvent(event);

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event, historyContext);
            List<? extends Resource> resources =
                    persistence.history(persistenceContext, resourceType, id).getResource();
            bundle = createHistoryBundle(resources, historyContext, type);
            bundle = addLinks(historyContext, bundle, requestUri);

            event.setFhirResource(bundle);

            // Invoke the 'afterHistory' interceptor methods.
            getInterceptorMgr().fireAfterHistoryEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            return bundle;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doHistory");
        }
    }

    @Override
    public Bundle doSearch(String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri, Resource contextResource) throws Exception {
        return doSearch(type, compartment, compartmentId, queryParameters, requestUri, contextResource, true);
    }

    /**
     * Performs heavy lifting associated with a 'search' operation.
     *
     * @param type
     *            the resource type associated with the search
     * @param compartment
     *            the compartment associated with the search
     * @param compartmentId
     *            the ID of the compartment associated with the search
     * @param queryParameters
     *            a Map containing the query parameters from the request URL
     * @param requestUri
     *            the request URI
     * @param contextResource
     *            a FHIR resource associated with this request
     * @param checkInteractionAllowed
     *            if true, check if this interaction is allowed per the tenant configuration; if false, assume interaction is allowed
     * @return a Bundle containing the search result set
     * @throws Exception
     */
    private Bundle doSearch(String type, String compartment, String compartmentId,
            MultivaluedMap<String, String> queryParameters, String requestUri,
            Resource contextResource, boolean checkInteractionAllowed) throws Exception {
        log.entering(this.getClass().getName(), "doSearch");

        // Validate that interaction is allowed for given resource type
        if (checkInteractionAllowed) {
            validateInteraction(Interaction.SEARCH.value(), type);
        }

        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        // Start a new txn in the persistence layer if one is not already active.
        txn.begin();

        Bundle bundle = null;

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            String resourceTypeName = type;

            // Check to see if it's supported, else, throw a bad request.
            // If this is removed, it'll result in nullpointer when processing the request
            if (!ModelSupport.isResourceType(type)) {
                throw buildUnsupportedResourceTypeException(type);
            }

            Class<? extends Resource> resourceType = getResourceType(resourceTypeName);

            FHIRSearchContext searchContext = SearchUtil.parseCompartmentQueryParameters(compartment, compartmentId, resourceType, queryParameters,
                HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference()));

            // First, invoke the 'beforeSearch' interceptor methods.
            FHIRPersistenceEvent event =
                    new FHIRPersistenceEvent(contextResource, buildPersistenceEventProperties(type, null, null, searchContext));
            getInterceptorMgr().fireBeforeSearchEvent(event);

            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(event, searchContext);
            List<Resource> resources =
                    persistence.search(persistenceContext, resourceType).getResource();

            bundle = createSearchBundle(resources, searchContext, type);
            if (requestUri != null) {
                bundle = addLinks(searchContext, bundle, requestUri);
            }
            event.setFhirResource(bundle);

            // Invoke the 'afterSearch' interceptor methods.
            getInterceptorMgr().fireAfterSearchEvent(event);

            // Commit our transaction if we started one before.
            txn.commit();
            txn = null;

            return bundle;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            // If we previously started a transaction and it's still active, we need to rollback due to an error.
            if (txn != null) {
                txn.rollback();
            }

            log.exiting(this.getClass().getName(), "doSearch");
        }
    }

    /**
     * Helper method which invokes a custom operation.
     *
     * @param operationContext
     *            the FHIROperationContext associated with the request
     * @param resourceTypeName
     *            the resource type associated with the request
     * @param logicalId
     *            the resource logical id associated with the request
     * @param versionId
     *            the resource version id associated with the request
     * @param operationName
     *            the name of the custom operation to be invoked
     * @param resource
     *            the input resource associated with the custom operation to be invoked
     * @param queryParameters
     *            query parameters may be passed instead of a Parameters resource for certain custom operations invoked
     *            via GET
     * @return a Resource that represents the response to the custom operation
     * @throws Exception
     */
    @Override
    public Resource doInvoke(FHIROperationContext operationContext, String resourceTypeName,
            String logicalId, String versionId, String operationName,
            Resource resource, MultivaluedMap<String, String> queryParameters) throws Exception {
        log.entering(this.getClass().getName(), "doInvoke");

        // Save the current request context.
        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            Class<? extends Resource> resourceType = null;
            if (resourceTypeName != null) {
                resourceType = getResourceType(resourceTypeName);
            }
            String operationKey = (resourceTypeName == null ? operationName : operationName + ":" + resourceTypeName);

            FHIROperation operation = FHIROperationRegistry.getInstance().getOperation(operationKey);
            Parameters parameters = null;
            if (resource instanceof Parameters) {
                parameters = (Parameters) resource;
            } else {
                if (resource == null) {
                    // build parameters object from query parameters
                    parameters =
                            FHIROperationUtil.getInputParameters(operation.getDefinition(), queryParameters);
                } else {
                    // wrap resource in a parameters object
                    parameters =
                            FHIROperationUtil.getInputParameters(operation.getDefinition(), resource);
                }
            }

            // Add properties to the FHIR operation context
            setOperationContextProperties(operationContext, resourceTypeName);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Invoking operation '" + operationName + "', context=\n"
                        + operationContext.toString());
            }
            Parameters result =
                    operation.invoke(operationContext, resourceType, logicalId, versionId, parameters, this);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Returned from invocation of operation '" + operationName + "'...");
            }

            // if single resource output parameter, return the resource
            if (FHIROperationUtil.hasSingleResourceOutputParameter(result)) {
                return FHIROperationUtil.getSingleResourceOutputParameter(result);
            }

            return result;
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            log.exiting(this.getClass().getName(), "doInvoke");
        }
    }

    @Override
    public Bundle doBundle(Bundle inputBundle, boolean skippableUpdates) throws Exception {
        log.entering(this.getClass().getName(), "doBundle");

        FHIRRequestContext requestContext = FHIRRequestContext.get();

        try {
            // First, validate the bundle and save the error / warning responses by index entry.
            Map<Integer, Entry> validationResponseEntries = validateBundle(inputBundle);

            // Next, process each of the entries in the bundle.
            return processBundleEntries(inputBundle, validationResponseEntries, skippableUpdates);
        } finally {
            // Restore the original request context.
            FHIRRequestContext.set(requestContext);

            log.exiting(this.getClass().getName(), "doBundle");
        }
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() throws Exception {
        return persistence.getTransaction();
    }

    /**
     * Validate the input resource and throw if there are validation errors
     *
     * @param resource
     * @throws FHIRValidationException if an error occurs during validation
     * @throws FHIROperationException if there are validation errors
     * @return A list of validation warnings
     */
    private List<OperationOutcome.Issue> validateInput(Resource resource)
            throws FHIRValidationException, FHIROperationException {
        List<OperationOutcome.Issue> issues = validateResource(resource);
        if (!issues.isEmpty()) {
            for (OperationOutcome.Issue issue : issues) {
                if (FHIRUtil.isFailure(issue.getSeverity())) {
                    throw new FHIROperationException("Input resource failed validation.").withIssue(issues);
                }
            }

            if (log.isLoggable(Level.FINE)) {
                String info = issues.stream()
                        .flatMap(issue -> Stream.of(issue.getDetails()))
                        .flatMap(details -> Stream.of(details.getText()))
                        .flatMap(text -> Stream.of(text.getValue()))
                        .collect(Collectors.joining(", "));
                log.fine("Validation warnings for input resource: " + info);
            }
        }
        return issues;
    }

    /**
     * @param issues
     * @return
     */
    private boolean anyFailureInIssues(List<OperationOutcome.Issue> issues) {
        boolean hasFailure = false;
        for (OperationOutcome.Issue issue : issues) {
            if (FHIRUtil.isFailure(issue.getSeverity())) {
                hasFailure = true;
            }
        }
        return hasFailure;
    }

    /**
     * Performs validation of a request Bundle and returns a Map of entry indices to error / warning
     * response entries that correspond to the entries in the request Bundle.
     *
     * @param bundle
     *            the bundle to be validated
     * @return a map of entry indices to error responses / warnings; empty if there are no validation warnings or errors
     * @throws Exception
     */
    private Map<Integer, Entry> validateBundle(Bundle bundle) throws Exception {
        log.entering(this.getClass().getName(), "validateBundle");
        Map<Integer, Entry> validationResponseEntries = new HashMap<>();

        try {
            // Make sure the bundle isn't empty
            if (bundle == null) {
                String msg = "Bundle parameter is missing or empty.";
                throw buildRestException(msg, IssueType.REQUIRED);
            }

            BundleType.Value requestType = bundle.getType().getValueAsEnum();
            if (requestType != BundleType.Value.BATCH && requestType != BundleType.Value.TRANSACTION) {
                // TODO add support for posting history bundles
                String msg = "Bundle.type must be either 'batch' or 'transaction'.";
                throw buildRestException(msg, IssueType.VALUE);
            }
            if (requestType == BundleType.Value.TRANSACTION && !persistence.isTransactional()) {
                // For a 'transaction' interaction, if the underlying persistence layer doesn't support
                // transactions, then throw an error.
                String msg = "Bundled 'transaction' request cannot be processed because "
                        + "the configured persistence layer does not support transactions.";
                IssueType extendedIssueType = IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(EXTENSION_URL +  "/not-supported-detail")
                            .value(Code.of("interaction"))
                            .build())
                        .build();
                throw buildRestException(msg, extendedIssueType);
            }

            // For 'transaction' bundle requests, keep a list of issues in case of failure
            List<OperationOutcome.Issue> issueList = new ArrayList<OperationOutcome.Issue>();

            Set<String> localIdentifiers = new HashSet<>();

            for (int i = 0; i < bundle.getEntry().size(); i++) {
                // Create a corresponding response entry and add it to the response bundle.
                Bundle.Entry requestEntry = bundle.getEntry().get(i);
                Bundle.Entry responseEntry = null;

                // Validate 'requestEntry' and update 'responseEntry' with any errors.
                try {
                    Bundle.Entry.Request request = requestEntry.getRequest();

                    // Verify that the request field is present.
                    if (request == null) {
                        String msg = "Bundle.Entry is missing the 'request' field.";
                        throw buildRestException(msg, IssueType.REQUIRED);
                    }

                    // Verify that a method was specified.
                    if (request.getMethod() == null || request.getMethod().getValue() == null) {
                        String msg = "Bundle.Entry.request is missing the 'method' field";
                        throw buildRestException(msg, IssueType.REQUIRED);
                    }

                    // Verify that a URL was specified.
                    if (request.getUrl() == null || request.getUrl().getValue() == null) {
                        String msg = "Bundle.Entry.request is missing the 'url' field";
                        throw buildRestException(msg, IssueType.REQUIRED);
                    }

                    // Verify that the fullUrl field is not a duplicate if it specifies a local reference
                    // and if the request method is POST or PUT.
                    if (request.getMethod().equals(HTTPVerb.POST) || request.getMethod().equals(HTTPVerb.PUT)) {
                        String localIdentifier = retrieveLocalIdentifier(requestEntry);
                        if (localIdentifier != null) {
                            if (localIdentifiers.contains(localIdentifier)) {
                                String msg = "Duplicate local identifier encountered in bundled request entry: " + localIdentifier;
                                throw buildRestException(msg, IssueType.DUPLICATE);
                            }
                            localIdentifiers.add(localIdentifier);
                        }
                    }

                    // Retrieve the resource from the request entry to prepare for some validations below.
                    Resource resource = requestEntry.getResource();

                    // Validate the resource for the requested HTTP method.
                    methodValidation(request.getMethod(), resource);

                    // If the request entry contains a resource, then validate it now.
                    if (resource != null) {
                        List<Issue> issues = validateResource(resource);
                        if (!issues.isEmpty()) {
                            OperationOutcome oo = FHIRUtil.buildOperationOutcome(issues);
                            if (anyFailureInIssues(issues)) {
                                if (requestType == BundleType.Value.TRANSACTION) {
                                    issueList.addAll(issues);
                                } else {
                                    responseEntry = Entry.builder()
                                                .response(Entry.Response.builder()
                                                    .status(SC_BAD_REQUEST_STRING)
                                                    .build())
                                                .resource(oo)
                                                .build();
                                }
                            } else {
                                responseEntry = Entry.builder()
                                        .response(Entry.Response.builder()
                                            .status(SC_ACCEPTED_STRING)
                                            .outcome(oo)
                                            .build())
                                        .build();
                            }
                        }
                    }
                } catch (FHIROperationException e) {
                    if (log.isLoggable(Level.FINE)) {
                        log.log(Level.FINE, "Failed to process BundleEntry ["
                                + bundle.getEntry().indexOf(requestEntry) + "]", e);
                    }
                    if (requestType == BundleType.Value.TRANSACTION) {
                        issueList.addAll(e.getIssues());
                    } else {
                        Entry.Response response = Entry.Response.builder()
                                .status(SC_BAD_REQUEST_STRING)
                                .build();
                        responseEntry = Entry.builder()
                                .response(response)
                                .resource(FHIRUtil.buildOperationOutcome(e, false))
                                .build();
                    }
                } finally {
                    if (responseEntry != null) {
                        validationResponseEntries.put(i, responseEntry);
                    }
                }
            } // End foreach requestEntry

            // If this is a "transaction" interaction and we encountered any errors, then we'll
            // abort processing this request right now since a transaction interaction is supposed to be
            // all or nothing.
            if (requestType == BundleType.Value.TRANSACTION && issueList.size() > 0) {
                String msg =
                        "One or more errors were encountered while validating a 'transaction' request bundle.";
                throw buildRestException(msg, IssueType.INVALID).withIssue(issueList);
            }

            return validationResponseEntries;
        } finally {
            log.exiting(this.getClass().getName(), "validateBundle");
        }
    }

    /**
     * Perform method-specific validation of the resource
     */
    private void methodValidation(HTTPVerb method, Resource resource) throws FHIRPersistenceException, FHIROperationException {
        switch(method.getValueAsEnum()) {
        case PATCH:
        case POST:
            break;
        case DELETE:
            // If the "delete" operation isn't supported by the configured persistence layer,
            // then we need to fail validation of this bundle entry.
            if (!persistence.isDeleteSupported()) {
                String msg = "Bundle.Entry.request contains unsupported HTTP method: "
                        + method.getValue();
                IssueType extendedIssueType = IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(EXTENSION_URL +  "/not-supported-detail")
                            .value(Code.of("interaction"))
                            .build())
                        .build();
                throw buildRestException(msg, extendedIssueType);
            }
            // Purposefully fall through to next clause
        case HEAD:
        case GET:
            if (resource != null) {
                String msg =
                        "Bundle.Entry.resource not allowed for BundleEntry with " + method.getValue() + " method.";
                throw buildRestException(msg, IssueType.INVALID);
            }
            break;
        case PUT:
            if (resource == null) {
                String msg =
                        "Bundle.Entry.resource is required for BundleEntry with PUT method.";
                throw buildRestException(msg, IssueType.INVALID);
            }
            break;
        default:
            String msg = "Bundle.Entry.request contains unsupported HTTP method: " + method.getValue();
            throw buildRestException(msg, IssueType.INVALID);
        }
    }

    /**
     * This function will perform the version-aware update check by making sure that the If-Match request header value
     * (if present) specifies a version # equal to the current latest version of the resource. If the check fails, then
     * a FHIRRestException will be thrown. If the check succeeds then nothing occurs and processing continues.
     *
     * @param currentResource
     *            the current latest version of the resource
     */
    private void performVersionAwareUpdateCheck(Resource currentResource, String ifMatchValue) throws FHIROperationException {
        if (ifMatchValue != null) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Performing a version aware update. ETag value =  " + ifMatchValue);
            }

            String ifMatchVersion = getVersionIdFromETagValue(ifMatchValue);

            // Make sure that we got a version # from the request header.
            // If not, then return a 400 Bad Request status code.
            if (ifMatchVersion == null || ifMatchVersion.isEmpty()) {
                throw buildRestException("Invalid ETag value specified in request: "
                        + ifMatchValue, IssueType.PROCESSING);
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Version id from ETag value specified in request: " + ifMatchVersion);
            }

            // Retrieve the version #'s from the current and updated resources.
            String currentVersion = null;
            if (currentResource.getMeta() != null
                    && currentResource.getMeta().getVersionId() != null) {
                currentVersion = currentResource.getMeta().getVersionId().getValue();
            }

            // Next, make sure that the If-Match version matches the version # found
            // in the current latest version of the resource.
            // If they don't match we'll return an HTTP 412 (Precondition Failed) status code.
            if (!ifMatchVersion.equals(currentVersion)) {
                String msg = "If-Match version '" + ifMatchVersion
                        + "' does not match current latest version of resource: " + currentVersion;
                IssueType extendedIssueType = IssueType.CONFLICT.toBuilder()
                        .extension(Extension.builder()
                            .url(EXTENSION_URL + "/http-failed-precondition")
                            .value(string("If-Match"))
                            .build())
                        .build();
                throw buildRestException(msg, extendedIssueType);
            }
        }
    }

    private FHIROperationException buildUnsupportedResourceTypeException(String resourceTypeName)
            throws FHIROperationException {
        String msg = "'" + resourceTypeName + "' is not a valid resource type.";
        Issue issue = OperationOutcome.Issue.builder()
                .severity(IssueSeverity.FATAL)
                .code(IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(EXTENSION_URL +  "/not-supported-detail")
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
     * Retrieves the version id value from an ETag header value. The ETag header value will be of the form:
     * W/"<version-id>".
     *
     * @param ifMatchValue
     *            the value of the If-Match request header.
     */
    private String getVersionIdFromETagValue(String ifMatchValue) {
        String result = null;
        if (ifMatchValue != null) {
            if (ifMatchValue.startsWith("W/")) {
                String s = ifMatchValue.substring(2);
                // If the part after "W/" starts and ends with a ",
                // then extract the part between the " characters and we're done.
                if (s.charAt(0) == '\"' && s.charAt(s.length() - 1) == '\"') {
                    result = s.substring(1, s.length() - 1);
                }
            }
        }
        return result;
    }

    /**
     * This function will process each request contained in the specified request bundle, and update the response bundle
     * with the appropriate response information.
     *
     * @param requestBundle
     *            the bundle containing the requests
     * @param validationResponseEntries
     *            a map from entry indices to the corresponding response entries created during validation
     * @param skippableUpdates
     *            if true, and the bundle contains an update for which the resource content in the update matches the existing
     *            resource on the server, then skip the update; if false, then always attempt the updates specified in the bundle
     * @return a response bundle
     */
    private Bundle processBundleEntries(Bundle requestBundle, Map<Integer, Entry> validationResponseEntries, boolean skippableUpdates) throws Exception {
        log.entering(this.getClass().getName(), "processBundleEntries");

        // Generate a request correlation id for this request bundle.
        bundleRequestCorrelationId = UUID.randomUUID().toString();
        if (log.isLoggable(Level.FINE)) {
            log.fine("Processing request bundle, request-correlation-id=" + bundleRequestCorrelationId);
        }

        try {
            // Build a mapping of local identifiers to external identifiers for local reference resolution.
            Map<String, String> localRefMap = buildLocalRefMap(requestBundle, validationResponseEntries);

            // Process entries.
            BundleType.Value bundleType = requestBundle.getType().getValueAsEnum();
            List<Entry> responseEntries = processEntriesByMethod(requestBundle, validationResponseEntries,
                    bundleType == BundleType.Value.TRANSACTION, localRefMap, bundleRequestCorrelationId, skippableUpdates);

            // Build the response bundle.
            // TODO add support for posting history bundles
            Bundle.Builder bundleResponseBuilder = Bundle.builder().entry(responseEntries);
            if (bundleType == BundleType.Value.BATCH) {
                bundleResponseBuilder.type(BundleType.BATCH_RESPONSE);
            } else if (bundleType == BundleType.Value.TRANSACTION) {
                bundleResponseBuilder.type(BundleType.TRANSACTION_RESPONSE);
            }

            return bundleResponseBuilder.build();

        } finally {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Finished processing request bundle, request-correlation-id="
                    + bundleRequestCorrelationId);
            }

            // Clear the request correlation id field since we're done processing the bundle.
            bundleRequestCorrelationId = null;

            log.exiting(this.getClass().getName(), "processBundleEntries");
        }
    }

    /**
     * Processes request entries in the specified request bundle whose method matches 'httpMethod'.
     *
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
    private List<Entry> processEntriesByMethod(Bundle requestBundle, Map<Integer, Entry> validationResponseEntries,
            boolean failFast, Map<String, String> localRefMap, String bundleRequestCorrelationId, boolean skippableUpdates) throws Exception {
        log.entering(this.getClass().getName(), "processEntriesByMethod");

        FHIRTransactionHelper txn = null;

        try {
            // Placeholder for response entries
            Entry[] responseEntries = new Entry[requestBundle.getEntry().size()];

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
                    // validation marked this entry as invalid, so write the validation response entry and skip it
                    responseEntries[i] = validationResponseEntries.get(i);
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

            // If we're working on a 'transaction' type interaction, or an interaction
            // where we're processing only GET or HEAD requests, then start a new transaction now.
            BundleType.Value bundleType = requestBundle.getType().getValueAsEnum();
            if (bundleType == BundleType.Value.TRANSACTION ||
                    ((!requestEntriesByMethod.get(HTTPVerb.Value.GET).isEmpty() ||
                            !requestEntriesByMethod.get(HTTPVerb.Value.HEAD).isEmpty()) &&
                            requestEntriesByMethod.get(HTTPVerb.Value.DELETE).isEmpty() &&
                            requestEntriesByMethod.get(HTTPVerb.Value.POST).isEmpty() &&
                            requestEntriesByMethod.get(HTTPVerb.Value.PUT).isEmpty() &&
                            requestEntriesByMethod.get(HTTPVerb.Value.PATCH).isEmpty())) {
                txn = new FHIRTransactionHelper(getTransaction());
                txn.begin();
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Started new transaction for '" + bundleType.toString() +
                            "' bundle, request-correlation-id=" + bundleRequestCorrelationId);
                }
            }

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
                // Use hashmap to store both the index and the accordingly updated response bundle entry.
                Map<Integer, Entry> responseIndexAndEntries = new HashMap<Integer, Entry>();
                for (Integer entryIndex : entryIndices) {
                    Entry requestEntry = requestBundle.getEntry().get(entryIndex);
                    Entry.Request request = requestEntry.getRequest();

                    StringBuilder requestDescription = new StringBuilder();
                    long initialTime = System.currentTimeMillis();

                    try {
                        FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());

                        // Log our initial info message for this request.
                        requestDescription.append("entryIndex:[");
                        requestDescription.append(entryIndex);
                        requestDescription.append("] correlationId:[");
                        requestDescription.append(bundleRequestCorrelationId);
                        requestDescription.append("] method:[");
                        requestDescription.append(request.getMethod().getValue());
                        requestDescription.append("] uri:[");
                        requestDescription.append(request.getUrl().getValue());
                        requestDescription.append("]");
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Processing bundled request: " + requestDescription.toString());
                            if (log.isLoggable(Level.FINER)) {
                                log.finer("--> path: '" + requestURL.getPath() + "'");
                                log.finer("--> query: '" + requestURL.getQuery() + "'");
                            }
                        }

                        // Construct the absolute requestUri to be used for any response bundles associated
                        // with history and search requests.
                        String absoluteUri = getAbsoluteUri(getRequestUri(), request.getUrl().getValue());

                        if (request.getMethod().equals(HTTPVerb.GET)) {
                            responseEntries[entryIndex] = processEntryForGet(request, requestURL, absoluteUri,
                                    requestDescription.toString(), initialTime);
                        } else if (request.getMethod().equals(HTTPVerb.POST)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            responseEntries[entryIndex] = processEntryForPost(requestEntry, validationResponseEntry, responseIndexAndEntries,
                                    entryIndex, localRefMap, requestURL, absoluteUri, requestDescription.toString(), initialTime);
                        } else if (request.getMethod().equals(HTTPVerb.PUT)) {
                            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);
                            responseEntries[entryIndex] = processEntryForPut(requestEntry, validationResponseEntry, responseIndexAndEntries,
                                    entryIndex, localRefMap, requestURL, absoluteUri, requestDescription.toString(), initialTime, skippableUpdates);
                        } else if (request.getMethod().equals(HTTPVerb.PATCH)) {
                            responseEntries[entryIndex] = processEntryforPatch(requestEntry, requestURL,entryIndex,
                                    requestDescription.toString(), initialTime, skippableUpdates);
                        } else if (request.getMethod().equals(HTTPVerb.DELETE)) {
                            responseEntries[entryIndex] = processEntryForDelete(requestURL, requestDescription.toString(), initialTime);
                        } else {
                            // Internal error, should not get here!
                            throw new IllegalStateException("Internal Server Error: reached an unexpected code location.");
                        }
                    } catch (FHIRPersistenceResourceNotFoundException e) {
                        if (failFast) {
                            String msg = "Error while processing request bundle.";
                            throw new FHIRRestBundledRequestException(msg).withIssue(e.getIssues());
                        }

                        responseEntries[entryIndex] = Entry.builder()
                                .resource(FHIRUtil.buildOperationOutcome(e, false))
                                .response(Entry.Response.builder()
                                    .status(SC_NOT_FOUND_STRING)
                                    .build())
                                .build();
                        logBundleRequestCompletedMsg(requestDescription.toString(), initialTime, SC_NOT_FOUND);
                    } catch (FHIRPersistenceResourceDeletedException e) {
                        if (failFast) {
                            String msg = "Error while processing request bundle.";
                            throw new FHIRRestBundledRequestException(msg).withIssue(e.getIssues());
                        }

                        responseEntries[entryIndex] = Entry.builder()
                                .resource(FHIRUtil.buildOperationOutcome(e, false))
                                .response(Entry.Response.builder()
                                    .status(SC_GONE_STRING)
                                    .build())
                                .build();
                        logBundleRequestCompletedMsg(requestDescription.toString(), initialTime, SC_GONE);
                    } catch (FHIROperationException e) {
                        if (failFast) {
                            String msg = "Error while processing request bundle.";
                            throw new FHIRRestBundledRequestException(msg).withIssue(e.getIssues());
                        }

                        Status status;
                        if (e instanceof FHIRSearchException) {
                            status = Status.BAD_REQUEST;
                        } else {
                            status = IssueTypeToHttpStatusMapper.issueListToStatus(e.getIssues());
                        }

                        responseEntries[entryIndex] = Entry.builder()
                                .resource(FHIRUtil.buildOperationOutcome(e, false))
                                .response(Entry.Response.builder()
                                    .status(string(Integer.toString(status.getStatusCode())))
                                    .build())
                                .build();
                        logBundleRequestCompletedMsg(requestDescription.toString(), initialTime, status.getStatusCode());
                    }
                } // end foreach method entry
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Finished processing for method: " + httpMethod);
                }
            } // end foreach method

            // Commit transaction if started
            if (txn != null) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Committing transaction for '" + bundleType.toString() +
                            "' bundle, request-correlation-id=" + bundleRequestCorrelationId);
                }
                txn.commit();
                txn = null;
            }

            return Arrays.asList(responseEntries);

        } finally {
            if (txn != null) {
                txn.rollback();
                txn = null;
            }
            log.exiting(this.getClass().getName(), "processEntriesForMethod");
        }
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
    private Entry processEntryforPatch(Entry requestEntry, FHIRUrlParser requestURL, Integer entryIndex, String requestDescription,
            long initialTime, boolean skippableUpdate) throws Exception {
        FHIRRestOperationResponse ior = null;
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

        if (!requestEntry.getResource().is(Parameters.class)) {
            String msg="Request resource type for PATCH request must be type 'Parameters'";
            throw buildRestException(msg, IssueType.INVALID);
        }

        Parameters parameters = requestEntry.getResource().as(Parameters.class);
        FHIRPatch patch = FHIRPathPatch.from(parameters);
        ior = doPatch(resourceType, resourceId, patch, null, null, skippableUpdate);

        return buildResponseBundleEntry(ior, null, requestDescription, initialTime);
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
    private Entry processEntryForGet(Entry.Request entryRequest, FHIRUrlParser requestURL, String absoluteUri,
            String requestDescription, long initialTime) throws Exception {

        String[] pathTokens = requestURL.getPathTokens();
        MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();
        Resource resource = null;

        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Process a GET (read, vread, history, search, etc.).
        // Determine the type of request from the path tokens.
        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
            // This is a custom operation request.

            // Chop off the '$' and save the name
            String operationName = pathTokens[pathTokens.length - 1].substring(1);

            FHIROperationContext operationContext;
            switch (pathTokens.length) {
            case 1:
                operationContext = FHIROperationContext.createSystemOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "GET");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                resource = doInvoke(operationContext, null, null, null, operationName, null, queryParams);
                break;
            case 2:
                operationContext = FHIROperationContext.createResourceTypeOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "GET");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                resource = doInvoke(operationContext, pathTokens[0], null, null, operationName, null, queryParams);
                break;
            case 3:
                operationContext = FHIROperationContext.createInstanceOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "GET");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                resource = doInvoke(operationContext, pathTokens[0], pathTokens[1], null, operationName, null, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }
        } else if (pathTokens.length == 1) {
            // This is a 'search' request.
            if ("_search".equals(pathTokens[0])) {
                resource = doSearch("Resource", null, null, queryParams, absoluteUri, null);
            } else {
                resource = doSearch(pathTokens[0], null, null, queryParams, absoluteUri, null);
            }
        } else if (pathTokens.length == 2) {
            // This is a 'read' request.
            resource = doRead(pathTokens[0], pathTokens[1], true, false, null);
        } else if (pathTokens.length == 3) {
            if ("_history".equals(pathTokens[2])) {
                // This is a 'history' request.
                resource = doHistory(pathTokens[0], pathTokens[1], queryParams, absoluteUri);
            } else {
                // This is a compartment based search
                resource = doSearch(pathTokens[2], pathTokens[0], pathTokens[1], queryParams, absoluteUri, null);
            }
        } else if (pathTokens.length == 4 && pathTokens[2].equals("_history")) {
            // This is a 'vread' request.
            resource = doVRead(pathTokens[0], pathTokens[1], pathTokens[3], null);
        } else {
            String msg = "Unrecognized path in request URL: " + requestURL.getPath();
            throw buildRestException(msg, IssueType.NOT_FOUND);
        }

        // Save the results of the operation in the bundle response field.
        logBundleRequestCompletedMsg(requestDescription, initialTime, SC_OK);

        return Entry.builder()
                .response(Entry.Response.builder()
                    .status(SC_OK_STRING)
                    .build())
                .resource(resource)
                .build();
    }

    /**
     * Processes a request entry with a request method of POST.
     *
     * @param requestEntry
     *            the request bundle entry
     * @param validationResponseEntry
     *            the response bundle entry created during validation, possibly null
     * @param responseIndexAndEntries
     *            the hashmap containing bundle entry indexes and their associated response entries
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
    private Entry processEntryForPost(Entry requestEntry, Entry validationResponseEntry, Map<Integer, Entry> responseIndexAndEntries,
            Integer entryIndex, Map<String, String> localRefMap, FHIRUrlParser requestURL, String absoluteUri, String requestDescription, long initialTime)
            throws Exception {

        String[] pathTokens = requestURL.getPathTokens();
        MultivaluedMap<String, String> queryParams = requestURL.getQueryParameters();
        Resource resource = null;

        FHIRRequestContext requestContext = FHIRRequestContext.get();

        // Process a POST (create or search, or custom operation).
        if (pathTokens.length > 0 && pathTokens[pathTokens.length - 1].startsWith("$")) {
            // This is a custom operation request.

            // Chop off the '$' and save the name.
            String operationName = pathTokens[pathTokens.length - 1].substring(1);

            // Retrieve the resource from the request entry.
            resource = requestEntry.getResource();

            FHIROperationContext operationContext;
            Resource result;
            switch (pathTokens.length) {
            case 1:
                operationContext = FHIROperationContext.createSystemOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                result = doInvoke(operationContext, null, null, null, operationName, resource, queryParams);
                break;
            case 2:
                operationContext = FHIROperationContext.createResourceTypeOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                result = doInvoke(operationContext, pathTokens[0], null, null, operationName, resource, queryParams);
                break;
            case 3:
                operationContext = FHIROperationContext.createInstanceOperationContext();
                operationContext.setProperty(FHIROperationContext.PROPNAME_URI_INFO, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_URI_INFO));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_HEADERS, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_HEADERS));
                operationContext.setProperty(FHIROperationContext.PROPNAME_METHOD_TYPE, "POST");
                operationContext.setProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_SECURITY_CONTEXT));
                operationContext.setProperty(FHIROperationContext.PROPNAME_HTTP_REQUEST, requestContext.getExtendedOperationProperties(FHIROperationContext.PROPNAME_HTTP_REQUEST));
                result = doInvoke(operationContext, pathTokens[0], pathTokens[1], null, operationName, resource, queryParams);
                break;
            default:
                String msg = "Invalid URL for custom operation '" + pathTokens[pathTokens.length - 1] + "'";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }

            logBundleRequestCompletedMsg(requestDescription, initialTime, SC_OK);
            return Bundle.Entry.builder()
                    .resource(result)
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .build();

        } else if (pathTokens.length == 2 && "_search".equals(pathTokens[1])) {
            // This is a 'search' request.
            Bundle searchResults = doSearch(pathTokens[0], null, null, queryParams, absoluteUri, null);

            logBundleRequestCompletedMsg(requestDescription, initialTime, SC_OK);
            return Bundle.Entry.builder()
                    .resource(searchResults)
                    .response(Entry.Response.builder()
                        .status(SC_OK_STRING)
                        .build())
                    .build();

        } else if (pathTokens.length == 1) {
            // This is a 'create' request.

            // Retrieve the local identifier from the request entry (if present).
            String localIdentifier = retrieveLocalIdentifier(requestEntry);

            // Retrieve the resource from the request entry.
            resource = requestEntry.getResource();
            if (resource == null) {
                String msg = "BundleEntry.resource is required for bundled create requests.";
                throw buildRestException(msg, IssueType.NOT_FOUND);
            }

            // Convert any local references found within the resource to their corresponding external reference.
            ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
            resource.accept(visitor);
            resource = visitor.getResult();

            // Determine if we have a pre-generated resource ID
            String resourceId = retrieveGeneratedIdentifier(localRefMap, localIdentifier);

            // Perform the 'create' or 'update' operation.
            FHIRRestOperationResponse ior;
            Entry.Request request = requestEntry.getRequest();
            String ifNoneExist = request.getIfNoneExist() != null
                    && request.getIfNoneExist().getValue() != null
                    && !request.getIfNoneExist().getValue().isEmpty() ? request.getIfNoneExist().getValue() : null;
            if (ifNoneExist != null || resourceId == null) {
                ior = doCreate(pathTokens[0], resource, ifNoneExist, !DO_VALIDATION);
            } else {
                resource = resource.toBuilder().id(resourceId).build();
                // Skip validation because its already been performed.
                ior = doUpdate(pathTokens[0], resourceId, resource, null, null, !SKIPPABLE_UPDATE, !DO_VALIDATION);
            }

            // If a local identifier was present and not already mapped to its external identifier, add mapping.
            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                addLocalRefMapping(localRefMap, localIdentifier, null, ior.getResource());
            }

            // Use the validationOutcome and not the FHIRRestOperationResponse outcome.
            OperationOutcome validationOutcome = null;
            if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
                validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
            }

            return buildResponseBundleEntry(ior, validationOutcome, requestDescription, initialTime);
        } else {
            String msg = "Request URL for bundled create requests should have a path with exactly one token (<resourceType>).";
            throw buildRestException(msg, IssueType.NOT_FOUND);
        }
    }

    /**
     * Processes a request entry with a request method of PUT.
     *
     * @param requestEntry
     *            the request bundle entry
     * @param validationResponseEntry
     *            the response bundle entry created during validation, possibly null
     * @param responseIndexAndEntries
     *            the hashmap containing bundle entry indexes and their associated response entries
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
    private Entry processEntryForPut(Entry requestEntry, Entry validationResponseEntry, Map<Integer, Entry> responseIndexAndEntries,
            Integer entryIndex, Map<String, String> localRefMap, FHIRUrlParser requestURL, String absoluteUri, String requestDescription,
            long initialTime, boolean skippableUpdate) throws Exception {

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

        // Retrieve the resource from the request entry.
        Resource resource = requestEntry.getResource();

        // Convert any local references found within the resource to their corresponding external reference.
        ReferenceMappingVisitor<Resource> visitor = new ReferenceMappingVisitor<Resource>(localRefMap);
        resource.accept(visitor);
        resource = visitor.getResult();

        // Perform the 'update' operation.
        String ifMatchBundleValue = null;
        if (requestEntry.getRequest().getIfMatch() != null) {
            ifMatchBundleValue = requestEntry.getRequest().getIfMatch().getValue();
        }
        FHIRRestOperationResponse ior = doUpdate(type, id, resource, ifMatchBundleValue, requestURL.getQuery(), skippableUpdate, !DO_VALIDATION);

        // If this was a conditional update, and if a local identifier was present and not already mapped to its external identifier, add mapping.
        if (pathTokens.length == 1) {
            String localIdentifier = retrieveLocalIdentifier(requestEntry);
            if (localIdentifier != null && localRefMap.get(localIdentifier) == null) {
                addLocalRefMapping(localRefMap, localIdentifier, null, ior.getResource());
            }
        }

        // Use the validationOutcome and not the FHIRRestOperationResponse outcome.
        OperationOutcome validationOutcome = null;
        if (validationResponseEntry != null && validationResponseEntry.getResponse() != null) {
            validationOutcome = validationResponseEntry.getResponse().getOutcome().as(OperationOutcome.class);
        }

        return buildResponseBundleEntry(ior, validationOutcome, requestDescription, initialTime);
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
    private Entry processEntryForDelete(FHIRUrlParser requestURL, String requestDescription, long initialTime) throws Exception {

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

        // Perform the 'delete' operation.
        FHIRRestOperationResponse ior = doDelete(type, id, requestURL.getQuery());

        int httpStatus = ior.getStatus().getStatusCode();
        OperationOutcome oo = ior.getOperationOutcome();

        logBundleRequestCompletedMsg(requestDescription, initialTime, httpStatus);
        return Entry.builder()
                .response(Entry.Response.builder()
                    .status(string(Integer.toString(httpStatus)))
                    .outcome(oo)
                    .build())
                .build();
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
     * This function converts the specified query string (a String) into an equivalent MultivaluedMap<String,String>
     * containing the query parameters defined in the query string.
     *
     * @param queryString
     *            the query string to be processed
     * @return
     */
    private MultivaluedMap<String, String> getQueryParameterMap(String queryString) {
        MultivaluedMap<String, String> result = null;
        FHIRUrlParser parser = new FHIRUrlParser("foo?" + queryString);
        result = parser.getQueryParameters();
        return result;
    }

    /**
     * This method will build a mapping of local identifiers to external identifiers for bundle entries
     * which specify local identifiers and which have a request method of POST or PUT.
     *
     * @param requestBundle
     *            the bundle containing the requests
     *
     * @return local reference map
     */
    private Map<String, String> buildLocalRefMap(Bundle requestBundle, Map<Integer, Entry> validationResponseEntries) throws Exception {
            Map<String, String> localRefMap = new HashMap<>();

        for (int entryIndex=0; entryIndex<requestBundle.getEntry().size(); ++entryIndex) {
            Entry requestEntry = requestBundle.getEntry().get(entryIndex);
            Entry.Request request = requestEntry.getRequest();
            Entry validationResponseEntry = validationResponseEntries.get(entryIndex);

            // Only add mappings for POST and PUT requests where response is ACCEPTED.
            if (validationResponseEntry != null && !validationResponseEntry.getResponse().getStatus().equals(SC_ACCEPTED_STRING)) {
                continue;
            }

            HTTPVerb.Value method = request.getMethod().getValueAsEnum();
            if (method == HTTPVerb.Value.POST || method == HTTPVerb.Value.PUT) {

                // Retrieve the local identifier from the request entry (if present).
                String localIdentifier = retrieveLocalIdentifier(requestEntry);
                if (localIdentifier != null) {

                    // Retrieve the resource from the request entry (if present).
                    Resource resource = requestEntry.getResource();
                    if (resource != null) {

                        // Get and parse the request URL.
                        FHIRUrlParser requestURL = new FHIRUrlParser(request.getUrl().getValue());
                        String[] pathTokens = requestURL.getPathTokens();

                        // Only add mapping for POST request if it's a non-conditional create.
                        // Only add mapping for PUT request if a resource ID is specified.
                        if (method == HTTPVerb.Value.POST && pathTokens.length == 1 && !pathTokens[0].startsWith("$") &&
                                (request.getIfNoneExist() == null || request.getIfNoneExist().getValue() == null || request.getIfNoneExist().getValue().isEmpty())) {
                            // Generate external identifier and add mapping.
                            String externalIdentifier = ModelSupport.getTypeName(resource.getClass()) + "/" + persistence.generateResourceId();
                            addLocalRefMapping(localRefMap, localIdentifier, externalIdentifier, null);
                        } else if (request.getMethod().equals(HTTPVerb.PUT) && resource.getId() != null) {
                            // Add mapping.
                            addLocalRefMapping(localRefMap, localIdentifier, null, resource);
                        }
                    }
                }
            }
        }

        return localRefMap;
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

    private Entry buildResponseBundleEntry(FHIRRestOperationResponse operationResponse, OperationOutcome validationOutcome,
            String requestDescription, long initialTime) throws FHIROperationException {

        Resource resource = operationResponse.getResource();
        URI locationURI = operationResponse.getLocationURI();
        int httpStatus = operationResponse.getStatus().getStatusCode();

        Entry.Response.Builder entryResponseBuilder = Entry.Response.builder()
                .status(string(Integer.toString(httpStatus)))
                .outcome(validationOutcome);
        if (resource != null) {
            entryResponseBuilder = entryResponseBuilder
                    .id(resource.getId())
                    .lastModified(resource.getMeta().getLastUpdated())
                    .etag(string(getEtagValue(resource)));
        }
        if (locationURI != null) {
            entryResponseBuilder = entryResponseBuilder.location(Uri.of(locationURI.toString()));
        }

        Entry.Builder bundleEntryBuilder = Entry.builder();
        if (HTTPReturnPreference.REPRESENTATION.equals(FHIRRequestContext.get().getReturnPreference())) {
            bundleEntryBuilder.resource(resource);
        } else if (HTTPReturnPreference.OPERATION_OUTCOME.equals(FHIRRequestContext.get().getReturnPreference())) {
            // Given that we execute the operation with validation turned off, the operationResponse outcome is unlikely
            // to contain useful information, but the validationOutcome already exists under the Entry.response
            bundleEntryBuilder.resource(operationResponse.getOperationOutcome());
        }

        logBundleRequestCompletedMsg(requestDescription, initialTime, httpStatus);
        return bundleEntryBuilder.response(entryResponseBuilder.build()).build();
    }

    private void logBundleRequestCompletedMsg(String requestDescription, long initialTime, int httpStatus) {
        StringBuffer statusMsg = new StringBuffer();
        statusMsg.append(" status:[" + httpStatus + "]");
        double elapsedSecs = (System.currentTimeMillis() - initialTime) / 1000.0;
        log.info("Completed bundle request[" + elapsedSecs + " secs]: "
                + requestDescription.toString() + statusMsg.toString());
    }

    private String getEtagValue(Resource resource) {
        return "W/\"" + resource.getMeta().getVersionId().getValue() + "\"";
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
            if (resources.size() > matchResourceCount + SearchConstants.MAX_PAGE_SIZE) {
                throw buildRestException(TOO_MANY_INCLUDE_RESOURCES, IssueType.BUSINESS_RULE, IssueSeverity.ERROR);
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
     * Creates a bundle that will hold the results of a history operation.
     *
     * @param resources
     *            the list of resources to include in the bundle
     * @param historyContext
     *            the FHIRHistoryContext associated with the history operation
     * @param type
     *            the name of the resource type on which the history operation was requested
     * @return the bundle
     * @throws Exception
     */
    private Bundle createHistoryBundle(List<? extends Resource> resources, FHIRHistoryContext historyContext, String type)
            throws Exception {

        // throws if we have a count of more than 2,147,483,647 resources
        UnsignedInt totalCount = historyContext.getTotalCount() != null ? UnsignedInt.of(historyContext.getTotalCount()) : null;
        // generate ID for this bundle and set the "total" field for the bundle
        Bundle.Builder bundleBuilder = Bundle.builder()
                                             .type(BundleType.HISTORY)
                                             .id(UUID.randomUUID().toString())
                                             .total(totalCount);

        Map<String, List<Integer>> deletedResourcesMap = historyContext.getDeletedResources();

        for (int i = 0; i < resources.size(); i++) {
            Resource resource = resources.get(i);

            if (resource == null) {
                String msg = "A resource with no data was found.";
                log.warning(msg);
                throw new IllegalStateException(msg);
            }
            if (resource.getId() == null) {
                String msg = "A resource with no id was found.";
                log.warning(msg);
                throw new IllegalStateException(msg);
            }

            Integer versionId = Integer.valueOf(resource.getMeta().getVersionId().getValue());
            String logicalId = resource.getId();
            String resourceType = ModelSupport.getTypeName(resource.getClass());
            List<Integer> deletedVersions = deletedResourcesMap.get(logicalId);

            // Determine the correct method to include in this history entry (POST, PUT, DELETE).
            HTTPVerb method;
            if (deletedVersions != null && deletedVersions.contains(versionId)) {
                method = HTTPVerb.DELETE;
            } else if (versionId == 1) {
                method = HTTPVerb.POST;
            } else {
                method = HTTPVerb.PUT;
            }

            // Create the 'request' entry, and set the request.url field.
            // 'create' --> url = "<resourceType>"
            // 'update'/'delete' --> url = "<resourceType>/<logicalId>"
            Entry.Request request =
                    Entry.Request.builder().method(method).url(Url.of(method == HTTPVerb.POST
                            ? resourceType : resourceType + "/" + logicalId)).build();

            Entry.Response response =
                    Entry.Response.builder().status(string("200")).build();

            Entry entry =
                    Entry.builder().request(request).fullUrl(Uri.of(getRequestBaseUri(type) + "/"
                            + resource.getClass().getSimpleName() + "/"
                            + resource.getId())).response(response).resource(resource).build();

            bundleBuilder.entry(entry);
        }

        return bundleBuilder.build();
    }

    /**
     * Retrieves the shared interceptor mgr instance from the servlet context.
     */
    private FHIRPersistenceInterceptorMgr getInterceptorMgr() {
        return FHIRPersistenceInterceptorMgr.getInstance();
    }

    private Bundle addLinks(FHIRPagingContext context, Bundle responseBundle, String requestUri) throws Exception {
        String selfUri = null;
        SummaryValueSet summaryParameter = null;
        Bundle.Builder bundleBuilder = responseBundle.toBuilder();

        if (context instanceof FHIRSearchContext) {
            FHIRSearchContext searchContext = (FHIRSearchContext) context;
            summaryParameter = searchContext.getSummaryParameter();
            try {
                selfUri = SearchUtil.buildSearchSelfUri(requestUri, searchContext);
            } catch (Exception e) {
                log.log(Level.WARNING, "Unable to construct self link for search result bundle; using the request URI instead.", e);
            }
        }
        if (selfUri == null) {
            selfUri = requestUri;
        }
        // create 'self' link
        Bundle.Link selfLink =
                Bundle.Link.builder().relation(string("self")).url(Url.of(selfUri)).build();
        bundleBuilder.link(selfLink);

        // If for search with _summary=count or pageSize == 0, then don't add previous and next links.
        if (!SummaryValueSet.COUNT.equals(summaryParameter) && context.getPageSize() > 0) {
            // In case the currently requested page is < 1, ensure the next link points to page 1,
            // to avoid unnecessarily paging through additional page numbers < 1
            int nextPageNumber = Math.max(context.getPageNumber() + 1, 1);
            if (nextPageNumber <= context.getLastPageNumber()
                    && (nextPageNumber == 1 || context.getTotalCount() != null || context.getMatchCount() > 0)) {

                // starting with the self URI
                String nextLinkUrl = selfUri;

                // remove existing _page parameters from the query string
                nextLinkUrl = nextLinkUrl.replace("&_page=" + context.getPageNumber(), "").replace("_page="
                        + context.getPageNumber() + "&", "").replace("_page=" + context.getPageNumber(), "");

                if (nextLinkUrl.contains("?")) {
                    if (!nextLinkUrl.endsWith("?")) {
                        // there are other parameters in the query string
                        nextLinkUrl += "&";
                    }
                } else {
                    nextLinkUrl += "?";
                }

                // add new _page parameter to the query string
                nextLinkUrl += "_page=" + nextPageNumber;

                // create 'next' link
                Bundle.Link nextLink =
                        Bundle.Link.builder().relation(string("next")).url(Url.of(nextLinkUrl)).build();
                bundleBuilder.link(nextLink);
            }

            int prevPageNumber = Math.min(context.getPageNumber() - 1, context.getLastPageNumber());
            if (prevPageNumber > 0) {

                // starting with the original request URI
                String prevLinkUrl = requestUri;

                // remove existing _page parameters from the query string
                prevLinkUrl =
                        prevLinkUrl.replace("&_page=" + context.getPageNumber(), "").replace("_page="
                                + context.getPageNumber() + "&", "").replace("_page="
                                        + context.getPageNumber(), "");

                if (prevLinkUrl.contains("?")) {
                    if (!prevLinkUrl.endsWith("?")) {
                        // there are other parameters in the query string
                        prevLinkUrl += "&";
                    }
                } else {
                    prevLinkUrl += "?";
                }

                // add new _page parameter to the query string
                prevLinkUrl += "_page=" + prevPageNumber;

                // create 'previous' link
                Bundle.Link prevLink =
                        Bundle.Link.builder().relation(string("previous")).url(Url.of(prevLinkUrl)).build();
                bundleBuilder.link(prevLink);
            }
        }

        return bundleBuilder.build();
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

    /**
     * Builds a collection of properties that will be passed to the persistence interceptors.
     *
     * @param type
     *            the resource type
     * @param id
     *            the resource logical ID
     * @param version
     *            the resource version
     * @param searchContext
     *            the request search context
     * @return a map of persistence event properties
     * @throws FHIRPersistenceException
     */
    private Map<String, Object> buildPersistenceEventProperties(String type, String id,
            String version, FHIRSearchContext searchContext) throws FHIRPersistenceException {
        Map<String, Object> props = new HashMap<>();
        props.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, persistence);
        if (type != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, type);
        }
        if (id != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, id);
        }
        if (version != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_VERSION_ID, version);
        }
        if (searchContext != null) {
            props.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
        }
        return props;
    }

    /**
     * Sets various properties on the FHIROperationContext instance.
     *
     * @param operationContext
     *            the FHIROperationContext on which to set the properties
     * @throws Exception
     */
    private void setOperationContextProperties(FHIROperationContext operationContext, String resourceTypeName) throws Exception {
        operationContext.setProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI, getRequestBaseUri(resourceTypeName));
        operationContext.setProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL, persistence);
    }

    @Override
    public int doReindex(FHIROperationContext operationContext, OperationOutcome.Builder operationOutcomeResult, Instant tstamp, String resourceLogicalId) throws Exception {
        int result = 0;
        // handle some retries in case of deadlock exceptions
        final int TX_ATTEMPTS = 5;
        int attempt = 1;
        do {
            FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
            txn.begin();
            try {
                FHIRPersistenceContext persistenceContext = null;
                result = persistence.reindex(persistenceContext, operationOutcomeResult, tstamp, resourceLogicalId);
                attempt = TX_ATTEMPTS; // end the retry loop
            } catch (FHIRPersistenceDataAccessException x) {
                if (x.isTransactionRetryable() && attempt < TX_ATTEMPTS) {
                    log.info("attempt #" + attempt + " failed, retrying transaction");
                } else {
                    throw x;
                }
            } finally {
                txn.end();
            }
        } while (attempt++ < TX_ATTEMPTS);

        return result;
    }

    /**
     * Validate a resource. First validate profile assertions for the resource if configured to do so,
     * then validate the resource itself.
     *
     * @param resource
     *            the resource to be validated
     * @return A list of validation errors and warnings
     * @throws FHIRValidationException
     */
    private List<Issue> validateResource(Resource resource) throws FHIRValidationException {
        List<String> profiles = null;
        List<String> profilesWithoutVersion = null;

        // Retrieve the profile configuration
        try {
            StringBuilder defaultProfileConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/Resource/")
                    .append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_PROFILES).append("/")
                    .append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_PROFILES_AT_LEAST_ONE);
            StringBuilder resourceSpecificProfileConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/")
                    .append(resource.getClass().getSimpleName()).append("/").append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_PROFILES)
                    .append("/").append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_PROFILES_AT_LEAST_ONE);

            // Get the 'atLeastOne' property
            List<String> resourceSpecificProfiles = FHIRConfigHelper.getStringListProperty(resourceSpecificProfileConfigPath.toString());
            if (resourceSpecificProfiles != null) {
                profiles = resourceSpecificProfiles;
            } else {
                List<String> defaultProfiles = FHIRConfigHelper.getStringListProperty(defaultProfileConfigPath.toString());
                if (defaultProfiles != null) {
                    profiles = defaultProfiles;
                }
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Required profile list: " + profiles);
            }

            // Build the list of profiles that didn't specify a version
            if (profiles != null && !profiles.isEmpty()) {
                profilesWithoutVersion = new ArrayList<>();
                for (String profile : profiles) {
                    if (!profile.contains("|")) {
                        profilesWithoutVersion.add(profile);
                    }
                }
            }
        } catch (Exception e) {
            return Collections.singletonList(buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.UNKNOWN,
                "Error retrieving profile configuration."));
        }

        // If required profiles were configured, perform validation of asserted profiles against required profiles
        if (profiles != null && !profiles.isEmpty()) {

            // Get the profiles asserted for this resource
            List<String> resourceAssertedProfiles = ProfileSupport.getResourceAssertedProfiles(resource);
            if (log.isLoggable(Level.FINE)) {
                log.fine("Asserted profiles: " + resourceAssertedProfiles);
            }

            // Check if a profile is required but none specified
            if (resourceAssertedProfiles.isEmpty()) {
                return Collections.singletonList(buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.BUSINESS_RULE,
                    "A required profile was not specified. Resources of type '" +
                            resource.getClass().getSimpleName() +
                            "' must declare conformance to at least one of the following profiles: " +
                            profiles));
            }

            // Check if at least one asserted profile is in list of required profiles.
            // If a required profile specifies a version, an asserted profile must be an exact match.
            // If a required profile does not specify a version, any asserted profile of the same name
            // will be a match regardless if it specifies a version or not.
            boolean validProfileFound = false;
            for (String resourceAssertedProfile : resourceAssertedProfiles) {
                // Check if asserted profile contains a version
                String strippedAssertedProfile = null;
                int index = resourceAssertedProfile.indexOf("|");
                if (index != -1) {
                    strippedAssertedProfile = resourceAssertedProfile.substring(0, index);
                }

                // Look for exact match or match after stripping version from asserted profile
                if (profiles.contains(resourceAssertedProfile) || profilesWithoutVersion.contains(strippedAssertedProfile)) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Valid asserted profile found: '" + resourceAssertedProfile + "'");
                    }
                    validProfileFound = true;
                    break;
                }
            }
            if (!validProfileFound) {
                return Collections.singletonList(buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.BUSINESS_RULE,
                    "A required profile was not specified. Resources of type '" +
                            resource.getClass().getSimpleName() +
                            "' must declare conformance to at least one of the following profiles: " +
                            profiles));
            }

            // Check if asserted profiles are supported
            List<Issue> issues = new ArrayList<>();
            for (String resourceAssertedProfile : resourceAssertedProfiles) {
                StructureDefinition profile = ProfileSupport.getProfile(resourceAssertedProfile);
                if (profile == null) {
                    issues.add(buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED,
                        "Profile '" + resourceAssertedProfile + "' is not supported"));
                }
            }
            if (!issues.isEmpty()) {
                return issues;
            }
        }

        return FHIRValidator.validator().validate(resource);
    }

    /**
     * Validate an interaction for a specified resource type.
     *
     * @param interaction
     *            the interaction to be performed
     * @param resourceType
     *            the resource type against which the interaction is to be performed
     * @throws FHIROperationException
     */
    private void validateInteraction(String interaction, String resourceType) throws FHIROperationException {
        List<String> interactions = null;
        boolean resourceValid = true;

        // Retrieve the interaction configuration
        try {
            StringBuilder defaultInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/Resource/")
                    .append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
            StringBuilder resourceSpecificInteractionsConfigPath = new StringBuilder(FHIRConfiguration.PROPERTY_RESOURCES).append("/")
                    .append(resourceType).append("/").append(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);

            // Get the 'interactions' property
            List<String> resourceSpecificInteractions = FHIRConfigHelper.getStringListProperty(resourceSpecificInteractionsConfigPath.toString());
            if (resourceSpecificInteractions != null) {
                interactions = resourceSpecificInteractions;
            } else {
                // Check the 'open' property, and if that's false, check if resource was specified
                if (!FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_RESOURCES + "/" + FHIRConfiguration.PROPERTY_FIELD_RESOURCES_OPEN, true)) {
                    PropertyGroup resourceGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES + "/" + resourceType);
                    if (resourceGroup == null) {
                        resourceValid = false;
                    }
                }
                if (resourceValid) {
                    // Get the 'Resource' interaction property
                    List<String> defaultInteractions = FHIRConfigHelper.getStringListProperty(defaultInteractionsConfigPath.toString());
                    if (defaultInteractions != null) {
                        interactions = defaultInteractions;
                    }
                }
            }

            if (log.isLoggable(Level.FINE)) {
                log.fine("Allowed interactions: " + interactions);
            }
        } catch (Exception e) {
            throw buildRestException("Error retrieving interactions configuration.", IssueType.UNKNOWN, IssueSeverity.ERROR);
        }

        // Perform validation of specified interaction against specified resourceType
        if (interactions != null && !interactions.contains(interaction)) {
            throw buildRestException("The requested interaction of type '" + interaction + "' is not allowed for resource type '" + resourceType + "'",
                IssueType.BUSINESS_RULE, IssueSeverity.ERROR);
        } else if (!resourceValid) {
            throw buildRestException("The requested resource type '" + resourceType + "' is not found",
                IssueType.NOT_FOUND, IssueSeverity.ERROR);
        }
    }

    public enum Interaction {
        CREATE("create"),
        DELETE("delete"),
        HISTORY("history"),
        PATCH("patch"),
        READ("read"),
        SEARCH("search"),
        UPDATE("update"),
        VREAD("vread");

        private final String value;

        Interaction(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Interaction from(String value) {
            for (Interaction interaction : Interaction.values()) {
                if (interaction.value.equals(value)) {
                    return interaction;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }

    @Override
    public Bundle doHistory(MultivaluedMap<String, String> queryParameters, String requestUri) throws Exception {
        log.entering(this.getClass().getName(), "doHistory");

        // Validate that the interaction is allowed
        validateInteraction(Interaction.HISTORY.value(), "Resource");

        // extract the query parameters
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        FHIRSystemHistoryContext historyContext =
                FHIRPersistenceUtil.parseSystemHistoryParameters(queryParameters, HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference()));

        List<ResourceChangeLogRecord> records;

        // Start a new txn in the persistence layer if one is not already active.
        Integer count = historyContext.getCount();
        Instant since = historyContext.getSince() != null && historyContext.getSince().getValue() != null ? historyContext.getSince().getValue().toInstant() : null;
        FHIRTransactionHelper txn = new FHIRTransactionHelper(getTransaction());
        txn.begin();
        try {
            if (count == null) {
                count = DEFAULT_HISTORY_ENTRIES;
            } else if (count > MAX_HISTORY_ENTRIES) {
                count = MAX_HISTORY_ENTRIES;
            }
            records = persistence.changes(count, since, historyContext.getAfterHistoryId(), null);
        } catch (FHIRPersistenceDataAccessException x) {
            log.log(Level.SEVERE, "Error reading history; params = {" + historyContext + "}",
                x);
            throw x;
        } finally {
            txn.end();
        }


        // Create a history bundle and add an entry for each record
        Bundle.Builder bundleBuilder = Bundle.builder();

        Long lastChangeId = null;
        Instant lastChangeTime = null;

        // The FHIR spec states the result bundle entries are "sorted with oldest versions last",
        // (which has to be with respect to versions of a particular resource)
        // When using _since          - the 'records' list is sorted {change_tstamp, resourceType, resourceId}
        // When using _afterHistoryId - the 'records' list is sorted {resourceId}
        // Both sort orders guarantee that for a given resource, versions will be increasing, so to
        // meet the spec, we just process in reverse.
        for (int i=records.size()-1; i>=0; i--) {
            ResourceChangeLogRecord changeRecord = records.get(i);
            if (lastChangeId == null || changeRecord.getChangeId() > lastChangeId) {
                // Keep track of the greatest change id value
                lastChangeId = changeRecord.getChangeId();
            }

            if (lastChangeTime == null || changeRecord.getChangeTstamp().isAfter(lastChangeTime)) {
                // Keep track of the latest timestamp
                lastChangeTime = changeRecord.getChangeTstamp();
            }

            Request.Builder requestBuilder = Request.builder();
            Entry.Response.Builder responseBuilder = Entry.Response.builder();
            switch (changeRecord.getChangeType()) {
            case CREATE:
                requestBuilder.method(HTTPVerb.POST);
                requestBuilder.url(Url.of(changeRecord.getResourceTypeName()));
                responseBuilder.status(com.ibm.fhir.model.type.String.of("201 Created"));
                break;
            case UPDATE:
                requestBuilder.method(HTTPVerb.PUT);
                requestBuilder.url(Url.of(changeRecord.getResourceTypeName() + "/" + changeRecord.getLogicalId()));
                responseBuilder.status(com.ibm.fhir.model.type.String.of("200 OK"));
                break;
            case DELETE:
                requestBuilder.method(HTTPVerb.DELETE);
                requestBuilder.url(Url.of(changeRecord.getResourceTypeName() + "/" + changeRecord.getLogicalId()));
                responseBuilder.status(com.ibm.fhir.model.type.String.of("200 OK"));
                break;
            }

            responseBuilder.lastModified(com.ibm.fhir.model.type.Instant.of(changeRecord.getChangeTstamp().atZone(UTC)));

            Entry.Builder entryBuilder = Entry.builder();
            entryBuilder.fullUrl(Url.of(changeRecord.getResourceTypeName() + "/" + changeRecord.getLogicalId() + "/_history/" + changeRecord.getVersionId()));
            entryBuilder.request(requestBuilder.build());
            entryBuilder.response(responseBuilder.build());
            bundleBuilder.entry(entryBuilder.build());
        }

        // Get the service base address to use for next and self links
        String serviceBase = ReferenceUtil.getBaseUrl(null);
        if (serviceBase.endsWith("/")) {
            serviceBase = serviceBase.substring(0, serviceBase.length()-1);
        }

        if (lastChangeId != null) {
            // post the next link which a client can use to get the next set of changes.
            // If this link is not included, the client can assume we've reached the end.
            // We don't include the _since filter, because the _afterHistoryId is more
            // specific and avoids any nasty issues related to clock drift in a cluster
            // of IBM FHIR Servers.

            StringBuilder nextRequest = new StringBuilder();
            nextRequest.append(serviceBase);
            nextRequest.append("?");
            nextRequest.append("_count=").append(count);

            if (historyContext.getSince() != null && historyContext.getSince().getValue() != null) {
                // As _since was given, we need to go with time-based paging, and the client
                // will be responsible for managing duplicates.
                nextRequest.append("&_since=").append(lastChangeTime.atZone(UTC).format(DateTime.PARSER_FORMATTER));
            } else {
                // in all other cases we fetch ordered by resource_id, and so the client
                // should get the next page using the _afterHistoryId marker, allowing
                // easy paging through the stream of changes without duplicates
                nextRequest.append("&_afterHistoryId=").append(lastChangeId);
            }
            Bundle.Link.Builder linkBuilder = Bundle.Link.builder();
            linkBuilder.url(Uri.of(nextRequest.toString()));
            linkBuilder.relation(com.ibm.fhir.model.type.String.of("next"));

            bundleBuilder.link(linkBuilder.build());
        }

        // Add a self link
        StringBuilder selfRequest = new StringBuilder();
        selfRequest.append(serviceBase);
        selfRequest.append("?");
        selfRequest.append("_count=").append(count);

        // only one of afterHistoryId or since can be not null at this stage
        if (historyContext.getAfterHistoryId() != null) {
            selfRequest.append("&_afterHistoryId=").append(historyContext.getAfterHistoryId());
        }
        if (historyContext.getSince() != null && historyContext.getSince().getValue() != null) {
            selfRequest.append("&_since=").append(historyContext.getSince().getValue().format(DateTime.PARSER_FORMATTER));
        }
        Bundle.Link.Builder linkBuilder = Bundle.Link.builder();
        linkBuilder.url(Uri.of(selfRequest.toString()));
        linkBuilder.relation(com.ibm.fhir.model.type.String.of("self"));
        bundleBuilder.link(linkBuilder.build());
        bundleBuilder.type(BundleType.HISTORY);

        return bundleBuilder.build();
    }

    @Override
    public ResourceEraseRecord doErase(FHIROperationContext operationContext, EraseDTO eraseBean) throws FHIROperationException {
        // @implNote doReindex has a nice pattern to handle some retries in case of deadlock exceptions
        final int TX_ATTEMPTS = 5;
        int attempt = 1;
        ResourceEraseRecord eraseRecord = new ResourceEraseRecord();
        do {
            FHIRTransactionHelper txn = null;
            try {
                txn = new FHIRTransactionHelper(getTransaction());
                txn.begin();
                eraseRecord = persistence.erase(eraseBean);
                attempt = TX_ATTEMPTS; // end the retry loop
            } catch (FHIRPersistenceDataAccessException x) {
                if (x.isTransactionRetryable() && attempt < TX_ATTEMPTS) {
                    log.info("attempt #" + attempt + " failed, retrying transaction");
                } else {
                    throw new FHIROperationException("Error during $erase", x);
                }
            } catch (Exception x) {
                attempt = TX_ATTEMPTS; // end the retry loop
                throw new FHIROperationException("Error during $erase", x);
            } finally {
                if (txn != null) {
                    txn.end();
                }
            }
        } while (attempt++ < TX_ATTEMPTS);
        return eraseRecord;
    }
}