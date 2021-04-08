/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import static com.ibm.fhir.database.utils.query.SqlConstants.FROM;
import static com.ibm.fhir.database.utils.query.SqlConstants.SELECT;
import static com.ibm.fhir.database.utils.query.SqlConstants.SPACE;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.query.FromClause;
import com.ibm.fhir.database.utils.query.GroupByClause;
import com.ibm.fhir.database.utils.query.HavingClause;
import com.ibm.fhir.database.utils.query.OrderByClause;
import com.ibm.fhir.database.utils.query.PaginationClause;
import com.ibm.fhir.database.utils.query.SelectList;
import com.ibm.fhir.database.utils.query.WhereClause;

/**
 * Helps to render a select statement as a string
 */
public class StringStatementRenderer implements StatementRenderer<String> {

    // fixed for now...perhaps a variable in the future
    private final char escapeChar = '+';

    // translator to handle SQL differences among databases
    private final IDatabaseTranslator translator;

    /**
     * Public constructor
     * @param translator
     */
    public StringStatementRenderer(IDatabaseTranslator translator) {
        this.translator = translator;
    }

    @Override
    public String and(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" AND ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String or(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" OR ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String equals(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" = ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String notEquals(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" != ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String not(String param) {
        StringBuilder result = new StringBuilder();
        result.append("NOT ");
        result.append(param);
        return result.toString();
    }

    @Override
    public String exists(String statement) {
        StringBuilder result = new StringBuilder();
        result.append("EXISTS (");
        result.append(statement);
        result.append(")");
        return result.toString();
    }

    @Override
    public String select(SelectList selectList, FromClause fromClause, WhereClause whereClause, GroupByClause groupByClause, HavingClause havingClause,
        OrderByClause orderByClause, PaginationClause paginationClause) {

        StringBuilder result = new StringBuilder();
        result.append(SELECT);
        result.append(SPACE).append(selectList.toString());
        result.append(SPACE).append(FROM);
        result.append(SPACE).append(fromClause.toString());

        if (whereClause != null) {
            result.append(SPACE).append(whereClause.toString());
        }

        if (groupByClause != null) {
            result.append(SPACE).append(groupByClause.toString());
        }

        if (havingClause != null) {
            result.append(SPACE).append(havingClause.toString());
        }

        if (orderByClause != null) {
            result.append(SPACE).append(orderByClause.toString());
        }

        if (paginationClause != null) {
            result.append(SPACE).append(paginationClause.getSqlString(this.translator));
        }

        return result.toString();
    }

    @Override
    public String paren(String expression) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(expression);
        result.append(")");
        return result.toString();
    }

    @Override
    public String expression(String expr) {
        // an expression which hasn't been broken into an expression tree (not a literal!)
        return expr;
    }

    @Override
    public String bind() {
        return "?";
    }

    @Override
    public String like(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" LIKE ");
        result.append(right);
        result.append(" ESCAPE '" + this.escapeChar + "'");
        return result.toString();
   }
}