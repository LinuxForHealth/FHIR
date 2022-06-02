/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains constants that are used through the fhir-* projects.
 */
public class FHIRConstants {
    public static final String FHIR_LOGGING_GROUP = "FHIRServer";

    public static final int FHIR_CONDITIONAL_DELETE_MAX_NUMBER_DEFAULT = 10;

    public static final int FHIR_PAGE_NUMBER_DEFAULT = 1;

    public static final int FHIR_PAGE_SIZE_DEFAULT = 10;

    public static final int FHIR_PAGE_SIZE_DEFAULT_MAX = 1000;

    public static final int FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX = 1000;

    public static final String FORMAT = "_format";

    public static final String PRETTY = "_pretty";

    public static final String SUMMARY = "_summary";

    public static final String ELEMENTS = "_elements";

    public static final String FORCE_UPDATE_HEADER = "X-FHIR-FORCE-UPDATE";

    public static final String EXT_BASE = "http://ibm.com/fhir/extension/";
    public static final String TAG_BASE = "http://ibm.com/fhir/tag/";

    /**
     * General parameter names that can be used with any FHIR interaction.
     *
     * @see <a href="https://www.hl7.org/fhir/r4/http.html#parameters">https://www.hl7.org/fhir/r4/http.html#parameters</a>
     */
    public static final Set<String> GENERAL_PARAMETER_NAMES =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(FORMAT, PRETTY, SUMMARY, ELEMENTS)));
}
