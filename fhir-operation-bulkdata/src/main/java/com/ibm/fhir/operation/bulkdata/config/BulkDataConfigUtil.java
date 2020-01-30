/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;

/**
 * bulkdata.json is picked up from the given file, and loaded into an intermediate map.
 */
public class BulkDataConfigUtil {
    private static final String CLASSNAME = BulkDataConfigUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static BulkDataTenantSpecificCache cache = new BulkDataTenantSpecificCache();

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    public static final String APPLICATION_NAME = "applicationName";
    public static final String MODULE_NAME = "moduleName";
    public static final String JOB_XML_NAME = "jobXMLName";
    public static final String JOB_PARAMETERS = "jobParameters";

    public static final String JOB_PARAMETERS_BUCKET = "cos.bucket.name";
    public static final String JOB_PARAMETERS_LOCATION = "cos.location";
    public static final String JOB_PARAMETERS_ENDPOINT = "cos.endpointurl";
    public static final String JOB_PARAMETERS_TENANT = "fhir.tenant";
    public static final String JOB_PARAMETERS_IBM = "cos.credential.ibm";
    public static final String JOB_PARAMETERS_KEY = "cos.api.key";
    public static final String JOB_PARAMETERS_ID = "cos.srvinst.id";
    public static final String JOB_PARAMETERS_PAGES = "cos.pagesperobject";
    public static final String JOB_PARAMETERS_MAX = "cos.read.maxobjects";
    public static final String JOB_PARAMETERS_COSREADPER = "fhir.cosreadsperdbbatch";

    public static final String BATCH_USER = "batch-user";
    public static final String BATCH_USER_PASS = "batch-user-password";
    public static final String BATCH_URL = "batch-uri";
    public static final String BASE_URI = "base-uri";

    public static final String BATCH_TRUSTSTORE = "batch-truststore";
    public static final String BATCH_TRUSTSTORE_PASS = "batch-truststore-password";

    public static final String IMPLEMENTATION_TYPE = "implementation_type";

    private BulkDataConfigUtil() {
        // No Operation
    }

    public static BulkDataTenantSpecificCache getInstance() {
        return cache;
    }

    /**
     * populates from a configuration file 
     * 
     * @param f
     * @return
     */
    public static Map<String, String> populateConfiguration(File f) {
        Map<String, String> configs = new LinkedHashMap<>();
        try (FileInputStream stream = new FileInputStream(f)) {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(stream, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();

                addToMap(jsonObject, configs, APPLICATION_NAME);
                addToMap(jsonObject, configs, MODULE_NAME);
                addToMap(jsonObject, configs, JOB_XML_NAME);

                addToMap(jsonObject, configs, IMPLEMENTATION_TYPE);

                addToMap(jsonObject, configs, BATCH_USER);
                addToMap(jsonObject, configs, BATCH_USER_PASS);
                addToMap(jsonObject, configs, BATCH_URL);
                addToMap(jsonObject, configs, BATCH_TRUSTSTORE);
                addToMap(jsonObject, configs, BATCH_TRUSTSTORE_PASS);

                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_BUCKET);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_LOCATION);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_ENDPOINT);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_IBM);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_KEY);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_ID);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_PAGES);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_MAX);
                addChildrenToMap(jsonObject, configs, JOB_PARAMETERS, JOB_PARAMETERS_COSREADPER);

            }

        } catch (FileNotFoundException e) {
            // This exception is highly unlikely, but still possible.
            log.warning("The file is not found for bulkdata.json");
        } catch (IOException e) {
            // This exception is highly unlikely, but still possible.
            log.log(Level.WARNING, "The file is not found for bulkdata.json", e);
        }
        return configs;
    }

    private static void addChildrenToMap(JsonObject jsonObject, Map<String, String> configs, String jobParameters,
            String paramName) {
        if (jsonObject.containsKey(jobParameters)) {
            JsonObject obj = jsonObject.getJsonObject(jobParameters);
            addToMap(obj, configs, paramName);
        } else {
            log.warning("JobParameters obj not found in bulkdata.json ");
        }
    }

    public static void addToMap(JsonObject jsonObject, Map<String, String> configs, String name) {
        if (jsonObject.containsKey(name)) {
            String value = jsonObject.getString(name);
            configs.put(name, value);
        } else {
            log.warning("Value not found in bulkdata.json '" + name + "'");
        }
    }
}