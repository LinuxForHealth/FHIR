/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.persistence.jdbc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;

public class JDBCConstants {
    /**
     * The maximum number of components allowed within a search parameter of type
     * composite
     */
    public static final int MAX_NUM_OF_COMPOSITE_COMPONENTS = 3;

    // Constants for the IBM FHIR Server database schema
    public static final String STR_VALUE = "STR_VALUE";
    public static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    public static final String TOKEN_VALUE = "TOKEN_VALUE";
    public static final String COMMON_TOKEN_VALUE_ID = "COMMON_TOKEN_VALUE_ID";
    public static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    public static final String CODE = "CODE";
    public static final String NUMBER_VALUE = "NUMBER_VALUE";
    public static final String QUANTITY_VALUE = "QUANTITY_VALUE";
    public static final String _LOW = "_LOW";
    public static final String _HIGH = "_HIGH";
    public static final String DATE_START = "DATE_START";
    public static final String DATE_END = "DATE_END";
    public static final String LATITUDE_VALUE = "LATITUDE_VALUE";
    public static final String LONGITUDE_VALUE = "LONGITUDE_VALUE";
    public static final String _RESOURCES = "_RESOURCES";
    public static final String _LOGICAL_RESOURCES = "_LOGICAL_RESOURCES";
    public static final String RESOURCE_ID = "RESOURCE_ID";
    public static final String RESOURCE_TYPE_ID = "RESOURCE_TYPE_ID";
    public static final String REF_LOGICAL_RESOURCE_ID = "REF_LOGICAL_RESOURCE_ID";
    public static final String LOGICAL_ID = "LOGICAL_ID";
    public static final String LOGICAL_RESOURCE_ID = "LOGICAL_RESOURCE_ID";
    public static final String CURRENT_RESOURCE_ID = "CURRENT_RESOURCE_ID";
    public static final String PARAMETER_NAME_ID = "PARAMETER_NAME_ID";
    public static final String IS_DELETED_NO = "IS_DELETED = 'N'";
    public static final String IS_DELETED = "IS_DELETED";

    // Generic SQL query string constants
    public static final String DOT = ".";
    public static final char DOT_CHAR = '.';
    public static final String WHERE = " WHERE ";
    public static final String PARAMETER_TABLE_ALIAS = "pX";
    public static final String PARAMETER_TABLE_NAME_PLACEHOLDER = "pX_TABLE_NAME";
    public static final String LEFT_PAREN = "(";
    public static final String RIGHT_PAREN = ")";
    public static final String BIND_VAR = "?";
    public static final String PERCENT_WILDCARD = "%";
    public static final String UNDERSCORE_WILDCARD = "_";
    public static final String ESCAPE_CHAR = "+";
    public static final String ESCAPE_UNDERSCORE = ESCAPE_CHAR + "_";
    public static final String ESCAPE_PERCENT = ESCAPE_CHAR + PERCENT_WILDCARD;
    public static final String ESCAPE_EXPR = " ESCAPE '" + ESCAPE_CHAR + "'";
    public static final String WHEN = " WHEN ";
    public static final String THEN = " THEN ";
    public static final String END = " END";
    public static final char SPACE = ' ';
    public static final String FROM = " FROM ";
    public static final String UNION = " UNION ALL ";
    public static final String ON = " ON ";
    public static final String JOIN = " JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String EXISTS = " EXISTS ";
    public static final String NOT_EXISTS = " NOT EXISTS ";
    public static final String NOT = " NOT ";
    public static final String COMBINED_RESULTS = ") COMBINED_RESULTS";
    public static final String COMMA = " , ";
    public static final char COMMA_CHAR = ',';
    public static final char QUOTE = '\'';
    public static final char PATH_CHAR = '/';
    public static final String SELECT = " SELECT ";
    public static final String AS = " AS ";
    public static final String LIMIT = " LIMIT ";
    public static final String OFFSET = " OFFSET ";
    public static final String FETCH_FIRST = " FETCH FIRST ";
    public static final String FETCH_NEXT = " FETCH NEXT ";
    public static final String ROWS = " ROWS ";
    public static final String ROWS_ONLY = " ROWS ONLY ";

    // JDBC Operators
    public static final String EQ = " = ";
    public static final String LIKE = " LIKE ";
    public static final String IN =" IN ";
    public static final String LT = " < ";
    public static final String LTE = " <= ";
    public static final String GT = " > ";
    public static final String GTE = " >= ";
    public static final String NE = " <> ";
    public static final String OR = " OR ";
    public static final String AND = " AND ";

    // ASC/DESC
    public static final String ORDER_BY = " ORDER BY ";
    public static final String ASCENDING = "ASC";
    public static final String DESCENDING = "DESC";

    public static final String DEFAULT_ORDERING = " ORDER BY LOGICAL_RESOURCE_ID ASC ";
    public static final String DEFAULT_ORDERING_WITH_TABLE = " ORDER BY R.RESOURCE_ID ASC ";

    // MIN / MAX
    public static final String MAX = "MAX";
    public static final String MIN = "MIN";

    // Db2 optimization hints
    public static final String SEARCH_REOPT = "search.reopt";

    // Default code_system_id value
    public static final String DEFAULT_TOKEN_SYSTEM = "default-token-system";

    /**
     * This Calendar object is not thread-safe! Use CalendarHelper#getCalendarForUTC() instead.
     */
    // DO NOT USE
    // public static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    // DO NOT USE

    /**
     * Maps search parameter types to the currently supported list of modifiers for that type.
     */
    public static final Map<Type, List<Modifier>> supportedModifiersMap;

    /**
     * Maps Parameter modifiers to SQL operators.
     */
    public static final Map<Modifier, String> modifierOperatorMap;

    static {
        supportedModifiersMap = new HashMap<>();
        supportedModifiersMap.put(Type.STRING, Arrays.asList(Modifier.EXACT, Modifier.CONTAINS, Modifier.MISSING));
        supportedModifiersMap.put(Type.REFERENCE, Arrays.asList(Modifier.TYPE, Modifier.MISSING, Modifier.IDENTIFIER));
        supportedModifiersMap.put(Type.URI, Arrays.asList(Modifier.BELOW, Modifier.ABOVE, Modifier.MISSING));
        supportedModifiersMap.put(Type.TOKEN, Arrays.asList(Modifier.MISSING, Modifier.NOT, Modifier.OF_TYPE,
                Modifier.IN, Modifier.NOT_IN, Modifier.TEXT, Modifier.ABOVE, Modifier.BELOW));
        supportedModifiersMap.put(Type.NUMBER, Arrays.asList(Modifier.MISSING));
        supportedModifiersMap.put(Type.DATE, Arrays.asList(Modifier.MISSING));
        supportedModifiersMap.put(Type.QUANTITY, Arrays.asList(Modifier.MISSING));
        supportedModifiersMap.put(Type.COMPOSITE, Arrays.asList(Modifier.MISSING));
        supportedModifiersMap.put(Type.SPECIAL, Arrays.asList(Modifier.MISSING));

        modifierOperatorMap = new HashMap<>();
        modifierOperatorMap.put(Modifier.ABOVE, GT);
        modifierOperatorMap.put(Modifier.BELOW, LT);
        modifierOperatorMap.put(Modifier.CONTAINS, LIKE);
        modifierOperatorMap.put(Modifier.EXACT, EQ);
        modifierOperatorMap.put(Modifier.NOT, EQ); // EQ since it will be within a "WHERE NOT EXISTS" subquery
        modifierOperatorMap.put(Modifier.TEXT, LIKE);
    }

    private JDBCConstants() {
        // Hide the constructor
    }
}
