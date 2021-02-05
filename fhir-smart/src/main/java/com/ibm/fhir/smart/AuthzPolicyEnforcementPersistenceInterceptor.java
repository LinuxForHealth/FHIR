/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.CompartmentType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.smart.Scope.ContextType;
import com.ibm.fhir.smart.Scope.Permission;

public class AuthzPolicyEnforcementPersistenceInterceptor implements FHIRPersistenceInterceptor {
    private static final Logger log = Logger.getLogger(AuthzPolicyEnforcementPersistenceInterceptor.class.getName());

    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final String PATIENT_REF_PREFIX = "Patient/";

    private static final String REQUEST_NOT_PERMITTED = "Requested interaction is not permitted by any of the passed scopes.";

    @Override
    public void beforeRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        enforceDirectPatientAccess(event);
    }

    @Override
    public void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        enforceDirectPatientAccess(event);
    }

    @Override
    public void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        enforceDirectPatientAccess(event);
    }

    private void enforceDirectPatientAccess(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        if ("Patient".equals(event.getFhirResourceType())) {
            DecodedJWT jwt = JWT.decode(getAccessToken());
            List<String> patientIdFromToken = getPatientIdFromToken(jwt);
            if (!patientIdFromToken.contains(event.getFhirResourceId())) {
                String msg = "Interaction with 'Patient/" + event.getFhirResourceId() +
                        "' is not permitted under patient context '" + patientIdFromToken + "'.";
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }
        }
    }

    /**
     * This method ensures the search is either for a resource type that is not a member of the
     * patient compartment, or is a valid patient-compartment resource search that is scoped
     * to the patient context from the access token.
     */
    @Override
    public void beforeSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        FHIRSearchContext searchContext = event.getSearchContextImpl();
        if (searchContext != null) {
            DecodedJWT jwt = JWT.decode(getAccessToken());
            List<String> patientIdFromToken = getPatientIdFromToken(jwt);

            // Determine if compartment search
            String compartment = null;
            String compartmentId = null;
            for (QueryParameter queryParameter : searchContext.getSearchParameters()) {
                if (queryParameter.isInclusionCriteria()) {
                    // Value will contain a reference to compartment
                    String[] tokens = queryParameter.getValues().get(0).getValueString().split("/");
                    compartment = tokens[0];
                    compartmentId = tokens[1];
                    break;
                }
            }

            if (compartment != null) {
                // Compartment search - validate patient access
                if (!"Patient".equals(compartment)) {
                    String msg = "Compartment search with compartment '" + compartment + "' is not permitted.";
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                }
                if (!patientIdFromToken.contains(compartmentId)) {
                    String msg = "Interaction with 'Patient/" + compartmentId + "' is not permitted under patient context '" + patientIdFromToken + "'.";
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                }
                try {
                    if (!CompartmentUtil.getCompartmentResourceTypes("Patient").contains(event.getFhirResourceType())) {
                        String msg = "Resource type '" + event.getFhirResourceType() + "' is not valid for Patient compartment search.";
                        throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.INVALID));
                    }
                } catch (FHIRSearchException e) {
                    log.log(Level.WARNING, "Unexpected exception while getting compartment resource types", e);
                }
            } else {
                // Not compartment search - validate and convert to Patient compartment search if resource type is member of Patient compartment
                if ("Patient".equals(event.getFhirResourceType())) {
                    String msg = "Non-compartment Patient search is not permitted.";
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                }
                try {
                    if (CompartmentUtil.getCompartmentResourceTypes("Patient").contains(event.getFhirResourceType())) {
                        // Get Patient compartment inclusion criteria search parameters. It will actually be one QueryParameter object with each inclusion
                        // criteria search parameter chained off the root.
                        // NOTE: We currently do not support OR'd compartment searches, nor do we currently expect more than one patient ID to be
                        // specified in the authorization token (see getPatientIdFromToken()). We will use the first ID specified for the compartment search.
                        FHIRSearchContext compartmentSearchContext = SearchUtil.parseQueryParameters("Patient", patientIdFromToken.get(0),
                                ModelSupport.getResourceType(event.getFhirResourceType()), Collections.emptyMap(), searchContext.isLenient());

                        // Add compartment search parameters to front of search parameter list
                        searchContext.getSearchParameters().addAll(0, compartmentSearchContext.getSearchParameters());
                    }
                } catch (Exception e) {
                    String msg = "Unexpected exception converting to Patient compartment search: " + e.getMessage();
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.EXCEPTION));
                }
            }
        }
    }

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getFhirResource(), getPatientIdFromToken(jwt), Permission.WRITE, getScopesFromToken(jwt));
    }

    @Override
    public void beforeDelete(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getPrevFhirResource(), getPatientIdFromToken(jwt), Permission.WRITE, getScopesFromToken(jwt));
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        // First, check READ permission on the existing resource to ensure we don't write over something that
        // the user doesn't have access to
        enforce(event.getPrevFhirResource(), patientIdFromToken, Permission.READ, scopesFromToken);
        enforce(event.getFhirResource(), patientIdFromToken, Permission.WRITE, scopesFromToken);
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Resource resource = event.getFhirResource();
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
        enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Resource resource = event.getFhirResource();
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
        enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        if (event.getFhirResource() instanceof Bundle) {
            for ( Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry()) {
                Resource resource = entry.getResource();

                if (resource != null) {
                    enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
                    enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
                }
            }
        } else {
            throw new IllegalStateException("Expected event resource of type Bundle but found " +
                    event.getFhirResource().getClass().getSimpleName());
        }
    }

    private void enforceDirectProvenanceAccess(FHIRPersistenceEvent event, Resource resource, List<String> patientIdFromToken, List<Scope> scopesFromToken)
            throws FHIRPersistenceInterceptorException {
        if (resource instanceof Provenance) {
            if (!isAllowed(((Provenance) resource).getTarget(), event.getPersistenceImpl(), patientIdFromToken, Permission.READ, scopesFromToken)) {
                String msg = Permission.READ + " permission to 'Provenance/" + resource.getId() +
                        "' with context id(s): " + patientIdFromToken +
                        " requires access to one or more of its target resources.";
                if (log.isLoggable(Level.FINE)) {
                    log.fine(msg);
                }
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }
        }
    }

    /**
     * Determine whether authorization to one or more referenced resources is granted by the end user in the form of scope strings
     *
     * @param references a list of resource references; this method will dereference only relative literal references
     * @param persistence the FHIRPersistence implementation to use for dereferencing the literal references
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws IllegalStateException if the baseUrl cannot be computed from the request context
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(List<Reference> references, FHIRPersistence persistence, List<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes) {
        boolean allow = false;

        String baseUrl;
        try {
            baseUrl = ReferenceUtil.getServiceBaseUrl();
        } catch (FHIRSearchException e) {
            throw new IllegalStateException("Unexpected error while computing the service baseUrl for ");
        }

        for (Reference ref : references) {
            ReferenceValue referenceValue = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
            if (ReferenceValue.ReferenceType.LITERAL_RELATIVE == referenceValue.getType()) {
                Class<? extends Resource> resourceType = ModelSupport.getResourceType(referenceValue.getTargetResourceType());
                try {
                    SingleResourceResult<? extends Resource> result = executeRead(persistence, referenceValue, resourceType);

                    if (result.isSuccess() && checkCompartment(result.getResource(), CompartmentType.PATIENT, contextIds)) {
                        allow = true;
                        break;
                    }
                    if (!result.isSuccess() && log.isLoggable(Level.FINE)) {
                        log.fine("Skipping target " + referenceValue.getTargetResourceType() + "/" + referenceValue.getType() +
                                "' during enforcement due to a read failure: " + result.getOutcome());
                    }
                } catch (FHIRPersistenceException e) {
                    if (log.isLoggable(Level.FINE)){
                        log.log(Level.FINE, "Skipping target '" + referenceValue.getTargetResourceType() + "/" + referenceValue.getType() +
                            "' during enforcement due to an error while reading.", e);
                    }
                }
            } else if (log.isLoggable(Level.FINE)){
                log.fine("Skipping target '" + referenceValue.getValue() + "' of type '" + referenceValue.getType() +
                        "' during enforcement of Provenance access.");
            }
        }

        return allow;
    }

    private SingleResourceResult<? extends Resource> executeRead(FHIRPersistence persistence, ReferenceValue referenceValue,
            Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        FHIRPersistenceContext freshContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
        return referenceValue.getVersion() == null ?
                persistence.read(freshContext, resourceType, referenceValue.getValue()) :
                persistence.vread(freshContext, resourceType, referenceValue.getValue(), referenceValue.getVersion().toString());
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        if (event.getFhirResource() instanceof Bundle) {
            for ( Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry() ) {
                if (entry.getResource() != null) {
                    enforce(entry.getResource(), patientIdFromToken, Permission.READ, scopesFromToken);
                }
            }
        } else {
            throw new IllegalStateException("Expected event resource of type Bundle but found " +
                    event.getFhirResource().getClass().getSimpleName());
        }
    }

    /**
     * Enforce the authorizations granted by the end user in the form of scope strings
     *
     * @param resource the resource to check
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private void enforce(Resource resource, List<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes)
            throws FHIRPersistenceInterceptorException {
        if (!isAllowed(resource, contextIds, requiredPermission, approvedScopes)) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(requiredPermission.value() + " permission for '" + resource.getClass().getSimpleName() + "/" + resource.getId() +
                        "' is not granted by any of the provided scopes: " + approvedScopes +
                        " with context id(s): " + contextIds);
            }
            throw new FHIRPersistenceInterceptorException(REQUEST_NOT_PERMITTED)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(REQUEST_NOT_PERMITTED, IssueType.FORBIDDEN));
        }
    }

    /**
     * Determine whether authorization to a given resource is granted by the end user in the form of scope strings
     *
     * @param resource the resource to check
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(Resource resource, List<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes)
            throws FHIRPersistenceInterceptorException {
        Objects.requireNonNull(resource, "resource");
        Objects.requireNonNull(contextIds, "contextIds");

        String resourceType = resource.getClass().getSimpleName();
        Map<ContextType, List<Scope>> approvedScopeMap = approvedScopes.stream()
                // First filter the list to only scopes which grant the required permissions on the passed resourceType
                .filter(s -> s.getResourceType() == ResourceType.ValueSet.RESOURCE ||
                        s.getResourceType().value().equals(resourceType))
                .filter(s -> hasPermission(s.getPermission(),requiredPermission))
                // Then group the scopes by their context type
                .collect(Collectors.groupingBy(s -> s.getContextType()));

        if (approvedScopeMap.containsKey(ContextType.PATIENT)) {
            if (resource instanceof Provenance) {
                // Addressed for issue #1881, Provenance is a special-case:  a Patient-compartment resource type that
                // we want to allow access to if and only if the patient has access to one or more resources that it targets.

                // In the case of search, Provenance resources can be in the response bundle for two reasons:
                // 1. direct search
                // 2. _revinclude from a different resource type
                // For case 1, the search is already scoped to the patient compartment and therefore only approved resources should be included
                // For case 2, only Provenance resources which target to another resource in the response bundle will be included and therefor we
                // can base the access decision on those resources rather than the Provenance

                // In the case of read/vread/history, access to Provenance will be handled elsewhere;
                // not by its Patient compartment membership but by the membership of the resources which it targets
                if (log.isLoggable(Level.FINE)) {
                    log.fine(requiredPermission.value() + " permission for 'Provenance/" + resource.getId() +
                        "' is granted via scope " + approvedScopeMap.get(ContextType.PATIENT));
                }
                return true;
            }

            // Else, see if the target resource belongs to the Patient compartment of the in-context patient
            return checkCompartment(resource, CompartmentType.PATIENT, contextIds);
        }

        if (approvedScopeMap.containsKey(ContextType.USER)) {
            throw new UnsupportedOperationException("SMART scopes with context type 'user' are not yet supported.");
        }

        return false;
    }

    /**
     * Internal helper for checking compartment membership
     *
     * @param resource
     * @param compartmentType
     * @param contextIds
     * @return true if the resource is in one of the compartment defined by the compartmentType and the contextIds
     *          or if the resource type is not applicable for the given compartmentType
     */
    private boolean checkCompartment(Resource resource, CompartmentType compartmentType, List<String> contextIds) {
        String resourceType = resource.getClass().getSimpleName();
        String compartment = compartmentType.getValue();

        // If the target resource type matches the compartment type, allow it if the id is one of the passed contextIds
        if (compartmentType.getValue().equals(resourceType) && resource.getId() != null && contextIds.contains(resource.getId())) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Bypassing compartment check for the compartment identity resource " + resourceType + "/" + resource.getId());
            }
            return true;
        }

        try {
            if (!CompartmentUtil.getCompartmentResourceTypes(compartment).contains(resourceType)) {
                // If the resource type is not applicable for the patient compartment, allow it
                // TODO: this may be overly broad...how do we appropriately scope user access to non-Patient resources?
                return true;
            }

            List<String> inclusionCriteria = CompartmentUtil
                    .getCompartmentResourceTypeInclusionCriteria(compartment, resourceType);

            EvaluationContext resourceContext = new FHIRPathEvaluator.EvaluationContext(resource);

            for (String searchParmCode : inclusionCriteria) {
                try {
                    SearchParameter inclusionParm = SearchUtil.getSearchParameter(resourceType, searchParmCode);
                    if (inclusionParm != null & inclusionParm.getExpression() != null) {
                        String expression = inclusionParm.getExpression().getValue();
                        Collection<FHIRPathNode> nodes = FHIRPathEvaluator.evaluator().evaluate(resourceContext, expression);
                        for (FHIRPathNode node : nodes) {
                            String patientRefVal = getPatientRefVal(node);
                            if (patientRefVal != null && contextIds.contains(patientRefVal)) {
                                if (log.isLoggable(Level.FINE)) {
                                    log.fine(resourceType + "/" + resource.getId() +
                                        "' is in " + compartment + " compartment '" + patientRefVal + "'");
                                }
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.log(Level.WARNING, "Unexpected exception while processing inclusionCriteria '" + searchParmCode +
                            "' in the " + compartment + " compartment for resource type " + resourceType, e);
                }
            }
        } catch (FHIRSearchException e) {
            log.log(Level.WARNING, "Unexpected exception while enforcing authorization policy in the " + compartment + " compartment"
                    + " for resource type " + resourceType, e);
        }

        return false;
    }

    /**
     * @param node
     * @return the id to the Patient resource referenced by this node (assuming it is a Reference with a valid
     *         reference value); otherwise null
     */
    private String getPatientRefVal(FHIRPathNode node) {
        if (!node.isElementNode() || !node.asElementNode().element().is(Reference.class)) {
            throw new IllegalStateException("Patient compartment inclusionCriteria expression has returned a non-Reference");
        }
        Reference reference = node.asElementNode().element().as(Reference.class);
        if (reference.getReference() != null && reference.getReference().hasValue()) {
            String refVal = reference.getReference().getValue();
            if (refVal != null && refVal.startsWith(PATIENT_REF_PREFIX)) {
                return refVal.substring(PATIENT_REF_PREFIX.length());
            }
        } else if (log.isLoggable(Level.FINE)){
            log.fine("Skipping non-patient / non-relative reference: '" + reference + "'");
        }

        return null;
    }

    /**
     * @param permission
     * @param requiredPermission
     * @return true if the grantedPermission includes the requiredPermission; otherwise false
     */
    private boolean hasPermission(Permission grantedPermission, Permission requiredPermission) {
        if (grantedPermission == Permission.ALL) {
            return true;
        } else {
            return grantedPermission == requiredPermission;
        }
    }

    private String getAccessToken() throws FHIRPersistenceInterceptorException {
        List<String> list = FHIRRequestContext.get().getHttpHeaders().get("Authorization");
        if (list.size() != 1) {
            throw new FHIRPersistenceInterceptorException("Request must contain exactly one Authorization header.");
        }
        String header = list.get(0);

        if (!header.startsWith(BEARER_TOKEN_PREFIX)) {
            throw new FHIRPersistenceInterceptorException("Authorization header must carry a Bearer token");
        }

        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private List<Scope> getScopesFromToken(DecodedJWT jwt) {
        Claim claim = jwt.getClaim("scope");
        if (claim.isNull()) {
            throw new IllegalArgumentException("Authorization token is missing 'scope' claim");
        }

        List<String> scopeStrings;
        String claimString = claim.asString();

        if (claimString != null) {
            scopeStrings = Arrays.asList(claim.asString().split("\\s+"));
        } else {
            log.fine("Found scope claim was expected to be a string but is not; processing as a list");
            scopeStrings = claim.asList(String.class);
        }

        return scopeStrings.stream()
                .filter(s -> s.matches(Scope.SCOPE_STRING_REGEX))
                .map(s -> new Scope(s))
                .collect(Collectors.toList());
    }

    private List<String> getPatientIdFromToken(DecodedJWT jwt) {
        Claim claim = jwt.getClaim("patient_id");
        if (claim.isNull()) {
            throw new IllegalArgumentException("Authorization token is missing 'patient_id' claim");
        }

        String patientId = claim.asString();
        if (patientId == null) {
            log.fine("Found patient_id claim was expected to be a string but is not; processing as a list");
            return claim.asList(String.class);
        }

        return Collections.singletonList(patientId);
    }
}
