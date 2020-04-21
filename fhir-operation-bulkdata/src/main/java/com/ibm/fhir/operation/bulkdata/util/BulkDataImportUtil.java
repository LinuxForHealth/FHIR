/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;

/**
 * BulkData Import Util captures common methods
 */
public class BulkDataImportUtil {
    private BulkDataImportUtil() {
        // No Operation
    }

    public static FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType)
            throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg).withIssue(ooi);
    }

    public static FHIROperationException buildExceptionWithIssue(String msg, Throwable cause, IssueType issueType)
            throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }

    public static String retrieveInputFormat(Parameters parameters) throws FHIROperationException {
        // Parameter: inputFormat (required)
        // If there are multiple entries, the processing only takes the first entry.
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (BulkDataConstants.PARAM_INPUT_FORMAT.equals(parameter.getName().getValue())
                        && parameter.getValue() != null
                        && parameter.getValue().is(com.ibm.fhir.model.type.String.class)) {
                    // If the parameter isn't passed, use application/fhir+ndjson
                    // Check the MediaType
                    String val = parameter.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                    if (BulkDataConstants.INPUT_FORMATS.contains(val)) {
                        return val;
                    }
                }
            }
        }

        throw buildExceptionWithIssue("$import requires 'inputFormat' is not found", IssueType.INVALID);
    }

    public static String retrieveInputSource(Parameters parameters) throws FHIROperationException {
        // Parameter: inputSource (required)
        // If there are multiple entries, the processing only takes the first entry.
        if (parameters != null) {
            for (Parameters.Parameter parameter : parameters.getParameter()) {
                if (BulkDataConstants.PARAM_INPUT_SOURCE.equals(parameter.getName().getValue())
                        && parameter.getValue() != null && parameter.getValue().is(com.ibm.fhir.model.type.Uri.class)) {
                    // If the parameter isn't passed, use application/fhir+ndjson
                    return parameter.getValue().as(com.ibm.fhir.model.type.Uri.class).getValue();
                }
            }
        }

        throw buildExceptionWithIssue("$import requires 'inputSource' is not found", IssueType.INVALID);
    }

    public static List<Input> retrieveInputs(Parameters parameters) throws FHIROperationException {
        // Parameter: input (required)
        List<Input> inputs = new ArrayList<>();

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(parameters);

        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'input')");

            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = (FHIRPathElementNode) iter.next();

                // Resource Types extracted and Type is verified.
                EvaluationContext evaluationContextPartType = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartType =
                        evaluator.evaluate(evaluationContextPartType, "part.where(name = 'type').value");
                String type =
                        ((FHIRPathElementNode) resultPartType.iterator().next()).element()
                                .as(com.ibm.fhir.model.type.String.class).getValue();

                // Checks if not valid, and throws exception
                if (!ModelSupport.isResourceType(type)) {
                    throw buildExceptionWithIssue("$import invalid Resource Type 'input'", IssueType.INVALID);
                }

                // Resource URL extracted.
                EvaluationContext evaluationContextPartUrl = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartUrl =
                        evaluator.evaluate(evaluationContextPartUrl, "part.where(name = 'url').value");
                String url =
                        ((FHIRPathElementNode) resultPartUrl.iterator().next()).element()
                                .as(com.ibm.fhir.model.type.Url.class).getValue();

                // Verify Url is allowed
                verifyUrlAllowed(url);

                // Add to the Inputs List
                inputs.add(new Input(type, url));
            }
        } catch (FHIRPathException e) {
            throw buildExceptionWithIssue("$import invalid parameters with expression in 'input'", e,
                    IssueType.INVALID);
        }

        if (inputs.isEmpty()) {
            throw buildExceptionWithIssue("$import requires 'input' is not found", IssueType.INVALID);
        }

        checkAllowedTotalSizeForTenantOrSystem(inputs.size());
        return inputs;
    }

    /**
     * check the allowed total size for tenant and system
     * 
     * @param inputSize
     * @throws FHIROperationException
     */
    public static void checkAllowedTotalSizeForTenantOrSystem(Integer inputSize) throws FHIROperationException {
        Integer tenantCount =
                FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_MAX_INPUT_PER_TENANT,
                        BulkDataConstants.IMPORT_MAX_DEFAULT_INPUTS);
        if (tenantCount == null || tenantCount < inputSize) {
            throw buildExceptionWithIssue(
                    "$import maximum input per bulkdata import request 'fhirServer/bulkdata/maxInputPerRequest'",
                    IssueType.INVALID);
        }
    }

    /**
     * verify url is allowed
     * 
     * @param url
     * @throws FHIROperationException
     */
    public static void verifyUrlAllowed(String url) throws FHIROperationException {
        List<String> baseUrls =
                FHIRConfigHelper.getStringListProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_VALID_BASE_URLS);
        if (url == null || baseUrls == null) {
            throw buildExceptionWithIssue("$import requires an approved and valid 'fhirServer/bulkdata/validBaseUrls'",
                    IssueType.INVALID);
        }

        for (String baseUrl : baseUrls) {
            // When the URL does not contain a double // by-pass the URL verification
            if (url.startsWith(baseUrl) || !url.contains("//")) {
                return;
            }
        }
        throw buildExceptionWithIssue("$import does not have a valid base url", IssueType.INVALID);
    }

    public static StorageDetail retrieveStorageDetails(Parameters parameters) throws FHIROperationException {
        // Parameter: storageDetail (optional)

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(parameters);

        try {
            Collection<FHIRPathNode> result =
                    evaluator.evaluate(evaluationContext, "parameter.where(name = 'storageDetail')");

            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = (FHIRPathElementNode) iter.next();

                // Resource Types extracted and Type is verified.
                EvaluationContext evaluationContextPartType = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartType = evaluator.evaluate(evaluationContextPartType, "value");
                String type =
                        ((FHIRPathElementNode) resultPartType.iterator().next()).element()
                                .as(com.ibm.fhir.model.type.String.class).getValue();

                // Checks if not valid, and throws exception
                if (!BulkDataConstants.STORAGE_TYPES.contains(type)) {
                    throw buildExceptionWithIssue("$import invalid type in 'storageDetail'", IssueType.INVALID);
                }

                // Resource URL extracted.
                EvaluationContext evaluationContextPartContentEncoding = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartContentEncoding =
                        evaluator.evaluate(evaluationContextPartContentEncoding,
                                "part.where(name = 'contentEncoding').value");

                List<String> contentEncodings = new ArrayList<>();
                Iterator<FHIRPathNode> iterEncoding = resultPartContentEncoding.iterator();
                while (iterEncoding.hasNext()) {
                    String contentEncoding =
                            ((FHIRPathElementNode) iterEncoding.next()).element()
                                    .as(com.ibm.fhir.model.type.String.class).getValue();
                    checkValidContentEncoding(contentEncoding);
                    contentEncodings.add(contentEncoding);
                }

                // Immediately Return and stop processing... we shouldn't have multiple storage details. 
                return new StorageDetail(type, contentEncodings);
            }
        } catch (FHIRPathException e) {
            throw buildExceptionWithIssue("$import invalid parameters with expression in 'input'", e,
                    IssueType.INVALID);
        }

        // There should be at least 1
        throw buildExceptionWithIssue("$import required 'storageDetail' is not found", IssueType.INVALID);
    }

    private static void checkValidContentEncoding(String contentEncoding) throws FHIROperationException {
        if (!BulkDataConstants.STORAGE_CONTENT_ENCODING.contains(contentEncoding)) {
            throw buildExceptionWithIssue(
                    "$import invalid 'contentEncoding' for storageDetail for '" + contentEncoding + "'",
                    IssueType.INVALID);
        }
    }
}