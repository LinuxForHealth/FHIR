/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.OperationContextAdapter;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil.Type;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * BulkData Import Util captures common methods
 */
public class BulkDataImportUtil {

    private static final CommonUtil COMMON = new CommonUtil(Type.IMPORT);

    private FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    private EvaluationContext evaluationContext = null;
    private FHIROperationContext operationContext = null;

    public BulkDataImportUtil(FHIROperationContext operationContext, Parameters parameters) throws FHIROperationException {
        if (parameters == null) {
            throw COMMON.buildExceptionWithIssue("$import parameters are empty or null", IssueType.INVALID);
        }
        this.operationContext = operationContext;

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
                FHIRPathElementNode node = iter.next().as(FHIRPathElementNode.class);
                String val = node.asElementNode().element().as(com.ibm.fhir.model.type.String.class).getValue();
                if (OperationConstants.INPUT_FORMATS.contains(val)) {
                    return val;
                }
            }
        } catch (ClassCastException | FHIRPathException e) {
            throw COMMON.buildExceptionWithIssue("invalid $import parameter value in 'inputFormat'", e, IssueType.INVALID);
        }

        throw COMMON.buildExceptionWithIssue("$import requires 'inputFormat' is not found", IssueType.INVALID);
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
        } catch (NoSuchElementException | ClassCastException | FHIRPathException e) {
            throw COMMON.buildExceptionWithIssue("invalid $import parameter value in 'inputSource'", e, IssueType.INVALID);
        }

        throw COMMON.buildExceptionWithIssue("$import requires 'inputSource' is not found", IssueType.INVALID);
    }

    /**
     * processes the retrieve inputs from the Parameters object and evaluationContext.
     *
     * @return
     * @throws FHIROperationException
     */
    public List<Input> retrieveInputs() throws FHIROperationException {
        // Parameter: input (required)
        List<Input> inputs = new ArrayList<>();

        try {
            List<String> supportedResourceTypes = FHIRConfigHelper.getSupportedResourceTypes();
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "parameter.where(name = 'input')");

            Iterator<FHIRPathNode> iter = result.iterator();
            while (iter.hasNext()) {
                FHIRPathElementNode node = (FHIRPathElementNode) iter.next();

                // Resource Types extracted and Type is verified.
                EvaluationContext evaluationContextPartType = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartType =
                        evaluator.evaluate(evaluationContextPartType, "part.where(name = 'type').value");
                String type =
                        ((FHIRPathElementNode) resultPartType.iterator().next()).element().as(com.ibm.fhir.model.type.String.class).getValue();

                // Checks if not valid, and throws exception
                if (!ModelSupport.isResourceType(type)) {
                    throw COMMON.buildExceptionWithIssue("$import invalid Resource Type 'input'", IssueType.INVALID);
                }

                // Checks to see if the Server supports this ResourceType
                if (supportedResourceTypes != null && !supportedResourceTypes.contains(type)) {
                    System.out.println(supportedResourceTypes);
                    throw COMMON.buildExceptionWithIssue("A resource type not configured for 'input' check server configuration", IssueType.EXCEPTION);
                }

                // Resource URL extracted.
                EvaluationContext evaluationContextPartUrl = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartUrl =
                        evaluator.evaluate(evaluationContextPartUrl, "part.where(name = 'url').value");
                String url =
                        ((FHIRPathElementNode) resultPartUrl.iterator().next()).element().as(com.ibm.fhir.model.type.Url.class).getValue();

                // Verify Url is allowed
                verifyUrlAllowed(url);

                // Add to the Inputs List
                inputs.add(new Input(type, url));
            }
        } catch (java.util.NoSuchElementException nsee) {
            throw COMMON.buildExceptionWithIssue("$import invalid elements in the 'input' field", nsee, IssueType.INVALID);
        } catch (FHIRPathException e) {
            throw COMMON.buildExceptionWithIssue("$import invalid parameters with expression in 'input'", e, IssueType.INVALID);
        } catch (FHIROperationException opEx) {
            throw opEx;
        } catch (FHIRException e) {
            throw COMMON.buildExceptionWithIssue("Unable to retrieve server configuration", e, IssueType.INVALID);
        }

        if (inputs.isEmpty()) {
            throw COMMON.buildExceptionWithIssue("$import requires 'input' is not found", IssueType.INVALID);
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
        Integer tenantCount = ConfigurationFactory.getInstance().getInputLimit();
        if (tenantCount == null || tenantCount < inputSize) {
            throw COMMON.buildExceptionWithIssue("$import maximum input per bulkdata import request 'fhirServer/bulkdata/maxInputPerRequest'", IssueType.INVALID);
        }
    }

    /**
     * verify url is allowed.
     *
     * @param url
     * @throws FHIROperationException
     */
    public void verifyUrlAllowed(String url) throws FHIROperationException {
        ConfigurationAdapter config = ConfigurationFactory.getInstance();
        OperationContextAdapter adapter = new OperationContextAdapter(operationContext, true);
        String source = adapter.getStorageProvider();
        Boolean disabled = config.shouldStorageProviderValidateBaseUrl(source);

        // Only for https do we need to check the urls.
        if (!disabled.booleanValue() && StorageType.HTTPS.equals(config.getStorageProviderStorageType(source))) {
            List<String> baseUrls = config.getStorageProviderValidBaseUrls(source);
            if (url == null || baseUrls == null) {
                throw COMMON.buildExceptionWithIssue("$import requires an approved and valid baseUrl", IssueType.INVALID);
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
            throw COMMON.buildExceptionWithIssue("$import does not have a valid base url", IssueType.INVALID);
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
                        ((FHIRPathElementNode) resultPartType.iterator().next()).element().as(com.ibm.fhir.model.type.String.class).getValue();

                // Checks if not valid, and throws exception
                if (!OperationConstants.STORAGE_TYPES.contains(type)) {
                    throw COMMON.buildExceptionWithIssue("$import invalid type in 'storageDetail'", IssueType.INVALID);
                }

                // Resource URL extracted.
                EvaluationContext evaluationContextPartContentEncoding = new EvaluationContext(node.element());
                Collection<FHIRPathNode> resultPartContentEncoding =
                        evaluator.evaluate(evaluationContextPartContentEncoding, "part.where(name = 'contentEncoding').value");

                List<String> contentEncodings = new ArrayList<>();
                Iterator<FHIRPathNode> iterEncoding = resultPartContentEncoding.iterator();
                while (iterEncoding.hasNext()) {
                    String contentEncoding =
                            ((FHIRPathElementNode) iterEncoding.next()).element().as(com.ibm.fhir.model.type.String.class).getValue();
                    checkValidContentEncoding(contentEncoding);
                    contentEncodings.add(contentEncoding);
                }

                COMMON.verifyAllowedType(type);

                // Immediately Return and stop processing... we shouldn't have multiple storage details.
                return new StorageDetail(type, contentEncodings);
            }
        } catch (FHIROperationException foe) {
            throw foe;
        } catch (java.util.NoSuchElementException nsee) {
            throw COMMON.buildExceptionWithIssue("$import invalid elements in the 'storageDetail' field", nsee, IssueType.INVALID);
        } catch (FHIRPathException e) {
            throw COMMON.buildExceptionWithIssue("$import invalid parameters with expression in 'storageDetail'", e, IssueType.INVALID);
        }

        // There should be at least 1
        throw COMMON.buildExceptionWithIssue("$import required 'storageDetail' is not found", IssueType.INVALID);
    }

    private void checkValidContentEncoding(String contentEncoding) throws FHIROperationException {
        if (!OperationConstants.STORAGE_CONTENT_ENCODING.contains(contentEncoding)) {
            throw COMMON.buildExceptionWithIssue("$import invalid 'contentEncoding' for storageDetail for '" + contentEncoding + "'", IssueType.INVALID);
        }
    }
}