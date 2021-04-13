/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._HIGH;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOW;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.WhereAdapter;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * Number Parameter Behavior Util encapsulates the logic specific to Prefix
 * treatment of the Search queries related to numbers, and the implied ranges
 * therein.
 * New implementation based on {@link Select} instead of {@link StringBuilder}
 */
public class NewNumberParmBehaviorUtil {
    protected static final BigDecimal FACTOR = new BigDecimal(".1");

    private NewNumberParmBehaviorUtil() {
        // No operation
    }

    public static void executeBehavior(WhereAdapter whereClauseSegment, QueryParameter queryParm,
            String tableAlias)
            throws FHIRPersistenceException {
        // If there are multiple parameter values, these will be OR'd together, so we
        // need to paren the whole statement.
        // Query: AND (
        whereClauseSegment.and().leftParen();

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
                    whereClauseSegment.or();
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition
                    parmValueProcessed = true;
                }

                addValue(whereClauseSegment, tableAlias, NUMBER_VALUE, prefix, value.getValueNumber());
            }
        }

        // ...)
        whereClauseSegment.rightParen();
    }

    /**
     * Append the condition and bind the variables according to the semantics of the
     * passed prefix
     * adds the value to the whereClause.
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param prefix
     * @param value
     */
    public static void addValue(WhereAdapter whereClauseSegment, String tableAlias,
            String columnBase, Prefix prefix, BigDecimal value) {

        BigDecimal lowerBound = generateLowerBound(value);
        BigDecimal upperBound = generateUpperBound(value);

        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the range of the search value does not overlap with the range of the target value,
            // and the range above the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, tableAlias, columnBase, columnBase + _HIGH,
                    LT, value, value);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    GT, value, value);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _HIGH,
                    GTE, value, value);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _HIGH,
                    GT, value, value);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    LTE, value, value);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    LT, value, value);
            break;
        case AP:
            // AP - Approximate - Relative
            // the range of the search value overlaps with the range of the target value
            buildApproxRangeClause(whereClauseSegment, tableAlias, columnBase, lowerBound, upperBound, value);
            break;
        case NE:
            // NE:  Upper and Lower Bounds - Range Based Search
            // the range of the search value does not fully contain the range of the target value
            buildNotEqualsRangeClause(whereClauseSegment, tableAlias, columnBase, lowerBound, upperBound);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds - Range Based Search
            // the range of the search value fully contains the range of the target value
            buildEqualsRangeClause(whereClauseSegment, tableAlias, columnBase, lowerBound, upperBound);
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
     * @param columnName
     * @param columnNameLowOrHigh
     * @param operator
     * @param value
     */
    public static void buildCommonClause(WhereAdapter whereClauseSegment, String tableAlias,
            String columnName, String columnNameLowOrHigh, String operator, BigDecimal value, BigDecimal bound) {

        // TODO get operator from string
        Operator op = null;
        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnName).operator(op).bind(value);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnNameLowOrHigh).operator(op).bind(bound);
        whereClauseSegment.rightParen();
    }

    /**
     * the build eb or sa clause considers only _VALUE_LOW and _VALUE_HIGH
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnName
     * @param columnNameLowOrHigh
     * @param operator
     * @param value
     */
    public static void buildEbOrSaClause(WhereAdapter whereClauseSegment, String tableAlias,
            String columnName, String columnNameLowOrHigh, String operator, BigDecimal value, BigDecimal bound) {

        // TODO
        Operator op = null;
        whereClauseSegment.col(tableAlias, columnNameLowOrHigh).operator(op).bind(value);
    }

    public static void buildEqualsRangeClause(WhereAdapter whereClauseSegment, String tableAlias,
           String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase).gte().bind(lowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase).lt().bind(upperBound);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).gte().bind(lowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).lte().bind(upperBound);
        whereClauseSegment.rightParen();
    }

    public static void buildApproxRangeClause(WhereAdapter whereClauseSegment, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound, BigDecimal value) {
        BigDecimal factor = value.multiply(FACTOR);

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase).gte().bind(lowerBound.subtract(factor)); // -10% of the Lower Bound
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase).lt().bind(upperBound.add(factor)); // +10% of the Upper Bound
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lte().bind(upperBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gte().bind(lowerBound);
        whereClauseSegment.rightParen();
    }

    public static void buildNotEqualsRangeClause(WhereAdapter whereClauseSegment, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase).lt().bind(lowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase).gte().bind(upperBound);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lt().bind(lowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gt().bind(upperBound);
        whereClauseSegment.rightParen();
    }

    public static BigDecimal generateLowerBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.subtract(scaleFactor);
    }

    public static BigDecimal generateUpperBound(BigDecimal original) {
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * (original.scale() + 1));
        return original.add(scaleFactor);
    }
}