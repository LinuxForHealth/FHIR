/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.database.utils.query.expression.ExpressionSupport.bind;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_END;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;

import java.time.Instant;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * Updated DateParmBehaviorUtil now based on {@link Select} instead of
 * {@link StringBuilder}
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search
 * - Date</a>
 * <br>
 * This utility encapsulates the logic specific to fhir-search related to
 * date.
 */
public class NewDateParmBehaviorUtil {
    /**
     * Generate WHERE clause predicates based on the query parameter data
     *
     * @param whereClauseSegment
     * @param queryParm
     * @param tableAlias
     */
    public void executeBehavior(WhereFragment whereClauseSegment, QueryParameter queryParm, String tableAlias) {
        whereClauseSegment.leftParen();

        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                // OR
                whereClauseSegment.or();
            } else {
                // Signal to the downstream to treat any subsequent value as an OR condition
                parmValueProcessed = true;
            }

            // Let's get the prefix.
            Prefix prefix = value.getPrefix();
            if (prefix == null) {
                // Default to EQ
                prefix = Prefix.EQ;
            }

            Instant lowerBound = value.getValueDateLowerBound();
            Instant upperBound = value.getValueDateUpperBound();
            buildPredicates(whereClauseSegment, tableAlias, prefix, lowerBound, upperBound);
        }

        // ...)
        whereClauseSegment.rightParen();
    }

    /**
     * builds query elements based on prefix type.
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param prefix
     * @param lowerBound
     * @param upperBound
     */
    public void buildPredicates(WhereFragment whereClauseSegment, String tableAlias,
            Prefix prefix, Instant lowerBound, Instant upperBound) {
        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the value for the parameter in the resource is equal to the provided value
            // the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_END, LT, lowerBound);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_START, GT, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_END, GTE, lowerBound);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_END, GT, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_START, LTE, upperBound);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, tableAlias, DATE_START, LT, lowerBound);
            break;
        case AP:
            // AP - Approximate - Relative
            // -10% of the Lower Bound
            // +10% of the Upper Bound
            // the range of the search value overlaps with the range of the target value
            buildApproxRangeClause(whereClauseSegment, tableAlias, lowerBound, upperBound);
            break;
        case NE:
            // NE:  Upper and Lower Bounds - Range Based Search
            // the range of the search value does not fully contain the range of the target value
            buildNotEqualsRangeClause(whereClauseSegment, tableAlias, lowerBound, upperBound);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds - Range Based Search
            // the range of the search value fully contains the range of the target value
            buildEqualsRangeClause(whereClauseSegment, tableAlias, lowerBound, upperBound);
            break;
        }
    }

    /**
     * builds the common clause
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param columnNameLowOrHigh
     * @param operator
     * @param bound
     */
    public void buildCommonClause(WhereFragment whereClauseSegment, String tableAlias,
            String columnNameLowOrHigh, String operator, Instant bound) {

        Operator op = OperatorUtil.convert(operator);
        whereClauseSegment.col(tableAlias, columnNameLowOrHigh).operator(op).bind(bound);
    }

    /**
     * builds equals range
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildEqualsRangeClause(WhereFragment whereClauseSegment,
            String tableAlias, Instant lowerBound, Instant upperBound) {

        whereClauseSegment.col(tableAlias, DATE_START).gte(bind(lowerBound));
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, DATE_START).lte(bind(upperBound));
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, DATE_END).lte(bind(upperBound));
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, DATE_END).gte(bind(lowerBound));
    }

    /**
     * builds approximate range clause
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildApproxRangeClause(WhereFragment whereClauseSegment,
            String tableAlias, Instant lowerBound, Instant upperBound) {

        whereClauseSegment.col(tableAlias, DATE_END).gte(bind(lowerBound));
        whereClauseSegment.and();
        whereClauseSegment.col(tableAlias, DATE_START).lte(bind(upperBound));
    }

    /**
     * build not equals range clause
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildNotEqualsRangeClause(WhereFragment whereClauseSegment,
            String tableAlias, Instant lowerBound, Instant upperBound) {

        whereClauseSegment.leftParen();
        whereClauseSegment.col(tableAlias, DATE_START).lt(bind(lowerBound));
        whereClauseSegment.or();
        whereClauseSegment.col(tableAlias, DATE_END).gt(bind(upperBound));
        whereClauseSegment.rightParen();
    }

    /**
     * build a custom range clause
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param lowerBoundQueryParm
     * @param upperBoundQueryParm
     */
    public void buildCustomRangeClause(WhereFragment whereClauseSegment, String tableAlias,
            QueryParameter lowerBoundQueryParm, QueryParameter upperBoundQueryParm) {

        // We expect the query parameters to have a single value
        QueryParameterValue lowerBoundQueryParmValue = lowerBoundQueryParm.getValues().get(0);
        QueryParameterValue upperBoundQueryParmValue = upperBoundQueryParm.getValues().get(0);
        Prefix lowerBoundPrefix = lowerBoundQueryParmValue.getPrefix();
        Prefix upperBoundPrefix = upperBoundQueryParmValue.getPrefix();
        boolean expandColumnCheck = Prefix.SA.equals(lowerBoundPrefix) && Prefix.EB.equals(upperBoundPrefix);

        buildPredicates(whereClauseSegment, tableAlias, lowerBoundPrefix,
            lowerBoundQueryParmValue.getValueDateLowerBound(), lowerBoundQueryParmValue.getValueDateUpperBound());
        if (expandColumnCheck) {
            whereClauseSegment.and();
            buildCommonClause(whereClauseSegment, tableAlias, DATE_START, LT, upperBoundQueryParmValue.getValueDateLowerBound());
        }
        whereClauseSegment.and();
        buildPredicates(whereClauseSegment, tableAlias, upperBoundPrefix,
            upperBoundQueryParmValue.getValueDateLowerBound(), upperBoundQueryParmValue.getValueDateUpperBound());
        if (expandColumnCheck) {
            whereClauseSegment.and();
            buildCommonClause(whereClauseSegment, tableAlias, DATE_END, GT, lowerBoundQueryParmValue.getValueDateUpperBound());
        }
    }
}