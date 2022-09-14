/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.util;

import org.hashids.Hashids;
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
    // min length constant for the "Hashids" we use to obfuscate job ids
    private static final int HASHID_MIN_LENGTH = 6;

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

    /**
     * encode the job id as a short string for use in URLs
     *
     * @param jobId the numeric job id to encode
     * @return
     * @implNote The implementation uses the tenantId from the request context and the
     *          corresponding coreBatchIdEncodingKey from fhir-server-config as a seed for
     *          the encoding.
     */
    public static String encodeJobId(long jobId) {
        Hashids encoder = getHashids();
        return encoder.encode(jobId);
    }

    /**
     * decode the job id back into the string representation of its numeric job id
     *
     * @param encodedJobId a job id encoded via {@link #encodeJobId(long, String, String)}
     * @return
     * @throws IllegalArgumentException if the passed encodedJobId could not be decoded
     * @implNote The implementation uses the tenantId from the request context and the
     *          corresponding coreBatchIdEncodingKey from fhir-server-config to decode.
     */
    public static String decodeJobId(String encodedJobId) {
        Hashids decoder = getHashids();
        long[] decodedArray = decoder.decode(encodedJobId);
        if (decodedArray.length != 1) {
            throw new IllegalArgumentException("expected an encodedJobId with a single part, but found " + decodedArray.length);
        }
        return Long.toString(decodedArray[0]);
    }

    /**
     * Get the tenantId from the request context and the corresponding coreBatchIdEncodingKey from
     * fhir-server-config and use that as the seed for the returned Hashids
     */
    private static Hashids getHashids() {
        String configSecret = ConfigurationFactory.getInstance().getCoreBatchIdEncodingKey();
        String seed = FHIRRequestContext.get().getTenantId() + ":" + configSecret;
        return new Hashids(seed, HASHID_MIN_LENGTH);
    }

    /**
     * Construct a FHIROperationException with the passed {@code msg} and
     * a single OperationOutcome.Issue of type {@code issueType}
     *
     * @param msg
     * @param issueType
     * @return
     * @throws FHIROperationException
     */
    public static FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType) {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg).withIssue(ooi);
    }

    /**
     * Construct a FHIROperationException with the passed {@code msg}, caused by (@code cause}, and
     * a single OperationOutcome.Issue of type {@code issueType}
     *
     * @param msg
     * @param cause
     * @param issueType
     * @return
     * @throws FHIROperationException
     */
    public static FHIROperationException buildExceptionWithIssue(String msg, Throwable cause, IssueType issueType) {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }
}