/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
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
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search
 * - Date - _lastUpdated</a>
 * <br>
 * This utility encapsulates the logic specific to fhir-search related to date.
 * <br>
 * The derived table looks similar to the following SQL:
 *
 * <pre>
 * (
 *       SELECT *
 *       FROM SubstancePolymer_RESOURCES IR
 *       WHERE
 *         (
 *           (
 *             LAST_UPDATED >= ?
 *             AND LAST_UPDATED <= ?
 *           )
 *         )
 *     ) R
 * </pre>
 */
@Deprecated
public class LastUpdatedParmBehaviorUtil {
    public static final String LAST_UPDATED = "_lastUpdated";
    public static final String LAST_UPDATED_COLUMN_NAME = "LAST_UPDATED";

    private List<Timestamp> bindVariables = new ArrayList<>();

    public LastUpdatedParmBehaviorUtil() {
        // No Operation
    }

    /**
     * builds the query parameters for the last updated
     * <br>
     *
     * @param fromClause
     * @param target
     * @param parameters
     */
    public void buildLastUpdatedDerivedTable(StringBuilder fromClause, String target, List<QueryParameter> parameters) {
        // Start the Derived Table
        fromClause.append(LEFT_PAREN);
        fromClause.append(" SELECT * FROM ");
        fromClause.append(target);
        fromClause.append("_RESOURCES IR WHERE ");

        boolean parmProcessed = false;
        for (QueryParameter queryParm : parameters) {
            // If multiple values are present, we need to AND them together.
            if (parmProcessed) {
                fromClause.append(AND);
            } else {
                // Signal to the downstream to treat any subsequent value as an AND condition
                parmProcessed = true;
            }
            executeBehavior(fromClause, queryParm);
        }

        // Close out the Derived Tables
        fromClause.append(RIGHT_PAREN);
    }

    /**
     * generate for each
     *
     * @param fromClause
     * @param queryParm
     */
    public void executeBehavior(StringBuilder whereClause, QueryParameter queryParm) {
        // Start the Clause
        // Query: (
        whereClause.append(LEFT_PAREN);

        // Initially we don't want to treat this as the second value.
        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                // OR
                whereClause.append(RIGHT_PAREN).append(OR).append(LEFT_PAREN);
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

            Instant v = value.getValueDateLowerBound();
            Instant upperBound = value.getValueDateUpperBound();
            buildPredicates(whereClause, prefix, v, upperBound);
        }

        // End the Clause started above, and closes the parameter expression.
        // Query: )
        whereClause.append(RIGHT_PAREN);
    }

    /**
     * builds query elements based on prefix type.
     *
     * @param whereClauseSegment
     * @param prefix
     * @param value
     * @param upperBound
     */
    public void buildPredicates(StringBuilder whereClauseSegment, Prefix prefix, Instant value, Instant upperBound) {
        switch (prefix) {
        case EB:
            // EB - Ends Before
            buildCommonClause(whereClauseSegment, LT, value);
            break;
        case SA:
            // SA - Starts After
            buildCommonClause(whereClauseSegment, GT, upperBound);
            break;
        case GE:
            // GE - Greater Than Equal
            buildCommonClause(whereClauseSegment, GTE, value);
            break;
        case GT:
            // GT - Greater Than
            buildCommonClause(whereClauseSegment, GT, upperBound);
            break;
        case LE:
            // LE - Less Than Equal
            buildCommonClause(whereClauseSegment, LTE, upperBound);
            break;
        case LT:
            // LT - Less Than
            buildCommonClause(whereClauseSegment, LT, value);
            break;
        case AP:
            // AP - Approximate - Relative
            // -10% of the Value
            // +10% of the Upper Bound
            buildEqualsRangeClause(whereClauseSegment, value, upperBound);
            break;
        case NE:
            // NE:  Upper and Lower Bounds
            buildNotEqualsRangeClause(whereClauseSegment, value, upperBound);
            break;
        case EQ:
        default:
            // EQ:  Upper and Lower Bounds
            buildEqualsRangeClause(whereClauseSegment, value, upperBound);
            break;
        }
    }

    /**
     * builds the common clause
     *
     * @param whereClauseSegment
     * @param operator
     * @param value
     */
    public void buildCommonClause(StringBuilder whereClauseSegment, String operator, Instant value) {
        // Example: <code>LAST_UPDATED <= ?</code>
        whereClauseSegment.append(LAST_UPDATED_COLUMN_NAME).append(operator).append(BIND_VAR);
        bindVariables.add(generateTimestamp(value));
    }

    /**
     * builds equals range
     *
     * @param whereClauseSegment
     * @param lowerBound
     * @param upperBound
     */
    public void buildEqualsRangeClause(StringBuilder whereClauseSegment, Instant lowerBound, Instant upperBound) {
        bindVariables.add(generateTimestamp(lowerBound));
        if (!lowerBound.equals(upperBound)) {
            // @formatter:off
            whereClauseSegment
                    .append(LEFT_PAREN)
                            .append(LAST_UPDATED_COLUMN_NAME).append(GTE).append(BIND_VAR)
                            .append(AND)
                            .append(LAST_UPDATED_COLUMN_NAME).append(LTE).append(BIND_VAR)
                    .append(RIGHT_PAREN);
            // @formatter:on
            bindVariables.add(generateTimestamp(upperBound));
        } else {
            // @formatter:off
            whereClauseSegment
                    .append(LEFT_PAREN)
                        .append(LAST_UPDATED_COLUMN_NAME).append(EQ).append(BIND_VAR)
                    .append(RIGHT_PAREN);
            // @formatter:on
        }
    }

    /**
     * build not equals range clause
     *
     * @param whereClauseSegment
     * @param lowerBound
     * @param upperBound
     */
    public void buildNotEqualsRangeClause(StringBuilder whereClauseSegment, Instant lowerBound, Instant upperBound) {
        // @formatter:off
        whereClauseSegment
                .append(LEFT_PAREN)
                        .append(LAST_UPDATED_COLUMN_NAME).append(LT).append(BIND_VAR)
                        .append(AND)
                        .append(LAST_UPDATED_COLUMN_NAME).append(GT).append(BIND_VAR)
                .append(RIGHT_PAREN);
        // @formatter:on

        bindVariables.add(generateTimestamp(lowerBound));
        bindVariables.add(generateTimestamp(upperBound));
    }

    public List<Timestamp> getBindVariables() {
        return bindVariables;
    }

    public void clearBindVariables() {
        bindVariables.clear();
    }
}