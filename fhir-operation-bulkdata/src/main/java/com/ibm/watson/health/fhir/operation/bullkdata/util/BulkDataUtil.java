/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.util;

import static com.ibm.watson.health.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.util.ModelSupport;
import com.ibm.watson.health.fhir.operation.bullkdata.BulkDataConstants;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;

/**
 * @author pbastide
 *
 */
public class BulkDataUtil {

    /**
     * repeated check of the resourceType.
     * 
     * @param resourceType
     * @param string
     * @return
     */
    public static boolean checkType(Class<? extends Resource> resourceType, String type) {
        return resourceType != null && type.compareTo(resourceType.getSimpleName()) == 0;
    }

    public static MediaType checkAndConvertToMediaType(Parameters parameters,
        FHIROperationContext operationContext) throws FHIROperationException {
        // Get the URI info.
        /*
         * The format for the requested bulk data files to be generated as per [FHIR Asynchronous Request
         * Pattern](http://hl7.org/fhir/async.html). Defaults to application/fhir+ndjson. Servers SHALL support [Newline
         * Delimited JSON](http://ndjson.org), but MAY choose to support additional output formats. Servers SHALL accept
         * the full content type of application/fhir+ndjson as well as the abbreviated representations
         * application/ndjson and ndjson.
         */

        // We're checking that it's acceptable.

        javax.ws.rs.core.UriInfo uriInfo =
                (javax.ws.rs.core.UriInfo) operationContext.getProperty(FHIROperationContext.PROPNAME_URI_INFO);
        MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        List<String> qps = queryParameters.get("_outputFormat");
        if (qps != null) {
            if (qps.isEmpty() || qps.size() != 1) {
                throw buildOperationException("Cardinality expectation for $apply operation parameter is 0..1 ");
            }

            String format = qps.get(0);

            if (!BulkDataConstants.EXPORT_FORMATS.contains(format)) {
                throw buildOperationException("Invalid requested format.  ");
            }

        }

        return MediaType.valueOf("application/fhir+ndjson");
    }

    public static FHIROperationException buildOperationException(String errMsg) {
        FHIROperationException operationException =
                new FHIROperationException(errMsg);

        List<Issue> issues = new ArrayList<>();
        Issue.Builder builder = Issue.builder();
        builder.code(IssueType.INVALID);
        builder.diagnostics(string(errMsg));
        builder.severity(IssueSeverity.ERROR);
        issues.add(builder.build());

        operationException.setIssues(issues);
        return operationException;
    }

    public static Instant checkAndExtractSince(Parameters parameters)
        throws FHIROperationException {

        /*
         * Resources will be included in the response if their state has changed after the supplied time (e.g. if
         * Resource.meta.lastUpdated is later than the supplied _since time).
         */

        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (parameter.getName() != null
                        && parameter.getName().getValue().compareTo("_since") == 0) {
                    return Instant.of(parameter.getValue().as(com.ibm.watson.health.fhir.model.type.Instant.class).getValue());
                }
            }
        }

        return null;
    }

    public static List<String> checkAndValidateTypes(Parameters parameters)
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

        List<String> result = new ArrayList<>();
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (parameter.getName() != null
                        && parameter.getName().getValue().compareTo("_type") == 0) {
                    String types =
                            parameter.getValue().as(com.ibm.watson.health.fhir.model.type.String.class).getValue();
                    for (String type : types.split(",")) {
                        if (type != null && !type.isEmpty() && ModelSupport.isResourceType(type)) {
                            result.add(type);
                        } else {
                            throw buildOperationException("invalid resource type sent as a parameter to $export operation");
                        }

                    }

                }
            }
        }

        return result;
    }

    public static List<String> checkAndValidateTypeFilters(Parameters parameters)
        throws FHIROperationException {

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

        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (parameter.getName() != null
                        && parameter.getName().getValue().compareTo("_type") == 0) {
                    String typeFilters =
                            parameter.getValue().as(com.ibm.watson.health.fhir.model.type.String.class).getValue();
                    if (!typeFilters.isEmpty()) {
                        throw new FHIROperationException("typeFilters is not supported");
                    }

                }
            }
        }

        return null;
    }

}
