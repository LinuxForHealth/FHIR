/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.model.transformer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.hashids.Hashids;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * Manages the encoding/decoding of Job Ids for use in URLs.
 */
public class JobIdEncodingTransformer {
    private static final int MIN_ENCODED_LENGTH = 5;

    // Tenant-specific util used for encoding/decoding JavaBatch Job ID
    private static ConcurrentMap<String, Hashids> KEY_MAP = new ConcurrentHashMap<>();

    private static JobIdEncodingTransformer transformer = null;

    private JobIdEncodingTransformer() {
        // No Operation
    }

    /**
     * get the instance
     *
     * @return
     */
    public static JobIdEncodingTransformer getInstance() {
        if (transformer == null) {
            transformer = new JobIdEncodingTransformer();
        }
        return transformer;
    }

    /**
     * Create the tenant-specific Hashids object with a "salt" from fhir-server-config
     * @return the Hashids object or null if no salt is configured in fhir-server-config
     */
    private Hashids createHashids() {
        String salt = ConfigurationFactory.getInstance().getCoreBatchIdEncodingKey();

        if (salt != null && !salt.isEmpty()) {
            return new Hashids(salt, MIN_ENCODED_LENGTH);
        } else {
            return null;
        }
    }

    /**
     * encodes the job id
     *
     * @param jobId
     * @return
     */
    public String encodeJobId(long jobId) {
        String tenantId = FHIRRequestContext.get().getTenantId();
        Hashids encoder = KEY_MAP.computeIfAbsent(tenantId, k -> createHashids());
        if (encoder == null) {
            return Long.toString(jobId);
        } else {
            return encoder.encode(jobId);
        }
    }

    /**
     * decodes the job id
     *
     * @param encodedJobId
     * @return
     */
    public String decodeJobId(String encodedJobId) {
        String tenantId = FHIRRequestContext.get().getTenantId();
        Hashids decoder = KEY_MAP.computeIfAbsent(tenantId, k -> createHashids());
        if (decoder == null) {
            return encodedJobId;
        } else {
            long[] decode = decoder.decode(encodedJobId);
            if (decode.length != 1) {
                throw new IllegalArgumentException("expected an encodedJobId with a single part, but found " + decode.length);
            }
            return Long.toString(decoder.decode(encodedJobId)[0]);
        }
    }
}
