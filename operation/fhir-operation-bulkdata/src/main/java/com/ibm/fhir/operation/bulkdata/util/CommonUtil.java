/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.OperationContextAdapter;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Common Util captures common methods
 */
public class CommonUtil {

    public CommonUtil() {
        // No Operation
    }

    /**
     * checks that bulkdata operation is enabled
     *
     * @throws FHIROperationException
     */
    public void checkEnabled() throws FHIROperationException {
        boolean enabled = ConfigurationFactory.getInstance().enabled();
        if (!enabled) {
            throw buildExceptionWithIssue("The bulkdata feature is disabled for this server or tenant", IssueType.FORBIDDEN);
        }
    }

    /**
     * @param operationContext
     * @param isImport
     */
    public void checkAllowed(FHIROperationContext operationContext, boolean isImport) throws FHIROperationException {
        try {
            OperationContextAdapter adapter = new OperationContextAdapter(operationContext, isImport);
            String source = adapter.getStorageProvider();
            String outcome = adapter.getStorageProviderOutcomes();

            if (!ConfigurationFactory.getInstance().hasStorageProvider(source)
                    || !ConfigurationFactory.getInstance().hasStorageProvider(outcome)) {
                String fhirTenant = FHIRRequestContext.get().getTenantId();
                StringBuilder builder = new StringBuilder("The 'fhirServer/bulkdata/storageProvider' for source [");
                builder.append(fhirTenant).append("/").append(source);
                builder.append("] or outcome [");
                builder.append(fhirTenant).append("/").append(outcome);
                builder.append("] passed is not configured properly");

                throw FHIROperationUtil.buildExceptionWithIssue(builder.toString(), IssueType.EXCEPTION);
            }

            StorageType type = ConfigurationFactory.getInstance().getStorageProviderStorageType(source);
            verifyAllowedType(type.value());
        } catch (FHIROperationException foe) {
            throw foe;
        } catch (Exception e) {
            throw buildExceptionWithIssue("storageType is disallowed, error processing request", IssueType.NOT_SUPPORTED);
        }

    }

    /**
     * checks the storage type is allowed.
     *
     * @param storageType
     * @throws FHIROperationException
     */
    public void verifyAllowedType(String storageType) throws FHIROperationException {
        if (!ConfigurationFactory.getInstance().isStorageTypeAllowed(storageType)) {
            throw buildExceptionWithIssue("storageType is disallowed", IssueType.NOT_SUPPORTED);
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