/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.List;

/**
 * This class assists the JDBCNormalizedQueryBuilder by encapsulating either a where clause segment or a complete SQL query along with any associated bind variables.
 * @author markd
 *
 */
public class SqlQueryData {
    
    private String queryString;
    private List<Object> bindVariables;

    public SqlQueryData(String query, List<Object> vars) {
        super();
        this.queryString = query;
        this.bindVariables = vars;
         
    }

    public String getQueryString() {
        return queryString;
    }

    public List<Object> getBindVariables() {
        return bindVariables;
    }

    @Override
    public String toString() {
        return "SqlQueryData [queryString=" + queryString + ", bindVariables=" + bindVariables + "]";
    }

}
