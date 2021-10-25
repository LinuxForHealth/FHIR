/*
 * (C) Copyright IBM Corp. 2021
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
import com.ibm.fhir.database.utils.query.WhereFragment;
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

    /**
     * Add the filter predicate logic to the given whereClauseSegment
     * @param whereClauseSegment
     * @param queryParm
     * @param tableAlias
     * @throws FHIRPersistenceException
     */
    public void executeBehavior(WhereFragment whereClauseSegment, QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {

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
    }

    /**
     * Append the condition and bind the variables according to the semantics of the
     * passed prefix
     * adds the value to the whereClause.
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnBase
     * @param prefix
     * @param value
     */
    public static void addValue(WhereFragment whereClauseSegment, String tableAlias,
            String columnBase, Prefix prefix, BigDecimal value) {

        BigDecimal lowerBound = generateLowerBound(value);
        BigDecimal upperBound = generateUpperBound(value);

        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the range of the search value does not overlap with the range of the target value,
            // and the range above the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, tableAlias, columnBase + _HIGH,
                    LT, lowerBound);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildEbOrSaClause(whereClauseSegment, tableAlias, columnBase + _LOW,
                    GT, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    columnBase + _HIGH, GTE, value, lowerBound);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    columnBase + _HIGH, GT, value, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    columnBase + _HIGH, LTE, value, upperBound);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, columnBase, columnBase + _LOW,
                    columnBase + _HIGH, LT, value, lowerBound);
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
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnName
     * @param columnNameLow
     * @param columnNameHigh
     * @param operator
     * @param value
     * @param bound
     */
    public static void buildCommonClause(WhereFragment whereClauseSegment, String tableAlias,
            String columnName, String columnNameLow, String columnNameHigh, String operator,
            BigDecimal value, BigDecimal bound) {

        Operator op = null;
        Operator orEqualsOp = null;
        if (GTE.equals(operator)) {
            op = OperatorUtil.convert(GT);
            orEqualsOp = OperatorUtil.convert(GTE);
        } else if (LTE.equals(operator)) {
            op = OperatorUtil.convert(LT);
            orEqualsOp = OperatorUtil.convert(LTE);
        } else {
            op = OperatorUtil.convert(operator);
        }
        boolean gtOp = Operator.GT.equals(op);

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, gtOp ? columnNameHigh : columnNameLow).operator(op).bind(value);
        whereClauseSegment.or();
        if (orEqualsOp != null) {
            whereClauseSegment.col(tableAlias, gtOp ? columnNameLow : columnNameHigh).operator(orEqualsOp).bind(bound);
        } else {
            whereClauseSegment.col(tableAlias, gtOp ? columnNameLow : columnNameHigh).operator(op).bind(value);
        }
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnName).isNotNull();
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, gtOp ? columnNameHigh : columnNameLow).isNull();
        whereClauseSegment.rightParen();
    }

    /**
     * the build eb or sa clause considers only _VALUE_LOW and _VALUE_HIGH
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnNameLowOrHigh
     * @param operator
     * @param bound
     */
    public static void buildEbOrSaClause(WhereFragment whereClauseSegment, String tableAlias,
            String columnNameLowOrHigh, String operator, BigDecimal bound) {

        Operator op = OperatorUtil.convert(operator);
        whereClauseSegment.col(tableAlias, columnNameLowOrHigh).operator(op).bind(bound);
    }

    /**
     * Add the equals range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     */
    public static void buildEqualsRangeClause(WhereFragment whereClauseSegment, String tableAlias,
           String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).gte().bind(lowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).lte().bind(upperBound);
        whereClauseSegment.rightParen();
    }

    /**
     * Add the approx range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     * @param value
     */
    public static void buildApproxRangeClause(WhereFragment whereClauseSegment, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound, BigDecimal value) {
        BigDecimal factor = value.multiply(FACTOR);
        BigDecimal approximateLowerBound = lowerBound.subtract(factor);
        BigDecimal approximateUpperBound = upperBound.add(factor);

        whereClauseSegment.leftParen();

        // The following clauses test for overlap when both xx_VALUE_HIGH and xx_VALUE_LOW are non-null.
        // Example:
        //      P2.QUANTITY_VALUE_HIGH >= 8.5
        //  AND P2.QUANTITY_VALUE_LOW <= 11.5
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gte().bind(approximateLowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lte().bind(approximateUpperBound);

        // The following clauses test for overlap when the target is a Range data type and either
        // xx_VALUE_HIGH or xx_VALUE_LOW is NULL (unknown bound).
        // Example:
        //   OR P2.QUANTITY_VALUE IS NULL
        //  AND (P2.QUANTITY_VALUE_HIGH >= 8.5
        //  AND P2.QUANTITY_VALUE_HIGH <= 11.5
        //   OR P2.QUANTITY_VALUE_LOW >= 8.5
        //  AND P2.QUANTITY_VALUE_LOW <= 11.5)
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase).isNull();
        whereClauseSegment.and();
        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gte().bind(approximateLowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).lte().bind(approximateUpperBound);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).gte().bind(approximateLowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lte().bind(approximateUpperBound);
        whereClauseSegment.rightParen();

        // The following clauses test for overlap when the target is a Quantity data type and either
        // xx_VALUE_HIGH or xx_VALUE_LOW is NULL (negative or positive infinity bound).
        // Example:
        //   OR P2.QUANTITY_VALUE IS NOT NULL
        //  AND (P2.QUANTITY_VALUE_HIGH >= 8.5
        //  AND P2.QUANTITY_VALUE_LOW IS NULL
        //   OR P2.QUANTITY_VALUE_LOW <= 11.5
        //  AND P2.QUANTITY_VALUE_HIGH IS NULL)
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase).isNotNull();
        whereClauseSegment.and();
        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gte().bind(approximateLowerBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).isNull();
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lte().bind(approximateUpperBound);
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).isNull();
        whereClauseSegment.rightParen();

        whereClauseSegment.rightParen();
    }

    /**
     * Add the not-equals range clause to the given whereClauseSegment
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnBase
     * @param lowerBound
     * @param upperBound
     */
    public static void buildNotEqualsRangeClause(WhereFragment whereClauseSegment, String tableAlias,
            String columnBase, BigDecimal lowerBound, BigDecimal upperBound) {

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).lt().bind(lowerBound);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _LOW).isNull();
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).gt().bind(upperBound);
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, columnBase + _HIGH).isNull();
        whereClauseSegment.rightParen();
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