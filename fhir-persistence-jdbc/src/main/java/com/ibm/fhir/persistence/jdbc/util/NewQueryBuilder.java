/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.JDBCConstants;
import com.ibm.fhir.persistence.jdbc.connection.QueryHints;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.domain.CanonicalSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.ChainedSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.CompositeSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.DateSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.DomainSortParameter;
import com.ibm.fhir.persistence.jdbc.domain.IdSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.InclusionSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.LastUpdatedSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.LocationSearchExtension;
import com.ibm.fhir.persistence.jdbc.domain.LocationSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.MissingSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.NumberSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.QuantitySearchParam;
import com.ibm.fhir.persistence.jdbc.domain.QueryData;
import com.ibm.fhir.persistence.jdbc.domain.ReferenceSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.SearchCountQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchDataQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchIncludeQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchQueryRenderer;
import com.ibm.fhir.persistence.jdbc.domain.SearchSortQuery;
import com.ibm.fhir.persistence.jdbc.domain.StringSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.TagSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.TokenSearchParam;
import com.ibm.fhir.persistence.jdbc.util.type.LastUpdatedParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.location.util.LocationUtil;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.SortParameter;

/**
 * This is the JDBC implementation of a query builder for the IBM FHIR Server
 * JDBC persistence layer schema.
 * The builder constructs an intermediate "domain" model of the query (which isn't
 * concerned about the intricacies of how to join two tables). This domain query
 * is then translated in a Select statement model which can be built piece by piece.
 * The statement is then rendered into a string which is the SQL select statement
 * executed by the database.
 * <br>
 * This approach improves maintainability by separating the logical structure
 * of the query from the physical join syntax.
 * <br>
 * <br>
 * Useful table reference:<br>
 *
 * <pre>
 * ------------------------
 * PARAMETER_NAMES        reference table of parameter names
 * RESOURCE_TYPES         reference table of resource type names
 * COMMON_TOKEN_VALUES    normalized set of token values
 * CODE_SYSTEMS           normalized set of code-system values
 * LOGICAL_RESOURCES      whole-system table of resources
 * xx_LOGICAL_RESOURCES   resource-specific table of logical resources
 * xx_RESOURCES           resource-specific table of resource versions
 * xx_STR_VALUES          string search parameters belonging to a resource
 * xx_RESOURCE_TOKEN_REFS map table connecting a given resource to a set of token values
 * xx_NUMBER_VALUES       number search parameters belonging to a resource
 * xx_DATE_VALUES         date search parameters belonging to a resource
 * xx_LATLNG_VALUES       lat/lng search parameters belonging to a resource
 * xx_QUANTITY_VALUES     quantity search parameters belonging to a resource
 * xx_TOKEN_VALUES_V      view hiding the join between xx_RESOURCE_TOKEN_REFS and COMMON_TOKEN_VALUES
 * </pre>
 * Useful column reference:<br>
 *
 * <pre>
 * ------------------------
 * RESOURCE_TYPE_NAME     the formal name of the resource type e.g. 'Patient'
 * RESOURCE_TYPE_ID       FK to the RESOURCE_TYPES table
 * LOGICAL_ID             the VARCHAR holding the logical-id of the resource. Unique for a given resource type
 * LOGICAL_RESOURCE_ID    the database BIGINT
 * CURRENT_RESOURCE_ID    the unique BIGINT id of the latest resource version for the logical resource
 * VERSION_ID             INT resource version number incrementing by 1
 * IS_DELETED             CHAR(1) flag indicating the current deletion status of the resource or resource-version.
 * RESOURCE_ID            the PK of the version-specific resource. Now only used as the target for CURRENT_RESOURCE_ID
 * </pre>
 */
public class NewQueryBuilder {
    private static final Logger log = java.util.logging.Logger.getLogger(JDBCQueryBuilder.class.getName());
    private static final String CLASSNAME = NewQueryBuilder.class.getName();

    // For id lookups
    private final JDBCIdentityCache identityCache;

    // Hints to use for certain queries
    private final QueryHints queryHints;

    /**
     * Public constructor
     * @param parameterDao
     * @param resourceDao
     * @param queryHints
     * @param identityCache
     */
    public NewQueryBuilder(QueryHints queryHints, JDBCIdentityCache identityCache) {
        this.queryHints = queryHints;
        this.identityCache = identityCache;
    }

    /**
     * Builds a query that returns the count of the search results that would be
     * found by applying the search parameters
     * contained within the passed search context.
     *
     * The count query is a simpler version of the main search query because
     * there's no need to join against the xx_RESOURCES table at the end because
     * the DATA column is not needed. This is now possible because the IS_DELETED
     * flag is now denormalized and stored at the xx_LOGICAL_RESOURCES level as
     * well as per resource version (in the xx_RESOURCES table).
     *
     * @param resourceType
     *                      - The type of resource being searched for.
     * @param searchContext
     *                      - The search context containing the search parameters.
     * @return String - A count query SQL statement
     * @throws Exception
     */
    public Select buildCountQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        SearchCountQuery domainModel = new SearchCountQuery(resourceType.getSimpleName());
        buildModelCommon(domainModel, resourceType, searchContext);

