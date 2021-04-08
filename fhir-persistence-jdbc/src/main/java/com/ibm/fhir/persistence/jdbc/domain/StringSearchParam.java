/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.bind;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_PERCENT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ESCAPE_UNDERSCORE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PERCENT_WILDCARD;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.UNDERSCORE_WILDCARD;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.database.utils.query.WhereFragment;
import com.ibm.fhir.database.utils.query.expression.StringExpNodeVisitor;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.NewUriModifierUtil;
import com.ibm.fhir.persistence.jdbc.util.SqlParameterEncoder;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * A string search parameter
 */
public class StringSearchParam extends SearchParam {
    private static final Logger logger = Logger.getLogger(StringSearchParam.class.getName());
    private final static String STR_VALUE = "STR_VALUE";
    private final static String STR_VALUE_LCASE = "STR_VALUE_LCASE";

    /**
     * Public constructor
     * @param name
     * @param queryParameter the search query parameter being wrapped
     */
    public StringSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException{

        // Process the values from the queryParameter to produce
        // the predicates we need to pass to the visitor (which is
        // responsible for building the full query).
        QueryParameter queryParm = getQueryParameter();
        final Operator operator = getOperator();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("StringSearchParam::visit: " + getName() + ", op=" + operator.name() + ", modifier=" + queryParm.getModifier());
        }
        WhereFragment whereFragment = new WhereFragment();

        boolean multiple = false;
        for (QueryParameterValue value : getQueryParameter().getValues()) {
            // Concatenate multiple matches with an OR
            if (multiple) {
                whereFragment.or();
            } else {
                multiple = true;
            }
            if (operator == Operator.LIKE) {
                // Must escape special wildcard characters _ and % in the parameter value string.
                String tempSearchValue =
                        SqlParameterEncoder.encode(value.getValueString()
                                .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));

                if (Modifier.CONTAINS.equals(queryParm.getModifier())) {
                    String searchValue = PERCENT_WILDCARD + tempSearchValue + PERCENT_WILDCARD;
                    whereFragment.col(STR_VALUE_LCASE).like(bind(searchValue)).escape("+");
                } else {
                    // If there is not a CONTAINS modifier on the query parm, construct
                    // a 'starts with' search value.
                    String searchValue = tempSearchValue + PERCENT_WILDCARD;

                    // Specific processing for
                    if (queryParm.getModifier() != null && queryParm.getType() == Type.URI) {
                        if (queryParm.getModifier() == Modifier.BELOW) {
                            searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;

                            whereFragment.leftParen()
                            .col(STR_VALUE).eq(bind(tempSearchValue))
                            .or(STR_VALUE).like(bind(searchValue)).escape("+")
                            .rightParen();

                        } else if (queryParm.getModifier() == Modifier.ABOVE) {
                            NewUriModifierUtil.generateAboveValuesQuery(whereFragment, STR_VALUE, searchValue);
                        } else {
                            // neither above nor below, so an exact match for URI
                            whereFragment.col(STR_VALUE).eq(bind(searchValue));
                        }
                    } else {
                        // Simple STARTS WITH
                        whereFragment.col(STR_VALUE).like(bind(searchValue)).escape("+");
                    }
                }
            } else if (queryParm.getType() == Type.URI) {
                // need to handle above/below modifier
                if (queryParm.getModifier() == Modifier.BELOW) {
                    String tempSearchValue =
                            SqlParameterEncoder.encode(value.getValueString()
                                    .replace(PERCENT_WILDCARD, ESCAPE_PERCENT)
                                    .replace(UNDERSCORE_WILDCARD, ESCAPE_UNDERSCORE));

                    String searchValue = tempSearchValue + "/" + PERCENT_WILDCARD;

                    whereFragment.leftParen()
                    .col(STR_VALUE).eq(bind(tempSearchValue))
                    .or(STR_VALUE).like(bind(searchValue)).escape("+")
                    .rightParen();

                } else if (queryParm.getModifier() == Modifier.ABOVE) {
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    NewUriModifierUtil.generateAboveValuesQuery(whereFragment, STR_VALUE, searchValue);
                } else {
                    // neither above nor below, so an exact match for URI
                    String searchValue = SqlParameterEncoder.encode(value.getValueString());
                    whereFragment.col(STR_VALUE).eq(bind(searchValue));
                }
            } else if (operator == Operator.EQ) {
                // Exact match
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                whereFragment.col(STR_VALUE).eq(bind(searchValue));
            } else {
                // For anything other than an exact match, we search against the STR_VALUE_LCASE column in the
                // Resource's string values table.
                // Also, the search value is "normalized"; it has accents removed and is lower-cased. This enables a
                // case-insensitive, accent-insensitive search.
                // Build this piece: pX.str_value_lcase {operator} search-attribute-value
                String searchValue = SqlParameterEncoder.encode(value.getValueString());
                searchValue = SearchUtil.normalizeForSearch(searchValue);
                whereFragment.col(STR_VALUE_LCASE).operator(operator).bind(searchValue);
                addEscapeIfRequired(whereFragment, operator);
            }
        }


        // Now that we have our arguments all prepared, we can make the call
        // to the visitor which is responsible for adding the required join
        // and dropping in the whereFragment predicate we've been building
        final ExpNode filter = whereFragment.getExpression();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("filter[" + getName() + "] := " + StringExpNodeVisitor.stringify(filter));
        }
        return visitor.addStringParam(query, getRootResourceType(), queryParm.getCode(), filter);
    }

    /**
     * Add the ESCAPE modified to the LIKE clause if needed
     * @param wf
     * @param op
     */
    private void addEscapeIfRequired(WhereFragment wf, Operator op) {
        if (op == Operator.LIKE) {
            wf.escape("+"); // adds ESCAPE '+'
        }
    }
}
