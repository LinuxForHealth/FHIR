/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.CompartmentType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.ReferenceUtil;
import com.ibm.fhir.search.util.ReferenceValue;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.spi.interceptor.FHIRPersistenceInterceptor;
import com.ibm.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.smart.JWT.Claim;
import com.ibm.fhir.smart.JWT.DecodedJWT;
import com.ibm.fhir.smart.Scope.ContextType;
import com.ibm.fhir.smart.Scope.Permission;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;

public class AuthzPolicyEnforcementPersistenceInterceptor implements FHIRPersistenceInterceptor {
    private static final Logger log = Logger.getLogger(AuthzPolicyEnforcementPersistenceInterceptor.class.getName());

    private static final String DAVINCI_DRUG_FORMULARY_COVERAGE_PLAN =
            "http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-CoveragePlan";

    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final String PATIENT = "Patient";

    private static final String REQUEST_NOT_PERMITTED = "Requested interaction is not permitted by any of the passed scopes.";

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @Override
    public void beforeInvoke(FHIROperationContext context) throws FHIRPersistenceInterceptorException {
        Permission neededPermission;
        List<String> resourceTypes;
        if ("import".equals(context.getOperationCode())) {
            neededPermission = Permission.WRITE;
            resourceTypes = computeImportResourceTypes(context);
        } else if ("export".equals(context.getOperationCode())) {
            neededPermission = Permission.READ;
            resourceTypes = computeExportResourceTypes(context);
        } else {
            return;
        }

        if (resourceTypes.isEmpty()) {
            throw new IllegalStateException("The set of resource types was unexpectedly empty");
        }

        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<Scope> scopesFromToken = getScopesFromToken(jwt).stream()
                .filter(s -> s.getContextType() == ContextType.SYSTEM)
                .collect(Collectors.toList());

        for (String resourceType : resourceTypes) {
            checkSystemScopes(resourceType, neededPermission, scopesFromToken, jwt);
        }
    }

    @Override
    public void afterInvoke(FHIROperationContext context) throws FHIRPersistenceInterceptorException {
        Permission neededPermission = Permission.READ;
        Set<String> resourceTypes = new HashSet<>();

        if (!"bulkdata-status".equals(context.getOperationCode())) {
            return;
        }

        Parameters parameters = (Parameters) context.getProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS);

        // bulkdata-status has a special JSON response that is not a Parameters object
        Response response = (Response) context.getProperty(FHIROperationContext.PROPNAME_RESPONSE);

        if (response.hasEntity()) {
            Object entity = response.getEntity();
            if (entity instanceof String) {
                JsonReader responseReader = JSON_READER_FACTORY.createReader(new StringReader((String)entity));
                JsonObject responseObj = responseReader.readObject();
                String request = responseObj.getJsonString("request").getString();
                if (request.contains("$export")) {
                    for (JsonValue output : responseObj.getJsonArray("output")) {
                         resourceTypes.add(output.asJsonObject().getString("type"));
                    }
                } else if (request.contains("$import")) {
                    // nothing much to check for bulk-import status; the only output is a set of OperationOutcome
                } else {
                    String jobId = parameters.getParameter().stream()
                            .filter(p -> "job".equals(p.getName().getValue()))
                            .map(p -> p.getValue().as(ModelSupport.FHIR_STRING).getValue())
                            .findFirst()
                            .get();
                    throw new IllegalStateException("Bulk data request for job '" + jobId + "' is neither '$import' nor '$export'!");
                }
            } else {
                throw new IllegalStateException("Encountered unexpected response entity of type " + entity.getClass().getName());
            }
        }

        DecodedJWT jwt = JWT.decode(getAccessToken());
        List<Scope> scopesFromToken = getScopesFromToken(jwt).stream()
                .filter(s -> s.getContextType() == ContextType.SYSTEM)
                .collect(Collectors.toList());

