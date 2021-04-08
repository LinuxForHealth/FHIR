/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.bind;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PATH_CHAR;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.query.WhereFragment;

/**
 * Encapsulates logic for building an expression for handling URI matching
 * according to the FHIR R4 specification.
 * Modifier: uri:above
 * <br>
 * <a href="https://www.hl7.org/fhir/search.html#uri">FHIR Specification: Search
 * uri:above</a>
 */
public class NewUriModifierUtil {

    private NewUriModifierUtil() {
        // No Operation
    }

    /**
     * generates the uri:above query
     *
     * @param searchValue
     * @param conditionalBuilder
     * @param tableColumnName
     * @return
     */
    public static void generateAboveValuesQuery(WhereFragment expression, String tableColumnName, String searchValue) {

        // If '://' exists, then find the location after the protocol
        // Otherwise pass through, it's going to be treated as-is
        int protocolLoc = searchValue.indexOf("://");
        if (protocolLoc != -1) {
            // We found the start, but we want the end of the protocol
            protocolLoc += 3;

            List<String> inList = new ArrayList<>();

            StringBuilder urlSegments = new StringBuilder();
            urlSegments.append(searchValue.substring(0, protocolLoc));

            // Ignore query parameters ?name=value
            String[] pathArgs = searchValue.substring(protocolLoc).split("\\?")[0].split("/");
            for (int i = 0; i < pathArgs.length; i++) {
                urlSegments.append(pathArgs[i]);
                inList.add(urlSegments.toString());

                // Add for the NEXT path element.
                urlSegments.append(PATH_CHAR);
            }

            if (inList.size() > 0) {
                expression.and(tableColumnName).in(inList);
            }
        }
    }

    /**
     * generates the uri:below query
     *
     * @param conditionalBuilder
     * @param tableColumnName
     */
    public static void generateBelowValuesQuery(WhereFragment whereAdapter,
            String tableColumnName, String value1, String value2) {
        // uri:below
        // SQL:
        // <pre>
        // TABLE.MY_STR_VALUES = ? )  OR  ( TABLE.MY_STR_VALUES LIKE ?
        // </pre>
        whereAdapter.and().leftParen()
            .col(tableColumnName).eq(bind(value1))
            .or(tableColumnName).like(bind(value2))
            .rightParen();
    }
}