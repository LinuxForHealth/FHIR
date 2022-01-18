/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.everything;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_DATE;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_INSTANT;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Account;
import com.ibm.fhir.model.resource.AdverseEvent;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Appointment;
import com.ibm.fhir.model.resource.AppointmentResponse;
import com.ibm.fhir.model.resource.AuditEvent;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Search;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.CareTeam;
import com.ibm.fhir.model.resource.Claim;
import com.ibm.fhir.model.resource.ClaimResponse;
import com.ibm.fhir.model.resource.ClinicalImpression;
import com.ibm.fhir.model.resource.Communication;
import com.ibm.fhir.model.resource.CommunicationRequest;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Consent;
import com.ibm.fhir.model.resource.Coverage;
import com.ibm.fhir.model.resource.CoverageEligibilityRequest;
import com.ibm.fhir.model.resource.CoverageEligibilityResponse;
import com.ibm.fhir.model.resource.DetectedIssue;
import com.ibm.fhir.model.resource.DeviceRequest;
import com.ibm.fhir.model.resource.DiagnosticReport;
import com.ibm.fhir.model.resource.DocumentManifest;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.EpisodeOfCare;
import com.ibm.fhir.model.resource.Flag;
import com.ibm.fhir.model.resource.Goal;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.ImagingStudy;
import com.ibm.fhir.model.resource.Immunization;
import com.ibm.fhir.model.resource.Invoice;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.MeasureReport;
import com.ibm.fhir.model.resource.Media;
import com.ibm.fhir.model.resource.Medication;
import com.ibm.fhir.model.resource.MedicationAdministration;
import com.ibm.fhir.model.resource.MedicationDispense;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.MedicationStatement;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Person;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.resource.QuestionnaireResponse;
import com.ibm.fhir.model.resource.RequestGroup;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.RiskAssessment;
import com.ibm.fhir.model.resource.Schedule;
import com.ibm.fhir.model.resource.ServiceRequest;
import com.ibm.fhir.model.resource.Specimen;
import com.ibm.fhir.model.resource.SupplyDelivery;
import com.ibm.fhir.model.resource.SupplyRequest;
import com.ibm.fhir.model.resource.VisionPrescription;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;


/**
 * This class implements the <a href="https://www.hl7.org/fhir/operation-patient-everything.html">$everything</a> operation
 * which is used to return all the information related to one or more patients described in the resource or context on
 * which this operation is invoked.
 */
public class EverythingOperation extends AbstractOperation {

    private static final Logger LOG = java.util.logging.Logger.getLogger(EverythingOperation.class.getName());

    /**
     * The <a href="https://www.hl7.org/fhir/search.html#prefix">prefix</a> used to indicate the start date for the $everything resources
     */
    protected static final String STARTING_FROM = SearchConstants.Prefix.GE.value();

    /**
     * The <a href="https://www.hl7.org/fhir/search.html#prefix">prefix</a> used to indicate the end date for the $everything resources
     */
    protected static final String UP_UNTIL = SearchConstants.Prefix.LE.value();

    /**
     * The "date" query parameter used in the underlying search operation.
     */
    protected static final String DATE_QUERY_PARAMETER = "date";

    /**
     * The "_lastUpdated" query parameter used in the underlying search operation.
     */
    protected static final String LAST_UPDATED_QUERY_PARAMETER = "_lastUpdated";

    /**
     * The query parameter to indicate a start date for the $everything operation
     */
    protected static final String START_QUERY_PARAMETER = "start";

    /**
     * The query parameter to indicate a stop date for the $everything operation
     */
    protected static final String END_QUERY_PARAMETER = "end";

    /**
     * The query parameter to only return resources last update since a date for the $everything operation
     */
    protected static final String SINCE_QUERY_PARAMETER = "_since";

    /**
     * The patient resource name
     */
    private static final String PATIENT = Patient.class.getSimpleName();

