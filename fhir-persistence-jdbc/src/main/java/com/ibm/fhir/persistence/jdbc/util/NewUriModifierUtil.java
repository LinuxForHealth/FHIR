/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PATH_CHAR;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.query.Operator;
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

    /**
     * Private constructor
     */
    private NewUriModifierUtil() {
        // No Operation
    }

    /**
     * generates the uri:above query
     * @param expression
     * @param paramAlias
     * @param tableColumnName
     * @param searchValue
     * @param operator
     */
    public static void generateAboveValuesQuery(WhereFragment expression, String paramAlias, String tableColumnName, String searchValue,
        Operator operator) {
        boolean added = false;
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
                // values will be processed as bind-variables
                expression.col(paramAlias, tableColumnName).in(inList);
                added = true;
            }
        }

        if (!added) {
            // Just use given operator to compare with the given searchValue
            expression.col(paramAlias, tableColumnName).operator(operator).bind(searchValue);
        }
    }

    /**
     * generates the uri:below query
     * @param expression
     * @param tableColumnName
     * @param value1
     * @param value2
     */
    public static void generateBelowValuesQuery(WhereFragment expression,
            String tableColumnName, String value1, String value2) {
        // uri:below
        // SQL:
        // <pre>
        // TABLE.MY_STR_VALUES = ? )  OR  ( TABLE.MY_STR_VALUES LIKE ?
        // </pre>
        expression.and().leftParen()
            .col(tableColumnName).eq(bind(value1))
            .or(tableColumnName).like(bind(value2))
            .rightParen();
    }
}