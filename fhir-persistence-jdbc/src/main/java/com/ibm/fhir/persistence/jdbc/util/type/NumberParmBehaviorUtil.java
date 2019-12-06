/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
import com.ibm.fhir.persistence.jdbc.util.JDBCQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;
import com.ibm.fhir.search.valuetypes.ValueTypesFactory;

/**
 * Number Parameter Behavior Util encapsulates the logic specific to Prefix
 * treatment of the Search queries related to numbers, and the implied ranges
 * therein.
 */
public class NumberParmBehaviorUtil {
    private static final Logger log = java.util.logging.Logger.getLogger(NumberParmBehaviorUtil.class.getName());

    private static final BigDecimal LOWER_BOUND = new BigDecimal(".9");
    private static final BigDecimal UPPER_BOUND = new BigDecimal("1.1");

    private NumberParmBehaviorUtil() {
        // No operation
    }

    public static void executeBehavior(StringBuilder whereClauseSegment, Parameter queryParm,
            List<Object> bindVariables, Class<?> resourceType, String tableAlias, JDBCQueryBuilder queryBuilder)
            throws FHIRPersistenceException {
        // Start the Clause 
        // Query: AND (
        whereClauseSegment.append(AND).append(LEFT_PAREN);

        // Process each parameter value in the query parameter
        boolean parmValueProcessed = false;
        Set<String> seen = new HashSet<>();
        for (ParameterValue value : queryParm.getValues()) {

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
                // Check for valid conditions and return if it needs to be treated as an INTEGER:
                boolean isInteger = checkIntegerSearchWithSaEb(prefix, resourceType, queryParm, originalNumber);

                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    // ) OR (
                    whereClauseSegment.append(RIGHT_PAREN)
                            .append(JDBCOperator.OR.value())
                            .append(LEFT_PAREN);
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition 
                    parmValueProcessed = true;
                }

                // Branch behavior based on the type of prefix. 
                // EQ/NE/AP have special handling
                switch (prefix) {
                case EQ:
                    // eq - equals is a specific range search.
                    if (isInteger) {
                        bindVariables.add(originalNumber);
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.EQ.value())
                                .append(BIND_VAR);

                    } else {
                        // Not an Integer search. 
                        // Bounds are based on precision.
                        bindVariables.add(generateLowerBound(originalNumber));
                        bindVariables.add(generateUpperBound(originalNumber));

                        // <CODE>BASIC_NUMBER_VALUE > ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.GT.value())
                                .append(BIND_VAR);
                        whereClauseSegment.append(JDBCConstants.SPACE).append(JDBCOperator.AND)
                                .append(JDBCConstants.SPACE);
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.LTE.value())
                                .append(BIND_VAR);
                    }

                    break;
                case NE:
                    // ne - not equals is a specific not in range search.
                    if (isInteger) {
                        bindVariables.add(originalNumber);
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.NE.value())
                                .append(BIND_VAR);
                    } else {
                        // Bounds are based on precision.

                        bindVariables.add(generateLowerBound(originalNumber));
                        bindVariables.add(generateUpperBound(originalNumber));

                        // <CODE>BASIC_NUMBER_VALUE <= ? OR BASIC_NUMBER_VALUE > ?</CODE> 
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.LTE.value())
                                .append(BIND_VAR);
                        whereClauseSegment.append(JDBCConstants.SPACE).append(JDBCOperator.OR)
                                .append(JDBCConstants.SPACE);
                        whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(JDBCOperator.GT.value())
                                .append(BIND_VAR);
                    }
                    break;
                case AP:
                    // ap - approximate
                    // -10% of the Lower Bound
                    BigDecimal lowerBound = generateLowerBound(originalNumber);
                    BigDecimal upperBound = generateUpperBound(originalNumber);
                    bindVariables.add(lowerBound.multiply(LOWER_BOUND));
                    // +10% of the UPPER Bound
                    bindVariables.add(upperBound.multiply(UPPER_BOUND));

                    // <CODE>BASIC_NUMBER_VALUE >= ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                    whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(JDBCOperator.GTE.value())
                            .append(BIND_VAR);
                    whereClauseSegment.append(JDBCConstants.SPACE).append(JDBCOperator.AND).append(JDBCConstants.SPACE);
                    whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(JDBCOperator.LTE.value())
                            .append(BIND_VAR);
                    break;
                default:
                    // gt, lt, ge, le, sa, eb
                    // take the default behavior.
                    // Build this piece: p1.value_string {operator} search-attribute-value
                    JDBCOperator operator = queryBuilder.getPrefixOperator(value);
                    whereClauseSegment.append(tableAlias + DOT).append(NUMBER_VALUE).append(operator.value())
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

    /**
     * per the specification we DO NOT process EB/SA integers.
     * <a href="https://hl7.org/fhir/search.html#prefix">FHIR Specification: Search
     * - Prefixes</a>
     * <code> 
     * sa (starts-after) and eb (ends-before) are not used with integer values but are used for decimals. 
     * </code>
     * 
     * @param prefix
     * @param resourceType
     * @param queryParm
     * @param originalNumber
     * @return boolean indicating that an integer search is being run.
     * @throws FHIRPersistenceException
     */
    public static boolean checkIntegerSearchWithSaEb(Prefix prefix, Class<?> resourceType, Parameter queryParm,
            BigDecimal originalNumber)
            throws FHIRPersistenceException {
        boolean isIntegerSearch = false;
        try {
            isIntegerSearch = ValueTypesFactory.getValueTypesProcessor().isIntegerSearch(resourceType, queryParm);
        } catch (FHIRSearchException e) {
            log.log(Level.INFO, "Caught exception while checking the value types for parameter '"
                    + queryParm.getCode() + "'; continuing...", e);
            // do nothing
        }

        if (isIntegerSearch) {
            if (prefix == Prefix.EB || prefix == Prefix.SA) {
                throw new FHIRPersistenceException(
                        "Search prefixes '" + Prefix.EB.value() + "' and '" + Prefix.SA.value()
                                + "' are not supported for integer searches.");
            } else {
                /*
                 * Per Specification: <br>
                 * When a number search is used against a resource element that stores a simple
                 * integer (e.g. ImmunizationRecommendation.recommendation.doseNumber), and the
                 * search parameter is not expressed using the exponential forms, and does not
                 * include any non-zero digits after a decimal point, the significance issues
                 * cancel out and searching is based on exact matches. Note that if there are
                 * non-zero digits after a decimal point, there cannot be any matches
                 */

                // Conditions:
                // We know the target is an integer.
                // if integer and ! exponential form ('E') and no non-zero
                // if indexOf 'E' and indexOf '.' are zero... then it's a specific equals search

                // we need to mutate into a string to test the conditions
                String num = "" + originalNumber;
                if (num.indexOf('E') > -1 || num.indexOf('.') > -1) {
                    isIntegerSearch = false;
                }
            }
        }

        return isIntegerSearch;
    }
}