    /**
     * The maximum number of cumulative resources from all compartments for a given patient.
     */
    private static final int MAX_OVERALL_RESOURCES = 10000;

    /**
     * The list of resources for which the <code>date</code> query parameter can be used
     */
    private static final Set<String> SUPPORT_CLINICAL_DATE_QUERY = new HashSet<>(Arrays.asList(
        "AllergyIntolerance",
        "CarePlan",
        "CareTeam",
        "ClinicalImpression",
        "Composition",
        "Consent",
        "DiagnosticReport",
        "Encounter",
        "EpisodeOfCare",
        "FamilyMemberHistory",
        "Flag",
        "Immunization",
        "List",
        "Observation",
        "Procedure",
        "RiskAssessment",
        "SupplyRequest"));

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Patient-everything",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        LOG.entering(this.getClass().getName(), "doInvoke");

        /* Per the specification, If there is no nominated patient (GET /Patient/$everything) and the context is not associated with a single patient record,
         * the actual list of patients is all patients that the user associated with the request has access to. This may be all patients
         * in the family that the patient has access to, or it may be all patients that a care provider has access to, or all patients
         * on the entire record system. In such cases, the server may choose to return an error rather than all the records.
         *
         * @implNote we do not currently support it. However, if we do, we can use Patient?link=Patient/<SmartLaunch Context Patient ID>
         * @see Issue #2402 for more details.
         */
        if (logicalId == null) {
            throw buildExceptionWithIssue("This implementation requires the Patient's logical id to be passed; "
                    + "the patient / set of patients cannot be inferred from the request context.", IssueType.NOT_SUPPORTED);
        }

