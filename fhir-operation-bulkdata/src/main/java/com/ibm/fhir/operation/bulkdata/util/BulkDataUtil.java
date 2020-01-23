/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.util;

import static com.ibm.fhir.model.type.String.string;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.context.FHIROperationContext;

/**
 * BulkData Util captures common methods
 */
public class BulkDataUtil {
    // Random generator for COS path prefix
    private static final SecureRandom RANDOM = new SecureRandom();

    private BulkDataUtil() {
        // No Operation
    }

    /**
     * Generate a random AES key or 32 byte value encoded as a Base64 string.
     *
     * @return
     */
    public static String getRandomKey(String key) {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance(key);
            keyGen.init(256);
            return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            byte[] buffer = new byte[32];
            RANDOM.setSeed(System.currentTimeMillis());
            RANDOM.nextBytes(buffer);
            return Base64.getEncoder().encodeToString(buffer);
        }
    }

    /**
     * coverts to one of the export types.
     * 
     * @param type
     * @param resourceType
     * @return
     */
    public static BulkDataConstants.ExportType checkExportType(FHIROperationContext.Type type,
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

    public static MediaType checkAndConvertToMediaType(FHIROperationContext operationContext)
            throws FHIROperationException {
        /*
         * The format for the requested bulk data files to be generated as per [FHIR Asynchronous Request
         * Pattern](http://hl7.org/fhir/async.html). Defaults to application/fhir+ndjson. Servers SHALL support [Newline
         * Delimited JSON](http://ndjson.org), but MAY choose to support additional output formats. Servers SHALL accept
         * the full content type of application/fhir+ndjson as well as the abbreviated representations
         * application/ndjson and ndjson.
         */
        UriInfo uriInfo = (UriInfo) operationContext.getProperty(FHIROperationContext.PROPNAME_URI_INFO);
        return MediaType.valueOf(retrieveOutputFormat(uriInfo));
    }

    private static String retrieveOutputFormat(javax.ws.rs.core.UriInfo uriInfo) throws FHIROperationException {
        // If the parameter isn't passed, use application/fhir+ndjson
        String value = "application/fhir+ndjson";

        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        List<String> qps = queryParameters.get("_outputFormat");

        if (qps != null) {
            if (qps.isEmpty() || qps.size() != 1) {
                throw buildOperationException(
                        "_outputFormat cardinality expectation for $apply operation parameter is 0..1 ");
            }

            String format = qps.get(0);
            // We're checking that it's acceptable.
            if (!BulkDataConstants.EXPORT_FORMATS.contains(format)) {

                // Workaround for Liberty/CXF replacing "+" with " "
                MultivaluedMap<String, String> notDecodedQueryParameters = uriInfo.getQueryParameters(false);
                List<String> notDecodedQps = notDecodedQueryParameters.get("_outputFormat");

                format = notDecodedQps.get(0);
                if (!BulkDataConstants.EXPORT_FORMATS.contains(format)) {
                    throw buildOperationException("Invalid requested format.");
                }
            }
        }
        return value;
    }

    public static FHIROperationException buildOperationException(String errMsg) {
        FHIROperationException operationException = new FHIROperationException(errMsg);

        List<Issue> issues = new ArrayList<>();
        Issue.Builder builder = Issue.builder();
        builder.code(IssueType.INVALID);
        builder.diagnostics(string(errMsg));
        builder.severity(IssueSeverity.ERROR);
        issues.add(builder.build());

        operationException.setIssues(issues);
        return operationException;
    }

    /**
     * @param parameters
     * @return
     * @throws FHIROperationException
     */
    public static Instant checkAndExtractSince(Parameters parameters) {
        /*
         * Resources will be included in the response if their state has changed after the supplied time (e.g. if
         * Resource.meta.lastUpdated is later than the supplied _since time).
         */
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                // Parameter name is non-null (required per spec).
                if (BulkDataConstants.PARAM_SINCE.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() != null) {
                        if (parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                            return Instant.of(parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue());
                        } else if (parameter.getValue().is(com.ibm.fhir.model.type.Instant.class)) {
                            return Instant
                                    .of(parameter.getValue().as(com.ibm.fhir.model.type.Instant.class).getValue());
                        }
                    }
                    // No matching type found
                    return null;
                }
            }
        }
        return null;
    }

    public static List<String> checkAndValidateTypes(Parameters parameters) throws FHIROperationException {
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
        List<String> result = new ArrayList<>();
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                // The model makes sure getName is never non-null. 
                if (BulkDataConstants.PARAM_TYPE.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() != null) {
                        String types =
                                parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                        for (String type : types.split(",")) {
                            // Type will never be null here. 
                            if (!type.isEmpty() && ModelSupport.isResourceType(type)) {
                                result.add(type);
                            } else {
                                throw buildOperationException(
                                        "invalid resource type sent as a parameter to $export operation");
                            }
                        }
                    } else {
                        throw buildOperationException(
                                "invalid resource type sent as a parameter to $export operation");
                    }
                }
            }
        }
        return result;
    }

    public static List<String> checkAndValidateTypeFilters(Parameters parameters) throws FHIROperationException {
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
                if (BulkDataConstants.PARAM_TYPE_FILTER.equals(parameter.getName().getValue())) {
                    if (parameter.getValue() != null && parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                        String typeFilters =
                                parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();

                        for (String typeFilter : typeFilters.split(",")) {
                            // Type will never be null here, just check for blanks 
                            if (!typeFilter.isEmpty()) {
                                result.add(typeFilter);
                            } else {
                                throw buildOperationException(
                                        "invalid typeFilter sent as a parameter to $export operation");
                            }
                        }
                        return result;
                    }
                    // Result must have NOT been returned. 
                    throw buildOperationException(
                            "invalid typeFilter parameter type sent to $export operation");
                }
            }
        }
        return result;
    }

    public static Parameters getOutputParametersWithJson(PollingLocationResponse resource) throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder
                .parameter(Parameter.builder().name(string("return")).value(string(resource.toJsonString())).build());
        return parametersBuilder.build();
    }

    /**
     * checks and validates the job.
     * 
     * @param parameters
     * @return
     * @throws FHIROperationException
     */
    public static String checkAndValidateJob(Parameters parameters) throws FHIROperationException {
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (BulkDataConstants.PARAM_JOB.equals(parameter.getName().getValue())
                        && parameter.getValue() != null && parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                    String job = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();

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