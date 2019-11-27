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
public class AboveUtil {
    public static final char PATH_CHAR = '/';
    public static final char SPACE_CHAR = ' ';
    public static final char COMMA_CHAR = ',';
    
    private AboveUtil() {
        // No Operation
    }

    public static List<String> generateAboveValuesQuery(String searchValue, StringBuilder conditionalBuilder) {
        List<String> queryArgs = new ArrayList<String>();

        // If '://' exists, then find the location after the protocol
        // Otherwise pass through, it's going to be treated as-is
        int protocolLoc = searchValue.indexOf("://");
        if (protocolLoc != -1) {
            // We found the start, but we want the end of the protocol
            protocolLoc += 3;

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
        }
        return queryArgs;
    }
}
