/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.CompartmentType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
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
        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<String> patientIdFromToken = getPatientIdFromToken(jwt);
        if ("Patient".equals(event.getFhirResourceType()) && !patientIdFromToken.contains(event.getFhirResourceId())) {
            String msg = "Read of 'Patient/" + event.getFhirResourceId() + "' is not permitted for patient context '" + patientIdFromToken + "'.";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
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
        // First, check READ permission on the existing resource to ensure we don't write over something that
        // the user doesn't have access to
        enforce(event.getPrevFhirResource(), getPatientIdFromToken(jwt), Permission.READ, getScopesFromToken(jwt));
        enforce(event.getFhirResource(), getPatientIdFromToken(jwt), Permission.WRITE, getScopesFromToken(jwt));
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getFhirResource(), getPatientIdFromToken(jwt), Permission.READ, getScopesFromToken(jwt));
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getFhirResource(), getPatientIdFromToken(jwt), Permission.READ, getScopesFromToken(jwt));
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        if (event.getFhirResource() instanceof Bundle) {
            for ( Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry()) {
                if (entry.getResource() != null) {
                    enforce(entry.getResource(), getPatientIdFromToken(jwt), Permission.READ, getScopesFromToken(jwt));
                }
            }
        } else {
            throw new IllegalStateException("Expected event resource of type Bundle but found " +
                    event.getFhirResource().getClass().getSimpleName());
        }
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        if (event.getFhirResource() instanceof Bundle) {
            for ( Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry()) {
                if (entry.getResource() != null) {
                    enforce(entry.getResource(), getPatientIdFromToken(jwt), Permission.READ, getScopesFromToken(jwt));
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
            // If the target resource is the Patient resource which matches the in-context patient, allow it
            if (resource instanceof Patient && resource.getId() != null && contextIds.contains(resource.getId())) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(requiredPermission.value() + " permission for 'Patient/" + resource.getId() +
                        "' is granted via scope " + approvedScopeMap.get(ContextType.PATIENT) +
                        " with patient context '" + resource.getId() + "'");
                }
                return;
            }

            // Else, see if the target resource belongs to the Patient compartment of the in-context patient
            try {
                if (!CompartmentUtil.getCompartmentResourceTypes("Patient").contains(resourceType)) {
                    // If the resource is not in the patient compartment, allow it
                    // TODO: this may be overly broad...how do we appropriately scope user access to non-Patient resources?
                    return;
                }

                List<String> inclusionCriteria = CompartmentUtil
                        .getCompartmentResourceTypeInclusionCriteria(CompartmentType.PATIENT.getValue(), resourceType);

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
                                        log.fine(requiredPermission.value() + " permission for '" + resource.getClass().getSimpleName() + "/" + resource.getId() +
                                            "' is granted via scope " + approvedScopeMap.get(ContextType.PATIENT) +
                                            " with patient context '" + patientRefVal + "'");
                                    }
                                    return;
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.log(Level.WARNING, "Unexpected exception while processing inclusionCriteria '" + searchParmCode +
                                "' in the Patient compartment for resource type " + resourceType, e);
                    }
                }
            } catch (FHIRSearchException e) {
                log.log(Level.WARNING, "Unexpected exception while enforcing authorization policy in the Patient compartment"
                        + " for resource type " + resourceType, e);
            }
        }

        if (approvedScopeMap.containsKey(ContextType.USER)) {
            throw new UnsupportedOperationException("SMART scopes with context type 'user' are not yet supported.");
        }


        if (log.isLoggable(Level.FINE)) {
            log.fine(requiredPermission.value() + " permission for '" + resource.getClass().getSimpleName() + "/" + resource.getId() +
                    "' is not granted by any of the provided scopes: " + approvedScopes +
                    " with context id(s): " + contextIds);
        }
        throw new FHIRPersistenceInterceptorException(REQUEST_NOT_PERMITTED)
                .withIssue(FHIRUtil.buildOperationOutcomeIssue(REQUEST_NOT_PERMITTED, IssueType.FORBIDDEN));
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
