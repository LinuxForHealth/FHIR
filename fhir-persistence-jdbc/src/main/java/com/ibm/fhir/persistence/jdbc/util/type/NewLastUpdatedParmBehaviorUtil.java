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

import java.time.Instant;
import java.util.List;

import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.WhereFragment;
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
public class NewLastUpdatedParmBehaviorUtil {
    public static final String LAST_UPDATED = "_lastUpdated";
    public static final String LAST_UPDATED_COLUMN_NAME = "LAST_UPDATED";

    // The alias for the xx_logical_resources table/subquery providing the LAST_UPDATED column
    private final String lrAlias;

    public NewLastUpdatedParmBehaviorUtil(String lrAlias) {
        this.lrAlias = lrAlias;
    }

    /**
     * builds the query parameters for the last updated
     * <br>
     *
     * @param fromClause
     * @param target
     * @param parameters
     */
    public void buildLastUpdatedDerivedTable(WhereFragment fromClause, List<QueryParameter> parameters) {

        boolean parmProcessed = false;
        for (QueryParameter queryParm : parameters) {
            // If multiple values are present, we need to AND them together.
            if (parmProcessed) {
                fromClause.and();
            } else {
                // Signal to the downstream to treat any subsequent value as an AND condition
                parmProcessed = true;
            }
            executeBehavior(fromClause, queryParm);
        }
    }

    /**
     * generate for each
     *
     * @param fromClause
     * @param queryParm
     */
    public void executeBehavior(WhereFragment whereClause, QueryParameter queryParm) {
        // Start the Clause
        // Query: (
        whereClause.leftParen();

        // Initially we don't want to treat this as the second value.
        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                // OR
                whereClause.rightParen().or().leftParen();
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
        whereClause.rightParen();
    }

    /**
     * builds query elements based on prefix type.
     *
     * @param whereClauseSegment
     * @param prefix
     * @param value
     * @param upperBound
     */
    public void buildPredicates(WhereFragment whereClauseSegment, Prefix prefix, Instant value, Instant upperBound) {
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
    public void buildCommonClause(WhereFragment whereClauseSegment, String operator, Instant value) {
        // Example: <code>LAST_UPDATED <= ?</code>
        Operator op = OperatorUtil.convert(operator);
        whereClauseSegment.col(lastUpdatedColumn()).operator(op).bind(value);
    }

    /**
     * Get the qualified name for the last_updated column
     * @return
     */
    private String lastUpdatedColumn() {
        if (lrAlias != null && lrAlias.length() > 0) {
            return DataDefinitionUtil.getQualifiedName(lrAlias, LAST_UPDATED_COLUMN_NAME);
        } else {
            return LAST_UPDATED_COLUMN_NAME;
        }
    }

    /**
     * builds equals range
     *
     * @param whereClauseSegment
     * @param lowerBound
     * @param upperBound
     */
    public void buildEqualsRangeClause(WhereFragment whereClauseSegment, Instant lowerBound, Instant upperBound) {
        if (!lowerBound.equals(upperBound)) {
            // @formatter:off
            whereClauseSegment
                .leftParen()
                    .col(lastUpdatedColumn()).gte().bind(lowerBound)
                    .and()
                    .col(lastUpdatedColumn()).lte().bind(upperBound)
                .rightParen();
        } else {
            // @formatter:off
            whereClauseSegment.leftParen().col(lastUpdatedColumn()).eq().bind(lowerBound).rightParen();
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
    public void buildNotEqualsRangeClause(WhereFragment whereClauseSegment, Instant lowerBound, Instant upperBound) {
        // @formatter:off
        whereClauseSegment
            .leftParen()
                .col(lastUpdatedColumn()).lt().bind(lowerBound)
                .or()
                .col(lastUpdatedColumn()).gt().bind(upperBound)
            .rightParen();
        // @formatter:on
    }
}