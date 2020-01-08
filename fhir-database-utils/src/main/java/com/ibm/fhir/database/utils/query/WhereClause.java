/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * The WhereClause SQL definition
 */
public class WhereClause {
    private List<String> predicates = new ArrayList<>();

    public void addPredicate(String predicate) {
        this.predicates.add(predicate);
    }

    @Override
    public String toString() {
        StringBuilder whereClauseBuilder = new StringBuilder();
        whereClauseBuilder.append("WHERE ");
        StringJoiner joiner = new StringJoiner(" ");
        for (String predicate : predicates) {
            joiner.add(predicate);
        }
        whereClauseBuilder.append(joiner.toString());
        return whereClauseBuilder.toString();
    }
}