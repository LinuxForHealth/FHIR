/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.util;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.util.ModelSupport.FHIR_STRING;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import org.owasp.encoder.Encode;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.operation.bulkdata.OperationConstants;
import org.linuxforhealth.fhir.operation.bulkdata.OperationConstants.ExportType;
import org.linuxforhealth.fhir.operation.bulkdata.model.PollingLocationResponse;
import org.linuxforhealth.fhir.operation.bulkdata.model.transformer.JobIdEncodingTransformer;
import org.linuxforhealth.fhir.search.compartment.CompartmentHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;

/**
 * BulkData Util captures common methods
 */
public class BulkDataExportUtil {
    private static final Logger LOG = Logger.getLogger(BulkDataExportUtil.class.getName());

    private final CompartmentHelper compartmentHelper;

    public BulkDataExportUtil() {
        compartmentHelper = new CompartmentHelper();
    }

    /**
     * Check the Export Type is valid and converts to intermediate enum
     *
     * @param type
     * @param resourceType
     * @return
     */
    public OperationConstants.ExportType checkExportType(FHIROperationContext.Type type,
        Class<? extends Resource> resourceType) {
        ExportType exportType = ExportType.INVALID;
        if (FHIROperationContext.Type.INSTANCE.equals(type) && "Group".equals(resourceType.getSimpleName())) {
            exportType = ExportType.GROUP;
        } else if (FHIROperationContext.Type.RESOURCE_TYPE.equals(type)
                && "Patient".equals(resourceType.getSimpleName())) {
            exportType = ExportType.PATIENT;
        } else if (FHIROperationContext.Type.SYSTEM.equals(type)) {
            exportType = ExportType.SYSTEM;
        }
        return exportType;
    }

    public MediaType checkAndConvertToMediaType(Parameters parameters) throws FHIROperationException {
        /*
         * The format for the requested bulk data files to be generated as per [FHIR Asynchronous Request
         * Pattern](http://hl7.org/fhir/async.html). Defaults to application/fhir+ndjson. Servers SHALL support [Newline
         * Delimited JSON](http://ndjson.org), but MAY choose to support additional output formats. Servers SHALL accept
         * the full content type of application/fhir+ndjson as well as the abbreviated representations
         * application/ndjson and ndjson.
         */
        Optional<Parameter> parameter = parameters.getParameter().stream()
                .filter(p -> OperationConstants.PARAM_OUTPUT_FORMAT.equals(p.getName().getValue()))
                .findFirst();

        String mediaType = FHIRMediaType.APPLICATION_NDJSON;
        if (parameter.isPresent() && parameter.get().getValue().is(org.linuxforhealth.fhir.model.type.String.class)) {
            mediaType = retrieveOutputFormat(parameter.get().getValue().as(org.linuxforhealth.fhir.model.type.String.class).getValue());
        }

        return MediaType.valueOf(mediaType);
    }

    private String retrieveOutputFormat(String requestedFormat) throws FHIROperationException {
        // If the parameter isn't passed, use application/fhir+ndjson
        String finalValue = FHIRMediaType.APPLICATION_NDJSON;

        if (requestedFormat != null) {
            // Normalize the NDJSON variants to MEDIA_TYPE_ND_JSON
            if (OperationConstants.NDJSON_VARIANTS.contains(requestedFormat)) {
                requestedFormat = FHIRMediaType.APPLICATION_NDJSON;
            }

            // We're checking that it's acceptable.
            if (!OperationConstants.EXPORT_FORMATS.contains(requestedFormat)) {
                // Workaround for Liberty/CXF replacing "+" with " "
                requestedFormat = requestedFormat.replaceAll(" ", "+");
            }

            if (OperationConstants.EXPORT_FORMATS.contains(requestedFormat)) {
                finalValue = requestedFormat;
            } else {
                throw buildOperationException("Invalid requested format must be one of '" + OperationConstants.EXPORT_FORMATS + "'", IssueType.INVALID);
            }
        }
        return finalValue;
    }

    public FHIROperationException buildOperationException(String errMsg, IssueType issueType) {
        return buildOperationException(errMsg, issueType, null);
    }

    public FHIROperationException buildOperationException(String errMsg, IssueType issueType, Exception e) {
        return new FHIROperationException(errMsg, e)
                .withIssue(Issue.builder()
                    .code(issueType)
                    .diagnostics(string(errMsg))
                    .severity(IssueSeverity.ERROR)
                    .build());
    }

