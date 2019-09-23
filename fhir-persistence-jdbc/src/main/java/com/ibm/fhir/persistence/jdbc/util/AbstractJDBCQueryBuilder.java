/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.logging.Logger;

import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.util.AbstractQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.parameters.Parameter;

/**
 * This class extends AbstractQueryBuilder with methods shared by the 'basic' and 'normalized' schema implementations.
 * @author lmsurpre
 *
 */
public abstract class AbstractJDBCQueryBuilder<T1, T2> extends AbstractQueryBuilder<T1, T2> {
    
    private static final Logger log = java.util.logging.Logger.getLogger(AbstractQueryBuilder.class.getName());
    private static final String CLASSNAME = AbstractQueryBuilder.class.getName();
    
    // Constants used in SQL query string construction
    protected static final String QUOTE = "'";
    protected static final String COMMA = ",";
    protected static final String DOT = ".";
    protected static final String EQUALS = "=";
    protected static final String WHERE = " WHERE ";
    protected static final String PARAMETERS_TABLE_ALIAS = "pX";
    protected static final String PARAMETERS_TABLE_ALIAS_QUALIFIER = PARAMETERS_TABLE_ALIAS + DOT;
    protected static final String LEFT_PAREN = "(";
    protected static final String RIGHT_PAREN = ")";
    protected static final String AND = " AND ";
    protected static final String BIND_VAR = "?";
    protected static final String PERCENT_WILDCARD = "%";
    protected static final String UNDERSCORE_WILDCARD = "_";
    protected static final String ESCAPE_CHAR = "+";
    protected static final String ESCAPE_UNDERSCORE = ESCAPE_CHAR + "_";
    protected static final String ESCAPE_PERCENT = ESCAPE_CHAR + PERCENT_WILDCARD;
    protected static final String ESCAPE_EXPR = " ESCAPE '" + ESCAPE_CHAR + "'";
    
    /**
     * An enumeration of SQL query operators.
     */
    public static enum JDBCOperator {
        EQ(" = "), 
        LIKE(" LIKE "), 
        IN(" IN "), 
        LT(" < "), 
        LTE(" <= "),
        GT(" > "), 
        GTE(" >= "),
        NE(" <> "), 
        OR(" OR "),
        AND(" AND ");
        
        private String value = null;
        
        JDBCOperator(String value) {
            this.value = value;
        }
        
        public String value() {
            return value;
        }
        
        public static JDBCOperator fromValue(String value) {
            for (JDBCOperator operator : JDBCOperator.values()) {
                if (operator.value.equalsIgnoreCase(value)) {
                    return operator;
                }
            }
            throw new IllegalArgumentException("No constant with value " + value + " found.");
        }
    }
    
    /**
     * Builds a query segment for the passed query parameter.
     * @param resourceType - A valid FHIR Resource type
     * @param queryParm - A Parameter object describing the name, value and type of search parm.
     * @return T1 - An object representing the selector query segment for the passed search parm.
     * @throws Exception 
     */
    protected T1 buildQueryParm(Class<?> resourceType, Parameter queryParm, String tableAlias) 
            throws Exception {
        final String METHODNAME = "buildQueryParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        
        T1 databaseQueryParm = null;
        Type type;
        
        try {
            if (Modifier.MISSING.equals(queryParm.getModifier())) {
                return this.processMissingParm(resourceType, queryParm, tableAlias);
            }
            // NOTE: The special logic needed to process NEAR and NEAR_DISTANCE query parms for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            if (! (Location.class.equals(resourceType) && 
                (queryParm.getName().equals(NEAR) || queryParm.getName().equals(NEAR_DISTANCE)))) {
                
                type = queryParm.getType();
                switch(type) {
                case STRING:    databaseQueryParm = this.processStringParm(queryParm, tableAlias);
                        break;
                case REFERENCE: if (queryParm.isChained()) {
                                    databaseQueryParm = this.processChainedReferenceParm(queryParm);
                                }
                                else if (queryParm.isInclusionCriteria()) {
                                    databaseQueryParm = this.processInclusionCriteria(queryParm);
                                }
                                else {
                                    databaseQueryParm = this.processReferenceParm(resourceType, queryParm, tableAlias);
                                }
                        break;
                case DATE:      databaseQueryParm = this.processDateParm(resourceType, queryParm, tableAlias);
                        break;
                case TOKEN:     databaseQueryParm = this.processTokenParm(queryParm, tableAlias);
                        break;
                case NUMBER:    databaseQueryParm = this.processNumberParm(resourceType, queryParm, tableAlias);
                        break;
                case QUANTITY:  databaseQueryParm = this.processQuantityParm(resourceType, queryParm, tableAlias);
                        break;
                case URI:       databaseQueryParm = this.processUriParm(queryParm, tableAlias);
                        break;
                
                default: throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            }
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] {databaseQueryParm});
        }
        return databaseQueryParm;
    }

    protected T1 processMissingParm(Class<?> resourceType, Parameter queryParm) throws FHIRPersistenceException {
        return processMissingParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }

    protected abstract T1 processMissingParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws FHIRPersistenceException;

    @Override
    protected T1 processNumberParm(Class<?> resourceType, Parameter queryParm) throws FHIRPersistenceException {
        return processNumberParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processNumberParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
    
    @Override
    protected T1 processDateParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processDateParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processDateParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception;
    
    @Override
    protected T1 processStringParm(Parameter queryParm) throws FHIRPersistenceException {
        return processStringParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processStringParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
    
    @Override
    protected T1 processTokenParm(Parameter queryParm) throws FHIRPersistenceException {
        return processTokenParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processTokenParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException;
    
    @Override
    protected T1 processReferenceParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processReferenceParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processReferenceParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception;
    
    @Override
    protected T1 processQuantityParm(Class<?> resourceType, Parameter queryParm) throws Exception {
        return processQuantityParm(resourceType, queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    protected abstract T1 processQuantityParm(Class<?> resourceType, Parameter queryParm, String tableAlias) throws Exception;
    
    @Override
    protected T1 processUriParm(Parameter queryParm) throws FHIRPersistenceException {
        return processUriParm(queryParm, PARAMETERS_TABLE_ALIAS);
    }
    
    /**
     * Creates a query segment for a URI type parameter.
     * @param queryParm - The query parameter. 
     * @param tableAlias - An alias for the table to query
     * @return T1 - An object containing query segment. 
     * @throws FHIRPersistenceException 
     */
    protected T1 processUriParm(Parameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processUriParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        
        T1 parmRoot;
        Parameter myQueryParm;
                
        myQueryParm = queryParm;
        Modifier queryParmModifier = queryParm.getModifier();
        // A BELOW modifier has the same behavior as a "starts with" String search parm. 
        if (queryParmModifier != null && queryParmModifier.equals(Modifier.BELOW)) {
             myQueryParm = new Parameter(queryParm.getType(), queryParm.getName(), null,
                                queryParm.getModifierResourceTypeName(), queryParm.getValues());
        }
        parmRoot = this.processStringParm(myQueryParm, tableAlias);
                        
        log.exiting(CLASSNAME, METHODNAME, parmRoot.toString());
        return parmRoot;
    }

}
