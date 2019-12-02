/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;

/**
 * Modifier: uri:above
 * <br>
 * <a href="https://www.hl7.org/fhir/search.html#uri">FHIR Specification: Search uri:above</a>
 */
public class UriModifierUtil {
    public static final char PATH_CHAR = '/';
    public static final char SPACE_CHAR = ' ';
    public static final char COMMA_CHAR = ',';
    
    private UriModifierUtil() {
        // No Operation
    }

    /**
     *  generates the uri:above query
     * @param searchValue
     * @param conditionalBuilder
     * @param tableColumnName
     * @return
     */
    public static List<String> generateAboveValuesQuery(String searchValue, StringBuilder conditionalBuilder,
            String tableColumnName) {
        List<String> queryArgs = new ArrayList<>();

        // If '://' exists, then find the location after the protocol
        // Otherwise pass through, it's going to be treated as-is
        int protocolLoc = searchValue.indexOf("://");
        if (protocolLoc != -1) {
            // We found the start, but we want the end of the protocol
            protocolLoc += 3;
            
            int position = conditionalBuilder.length();
            
            // Start the Conditional Builder
            conditionalBuilder.append(SPACE_CHAR).append(JDBCOperator.IN).append(SPACE_CHAR).append(JDBCConstants.LEFT_PAREN);

            StringBuilder urlSegments = new StringBuilder();
            urlSegments.append(searchValue.substring(0, protocolLoc));

            // Ignore query parameters ?name=value
            String[] pathArgs = searchValue.substring(protocolLoc).split("\\?")[0].split("/");
            for (int i = 0; i < pathArgs.length; i++) {
                conditionalBuilder.append(SPACE_CHAR).append(BIND_VAR).append(COMMA_CHAR);

                urlSegments.append(pathArgs[i]);
                queryArgs.add(urlSegments.toString());

                // Add for the NEXT path element.
                urlSegments.append(PATH_CHAR);
            }

            // Close out the Conditional Builder
            conditionalBuilder.deleteCharAt(conditionalBuilder.length() - 1)
                    .append(' ')
                    .append(JDBCConstants.RIGHT_PAREN);
            
            if(!queryArgs.isEmpty()) {
                conditionalBuilder.insert(position, tableColumnName);
            }
        }
        return queryArgs;
    }
    
    /**
     * generates the uri:below query
     * @param conditionalBuilder
     * @param tableColumnName
     */
    public static void generateBelowValuesQuery(StringBuilder conditionalBuilder,
            String tableColumnName) {
        // uri:below
        // SQL: 
        // <pre> 
        // TABLE.MY_STR_VALUES = ? )  OR  ( TABLE.MY_STR_VALUES LIKE ?
        // </pre>
        
        conditionalBuilder.append(tableColumnName).append(SPACE_CHAR).append('=').append(SPACE_CHAR)
            .append(BIND_VAR).append(SPACE_CHAR).append(SPACE_CHAR).append(JDBCOperator.OR.value())
            .append(SPACE_CHAR).append(SPACE_CHAR).append(tableColumnName).append(" LIKE ")
            .append(BIND_VAR);
        
    }
}
