/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_END;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_START;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DATE_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.search.date.DateTimeHandler.generateTimestamp;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.ibm.fhir.persistence.jdbc.JDBCConstants.JDBCOperator;
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

    public static final String LAST_UPDATED = "_lastUpdated";

    public DateParmBehaviorUtil() {
        // No Operation
    }

    public void executeBehavior(StringBuilder whereClauseSegment, QueryParameter queryParm,
            List<Timestamp> bindVariables,
            String tableAlias)
            throws Exception {
        // Start the Clause 
        // Query: AND ((
        whereClauseSegment.append(AND).append(LEFT_PAREN).append(LEFT_PAREN);

        // Initially we don't want to 
        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                // OR
                whereClauseSegment.append(RIGHT_PAREN).append(JDBCOperator.OR.value()).append(LEFT_PAREN);
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

            Instant v = value.getValueDate();
            Instant lowerBound = value.getValueDateLowerBound();
            Instant upperBound = value.getValueDateUpperBound();
            buildPredicates(whereClauseSegment, bindVariables, tableAlias, prefix, v, lowerBound, upperBound);
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
     * @param value
     * @param lowerBound
     * @param upperBound
     */
    public void buildPredicates(StringBuilder whereClauseSegment, List<Timestamp> bindVariables, String tableAlias,
            Prefix prefix, Instant value, Instant lowerBound, Instant upperBound) {
        switch (prefix) {
        case EB:
            // EB - Ends Before
            // the value for the parameter in the resource is equal to the provided value
            // the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_END,
                    JDBCOperator.LT.value(), value, value);
            break;
        case SA:
            // SA - Starts After
            // the range of the search value does not overlap with the range of the target value,
            // and the range below the search value contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_START,
                    JDBCOperator.GT.value(), upperBound, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            // the range above the search value intersects (i.e. overlaps) with the range of the target value,
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_START,
                    JDBCOperator.GTE.value(), value, value);
            break;
        case GT:
            // GT - Greater Than
            // the range above the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_START,
                    JDBCOperator.GT.value(), upperBound, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            // or the range of the search value fully contains the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_END,
                    JDBCOperator.LTE.value(), upperBound, upperBound);
            break;
        case LT:
            // LT - Less Than
            // the range below the search value intersects (i.e. overlaps) with the range of the target value
            buildCommonClause(whereClauseSegment, bindVariables, tableAlias, DATE_VALUE, DATE_END,
                    JDBCOperator.LT.value(), value, value);
            break;
        case AP:
            // AP - Approximate - Relative
            // -10% of the Lower Bound
            // +10% of the Upper Bound
            buildApproxRangeClause(whereClauseSegment, bindVariables, tableAlias, lowerBound, upperBound, value);
            break;
        case NE:
            // NE:  Upper and Lower Bounds - Range Based Search
            // the range of the search value does not fully contain the range of the target value
            buildNotEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, value, upperBound, value);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds - Range Based Search
            // the range of the search value fully contains the range of the target value
            buildEqualsRangeClause(whereClauseSegment, bindVariables, tableAlias, value, upperBound, value);
            break;
        }
    }

    /**
     * builds the common clause
     * 
     * @param whereClauseSegment
     * @param bindVariables
     * @param tableAlias
     * @param columnName
     * @param columnNameLowOrHigh
     * @param operator
     * @param value
     * @param bound
     */
    public void buildCommonClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables, String tableAlias,
            String columnName, String columnNameLowOrHigh, String operator, Instant value, Instant bound) {
        // ( TABLE.DATE_VALUE <= ? OR 
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                    .append(tableAlias).append(DOT).append(columnName).append(operator).append(BIND_VAR)
                .append(JDBCOperator.OR.value())
                    .append(tableAlias).append(DOT).append(columnNameLowOrHigh).append(operator).append(BIND_VAR)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(value));
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
     * @param value
     */
    public void buildEqualsRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias,
            Instant lowerBound, Instant upperBound, Instant value) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN).append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR)
                        .append(JDBCOperator.AND.value())
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR)
                    .append(RIGHT_PAREN)
                    .append(JDBCOperator.OR.value())
                    .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_START).append(JDBCOperator.GTE.value()).append(BIND_VAR)
                        .append(JDBCOperator.AND.value())
                        .append(tableAlias).append(DOT).append(DATE_END).append(JDBCOperator.LTE.value()).append(BIND_VAR)
                .append(RIGHT_PAREN).append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
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
     * @param value
     */
    public void buildApproxRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias,
            Instant lowerBound, Instant upperBound, Instant value) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                    .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.GTE.value()).append(BIND_VAR)
                    .append(JDBCOperator.AND.value())
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.LTE.value()).append(BIND_VAR)
                    .append(RIGHT_PAREN)
                    .append(JDBCOperator.OR.value())
                    .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_START).append(JDBCOperator.GTE.value()).append(BIND_VAR)
                    .append(JDBCOperator.AND.value())
                        .append(tableAlias).append(DOT).append(DATE_END).append(JDBCOperator.LTE.value()).append(BIND_VAR)
                    .append(RIGHT_PAREN)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
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
     * @param value
     */
    public void buildNotEqualsRangeClause(StringBuilder whereClauseSegment, List<Timestamp> bindVariables,
            String tableAlias, Instant lowerBound, Instant upperBound, Instant value) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                    .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.LT.value()).append(BIND_VAR)
                        .append(JDBCOperator.OR.value())
                        .append(tableAlias).append(DOT).append(DATE_VALUE).append(JDBCOperator.GT.value()).append(BIND_VAR)
                    .append(RIGHT_PAREN)
                    .append(JDBCOperator.OR.value())
                    .append(LEFT_PAREN)
                        .append(tableAlias).append(DOT).append(DATE_START).append(JDBCOperator.LT.value()).append(BIND_VAR)
                        .append(JDBCOperator.OR.value())
                        .append(tableAlias).append(DOT).append(DATE_END).append(JDBCOperator.GT.value()).append(BIND_VAR)
                    .append(RIGHT_PAREN)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
    }
}