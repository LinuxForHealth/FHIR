/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;


/**
 * Handles search for the missing param
 */
public class MissingSearchParam extends SearchParam {
    private static final Logger logger = Logger.getLogger(MissingSearchParam.class.getName());

    /**
     * @param rootResourceType
     * @param name
     * @param queryParameter
     */
    public MissingSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        QueryParameter queryParm = getQueryParameter();

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("MissingSearchParam::visit: " + getName() + ", type=" + queryParm.getType());
        }

        // boolean to track whether the user has requested the resources missing this parameter (true) or not missing it
        // (false)
        Boolean missing = null;
        for (QueryParameterValue parameterValue : queryParm.getValues()) {
            if (missing == null) {
                missing = Boolean.parseBoolean(parameterValue.getValueCode());
            } else {
                // multiple values would be very unusual, but I suppose we should handle it like an "or"
                if (missing != Boolean.parseBoolean(parameterValue.getValueCode())) {
                    // user has requested both missing and not missing values for this field which makes no sense
                    logger.warning("Processing query with conflicting values for query param with 'missing' modifier");
                    throw new FHIRPersistenceException("Processing query with conflicting values for query param with 'missing' modifier");
                }
            }
        }

        // Delegate the actual query building to the visitor
        if (Type.COMPOSITE.equals(queryParm.getType())) {
            return visitor.addCompositeParam(query, queryParm, missing == null || missing);
        } else {
            return visitor.addMissingParam(query, queryParm, missing == null || missing);
        }
    }
}