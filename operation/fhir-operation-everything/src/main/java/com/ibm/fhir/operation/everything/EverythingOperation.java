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
            
            Bundle results = null;
            int currentResourceCount = 0;
            try {
                addIncludesSearchParameters(compartmentType, searchParameters);
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
    
    private void addIncludesSearchParameters(String compartmentMemberType, MultivaluedMap<String, String> searchParameters)
    throws Exception {
        // Add in Location, Medication, Organization, and Practitioner resources which are pointed to
        // from search parameters only if the request does not have a _type parameter or it does have a 
        // _type parameter that includes these

        // Remove any _includes we added last time around
        searchParameters.remove("_include");
        
        // Add in _includes for all search parameters that are Location, Medication, Organization, or Practitioner
        if (compartmentMemberType.equals(AdverseEvent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "recorder", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "substance", Medication.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(AllergyIntolerance.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "recorder", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "asserter", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Appointment.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "practitioner", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(AppointmentResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "practitioner", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Account.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "owner", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(AuditEvent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "agent", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "agent", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Basic.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(CarePlan.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters); 
        } else if (compartmentMemberType.equals(CareTeam.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Organization.class.getSimpleName(),searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals("ChargeHistory")) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "service", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "enterer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer-actor", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Claim.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "care-team", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "care-team", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "enterer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "facility", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "insurer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "payee", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "payee", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "provider", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "provider", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(ClaimResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "insurer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requestor", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requestor", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(ClinicalImpression.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "assessor", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Communication.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "sender", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Composition.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "attester", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Condition.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "asserter", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(CommunicationRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "sender", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "sender", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Consent.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "consentor", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "consentor", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "organization", Organization.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Coverage.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "payor", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "policy-holder", Organization.class.getSimpleName(), searchParameters); 
        } else if (compartmentMemberType.equals(CoverageEligibilityRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "enterer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "facility", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "provider", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "provider", Organization.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(CoverageEligibilityResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "insurer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requestor", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requestor", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(DetectedIssue.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(DeviceRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(DiagnosticReport.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "results-interpreter", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "results-interpreter", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(DocumentManifest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Encounter.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "service-provider", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(EpisodeOfCare.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "care-manager", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "organization", Organization.class.getSimpleName(), searchParameters); 
        } else if (compartmentMemberType.equals(Flag.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Goal.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Organization.class.getSimpleName(), searchParameters); 
        } else if (compartmentMemberType.equals(Group.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "member", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "member", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "managing-entity", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "managing-entity", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(ImagingStudy.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "interpreter", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "referrer", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Immunization.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "manufacturer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Invoice.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "issuer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "recipient", Organization.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(List.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(MeasureReport.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "reporter", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "reporter", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "reporter", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Media.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "operator", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "operator", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(MedicationAdministration.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "medication", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(MedicationDispense.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "destination", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "medication", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "receiver", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "responsibleparty", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(MedicationRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "intended-dispenser", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "intended-performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "intended-performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "medication", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(MedicationStatement.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "medication", Medication.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals("NutritionHistory")) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "provider", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Observation.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(QuestionnaireResponse.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "source", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Patient.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "general-practitioner", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "general-practitioner", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "organization", Organization.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Person.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "organization", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "practitioner", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Procedure.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Provenance.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "location", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "agent", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "agent", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Reference.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "authenticator", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "authenticator", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "custodian", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(RequestGroup.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "author", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "participant", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(RiskAssessment.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(Schedule.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "actor", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(ServiceRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "performer", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters); 
        } else if (compartmentMemberType.equals(Specimen.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "collector", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(SupplyDelivery.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "receiver", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "supplier", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "supplier", Practitioner.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(SupplyRequest.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "requester", Practitioner.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Location.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "subject", Organization.class.getSimpleName(), searchParameters);
            addSearchParameterIfNotExcluded(compartmentMemberType, "supplier", Organization.class.getSimpleName(), searchParameters);
        } else if (compartmentMemberType.equals(VisionPrescription.class.getSimpleName())) {
            addSearchParameterIfNotExcluded(compartmentMemberType, "prescriber", Practitioner.class.getSimpleName(), searchParameters);
        } 
            
        // BodyStructure, ResearchSubject, and RelatedPerson have no references to:
        // Location, Medication, Organization, and Practitioner
        
        // DeviceUseStatement, EnrollmentRequest, FamilyMemberHistory, ChargeItem, 
        // ImmunizationEvaluation, ImmunizationRecommendation, MolecularSequence, 
        // and NutritionOrder have no search parameters that reference: 
        // Location, Medication, Organization, and Practitioner
    }
    
    private void addSearchParameterIfNotExcluded(String compartmentMemberType, String nameOfSearchParameter, 
        String subResourceType, MultivaluedMap<String, String> searchParameters) throws Exception
    {
        List<String> allowedIncludes = new ArrayList<String>(10);
        try {
            allowedIncludes = FHIRConfigHelper.getSearchPropertyRestrictions(compartmentMemberType, FHIRConfigHelper.SEARCH_PROPERTY_TYPE_INCLUDE);
        } catch (Exception e) {
            FHIRSearchException exceptionWithIssue = new FHIRSearchException("There has been an error retrieving the list of supported search parameters for the $everything operation.", e);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;      
        }
        // Need to make sure the search parameter has not been excluded
        String parameterName = compartmentMemberType + ":" + nameOfSearchParameter + ":" + subResourceType;
        if (allowedIncludes != null && !allowedIncludes.contains(parameterName)) {
            searchParameters.add("_include", parameterName);
        } else {
            LOG.fine("Filtering out searchParameter because it is not supported by the server config: " + parameterName);
        }
    }
}