        Select result = renderQuery(domainModel, searchContext);

        log.exiting(CLASSNAME, METHODNAME);
        return result;
    }

    /**
     * Render the domain model into a Select statement
     * @param domainModel
     * @return
     */
    private Select renderQuery(SearchQuery domainModel, FHIRSearchContext searchContext) throws FHIRPersistenceException {
        final int offset = (searchContext.getPageNumber()-1) * searchContext.getPageSize();
        final int rowsPerPage = searchContext.getPageSize();
        SearchQueryRenderer renderer = new SearchQueryRenderer(this.identityCache, offset, rowsPerPage);
        QueryData queryData = domainModel.visit(renderer);
        return queryData.getQuery().build();
    }

    /**
     * Construct a FHIR search query
     * @param resourceType
     * @param searchContext
     * @return
     * @throws Exception
     */
    public Select buildQuery(Class<?> resourceType, FHIRSearchContext searchContext) throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        final SearchQuery domainModel;

        if (searchContext.hasSortParameters()) {
            // Special variant of the query which will sort based on the given sort params
            // and return a list of resource-ids which are then used to fetch the actual data
            // (matching the old query builder design...for now).
            SearchSortQuery sortQuery = new SearchSortQuery(resourceType.getSimpleName());
            for (SortParameter sp: searchContext.getSortParameters()) {
                sortQuery.add(new DomainSortParameter(sp));
            }
            domainModel = sortQuery;
        } else {
            domainModel = new SearchDataQuery(resourceType.getSimpleName());
        }
        buildModelCommon(domainModel, resourceType, searchContext);
        Select result = renderQuery(domainModel, searchContext);

        log.exiting(CLASSNAME, METHODNAME);
        return result;
    }

    /**
     * Builds a query that returns included resources.
     *
     * @param resourceType  - the type of resource being searched for.
     * @param searchContext - the search context containing the search parameters.
     * @param inclusionParm - the inclusion parameter for which the query is being built.
     * @param ids           - the list of logical resource IDs the query will run against.
     * @param inclusionType - either INCLUDE or REVINCLUDE.
     * @return Select the query to fetch the matching list of included resources
     * @throws Exception
     */
    public Select buildIncludeQuery(Class<?> resourceType, FHIRSearchContext searchContext,
            InclusionParameter inclusionParm, List<Long> logicalResourceIds, String inclusionType) throws Exception {
        final String METHODNAME = "buildIncludeQuery";
        log.entering(CLASSNAME, METHODNAME);

        // Build the special "include" query to fetch additional resources
        // that need to be included with the main search result
        final String includeResourceType;
        if (SearchConstants.INCLUDE.equals(inclusionType)) {
            log.fine("Building _include query");
            includeResourceType = inclusionParm.getSearchParameterTargetType();
        } else {
            log.fine("Building _revinclude query");
            includeResourceType = inclusionParm.getJoinResourceType();
        }

        // Start building a query model to fetch resources of the type we want to include
        final SearchQuery domainModel = new SearchIncludeQuery(includeResourceType);
        buildIncludeModel(domainModel, resourceType, searchContext, inclusionParm, logicalResourceIds, inclusionType);

        // Be careful - we need to override the searchContext here, because we don't want
        // to be using same pagination as the main query.
        int pageSize = searchContext.getPageSize();
        int pageNumber = searchContext.getPageNumber();
        searchContext.setPageSize(searchContext.getMaxPageIncludeCount()+1); // so we know when we have too many
        searchContext.setPageNumber(1); // only need the first page of includes
        final Select result;
        try {
            result = renderQuery(domainModel, searchContext);
        } finally {
            // reset the context
            searchContext.setPageSize(pageSize);
            searchContext.setPageNumber(pageNumber);
        }

        log.exiting(CLASSNAME, METHODNAME);
        return result;
    }

    /**
     * Build the include query used to fetch additional resources for _include
     * and _revinclude searches
     * @param domainModel
     * @param resourceType
     * @param searchContext
     * @param inclusionParm
     * @param logicalResourceIds
     * @param inclusionType
     */
    private void buildIncludeModel(SearchQuery domainModel, Class<?> resourceType, FHIRSearchContext searchContext, InclusionParameter inclusionParm,
        List<Long> logicalResourceIds, String inclusionType) {

        if (SearchConstants.INCLUDE.equals(inclusionType)) {
            domainModel.add(new IncludeExtension(inclusionParm, logicalResourceIds));
        } else {
            domainModel.add(new RevIncludeExtension(inclusionParm, logicalResourceIds));
        }

    }

    /**
     * Contains logic common to the building of both 'count' resource queries and
     * 'data' resource queries.
     * @param domainModel   Domain model of the query we are trying to build
     * @param resourceType
     *                      The type of FHIR resource being searched for.
     * @param searchContext
     *                      The search context containing search parameters.
     * @throws Exception
     */
    private void buildModelCommon(SearchQuery domainModel, Class<?> resourceType, FHIRSearchContext searchContext)
            throws Exception {
        final String METHODNAME = "buildQueryCommon";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        List<QueryParameter> searchParameters = searchContext.getSearchParameters();

        // Forces _id and _lastUpdated to come before all other parameters, which is good for this bit here
        // zero is used to for all other cases.
        searchParameters.sort(new Comparator<QueryParameter>() {
            @Override
            public int compare(QueryParameter leftParameter, QueryParameter rightParameter) {
                int result = 0;
                if (QuerySegmentAggregator.ID.equals(leftParameter.getCode())) {
                    result = -100;
                } else if (LastUpdatedParmBehaviorUtil.LAST_UPDATED.equals(leftParameter.getCode())) {
                    result = -90;
                }
                return result;
            }

        });

        if (Location.class.equals(resourceType)) {
            // Special logic for handling LocationPosition queries. These queries have interdependencies between
            // a couple of related input query parameters
            domainModel.add(new LocationSearchExtension(searchParameters));
        }

        // Add each query parameter to our domain model
        for (QueryParameter queryParameter : searchParameters) {
            processQueryParameter(domainModel, resourceType, queryParameter);
        }
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Process the given queryParameter and add the relevant artifacts to the
     * domain model (precursor to actually building the query)
     * @param domainModel
     * @param resourceType
     * @param queryParm
     */
    private void processQueryParameter(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processQueryParameter";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        try {
            if (Modifier.MISSING.equals(queryParm.getModifier())) {
                domainModel.add(new MissingSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                return;
            }
            // NOTE: The special logic needed to process NEAR query parms for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            final String code = queryParm.getCode();
            if (LocationUtil.isLocation(resourceType, queryParm)) {
                domainModel.add(new LocationSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
            } else if ("_id".equals(code)) {
                domainModel.add(new IdSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
            } else if ("_lastUpdated".equals(code)) {
                domainModel.add(new LastUpdatedSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
            } else {
                final Type type = queryParm.getType();
                switch (type) {
                case STRING:
                    if (JDBCConstants.PARAM_NAME_PROFILE.equals(queryParm.getCode())) {
                        domainModel.add(new CanonicalSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else {
                        domainModel.add(new StringSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    }
                    break;
                case REFERENCE:
                    if (queryParm.isReverseChained()) {
                        domainModel.add(new ChainedSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else if (queryParm.isChained()) {
                        domainModel.add(new ChainedSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else if (queryParm.isInclusionCriteria()) {
                        domainModel.add(new InclusionSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else {
                        domainModel.add(new ReferenceSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    }
                    break;
                case DATE:
                    domainModel.add(new DateSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                case TOKEN:
                    if (JDBCConstants.PARAM_NAME_TAG.equals(queryParm.getCode())) {
                        domainModel.add(new TagSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else {
                        domainModel.add(new TokenSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    }
                    break;
                case NUMBER:
                    domainModel.add(new NumberSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                case QUANTITY:
                    domainModel.add(new QuantitySearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                case URI:
                    domainModel.add(new StringSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                case COMPOSITE:
                    domainModel.add(new CompositeSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                default:
                    throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] { queryParm });
        }
    }

    protected String getOperator(QueryParameter queryParm) {
        final String METHODNAME = "getOperator(QueryParameter)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        String operator = LIKE;
        Modifier modifier = queryParm.getModifier();

        // In the case where a URI, we need specific behavior/manipulation
        // so that URI defaults to EQ, unless... BELOW
        if (Type.URI.equals(queryParm.getType())) {
            if (modifier != null && Modifier.BELOW.equals(modifier)) {
                operator = LIKE;
            } else {
                operator = EQ;
            }
        } else if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            operator = LIKE;
        }

        log.exiting(CLASSNAME, METHODNAME, operator);
        return operator;
    }

    /**
     * Map the Modifier in the passed Parameter to a supported query operator. If
     * the mapping results in the default
     * operator, override the default operator with the passed operator if the
     * passed operator is not null.
     *
     * @param queryParm
     *                        - A valid query Parameter.
     * @param defaultOverride
     *                        - An operator that should override the default
     *                        operator.
     * @return A supported operator.
     */
    protected String getOperator(QueryParameter queryParm, String defaultOverride) {
        final String METHODNAME = "getOperator(Parameter, String)";
        log.entering(CLASSNAME, METHODNAME, queryParm.getModifier());

        String operator = defaultOverride;
        Modifier modifier = queryParm.getModifier();

        if (modifier != null) {
            operator = modifierOperatorMap.get(modifier);
        }

        if (operator == null) {
            if (defaultOverride != null) {
                operator = defaultOverride;
            } else {
                operator = LIKE;
            }
        }

        log.exiting(CLASSNAME, METHODNAME, operator);
        return operator;
    }
}