        Patient patient = null;
        try {
            patient = (Patient) resourceHelper.doRead(PATIENT, logicalId, false, false, null).getResource();
        } catch (FHIRPersistenceResourceDeletedException fde) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Patient with ID '" + logicalId + "' "
                    + "does not exist.", IssueType.NOT_FOUND);
            throw exceptionWithIssue;
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while "
                    + "reading patient '" + logicalId + "'", IssueType.EXCEPTION, e);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        if (patient == null) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Patient with ID '" + logicalId + "' "
                    + "does not exist.", IssueType.NOT_FOUND);
            throw exceptionWithIssue;
        }

        Entry patientEntry = buildPatientEntry(operationContext, patient);
        int maxPageSize = Math.max(1, FHIRConfigHelper.getIntProperty("fhirServer/core/maxPageSize", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX));
        List<Entry> allEntries = new ArrayList<>(maxPageSize);
        allEntries.add(patientEntry);

        // We can't always use the "date" query parameter to query by clinical date, only with some resources.
        // Initial list obtained from the github issue: https://github.com/IBM/FHIR/issues/1044#issuecomment-769788097
        // Otherwise the search throws an exception. We create a params map with and without and use as needed
        MultivaluedMap<String, String> queryParameters = parseQueryParameters(parameters, maxPageSize);
        MultivaluedMap<String, String> queryParametersWithoutDates = new MultivaluedHashMap<String,String>(queryParameters);
        boolean startOrEndProvided = queryParametersWithoutDates.remove(DATE_QUERY_PARAMETER) != null;

        List<String> defaultResourceTypes = new ArrayList<String>(0);
        try {
            defaultResourceTypes = getDefaultIncludedResourceTypes(resourceHelper);
        } catch (FHIRSearchException e) {
            throw new Error("There has been an error retrieving the list of included resources of the $everything operation.", e);
        }
        
        List<String> resourceTypesOverride = getOverridenIncludedResourceTypes(parameters, defaultResourceTypes);
        List<String> resourceTypes = resourceTypesOverride.isEmpty() ? defaultResourceTypes : resourceTypesOverride;

        int totalResourceCount = 0;
        for (String compartmentType : resourceTypes) {
            MultivaluedMap<String, String> searchParameters = queryParameters;
            if (startOrEndProvided  && !SUPPORT_CLINICAL_DATE_QUERY.contains(compartmentType)) {
                LOG.finest("The request specified a '" + START_QUERY_PARAMETER + "' and/or '" + END_QUERY_PARAMETER + "' query parameter. They are not valid for resource type '" + compartmentType + "', so will be ignored.");
                searchParameters = queryParametersWithoutDates;
            }
            
            // Need to get the value of the _type parameter out, if it exists
            List<String> typeParameters = new ArrayList<String>(0);
            List<Parameters.Parameter> parameterList = parameters.getParameter();
            for (Parameters.Parameter parameter: parameterList) {
                typeParameters = new ArrayList<String>(parameterList.size());
                if (parameter.getName().getValue().equals("_type")) {
                    String[] types = parameter.getValue().as(ModelSupport.FHIR_STRING).getValue().split(",");
                    Collections.addAll(typeParameters, types);
                    break;
                }
            }
            
            addIncludesSearchParameters(compartmentType, typeParameters, searchParameters);
            
            Bundle results = null;
            int currentResourceCount = 0;
            try {
                results = resourceHelper.doSearch(compartmentType, PATIENT, logicalId, searchParameters, null, null);
                currentResourceCount = results.getTotal().getValue();
                totalResourceCount += currentResourceCount;
                LOG.finest("Got " + compartmentType + " resources " + currentResourceCount + " for a total of " + totalResourceCount);
            } catch (Exception e) {
                FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Error retrieving $everything "
                        + "resources of type '" + compartmentType + "' for patient " + logicalId, IssueType.EXCEPTION, e);
                LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                throw exceptionWithIssue;
            }
            // If retrieving all these resources exceeds the maximum number of resources allowed for this operation the operation is failed
            if (totalResourceCount > MAX_OVERALL_RESOURCES) {
                FHIROperationException exceptionWithIssue = buildExceptionWithIssue("The maximum number of resources "
                        + "allowed for the $everything operation (" + MAX_OVERALL_RESOURCES + ") has been exceeded "
                        + "for patient '" + logicalId + "'. Try using the bulkexport feature.", IssueType.TOO_COSTLY);
                LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                throw exceptionWithIssue;
            }
            allEntries.addAll(results.getEntry());

            // We are retrieving sub-resources MAX_PAGE_SIZE items at a time, but there could be more so we need to retrieve the rest of the pages for the last resource if needed
            if (currentResourceCount > maxPageSize) {
                // We already retrieved page 1 so we account for that and start retrieving the rest of the pages
                int page = 2;
                while ((currentResourceCount -= maxPageSize) > 0) {
                    LOG.finest("Retrieving page " + page + " of the " + compartmentType + " resources for patient " + logicalId);
                    try {
                        searchParameters.putSingle(SearchConstants.PAGE, page++ + "");
                        results = resourceHelper.doSearch(compartmentType, PATIENT, logicalId, searchParameters, null, null);
                    } catch (Exception e) {
                        FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Error retrieving "
                                + "$everything resources page '" + page + "' of type '" + compartmentType + "' "
                                + "for patient " + logicalId, IssueType.EXCEPTION, e);
                        LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                        throw exceptionWithIssue;
                    }
                    allEntries.addAll(results.getEntry());
                }
            }
        }

        Bundle.Builder bundleBuilder = Bundle.builder()
                .type(BundleType.SEARCHSET)
                .id(UUID.randomUUID().toString())
                .entry(allEntries)
                .total(UnsignedInt.of(allEntries.size()));

        Parameters outputParameters;
        try {
            outputParameters = FHIROperationUtil.getOutputParameters(bundleBuilder.build());
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while "
                    + "creating the operation output parameters for the resulting Bundle.", IssueType.EXCEPTION, e);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        LOG.exiting(this.getClass().getName(), "doInvoke", outputParameters);
        return outputParameters;
    }

    /**
     * Parse the parameters and turn them into a {@link MultivaluedMap} to pass to the search service
     *
     * @param parameters the operation parameters
     * @param maxPageSize the max page size
     * @return the {@link MultivaluedMap} for the search service built from the parameters
     */
    protected MultivaluedMap<String, String> parseQueryParameters(Parameters parameters, int maxPageSize) {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        Parameter countParameter = getParameter(parameters, SearchConstants.COUNT);
        if (countParameter != null) {
            LOG.fine("The `count` parameter is currently not supported by the $everything operation, it will be ignored.");
        }
        // We will try to grab all resources of a given type, in order to reduce the number of pages we will get the max page size
        queryParameters.add(SearchConstants.COUNT, maxPageSize + "");
        // We use gt/lt here in an effort to be more liberal in terms of what we return
        // https://ibm-watsonhealth.slack.com/archives/C14JTTR6C/p1614181836050500?thread_ts=1614180853.047300&cid=C14JTTR6C
        Parameter startParameter = getParameter(parameters, START_QUERY_PARAMETER);
        if (startParameter != null) {
            queryParameters.add(DATE_QUERY_PARAMETER, STARTING_FROM + startParameter.getValue().as(FHIR_DATE).getValue());
        }
        Parameter endParameter = getParameter(parameters, END_QUERY_PARAMETER);
        if (endParameter != null) {
            queryParameters.add(DATE_QUERY_PARAMETER, UP_UNTIL + endParameter.getValue().as(FHIR_DATE).getValue());
        }
        Parameter sinceParameter = getParameter(parameters, SINCE_QUERY_PARAMETER);
        if (sinceParameter != null) {
            queryParameters.add(LAST_UPDATED_QUERY_PARAMETER, STARTING_FROM + sinceParameter.getValue().as(FHIR_INSTANT).getValue());
        }
        return queryParameters;
    }

    /**
     * @param parameters the {@link Parameters} object
     * @return the list of patient subresources that will be included in the $everything operation, as provided by the user
     * @throws FHIRSearchException
     */
    protected List<String> getOverridenIncludedResourceTypes(Parameters parameters, List<String> defaultResourceTypes) throws FHIRSearchException {
        List<String> typeOverrides = new ArrayList<>();
        Parameter typesParameter = getParameter(parameters, SearchConstants.RESOURCE_TYPE);
        if (typesParameter == null) {
            return typeOverrides;
        }
        String typeOverridesParam = typesParameter.getValue().as(FHIR_STRING).getValue();
        String[] typeOverridesList = typeOverridesParam.split(SearchConstants.JOIN_STR);
        List<String> unknownTypes= new ArrayList<>();
        for (String typeOverride : typeOverridesList) {
            if (!defaultResourceTypes.contains(typeOverride)) {
                unknownTypes.add(typeOverride);
            } else {
                typeOverrides.add(typeOverride.trim());
            }
        }
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        if (!unknownTypes.isEmpty()) {
            String msg = "The following resource types are not supported by this operation: " + String.join(",", unknownTypes);
            if (HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference())) {
                LOG.fine(msg);
            } else {
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }
        }
        return typeOverrides;
    }

    /**
     * @return the list of patient subresources that will be included in the $everything operation
     * @throws FHIRSearchException
     */
    private List<String> getDefaultIncludedResourceTypes(FHIRResourceHelpers resourceHelper) throws FHIRSearchException {
        List<String> resourceTypes = new ArrayList<>(CompartmentUtil.getCompartmentResourceTypes(PATIENT));
        
        try {
            List<String> supportedResourceTypes = FHIRConfigHelper.getSupportedResourceTypes();
            // Examine the resource types to see if they support SEARCH
            for (String resourceType: supportedResourceTypes) {
                try {
                    resourceHelper.validateInteraction(FHIRResourceHelpers.Interaction.SEARCH, resourceType);
                } catch (FHIROperationException e) {
                    if (LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Removing resourceType " + resourceType + " because it does not support SEARCH");
                    }
                    supportedResourceTypes.remove(resourceType);
                }
            }
            if (LOG.isLoggable(Level.FINE)) {
                StringBuilder resourceTypeBuilder = new StringBuilder(supportedResourceTypes.size());
                resourceTypeBuilder.append("supportedResourceTypes are: ");
                for (String resourceType: supportedResourceTypes) {
                   resourceTypeBuilder.append(resourceType);
                   resourceTypeBuilder.append(" ");
                }
                LOG.fine(resourceTypeBuilder.toString());
            }
            
            // Need to have this if check to support server config files that do not specify resources
            if (!supportedResourceTypes.isEmpty()) {
                resourceTypes.retainAll(supportedResourceTypes);
            }
        } catch (Exception e) {
            FHIRSearchException exceptionWithIssue = new FHIRSearchException("There has been an error retrieving the list of supported resource types of the $everything operation.", e);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        
        if (LOG.isLoggable(Level.FINE)) {
            StringBuilder resourceTypeBuilder = new StringBuilder(resourceTypes.size());
            resourceTypeBuilder.append("resourceTypes are: ");
            for (String resourceType: resourceTypes) {
                resourceTypeBuilder.append(resourceType);
                resourceTypeBuilder.append(" ");
            }
            LOG.fine(resourceTypeBuilder.toString());
        }
        return resourceTypes;
    }

    /**
     * Builds an {@link Entry} out of the given {@link Patient} resource including its fullURL
     *
     * @param operationContext the {@link FHIROperationContext} to get the base URI
     * @param patient the patient to wrap
     * @return the entry with URL
     */
    private Entry buildPatientEntry(FHIROperationContext operationContext, Patient patient) {
        Uri patientURL = uri(operationContext, PATIENT + "/" + patient.getId());
        Entry patientEntry = Entry.builder()
                .resource(patient)
                .fullUrl(patientURL)
                .search(Search.builder()
                    .mode(SearchEntryMode.MATCH)
                    .score(Decimal.of("1"))
                    .build())
                .build();
        return patientEntry;
    }

    /**
     * Builds a URI with the base URI from the given {@link FHIROperationContext} and then provided URI path.
     *
     * @param operationContext the {@link FHIROperationContext} to get the base URI
     * @param uriPath the path to append to the base URI
     * @return the {@link Uri}
     */
    private static Uri uri(FHIROperationContext operationContext, String uriPath) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        return Uri.builder()
                .value(requestBaseURI + "/" + uriPath)
                .build();
    }
    
    private void addIncludesSearchParameters(String compartmentType, List<String> typeParameters, MultivaluedMap<String, String> searchParameters) {
        // Add in Location, Medication, Organization, and Practitioner resources which are pointed to
        // from search parameters only if the request does not have a _type parameter or it does have a 
        // _type parameter that includes these

        // Remove any _includes we added last time around
        searchParameters.remove("_include");
        
        // Add in _includes for all search parameters that are Location, Medication, Organization, or Practitioner
        if (compartmentType.equals(AdverseEvent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "recorder", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "substance", Medication.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(AllergyIntolerance.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "recorder", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "asserter", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Appointment.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "practitioner", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(AppointmentResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "actor", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "actor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "practitioner", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Account.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "owner", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(AuditEvent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "agent", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "agent", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "source", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "source", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Basic.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(CarePlan.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters); 
        } else if (compartmentType.equals(CareTeam.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "participant", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "participant", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals("ChargeHistory")) {
            addSearchParameterIfNotExcluded(compartmentType, "service", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "enterer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer-actor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Claim.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "care-team", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "care-team", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "enterer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "facility", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "insurer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "payee", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "payee", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "provider", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "provider", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(ClaimResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "insurer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requestor", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requestor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(ClinicalImpression.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "assessor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Communication.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "sender", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Composition.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "attester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Condition.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "asserter", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(CommunicationRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "sender", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "sender", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Consent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "actor", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "actor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "consentor", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "consentor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "organization", Organization.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Coverage.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "payor", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "policy-holder", Organization.class.getSimpleName(), typeParameters, searchParameters); 
        } else if (compartmentType.equals(CoverageEligibilityRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "enterer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "facility", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "provider", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "provider", Organization.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(CoverageEligibilityResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "insurer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requestor", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requestor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(DetectedIssue.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(DeviceRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(DiagnosticReport.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "results-interpreter", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "results-interpreter", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(DocumentManifest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Encounter.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "service-provider", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "participant", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(EpisodeOfCare.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "care-manager", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "organization", Organization.class.getSimpleName(), typeParameters, searchParameters); 
        } else if (compartmentType.equals(Flag.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Goal.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "subject", Organization.class.getSimpleName(), typeParameters, searchParameters); 
        } else if (compartmentType.equals(Group.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "member", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "member", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "managing-entity", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "managing-entity", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(ImagingStudy.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "interpreter", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "referrer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Immunization.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "manufacturer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Invoice.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "issuer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "participant", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "participant", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "recipient", Organization.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(List.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "source", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(MeasureReport.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "reporter", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "reporter", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "reporter", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Media.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "operator", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "operator", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(MedicationAdministration.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "medication", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(MedicationDispense.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "destination", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "medication", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "receiver", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "responsibleparty", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(MedicationRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "intended-dispenser", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "intended-performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "intended-performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "medication", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(MedicationStatement.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "medication", Medication.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "source", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "source", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals("NutritionHistory")) {
            addSearchParameterIfNotExcluded(compartmentType, "provider", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Observation.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(QuestionnaireResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "source", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Patient.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "general-practitioner", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "general-practitioner", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "organization", Organization.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Person.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "organization", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "practitioner", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Procedure.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Provenance.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "location", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "agent", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "agent", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Reference.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "authenticator", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "authenticator", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "custodian", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(RequestGroup.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "author", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "participant", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(RiskAssessment.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(Schedule.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "actor", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "actor", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(ServiceRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "performer", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "performer", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters); 
        } else if (compartmentType.equals(Specimen.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "collector", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(SupplyDelivery.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "receiver", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "supplier", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "supplier", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(SupplyRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "requester", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "requester", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Location.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "subject", Organization.class.getSimpleName(), typeParameters, searchParameters);
            addSearchParameterIfNotExcluded(compartmentType, "supplier", Organization.class.getSimpleName(), typeParameters, searchParameters);
        } else if (compartmentType.equals(VisionPrescription.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentType, "prescriber", Practitioner.class.getSimpleName(), typeParameters, searchParameters);
        } 
            
        // BodyStructure, ResearchSubject, and RelatedPerson have no references to:
        // Location, Medication, Organization, and Practitioner
        
        // DeviceUseStatement, EnrollmentRequest, FamilyMemberHistory, ChargeItem, 
        // ImmunizationEvaluation, ImmunizationRecommendation, MolecularSequence, 
        // and NutritionOrder have no search parameters that reference: 
        // Location, Medication, Organization, and Practitioner
    }
    
    private void addSearchParameterIfNotExcluded(String compartmentType, String nameOfSearchParameter, String subResourceType, List<String> typeParameters, MultivaluedMap<String, String> searchParameters)
    {
        // Need to make sure the subtype has not been excluded
        if (typeParameters.isEmpty() || typeParameters.contains(subResourceType)) {
            searchParameters.add("_include", compartmentType + ":" + nameOfSearchParameter + ":" + subResourceType);
        } else {
            LOG.fine("Filtering out subResourceType= " + subResourceType + "for compartmentType= " + compartmentType);
        }
    }
}
