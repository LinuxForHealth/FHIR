/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;

/**
 * This class contains definitions of some non-standard media types.
 */
public class FHIRMediaType extends MediaType {

    public final static String SUBTYPE_FHIR_JSON = "fhir+json";
    public final static String APPLICATION_FHIR_JSON = "application/" + SUBTYPE_FHIR_JSON;
    public final static MediaType APPLICATION_FHIR_JSON_TYPE = new MediaType("application", SUBTYPE_FHIR_JSON);

    public final static String SUBTYPE_FHIR_XML = "fhir+xml";
    public final static String APPLICATION_FHIR_XML = "application/" + SUBTYPE_FHIR_XML;
    public final static MediaType APPLICATION_FHIR_XML_TYPE = new MediaType("application", SUBTYPE_FHIR_XML);

    public final static String SUBTYPE_JSON_PATCH = "json-patch+json";
    public final static String APPLICATION_JSON_PATCH = "application/" + SUBTYPE_JSON_PATCH;
    public final static MediaType APPLICATION_JSON_PATCH_TYPE = new MediaType("application", SUBTYPE_JSON_PATCH);

    public final static String SUBTYPE_FHIR_NDJSON = "fhir+ndjson";
    public static final String APPLICATION_NDJSON = "application/" + SUBTYPE_FHIR_NDJSON;
    public final static MediaType APPLICATION_FHIR_NDJSON_TYPE = new MediaType("application", SUBTYPE_FHIR_NDJSON);

    public final static String SUBTYPE_FHIR_PARQUET = "fhir+parquet";
    public static final String APPLICATION_PARQUET = "application/" + SUBTYPE_FHIR_PARQUET;
    public final static MediaType APPLICATION_FHIR_PARQUET_TYPE = new MediaType("application", SUBTYPE_FHIR_PARQUET);

    // Supported values for the MIME-type parameter fhirVersion.
    // https://www.hl7.org/fhir/http.html#version-parameter
    public static final String FHIR_VERSION_PARAMETER = "fhirVersion";
    public static final Set<String> SUPPORTED_FHIR_VERSIONS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList("4.0","4.0.1")));
}
