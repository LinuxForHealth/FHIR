/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.smart;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.model.resource.Binary;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Provenance;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.code.CompartmentType;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.compartment.CompartmentHelper;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.util.ReferenceUtil;
import org.linuxforhealth.fhir.search.util.ReferenceValue;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptor;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.smart.JWT.Claim;
import org.linuxforhealth.fhir.smart.JWT.DecodedJWT;
import org.linuxforhealth.fhir.smart.Scope.ContextType;
import org.linuxforhealth.fhir.smart.Scope.Permission;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;

/**
 * A persistence interceptor that enforces authorization policy based on a JWT access token with SMART-on-FHIR scopes.
 *
 * <p><a href="http://hl7.org/fhir/smart-app-launch/1.0.0/scopes-and-launch-context/index.html">SMART App Launch: Scopes and Launch Context</a>
 * defines the following pattern for the OAuth 2.0 scopes expected in the JWT:
 * <pre>
 * ( 'patient' | 'user' ) '/' ( fhir-resource | '*' ) '.' ( 'read' | 'write' | '*' )`
 * </pre>
 *
 * <p><a href="http://www.hl7.org/fhir/smart-app-launch/backend-services.html">SMART Backend Services</a>
 * extends that to include an additional context type for 'system'.
 *
 * <p>This interceptor supports both flavors.
 * <p>Before and after each interaction, as appropriate, the Authorization header
 * is checked for a scope that permits the requested interaction. If the scope that permits the interaction is of
 * context type 'patient' then the interceptor looks for a {@code patient_id} claim in the access token.
 * <ol>
 * <li> For search interactions targeting resource types that can be in a patient compartment, the search is automatically
 *   scoped to the Patient compartment(s) of the id(s) in the patient_id claim.
 * <li> For any interaction that returns resources, if the resource type can be in a patient compartment then the interceptor
 *   ensures that it is in the compartment of the the id(s) passed in the patient_id claim.
 * </ol>
 */
public class AuthzPolicyEnforcementPersistenceInterceptor implements FHIRPersistenceInterceptor {
    private static final Logger log = Logger.getLogger(AuthzPolicyEnforcementPersistenceInterceptor.class.getName());

    private static final String DAVINCI_DRUG_FORMULARY_COVERAGE_PLAN =
            "http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition/usdf-CoveragePlan";

    private static final String BEARER_TOKEN_PREFIX = "Bearer";
    private static final String PATIENT = "Patient";
    private static final String PROVENANCE = "Provenance";
    private static final String RESOURCE = "Resource";

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    private final CompartmentHelper compartmentHelper = new CompartmentHelper();
    private final SearchHelper searchHelper = new SearchHelper();

    @Override
    public void beforeInvoke(FHIROperationContext context) throws FHIRPersistenceInterceptorException {
        Permission neededPermission;
        Set<String> resourceTypes;
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
        List<Scope> systemScopesFromToken = getScopesFromToken(jwt).get(ContextType.SYSTEM);

        for (String resourceType : resourceTypes) {
            checkSystemScopes(resourceType, neededPermission, systemScopesFromToken, jwt);
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
        List<Scope> systemScopesFromToken = getScopesFromToken(jwt).get(ContextType.SYSTEM);

        for (String resourceType : resourceTypes) {
            checkSystemScopes(resourceType, neededPermission, systemScopesFromToken, jwt);
        }
    }

    private Set<String> computeImportResourceTypes(FHIROperationContext context) {
        Parameters parameters = (Parameters) context.getProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS);
        Set<String> types = parameters.getParameter().stream()
            .filter(p -> "input".equals(p.getName().getValue()))
            .map(p -> p.getPart())
            .flatMap(part -> part.stream()
                .filter(pp -> "type".equals(pp.getName().getValue()))
                .map(pp -> pp.getValue().as(ModelSupport.FHIR_STRING).getValue()))
            .collect(Collectors.toSet());

        return Collections.unmodifiableSet(types);
    }

