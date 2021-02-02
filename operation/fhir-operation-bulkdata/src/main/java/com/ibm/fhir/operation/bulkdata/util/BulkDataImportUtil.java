/*
 * (C) Copyright IBM Corp. 2019, 2021
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
    private FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    private EvaluationContext evaluationContext = null;

    public BulkDataImportUtil(Parameters parameters) throws FHIROperationException {
        if (parameters == null) {
            throw buildExceptionWithIssue("$import parameters are empty or null", IssueType.INVALID);
        }

        evaluationContext = new EvaluationContext(parameters);
    }

    /**
     * processes the parameter inputFormat from the Parameters object and evaluationContext.
     *
     * @implNote If there are multiple entries, the processing only takes the first entry that matches.
     *
     * @return
     * @throws FHIROperationException
     */
    public String retrieveInputFormat() throws FHIROperationException {
        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'inputFormat').value");
            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = (FHIRPathElementNode) iter.next();
                String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                if (BulkDataConstants.INPUT_FORMATS.contains(val)) {
                    return val;
                }
            }
        } catch (ClassCastException ce) {
            throw buildExceptionWithIssue("$import invalid parameter name in 'inputFormat'", ce, IssueType.INVALID);
        } catch (FHIRPathException e) {
            throw buildExceptionWithIssue("$import invalid parameters value type in 'inputFormat'", e, IssueType.INVALID);
        }

        throw buildExceptionWithIssue("$import requires 'inputFormat' is not found", IssueType.INVALID);
    }

    /**
     * processes the retrieveInputSource from the parameters object and evaluationContext.
     *
     * @implNote If there are multiple entries, the processing only takes the first entry.
     *
     * @return
     * @throws FHIROperationException
     */
    public String retrieveInputSource() throws FHIROperationException {
        try {
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'inputSource').value");
            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = (FHIRPathElementNode) iter.next();
                return node.asElementNode().element().as(com.ibm.fhir.model.type.Uri.class).getValue();
            }
        } catch (ClassCastException ce) {
            throw buildExceptionWithIssue("$import invalid parameter name in 'inputSource'", ce, IssueType.INVALID);
        } catch (FHIRPathException e) {
            throw buildExceptionWithIssue("$import found invalid parameter type while processing inputSource'", e, IssueType.INVALID);
        }

        throw buildExceptionWithIssue("$import requires 'inputSource' is not found", IssueType.INVALID);
    }

    /**
     * processes the retrieve inputs from the Parameters object and evaluationContext.
     * @return
     * @throws FHIROperationException
     */
    public List<Input> retrieveInputs() throws FHIROperationException {
        // Parameter: input (required)
        List<Input> inputs = new ArrayList<>();

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
    public void checkAllowedTotalSizeForTenantOrSystem(Integer inputSize) throws FHIROperationException {
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
     * verify url is allowed.
     *
     * @param url
     * @throws FHIROperationException
     */
    public void verifyUrlAllowed(String url) throws FHIROperationException {
        Boolean disabled =
                FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_VALID_URLS_DISABLED,
                        Boolean.FALSE);
        if (!disabled.booleanValue()) {
            List<String> baseUrls =
                    FHIRConfigHelper
                            .getStringListProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_VALID_BASE_URLS);
            if (url == null || baseUrls == null) {
                throw buildExceptionWithIssue(
                        "$import requires an approved and valid 'fhirServer/bulkdata/validBaseUrls'",
                        IssueType.INVALID);
            }

            if (!url.contains("//")) {
                // When the URL does not contain a double // by-pass the URL verification
                return;
            }

            // We have Urls
            for (String baseUrl : baseUrls) {
                if (url.startsWith(baseUrl)) {
                    return;
                }
            }
            throw buildExceptionWithIssue("$import does not have a valid base url", IssueType.INVALID);
        }
    }

    public StorageDetail retrieveStorageDetails() throws FHIROperationException {
        // Parameter: storageDetail (optional)

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

    private void checkValidContentEncoding(String contentEncoding) throws FHIROperationException {
        if (!BulkDataConstants.STORAGE_CONTENT_ENCODING.contains(contentEncoding)) {
            throw buildExceptionWithIssue(
                    "$import invalid 'contentEncoding' for storageDetail for '" + contentEncoding + "'",
                    IssueType.INVALID);
        }
    }

    public FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType)
            throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg).withIssue(ooi);
    }

    public FHIROperationException buildExceptionWithIssue(String msg, Throwable cause, IssueType issueType)
            throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }
}