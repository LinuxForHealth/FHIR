/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.persistence.jdbc;

import java.util.HashMap;

import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;

public class JDBCConstants {
    // Constants for the IBM FHIR Server database schema
    public static final String STR_VALUE = "STR_VALUE";
    public static final String STR_VALUE_LCASE = "STR_VALUE_LCASE";
    public static final String TOKEN_VALUE = "TOKEN_VALUE";
    public static final String CODE_SYSTEM_ID = "CODE_SYSTEM_ID";
    public static final String CODE = "CODE";
    public static final String NUMBER_VALUE = "NUMBER_VALUE";
    public static final String QUANTITY_VALUE = "QUANTITY_VALUE";
    public static final String QUANTITY_VALUE_LOW = "QUANTITY_VALUE_LOW";
    public static final String QUANTITY_VALUE_HIGH = "QUANTITY_VALUE_HIGH";
    public static final String DATE_VALUE = "DATE_VALUE";
    public static final String DATE_START = "DATE_START";
    public static final String DATE_END = "DATE_END";
    public static final String LATITUDE_VALUE = "LATITUDE_VALUE";
    public static final String LONGITUDE_VALUE = "LONGITUDE_VALUE";
    
    // Generic SQL query string constants
    public static final String DOT = ".";
    public static final String WHERE = " WHERE ";
    public static final String PARAMETER_TABLE_ALIAS = "pX";
    public static final String LEFT_PAREN = "(";
    public static final String RIGHT_PAREN = ")";
    public static final String AND = " AND ";
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
    public static final String COMBINED_RESULTS = " COMBINED_RESULTS";
    
    /**
     * Maps Parameter modifiers to SQL operators.
     */
    public static HashMap<Modifier, JDBCOperator> modifierMap;

    /**
     * Maps Parameter value prefix operators to SQL operators.
     */
    public static HashMap<Prefix, JDBCOperator> prefixOperatorMap;

    static {
        modifierMap = new HashMap<>();
        modifierMap.put(Modifier.ABOVE, JDBCOperator.GT);
        modifierMap.put(Modifier.BELOW, JDBCOperator.LT);
        modifierMap.put(Modifier.CONTAINS, JDBCOperator.LIKE);
        modifierMap.put(Modifier.EXACT, JDBCOperator.EQ);
        modifierMap.put(Modifier.NOT, JDBCOperator.NE);

        prefixOperatorMap = new HashMap<>();
        prefixOperatorMap.put(Prefix.EQ, JDBCOperator.EQ);
        prefixOperatorMap.put(Prefix.GE, JDBCOperator.GTE);
        prefixOperatorMap.put(Prefix.GT, JDBCOperator.GT);
        prefixOperatorMap.put(Prefix.LE, JDBCOperator.LTE);
        prefixOperatorMap.put(Prefix.LT, JDBCOperator.LT);
        prefixOperatorMap.put(Prefix.NE, JDBCOperator.NE);
        prefixOperatorMap.put(Prefix.SA, JDBCOperator.GT);
        prefixOperatorMap.put(Prefix.EB, JDBCOperator.LT);
        prefixOperatorMap.put(Prefix.AP, JDBCOperator.EQ);
    }
    
    /**
     * An enumeration of SQL query operators.
     */
    public static enum JDBCOperator {
        EQ(" = "), 
        LIKE(" LIKE "), 
        IN(" IN "), 
        LT(" < "), 
        LTE(" <= "),
        GT(" > "), 
        GTE(" >= "),
        NE(" <> "), 
        OR(" OR "),
        AND(" AND ");
        
        private String value = null;
        
        JDBCOperator(String value) {
            this.value = value;
        }
        
        public String value() {
            return value;
        }
        
        public static JDBCOperator fromValue(String value) {
            for (JDBCOperator operator : JDBCOperator.values()) {
                if (operator.value.equalsIgnoreCase(value)) {
                    return operator;
                }
            }
            throw new IllegalArgumentException("No constant with value " + value + " found.");
        }
    }
}
