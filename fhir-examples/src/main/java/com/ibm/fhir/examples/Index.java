/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Index {
    /**
     * All examples in all formats
     */
    ALL("/spec-json.txt", "/ibm-json.txt", "/spec-xml.txt", "/ibm-xml.txt"),

    /**
     * Both R4 spec and IBM generated examples
     */
    ALL_JSON("/spec-json.txt", "/ibm-json.txt"),

    /**
     * Small mix of spec and IBM examples used for unit tests to keep build times short
     */
    MINIMAL_JSON("/minimal-json.txt"),

    /**
     * Examples shipped with the FHIR R4 specification
     */
    SPEC_JSON("/spec-json.txt"),

    /**
     * All IBM generated examples
     */
    IBM_JSON("/ibm-json.txt"),

    /**
     * R4 spec and IBM examples less than 1MB - used for concurrency tests
     */
    PERFORMANCE_JSON("/performance-json.txt"),

    /**
     * Both R4 spec and IBM generated examples
     */
    ALL_XML("/spec-xml.txt", "/ibm-xml.txt"),

    /**
     * Small mix of spec and IBM examples used for unit tests to keep build times short
     */
    MINIMAL_XML("/minimal-xml.txt"),

    /**
     * All R4 spec examples
     */
    SPEC_XML("/spec-xml.txt"),

    /**
     * All IBM generated examples
     */
    IBM_XML("/ibm-xml.txt"),

    /**
     * Implementation Guides examples in JSON
     */
    PROFILES_CARRIN_BB_JSON("/profiles-fhir-ig-carin-bb-json.txt"),

    /**
     * Implementation Guides examples in XML
     */
    PROFILES_CARRIN_BB_XML("/profiles-fhir-ig-carin-bb-xml.txt"),

    /**
     * Implementation Guides examples in JSON
     */
    PROFILES_DAVINCI_PDEX_JSON("/profiles-fhir-ig-davinci-pdex-formulary-json.txt"),

    /**
     * Implementation Guides examples in XML
     */
    PROFILES_DAVINCI_PDEX_XML("/profiles-fhir-ig-davinci-pdex-formulary-xml.txt"),

    /**
     * Implementation Guides examples in JSON
     */
    PROFILES_US_CORE_JSON("/profiles-fhir-ig-us-core-json.txt"),

    /**
     * Implementation Guides examples in XML
     */
    PROFILES_US_CORE_XML("/profiles-fhir-ig-us-core-xml.txt"),

    /**
     * Implementation Guides examples in JSON
     */
    PROFILES_JSON("/profiles-fhir-ig-carin-bb-json.txt", "/profiles-fhir-ig-davinci-pdex-formulary-json.txt", "/profiles-fhir-ig-us-core-json.txt"),

    /**
     * Implementation Guides examples in XML
     */
    PROFILES_XML("/profiles-fhir-ig-carin-bb-xml.txt", "/profiles-fhir-ig-davinci-pdex-formulary-xml.txt", "/profiles-fhir-ig-us-core-xml.txt"),

    /**
     * Bulk Data Location examples in JSON
     */
    BULKDATA_LOCATION_JSON("/ibm-json-bulk-data-location.txt"),

    /**
     * Bulk Data Dynamic Group examples in JSON
     */
    BULKDATA_GROUP_JSON("/ibm-json-bulk-data-group.txt");

    private List<String> paths = new ArrayList<>();

    private Index(String... path) {
        paths.addAll(Arrays.asList(path));
    }

    /**
     * @return the String path for this index file
     */
    public List<String> paths() {
        return paths;
    }
}