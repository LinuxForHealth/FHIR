/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;

/**
 * A token search parameter for the _security search parameter
 */
public class SecuritySearchParam extends SearchParam {

    /**
     * Public constructor
     * @param rootResourceName
     * @param name
     * @param queryParameter
     */
    public SecuritySearchParam(String rootResourceName, String name, QueryParameter queryParameter) {
        super(rootResourceName, name, queryParameter);
    }

    @Override
    public <T> T visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        QueryParameter queryParm = getQueryParameter();
        return visitor.addSecurityParam(queryData, getRootResourceType(), queryParm);
    }
}