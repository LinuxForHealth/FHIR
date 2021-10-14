/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility functions for storing FHIR payload data in S3/COS
 */
public class COSPayloadHelper {
    private static final String FHIR_PAYLOAD_BUCKETNAME_TENANT_SALT = "fhir.payload.bucketname.tenant.salt";
    private static final String SHA_256 = "SHA-256";

    /**
     * Generate a bucket name for the given tenant name. The name is computed as a hash
     * of the tenantId and dsId values combined with a salt value obtained from the environment. 
     * Deployments should use a 256 bit value for the salt, ideally created with a 
     * cryptographically secure random number generator. This value should be unique per 
     * deployment, but shared across multiple (scale-out) servers within that deployment.
     *
     * In the unlikely event that HASH(salt, tenantId, dsId) generates a value colliding with an
     * existing bucket (whose names are required to be globally unique), the tenant configuration
     * can override this value and provide a specific name (which should also be a large random
     * value so that it can't be guessed).
     * @param tenantId
     * @param dsId
     * @return
     */
    public static String makeTenantBucketName(String tenantId, String dsId) {
        final String salt64 = System.getProperty(FHIR_PAYLOAD_BUCKETNAME_TENANT_SALT);

        if (salt64 == null) {
            throw new IllegalStateException("Missing value for environment variable: '" + FHIR_PAYLOAD_BUCKETNAME_TENANT_SALT + "'");
        }

        byte[] salt = Base64.getDecoder().decode(salt64);
        try {
            /** To conform with DNS requirements, the following constraints apply:
             *       Bucket names should not contain underscores
             *       Bucket names should be between 3 and 63 characters long
             *       Bucket names should not end with a dash
             *       Bucket names cannot contain adjacent periods
             *       Bucket names cannot contain dashes next to periods (e.g., "my-.bucket.com" and "my.-bucket" are invalid)
             *       Bucket names cannot contain uppercase characters
             */
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            digest.digest(salt);
            digest.digest("~".getBytes(StandardCharsets.UTF_8));
            digest.digest(tenantId.getBytes(StandardCharsets.UTF_8));
            digest.digest("~".getBytes(StandardCharsets.UTF_8));
            digest.digest(dsId.getBytes(StandardCharsets.UTF_8));

            // A 32-byte value, which we need to convert into a character string
            // matching the above requirements. Simplest is base-36, all lower
            // case letters and numbers 0..9. But that's non-trivial, so for now
            // we just use Base64 and reduce to lower case plus some simple subs
            // Not perfect, but OK for now. Users can always pick their own bucket
            // name if they don't want to use the generated value.
            // NOTE: this is not intended in any way to be cryptographically secure!
            byte[] value = digest.digest();
            String name = Base64.getEncoder().withoutPadding().encodeToString(value).toLowerCase();
            return name.replaceAll("/", "0").replaceAll("+", "1");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MessageDigest not found: " + SHA_256, e);
        }
    }
}