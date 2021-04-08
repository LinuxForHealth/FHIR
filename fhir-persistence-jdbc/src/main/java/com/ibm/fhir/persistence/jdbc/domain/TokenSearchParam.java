/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Operator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.SqlQueryData;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * A token search parameter
 */
public class TokenSearchParam extends SearchParam {
    private static final String CLASSNAME = TokenSearchParam.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    /**
     * Public constructor
     * @param name
     */
    public TokenSearchParam(String rootResourceName, String name, QueryParameter queryParameter) {
        super(rootResourceName, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        QueryParameter queryParm = getQueryParameter();
        final String METHODNAME = "processTokenParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        final Operator operator = getOperator();
        // String operator = this.getOperator(queryParm, EQ);
        boolean parmValueProcessed = false;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // TODO, obviously
        return visitor.addTokenParam(query, getRootResourceType(), -1, -1L);
    }
}
