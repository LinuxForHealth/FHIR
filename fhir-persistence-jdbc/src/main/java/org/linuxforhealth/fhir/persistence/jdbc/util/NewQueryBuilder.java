/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.EQ;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;
import static org.linuxforhealth.fhir.search.SearchConstants.ID;
import static org.linuxforhealth.fhir.search.SearchConstants.LAST_UPDATED;
import static org.linuxforhealth.fhir.search.SearchConstants.PROFILE;
import static org.linuxforhealth.fhir.search.SearchConstants.SECURITY;
import static org.linuxforhealth.fhir.search.SearchConstants.TAG;
import static org.linuxforhealth.fhir.search.SearchConstants.URL;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.query.Select;
import org.linuxforhealth.fhir.model.resource.Location;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import org.linuxforhealth.fhir.persistence.jdbc.connection.QueryHints;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import org.linuxforhealth.fhir.persistence.jdbc.domain.CanonicalSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.ChainedSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.CompositeSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.DateSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.DomainSortParameter;
import org.linuxforhealth.fhir.persistence.jdbc.domain.IdSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.InclusionSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.LastUpdatedSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.LocationSearchExtension;
import org.linuxforhealth.fhir.persistence.jdbc.domain.LocationSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.MissingSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.NumberSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.QuantitySearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.QueryData;
import org.linuxforhealth.fhir.persistence.jdbc.domain.ReferenceSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchCountQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchDataQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchIncludeQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchQueryRenderer;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchSortQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchWholeSystemDataQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchWholeSystemFilterQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SearchWholeSystemQuery;
import org.linuxforhealth.fhir.persistence.jdbc.domain.SecuritySearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.StringSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.TagSearchParam;
import org.linuxforhealth.fhir.persistence.jdbc.domain.TokenSearchParam;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.location.util.LocationUtil;
import org.linuxforhealth.fhir.search.parameters.InclusionParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;
import org.linuxforhealth.fhir.search.parameters.SortParameter;

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
    private static final Logger log = java.util.logging.Logger.getLogger(NewQueryBuilder.class.getName());
    private static final String CLASSNAME = NewQueryBuilder.class.getName();

    // Database translator to handle SQL syntax variations among databases
    private final IDatabaseTranslator translator;
    
    // For id lookups
    private final JDBCIdentityCache identityCache;

    // Hints to use for certain queries
    private final QueryHints queryHints;

    // Enable use of legacy whole-system search parameters for the search request
    private final boolean legacyWholeSystemSearchParamsEnabled;

    /**
     * Public constructor
     * @param translator
     * @param queryHints
     * @param identityCache
     */
    public NewQueryBuilder(IDatabaseTranslator translator, QueryHints queryHints, JDBCIdentityCache identityCache) {
        this.translator = translator;
        this.queryHints = queryHints;
        this.identityCache = identityCache;
        this.legacyWholeSystemSearchParamsEnabled =
                FHIRConfigHelper.getBooleanProperty(PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS, false);
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
     * @param schemaType    - The type of schema we are querying (PLAIN, DISTRIBUTED etc)
     * @return String - A count query SQL statement
     * @throws Exception
     */
    public Select buildCountQuery(Class<?> resourceType, FHIRSearchContext searchContext, SchemaType schemaType) throws Exception {
        final String METHODNAME = "buildCountQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        final SearchQuery domainModel;
        if (Resource.class.equals(resourceType)) {
            // Whole-system search
            if (allSearchParmsAreGlobal(searchContext.getSearchParameters())) {
                // Can do query against global tables
                domainModel = new SearchCountQuery(resourceType.getSimpleName());
                if (searchContext.getSearchResourceTypes() != null) {
                    // The _type parameter was specified, so we need to filter the
                    // query by resource type. Add an extension to the domain model
                    // with the specified resource type IDs.
                    addResourceTypeExtension(domainModel, searchContext.getSearchResourceTypes());
                }
            } else {
                // Not all search parameters are global (values indexed into global values tables).
                // Need to do old-style UNION'd query against all resource types.
                List<String> resourceTypes = searchContext.getSearchResourceTypes();
                if (resourceTypes == null) {
                    // The _type parameter was not specified, so we need to generate a list
                    // of all supported resource types for our UNION query.
                    resourceTypes = this.identityCache.getResourceTypeNames();
                    resourceTypes.remove("Resource");
                    resourceTypes.remove("DomainResource");
                }
                // Create a domain model for each resource type
                List<SearchQuery> subDomainModels = new ArrayList<>();
                for (String domainResourceType : resourceTypes) {
                    SearchQuery subDomainModel = new SearchCountQuery(domainResourceType);
                    buildModelCommon(subDomainModel, ModelSupport.getResourceType(domainResourceType), searchContext);
                    subDomainModels.add(subDomainModel);
                }
                // Create a wrapper whole-system search domain model
                domainModel = new SearchWholeSystemQuery(subDomainModels, true, false);
            }
        } else {
            domainModel = new SearchCountQuery(resourceType.getSimpleName());
        }
        buildModelCommon(domainModel, resourceType, searchContext);
        Select result = renderQuery(domainModel, searchContext, schemaType);

        log.exiting(CLASSNAME, METHODNAME);
        return result;
    }

    /**
     * Render the domain model into a Select statement
     * @param domainModel
     * @param searchContext
     * @param schemaType
     * @return
     */
    private Select renderQuery(SearchQuery domainModel, FHIRSearchContext searchContext, SchemaType schemaType) throws FHIRPersistenceException {
        // adjust the offset by -1 of the search is for any other page than the first page.
        int offsetIncrement = getOffsetCounter(searchContext);
        // increase the value of row count by 1 for first page, 2 for any other pages of search.
        int rowCountIncrement = getAdditionalRowCount(searchContext);
        
        final int offset = ((searchContext.getPageNumber()-1) * searchContext.getPageSize()) + offsetIncrement;
        final int rowsPerPage = searchContext.getPageSize() + rowCountIncrement;
        // System.out.println("############### offsetIncrement: "+offsetIncrement +" rowCountIncrement: "+rowCountIncrement +" offset: "+offset +" pageSize: " + searchContext.getPageSize() +" rowsPerPage: "+rowsPerPage);
        SearchQueryRenderer renderer = new SearchQueryRenderer(this.translator, this.identityCache, offset, rowsPerPage, searchContext.isIncludeResourceData(), schemaType);
        QueryData queryData = domainModel.visit(renderer);
        return queryData.getQuery().build();
    }
    
    /**
     * Fetch the value by which the row count needs to be increased in the Select statement.
     * The additional records and needed to validate if search pages have shifted in an ongoing search session with pagination.
     * If the request is for the first page of search results then re
     * @param searchContext The search context containing the search parameters.
     * @return
     * If the request is for the first page of search results then return 1 since one additional record is needed at the end of search results.
     * If the request is for any other page then return 2 since two additional records are needed, one at the beginning of the search results
     * and one at the end of search results.
     */
    private int getAdditionalRowCount(FHIRSearchContext searchContext) {
        if (searchContext.getPageSize() > 0) {
            if (searchContext.getPageNumber() == 1) {
                return 1;
            } else {
                return 2;
            }
        }
        return 0;
    }
    
    /**
     * Fetch the value by which the offset needs to be adjusted in the Select statement.
     * The offset needs to be adjusted for all other pages other than the first page of the search results.
     * @param searchContext The search context containing the search parameters.
     * @return
     * If the request is for the first page of search results then return 0.
     * If the request is for any other page then return -1.
     */
    private int getOffsetCounter(FHIRSearchContext searchContext) {
        if (searchContext.getPageNumber() == 1) {
            return 0;
        } else {
            return -1;
        }
    }
    

    /**
     * Construct a FHIR search query
     * @param resourceType
     * @param searchContext
     * @param schemaType    - The type of schema we are querying (PLAIN, DISTRIBUTED etc)
     * @return
     * @throws Exception
     */
    public Select buildQuery(Class<?> resourceType, FHIRSearchContext searchContext, SchemaType schemaType) throws Exception {
        final String METHODNAME = "buildQuery";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        final SearchQuery domainModel;
        if (Resource.class.equals(resourceType)) {
            // Whole-system search
            if (allSearchParmsAreGlobal(searchContext.getSearchParameters())) {
                // Can do a filter query against global tables
                SearchWholeSystemFilterQuery wholeSystemFilterQuery = new SearchWholeSystemFilterQuery();
                for (SortParameter sp: searchContext.getSortParameters()) {
                    wholeSystemFilterQuery.add(new DomainSortParameter(sp));
                }
                if (searchContext.getSearchResourceTypes() != null) {
                    // The _type parameter was specified, so we need to filter the
                    // query by resource type. Add an extension to the domain model
                    // with the specified resource type IDs.
                    addResourceTypeExtension(wholeSystemFilterQuery, searchContext.getSearchResourceTypes());
                }
                domainModel = wholeSystemFilterQuery;
            } else {
                // Not all search parameters are global (values indexed into global values tables).
                // Need to do old-style UNION'd query against all resource types.
                List<String> resourceTypes = searchContext.getSearchResourceTypes();
                if (resourceTypes == null) {
                    // The _type parameter was not specified, so we need to generate a list
                    // of all supported resource types for our UNION query.
                    resourceTypes = this.identityCache.getResourceTypeNames();
                    resourceTypes.remove("Resource");
                    resourceTypes.remove("DomainResource");
                }
                // Create a domain model for each resource type
                List<SearchQuery> subDomainModels = new ArrayList<>();
                for (String domainResourceType : resourceTypes) {
                    int domainResourceTypeId = identityCache.getResourceTypeId(domainResourceType);
                    SearchQuery subDomainModel = new SearchDataQuery(domainResourceType, false, false, domainResourceTypeId);
                    buildModelCommon(subDomainModel, ModelSupport.getResourceType(domainResourceType), searchContext);
                    subDomainModels.add(subDomainModel);
                }
                // Create a wrapper whole-system search domain model
                SearchWholeSystemQuery wholeSystemAllDataQuery =
                        new SearchWholeSystemQuery(subDomainModels, false, true);
                for (SortParameter sp: searchContext.getSortParameters()) {
                    wholeSystemAllDataQuery.add(new DomainSortParameter(sp));
                }
                domainModel = wholeSystemAllDataQuery;
           }
        } else if (searchContext.hasSortParameters()) {
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
        Select result = renderQuery(domainModel, searchContext, schemaType);

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
     * @param schemaType    - The type of schema we are querying (PLAIN, DISTRIBUTED etc)
     * @return Select the query to fetch the matching list of included resources
     * @throws Exception
     */
    public Select buildIncludeQuery(Class<?> resourceType, FHIRSearchContext searchContext,
            InclusionParameter inclusionParm, List<Long> logicalResourceIds, String inclusionType,
            SchemaType schemaType) throws Exception {
        final String METHODNAME = "buildIncludeQuery";
        log.entering(CLASSNAME, METHODNAME,
            new Object[] { resourceType.getSimpleName(), inclusionParm });

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
            result = renderQuery(domainModel, searchContext, schemaType);
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
     * Builds a query that returns resource data for the specified whole-system search.
     *
     * @param searchContext - the search context.
     * @param resourceTypeIdToLogicalResourceIdMap - map of resource type Ids to logical resource Ids
     * @param schemaType    - The type of schema we are querying (PLAIN, DISTRIBUTED etc)
     * @return Select the query to fetch the specified list of resources
     * @throws Exception
     */
    public Select buildWholeSystemDataQuery(FHIRSearchContext searchContext, Map<Integer,
            List<Long>> resourceTypeIdToLogicalResourceIdMap, SchemaType schemaType) throws Exception {
        final String METHODNAME = "buildWholeSystemDataQuery";
        log.entering(CLASSNAME, METHODNAME);

        // Create domain model for each resource type found by the filter query
        List<SearchQuery> subDomainModels = new ArrayList<>();
        for (Integer resourceTypeId : resourceTypeIdToLogicalResourceIdMap.keySet()) {
            String resourceType = identityCache.getResourceTypeName(resourceTypeId);
            List<Long> logicalResourceIds = resourceTypeIdToLogicalResourceIdMap.get(resourceTypeId);
            SearchQuery subDomainModel = new SearchWholeSystemDataQuery(resourceType, resourceTypeId);
            subDomainModel.add(new WholeSystemDataExtension(resourceType, logicalResourceIds));
            buildModelCommon(subDomainModel, ModelSupport.getResourceType(resourceType), searchContext);
            subDomainModels.add(subDomainModel);
        }
        // Create whole-system search domain model
        SearchWholeSystemQuery wholeSystemAllDataQuery =
                new SearchWholeSystemQuery(subDomainModels, false, false);
        for (SortParameter sp: searchContext.getSortParameters()) {
            wholeSystemAllDataQuery.add(new DomainSortParameter(sp));
        }
        final SearchQuery domainModel = wholeSystemAllDataQuery;

        buildModelCommon(domainModel, Resource.class, searchContext);
        Select result = renderQuery(domainModel, searchContext, schemaType);

        log.exiting(CLASSNAME, METHODNAME);
        return result;
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
                if (ID.equals(leftParameter.getCode())) {
                    result = -100;
                } else if (LAST_UPDATED.equals(leftParameter.getCode())) {
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
            // DATE parameters will be processed separately after other parameters
            if (!Type.DATE.equals(queryParameter.getType())) {
                processQueryParameter(domainModel, resourceType, queryParameter);
            }
        }

        // Special logic for search parameters of type DATE. We may be able to
        // consolidate multiple parameters into a single parameter.
        consolidateDateParms(domainModel, resourceType, searchParameters);

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
        if (log.isLoggable(Level.FINER)) {
            log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        }

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
            } else if (ID.equals(code)) {
                domainModel.add(new IdSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
            } else if (LAST_UPDATED.equals(code)) {
                domainModel.add(new LastUpdatedSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
            } else {
                final Type type = queryParm.getType();
                switch (type) {
                case STRING:
                    domainModel.add(new StringSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
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
                    if (!this.legacyWholeSystemSearchParamsEnabled && TAG.equals(queryParm.getCode())) {
                        domainModel.add(new TagSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else if (!this.legacyWholeSystemSearchParamsEnabled && SECURITY.equals(queryParm.getCode())) {
                        domainModel.add(new SecuritySearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
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
                    if ((!this.legacyWholeSystemSearchParamsEnabled && PROFILE.equals(queryParm.getCode()))
                            || URL.equals(queryParm.getCode()) || queryParm.isCanonical()) {
                        domainModel.add(new CanonicalSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    } else {
                        domainModel.add(new StringSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    }
                    break;
                case COMPOSITE:
                    domainModel.add(new CompositeSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
                    break;
                default:
                    throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            }
        } finally {
            if (log.isLoggable(Level.FINER)) {
                log.exiting(CLASSNAME, METHODNAME, new Object[] { queryParm });
            }
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

    private boolean allSearchParmsAreGlobal(List<QueryParameter> queryParms) {
        for (QueryParameter queryParm : queryParms) {
            if (!SearchConstants.SYSTEM_LEVEL_GLOBAL_PARAMETER_NAMES.contains(queryParm.getCode())) {
                return false;
            }
        }
        return true;
    }

    private void addResourceTypeExtension(SearchQuery domainModel, List<String> resourceTypes) throws FHIRPersistenceException {
        List<Integer> resourceTypeIds = new ArrayList<>();
        for (String resourceType : resourceTypes) {
            resourceTypeIds.add(this.identityCache.getResourceTypeId(resourceType));
        }
        domainModel.add(new WholeSystemResourceTypeExtension(resourceTypeIds));
    }

    /**
     * Process DATE parameters. If there are multiple query parameters specified for a
     * single DATE search parameter, we will attempt to consolidate in two ways:
     * 1. We will try to consolidate all query parameters which constrain a lower or upper
     *    bound of the search into a single query parameter (i.e. if there are query
     *    parameters with 'le', 'lt', and 'eb' prefixes, we will try to consolidate them
     *    into a single query parameter.
     * 2. We will chain together all consolidated query parameters so that the query
     *    builder can do a single JOIN against the xx_date_values table rather than one
     *    JOIN per query parameter. For example, a search of:
     *       'Patient?birthdate=gt1980-01-01&birthdate=lt2020-01-01'
     *    will generate two JOINS to the Patient_DATE_VALUES table if the query parameters
     *    are not consolidated. However, when consolidated into a chained DATE query parameter,
     *    the query builder will generate a single JOIN.
     * We will not attempt to consolidate query parameters that have a modifier specified,
     * or that are chain or inclusion parameters, or that have multiple parameter values.
     * Those will be processed as normal DATE query parameters.
     *
     * @param domainModel
     * @param resourceType
     * @param searchParameters
     * @throws Exception
     */
    private void consolidateDateParms(SearchQuery domainModel, Class<?> resourceType, List<QueryParameter> searchParameters)
            throws Exception {

        // We only need to attempt to consolidate if we have multiple query parameters for the
        // same search parameter name. Loop through the search parameters, mapping parameter name
        // to parameter(s) to determine if this is the case.
        Map<String,List<QueryParameter>> consolidationMap = new HashMap<>();
        for (QueryParameter searchParameter : searchParameters) {
            if (Type.DATE.equals(searchParameter.getType())) {
                consolidationMap.computeIfAbsent(searchParameter.getCode(), k -> new ArrayList<>()).add(searchParameter);
            }
        }

        // Now loop through the map to find any cases of same parameter specified multiple times.
        // If found, we will attempt to consolidate. If not, we will simply process as a normal
        // date parameter.
        for (Map.Entry<String,List<QueryParameter>> entry : consolidationMap.entrySet()) {
            List<QueryParameter> queryParameters = entry.getValue();
            boolean eligibleToConsolidate = true;
            if (queryParameters.size() == 1 || LAST_UPDATED.equals(entry.getKey())) {
                eligibleToConsolidate = false;
            } else {
                // We have multiple parameters. If any of the parameters have a modifier specified, or
                // if chain or inclusion parameter, or if multiple values, don't attempt to consolidate.
                for (QueryParameter queryParm : queryParameters) {
                    List<QueryParameterValue> queryParmValues = queryParm.getValues();
                    if (queryParm.getModifier() != null || queryParm.isChained() ||
                            queryParm.isInclusionCriteria() || queryParmValues.size() > 1) {
                        eligibleToConsolidate = false;
                        break;
                    }
                }
            }

            if (eligibleToConsolidate) {
                // Attempt to consolidate the upper and lower bound constraints
                List<QueryParameter> consolidatedParms = new ArrayList<>();
                Instant gteBound = null;
                Instant lteBound = null;
                Instant saBound = null;
                Instant ebBound = null;
                QueryParameter gteBoundParm = null;
                QueryParameter lteBoundParm = null;
                QueryParameter saBoundParm = null;
                QueryParameter ebBoundParm = null;
                for (QueryParameter queryParm : queryParameters) {
                    QueryParameterValue queryParmValue = queryParm.getValues().get(0);
                    Prefix prefix = queryParmValue.getPrefix();
                    Instant valueLowerBound = queryParmValue.getValueDateLowerBound();
                    Instant valueUpperBound = queryParmValue.getValueDateUpperBound();
                    switch(prefix) {
                    case GT:
                        if (gteBound == null || valueUpperBound.isAfter(gteBound) || valueUpperBound.equals(gteBound)) {
                            gteBound = valueUpperBound;
                            gteBoundParm = queryParm;
                        }
                        break;
                    case GE:
                        if (gteBound == null || valueLowerBound.isAfter(gteBound)) {
                            gteBound = valueLowerBound;
                            gteBoundParm = queryParm;
                        }
                        break;
                    case SA:
                        if (saBound == null || valueUpperBound.isAfter(saBound)) {
                            saBound = valueUpperBound;
                            saBoundParm = queryParm;
                        }
                        break;
                    case LT:
                        if (lteBound == null || valueLowerBound.isBefore(lteBound) || valueLowerBound.equals(lteBound)) {
                            lteBound = valueLowerBound;
                            lteBoundParm = queryParm;
                        }
                        break;
                    case LE:
                        if (lteBound == null || valueUpperBound.isBefore(lteBound)) {
                            lteBound = valueUpperBound;
                            lteBoundParm = queryParm;
                        }
                        break;
                    case EB:
                        if (ebBound == null || valueLowerBound.isBefore(ebBound)) {
                            ebBound = valueLowerBound;
                            ebBoundParm = queryParm;
                        }
                        break;
                    default:
                        // If not a simple bound constraint, add to list to be processed as is
                        consolidatedParms.add(queryParm);
                    }
                }

                // Add the consolidated parms
                if (saBound != null) {
                    // Add the SA queryParm with the most restrictive bound
                    consolidatedParms.add(saBoundParm);
                    if (gteBound != null &&
                            (saBound.isAfter(gteBound) || saBound.equals(gteBound))) {
                        // ignore the GT/GE queryParm since SA queryParm is more restrictive
                        gteBound = null;
                    }
                }
                if (gteBound != null) {
                    // Add the GT/GE queryParm with the most restrictive bound
                    consolidatedParms.add(gteBoundParm);
                }
                if (ebBound != null) {
                    // Add the EB queryParm with the most restrictive bound
                    consolidatedParms.add(ebBoundParm);
                    if (lteBound != null &&
                            (ebBound.isBefore(lteBound) || ebBound.equals(lteBound))) {
                        // ignore the LT/LE queryParm since EB queryParm is more restrictive
                        lteBound = null;
                    }
                }
                if (lteBound != null) {
                    // Add the LT/LE queryParm with the most restrictive bound
                    consolidatedParms.add(lteBoundParm);
                }

                // Chain all the consolidated parms together - need to make copies since we're
                // modifying by chaining.
                QueryParameter consolidatedDateParm = null;
                for (QueryParameter consolidatedParm : consolidatedParms) {
                    QueryParameter cp = new QueryParameter(consolidatedParm.getType(), consolidatedParm.getCode(),
                        null, null, consolidatedParm.getValues());
                    if (consolidatedDateParm == null) {
                        consolidatedDateParm = cp;
                    } else {
                        if (consolidatedDateParm.getChain().isEmpty()) {
                            consolidatedDateParm.setNextParameter(cp);
                        } else {
                            consolidatedDateParm.getChain().getLast().setNextParameter(cp);
                        }
                    }
                }

                // Process new consolidated DATE parameter
                domainModel.add(new DateSearchParam(
                    resourceType.getSimpleName(), consolidatedDateParm.getCode(), consolidatedDateParm));
            } else {
                // Process as normal date parameters
                for (QueryParameter queryParm : queryParameters) {
                    processQueryParameter(domainModel, resourceType, queryParm);
                }
            }
        }
    }
}