    private Set<String> computeExportResourceTypes(FHIROperationContext context) {
        Set<String> supportedResourceTypes = FHIRConfigHelper.getSupportedResourceTypes();
        Set<String> resourceTypes = new HashSet<>();
        Parameters parameters = (Parameters) context.getProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS);
        Optional<Parameter> typesParam = parameters.getParameter().stream().filter(p -> "_type".equals(p.getName().getValue())).findFirst();
        switch (context.getType()) {
        case INSTANCE:      // Group/:id/$export
        case RESOURCE_TYPE: // Patient/$export
            // Either way, the set of resourceTypes to export are those from the Patient compartment
            try {
                Set<String> compartmentResourceMembers = compartmentHelper.getCompartmentResourceTypes(PATIENT);
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
                resourceTypes = Set.of(typesString.split(","));
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

        ContextType approvedContext = checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
        if (approvedContext == ContextType.PATIENT) {
            assertPatientIdClaimIsValued(jwt);
        }
    }

    private void assertPatientIdClaimIsValued(DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        if (jwt.getClaim("patient_id").isNull()) {
            String msg = "Access token is missing 'patient_id' claim";
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    @Override
    public void beforeVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());

        ContextType approvedContext = checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
        if (approvedContext == ContextType.PATIENT) {
            assertPatientIdClaimIsValued(jwt);
        }
    }

    @Override
    public void beforeHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());