    /**
     * @param parameters
     * @return
     * @throws FHIROperationException
     */
    public Instant checkAndExtractSince(Parameters parameters) {
        /*
         * Resources will be included in the response if their state has changed after the supplied time (e.g. if
         * Resource.meta.lastUpdated is later than the supplied _since time).
         */
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                // Parameter name is non-null (required per spec).
                if (OperationConstants.PARAM_SINCE.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() != null) {
                        if (parameter.getValue().is(org.linuxforhealth.fhir.model.type.String.class)) {
                            return Instant.of(parameter.getValue().as(org.linuxforhealth.fhir.model.type.String.class).getValue());
                        } else if (parameter.getValue().is(org.linuxforhealth.fhir.model.type.Instant.class)) {
                            return Instant.of(parameter.getValue().as(org.linuxforhealth.fhir.model.type.Instant.class).getValue());
                        }
                    }
                    // No matching type found
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * processes both the Parameters object and the query parameters
     *
     * @param exportType
     * @param fhirVersion
     * @param parameters
     * @return
     * @throws FHIROperationException
     */
    public Set<String> checkAndValidateTypes(OperationConstants.ExportType exportType, FHIRVersionParam fhirVersion, List<Parameter> parameters)
            throws FHIROperationException {
        /*
         * Only resources of the specified resource types(s) SHALL be included in the response. If this parameter is
         * omitted, the server SHALL return all supported resources within the scope of the client authorization. For
         * Patient- and Group-level requests, the Patient Compartment SHOULD be used as a point of reference for
         * recommended resources to be returned. However, other resources outside of the patient compartment that are
         * helpful in interpreting the patient data (such as Organization and Practitioner) may also be returned.
         * Servers unable to support _type SHOULD return an error and OperationOutcome resource so clients can re-submit
         * a request omitting the _type parameter. Resource references MAY be relative URLs with the format <resource
         * type>/<id>, or absolute URLs with the same structure rooted in the base URL for the server from which the
         * export was performed. References will be resolved by looking for a resource with the specified type and id
         * within the file set. For example _type=Practitioner could be used to bulk data extract all Practitioner
         * resources from a FHIR endpoint.
         */
        Set<String> supportedResourceTypes = FHIRConfigHelper.getSupportedResourceTypes(fhirVersion);
        Set<String> result = new HashSet<>();
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters) {
                // The model makes sure getName is never non-null.
                if (OperationConstants.PARAM_TYPE.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() == null) {
                        throw buildOperationException("invalid resource type sent as a parameter to $export operation", IssueType.INVALID);
                    }

                    String types = parameter.getValue().as(FHIR_STRING).getValue();
                    for (String type : types.split(",")) {
                        // Type will never be null here.
                        if (!type.isEmpty() && supportedResourceTypes.contains(type)) {
                            result.add(type);
                        } else {
                            throw buildOperationException("invalid resource type sent as a parameter to $export operation: "
                                    + Encode.forHtml(type), IssueType.INVALID);
                        }
                    }
                }
            }
        }

        // The case where no resourceTypes are specified on a system export, inlining only the supported ResourceTypes
        if (result.isEmpty() && ExportType.SYSTEM.equals(exportType)) {
            result = new HashSet<>(supportedResourceTypes);
        } else if (ExportType.PATIENT.equals(exportType) || ExportType.GROUP.equals(exportType)) {
            if (!result.isEmpty()) {
                result = filterTypesToPatientResourceTypes(result);
            } else {
                result = getDefaultsForPatientCompartment(supportedResourceTypes);
            }
        }
        return result;
    }

    /**
     * Filter the passed resourceTypes down to the set that can be in the Patient compartment.
     *
     * @param resourceTypes
     * @return a new Set that represents the subset of the requested types that can be exported in a Patient export
     * @throws FHIROperationException if none of the passed resourceTypes are valid for a Patient export
     * @implNote originally this was in the PartitionMapper for Patient Export.
     */
    private Set<String> filterTypesToPatientResourceTypes(Set<String> resourceTypes) throws FHIROperationException {
        Set<String> result = new HashSet<>();

        // Filter the set of types down to those that exist in the Patient compartment.
        Set<String> patientCompartmentResourceTypes = new HashSet<>(compartmentHelper.getCompartmentResourceTypes("Patient"));
        for (String resourceType : resourceTypes) {
            if (patientCompartmentResourceTypes.contains(resourceType)) {
                result.add(resourceType);
            } else {
                LOG.info("Requested type '" + Encode.forHtml(resourceType) + "' cannot be in the Patient compartment;"
                        + " this is not supported for Patient/Group export and so this type will be skipped");
            }
        }

        if (result.isEmpty()) {
            throw buildOperationException("None of the requested types are valid for a Patient (or Group) export", IssueType.INVALID);
        }
        return result;
    }

    /**
     * Get the set of default resource types for a Patient export (filtered by what is supported for the tenant in the FHIRRequestContext).
     *
     * @param supportedResourceTypes
     * @return
     * @throws FHIROperationException
     */
    private Set<String> getDefaultsForPatientCompartment(Set<String> supportedResourceTypes) throws FHIROperationException {
        // Ensures only supported resources are added
        Set<String> supportedDefaultResourceTypes = new HashSet<>();
        for (String compartmentResourceType : compartmentHelper.getCompartmentResourceTypes("Patient")) {
            if (supportedResourceTypes.contains(compartmentResourceType)) {
                supportedDefaultResourceTypes.add(compartmentResourceType);
            }
        }
        return supportedDefaultResourceTypes;
    }

    public List<String> checkAndValidateTypeFilters(Parameters parameters) throws FHIROperationException {
        /*
         * To request finer-grained filtering, a client MAY supply a _typeFilter parameter alongside the _type
         * parameter. The value of the _typeFilter parameter is a comma-separated list of FHIR REST API queries that
         * further restrict the results of the query. Servers MAY further limit the data returned to a specific client
         * in accordance with local considerations (e.g. policies or regulations). Understanding _typeFilter is OPTIONAL
         * for FHIR servers; clients SHOULD be robust to servers that ignore _typeFilter. Note for client developers:
         * Because both _typeFilter and _since can restrict the results returned, the interaction of these parameters
         * may be surprising. Think carefully through the implications when constructing a query with both of these
         * parameters. As the _typeFilter is experimental and optional, we have not yet determined expectation for
         * _include, _revinclude, or support for any specific search parameters.
         */
        List<String> result = new ArrayList<>();
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (OperationConstants.PARAM_TYPE_FILTER.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() != null && parameter.getValue().is(FHIR_STRING)) {
                        String typeFilters = parameter.getValue().as(FHIR_STRING).getValue();

                        for (String typeFilter : typeFilters.split(",")) {
                            // Type will never be null here, just check for blanks
                            if (!typeFilter.isEmpty()) {
                                result.add(typeFilter);
                            } else {
                                throw buildOperationException("invalid typeFilter sent as a parameter to $export operation", IssueType.INVALID);
                            }
                        }
                        return result;
                    }
                    // Result must have NOT been returned.
                    throw buildOperationException("invalid typeFilter parameter type sent to $export operation", IssueType.INVALID);
                }
            }
        }
        return result;
    }

    public Parameters getOutputParametersWithJson(PollingLocationResponse resource) throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder.parameter(Parameter.builder().name(string("return")).value(string(PollingLocationResponse.Writer.generate(resource))).build());
        return parametersBuilder.build();
    }

    /**
     * checks and validates the job.
     *
     * @param parameters
     * @return
     * @throws FHIROperationException
     */
    public String checkAndValidateJob(Parameters parameters) throws FHIROperationException {
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (OperationConstants.PARAM_JOB.equals(parameter.getName().getValue())
                        && parameter.getValue() != null && parameter.getValue().is(org.linuxforhealth.fhir.model.type.String.class)) {
                    String job = JobIdEncodingTransformer.getInstance().decodeJobId(parameter.getValue().as(org.linuxforhealth.fhir.model.type.String.class).getValue());

                    // The job is never going to be empty or null as STRING is never empty at this point.
                    if (job.contains("/") || job.contains("?")) {
                        throw new FHIROperationException("job passed is invalid and is not supported");
                    }
                    // Don't look at any other parameters.
                    return job;
                }
            }
        }

        throw new FHIROperationException("no job identifier is passed");
    }
}
