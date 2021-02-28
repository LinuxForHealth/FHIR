/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model.transformer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * Manages the Job Id Encryption.
 */
public class JobIdEncodingTransformer {

    private static final String CLASSNAME = JobIdEncodingTransformer.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    // Encryption key used for JavaBatch Job ID
    public static final SecretKeySpec BATCHJOBID_ENCRYPTION_KEY =  getJobIdEncryptionKey();

    public JobIdEncodingTransformer() {
        // No Operation
    }

    /*
     * gets the server-wide specific encryption key.
     * @return
     */
    private static SecretKeySpec getJobIdEncryptionKey() {
        String encryptionKey = ConfigurationFactory.getInstance().getCoreBatchIdEncryptionKey();
        SecretKeySpec secretKey = null;

        if (encryptionKey != null && !encryptionKey.isEmpty()) {
            try {
                byte[] keyBytes = encryptionKey.getBytes("UTF-8");
                keyBytes = Arrays.copyOf(MessageDigest.getInstance("SHA-1").digest(keyBytes), 16);
                secretKey = new SecretKeySpec(keyBytes, "AES");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Fail to generate encryption key from config!", e);
            }
        }

        if (secretKey == null) {
            logger.warning("Failed to get encryption key, JavaBatch Job ids will not be encrypted!");
        }
        return secretKey;
    }

    /**
     * encodes the job id
     *
     * @implNote note a Cipher is used here, however it is not used to encrypt sensitive information rather encode the jobId.
     *
     * @param jobId
     * @return
     */
    public String endcodeJobId(String jobId) {
        // Encrypt and UrlEncode the batch job id.
        if (BATCHJOBID_ENCRYPTION_KEY == null) {
            return jobId;
        } else {
            try {
                // Use light weight encryption without salt to simplify both the encryption/decryption and also config.
                Cipher cp = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cp.init(Cipher.ENCRYPT_MODE, BATCHJOBID_ENCRYPTION_KEY);

                // Encrypt the job id, base64-encode it, and replace all `/` chars with the less problematic `_` char
                String encodedJobId = Base64.getEncoder().withoutPadding().encodeToString(cp.doFinal(jobId.getBytes("UTF-8"))).replaceAll("/", "_");
                // The encrypted job id is used in the polling content location url directly, so urlencode here.
                return java.net.URLEncoder.encode(encodedJobId, StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                return jobId;
            }
        }
    }

    /**
     * decodes the job id.
     *
     * @implNote note a Cipher is used here, however it is not used to encrypt sensitive information rather encode the jobId.
     *
     * @param encodedJobId
     * @return
     */
    public String decodeJobId(String encodedJobId) {
        // Decrypt to get the batch job id.
        if (BATCHJOBID_ENCRYPTION_KEY == null) {
            return encodedJobId;
        } else {
            try {
                // Use light weight encryption without salt to simplify both the encryption/decryption and also config.
                Cipher cp = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                cp.init(Cipher.DECRYPT_MODE, BATCHJOBID_ENCRYPTION_KEY);
                // The encrypted job id has already been urldecoded by liberty runtime before reaching this function,
                // so, we don't do urldecode here.)
                return new String(cp.doFinal(Base64.getDecoder().decode(encodedJobId.replaceAll("_", "/"))), "UTF-8");
            } catch (Exception e) {
                return encodedJobId;
            }
        }
    }
}
