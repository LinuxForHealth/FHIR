/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import static org.linuxforhealth.fhir.search.SearchConstants.PROFILE;
import static org.linuxforhealth.fhir.search.SearchConstants.SECURITY;
import static org.linuxforhealth.fhir.search.SearchConstants.TAG;
import static org.linuxforhealth.fhir.search.SearchConstants.URL;

import java.util.logging.Logger;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;

/**
 * Search parameter chaining. We try to support both forward and reverse parameter
 * chaining here because one chain can be mixed
 */
public class ChainedSearchParam extends SearchParam {
    private static final String CLASSNAME = ChainedSearchParam.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    /**
     * Public constructor
     * @param rootResourceType
     * @param name
     * @param queryParameter
     */
    public ChainedSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        QueryParameter currentParm = getQueryParameter();

        // The query representing the last exists to be added, to
        // which we'll add the final filter if there is one
        T currentSubQuery = query;

        // Walk the parameter chain, nesting an EXISTS clause each time
        while (currentParm != null) {
            QueryParameter nextParm = currentParm.getNextParameter();

            // need to check reverse-chained first, because reverse-chained params also have isChained() == true!
            if (currentParm.isReverseChained()) {
                currentSubQuery = addReverseChained(currentSubQuery, visitor, currentParm);
            } else if (currentParm.isChained()) {
                currentSubQuery = addChained(currentSubQuery, visitor, currentParm);
            } else if (nextParm == null) {
                addFinalFilter(currentSubQuery, visitor, currentParm);
            } else {
                logger.warning("intermediate chained search parameter must be chained or reverse-chained, not " + currentParm);
                throw new FHIRPersistenceException("Invalid search parameter chain");
            }

            currentParm = nextParm;
        }
        return query;
    }

    private <T> T addChained(T currentSubQuery, SearchQueryVisitor<T> visitor, QueryParameter currentParm) throws FHIRPersistenceException {
        // Ask the visitor to attach an exists sub-query to the currentSubQuery
        return visitor.addChained(currentSubQuery, currentParm);
    }

    private <T> T addReverseChained(T currentSubQuery, SearchQueryVisitor<T> visitor, QueryParameter currentParm) throws FHIRPersistenceException {
        return visitor.addReverseChained(currentSubQuery, currentParm);
    }

    /**
     * Add a final filter to the last element of the chain (the current query). This could be a simple parameter
     * filter, or a composite (which is slightly more complex, and could be multiple EXISTS), or a canonical.
     * @param <T>
     * @param currentSubQuery
     * @param visitor
     * @param currentParm
     * @throws FHIRPersistenceException
     */
    private <T> void addFinalFilter(T currentSubQuery, SearchQueryVisitor<T> visitor, QueryParameter currentParm) throws FHIRPersistenceException {
        if (currentParm.getModifier() == Modifier.MISSING) {
            // Process this final element as a MissingSearchParam
            MissingSearchParam msp = new MissingSearchParam(getRootResourceType(), getName(), currentParm);
            msp.visit(currentSubQuery, visitor);
        } else if (currentParm.getType() == Type.COMPOSITE) {
            visitor.addCompositeParam(currentSubQuery, currentParm);
        } else if (currentParm.isCanonical() || PROFILE.equals(currentParm.getCode()) ||
                (currentParm.getType() == Type.URI && URL.equals(currentParm.getCode()))) {
            visitor.addCanonicalParam(currentSubQuery, ((QueryData)currentSubQuery).getResourceType(), currentParm);
        } else if (TAG.equals(currentParm.getCode())) {
            visitor.addTagParam(currentSubQuery, ((QueryData)currentSubQuery).getResourceType(), currentParm);
        } else if (SECURITY.equals(currentParm.getCode())) {
            visitor.addSecurityParam(currentSubQuery, ((QueryData)currentSubQuery).getResourceType(), currentParm);
        } else {
            visitor.addFilter(currentSubQuery, getRootResourceType(), currentParm);
        }
    }
}