/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._HIGH;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOW;

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
@Deprecated
public class NumberParmBehaviorUtil {
    protected static final BigDecimal FACTOR = new BigDecimal(".1");

    private NumberParmBehaviorUtil() {
        // No operation
    }

    /**
     * Add the filter predicate logic to the given whereClauseSegment
     * @param whereClauseSegment
     * @param queryParm
     * @param bindVariables
     * @param resourceType
     * @param tableAlias
     * @param queryBuilder
     * @throws FHIRPersistenceException
     */
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
                    whereClauseSegment.append(RIGHT_PAREN).append(OR).append(LEFT_PAREN);
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition
                    parmValueProcessed = true;
                }

                addValue(whereClauseSegment, bindVariables, tableAlias, NUMBER_VALUE, prefix, value.getValueNumber());
            }
        }

        // End the Clause started above, and closes the parameter expression.
        // Query: ))
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
    }

    /**
     * Append the condition and bind the variables according to the semantics of the
     * passed prefix
     * adds the value to the whereClause.
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnBase
     * @param prefix
     * @param value
     */
    public static void addValue(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
            String columnBase, Prefix prefix, BigDecimal value) {

        BigDecimal lowerBound = generateLowerBound(value);
        BigDecimal upperBound = generateUpperBound(value);

        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the range of the search value does not overlap with the range of the target value,
            // and the range above the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, bindVariables, tableAlias, columnBase + _HIGH,
                    LT, lowerBound);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, bindVariables, tableAlias, columnBase + _LOW,
                    GT, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, columnBase, GTE, value, lowerBound);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, columnBase, GT, value, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, columnBase, LTE, value, upperBound);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, columnBase, LT, value, lowerBound);
            break;
        case AP:
            // AP - Approximate - Relative
            // the range of the search value overlaps with the range of the target value
            buildApproxRangeClause(whereClauseSegment, bindVariables, tableAlias, columnBase, lowerBound, upperBound, value);
            break;
        case NE:
            // NE:  Upper and Lower Bounds - Range Based Search
            // the range of the search value does not fully contain the range of the target value
            buildNotEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, columnBase, lowerBound, upperBound);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds - Range Based Search
            // the range of the search value fully contains the range of the target value
            buildEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, columnBase, lowerBound, upperBound);
            break;
        }
    }

    /**
     * the build common clause considers _VALUE_*** and _VALUE when querying the
     * data.
     * <br>
     * The data should not result in a duplication as the OR condition short
     * circuits double matches.
     * If one exists, great, we'll return it, else we'll peek at the other column.
     * <br>
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnBase
     * @param operator
     * @param value
     * @param bound
     */
    public static void buildCommonClause(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
            String columnBase, String operator, BigDecimal value, BigDecimal bound) {

        String orEqualsOperator = null;
        if (GTE.equals(operator)) {
            operator = GT;
            orEqualsOperator = GTE;
        } else if (LTE.equals(operator)) {
            operator = LT;
            orEqualsOperator = LTE;
        }
        boolean gtOp = GT.equals(operator);

        whereClauseSegment
            .append(LEFT_PAREN)
                .append(tableAlias).append(DOT).append(gtOp ? columnBase + _HIGH : columnBase + _LOW).append(operator).append(BIND_VAR)
                .append(OR)
                .append(tableAlias).append(DOT).append(gtOp ? columnBase + _LOW : columnBase + _HIGH)
                .append(orEqualsOperator != null ? orEqualsOperator : operator).append(BIND_VAR)
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase).append(" IS NOT NULL")
                .append(AND)
                .append(tableAlias).append(DOT).append(gtOp ? columnBase + _HIGH : columnBase + _LOW).append(" IS NULL")
            .append(RIGHT_PAREN);

        bindVariables.add(value);
        bindVariables.add(orEqualsOperator != null ? bound : value);
    }

    /**
     * the build eb or sa clause considers only _VALUE_LOW and _VALUE_HIGH
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnNameLowOrHigh
     * @param operator
     * @param bound
     */
    public static void buildEbOrSaClause(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
            String columnNameLowOrHigh, String operator, BigDecimal bound) {
        whereClauseSegment.append(tableAlias).append(DOT).append(columnNameLowOrHigh).append(operator).append(BIND_VAR);
        bindVariables.add(bound);
    }

    /**
     * Add the equals range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     */
    public static void buildEqualsRangeClause(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
           String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {
        whereClauseSegment
            .append(LEFT_PAREN)
                .append(tableAlias).append(DOT).append(columnBase + _LOW).append(GTE).append(BIND_VAR)
                .append(AND)
                .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(LTE).append(BIND_VAR)
            .append(RIGHT_PAREN);

        bindVariables.add(lowerBound);
        bindVariables.add(upperBound);
    }

    /**
     * Add the approx range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     * @param value
     */
    public static void buildApproxRangeClause(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound, BigDecimal value) {
        BigDecimal factor = value.multiply(FACTOR);
        BigDecimal approximateLowerBound = lowerBound.subtract(factor);
        BigDecimal approximateUpperBound = upperBound.add(factor);

        whereClauseSegment
            .append(LEFT_PAREN)
                .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(GTE).append(BIND_VAR)
                .append(AND)
                .append(tableAlias).append(DOT).append(columnBase + _LOW).append(LTE).append(BIND_VAR)
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase).append(" IS NULL")
                .append(AND)
                .append(LEFT_PAREN)
                    .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(GTE).append(BIND_VAR)
                    .append(AND)
                    .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(LTE).append(BIND_VAR)
                    .append(OR)
                    .append(tableAlias).append(DOT).append(columnBase + _LOW).append(GTE).append(BIND_VAR)
                    .append(AND)
                    .append(tableAlias).append(DOT).append(columnBase + _LOW).append(LTE).append(BIND_VAR)
                .append(RIGHT_PAREN)
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase).append(" IS NOT NULL")
                .append(AND)
                .append(LEFT_PAREN)
                    .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(GTE).append(BIND_VAR)
                    .append(AND)
                    .append(tableAlias).append(DOT).append(columnBase + _LOW).append(" IS NULL")
                    .append(OR)
                    .append(tableAlias).append(DOT).append(columnBase + _LOW).append(LTE).append(BIND_VAR)
                    .append(AND)
                    .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(" IS NULL")
                .append(RIGHT_PAREN)
            .append(RIGHT_PAREN);

        bindVariables.add(approximateLowerBound);
        bindVariables.add(approximateUpperBound);
        bindVariables.add(approximateLowerBound);
        bindVariables.add(approximateUpperBound);
        bindVariables.add(approximateLowerBound);
        bindVariables.add(approximateUpperBound);
        bindVariables.add(approximateLowerBound);
        bindVariables.add(approximateUpperBound);
    }

    /**
     * Add the not-equals range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     */
    public static void buildNotEqualsRangeClause(StringBuilder whereClauseSegment, List<Object> bindVariables, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {
        whereClauseSegment
            .append(LEFT_PAREN)
                .append(tableAlias).append(DOT).append(columnBase + _LOW).append(LT).append(BIND_VAR)
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase + _LOW).append(" IS NULL")
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(GT).append(BIND_VAR)
                .append(OR)
                .append(tableAlias).append(DOT).append(columnBase + _HIGH).append(" IS NULL")
            .append(RIGHT_PAREN);

        bindVariables.add(lowerBound);
        bindVariables.add(upperBound);
    }

    /**
     * Generate the lower bound for the given value
     * @param original
     * @return
     */
    public static BigDecimal generateLowerBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.subtract(scaleFactor);
    }

    /**
     * Generate the upper bound for the given value
     * @param original
     * @return
     */
    public static BigDecimal generateUpperBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.add(scaleFactor);
    }
}