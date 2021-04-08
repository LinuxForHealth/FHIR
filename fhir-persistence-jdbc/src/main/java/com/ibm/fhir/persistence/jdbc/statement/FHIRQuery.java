/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.statement;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.database.utils.query.Select;

/**
 * Representation of an executable query statement together with a
 * catalog of any bind variables (e.g. search parameter values) which
 * need to be provided when running the query
 */
public class FHIRQuery {

    // the top level select statement
    private final Select select;

    // the bind variables associated with the statement
    private final List<IStatementParam> params;

    /**
     * Public constructor
     * @param select
     * @param params
     */
    public FHIRQuery(Select select, List<IStatementParam> params) {
        this.select = select;
        this.params = new ArrayList<>(params);
    }

    /**
     * Add each bind value to the statement
     * @param ps
     */
    public void setValues(PreparedStatement ps) {
        int posn = 1;
        for (IStatementParam param: params) {
            params.set(posn++, param);
        }
    }

    public String render() {
        return select.toString();
    }
}