        if (event.getFhirResourceId() == null) {
            // System/type-level history
            enforceSystemLevelAccess(resourceType, event.getSystemHistoryContextImpl().getResourceTypes(), jwt);
        } else {
            ContextType approvedContext = checkScopes(resourceType, Permission.READ, getScopesFromToken(jwt));
            if (approvedContext == ContextType.PATIENT) {
                assertPatientIdClaimIsValued(jwt);
            }
        }
    }

    private void enforceSystemLevelAccess(String resourceType, List<String> types, DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        Map<ContextType, List<Scope>> groupedScopes = getScopesFromToken(jwt);
        boolean hasTypeParam = types != null && !types.isEmpty();

        // Check for user or system access to the resource types
        // If types specified, then check each of those, otherwise check the single type (which will be 'Resource')
        if (!hasTypeParam && RESOURCE.equals(resourceType)) {
            if (!isApprovedByScopes(resourceType, Permission.READ, groupedScopes.get(ContextType.USER)) &&
                    !isApprovedByScopes(resourceType, Permission.READ, groupedScopes.get(ContextType.SYSTEM))) {
                String msg = "Whole-system interactions require a user or system scope with a wildcard resource type: "
                        + "('user'|'system') '/' '*' '.' ('read'|'*')";
                if (log.isLoggable(Level.FINE)) {
                    log.fine(msg);
                }
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }
        } else {
            List<String> typesToCheck = hasTypeParam ? types : Arrays.asList(resourceType);
            for (String type : typesToCheck) {
                if (!isApprovedByScopes(type, Permission.READ, groupedScopes.get(ContextType.USER)) &&
                        !isApprovedByScopes(type, Permission.READ, groupedScopes.get(ContextType.SYSTEM))) {

                    if (isApprovedByScopes(type, Permission.READ, groupedScopes.get(ContextType.PATIENT))) {
                        boolean isPatientCompartmentResource = false;
                        try {
                            isPatientCompartmentResource = compartmentHelper.getCompartmentResourceTypes(PATIENT).contains(type);
                        } catch (Exception e) {
                            String msg = "Unexpected exception while enforcing system level access";
                            throw new FHIRPersistenceInterceptorException(msg, e)
                                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.EXCEPTION));
                        }

                        if (isPatientCompartmentResource) {
                            String msg = "'patient' scoped access tokens are not supported for system-level interactions against " +
                                    "patient compartment resource types like " + type;
                            if (log.isLoggable(Level.FINE)) {
                                log.fine(msg);
                            }
                            throw new FHIRPersistenceInterceptorException(msg)
                                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                        } else {
                            // allow it and move on to the next type to check
                            continue;
                        }
                    }

                    String msg = "Read permission for system-level interaction with type '" + type +
                            "' is not granted by any of the provided scopes: " + groupedScopes.values();
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(msg);
                    }
                    throw new FHIRPersistenceInterceptorException(msg)
                            .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                }
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
        String resourceType = event.getFhirResourceType();
        DecodedJWT jwt = JWT.decode(getAccessToken());
        FHIRSearchContext searchContext = event.getSearchContextImpl();

        if (RESOURCE.equals(resourceType)) {
            // System-level search
            enforceSystemLevelAccess(resourceType, searchContext.getSearchResourceTypes(), jwt);
            return;
        }

        ContextType approvedContext = checkScopes(event.getFhirResourceType(), Permission.READ, getScopesFromToken(jwt));
        if (approvedContext == ContextType.SYSTEM || approvedContext == ContextType.USER) {
            return;
        }

        // if the interaction is granted by PATIENT (and not SYSTEM or USER)
        // then scope the search to the compartment(s) of the in-context patient(s)
        assertPatientIdClaimIsValued(jwt);
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
                if (r == null || !isInCompartment(compartment, compartmentId, r, CompartmentType.PATIENT, patientIdFromToken)) {
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
                if (compartmentHelper.getCompartmentResourceTypes(PATIENT).contains(event.getFhirResourceType())) {
                    // Special case for the List resource because we want to enable searches for Formulary Coverage Plan lists
                    if ("List".equals(event.getFhirResourceType())) {
                        // In this case, we will rely on the afterSearch logic to prevent unauthorized access to patient-scoped Lists
                        return;
                    }

                    // Build the Patient compartment inclusion criteria search parameter
                    QueryParameter inclusionCriteria = searchHelper.buildInclusionCriteria(PATIENT, patientIdFromToken, event.getFhirResourceType());

                    // Add the inclusion criteria parameter to the front of the search parameter list
                    searchContext.getSearchParameters().add(0, inclusionCriteria);
                }
            } catch (Exception e) {
                String msg = "Unexpected exception converting to Patient compartment search: " + e.getMessage();
                throw new FHIRPersistenceInterceptorException(msg).withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.EXCEPTION));
            }
        }
    }

    @Override
    public void beforeCreate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getFhirResource(), getPatientIdFromToken(jwt), Permission.WRITE, getScopesFromToken(jwt));
        validateBinarySecurityContext(event.getFhirResourceType(), event.getFhirResource());
    }

    @Override
    public void beforeDelete(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        enforce(event.getPrevFhirResource(), getPatientIdFromToken(jwt), Permission.WRITE, getScopesFromToken(jwt));
    }

    @Override
    public void beforeUpdate(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        beforeUpdateOrPatch(event);
    }

    @Override
    public void beforePatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        beforeUpdateOrPatch(event);
    }

    private void beforeUpdateOrPatch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        Map<ContextType, List<Scope>> scopesFromToken = getScopesFromToken(jwt);

        // First, check READ permission on the existing resource to ensure we don't write over something that
        // the user doesn't have access to
        enforce(event.getPrevFhirResource(), patientIdFromToken, Permission.READ, scopesFromToken);
        enforce(event.getFhirResource(), patientIdFromToken, Permission.WRITE, scopesFromToken);
        validateBinarySecurityContext(event.getFhirResourceType(), event.getFhirResource());
    }

    @Override
    public void afterRead(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        Map<ContextType, List<Scope>> scopesFromToken = getScopesFromToken(jwt);
        String resourceType = event.getFhirResourceType();
        String resourceId = event.getFhirResourceId();
        Resource resource = event.getFhirResource();

        if (PATIENT.equals(resourceType)) {
            // Patient differs from other resource types in that we enforce on null
            // to avoid leaking "does this resource id exist on the system?" to unauthorized users
            enforce(resourceType, resourceId, resource, patientIdFromToken, Permission.READ, scopesFromToken);
        } else if (resource != null) {
            enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
            enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
        }
    }

    @Override
    public void afterVread(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        Map<ContextType, List<Scope>> scopesFromToken = getScopesFromToken(jwt);
        String resourceType = event.getFhirResourceType();
        String resourceId = event.getFhirResourceId();
        Resource resource = event.getFhirResource();

        if (PATIENT.equals(resourceType)) {
            // Patient differs from other resource types in that we enforce on null
            // to avoid leaking "does this resource id exist on the system?" to unauthorized users
            enforce(resourceType, resourceId, resource, patientIdFromToken, Permission.READ, scopesFromToken);
        } else if (resource != null) {
            enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
            enforce(resource, patientIdFromToken, Permission.READ, scopesFromToken);
        }
    }

    @Override
    public void afterHistory(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        Map<ContextType, List<Scope>> scopesFromToken = getScopesFromToken(jwt);

        // used to avoid leaking Patient id existence in the off-chance of a history page with only deleted entries
        boolean hasResourceContent = false;
        if (event.getFhirResource() instanceof Bundle) {
            for (Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry()) {
                String resourceType = getResourceType(entry);
                String resourceId = getResourceId(entry);
                Resource resource = entry.getResource();

                if (resourceType == null && resourceId == null) {
                    throw new FHIRPersistenceInterceptorException("Unable to enforce authorization for history interaction "
                            + "due to failure to compute the resource type and id for one or more response Bundle entries.");
                }

                enforceDirectProvenanceAccess(event, resource, patientIdFromToken, scopesFromToken);
                if (resource == null && entry.getRequest() != null && entry.getRequest().getMethod().getValueAsEnum() == HTTPVerb.Value.DELETE) {
                    // explicitly allow DELETE entries
                    // this is safe because beforeHistory prohibits system and type-level history request for patient-compartment resource types
                    // that are only covered by 'patient' scoped access tokens (and not 'user' or 'system' scopes)
                    continue;
                }

                hasResourceContent = true;
                enforce(resourceType, resourceId, resource, patientIdFromToken, Permission.READ, scopesFromToken);
            }
        } else {
            throw new IllegalStateException("Expected event resource of type Bundle but found " +
                    event.getFhirResource().getClass().getSimpleName());
        }

        // avoid Patient id existence leakage
        if (PATIENT.equals(event.getFhirResourceType()) && !patientIdFromToken.contains(event.getFhirResourceId()) && !hasResourceContent) {
            String msg = "The requested interaction is not permitted by any of the passed scopes " + scopesFromToken +
                    " for context id(s): " + patientIdFromToken;
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    private void enforceDirectProvenanceAccess(FHIRPersistenceEvent event, Resource resource, Set<String> patientIdFromToken, Map<ContextType, List<Scope>> scopesFromToken)
            throws FHIRPersistenceInterceptorException {
        if (resource instanceof Provenance) {
            ContextType approvedContext = checkScopes(PROVENANCE, Permission.READ, scopesFromToken);
            // if the approving context is of type 'patient' then the Provenance should only be returned if the patient has access
            // to the target of the Provenance
            if (approvedContext == ContextType.PATIENT) {
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
    }

    /**
     * Determine whether authorization to one or more referenced resources is granted by the end user in the form of scope strings.
     *
     * @param references a list of resource references; this method will dereference only relative literal references
     * @param persistence the FHIRPersistence implementation to use for dereferencing the literal references
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission
     * @param grantedScopes the SMART scopes associated with the request, indexed by ContextType
     * @throws IllegalStateException if the baseUrl cannot be computed from the request context
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(List<Reference> references, FHIRPersistence persistence, Set<String> contextIds, Permission requiredPermission, Map<ContextType, List<Scope>> grantedScopes) {
        boolean allow = false;

        String baseUrl;
        try {
            baseUrl = ReferenceUtil.getServiceBaseUrl();
        } catch (FHIRSearchException e) {
            throw new IllegalStateException("Unexpected error while computing the service baseUrl");
        }

        for (Reference ref : references) {
            ReferenceValue referenceValue = ReferenceUtil.createReferenceValueFrom(ref, baseUrl);
            if (ReferenceValue.ReferenceType.LITERAL_RELATIVE == referenceValue.getType()) {
                Class<? extends Resource> resourceType = ModelSupport.getResourceType(referenceValue.getTargetResourceType());
                try {
                    SingleResourceResult<? extends Resource> result = executeRead(persistence, referenceValue, resourceType);

                    if (result.isSuccess() && isInCompartment(result.getResourceTypeName(), result.getLogicalId(), result.getResource(), CompartmentType.PATIENT, contextIds)) {
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

    /**
     * @param persistence
     * @param resourceType
     * @param resourceId
     * @return the requested resource or null if the resource could not be read (e.g. if it is deleted or does not exist)
     * @throws FHIRPersistenceInterceptorException
     */
    private Resource executeRead(FHIRPersistence persistence, Class<? extends Resource> resourceType, String resourceId)
            throws FHIRPersistenceInterceptorException {
        try {
            FHIRPersistenceContext freshContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
            SingleResourceResult<? extends Resource> result = persistence.read(freshContext, resourceType, resourceId);
            return result.getResource();
        } catch (FHIRPersistenceException e) {
            String msg = "Unexpected error while reading resource of type " + resourceType.getSimpleName();
            log.log(Level.WARNING, msg + " and id " + resourceId, e);
            throw new FHIRPersistenceInterceptorException(msg);
        }
    }

    @Override
    public void afterSearch(FHIRPersistenceEvent event) throws FHIRPersistenceInterceptorException {
        DecodedJWT jwt = JWT.decode(getAccessToken());
        Set<String> patientIdFromToken = getPatientIdFromToken(jwt);
        Map<ContextType, List<Scope>> scopesFromToken = getScopesFromToken(jwt);

        if (event.getFhirResource() instanceof Bundle) {
            for (Bundle.Entry entry : ((Bundle) event.getFhirResource()).getEntry()) {
                String resourceType = getResourceType(entry);
                String resourceId = getResourceId(entry);
                if (resourceType != null && resourceId != null) {
                    enforce(resourceType, resourceId, entry.getResource(), patientIdFromToken, Permission.READ, scopesFromToken);
                } else {
                    throw new FHIRPersistenceInterceptorException("Unable to enforce authorization for search interaction "
                            + "due to failure to compute the resource type and id for one or more response Bundle entries.");
                }
            }
        } else {
            throw new IllegalStateException("Expected event resource of type Bundle but found " +
                    event.getFhirResource().getClass().getSimpleName());
        }
    }

    /**
     * Check whether the permissions required for the current interaction are granted by the passed scopes.
     *
     * @param resourceType the resource type used in the endpoint of the request (or "Resource" for whole-system interactions)
     * @param requiredPermission the permission required for the requested interaction
     * @param grantedScopes the SMART scopes associated with the request, indexed by ContextType
     * @return
     *      The ContextType of the scope that permits the requested interaction; 'system/' before 'user/' before 'patient/' scopes
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private ContextType checkScopes(String resourceType, Permission requiredPermission, Map<ContextType, List<Scope>> grantedScopes) throws FHIRPersistenceInterceptorException {
        // The order is important
        for (ContextType context : List.of(ContextType.SYSTEM, ContextType.USER, ContextType.PATIENT)) {
            if (isApprovedByScopes(resourceType, requiredPermission, grantedScopes.get(context))) {
                return context;
            }
        }

        String msg = requiredPermission.value() + " permission for '" + resourceType +
                "' is not granted by any of the provided SMART scopes: " + grantedScopes.values();
        if (log.isLoggable(Level.FINE)) {
            log.fine(msg);
        }
        throw new FHIRPersistenceInterceptorException(msg)
                .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
    }

    /**
     * Check whether the permissions required for the current interaction are granted by the passed *system* scopes.
     *
     * @see #checkScopes(String, Permission, List)
     */
    private void checkSystemScopes(String resourceType, Permission requiredPermission, List<Scope> systemScopes, DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        if (!isApprovedByScopes(resourceType, requiredPermission, systemScopes)) {
            String msg = requiredPermission.value() + " permission for '" + resourceType
                    + "' not granted by any of the provided scopes that begin with 'system/':" + getScopesFromToken(jwt);
            if (log.isLoggable(Level.FINE)) {
                log.fine(msg);
            }
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    private boolean isApprovedByScopes(String resourceType, Permission requiredPermission, List<Scope> grantedScopes) {
        if (grantedScopes == null) {
            return false;
        }

        for (Scope scope : grantedScopes) {
            // "Resource" is used for "*" which applies to all resource types
            if (scope.getResourceType() == ResourceType.RESOURCE || scope.getResourceType().value().equals(resourceType)) {
                if (hasPermission(scope.getPermission(), requiredPermission)) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(requiredPermission.value() + " permission for '" + resourceType +
                                "' is granted via scope " + scope);
                    }
                    return true;
                }
            }
        }
        return false;
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

    /**
     * Enforce the authorizations granted by the end user in the form of scope strings.
     *
     * @param resource the resource to check
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission the permission required for the requested interaction
     * @param grantedScopes the SMART scopes associated with the request, indexed by ContextType
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private void enforce(Resource resource, Set<String> contextIds, Permission requiredPermission, Map<ContextType, List<Scope>> grantedScopes)
            throws FHIRPersistenceInterceptorException {
        Objects.requireNonNull(resource, "resource");
        enforce(resource.getClass().getSimpleName(), resource.getId(), resource, contextIds, requiredPermission, grantedScopes);
    }

    /**
     * Enforce the authorizations granted by the end user in the form of scope strings.
     *
     * @param resourceType the resource type
     * @param resourceId the resource ID; may be null for a FHIR create (POST)
     * @param resource the resource to check, or null if resource was not retrieved
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission the permission required for the requested interaction
     * @param grantedScopes the SMART scopes associated with the request, indexed by ContextType
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private void enforce(String resourceType, String resourceId, Resource resource, Set<String> contextIds, Permission requiredPermission, Map<ContextType, List<Scope>> grantedScopes)
            throws FHIRPersistenceInterceptorException {
        if (!isAllowed(resourceType, resourceId, resource, contextIds, requiredPermission, grantedScopes)) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(requiredPermission.value() + " permission for '" + resourceType + "/" + resourceId +
                        "' is not granted by any of the provided scopes: " + grantedScopes.values() +
                        " for context id(s): " + contextIds);
            }
            String msg = "The requested interaction is not permitted by any of the passed scopes " + grantedScopes.values() +
                    " for context id(s): " + contextIds;
            throw new FHIRPersistenceInterceptorException(msg)
                    .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
        }
    }

    /**
     * Determine whether authorization to a given resource is granted by the end user in the form of scope strings.
     *
     * @param resourceType the resource type
     * @param resourceId the resource ID; may be null in the case of a FHIR create (POST)
     * @param resource the resource to check; null if resource not retrieved
     * @param contextIds an identifier for the current context (e.g. patient or user) as determined by the scope strings
     * @param requiredPermission the permission required for the requested interaction
     * @param grantedScopes the SMART scopes associated with the request, indexed by ContextType
     * @throws FHIRPersistenceInterceptorException if the interaction is not permitted
     */
    private boolean isAllowed(String resourceType, String resourceId, Resource resource, Set<String> contextIds, Permission requiredPermission, Map<ContextType, List<Scope>> grantedScopes)
            throws FHIRPersistenceInterceptorException {
        Objects.requireNonNull(resourceType, "resourceType");
        Objects.requireNonNull(contextIds, "contextIds");

        // For `system` and `user` scopes, we grant access to all resources of the requested type.
        // Implementers that use these scopes are encouraged to layer on their own permissions model beyond this.
        for (ContextType context : List.of(ContextType.SYSTEM, ContextType.USER)) {
            if (!grantedScopes.containsKey(context)) {
                continue;
            }

            // look for any scope that approves the current interaction
            Optional<Scope> approvingScope = grantedScopes.get(context).stream()
                    .filter(s -> s.getResourceType() == ResourceType.RESOURCE || s.getResourceType().value().equals(resourceType))
                    .filter(s -> hasPermission(s.getPermission(), requiredPermission))
                    .findAny();
            if (approvingScope.isPresent()) {
                if (log.isLoggable(Level.FINE)) {
                    String typeWithOptionalId = resourceType;
                    if (resourceId != null) {
                        typeWithOptionalId = typeWithOptionalId + "/" + resourceId;
                    }
                    log.fine(requiredPermission.value() + " permission for '" + typeWithOptionalId +
                        "' is granted via scope " + approvingScope.get());
                }
                return true;
            }
        }

        // For `patient` scopes, except for a couple special cases, we prevent access to resources that can be in a patient
        // compartment unless those resources exist in one or more compartment of the patient ids passed via 'contextIds'
        if (grantedScopes.containsKey(ContextType.PATIENT)) {
            if (contextIds.isEmpty()) {
                String msg = "Access token is missing 'patient_id' claim";
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }

            // look for any scope that approves the current interaction
            Optional<Scope> approvingScope = grantedScopes.get(ContextType.PATIENT).stream()
                    .filter(s -> s.getResourceType() == ResourceType.RESOURCE || s.getResourceType().value().equals(resourceType))
                    .filter(s -> hasPermission(s.getPermission(), requiredPermission))
                    .findAny();

            if (approvingScope.isPresent()) {
                if (PATIENT.equals(resourceType)) {
                    if (contextIds.contains(resourceId)) {
                        log.fine(requiredPermission.value() + " permission for 'Patient/" + resourceId +
                                "' is granted via scope " + approvingScope.get());
                        return true;
                    } else if (resource == null) {
                        // avoid leaking whether a patient resource exists with id or not
                        return false;
                    }
                    // else fall through to the compartment membership check
                } else if (PROVENANCE.equals(resourceType)) {
                    // Addressed for issue #1881, Provenance is a special-case:  a Patient-compartment resource type that
                    // we want to allow access to if and only if the patient has access to one or more resources that it targets.

                    // In the case of search, Provenance resources can be in the response bundle for two reasons:
                    // 1. direct search
                    // 2. _revinclude from a different resource type
                    // For case 1, the search is already scoped to the patient compartment and therefore only approved resources should be included
                    // For case 2, only Provenance resources which target to another resource in the response bundle will be included and therefore we
                    // can base the access decision on those resources rather than the Provenance

                    // In the case of read/vread/history, access to Provenance will be handled elsewhere;
                    // not by its Patient compartment membership but by the membership of the resources which it targets
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(requiredPermission.value() + " permission for 'Provenance/" + resource.getId() +
                            "' is granted via scope " + approvingScope.get());
                    }
                    return true;
                } else if (resource instanceof org.linuxforhealth.fhir.model.resource.List) {
                    // List is another special-case:  a Patient-compartment resource type that
                    // we may want to grant access to when it is not scoped to any particular patient (e.g. for
                    // <a href="http://hl7.org/fhir/us/davinci-drug-formulary/StructureDefinition-usdf-CoveragePlan.html">Drug Formulary</a>).

                    // TODO: consider double-checking that the Patient compartment inclusion criteria for List have no
                    // patient references. The inclusion criteria are "subject" and "source".
                    if (resource.getMeta() != null &&
                            resource.getMeta().getProfile().contains(Canonical.of(DAVINCI_DRUG_FORMULARY_COVERAGE_PLAN))) {
                        if (log.isLoggable(Level.FINE)) {
                            log.fine(requiredPermission.value() + " permission for 'List/" + resource.getId() +
                                "' is granted via scope " + approvingScope.get());
                        }
                        return true;
                    }
                }

                // Else, see if the target resource belongs to the Patient compartment of the in-context patient
                return isInCompartment(resourceType, resourceId, resource, CompartmentType.PATIENT, contextIds);
            }
        }

        return false;
    }

    /**
     * Internal helper for checking compartment membership.
     *
     * @param resourceType
     * @param resourceId possibly null
     * @param resource possibly null
     * @param compartmentType
     * @param contextIds
     * @return true if the resource is in one of the compartment defined by the compartmentType and the contextIds
     *          or if the resource type is not applicable for the given compartmentType
     * @throws FHIRPersistenceInterceptorException if the membership could not be checked
     */
    private boolean isInCompartment(String resourceType, String resourceId, Resource resource, CompartmentType compartmentType, Set<String> contextIds)
            throws FHIRPersistenceInterceptorException {
        String compartment = compartmentType.getValue();

        try {
            if (!compartmentHelper.getCompartmentResourceTypes(compartment).contains(resourceType)) {
                // If the resource type is not applicable for the patient compartment, allow it
                // TODO: this may be overly broad...how do we appropriately scope user access to non-Patient resources?
                return true;
            }

            if (resource == null) {
                // Two reasons we might not have a resource:
                // 1. if this is a history response and there exists one or more DELETE entries in the response
                // 2. if this is a history/search operation with an HTTP Prefer header with 'return=minimal'

                // Case 1 is handled in afterHistory before this method would be called (where we can better determine the type of the entry)
                // For case 2, if we've made it this far then the resourceType can possibly exist in a patient compartment but we have no way to check which one
                String msg = "Unable to determine compartment membership for one or more resources of type '" + resourceType + "'"
                        + " due to the resource content being absent in the response";
                throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
            }

            // If the target resource type matches the compartment type, allow it if the id is one of the passed contextIds
            if (compartment.equals(resourceType) && contextIds.contains(resourceId)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Bypassing compartment check for the compartment identity resource " + resourceType + "/" + resourceId);
                }
                return true;
            }

            Set<String> inclusionCriteria = compartmentHelper.getCompartmentResourceTypeInclusionCriteria(compartment, resourceType);

            EvaluationContext resourceContext = new FHIRPathEvaluator.EvaluationContext(resource);

            for (String searchParmCode : inclusionCriteria) {
                try {
                    SearchParameter inclusionParm = searchHelper.getSearchParameter(resourceType, searchParmCode);
                    if (inclusionParm != null && inclusionParm.getExpression() != null) {
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

        if (refValue.getType() == org.linuxforhealth.fhir.search.util.ReferenceValue.ReferenceType.LITERAL_RELATIVE &&
                PATIENT.equals(refValue.getTargetResourceType())) {
            return refValue.getValue();
        } else if (log.isLoggable(Level.FINE)){
            log.fine("Skipping non-patient / non-relative reference: '" + reference + "'");
        }

        return null;
    }

    private String getAccessToken() throws FHIRPersistenceInterceptorException {
        List<String> list = FHIRRequestContext.get().getHttpHeaders().get("Authorization");
        if (list == null || list.size() != 1) {
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

    /**
     * @param jwt
     * @return a map from context type (patient | user | system) to the list of SMART scopes for that type
     * @throws FHIRPersistenceInterceptorException
     */
    private Map<ContextType, List<Scope>> getScopesFromToken(DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
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
                .collect(Collectors.groupingBy(s -> s.getContextType()));
    }

    /**
     * @return the set of Patient.id context values in the patient_id claim or an empty set if none exist
     */
    private Set<String> getPatientIdFromToken(DecodedJWT jwt) throws FHIRPersistenceInterceptorException {
        Claim claim = jwt.getClaim("patient_id");
        if (claim.isMissing() || claim.isNull()) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Authorization token is missing 'patient_id' claim");
            }
            return Collections.emptySet();
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

    /**
     * Gets the resource type of the resource represented in the bundle entry.
     * @param entry the bundle entry
     * @return the resource type, or null
     */
    private static String getResourceType(Bundle.Entry entry) {
        Resource resource = entry.getResource();
        if (resource != null) {
            return resource.getClass().getSimpleName();
        } else if (entry.getFullUrl() != null && entry.getFullUrl().getValue() != null) {
            String[] urlParts = entry.getFullUrl().getValue().split("/");
            if (urlParts.length > 1) {
                return urlParts[urlParts.length - 2];
            }
        }
        return null;
    }

    /**
     * Gets the resource ID of the resource represented in the bundle entry.
     * @param entry the bundle entry
     * @return the resource ID, or null
     */
    private static String getResourceId(Bundle.Entry entry) {
        Resource resource = entry.getResource();
        if (resource != null) {
            return resource.getId();
        } else if (entry.getFullUrl() != null && entry.getFullUrl().getValue() != null) {
            String[] urlParts = entry.getFullUrl().getValue().split("/");
            if (urlParts.length > 1) {
                return urlParts[urlParts.length - 1];
            }
        }
        return null;
    }

    /**
     * Validate if a FHIR Binary resource passed by the user for create/update operation has the securityContext field.
     * If the securityContext field exists, throw an error or log a warning message based on the validateBinarySecurityContext configuration.
     * By default, validateBinarySecurityContext is set to true and will throw an error if securityContext field is passed in the request.
     * If validateBinarySecurityContext configuration is set to false, then log a warning message.
     * @param resourceType the FHIR resource type.
     * @param resource the FHIR Resource instance.
     * @throws FHIRPersistenceInterceptorException when validateBinarySecurityContext configuration is set to true and securityContext field is passed in the request.
     */
    private void validateBinarySecurityContext(String resourceType, Resource resource) throws FHIRPersistenceInterceptorException {
        if (resource instanceof Binary) {
            Binary binaryResource = (Binary) resource;
            if (binaryResource.getSecurityContext() != null) {
                boolean validateBinarySecurityContext = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_VALIDATE_BINARY_SECURITY_CONTEXT, true);
                String msg = "securityContext is not supported for resource type " + resourceType;
                if (validateBinarySecurityContext) {
                    throw new FHIRPersistenceInterceptorException(msg)
                        .withIssue(FHIRUtil.buildOperationOutcomeIssue(msg, IssueType.FORBIDDEN));
                }
                log.warning(msg);
            }
        }
    }
}
