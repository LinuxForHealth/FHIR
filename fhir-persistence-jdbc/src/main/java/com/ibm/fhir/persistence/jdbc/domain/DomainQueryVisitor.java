/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;


/**
 * A visitor used by the count and data query models to build
 * the actual query (denoted by type T).
 */
public interface DomainQueryVisitor<T> {

    T build(String foo);

    /**
     * Add a token search join to the query being constructed
     * @param name
     * @return
     */
    T tokenParam(String name);

    /**
     * @param name
     * @return
     */
    T referenceParam(String name);

    /**
     * Add a string value exists clause to the query being constructed
     * @param paramName
     * @return
     */
    T stringParam(String paramName, String operator, boolean lowerCaseMatch, String value);
}
