/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import java.util.logging.Logger;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.QueryParameter;

/**
 * Search parameter chaining. We try to support both forward and reverse parameter
 * chaining here because one chain can be mixed
 */
public class ChainedSearchParam extends SearchParam {
    private static final String CLASSNAME = ChainedSearchParam.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    /**
     * Public constructor
     * @param name
     */
    public ChainedSearchParam(String rootResourceType, String name, QueryParameter queryParameter) {
        super(rootResourceType, name, queryParameter);
    }

    @Override
    public <T> T visit(T query, SearchQueryVisitor<T> visitor) throws FHIRPersistenceException {
        final String METHODNAME = "processChainedReferenceParm";
        QueryParameter currentParm = getQueryParameter();
        logger.entering(CLASSNAME, METHODNAME, currentParm.toString());

        // The query representing the last exists to be added, to
        // which we'll add the final filter if there is one
        T currentSubQuery = query;

        // Walk the parameter chain, nesting an EXISTS clause each time
        while (currentParm != null) {
            QueryParameter nextParm = currentParm.getNextParameter();

            if (currentParm.isChained()) {
                currentSubQuery = addChained(currentSubQuery, visitor, currentParm);
            } else if (currentParm.isReverseChained()) {
                currentSubQuery = addReverseChained(currentSubQuery, visitor, currentParm);
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
     * filter, or a composite (which is slightly more complex, and could be multiple EXISTS).
     * @param <T>
     * @param currentSubQuery
     * @param visitor
     * @param currentParm
     * @throws FHIRPersistenceException
     */
    private <T> void addFinalFilter(T currentSubQuery, SearchQueryVisitor<T> visitor, QueryParameter currentParm) throws FHIRPersistenceException {
        if (currentParm.getType() == Type.COMPOSITE) {
            visitor.addCompositeParam(currentSubQuery, currentParm.getModifierResourceTypeName(), currentParm);
        } else {
            visitor.addFilter(currentSubQuery, currentParm);
        }
    }

    /*
        while (currentParm != null) {
            QueryParameter nextParameter = currentParm.getNextParameter();
            if (nextParameter != null) {
                if (refParmIndex == 0) {
                    // Must build this first piece using px placeholder table alias, which will be replaced with a
                    // generated value in the buildQuery() method. The CODE_SYSTEM_ID filter is added for issue #1366
                    // due to the normalization of token values
                    // Build this piece:P1.PARAMETER_NAME_ID = x AND AND P1.CODE_SYSTEM_ID = x AND (p1.TOKEN_VALUE IN
                    // TODO this.populateNameIdSubSegment(whereClauseSegment, currentParm.getCode(), PARAMETER_TABLE_ALIAS);

                    // The resource type of the reference is encoded as the code system associated with the token value
                    // so we need to add a filter to ensure we don't match logical-ids for other resource types
                    // Note if the match is for any resource, we simply don't filter on the resource type
                    final String codeSystemName = currentParm.getModifierResourceTypeName();
                    if (codeSystemName != null && !codeSystemName.equals("*")) {
                        Integer codeSystemId = identityCache.getCodeSystemId(codeSystemName);
                        whereClauseSegment.append(AND).append(PARAMETER_TABLE_ALIAS).append(DOT).append(CODE_SYSTEM_ID).append(EQ)
                                .append(nullCheck(codeSystemId));
                    }

                    whereClauseSegment.append(AND);
                    whereClauseSegment.append(LEFT_PAREN);
                    whereClauseSegment.append(PARAMETER_TABLE_ALIAS).append(DOT).append(TOKEN_VALUE).append(IN);

                    // Add an object to the model representing the first node in the chain
                    // currentChainObject =
                } else {
                    // Build this piece: CP1.PARAMETER_NAME_ID = x AND CP1.TOKEN_VALUE IN
                    // currentChainObject = appendMidChainParm(currentChainObject, whereClauseSegment, currentParm, chainedParmVar);
                }

                refParmIndex++;
                chainedResourceVar        = CR + refParmIndex;
                chainedLogicalResourceVar = CLR + refParmIndex;
                chainedParmVar            = CP + refParmIndex;

                // The * is a wildcard for any resource type. This occurs only in the case where a reference parameter
                // chain was built to represent a compartment search with chained inclusion criteria that includes a
                // wildcard.
                //
                // For this situation, a separate method is called, and further processing of the chain by this method
                // is halted.
                if (currentParm.getModifierResourceTypeName().equals("*")) {
                    this.processWildcardChainedRefParm(domainModel, currentParm, chainedResourceVar, chainedLogicalResourceVar,
                            chainedParmVar, whereClauseSegment, bindVariables);
                    break;
                }
                resourceTypeName = currentParm.getModifierResourceTypeName();
                // Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID ...
                // since #1366, we no longer need to prepend the resource-type-name
                whereClauseSegment.append(LEFT_PAREN);
                appendInnerSelect(whereClauseSegment, currentParm, resourceTypeName,
                        chainedResourceVar, chainedLogicalResourceVar, chainedParmVar);

//                whereClauseSegment.append(chainedLogicalResourceVar).append(DOT).append(IS_DELETED_NO).append(AND);

            } else {
                // This logic processes the LAST parameter in the chain.
                // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.TOKEN_VALUE = ?
                // TODO do we need to filter the code-system here too?
                if (chainedParmVar == null) {
                    chainedParmVar = CP + 1;
                }
                Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);

                String code = currentParm.getCode();
                SqlQueryData sqlQueryData;
                if ("_id".equals(code)) {
                    // The code '_id' is only going to be the end of the change as it is a base element.
                    // We know at this point this is an '_id' and at the tail of the parameter chain
                    sqlQueryData = buildChainedIdClause(currentParm, chainedParmVar);
                } else if ("_lastUpdated".equals(code)) {
                    // Build the rest: (LAST_UPDATED <operator> ?)
                    LastUpdatedParmBehaviorUtil util = new LastUpdatedParmBehaviorUtil(chainedLogicalResourceVar);
                    StringBuilder lastUpdatedWhereClause = new StringBuilder();
                    util.executeBehavior(lastUpdatedWhereClause, currentParm);

                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString(), util.getBindVariables());
                        // issue-2011 LAST_UPDATED now in both XXX_resources and XXX_logical_resources so we need an alias
//                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString()
//                        .replaceAll(LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME,
//                            chainedLogicalResourceVar + DOT + LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME), util.getBindVariables());
                } else {
                    // TODO
                    // processQueryParameter(domainModel, chainedResourceType, currentParm, chainedParmVar, chainedLogicalResourceVar, true);
                }
            }
            currentParm = currentParm.getNextParameter();
        }

        // Finally, ensure the correct number of right parens are inserted to balance the where clause segment.
        int rightParensRequired = queryParm.getChain().size() + 2;
        for (int i = 0; i < rightParensRequired; i++) {
            whereClauseSegment.append(RIGHT_PAREN);
        }

        // queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
        // return visitor.addTokenParam(query, resourceType, parameterNameId, commonTokenValueId);
        return null;
    }
    */
}