        for (String resourceType : resourceTypes) {
            checkSystemScopes(resourceType, neededPermission, scopesFromToken, jwt);
        }
    }

    private List<String> computeImportResourceTypes(FHIROperationContext context) {
        Parameters parameters = (Parameters) context.getProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS);
        Set<String> types = parameters.getParameter().stream()
            .filter(p -> "input".equals(p.getName().getValue()))
            .map(p -> p.getPart())
            .flatMap(part -> part.stream()
                .filter(pp -> "type".equals(pp.getName().getValue()))
                .map(pp -> pp.getValue().as(ModelSupport.FHIR_STRING).getValue()))
            .collect(Collectors.toSet());

        return new ArrayList<>(types);
    }

    /**
     * gets the supported resource types
     * @return
     */
    private List<String> getSupportedResourceTypes() {
        try {
            List<String> rts = FHIRConfigHelper.getSupportedResourceTypes();
            if (!rts.isEmpty()) {
                return rts;
            }
        } catch (FHIRException e) {
            log.throwing(this.getClass().getName(), "getSupportedResourceTypes", e);
        }
        return ModelSupport.getResourceTypes(false)
                .stream()
                .map(clz -> clz.getSimpleName())
                .collect(Collectors.toList());
    }

    private List<String> computeExportResourceTypes(FHIROperationContext context) {
        List<String> supportedResourceTypes = getSupportedResourceTypes();
        List<String> resourceTypes = new ArrayList<>();
        Parameters parameters = (Parameters) context.getProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS);
        Optional<Parameter> typesParam = parameters.getParameter().stream().filter(p -> "_type".equals(p.getName().getValue())).findFirst();
        switch (context.getType()) {
        case INSTANCE:      // Group/:id/$export
        case RESOURCE_TYPE: // Patient/$export
            // Either way, the set resourceTypes to export are those from the Patient compartment
            try {
                List<String> compartmentResourceMembers = CompartmentUtil.getCompartmentResourceTypes(PATIENT);
                if (typesParam.isPresent() && typesParam.get().getValue() != null) {
                    String typesString = typesParam.get().getValue().as(ModelSupport.FHIR_STRING).getValue();
                    for (String requestedType : Arrays.asList(typesString.split(","))) {
                        if (!supportedResourceTypes.contains(requestedType)) {
                            throw new IllegalStateException("Requested resource is not configured");
                        }

                        if (!compartmentResourceMembers.contains(requestedType)) {
                            throw new IllegalStateException("Requested resource is outside of the Patient Compartment");
                        }
                        resourceTypes.add(requestedType);
                    }
                } else {
                    resourceTypes = compartmentResourceMembers;
                }
            } catch (FHIRSearchException e) {
                throw new IllegalStateException("Unexpected error while computing the resource types for the export", e);
            }
            break;
        case SYSTEM:
            if (typesParam.isPresent() && typesParam.get().getValue() != null) {
                String typesString = typesParam.get().getValue().as(ModelSupport.FHIR_STRING).getValue();
                resourceTypes = Arrays.asList(typesString.split(","));
                for (String resourceType : resourceTypes) {
                    if (!supportedResourceTypes.contains(resourceType)) {
                        throw new IllegalStateException("Requested resource is not configured");
                    }
                }
            } else {
                // "Resource" is used as a placeholder for the set of all resource types; equivalent to "*" in SMART
                resourceTypes = supportedResourceTypes;
            }
            break;
        default:
            log.warning("Unexpected export of type " + context.getType());
            break;
        }
        return resourceTypes;
    }

    @Override
    public void beforeRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());

        if (PATIENT.equals(resourceType)) {
            enforceDirectPatientAccess(resourceType, event.getFhirResourceId(), jwt);
        } else {
            checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
        }
    }

    @Override
    public void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());

        if (PATIENT.equals(resourceType)) {
            enforceDirectPatientAccess(resourceType, event.getFhirResourceId(), jwt);
        } else {
            checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
        }
    }

    @Override
    public void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());

        if (PATIENT.equals(resourceType)) {
            enforceDirectPatientAccess(resourceType, event.getFhirResourceId(), jwt);
        } else {
            checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
        }
    }

    private void enforceDirectPatientAccess(String resourceType, String resourceId, DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        List<Scope> scopesFromToken = getScopesFromToken(jwt);
        Map<ContextType, List<Scope>> groupedScopes = getScopesFromToken(jwt).stream()
                .collect(Collectors.groupingBy(s -> s.getContextType()));

        if (isApprovedByScopes(resourceType, Permission.READ, groupedScopes.get(ContextType.USER)) ||
                isApprovedByScopes(resourceType, Permission.READ, groupedScopes.get(ContextType.SYSTEM))) {
            return;
        }

        // If a patient/ scope is the only approving scope, then check the patient context
        if (isApprovedByScopes(resourceType, Permission.READ, groupedScopes.get(ContextType.PATIENT))) {
            Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
            if (!patientIdFromToken.contains(resourceId)) {
                String msg = "Interaction with 'Patient/" + resourceId +
                        "' is not permitted under patient context '" + patientIdFromToken + "'.";
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }
        } else {
            // no approving scopes
            String msg = "Read permission for '" + resourceType +
                    "' is not granted by any of the provided scopes: " + scopesFromToken;
            if (log.isLoggable(Level.FINE)) {
                log.fine(msg);
            }
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    /**
     * This method ensures the search is either for a resource type that is not a member of the
     * patient compartment, or is a valid patient-compartment resource search that is scoped
     * to the patient context from the access token.
     */
    @Override
    public void beforeSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        checkScopes(event.getFhirResourceType(), Permission.READ, getScopesFromToken(jwt));

        FHIRSearchContext searchContext = event.getSearchContextImpl();
        if (searchContext != null) {
            Set<String> patientIdFromToken = getPatientIdFromToken(jwt);

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
                switch(compartment) {
                case "Patient":
                    if (!patientIdFromToken.contains(compartmentId)) {
                        String msg = "Interaction with 'Patient/" + compartmentId + "' is not permitted for patient context " + patientIdFromToken;
                        throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                    }
                    break;
                case "Encounter":
                    Resource r = executeRead(event.getPersistenceImpl(), ModelSupport.getResourceType(compartment), compartmentId);
                    if (!isInCompartment(r, CompartmentType.PATIENT, patientIdFromToken)) {
                        String msg = "Interaction with 'Encounter/" + compartmentId + "' is not permitted for patient context " + patientIdFromToken;
                        throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                    }
                    break;
                case "Device":
                case "Practitioner":
                case "RelatedPerson":
                    String msg = "Compartment search for compartment type '" + compartment + "' is not permitted.";
                    throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                default:
                    // If the operator has extended the server with custom compartment definitions, they will need to protect those separately
                }

                if (log.isLoggable(Level.FINE)) {
                    log.fine("Performing compartment search for compartment '" + compartment + "/" + compartmentId + "'.");
                }
            } else {
                // Not compartment search - validate and convert to Patient compartment search if the resource type can be in the Patient compartment
                try {
                    if (CompartmentUtil.getCompartmentResourceTypes(PATIENT).contains(event.getFhirResourceType())) {
                        // Special case for the List resource because we want to enable searches for Formulary Coverage Plan lists
                        if ("List".equals(event.getFhirResourceType())) {
                            // In this case, we will rely on the afterSearch logic to prevent unauthorized access to patient-scoped Lists
                            return;
                        }

                        // Build the Patient compartment inclusion criteria search parameter
                        QueryParameter inclusionCriteria = SearchUtil.buildInclusionCriteria(PATIENT, patientIdFromToken, event.getFhirResourceType());

                        // Add the inclusion criteria parameter to the front of the search parameter list
                        searchContext.getSearchParameters().add(0, inclusionCriteria);
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
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        List<Scope> scopesFromToken = getScopesFromToken(jwt);

        // First, check READ permission on the existing resource to ensure we don't write over something that
        // the user doesn't have access to
        enforce(event.getPrevFhirResource(), patientIdFromToken, Permission.READ, scopesFromToken);
        enforce(event.getFhirResource(), patientIdFromToken, Permission.WRITE, scopesFromToken);
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Resource resource = event.getFhirResource();
        if (resource != null) {
            DecodedJWT jwt = JWT.decode(getAccessToken());
            Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
            List<Scope> scopesFromToken = getScopesFromToken(jwt);

            enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
            enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
        }
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        Resource resource = event.getFhirResource();
        if (resource != null) {
            DecodedJWT jwt = JWT.decode(getAccessToken());
            Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
            List<Scope> scopesFromToken = getScopesFromToken(jwt);

            enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
            enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
        }
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
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

    private void enforceDirectProvenanceAccess(FHIRPersistenceEvent event, Resource resource, Set<String> patientIdFromToken, List<Scope> scopesFromToken)
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
     * Determine whether authorization to one or more referenced resources is granted by the end user in the form of scope strings.
     *
     * @param references a list of resource references; this method will dereference only relative literal references
     * @param persistence the FHIRPersistence implementation to use for dereferencing the literal references
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws IllegalStateException if the baseUrl cannot be computed from the request context
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(List<Reference> references, FHIRPersistence persistence, Set<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes) {
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

                    if (result.isSuccess() && isInCompartment(result.getResource(), CompartmentType.PATIENT, contextIds)) {
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

    private Resource executeRead(FHIRPersistence persistence, Class<? extends Resource> resourceType, String resourceId)
            throws FHIRPersistenceInterceptorException {
        try {
            FHIRPersistenceContext freshContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
            SingleResourceResult<? extends Resource> result = persistence.read(freshContext, resourceType, resourceId);
            if (!result.isSuccess()) {
                String msg = "Unexpected error while reading resource " + resourceType.getSimpleName() + "/" + resourceId;
                throw new FHIRPersistenceInterceptorException(msg).withIssue(result.getOutcome().getIssue());
            }
            return result.getResource();
        } catch (FHIRPersistenceInterceptorException e) {
            throw e;
        } catch (FHIRPersistenceResourceNotFoundException | FHIRPersistenceResourceDeletedException e) {
            String msg = "The resource '" + resourceType.getSimpleName() + "/" + resourceId + "' does not exist.";
            throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        } catch (FHIRPersistenceException e) {
            String msg = "Unexpected error while reading resource " + resourceType.getSimpleName() + "/" + resourceId;
            log.log(Level.WARNING, msg, e);
            throw new FHIRPersistenceInterceptorException(msg);
        }
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
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
     * Check whether the permissions required for the current interaction are granted by the approved scopes.
     *
     * @param resourceType the resource type used in the endpoint of the request (or "Resource" for whole-system interactions)
     * @param requiredPermission the permission required for the requested interaction
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private void checkScopes(String resourceType, Permission requiredPermission, List<Scope> approvedScopes) throws FHIRPersistenceInterceptorException {
        if (!isApprovedByScopes(resourceType, requiredPermission, approvedScopes)) {
            String msg = requiredPermission.value() + " permission for '" + resourceType +
                    "' is not granted by any of the provided scopes: " + approvedScopes;
            if (log.isLoggable(Level.FINE)) {
                log.fine(msg);
            }
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    /**
     * Check whether the permissions required for the current interaction are granted by the approved *system* scopes.
     *
     * @see #checkScopes(String, Permission, List)
     */
    private void checkSystemScopes(String resourceType, Permission requiredPermission, List<Scope> systemScopes, DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        if (!isApprovedByScopes(resourceType, requiredPermission, systemScopes)) {
            String msg = requiredPermission.value() + " permission for '" + resourceType
                    + "' is not granted by any of the provided 'system/' scopes: " + systemScopes
                    + " requested scopes: " + getScopesFromToken(jwt);
            if (log.isLoggable(Level.FINE)) {
                log.fine(msg);
            }
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    private boolean isApprovedByScopes(String resourceType, Permission requiredPermission, List<Scope> approvedScopes) {
        if (approvedScopes == null) {
            return false;
        }

        for (Scope scope : approvedScopes) {
            // "Resource" is used for "*" which applies to all resource types
            if (scope.getResourceType() == ResourceType.RESOURCE || scope.getResourceType().value().equals(resourceType)) {
                if (hasPermission(scope.getPermission(), requiredPermission)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Enforce the authorizations granted by the end user in the form of scope strings.
     *
     * @param resource the resource to check
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private void enforce(Resource resource, Set<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes)
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
     * Determine whether authorization to a given resource is granted by the end user in the form of scope strings.
     *
     * @param resource the resource to check
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param approvedScopes a list of SMART scopes associated with the request
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(Resource resource, Set<String> contextIds, Permission requiredPermission, List<Scope> approvedScopes)
            throws FHIRPersistenceInterceptorException {
        Objects.requireNonNull(resource, "resource");
        Objects.requireNonNull(contextIds, "contextIds");

        String resourceType = resource.getClass().getSimpleName();
        Map<ContextType, List<Scope>> approvedScopeMap = approvedScopes.stream()
                // First filter the list to only scopes which grant the required permissions on the passed resourceType
                .filter(s -> s.getResourceType() == ResourceType.RESOURCE ||
                        s.getResourceType().value().equals(resourceType))
                .filter(s -> hasPermission(s.getPermission(), requiredPermission))
                // Then group the scopes by their context type
                .collect(Collectors.groupingBy(s -> s.getContextType()));

        if (approvedScopeMap.containsKey(ContextType.SYSTEM)) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(requiredPermission.value() + " permission for '" + resourceType + "/" + resource.getId() +
                    "' is granted via scope " + approvedScopeMap.get(ContextType.SYSTEM));
            }
            return true;
        }

        if (approvedScopeMap.containsKey(ContextType.USER)) {
            // For `user` scopes, we grant access to all resources of the requested type.
            // Implementers that use these scopes are encouraged to layer on their own permissions model beyond this.
            if (log.isLoggable(Level.FINE)) {
                log.fine(requiredPermission.value() + " permission for '" + resourceType + "/" + resource.getId() +
                    "' is granted via scope " + approvedScopeMap.get(ContextType.USER));
            }
            return true;
        }

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
            } else if (resource instanceof com.ibm.fhir.model.resource.List) {
                // List is another special-case:  a Patient-compartment resource type that
                // we may want to grant access to when it is not scoped to any particular patient (e.g. for
                // <a href="http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition-usdf-CoveragePlan.html">Drug Formulary</a>).

                // TODO: consider double-checking that the Patient compartment inclusion criteria for List have no
                // patient references.  The inclusion criteria are "subject" and "source".
                if (resource.getMeta() != null &&
                        resource.getMeta().getProfile().contains(Canonical.of(DAVINCI_DRUG_FORMULARY_COVERAGE_PLAN))) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(requiredPermission.value() + " permission for 'List/" + resource.getId() +
                            "' is granted via scope " + approvedScopeMap.get(ContextType.PATIENT));
                    }
                    return true;
                }
            }

            // Else, see if the target resource belongs to the Patient compartment of the in-context patient
            return isInCompartment(resource, CompartmentType.PATIENT, contextIds);
        }

        return false;
    }

    /**
     * Internal helper for checking compartment membership.
     *
     * @param resource
     * @param compartmentType
     * @param contextIds
     * @return true if the resource is in one of the compartment defined by the compartmentType and the contextIds
     *          or if the resource type is not applicable for the given compartmentType
     */
    private boolean isInCompartment(Resource resource, CompartmentType compartmentType, Set<String> contextIds) {
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
     * @throws FHIRSearchException
     */
    private String getPatientRefVal(FHIRPathNode node) throws FHIRSearchException {
        if (!node.isElementNode() || !node.asElementNode().element().is(Reference.class)) {
            throw new IllegalStateException("Patient compartment inclusionCriteria expression has returned a non-Reference");
        }

        Reference reference = node.asElementNode().element().as(Reference.class);
        ReferenceValue refValue = ReferenceUtil.createReferenceValueFrom(reference, ReferenceUtil.getServiceBaseUrl());

        if (refValue.getType() == com.ibm.fhir.search.util.ReferenceValue.ReferenceType.LITERAL_RELATIVE &&
                PATIENT.equals(refValue.getTargetResourceType())) {
            return refValue.getValue();
        } else if (log.isLoggable(Level.FINE)){
            log.fine("Skipping non-patient / non-relative reference: '" + reference + "'");
        }

        return null;
    }

    /**
     * @param grantedPermission
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
            String msg = "Request must contain exactly one Authorization header.";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
        String header = list.get(0);

        if (!header.startsWith(BEARER_TOKEN_PREFIX)) {
            String msg = "Authorization header must carry a Bearer token";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }

        return header.substring(BEARER_TOKEN_PREFIX.length()).trim();
    }

    private List<Scope> getScopesFromToken(DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        Claim claim = jwt.getClaim("scope");
        if (claim.isNull()) {
            String msg = "Authorization token is missing 'scope' claim";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }

        List<String> scopeStrings;
        String claimString = claim.asString();

        if (claimString != null) {
            scopeStrings = Arrays.asList(claim.asString().split("\\s+"));
        } else {
            log.fine("Found scope claim was expected to be a string but is not; processing as a list");
            scopeStrings = claim.asList();
        }

        return scopeStrings.stream()
                .filter(s -> s.matches(Scope.SCOPE_STRING_REGEX))
                .map(s -> new Scope(s))
                .collect(Collectors.toList());
    }

    private Set<String> getPatientIdFromToken(DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        Claim claim = jwt.getClaim("patient_id");
        if (claim.isNull()) {
            String msg = "Authorization token is missing 'patient_id' claim";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }

        String patientId = claim.asString();
        if (patientId == null) {
            return new HashSet<>(claim.asList());
        }

        return Stream.of(patientId.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
