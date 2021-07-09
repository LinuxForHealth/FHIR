/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.List;

/**
 * This class assists the JDBCQueryBuilder by encapsulating either a where clause segment or a complete SQL query along
 * with any associated bind variables.
 */
@Deprecated
public class SqlQueryData {

    private String queryString;
    private List<? extends Object> bindVariables;

    public SqlQueryData(String query, List<? extends Object> vars) {
        super();
        this.queryString = query;
        this.bindVariables = vars;

    }

    public String getQueryString() {
        return queryString;
    }

    public List<? extends Object> getBindVariables() {
        return bindVariables;
    }

    @Override
    public String toString() {
        return "SqlQueryData [queryString=" + queryString + ", bindVariables=" + bindVariables + "]";
    }

}
