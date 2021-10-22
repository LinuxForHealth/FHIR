/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.expression;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.node.BigDecimalBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.BindMarkerNode;
import com.ibm.fhir.database.utils.query.node.DoubleBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.database.utils.query.node.ExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.InstantBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.IntegerBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.LongBindMarkerNode;
import com.ibm.fhir.database.utils.query.node.StringBindMarkerNode;


/**
 * Renders the expression node tree into a string
 */
public class StringExpNodeVisitor implements ExpNodeVisitor<String> {
    private final List<BindMarkerNode> bindMarkers;

    // Pretty-print the SQL statement for improved readability
    private final boolean pretty;

    public static final String NEWLINE = System.lineSeparator();

    private final IDatabaseTranslator translator;

    // For parameter encoding
    private static final String SQL_PARAM_ESCAPE_CHARACTER = "";
    private static final String SQL_PARAM_BLACKLIST_CHARACTERS_REGEX = "['\"]";
    private final Pattern escapeCharacterPattern;

    /**
     * Simple rendering of the expression tree to a string, ignoring
     * the bind marker values
     */
    public StringExpNodeVisitor() {
        escapeCharacterPattern = Pattern.compile(SQL_PARAM_BLACKLIST_CHARACTERS_REGEX);
        this.translator = null;
        this.bindMarkers = null;
        this.pretty = false;
    }

    /**
     * For rendering the expression tree to a string with additional formatting
     * to improve readability.
     * @param pretty
     */
    public StringExpNodeVisitor(boolean pretty) {
        escapeCharacterPattern = Pattern.compile(SQL_PARAM_BLACKLIST_CHARACTERS_REGEX);
        this.translator = null;
        this.bindMarkers = null;
        this.pretty = pretty;
    }

    /**
     * Collect the bind marker values into the given list
     * @param translator
     * @param collectBindMarkersInto
     */
    public StringExpNodeVisitor(IDatabaseTranslator translator, List<BindMarkerNode> collectBindMarkersInto, boolean pretty) {
        escapeCharacterPattern = Pattern.compile(SQL_PARAM_BLACKLIST_CHARACTERS_REGEX);
        this.translator = translator;
        this.bindMarkers = collectBindMarkersInto;
        this.pretty = pretty;
    }

    /**
     * Helper to render an expression as a string using this class as a visitor
     * @param exp
     * @return
     */
    public static String stringify(ExpNode exp) {
        return exp.visit(new StringExpNodeVisitor(true));
    }

