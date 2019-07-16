/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search Constants
 * 
 * @author pbastide
 *
 */
public class SearchConstants {

    private SearchConstants() {
        // No Op
    }

    // XML Processing.
    public static final String DTM_MANAGER = "com.sun.org.apache.xml.internal.dtm.DTMManager";

    public static final String DTM_MANAGER_DEFAULT = "com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault";

    // Line Separator
    public static final String NL = System.getProperty("line.separator");

    // Used to find delimiters escaped by '\' so we don't split on them
    // @see https://www.hl7.org/fhir/r4/search.html#escaping
    public static final String BACKSLASH_NEGATIVE_LOOKBEHIND = "(?<!\\\\)";

    public static final String COMPARTMENTS_JSON = "compartments.json";

    // This constant represents the maximum _count parameter value.
    // If the user specifies a value greater than this, we'll just use this value instead.
    // In the future, we might want to make this value configurable.
    public static final int MAX_PAGE_SIZE = 1000;

    // _format
    public static final String FORMAT = "_format";

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

    // set as unmodifiable
    public static final List<String> SEARCH_RESULT_PARAMETER_NAMES =
            Collections.unmodifiableList(Arrays.asList(SORT, "_sort:asc", "_sort:desc", COUNT, PAGE, INCLUDE, REVINCLUDE, ELEMENTS));

    // set as unmodifiable
    public static final List<String> SYSTEM_LEVEL_SORT_PARAMETER_NAMES = Collections.unmodifiableList(Arrays.asList("_id", "_lastUpdated"));

    // Empty Query String
    public static final String EMPTY_QUERY_STRING = "";

    /*
     * chained parameter character "."
     */
    public static final String CHAINED_PARAMETER_CHARACTER = ".";

    public static final String PARAMETER_DELIMITER = "|";
    
    public static final char COLON_DELIMITER = ':';
    
    public static final String COLON_DELIMITER_STR = ":";

    // The resourceTypeModifierMap is set one time on startup and is a final value.
    // Set as unmodifiable.
    public static final Map<Type, List<Modifier>> RESOURCE_TYPE_MODIFIER_MAP =
            Collections.unmodifiableMap(new HashMap<SearchConstants.Type, List<SearchConstants.Modifier>>() {

                private static final long serialVersionUID = -7809685447880880523L;

                {
                    put(SearchConstants.Type.STRING, Arrays.asList(SearchConstants.Modifier.EXACT, SearchConstants.Modifier.CONTAINS, SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.REFERENCE, Arrays.asList(SearchConstants.Modifier.TYPE, SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.URI, Arrays.asList(SearchConstants.Modifier.BELOW, SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.TOKEN, Arrays.asList(SearchConstants.Modifier.BELOW, SearchConstants.Modifier.NOT, SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.NUMBER, Arrays.asList(SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.DATE, Arrays.asList(SearchConstants.Modifier.MISSING));
                    put(SearchConstants.Type.QUANTITY, Arrays.asList(SearchConstants.Modifier.MISSING));
                }
            });

    /**
     * ascending and descending enumeration
     * 
     * @author markd
     *
     */
    public enum SortDirection {
        ASCENDING("asc"), DESCENDING("desc");

        private String value = null;

        SortDirection(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }

        public static SortDirection fromValue(String value) {
            for (SortDirection direction : SortDirection.values()) {
                if (direction.value.equalsIgnoreCase(value)) {
                    return direction;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }

    /**
     * Prefixes for Search parameters
     * 
     * @author markd
     *
     */
    public enum Prefix {
        EQ("eq"), NE("ne"), GT("gt"), LT("lt"), GE("ge"), LE("le"), SA("sa"), EB("eb"), AP("ap");

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
            throw new IllegalArgumentException("No constant with value " + value + " found.");
        }
    }

    /**
     * Types
     * 
     * @author markd
     *
     */
    public enum Type {
        NUMBER("number"), DATE("date"), STRING("string"), TOKEN("token"), REFERENCE("reference"), QUANTITY("quantity"), URI("uri");

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
            throw new IllegalArgumentException("No constant with value " + value + " found.");
        }
    }

    /**
     * Modifiers
     * 
     * @author markd
     *
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
        TYPE("[type]");

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
            throw new IllegalArgumentException("No constant with value " + value + " found.");
        }

        public static boolean isSupported(Modifier modifier) {
            return modifier.equals(Modifier.ABOVE) || modifier.equals(Modifier.BELOW) || modifier.equals(Modifier.CONTAINS) || modifier.equals(EXACT)
                    || modifier.equals(Modifier.NOT) || modifier.equals(Modifier.MISSING);
        }
    }
}
