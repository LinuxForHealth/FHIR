/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util.type;

import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.EQ;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.GT;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.GTE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.LT;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.LTE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.NE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

import org.linuxforhealth.fhir.database.utils.query.Operator;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;

/**
 * Helper functions to obtain Operator values from QueryParameter objects
 */
public class OperatorUtil {

    /**
     * Private constructor to prevent instantiation
     */
    private OperatorUtil() {
    }
    /**
     * Get the operator we need to use for matching values for this parameter
     * @return
     */
    public static Operator getOperator(QueryParameter queryParameter) {
        String operator = LIKE;
        Modifier modifier = queryParameter.getModifier();

        // In the case where a URI, we need specific behavior/manipulation
        // so that URI defaults to EQ, unless... BELOW
        if (Type.URI.equals(queryParameter.getType())) {
            if (modifier != null && Modifier.BELOW.equals(modifier)) {
                operator = LIKE;
            } else {
                operator = EQ;
            }
        } else if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            operator = LIKE;
        }

        return convert(operator);
    }

    /**
     * Convert the operator string value to its enum equivalent
     * @param op
     * @return
     */
    public static Operator convert(String op) {
        final Operator result;
        switch (op) {
        case LIKE:
            result = Operator.LIKE;
            break;
        case EQ:
            result = Operator.EQ;
            break;
        case NE:
            result = Operator.NE;
            break;
        case LT:
            result = Operator.LT;
            break;
        case LTE:
            result = Operator.LTE;
            break;
        case GT:
            result = Operator.GT;
            break;
        case GTE:
            result = Operator.GTE;
            break;
        default:
            throw new IllegalArgumentException("Operator not supported: " + op);
        }

        return result;
    }


    /**
     * Map the Modifier in the passed Parameter to a supported query operator. If
     * the mapping results in the default
     * operator, override the default operator with the passed operator if the
     * passed operator is not null.
     *
     * @param queryParm
     *                        - A valid query Parameter.
     * @param defaultOverride
     *                        - An operator that should override the default
     *                        operator.
     * @return A supported operator.
     */
    public static Operator getOperator(QueryParameter queryParm, String defaultOverride) {
        String operator = defaultOverride;
        Modifier modifier = queryParm.getModifier();

        if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            if (defaultOverride != null) {
                operator = defaultOverride;
            } else {
                operator = LIKE;
            }
        }

        return convert(operator);
    }
}