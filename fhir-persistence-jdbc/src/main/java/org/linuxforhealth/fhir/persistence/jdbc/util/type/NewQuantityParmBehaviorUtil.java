/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util.type;

import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.CODE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.QUANTITY_VALUE;

import java.util.HashSet;
import java.util.Set;

import org.linuxforhealth.fhir.database.utils.query.WhereFragment;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;

/**
 * Quantity parameter handling using the Select model.
 *
 * <a href="https://hl7.org/fhir/search.html#quantity">FHIR Search - Quantity</a>
 * <br>
 * This utility encapsulates the logic specific to fhir-search related to
 * quantity.
 */
public class NewQuantityParmBehaviorUtil {

    // Cache for code-system lookups
    private final JDBCIdentityCache identityCache;
    public NewQuantityParmBehaviorUtil(JDBCIdentityCache identityCache) {
        this.identityCache = identityCache;
    }

    public void executeBehavior(WhereFragment whereClauseSegment, QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {

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
                    whereClauseSegment.or();
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition
                    parmValueProcessed = true;
                }

                // Wrap the expression in parens in case we have multiple values being or'd...for safety
                whereClauseSegment.leftParen();
                NewNumberParmBehaviorUtil.addValue(whereClauseSegment, tableAlias, QUANTITY_VALUE, prefix, value.getValueNumber());
                addSystemIfPresent(whereClauseSegment, tableAlias, value.getValueSystem());
                addCodeIfPresent(whereClauseSegment, tableAlias, value.getValueCode());
                whereClauseSegment.rightParen();
            }
        }
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
    public void addSystemIfPresent(WhereFragment whereClauseSegment, String tableAlias, String system) throws FHIRPersistenceException {
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
            Integer systemId = this.identityCache.getCodeSystemId(system);
            if (systemId == null) {
                // This is an invalid number in the sequence.
                // All of our sequences start with 1 and NO CYCLE.
                systemId = -1;
            }

            whereClauseSegment.and(tableAlias, CODE_SYSTEM_ID).eq().bind(systemId);
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
    public void addCodeIfPresent(WhereFragment whereClauseSegment, String tableAlias, String code) {
        // Include code if present.
        if (isPresent(code)) {
            whereClauseSegment.and(tableAlias, CODE).eq().bind(code);
        }
    }

    public boolean isPresent(String value) {
        return value != null && !value.isEmpty();
    }
}