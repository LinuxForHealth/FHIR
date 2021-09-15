/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import static com.ibm.fhir.database.utils.query.SqlConstants.FROM;
import static com.ibm.fhir.database.utils.query.SqlConstants.SELECT;
import static com.ibm.fhir.database.utils.query.SqlConstants.SPACE;
import static com.ibm.fhir.database.utils.query.SqlConstants.UNION;
import static com.ibm.fhir.database.utils.query.SqlConstants.UNION_ALL;
import static com.ibm.fhir.database.utils.query.SqlConstants.WHERE;

import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.query.FromClause;
import com.ibm.fhir.database.utils.query.FromItem;
import com.ibm.fhir.database.utils.query.GroupByClause;
import com.ibm.fhir.database.utils.query.HavingClause;
import com.ibm.fhir.database.utils.query.OrderByClause;
import com.ibm.fhir.database.utils.query.PaginationClause;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectList;
import com.ibm.fhir.database.utils.query.WhereClause;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;

/**
 * Helps to render a select statement as a string
 */
public class StringStatementRenderer implements StatementRenderer<String> {

    private static final String NEWLINE = System.lineSeparator();

    // fixed for now...perhaps a variable in the future
    private final char escapeChar = '+';

    // translator to handle SQL differences among databases
    private final IDatabaseTranslator translator;

    // pretty-print the statement for easier debug
    private final boolean pretty;

    // For collecting parameter markers seen when rendering the statement
    private final List<BindMarkerNode> collectBindMarkersInto;

    /**
     * Public constructor
     * @param translator
     * @param collectBindMarkersInto
     * @param pretty
     */
    public StringStatementRenderer(IDatabaseTranslator translator, List<BindMarkerNode> collectBindMarkersInto, boolean pretty) {
        this.translator = translator;
        this.collectBindMarkersInto = collectBindMarkersInto;
        this.pretty = pretty;
    }

    @Override
    public String select(boolean distinct, SelectList selectList, FromClause fromClause, WhereClause whereClause, GroupByClause groupByClause, HavingClause havingClause,
        OrderByClause orderByClause, PaginationClause paginationClause, boolean unionAll, Select union) {

        StringExpNodeVisitor whereClauseRenderer = new StringExpNodeVisitor(this.translator, this.collectBindMarkersInto, this.pretty);

        StringBuilder result = new StringBuilder();
        if (this.pretty) {
            result.append(NEWLINE).append("      "); // 6 spaces
        }
        result.append(SELECT);
        if (distinct) {
            result.append(" DISTINCT");
        }
        result.append(SPACE).append(selectList.toString());
        result.append(SPACE);
        if (this.pretty) {
            result.append(NEWLINE).append("        "); // 8 spaces
        }
        result.append(FROM);
        result.append(SPACE).append(fromClause.render(this));

        if (whereClause != null && !whereClause.isEmpty()) {
            if (this.pretty) {
                result.append(NEWLINE).append("      ");
            }
            result.append(SPACE).append(WHERE);
            result.append(SPACE).append(whereClause.visit(whereClauseRenderer));
        }

        if (groupByClause != null) {
            if (this.pretty) {
                result.append(NEWLINE).append("   ");
            }
            result.append(SPACE).append(groupByClause.toString());
        }

        if (havingClause != null) {
            if (this.pretty) {
                result.append(NEWLINE).append("    ");
            }
            result.append(SPACE).append(havingClause.toString());
        }

        if (orderByClause != null) {
            if (this.pretty) {
                result.append(NEWLINE).append("   ");
            }
            result.append(SPACE).append(orderByClause.toString());
        }

        if (paginationClause != null) {
            if (this.pretty) {
                result.append(NEWLINE).append("");
            }
            result.append(SPACE).append(paginationClause.getSqlString(this.translator));
        }

        if (union != null) {
            if (this.pretty) {
                result.append(NEWLINE).append("     "); // 5 spaces
            }
            result.append(SPACE).append(unionAll ? UNION_ALL : UNION)
                .append(SPACE).append(union.render(this));
        }

        return result.toString();
    }

    @Override
    public String from(List<FromItem> items) {
        StringBuilder result = new StringBuilder();
        FromItem element = items.get(0);
        result.append(element.render(this));
        for (int i=1; i<items.size(); i++) {
            FromItem nextElement = items.get(i);
            if (nextElement.isAnsiJoin()) {
                result.append(" "); // e.g. INNER JOIN ... ON ...
                if (this.pretty) {
                    result.append(NEWLINE).append("  ");
                }
            } else {
                result.append(", ");
                if (this.pretty) {
                    result.append(NEWLINE).append("             ");
                }
            }
            result.append(nextElement.render(this));
        }
        return result.toString();
    }

    @Override
    public String fromItem(FromItem item) {
        return item.toPrettyString(this.pretty);
    }

    @Override
    public String rowSource(String sub) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(sub);
        result.append(")");
        return result.toString();
    }

    @Override
    public String fromItem(String subValue, String aliasValue) {
        StringBuilder result = new StringBuilder();
        result.append(subValue);

        if (aliasValue != null) {
            result.append(" AS ");
            result.append(aliasValue);
        }
        return result.toString();
    }

    @Override
    public String alias(String alias) {
        return alias;
    }

    @Override
    public String rowSource(String schemaName, String tableName) {
        // schema name is optional
        if (schemaName == null) {
            return DataDefinitionUtil.assertValidName(tableName);
        }
        return DataDefinitionUtil.getQualifiedName(schemaName, tableName);
    }

    @Override
    public String render(ExpNode expression) {
        // Render the expression node as a string making sure we collect any
        // bind markers we happen to come across along the way
        StringExpNodeVisitor sv = new StringExpNodeVisitor(this.translator, this.collectBindMarkersInto, this.pretty);
        return expression.visit(sv);
    }

    @Override
    public String innerJoin(String joinFromValue, String joinOnValue) {
        StringBuilder result = new StringBuilder();
        result.append("INNER JOIN ");
        result.append(joinFromValue);
        result.append(" ON ");
        result.append(joinOnValue);
        return result.toString();
    }

    @Override
    public String leftOuterJoin(String joinFromValue, String joinOnValue) {
        StringBuilder result = new StringBuilder();
        result.append("LEFT OUTER JOIN ");
        result.append(joinFromValue);
        result.append(" ON ");
        result.append(joinOnValue);
        return result.toString();
    }

    @Override
    public String fullOuterJoin(String joinFromValue, String joinOnValue) {
        StringBuilder result = new StringBuilder();
        result.append("FULL OUTER JOIN ");
        result.append(joinFromValue);
        result.append(" ON ");
        result.append(joinOnValue);
        return result.toString();
    }
}