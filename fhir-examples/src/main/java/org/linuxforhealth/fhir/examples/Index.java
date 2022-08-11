/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Index {
    /**
     * All examples in all formats
     */
    ALL("/spec-json.txt",
        "/generated-json.txt",
        "/spec-xml.txt",
        "/generated-xml.txt"),

    /**
     * All JSON examples
     */
    ALL_JSON("/spec-json.txt",
        "/generated-json.txt"),

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
    GENERATED_JSON("/generated-json.txt"),

    /**
     * R4 spec and IBM examples less than 1MB - used for concurrency tests
     */
    PERFORMANCE_JSON("/performance-json.txt"),

    /**
     * Both R4 spec and IBM generated examples
     */
    ALL_XML("/spec-xml.txt", "/generated-xml.txt"),

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
    GENERATED_XML("/generated-xml.txt"),

    /**
     * Bulk Data Location examples in JSON
     */
    BULKDATA_LOCATION_JSON("/bulk-data-location.txt"),

    /**
     * Bulk Data Dynamic Group examples in JSON
     */
    BULKDATA_GROUP_JSON("/bulk-data-group.txt");

    private List<String> paths = new ArrayList<>();

    private Index(String... path) {
        paths.addAll(Arrays.asList(path));
    }

    /**
     * @return the String path for each index file in this conceptual index
     */
    public List<String> paths() {
        return paths;
    }
}
