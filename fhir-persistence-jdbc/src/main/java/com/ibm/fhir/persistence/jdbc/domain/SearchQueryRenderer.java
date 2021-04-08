/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.domain;

import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.alias;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.on;
import static com.ibm.fhir.database.utils.query.expression.ExpressionUtils.string;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._RESOURCES;

import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.database.utils.query.SelectAdapter;
import com.ibm.fhir.database.utils.query.node.ExpNode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;

/**
 * Used to render the domain model into a physical, executable query
 * modeled as a Select statement. The domain model knows about resources
 * and parameters. This class is used to translate the logical structure
 * of the query into a physical one, using the correct table names, join
 * predicates and filter expressions.
 */
public class SearchQueryRenderer implements SearchQueryVisitor<SelectAdapter> {
    private static final Logger logger = Logger.getLogger(SearchQueryRenderer.class.getName());

    private final JDBCIdentityCache identityCache;

    // pagination page number
    private final int rowOffset;

    // pagination page size
    private final int rowsPerPage;

    /**
     * Public constructor
     * @param identityCache
     */
    public SearchQueryRenderer(JDBCIdentityCache identityCache,
        int rowOffset, int rowsPerPage) {
        this.identityCache = identityCache;
        this.rowOffset = rowOffset;
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * Get the table name for the xx_logical_resources table where xx is the
     * resource type name
     * @param resourceType
     * @return the table name
     */
    private String resourceLogicalResources(String resourceType) {
        return resourceType + _LOGICAL_RESOURCES;
    }

    /**
     * Get the table name for the xx_resources table where xx is the resource type name
     * @param resourceType
     * @return
     */
    private String resourceResources(String resourceType) {
        return resourceType + _RESOURCES;
    }

    /**
     * Get the id for the given parameter name (cache lookup)
     * @param parameterName
     * @return
     */
    private int getParameterNameId(String parameterName) throws FHIRPersistenceException {
        return this.identityCache.getParameterNameId(parameterName);
    }

    @Override
    public SelectAdapter countRoot(String rootResourceType) {
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String lrAliasName = "LR";

        // The basic count query from the xx_LOGICAL_RESOURCES table. Query
        // parameters are bolted on as exists statements in the WHERE clause.
        // No need to join with xx_RESOURCES, because we only need to count
        // undeleted logical resources, not individual resource versions
        SelectAdapter select = Select.select("COUNT(*)");
        select.from(xxLogicalResources, alias(lrAliasName))
            .where(lrAliasName, IS_DELETED).eq(string("N"));
        return select;
    }

    @Override
    public SelectAdapter dataRoot(String rootResourceType) {
        final String xxLogicalResources = resourceLogicalResources(rootResourceType);
        final String xxResources = resourceResources(rootResourceType);
        final String lrAliasName = "LR";

        // The core data query joining together the logical resources table
        // with the resources table for the current Query
        // parameters are bolted on as exists statements in the WHERE clause
        SelectAdapter select = Select.select("R.RESOURCE_ID", "R.LOGICAL_RESOURCE_ID", "R.VERSION_ID", "R.LAST_UPDATED", "R.IS_DELETED", "R.DATA", "LR.LOGICAL_ID");
        select.from(xxLogicalResources, alias(lrAliasName))
            .innerJoin(xxResources, alias("R"), on("LR", "CURRENT_RESOURCE_ID").eq("R", "RESOURCE_ID"))
            .where(lrAliasName, IS_DELETED).eq().literal("N");
        return select;
    }

    @Override
    public SelectAdapter addTokenParam(SelectAdapter query, String resourceType, int parameterNameId, Long commonTokenValueId) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String xxTokenValues = resourceType + "_TOKEN_VALUES_V";
        final String paramAlias = "p";
        Select exists = Select.select("1")
                .from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(parameterNameId)
                .and(paramAlias, "COMMON_TOKEN_VALUE_ID").eq(commonTokenValueId)
                .build();

        // Add the exists to the where clause of the main query
        query.from().where().and().exists(exists);
        return query;
    }

    @Override
    public SelectAdapter addStringParam(SelectAdapter query, String resourceType, String parameterName, ExpNode filter) throws FHIRPersistenceException {
        // Attach an exists clause to the query
        final String xxTokenValues = resourceType + "_STR_VALUES";
        final String paramAlias = "p";
        Select exists = Select.select("1")
                .from(xxTokenValues, alias(paramAlias))
                .where(paramAlias, "LOGICAL_RESOURCE_ID").eq("LR", "LOGICAL_RESOURCE_ID") // correlate with the main query
                .and(paramAlias, "PARAMETER_NAME_ID").eq(getParameterNameId(parameterName))
                .and(filter)
                .build();
                ;

        logger.fine("string param EXISTS: " + exists.toDebugString());

        // Add the exists to the where clause of the main query which already has a predicate
        // so we need to AND the exists
        query.from().where().and().exists(exists);
        return query;
    }

    @Override
    public SelectAdapter addSorting(SelectAdapter query) {
        query.from().orderBy("LR.LOGICAL_RESOURCE_ID");
        return query;
    }

    @Override
    public SelectAdapter addPagination(SelectAdapter query) {
        query.pagination(rowOffset, rowsPerPage);
        return query;
    }
}