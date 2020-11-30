/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;

public class BulkDataConfigUtil {
    private static final String CLASSNAME = BulkDataConfigUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public static final String APPLICATION_NAME = "applicationName";
    public static final String MODULE_NAME = "moduleName";
    public static final String JOB_XML_NAME = "jobXMLName";

    public static final String JOB_PARAMETERS_BUCKET = "cos.bucket.name";
    public static final String JOB_PARAMETERS_LOCATION = "cos.location";
    public static final String JOB_PARAMETERS_ENDPOINT_INTERNAL = "cos.endpoint.internal";
    public static final String JOB_PARAMETERS_ENDPOINT_EXTERNAL = "cos.endpoint.external";
    public static final String JOB_PARAMETERS_TENANT = "fhir.tenant";
    public static final String JOB_PARAMETERS_IBM = "cos.credential.ibm";
    public static final String JOB_PARAMETERS_KEY = "cos.api.key";
    public static final String JOB_PARAMETERS_ID = "cos.srvinst.id";

    public static final String BATCH_USER = "batch-user";
    public static final String BATCH_USER_PASS = "batch-user-password";
    public static final String BATCH_URL = "batch-uri";
    public static final String BASE_URI = "base-uri";
    public static final String BATCH_TRUSTSTORE = "batch-truststore";
    public static final String BATCH_TRUSTSTORE_PASS = "batch-truststore-password";

    public static final String IMPLEMENTATION_TYPE = "implementation_type";
    public static final String INCOMING_URL = "incomingUrl";

    private BulkDataConfigUtil() {
        // No Operation
    }

    /**
     * Get JavaBatch Job configuration from the default/tenant configuration.
     */
    public static Map<String, String> getBatchJobConfig() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put(APPLICATION_NAME, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_APPLICATIONNAME, null));
        properties.put(MODULE_NAME, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_MODULENAME, null));
        properties.put(BATCH_USER, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_BATCHUSER, null));
        properties.put(BATCH_USER_PASS, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_BATCHUSERPWD, null));
        properties.put(BATCH_URL, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_BATCHURI, null));
        properties.put(BATCH_TRUSTSTORE, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_BATCHTRUSTSTORE, null));
        properties.put(BATCH_TRUSTSTORE_PASS, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_BATCHTRUSTSTOREPWD, null));
        properties.put(IMPLEMENTATION_TYPE, FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_IMPTYPE, null));

        PropertyGroup jobParameters = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_BULKDATA_BATCHJOB_PARAMETERS);
        if (jobParameters != null) {
            properties.put(JOB_PARAMETERS_BUCKET, jobParameters.getStringProperty(JOB_PARAMETERS_BUCKET));
            properties.put(JOB_PARAMETERS_LOCATION, jobParameters.getStringProperty(JOB_PARAMETERS_LOCATION));
            properties.put(JOB_PARAMETERS_ENDPOINT_INTERNAL, jobParameters.getStringProperty(JOB_PARAMETERS_ENDPOINT_INTERNAL));
            properties.put(JOB_PARAMETERS_ENDPOINT_EXTERNAL, jobParameters.getStringProperty(JOB_PARAMETERS_ENDPOINT_EXTERNAL));
            properties.put(JOB_PARAMETERS_TENANT, jobParameters.getStringProperty(JOB_PARAMETERS_TENANT));
            properties.put(JOB_PARAMETERS_IBM, jobParameters.getStringProperty(JOB_PARAMETERS_IBM));
            properties.put(JOB_PARAMETERS_KEY, jobParameters.getStringProperty(JOB_PARAMETERS_KEY));
            properties.put(JOB_PARAMETERS_ID, jobParameters.getStringProperty(JOB_PARAMETERS_ID));
        }
        return properties;
    }


    public static SecretKeySpec getBatchJobIdEncryptionKey(String strJobIdEncryptionKey) {
        SecretKeySpec secretKey = null;

        if (strJobIdEncryptionKey != null && !strJobIdEncryptionKey.isEmpty()) {
            try {
                byte[] keyBytes = strJobIdEncryptionKey.getBytes("UTF-8");
                keyBytes = Arrays.copyOf(MessageDigest.getInstance("SHA-1").digest(keyBytes), 16);
                secretKey = new SecretKeySpec(keyBytes, "AES");
            } catch (Exception e) {
                log.log(Level.WARNING, "Fail to generate encryption key from config!", e);
            }
        }

        if (secretKey == null) {
            log.warning("Failed to get encryption key, JavaBatch Job ids will not be encrypted!");
        }
        return secretKey;
    }
}