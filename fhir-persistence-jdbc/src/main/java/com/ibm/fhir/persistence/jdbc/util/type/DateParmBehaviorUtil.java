/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_END;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.search.date.DateTimeHandler.generateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search
 * - Date</a>
 * <br>
 * This utility encapsulates the logic specific to fhir-search related to
 * quantity.
 */
public class DateParmBehaviorUtil {

    public DateParmBehaviorUtil() {
        // No Operation
    }

    public void executeBehavior(StringBuilder whereClauseSegment, QueryParameter queryParm,
            List<Timestamp> bindVariables,
            String tableAlias) {
        // Start the Clause
        // Query: AND ((
        whereClauseSegment.append(AND).append(LEFT_PAREN).append(LEFT_PAREN);

        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                // OR
                whereClauseSegment.append(RIGHT_PAREN).append(OR).append(LEFT_PAREN);
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
            buildPredicates(whereClauseSegment, bindVariables, tableAlias, prefix, lowerBound, upperBound);
        }

        // End the Clause started above, and closes the parameter expression.
        // Query: )))
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN).append(RIGHT_PAREN);
    }

    /**
     * builds query elements based on prefix type.
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param prefix
     * @param lowerBound
     * @param upperBound
     */
    public void buildPredicates(StringBuilder whereClauseSegment, List<Timestamp> bindVariables, String tableAlias,
            Prefix prefix, Instant lowerBound, Instant upperBound) {
        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the value for the parameter in the resource is equal to the provided value
            // the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_END, LT, lowerBound);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_START, GT, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_END, GTE, lowerBound);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_END, GT, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_START, LTE, upperBound);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_START, LT, lowerBound);
            break;
        case AP:
            // AP - Approximate - Relative
            // -10% of the Lower Bound
            // +10% of the Upper Bound
            // the range of the search value overlaps with the range of the target value
            buildApproxRangeClause(whereClauseSegment, bindVariables, tableAlias, lowerBound, upperBound);
            break;
        case NE:
            // NE:  Upper and Lower Bounds - Range Based Search
            // the range of the search value does not fully contain the range of the target value
            buildNotEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, lowerBound, upperBound);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds - Range Based Search
            // the range of the search value fully contains the range of the target value
            buildEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, lowerBound, upperBound);
            break;
        }
    }

    /**
     * builds the common clause
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnNameLowOrHigh
     * @param operator
     * @param bound
     */
    public void buildCommonClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables, String tableAlias,
            String columnNameLowOrHigh, String operator, Instant bound) {
        whereClauseSegment.append(tableAlias).append(DOT).append(columnNameLowOrHigh).append(operator).append(BIND_VAR);
        bindVariables.add(generateTimestamp(bound));
    }

    /**
     * builds equals range
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildEqualsRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias, Instant lowerBound, Instant upperBound) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                    .append(tableAlias).append(DOT).append(DATE_START).append(GTE).append(BIND_VAR)
                .append(AND)
                    .append(tableAlias).append(DOT).append(DATE_END).append(LTE).append(BIND_VAR)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
    }

    /**
     * builds approximate range clause
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildApproxRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias, Instant lowerBound, Instant upperBound) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                     .append(tableAlias).append(DOT).append(DATE_END).append(GTE).append(BIND_VAR)
                .append(AND)
                     .append(tableAlias).append(DOT).append(DATE_START).append(LTE).append(BIND_VAR)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
    }

    /**
     * build not equals range clause
     *
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param lowerBound
     * @param upperBound
     */
    public void buildNotEqualsRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias, Instant lowerBound, Instant upperBound) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_START).append(LT).append(BIND_VAR)
                        .append(OR)
                        .append(tableAlias).append(DOT).append(DATE_END).append(GT).append(BIND_VAR)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
    }
}