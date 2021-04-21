/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * An inclusion search parameter
 */
public class InclusionSearchParam extends SearchParam {
    private static final String CLASSNAME = TokenSearchParam.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    /**
     * Public constructor
     * @param name
     */
    public InclusionSearchParam(String rootResourceName, String name, QueryParameter queryParameter) {
        super(rootResourceName, name, queryParameter);
    }

    @Override
    public <T> T visit(T queryData, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {

        QueryParameter queryParm = getQueryParameter();
        final String METHODNAME = "visit";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        return visitor.addInclusionParam(queryData, getRootResourceType(), queryParm);
    }
}
