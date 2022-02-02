/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

/**
 * This class contains definitions of some non-standard media types.
 */
public class FHIRMediaType extends MediaType {

    // use com.ibm.fhir.core.FHIRVersionParam instead
    private static final String VERSION_40 = "4.0";
    private static final String VERSION_43 = "4.3";
    private static final String VERSION_401 = "4.0.1";
    private static final String VERSION_430 = "4.3.0";

    // Supported values for the MIME-type parameter fhirVersion.
    // https://www.hl7.org/fhir/http.html#version-parameter
    public static final String FHIR_VERSION_PARAMETER = "fhirVersion";
    public static final Set<String> SUPPORTED_FHIR_VERSIONS =
            Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(VERSION_40, VERSION_401, VERSION_43, VERSION_430)));

    private static final Map<String,String> fhirVersion40ParameterMap = Collections.singletonMap(FHIR_VERSION_PARAMETER, VERSION_40);
    private static final Map<String,String> fhirVersion43ParameterMap = Collections.singletonMap(FHIR_VERSION_PARAMETER, VERSION_43);

    public static final String SUBTYPE_FHIR_JSON = "fhir+json";
    public static final String APPLICATION_FHIR_JSON = "application/" + SUBTYPE_FHIR_JSON;
    public static final MediaType APPLICATION_FHIR_JSON_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON);
    public static final MediaType APPLICATION_FHIR_40_JSON_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON, fhirVersion40ParameterMap);
    public static final MediaType APPLICATION_FHIR_43_JSON_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON, fhirVersion43ParameterMap);

    public static final String SUBTYPE_FHIR_XML = "fhir+xml";
    public static final String APPLICATION_FHIR_XML = "application/" + SUBTYPE_FHIR_XML;
    public static final MediaType APPLICATION_FHIR_XML_TYPE = new MediaType("application", SUBTYPE_FHIR_XML);
    public static final MediaType APPLICATION_FHIR_40_XML_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON, fhirVersion40ParameterMap);
    public static final MediaType APPLICATION_FHIR_43_XML_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON, fhirVersion43ParameterMap);

    public static final String SUBTYPE_JSON_PATCH = "json-patch+json";
    public static final String APPLICATION_JSON_PATCH = "application/" + SUBTYPE_JSON_PATCH;
    public static final MediaType APPLICATION_JSON_PATCH_TYPE = new MediaType("application", SUBTYPE_JSON_PATCH);

    public static final String SUBTYPE_FHIR_NDJSON = "fhir+ndjson";
    public static final String APPLICATION_NDJSON = "application/" + SUBTYPE_FHIR_NDJSON;
    public static final MediaType APPLICATION_FHIR_NDJSON_TYPE = new MediaType("application", SUBTYPE_FHIR_NDJSON);

    public static final String SUBTYPE_FHIR_PARQUET = "fhir+parquet";
    public static final String APPLICATION_PARQUET = "application/" + SUBTYPE_FHIR_PARQUET;
    public static final MediaType APPLICATION_FHIR_PARQUET_TYPE = new MediaType("application", SUBTYPE_FHIR_PARQUET);
}
