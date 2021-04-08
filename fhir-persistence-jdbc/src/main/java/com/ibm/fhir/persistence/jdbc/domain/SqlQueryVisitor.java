/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import com.ibm.fhir.database.utils.query.Select;

/**
 * Build a Select statement
 */
public class SqlQueryVisitor implements DomainQueryVisitor<Select> {

    @Override
    public Select build(String foo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Select tokenParam(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Select referenceParam(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Select stringParam(String paramName, String operator, boolean lowerCaseMatch, String value) {
        return null;
    }
}