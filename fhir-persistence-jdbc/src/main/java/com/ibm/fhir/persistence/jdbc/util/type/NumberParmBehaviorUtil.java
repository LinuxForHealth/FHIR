/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.JDBCQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * Number Parameter Behavior Util encapsulates the logic specific to Prefix
 * treatment of the Search queries related to numbers, and the implied ranges
 * therein.
 */
public class NumberParmBehaviorUtil {
    protected static final BigDecimal FACTOR = new BigDecimal(".1");

    private NumberParmBehaviorUtil() {
        // No operation
    }

    public static void executeBehavior(StringBuilder whereClauseSegment, QueryParameter queryParm,
            List<Object> bindVariables, Class<?> resourceType, String tableAlias, JDBCQueryBuilder queryBuilder)
            throws FHIRPersistenceException {
        // Start the Clause 
        // Query: AND (
        whereClauseSegment.append(AND).append(LEFT_PAREN);

        // Process each parameter value in the query parameter
        boolean parmValueProcessed = false;
        Set<String> seen = new HashSet<>();
        for (QueryParameterValue value : queryParm.getValues()) {

            Prefix prefix = value.getPrefix();

            // Default to EQ
            if (prefix == null) {
                prefix = Prefix.EQ;
            }
            BigDecimal originalNumber = value.getValueNumber();

            // seen is used to optimize against a repeated value passed in. 
            // the hash must use the prefix and original number. 
            String hash = prefix.value() + originalNumber.toPlainString();
            if (!seen.contains(hash)) {
                seen.add(hash);

                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    // ) OR (
                    whereClauseSegment.append(RIGHT_PAREN)
                            .append(OR)
                            .append(LEFT_PAREN);
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition 
                    parmValueProcessed = true;
                }

                // Branch behavior based on the type of prefix. 
                // EQ/NE/AP have special handling
                switch (prefix) {
                case EQ:
                    // Not an Integer search. 
                    // Bounds are based on precision.
                    bindVariables.add(generateLowerBound(originalNumber));
                    bindVariables.add(generateUpperBound(originalNumber));

                    // <CODE>BASIC_NUMBER_VALUE > ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(GT).append(BIND_VAR).append(AND);
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(LTE).append(BIND_VAR);
                    break;
                case NE:
                    // ne - not equals is a specific not in range search.
                    // Bounds are based on precision.
                    bindVariables.add(generateLowerBound(originalNumber));
                    bindVariables.add(generateUpperBound(originalNumber));

                    // <CODE>BASIC_NUMBER_VALUE <= ? OR BASIC_NUMBER_VALUE > ?</CODE> 
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(LTE).append(BIND_VAR);
                    whereClauseSegment.append(OR);
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(GT).append(BIND_VAR);
                    break;
                case AP:
                    // ap - approximate
                    // -10% of the Lower Bound
                    BigDecimal lowerBound = generateLowerBound(originalNumber);
                    BigDecimal upperBound = generateUpperBound(originalNumber);
                    BigDecimal factor = originalNumber.multiply(FACTOR);
                    bindVariables.add(lowerBound.subtract(factor));
                    // +10% of the UPPER Bound
                    bindVariables.add(upperBound.add(factor));

                    // <CODE>BASIC_NUMBER_VALUE >= ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(GTE).append(BIND_VAR);
                    whereClauseSegment.append(AND);
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(LTE).append(BIND_VAR);
                    break;
                default:
                    // gt, lt, ge, le, sa, eb
                    // take the default behavior.
                    // Build this piece: p1.value_string {operator} search-attribute-value
                    String operator = EQ;
                    switch (prefix) {
                    case EB:
                        // EB - Ends Before
                        operator = LT;
                        break;
                    case SA:
                        // SA - Starts After
                        operator = GT;
                        break;
                    case GE:
                        // GE - Greater Than Equal
                        operator = GTE;
                        break;
                    case GT:
                        // GT - Greater Than
                        operator = GT;
                        break;
                    case LE:
                        // LE - Less Than Equal
                        operator = LTE;
                        break;
                    case LT:
                    default:
                        // LT - Less Than
                        operator = LT;
                        break;
                    }

                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE).append(operator)
                            .append(BIND_VAR);
                    bindVariables.add(originalNumber);
                    break;
                }
            }
        }

        // End the Clause started above, and closes the parameter expression. 
        // Query: )) 
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
    }

    public static BigDecimal generateLowerBound(BigDecimal original) {

        int factor = calculateSignificantFigures(original);
        if (factor < 0) {
            factor = Math.abs(factor) - 1;
        } else {
            factor = original.scale() + 1;

            // Mutate it to the ORIGINAL string
            String t = "" + original;
            int loc = t.indexOf('E');
            if (loc > -1) {
                factor = -1 * (loc - 2);
            }
        }

        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * factor);
        return original.subtract(scaleFactor);
    }

    public static BigDecimal generateUpperBound(BigDecimal original) {
        int factor = calculateSignificantFigures(original);
        if (factor < 0) {
            factor = Math.abs(factor) - 1;
        } else {
            factor = original.scale() + 1;

            // Mutate it to the ORIGINAL string
            String t = "" + original;
            int loc = t.indexOf('E');
            if (loc > -1) {
                factor = -1 * (loc - 2);
            }
        }
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * factor);
        return original.add(scaleFactor);
    }

    /**
     * calculates the significant figures
     * based on
     * <a href="https://en.wikipedia.org/wiki/Significant_figures">Significant
     * Figures</a>
     * 
     * @param original
     * @return
     */
    public static int calculateSignificantFigures(BigDecimal original) {
        int count = original.precision();
        if (original.scale() <= 0) {
            // Common pattern is to strip zeros... <code>.stripTrailingZeros()</code> 
            // We don't per the pattern in FHIR. 
            count += original.scale();
        }
        return count;
    }
}