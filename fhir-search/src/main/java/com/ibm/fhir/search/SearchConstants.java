/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search;

import static com.ibm.fhir.model.type.String.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.search.exception.SearchExceptionUtil;

/**
 * Search Constants
 */
public class SearchConstants {

    private SearchConstants() {
        // No Op
    }

    private static final String SUBSETTED_TAG_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ObservationValue";
    private static final String SUBSETTED_TAG_CODE = "SUBSETTED";
    private static final String SUBSETTED_TAG_DISPLAY = "subsetted";
    public static final Coding SUBSETTED_TAG = Coding.builder()
            .system(Uri.of(SUBSETTED_TAG_SYSTEM))
            .code(Code.of(SUBSETTED_TAG_CODE))
            .display(string(SUBSETTED_TAG_DISPLAY))
            .build();

    public static final String LOG_BOUNDARY = "---------------------------------------------------------";

    // XML Processing.
    public static final String DTM_MANAGER = "com.sun.org.apache.xml.internal.dtm.DTMManager";

    public static final String DTM_MANAGER_DEFAULT = "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault";

    // Line Separator
    public static final String NL = System.getProperty("line.separator");

    // Used to find delimiters escaped by '\' so we don't split on them
    // @see https://www.hl7.org/fhir/r4/search.html#escaping
    public static final String BACKSLASH_NEGATIVE_LOOKBEHIND = "(?<!\\\\)";

    public static final String COMPARTMENTS_JSON = "compartments.json";

    // Value Types Regex.
    public static final String PARAMETER_DELIMITER_REGEX = "\\|";
    public static final String COMPONENT_PATH_REGEX = "\\.";
    public static final char START_WHERE = '(';

    // This constant represents the maximum _count parameter value.
    // If the user specifies a value greater than this, we'll just use this value instead.
    // In the future, we might want to make this value configurable.
    public static final int MAX_PAGE_SIZE = 1000;

    // _sort
    public static final String SORT = "_sort";

    // _include
    public static final String INCLUDE = "_include";

    // _revinclude
    public static final String REVINCLUDE = "_revinclude";

    // _page
    public static final String PAGE = "_page";

    // _elements
    public static final String ELEMENTS = "_elements";

    // _count
    public static final String COUNT = "_count";

    // _summary
    public static final String SUMMARY = "_summary";

    // _total
    public static final String TOTAL = "_total";

    // _type
    public static final String RESOURCE_TYPE = "_type";

    // _has
    public static final String HAS = "_has";

    public static final String BASE_SYSTEM_EXT_URL = "http://ibm.com/fhir/extension/";
    public static final String IMPLICIT_SYSTEM_EXT_URL = BASE_SYSTEM_EXT_URL + "implicit-system";

    // set as unmodifiable
    public static final List<String> SEARCH_RESULT_PARAMETER_NAMES =
            Collections.unmodifiableList(Arrays.asList(SORT, COUNT, PAGE, INCLUDE, REVINCLUDE, ELEMENTS, SUMMARY, TOTAL));

    // set as unmodifiable
    public static final String LAST_UPDATED = "_lastUpdated";
    public static final String ID = "_id";
    public static final List<String> SYSTEM_LEVEL_SORT_PARAMETER_NAMES = Collections.unmodifiableList(Arrays.asList(ID, LAST_UPDATED));

    // set as unmodifiable
    public static final List<String> SEARCH_SINGLETON_PARAMETER_NAMES =
            Collections.unmodifiableList(Arrays.asList(SORT, COUNT, PAGE, SUMMARY, TOTAL, ELEMENTS, RESOURCE_TYPE));

    // Empty Query String
    public static final String EMPTY_QUERY_STRING = "";

    /*
     * chained parameter character "."
     */
    public static final String CHAINED_PARAMETER_CHARACTER = ".";

    public static final String PARAMETER_DELIMITER = "|";

    public static final char COLON_DELIMITER = ':';