    @Override
    public String paren(String expr) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(expr);
        result.append(")");
        return result.toString();
    }

    @Override
    public String and(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        if (pretty) {
            result.append(NEWLINE).append("        ");
        }
        result.append(" AND ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String or(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        if (pretty) {
            result.append(NEWLINE).append("         "); // 10 spaces
        }
        result.append(" OR ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String not(String exp) {
        StringBuilder result = new StringBuilder();
        if (pretty) {
            result.append(NEWLINE).append("        "); // 9 spaces
        }
        result.append("NOT ");
        result.append(exp);
        return result.toString();
    }

    @Override
    public String exists(String selectString) {
        StringBuilder result = new StringBuilder();
        result.append("EXISTS (");
        result.append(selectString);
        result.append(")");
        return result.toString();
    }

    @Override
    public String notExists(String selectString) {
        StringBuilder result = new StringBuilder();
        result.append("NOT EXISTS (");
        result.append(selectString);
        result.append(")");
        return result.toString();
    }

    @Override
    public String eq(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" = ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String neq(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" != ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String gt(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" > ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String gte(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" >= ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String lt(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" < ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String lte(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" <= ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String literal(final String value) {
        final String safeValue = encodeLiteralString(value);
        StringBuilder result = new StringBuilder();
        result.append("'");
        result.append(safeValue);
        result.append("'");
        return result.toString();
    }

    @Override
    public String literal(Long value) {
        StringBuilder result = new StringBuilder();
        result.append(value);
        return result.toString();
    }

    @Override
    public String literal(Double value) {
        StringBuilder result = new StringBuilder();
        result.append(value);
        return result.toString();
    }

    @Override
    public String add(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" + ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String subtract(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" - ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String multiply(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" * ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String divide(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" / ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String isNull(String expr) {
        StringBuilder result = new StringBuilder();
        result.append(expr);
        result.append(" IS NULL");
        return result.toString();
    }

    @Override
    public String isNotNull(String expr) {
        StringBuilder result = new StringBuilder();
        result.append(expr);
        result.append(" IS NOT NULL");
        return result.toString();
    }

    @Override
    public String between(String leftValue, String rightValue) {
        StringBuilder result = new StringBuilder();
        result.append(" BETWEEN ");
        result.append(leftValue);
        result.append(" AND ");
        result.append(rightValue);
        return result.toString();
    }

    @Override
    public String column(String tableAlias, String columnName) {
        StringBuilder result = new StringBuilder();
        if (tableAlias != null) {
            // check safety
            DataDefinitionUtil.assertValidName(tableAlias);
            DataDefinitionUtil.assertValidName(columnName);
            result.append(tableAlias);
            result.append(".");
        }

        // Currently, columnName by itself may be a more complex expression
        // which isn't ideal. Consider refactor as part of #2321
        result.append(columnName);
        return result.toString();
    }

    @Override
    public String bindMarker(Double value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new DoubleBindMarkerNode(value));
        }
        return "?";
    }

    @Override
    public String bindMarker(Long value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new LongBindMarkerNode(value));
        }
        return "?";
    }

    @Override
    public String bindMarker(String value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new StringBindMarkerNode(value));
        }
        return "?";
    }

    @Override
    public String bindMarker(Instant value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new InstantBindMarkerNode(value));
        }
        return "?";
    }

    @Override
    public String bindMarker(Integer value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new IntegerBindMarkerNode(value));
        }
        return "?";
    }

    @Override
    public String in(String left, List<String> args) {
        // Each argument here should already have been rendered into a SQL
        // string value - one that has been quoted. This also means that
        // the values are already encoded fpr safety, so there's no need
        // to encode again (which would break the statement syntax).
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" IN (");
        result.append(String.join(",", args));
        result.append(")");
        return result.toString();
    }

    @Override
    public String like(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left);
        result.append(" LIKE ");
        result.append(right);
        return result.toString();
    }

    @Override
    public String escape(String left, String right) {
        StringBuilder result = new StringBuilder();
        result.append(left); // a like expression
        result.append(" ESCAPE ");
        result.append(right); // the escape chars
        return result.toString();
    }

    @Override
    public String select(Select select) {
        // Render the sub-statement as a string
        StringStatementRenderer renderer = new StringStatementRenderer(this.translator, this.bindMarkers, pretty);
        return select.render(renderer);
    }

    @Override
    public String coalesce(List<ColumnRef> columnRefs) {
        final String argsList = columnRefs.stream().map(Object::toString).collect(Collectors.joining(","));
        StringBuilder result = new StringBuilder();
        result.append("COALESCE(");
        result.append(argsList);
        result.append(")");
        return result.toString();
   }

    @Override
    public String bindMarker(BigDecimal value) {
        if (this.bindMarkers != null) {
            bindMarkers.add(new BigDecimalBindMarkerNode(value));
        }
        return "?";
   }

    @Override
    public String cos(String arg) {
        StringBuilder result = new StringBuilder();
        result.append("COS(");
        result.append(arg);
        result.append(")");
        return result.toString();
    }

    @Override
    public String acos(String arg) {
        StringBuilder result = new StringBuilder();
        result.append("ACOS(");
        result.append(arg);
        result.append(")");
        return result.toString();
    }

    @Override
    public String sin(String arg) {
        StringBuilder result = new StringBuilder();
        result.append("SIN(");
        result.append(arg);
        result.append(")");
        return result.toString();
    }

    @Override
    public String radians(String arg) {
        StringBuilder result = new StringBuilder();
        result.append("RADIANS(");
        result.append(arg);
        result.append(")");
        return result.toString();
    }

    /**
     * Make sure any literal strings we're embedding in our SQL statements are safe. String parameters
     * should almost always use bind variables (which don't require encoding).
     * <br>
     * If parameter is null, returns empty string
     * @param parameter
     * @return SQL-safe encoded value
     */
    private final String encodeLiteralString(String parameter) {
        String safeParameter = Optional.ofNullable(parameter).orElse("");
        Matcher matcher = escapeCharacterPattern.matcher(safeParameter);
        safeParameter = matcher.replaceAll(SQL_PARAM_ESCAPE_CHARACTER);

        // extra check
        DataDefinitionUtil.assertSecure(safeParameter);
        return safeParameter;
    }
}