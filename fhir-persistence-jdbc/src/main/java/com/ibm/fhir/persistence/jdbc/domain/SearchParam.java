/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * Represents a search parameter in the search domain model
 */
public abstract class SearchParam {
    private final String rootResourceType;

    private final String name;

    private final QueryParameter queryParameter;

    protected SearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        this.rootResourceType = rootResourceType;
        this.name = name;
        this.queryParameter = queryParameter;
    }

    /**
     * Getter for the parameter's name
     * @return
     */
    protected String getName() {
        return this.name;
    }

    /**
     * Getter for the root resource type of the query
     * @return
     */
    protected String getRootResourceType() {
        return this.rootResourceType;
    }

    /**
     * Getter for the {@link QueryParameter} we've wrapped
     * @return
     */
    protected QueryParameter getQueryParameter() {
        return this.queryParameter;
    }

    public abstract <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException;

    /**
     * Get the operator we need to use for matching values for this parameter
     * @return
     */
    protected Operator getOperator() {
        String operator = LIKE;
        Modifier modifier = queryParameter.getModifier();

        // In the case where a URI, we need specific behavior/manipulation
        // so that URI defaults to EQ, unless... BELOW
        if (Type.URI.equals(queryParameter.getType())) {
            if (modifier != null && Modifier.BELOW.equals(modifier)) {
                operator = LIKE;
            } else {
                operator = EQ;
            }
        } else if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            operator = LIKE;
        }

        return convert(operator);
    }

    /**
     * Convert the operator string value to its enum equivalent
     * @param op
     * @return
     */
    private Operator convert(String op) {
        final Operator result;
        switch (op) {
        case LIKE:
            result = Operator.LIKE;
            break;
        case EQ:
            result = Operator.EQ;
            break;
        case NE:
            result = Operator.NE;
            break;
        case LT:
            result = Operator.LT;
            break;
        case LTE:
            result = Operator.LTE;
            break;
        case GT:
            result = Operator.GT;
            break;
        case GTE:
            result = Operator.GTE;
            break;
        default:
            throw new IllegalArgumentException("Operator not supported: " + op);
        }

        return result;
    }
}