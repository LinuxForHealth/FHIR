/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.util.CodeSystemsCache;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;

/**
 * <a href="https://hl7.org/fhir/search.html#quantity>FHIR Search - Quantity</a>
 * <br>
 * This utility encapsulates the logic specific to fhir-search related to
 * quantity.
 */
@Deprecated
public class QuantityParmBehaviorUtil {

    public QuantityParmBehaviorUtil() {
        // No operation
    }

    public void executeBehavior(StringBuilder whereClauseSegment, QueryParameter queryParm, List<Object> bindVariables,
            String tableAlias, ParameterDAO parameterDao)
            throws Exception {
        // Start the Clause
        // Query: AND ((
        whereClauseSegment.append(AND).append(LEFT_PAREN).append(LEFT_PAREN);

        // Process each parameter value in the query parameter
        boolean parmValueProcessed = false;
        Set<String> seen = new HashSet<>();
        for (QueryParameterValue value : queryParm.getValues()) {

            // Let's get the prefix.
            Prefix prefix = value.getPrefix();
            if (prefix == null) {
                // Default to EQ
                prefix = Prefix.EQ;
            }

            // seen is used to optimize against a repeated value passed in.
            // the hash must use the prefix and original values (reassembled).
            String hash =
                    prefix.value() + value.getValueNumber() + '|' + value.getValueSystem() + '|' + value.getValueCode();
            if (!seen.contains(hash)) {
                seen.add(hash);

                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    // ) OR (
                    whereClauseSegment.append(RIGHT_PAREN).append(OR).append(LEFT_PAREN);
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition
                    parmValueProcessed = true;
                }

                NumberParmBehaviorUtil.addValue(whereClauseSegment, bindVariables, tableAlias, QUANTITY_VALUE, prefix, value.getValueNumber());
                addSystemIfPresent(parameterDao, whereClauseSegment, tableAlias, bindVariables,
                        value.getValueSystem());
                addCodeIfPresent(whereClauseSegment, tableAlias, bindVariables,
                        value.getValueCode());
            }
        }

        // End the Clause started above, and closes the parameter expression.
        // Query: ))
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN).append(RIGHT_PAREN);
    }

    /**
     * adds the system if present.
     *
     * @param parameterDao
     * @param whereClauseSegment
     * @param tableAlias
     * @param bindVariables
     * @param system
     * @throws FHIRPersistenceException
     */
    public void addSystemIfPresent(ParameterDAO parameterDao, StringBuilder whereClauseSegment, String tableAlias,
            List<Object> bindVariables,
            String system) throws FHIRPersistenceException {
        /*
         * <code>GET
         * [base]/Observation?value-quantity=5.4|http://unitsofmeasure.org|mg</code>
         * system -> http://unitsofmeasure.org
         * <br>
         * In the above example, the system is unitsofmeasure.org
         * <br>
         * When a system is present, the following sql is returned:
         * <code>AND BASIC.CODE_SYSTEM_ID = ?</code>
         * -1 indicates the system is not found (rather than returning null)
         * 1 ... * - is used to indicate the key of the parameter in the parameters
         * table,
         * and to enable faster filtering.
         * <br>
         * This SQL is always an EXACT match unless a NOT modifier is used.
         * When :not is used, the semantics are treated as:
         * <code>value <> ? AND system = ? AND code = ?</code>
         */
        if (isPresent(system)) {
            Integer systemId = CodeSystemsCache.getCodeSystemId(system);
            if (systemId == null) {
                systemId = parameterDao.readCodeSystemId(system);
                if (systemId != null) {
                    // If found, we want to cache it.
                    parameterDao.addCodeSystemsCacheCandidate(system, systemId);
                } else {
                    // This is an invalid number in the sequence.
                    // All of our sequences start with 1 and NO CYCLE.
                    systemId = -1;
                }
            }

            // We shouldn't be adding to the query if it's NULL at this point.
            // What should we do?
            whereClauseSegment.append(AND).append(tableAlias).append(DOT)
                    .append(CODE_SYSTEM_ID)
                    .append(EQ).append(BIND_VAR);
            bindVariables.add(systemId);
        }
    }

    /**
     * add code if present.
     *
     * @param whereClauseSegment
     * @param tableAlias
     * @param bindVariables
     * @param code
     */
    public void addCodeIfPresent(StringBuilder whereClauseSegment, String tableAlias, List<Object> bindVariables,
            String code) {
        // Include code if present.
        if (isPresent(code)) {
            whereClauseSegment.append(AND).append(tableAlias + DOT).append(CODE)
                    .append(EQ).append(BIND_VAR);
            bindVariables.add(code);
        }
    }

    public boolean isPresent(String value) {
        return value != null && !value.isEmpty();
    }
}