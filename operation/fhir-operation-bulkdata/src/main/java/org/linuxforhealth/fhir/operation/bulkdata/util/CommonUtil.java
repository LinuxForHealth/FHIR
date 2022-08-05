/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.util;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.config.OperationContextAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Common Util captures common methods
 */
public class CommonUtil {

    /**
     * Type of Operation Call
     */
    public enum Type {
        EXPORT,
        IMPORT,
        STATUS;
    }

    private Type opType;

    /**
     *
     * @param type the type of the Export
     */
    public CommonUtil(Type opType) {
        this.opType = opType;
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

            String opTypeMsg = "export";
            if (opType.equals(Type.IMPORT)) {
                opTypeMsg = "import";
            }

            if (!ConfigurationFactory.getInstance().hasStorageProvider(source)) {
                String fhirTenant = FHIRRequestContext.get().getTenantId();
                StringBuilder builder = new StringBuilder("The requested ");
                builder.append(opTypeMsg);
                builder.append(" source storageProvider configuration is missing [");
                builder.append(fhirTenant).append("/").append(source);
                builder.append("]");

                throw FHIROperationUtil.buildExceptionWithIssue(builder.toString(), IssueType.EXCEPTION);
            }

            // For the OperationOutcomes if they are used.
            if (ConfigurationFactory.getInstance().shouldStorageProviderCollectOperationOutcomes(source)
                    && !ConfigurationFactory.getInstance().hasStorageProvider(source)) {
                String fhirTenant = FHIRRequestContext.get().getTenantId();
                StringBuilder builder = new StringBuilder("The requested ");
                builder.append(opTypeMsg);
                builder.append(" outcome storageProvider configuration is missing [");
                builder.append(fhirTenant).append("/").append(outcome);
                builder.append("]");

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
}