    public static final String COLON_DELIMITER_STR = ":";

    public static final String WILDCARD = "*";

    public static final char AND_CHAR = '&';

    public static final char EQUALS_CHAR = '=';

    public static final String JOIN_STR = ",";

    public static final String AND_CHAR_STR = "&";

    // Filter
    public static final String WILDCARD_FILTER = "*";

    // Resource Constants to reflect a hierarchy:
    // RESOURCE -> DOMAIN_RESOURCE -> Instance (e.g. Claim);
    public static final String RESOURCE_RESOURCE = "Resource";
    public static final String DOMAIN_RESOURCE_RESOURCE = "DomainResource";

    // The resourceTypeModifierMap is set one time on startup and is a final value.
    // Set as unmodifiable.
    public static final Map<Type, List<Modifier>> RESOURCE_TYPE_MODIFIER_MAP =
            Collections.unmodifiableMap(new HashMap<SearchConstants.Type, List<SearchConstants.Modifier>>() {

                private static final long serialVersionUID = -7809685447880880523L;

                {
                    put(SearchConstants.Type.STRING, Arrays.asList(Modifier.EXACT, Modifier.CONTAINS, Modifier.MISSING));
                    put(SearchConstants.Type.REFERENCE, Arrays.asList(Modifier.TYPE, Modifier.IDENTIFIER, Modifier.MISSING));
                    put(SearchConstants.Type.URI, Arrays.asList(Modifier.BELOW, Modifier.ABOVE, Modifier.MISSING));
                    put(SearchConstants.Type.TOKEN, Arrays.asList(Modifier.TEXT, Modifier.NOT,
                            Modifier.ABOVE, Modifier.BELOW, Modifier.IN, Modifier.NOT_IN, Modifier.OF_TYPE, Modifier.MISSING));
                    put(SearchConstants.Type.NUMBER, Arrays.asList(Modifier.MISSING));
                    put(SearchConstants.Type.DATE, Arrays.asList(Modifier.MISSING));
                    put(SearchConstants.Type.QUANTITY, Arrays.asList(Modifier.MISSING));
                    put(SearchConstants.Type.COMPOSITE, Arrays.asList(Modifier.MISSING));
                    put(SearchConstants.Type.SPECIAL, Arrays.asList(Modifier.MISSING));
                }
            });

    /**
     * Prefixes for Search parameters
     */
    public enum Prefix {
        EQ("eq"),
        NE("ne"),
        GT("gt"),
        LT("lt"),
        GE("ge"),
        LE("le"),
        SA("sa"),
        EB("eb"),
        AP("ap");

        private String value = null;

        Prefix(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Prefix fromValue(String value) {
            for (Prefix prefix : Prefix.values()) {
                if (prefix.value.equals(value)) {
                    return prefix;
                }
            }
            throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
        }
    }

    /**
     * Types
     *
     * @author markd
     *
     */
    public enum Type {
        NUMBER("number"),
        DATE("date"),
        STRING("string"),
        TOKEN("token"),
        REFERENCE("reference"),
        COMPOSITE("composite"),
        QUANTITY("quantity"),
        URI("uri"),
        SPECIAL("special");

        private String value = null;

        Type(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
        }
    }

    /**
     * Search Modifiers
     */
    public enum Modifier {
        MISSING("missing"),
        EXACT("exact"),
        CONTAINS("contains"),
        TEXT("text"),
        IN("in"),
        BELOW("below"),
        ABOVE("above"),
        NOT("not"),
        NOT_IN("not-in"),
        TYPE("[type]"),
        OF_TYPE("of-type"),
        IDENTIFIER("identifier");

        private String value = null;

        Modifier(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static Modifier fromValue(String value) {
            for (Modifier modifier : Modifier.values()) {
                if (modifier.value.equalsIgnoreCase(value)) {
                    return modifier;
                }
            }
            throw SearchExceptionUtil.buildNewIllegalArgumentException(value);
        }
    }
}
