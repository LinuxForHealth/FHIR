/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.CODE_SYSTEM_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMA;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.COMMON_TOKEN_VALUE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EXISTS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.FROM;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.IS_DELETED_NO;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.JOIN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LIKE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LOGICAL_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LOGICAL_RESOURCE_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.ON;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_NAME_ID;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_ALIAS;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.PARAMETER_TABLE_NAME_PLACEHOLDER;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SELECT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.SPACE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.TOKEN_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.WHERE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants._LOGICAL_RESOURCES;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.modifierOperatorMap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.query.Select;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.connection.QueryHints;
import com.ibm.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ResourceDAO;
import com.ibm.fhir.persistence.jdbc.domain.ChainedSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.MissingSearchParam;
import com.ibm.fhir.persistence.jdbc.domain.QueryData;
import com.ibm.fhir.persistence.jdbc.domain.SearchCountQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchDataQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchQuery;
import com.ibm.fhir.persistence.jdbc.domain.SearchQueryRenderer;
import com.ibm.fhir.persistence.jdbc.domain.StringSearchParam;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.fhir.persistence.jdbc.util.type.DateParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.LastUpdatedParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.LocationParmBehaviorUtil;
import com.ibm.fhir.persistence.jdbc.util.type.QuantityParmBehaviorUtil;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.location.NearLocationHandler;
import com.ibm.fhir.search.location.bounding.Bounding;
import com.ibm.fhir.search.location.util.LocationUtil;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

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

    private final ParameterDAO parameterDao;
    private final ResourceDAO resourceDao;

    // For id lookups
    private final JDBCIdentityCache identityCache;

    // Hints to use for certain queries
    private final QueryHints queryHints;

    private int paramAliasCounter = 0;

    // Table aliases
    private static final String LR = "LR";

    // Table alias prefixes
    private static final String CR = "CR";
    private static final String CLR = "CLR";
    private static final String CP = "CP";

    /**
     * Public constructor
     * @param parameterDao
     * @param resourceDao
     * @param queryHints
     * @param identityCache
     */
    public NewQueryBuilder(ParameterDAO parameterDao, ResourceDAO resourceDao, QueryHints queryHints, JDBCIdentityCache identityCache) {
        this.parameterDao = parameterDao;
        this.resourceDao  = resourceDao;
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
     * @return String - A count query SQL string
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

        SearchDataQuery domainModel = new SearchDataQuery(resourceType.getSimpleName());
        buildModelCommon(domainModel, resourceType, searchContext);
        Select result = renderQuery(domainModel, searchContext);

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
     * @return QuerySegmentAggregator - A query builder helper containing processed
     *         query segments.
     * @throws Exception
     */
    private void buildModelCommon(SearchQuery domainModel, Class<?> resourceType, FHIRSearchContext searchContext)
            throws Exception {
        final String METHODNAME = "buildQueryCommon";
        log.entering(CLASSNAME, METHODNAME,
                new Object[] { resourceType.getSimpleName(), searchContext.getSearchParameters() });

        SqlQueryData querySegment;
        int nearParameterIndex;
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

        int pageSize = searchContext.getPageSize();
        int offset = (searchContext.getPageNumber() - 1) * pageSize;
        boolean isValidQuery = true;

//        helper =
//                QuerySegmentAggregatorFactory.buildQuerySegmentAggregator(resourceType, offset, pageSize,
//                        this.parameterDao, this.resourceDao, searchContext, this.queryHints, this.identityCache);

        // Special logic for handling LocationPosition queries. These queries have interdependencies between
        // a couple of related input query parameters
        if (Location.class.equals(resourceType)) {
            processLocationPosition(domainModel, searchParameters, PARAMETER_TABLE_ALIAS);
            // TODO
//            if (querySegment != null) {
//                nearParameterIndex = LocationUtil.findNearParameterIndex(searchParameters);
//                helper.addQueryData(querySegment, searchParameters.get(nearParameterIndex));
//            }
            // If there are Location-position parameters but a querySegment was not built,
            // the query would be invalid. Note that valid parameters could be found in the following
            // for loop.
//            else if (!searchParameters.isEmpty()) {
//                isValidQuery = false;
//            }
        }

        // Add each query parameter to our domain model
        for (QueryParameter queryParameter : searchParameters) {
            processQueryParameter(domainModel, resourceType, queryParameter, null, null, false);
        }
        log.exiting(CLASSNAME, METHODNAME);

    }

    /**
     * Make up a new alias name to use for parameter tables where we have multiple such tables
     * used in a single join
     * @return
     */
    private String getNextTableAlias() {
        return String.format("qp%02d", this.paramAliasCounter++);
    }

    /**
     * Process the given queryParameter and add the relevant artifacts to the
     * domain model (precursor to actually building the query)
     * @param domainModel
     * @param queryParameter
     */
    private void processQueryParameter(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain) throws Exception {
        final String METHODNAME = "processQueryParameter";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        if (paramTableAlias == null) {
            paramTableAlias = getNextTableAlias();
            logicalRsrcTableAlias = "LR";
        }
        SqlQueryData databaseQueryParm = null;
        Type type;

        try {
            if (Modifier.MISSING.equals(queryParm.getModifier())) {
                processMissingParm(domainModel, resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias, endOfChain);
            }
            // NOTE: The special logic needed to process NEAR query parms for the Location resource type is
            // found in method processLocationPosition(). This method will not handle those.
            if (!LocationUtil.isLocation(resourceType, queryParm)) {
                type = queryParm.getType();
                switch (type) {
                case STRING:
                    processStringParm(domainModel, resourceType, queryParm, paramTableAlias);
                    break;
                case REFERENCE:
                    if (queryParm.isReverseChained()) {
                        processReverseChainedReferenceParm(domainModel, resourceType, queryParm);
                    } else if (queryParm.isChained()) {
                        processChainedReferenceParm(domainModel, resourceType, queryParm);
                    } else if (queryParm.isInclusionCriteria()) {
                        // TODO
                        // processInclusionCriteria(domainModel, queryParm);
                    } else {
                        processReferenceParm(domainModel, resourceType, queryParm, paramTableAlias);
                    }
                    break;
                case DATE:
                    processDateParm(domainModel, resourceType, queryParm, paramTableAlias);
                    break;
                case TOKEN:
                    processTokenParm(domainModel, resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias, endOfChain);
                    break;
                case NUMBER:
                    processNumberParm(domainModel, resourceType, queryParm, paramTableAlias);
                    break;
                case QUANTITY:
                    processQuantityParm(domainModel, resourceType, queryParm, paramTableAlias);
                    break;
                case URI:
                    processUriParm(domainModel, resourceType, queryParm, paramTableAlias);
                    break;
                case COMPOSITE:
                    processCompositeParm(domainModel, resourceType, queryParm, paramTableAlias, logicalRsrcTableAlias);
                    break;
                default:
                    throw new FHIRPersistenceNotSupportedException("Parm type not yet supported: " + type.value());
                }
            } else {
                NearLocationHandler handler = new NearLocationHandler();
                List<Bounding> boundingAreas;
                try {
                    boundingAreas = handler.generateLocationPositionsFromParameters(Arrays.asList(queryParm));
                } catch (FHIRSearchException e) {
                    throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
                }
                buildLocationQuerySegment(domainModel, NearLocationHandler.NEAR, boundingAreas, paramTableAlias);
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME, new Object[] { databaseQueryParm });
        }
    }

    /**
     * This method executes special logic for a Token type query that maps to a
     * LocationPosition data type.
     *
     * @param queryParameters The entire collection of query input parameters
     * @param paramTableAlias the alias name of the parameter table
     * @return T1 - A query segment related to a LocationPosition
     * @throws FHIRPersistenceException
     */
    protected void processLocationPosition(SearchQuery domainModel, List<QueryParameter> queryParameters, String paramTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processLocationPosition";
        log.entering(CLASSNAME, METHODNAME);

        NearLocationHandler handler = new NearLocationHandler();
        List<Bounding> boundingAreas;
        try {
            boundingAreas = handler.generateLocationPositionsFromParameters(queryParameters);
        } catch (FHIRSearchException e) {
            throw new FHIRPersistenceException("input parameter is invalid bounding area, bad prefix, or bad units", e);
        }

        if (!boundingAreas.isEmpty()) {
            buildLocationQuerySegment(domainModel, NearLocationHandler.NEAR, boundingAreas, paramTableAlias);
        }

        log.exiting(CLASSNAME, METHODNAME);
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

    private void processStringParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {


        domainModel.add(new StringSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
    }

    /**
     * Get the code system id, reading from the database if necessary
     * @param codeSystemValue
     * @return
     * @throws FHIRPersistenceException
     */
    private Integer getCodeSystemId(String codeSystemValue) throws FHIRPersistenceException {
        return identityCache.getCodeSystemId(codeSystemValue);
    }

    /**
     * The queryParm is a REFERENCE type. Process it and update the domain model to include the
     * necessary joins and filters
     * @param domainModel
     * @param resourceType
     * @param queryParm
     * @param tableAlias
     * @return
     * @throws Exception
     */
    private SqlQueryData processReferenceParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        String operator = getOperator(queryParm, EQ);

        String searchValue;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        // We no longer build the query directly here. Instead we just want to build a logical model
        // which can be easily translated into a query.
        boolean parmValueProcessed = false;
        for (QueryParameterValue value : queryParm.getValues()) {
            String targetResourceType = null;
            searchValue = SqlParameterEncoder.encode(value.getValueString());

            // Make sure we split out the resource type if it is included in the search value
            String[] parts = value.getValueString().split("/");
            if (parts.length == 2) {
                targetResourceType = parts[0];
                searchValue = parts[1];
            }

            // Handle query parm representing this name/value pair construct:
            // <code>{name}:{Resource Type} = {resource-id}</code>
            if (queryParm.getModifier() != null && queryParm.getModifier().equals(Modifier.TYPE)) {
                if (!SearchConstants.Type.REFERENCE.equals(queryParm.getType())) {
                    // Not a Reference
                    searchValue =
                            queryParm.getModifierResourceTypeName() + "/"
                                    + SqlParameterEncoder.encode(value.getValueString());
                } else {
                    // This is a Reference type.
                    if (parts.length != 2) {
                        // fallback to get the target resource type using the modifier
                        targetResourceType = queryParm.getModifierResourceTypeName();
                    }
                }
            }

            // If multiple values are present, we need to OR them together.
            if (parmValueProcessed) {
                whereClauseSegment.append(OR);
            } else {
                parmValueProcessed = true;
            }

            // If the predicate includes a code-system it will resolve to a single value from
            // common_token_values. It helps the query optimizer if we include this additional
            // filter because it can make better cardinality estimates.
            Long commonTokenValueId = null;
            if (EQ.equals(operator) && targetResourceType != null) {
                // targetResourceType is treated as the code-system for references
                commonTokenValueId = getCommonTokenValueId(targetResourceType, searchValue);
            }

            // Build this piece: pX.token_value {operator} search-attribute-value [ AND pX.code_system_id = <n> ]
            whereClauseSegment.append(tableAlias).append(DOT).append(TOKEN_VALUE).append(operator).append(BIND_VAR);
            bindVariables.add(searchValue);

            // add the [optional] condition for the resource type if we have one
            if (commonTokenValueId != null) {
                // #1929 improves cardinality estimation
                // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                whereClauseSegment.append(AND).append(tableAlias).append(DOT).append(COMMON_TOKEN_VALUE_ID).append(EQ)
                    .append(commonTokenValueId);
            } else if (targetResourceType != null) {
                // For better performance, use a literal for the resource type code-system-id, not a parameter marker
                Integer codeSystemIdForResourceType = getCodeSystemId(targetResourceType);
                whereClauseSegment.append(AND).append(tableAlias).append(DOT).append(CODE_SYSTEM_ID).append(EQ).append(nullCheck(codeSystemIdForResourceType));
            }
        }
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    /**
     * Get the common_token_value_id values matching the given code-system/token_value
     * @return
     */
    private Long getCommonTokenValueId(String codeSystem, String tokenValue) {
        Long result = identityCache.getCommonTokenValueId(codeSystem, tokenValue);

        return result;
    }

    /**
     * Contains special logic for handling chained reference search parameters.
     * <p>
     * Nested sub-selects are built to realize the chaining logic required. Here is
     * a sample chained query for an Observation given this search parameter:
     * {@code device:Device.patient.family=Monella}
     *
     * <pre>
     * SELECT R.RESOURCE_ID, R.LOGICAL_RESOURCE_ID, R.VERSION_ID, R.LAST_UPDATED, R.IS_DELETED, R.DATA, LR.LOGICAL_ID
     * FROM Observation_LOGICAL_RESOURCES LR
     * JOIN Observation_RESOURCES R ON R.LOGICAL_RESOURCE_ID = LR.LOGICAL_RESOURCE_ID AND R.RESOURCE_ID = LR.CURRENT_RESOURCE_ID AND R.IS_DELETED = 'N'
     * JOIN (SELECT DISTINCT LOGICAL_RESOURCE_ID FROM Observation_STR_VALUES
     * WHERE(P1.PARAMETER_NAME_ID = 107 AND (p1.STR_VALUE IN
     *    (SELECT 'Device' || '/' || CLR1.LOGICAL_ID FROM Device_RESOURCES CR1, Device_LOGICAL_RESOURCES CLR1, Device_STR_VALUES CP1 WHERE
     *        CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N' AND CP1.RESOURCE_ID = CR1.RESOURCE_ID AND
     *          CP1.PARAMETER_NAME_ID = 17 AND CP1.STR_VALUE IN
     *                 (SELECT 'Patient' || '/' || CLR2.LOGICAL_ID FROM Patient_RESOURCES CR2, Patient_LOGICAL_RESOURCES CLR2, Patient_STR_VALUES CP2 WHERE
     *                     CR2.RESOURCE_ID = CLR2.CURRENT_RESOURCE_ID AND CR2.IS_DELETED = 'N' AND CP2.RESOURCE_ID = CR2.RESOURCE_ID AND
     *                     CP2.PARAMETER_NAME_ID = 5 AND CP2.STR_VALUE = 'Monella')))
     * TMP0 ON TMP0.LOGICAL_RESOURCE_ID = R.LOGICAL_RESOURCE_ID;
     * </pre>
     *
     * @see https://www.hl7.org/fhir/search.html#reference (section 2.1.1.4.13)
     * @param queryParm
     *                  - A Parameter representing a chained query.
     * @throws Exception
     */
    protected void processChainedReferenceParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm) throws Exception {
        domainModel.add(new ChainedSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
    }

    /*
     * Builds the specific handling for exact matches on _id.
     * The procedure here is SIMILAR to that of QuerySegmentAggregator.processFromClauseForId
     *
     * Results in a query like: CP1.LOGICAL_ID IN (?)
     */
    private SqlQueryData buildChainedIdClause(QueryParameter currentParm, String chainedParmVar) {
        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        whereClauseSegment
            .append(chainedParmVar.replace("CP", "CLR")).append(DOT).append(LOGICAL_ID).append(" IN (");

        List<QueryParameterValue> vals = currentParm.getValues();
        boolean add = false;
        for (QueryParameterValue v : vals) {
            if (add) {
                whereClauseSegment.append(COMMA);
            } else {
                add = true;
            }
            whereClauseSegment.append(BIND_VAR);
            bindVariables.add(SqlParameterEncoder.encode(v.getValueCode()));
        }
        whereClauseSegment.append(") ");

        return new SqlQueryData(whereClauseSegment.toString(), bindVariables);
    }

    private void appendMidChainParm(StringBuilder whereClauseSegment, QueryParameter currentParm, String chainedParmVar)
            throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException, FHIRPersistenceException {
        Integer parameterNameId = identityCache.getParameterNameId(currentParm.getCode());
        whereClauseSegment.append(chainedParmVar).append(DOT).append(PARAMETER_NAME_ID).append(EQ)
                .append(parameterNameId);

        final String codeSystemName = currentParm.getModifierResourceTypeName();
        if (codeSystemName != null && !codeSystemName.equals("*")) {
            Integer codeSystemId = identityCache.getCodeSystemId(codeSystemName);
            whereClauseSegment.append(AND).append(chainedParmVar).append(DOT).append(CODE_SYSTEM_ID).append(EQ)
                    .append(nullCheck(codeSystemId));
        }

        whereClauseSegment.append(AND).append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(IN);
    }

    private void appendInnerSelect(StringBuilder whereClauseSegment, QueryParameter currentParm,
            String resourceTypeName, String chainedResourceVar, String chainedLogicalResourceVar, String chainedParmVar) {
        String chainedLogicalResourceTableAlias = chainedLogicalResourceVar + DOT;
        String chainedParmTableAlias = chainedParmVar + DOT;

        // Build this piece: SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID
        // Note since #1366, we no longer need to prepend the resourceTypeName
        whereClauseSegment.append(SELECT).append(chainedLogicalResourceTableAlias).append(LOGICAL_ID);

        QueryParameter nextParameter = currentParm.getNextParameter();

        // Build this piece: FROM Device_LOGICAL_RESOURCES CLR1
        whereClauseSegment.append(FROM)
            .append(resourceTypeName).append(_LOGICAL_RESOURCES).append(SPACE).append(chainedLogicalResourceVar);

        if (Type.COMPOSITE.equals(nextParameter.getType())) {
            // If next parameter is composite, just join RESOURCES table:
            // JOIN Device_RESOURCES CR1 ON CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N'
            // TODO remove once refactor is working
//            whereClauseSegment.append(JOIN)
//                .append(resourceTypeName).append(_RESOURCES).append(SPACE).append(chainedResourceVar)
//                .append(ON)
//                .append(chainedResourceTableAlias).append(RESOURCE_ID).append(EQ)
//                .append(chainedLogicalResourceTableAlias).append(CURRENT_RESOURCE_ID)
//                .append(AND)
//                .append(chainedLogicalResourceTableAlias).append(IS_DELETED_NO);
            // can't add the IS_DELETED until we've got a WHERE which comes after any JOIN
            // If composite parameter has :missing modifier, add WHERE
            if (Modifier.MISSING.equals(nextParameter.getModifier())) {
                // no more joins so we need to add our WHERE clause here
                whereClauseSegment.append(" /* #appendInnerSelect-comp-missing */ ");
                whereClauseSegment.append(WHERE).append(chainedLogicalResourceTableAlias).append(IS_DELETED_NO).append(AND);
            } else {
                whereClauseSegment.append(" /* #appendInnerSelect-comp */ ");
                // Don't add the WHERE yet because there are more JOIN statements to be added first
            }
        } else {
            // Build this piece: , Device_LOGICAL_RESOURCES CLR1
            // whereClauseSegment.append(COMMA).append(resourceTypeName).append(_LOGICAL_RESOURCES).append(SPACE).append(chainedLogicalResourceVar);

            // If we're dealing with anything other than id, then proceed to add the parameters table.
            if (nextParameter != null && !"_id".equals(nextParameter.getCode())) {
                whereClauseSegment.append(" /* #appendInnerSelect-2 */ ");
                whereClauseSegment.append(COMMA)
                    .append(QuerySegmentAggregator.tableName(resourceTypeName, nextParameter)).append(chainedParmVar);
            }

            // CLR1.IS_DELETED = 'N' AND
            whereClauseSegment.append(" /* #appendInnerSelect-3 */ ");
            whereClauseSegment.append(WHERE).append(chainedLogicalResourceTableAlias).append(IS_DELETED_NO).append(AND);


            // CP1.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID AND
            if (nextParameter != null && !"_id".equals(nextParameter.getCode())) {
                whereClauseSegment.append(" /* #appendInnerSelect-4 */ ");
                whereClauseSegment.append(chainedParmTableAlias).append(LOGICAL_RESOURCE_ID).append(EQ)
                    .append(chainedLogicalResourceTableAlias).append(LOGICAL_RESOURCE_ID)
                    .append(AND);
            }
        }
    }

    /**
     * This method handles the processing of a wildcard chained reference parameter.
     * The wildcard represents ALL FHIR
     * resource types stored in the FHIR database.
     *
     * @throws Exception
     */
    private void processWildcardChainedRefParm(SearchQuery domainModel, QueryParameter currentParm, String chainedResourceVar,
            String chainedLogicalResourceVar, String chainedParmVar,
            StringBuilder whereClauseSegment, List<Object> bindVariables) throws Exception {
        final String METHODNAME = "processWildcardChainedRefParm";
        log.entering(CLASSNAME, METHODNAME, currentParm.toString());

        String resourceTypeName;
        Collection<Integer> resourceTypeIds;
        QueryParameter lastParm;
        boolean selectGenerated = false;
        Map<String, Integer> resourceNameIdMap = null;
        Map<Integer, String> resourceIdNameMap = null;

        lastParm          = currentParm.getNextParameter();

        // Acquire ALL Resource Type Ids
        resourceNameIdMap = resourceDao.readAllResourceTypeNames();
        resourceTypeIds   = resourceNameIdMap.values();
        resourceIdNameMap =
                resourceNameIdMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        // Build a sub-SELECT for each resource type, and put them together in a UNION.
        for (Integer resourceTypeId : resourceTypeIds) {
            if (selectGenerated) {
                whereClauseSegment.append(" UNION ");
            }
            // Build this piece: (SELECT 'resource-type-name' || '/' || CLRx.LOGICAL_ID ...
            resourceTypeName = resourceIdNameMap.get(resourceTypeId);

            if (!selectGenerated) {
                whereClauseSegment.append(LEFT_PAREN);
            }

            appendInnerSelect(whereClauseSegment, currentParm, resourceTypeName, chainedResourceVar,
                    chainedLogicalResourceVar, chainedParmVar);

            // This logic processes the LAST parameter in the chain.
            // Build this piece: CPx.PARAMETER_NAME_ID = x AND CPx.STR_VALUE = ?
            Class<?> chainedResourceType = ModelSupport.getResourceType(resourceTypeName);
            processQueryParameter(domainModel, chainedResourceType, lastParm, chainedParmVar, chainedLogicalResourceVar, false);

            selectGenerated = true;
        }

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());

    }


    private SqlQueryData processDateParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processDateParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        List<Timestamp> bindVariables = new ArrayList<>();
        DateParmBehaviorUtil behaviorUtil = new DateParmBehaviorUtil();
        behaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, tableAlias);

        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    private SqlQueryData processTokenParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain) throws FHIRPersistenceException {
        final String METHODNAME = "processTokenParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        String operator = this.getOperator(queryParm, EQ);
        boolean parmValueProcessed = false;
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        String tableAlias = paramTableAlias;

        String code = queryParm.getCode();
        if (!QuerySegmentAggregator.ID.equals(code)) {

            // Only generate NOT EXISTS subquery if :not modifier is within chained query;
            // when :not modifier is within non-chained query QuerySegmentAggregator.buildWhereClause generates the NOT EXISTS subquery
            boolean surroundWithNotExistsSubquery = Modifier.NOT.equals(queryParm.getModifier()) && endOfChain;
            if (surroundWithNotExistsSubquery) {
                whereClauseSegment.append(NOT).append(EXISTS);

                // PARAMETER_TABLE_NAME_PLACEHOLDER is replaced by the actual table name for the resource type by QuerySegmentAggregator.buildWhereClause(...)
                String valuesTable = !ModelSupport.isAbstract(resourceType) ? QuerySegmentAggregator.tableName(resourceType.getSimpleName(), queryParm) : PARAMETER_TABLE_NAME_PLACEHOLDER;
                tableAlias = paramTableAlias + "_param0";
                whereClauseSegment.append("(SELECT 1 FROM " + valuesTable + AS + tableAlias + WHERE);
            }

            // Build this piece of the segment:
            // (P1.PARAMETER_NAME_ID = x AND
            this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

            whereClauseSegment.append(AND).append(LEFT_PAREN);
            for (QueryParameterValue value : queryParm.getValues()) {
                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    whereClauseSegment.append(OR);
                }

                whereClauseSegment.append(LEFT_PAREN);
                // Include code
                whereClauseSegment.append(tableAlias + DOT).append(TOKEN_VALUE).append(operator).append(BIND_VAR);
                bindVariables.add(SqlParameterEncoder.encode(value.getValueCode()));

                // Include system if present.
                if (value.getValueSystem() != null && !value.getValueSystem().isEmpty()) {
                    Long commonTokenValueId = null;
                    if (NE.equals(operator)) {
                        whereClauseSegment.append(OR);
                    } else {
                        whereClauseSegment.append(AND);

                        // use #1929 optimization if we can
                        commonTokenValueId = getCommonTokenValueId(value.getValueSystem(), value.getValueCode());
                    }

                    if (commonTokenValueId != null) {
                        // #1929 improves cardinality estimation
                        // resulting in far better execution plans for many search queries. Because COMMON_TOKEN_VALUE_ID
                        // is the primary key for the common_token_values table, we don't need the CODE_SYSTEM_ID = ? predicate.
                        whereClauseSegment.append(tableAlias).append(DOT).append(COMMON_TOKEN_VALUE_ID).append(EQ)
                            .append(commonTokenValueId);
                    } else {
                        // common token value not found so we can't use the optimization. Filter the code-system-id
                        // instead, which ends up being the logical equivalent.
                        Integer codeSystemId = identityCache.getCodeSystemId(value.getValueSystem());
                        whereClauseSegment.append(tableAlias).append(DOT).append(CODE_SYSTEM_ID).append(operator)
                        .append(nullCheck(codeSystemId));
                    }
                }
                whereClauseSegment.append(RIGHT_PAREN);
                parmValueProcessed = true;
            }

            whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);

            if (surroundWithNotExistsSubquery) {
                whereClauseSegment.append(AND).append(tableAlias).append(".LOGICAL_RESOURCE_ID = ").append(logicalRsrcTableAlias).append(".LOGICAL_RESOURCE_ID");
                whereClauseSegment.append(RIGHT_PAREN);
            }
        }
        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME);
        return queryData;
    }

    private void processNumberParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "processNumberParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        // Calls to the NumberParmBehaviorUtil which encapsulates the precision
        // selection criteria.
        // TODO
//        NumberParmBehaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, resourceType, tableAlias,
//                this);
//        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
    }

    private void processQuantityParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias)
            throws Exception {
        final String METHODNAME = "processQuantityParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x AND
        this.populateNameIdSubSegment(whereClauseSegment, queryParm.getCode(), tableAlias);

        // Calls to the QuantityParmBehaviorUtil which encapsulates the precision
        // selection criteria.
        QuantityParmBehaviorUtil behaviorUtil = new QuantityParmBehaviorUtil();
        behaviorUtil.executeBehavior(whereClauseSegment, queryParm, bindVariables, tableAlias, parameterDao);
        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);

        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
    }

    /**
     * Creates a query segment for a URI type parameter.
     *
     * @param queryParm  - The query parameter
     * @param tableAlias - An alias for the table to query
     * @return SqlQueryData - An object containing query segment
     * @throws FHIRPersistenceException
     */
    protected void processUriParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String tableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "processUriParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        this.processStringParm(domainModel, resourceType, queryParm, tableAlias);
        log.exiting(CLASSNAME, METHODNAME, queryParm.toString());
    }

    /**
     * Creates a query segment for a COMPOSITE type parameter.
     *
     * @param queryParm  - The query parameter
     * @param paramTableAlias - An alias for the parameter table to query
     * @param logicalResourceTableAlias - An alias for the logical resource table to query
     * @return SqlQueryData - An object containing query segment
     * @throws FHIRPersistenceException
     */
    private void processCompositeParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalResourceTableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "processCompositeParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        StringBuilder joinSegment = new StringBuilder();
        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();

        try {
            if (queryParm.getValues().size() > 1) {
                // Build as WHERE clause using EXISTS
                populateCompositeSelectSubSegment(domainModel, queryParm, resourceType, paramTableAlias, logicalResourceTableAlias);
//                joinSegment.append(" WHERE (EXISTS ").append(compositeQueryData.getQueryString()).append(RIGHT_PAREN);
//                bindVariables.addAll(compositeQueryData.getBindVariables());
            } else {
                // Build as JOINS
                QueryParameterValue compositeValue = queryParm.getValues().get(0);
                List<QueryParameter> components = compositeValue.getComponent();
                for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                    QueryParameter component = components.get(componentNum - 1);
                    String componentTableAlias = paramTableAlias + "_p" + componentNum;

                    // Build JOIN clause
                    joinSegment.append(JOIN).append(QuerySegmentAggregator.tableName(resourceType.getSimpleName(), component))
                                .append(AS).append(componentTableAlias).append(ON);

                    processQueryParameter(domainModel, resourceType, component, componentTableAlias, logicalResourceTableAlias, false);
//                    joinSegment.append(subQueryData.getQueryString());
//                    bindVariables.addAll(subQueryData.getBindVariables());

                    joinSegment.append(AND).append(logicalResourceTableAlias).append(".LOGICAL_RESOURCE_ID = ").append(componentTableAlias).append(".LOGICAL_RESOURCE_ID");
                    if (componentNum > 1) {
                        joinSegment.append(AND).append(componentTableAlias).append(".COMPOSITE_ID = " + paramTableAlias + "_p1.COMPOSITE_ID");
                    }
                }
            }
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error while creating subquery for parameter '" + queryParm.getCode() + "'", e);
        }

        queryData = new SqlQueryData(joinSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
    }

    protected void buildLocationQuerySegment(SearchQuery domainModel, String parmName, List<Bounding> boundingAreas, String paramTableAlias)
            throws FHIRPersistenceException {
        final String METHODNAME = "buildLocationQuerySegment";
        log.entering(CLASSNAME, METHODNAME, parmName);

        StringBuilder whereClauseSegment = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        StringBuilder populateNameIdSubSegment = new StringBuilder();
        this.populateNameIdSubSegment(populateNameIdSubSegment, parmName, paramTableAlias);

        LocationParmBehaviorUtil behaviorUtil = new LocationParmBehaviorUtil();
        behaviorUtil.buildLocationSearchQuery(populateNameIdSubSegment.toString(), whereClauseSegment, bindVariables, boundingAreas, paramTableAlias);

        SqlQueryData queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, whereClauseSegment.toString());
    }

    /**
     * Populates the parameter name sub-segment of the passed where clause segment.
     *
     * @param whereClauseSegment
     * @param queryParmName
     * @param parameterTableAlias the alias for the parameter table e.g. pX
     * @param resourceTypeName the resource type of the reference being followed
     * @throws FHIRPersistenceException
     */
    private void populateNameIdSubSegment(StringBuilder whereClauseSegment, String queryParmName,
            String parameterTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateNameIdSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParmName);

        Integer parameterNameId;

        // Build this piece of the segment:
        // (P1.PARAMETER_NAME_ID = x
        parameterNameId = identityCache.getParameterNameId(queryParmName);

        whereClauseSegment.append(LEFT_PAREN);
        whereClauseSegment.append(parameterTableAlias + DOT).append(PARAMETER_NAME_ID).append(EQ)
                .append(nullCheck(parameterNameId));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Use -1 in place of a null literal, otherwise return the literal value
     *
     * @param n
     * @return
     */
    private String nullCheck(Integer n) {
        return n == null ? "-1" : n.toString();
    }

    /**
     * This method handles the special case of chained inclusion criteria. Using
     * data extracted from the passed query
     * parameter, a new Parameter chain is built to represent the chained inclusion
     * criteria. That new Parameter is then
     * passed to the inherited processChainedReferenceParamter() method to generate
     * the required where clause segment.
     *
     * @see https://www.hl7.org/fhir/compartments.html
     * @param queryParm
     *                  - A Parameter representing chained inclusion criterion.
     * @return SqlQueryData - the where clause segment and bind variables for a
     *         chained inclusion criterion.
     * @throws Exception
     */
    private void processChainedInclusionCriteria(SearchQuery domainModel, QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processChainedInclusionCriteria";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        QueryParameter rootParameter = null;
        // TODO chained inclusions are done as a separate query
        // Transform the passed query parm into a chained parameter representation.
        rootParameter = SearchUtil.parseChainedInclusionCriteria(queryParm);
        // Call method to process the Parameter built by this method as a chained parameter.
        // processChainedReferenceParm(domainModel, rootParameter);

        log.exiting(CLASSNAME, METHODNAME);

    }

    private void processMissingParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm, String paramTableAlias, String logicalRsrcTableAlias, boolean endOfChain)
            throws FHIRPersistenceException {
        final String METHODNAME = "processMissingParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());
        domainModel.add(new MissingSearchParam(resourceType.getSimpleName(), queryParm.getCode(), queryParm));
        log.exiting(CLASSNAME, METHODNAME, queryParm.toString());
    }

    /**
     * Contains special logic for handling reverse chained reference search parameters.
     * <p>
     * A select statement is built to realize the reverse chaining logic required. Here is a sample
     * reverse chained query for a Patient given this search parameter: _has:Observation:patient:code=1234
     *
     * <pre>
     * SELECT
     *   CLR0.LOGICAL_ID
     * FROM
     *   Patient_LOGICAL_RESOURCES AS CLR0
     *   JOIN Patient_RESOURCES AS CR0 ON CR0.RESOURCE_ID = CLR0.CURRENT_RESOURCE_ID AND CR0.IS_DELETED = 'N'
     * WHERE
     *   EXISTS (
     *     SELECT
     *       1
     *     FROM
     *       Observation_TOKEN_VALUES_V AS CP1
     *       JOIN Observation_LOGICAL_RESOURCES AS CLR1 ON CLR1.LOGICAL_RESOURCE_ID = CP1.LOGICAL_RESOURCE_ID
     *       JOIN Observation_RESOURCES AS CR1 ON CR1.RESOURCE_ID = CLR1.CURRENT_RESOURCE_ID AND CR1.IS_DELETED = 'N'
     *       JOIN Observation_TOKEN_VALUES_V AS CP2 ON CP2.LOGICAL_RESOURCE_ID = CLR1.LOGICAL_RESOURCE_ID
     *       AND (
     *         CP2.PARAMETER_NAME_ID = 1073
     *         AND ((CP2.TOKEN_VALUE = ?))
     *       )
     *     WHERE
     *       COALESCE(CP1.REF_VERSION_ID, CR0.VERSION_ID) = CR0.VERSION_ID
     *       AND CP1.TOKEN_VALUE = CLR0.LOGICAL_ID
     *       AND CP1.PARAMETER_NAME_ID = 1274
     *       AND CP1.CODE_SYSTEM_ID = 20004
     *   )
     * </pre>
     *
     * @see https://www.hl7.org/fhir/search.html#has
     * @param resourceType
     *                  - The resource type being searched.
     * @param queryParm
     *                  - A Parameter representing a reverse chained query.
     * @return SqlQueryData
     *                  - The query segment for a reverse chained parameter reference search.
     * @throws Exception
     */
    protected SqlQueryData processReverseChainedReferenceParm(SearchQuery domainModel, Class<?> resourceType, QueryParameter queryParm) throws Exception {
        final String METHODNAME = "processReverseChainedReferenceParm";
        log.entering(CLASSNAME, METHODNAME, queryParm.toString());

        String prevChainedResourceVar = null;
        String prevChainedLogicalResourceVar = null;
        String chainedResourceVar = null;
        String chainedLogicalResourceVar = null;
        String chainedParmVar = null;
        String nextChainedResourceVar = null;
        String nextChainedLogicalResourceVar = null;
        String nextChainedParmVar = null;
        QueryParameter previousParm = null;;
        int parmIndex = 0;
        int lastParmIndex = queryParm.getChain().size();
        boolean chainedParmProcessed = false;
        StringBuilder selectSegments = new StringBuilder();
        StringBuilder whereClauseSegments = new StringBuilder();
        List<Object> bindVariables = new ArrayList<>();

        // Loop through the chained query parameters in order
        List<QueryParameter> queryParms = queryParm.getChain();
        queryParms.add(0, queryParm);
        for (QueryParameter currentParm : queryParms) {

            prevChainedResourceVar        = CR + parmIndex;
            prevChainedLogicalResourceVar = CLR + parmIndex;
            chainedResourceVar        = CR + (parmIndex + 1);
            chainedLogicalResourceVar = CLR + (parmIndex + 1);
            chainedParmVar            = CP + (parmIndex + 1);
            nextChainedResourceVar        = CR + (parmIndex + 2);
            nextChainedLogicalResourceVar = CLR + (parmIndex + 2);
            nextChainedParmVar        = CP + (parmIndex + 2);
            StringBuilder whereClauseSegment = new StringBuilder();

            if (parmIndex == 0) {
                // Build outer select:
                // @formatter:off
                //   SELECT CLR0.LOGICAL_ID
                //     FROM <resource-type>_LOGICAL_RESOURCES AS CLR0
                //    WHERE CLR0.IS_DELETED = 'N'
                // @formatter:on
                selectSegments.append(SELECT).append(" /* processReverseChainedReferenceParm-1 */ ")
                                .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID)
                                .append(FROM).append(resourceType.getSimpleName()).append(_LOGICAL_RESOURCES).append(AS).append(prevChainedLogicalResourceVar)
                                .append(WHERE)
                                .append(prevChainedLogicalResourceVar).append(DOT).append(IS_DELETED_NO)
                                .append(AND);
            }

            if (parmIndex < lastParmIndex) {
                if (currentParm.isReverseChained()) {
                    // Build inner select joins:
                    // @formatter:off
                    //   EXISTS (SELECT 1
                    //     FROM <modifierTypeResourceName>_TOKEN_VALUES_V AS CPx
                    //     JOIN <modifierTypeResourceName>_LOGICAL_RESOURCES AS CLRx
                    //       ON CLRx.LOGICAL_RESOURCE_ID = CPx.LOGICAL_RESOURCE_ID
                    //      AND CLRx.IS_DELETED = 'N'
                    // @formatter:on
                    selectSegments.append(" /* processReverseChainedReferenceParm-2 */ ");
                    selectSegments.append(EXISTS).append(LEFT_PAREN).append("SELECT 1")
                                    .append(FROM).append(currentParm.getModifierResourceTypeName()).append("_TOKEN_VALUES_V").append(AS).append(chainedParmVar)
                                    .append(JOIN).append(currentParm.getModifierResourceTypeName()).append(_LOGICAL_RESOURCES).append(AS).append(chainedLogicalResourceVar)
                                    .append(ON).append(chainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(EQ)
                                    .append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                    .append(AND).append(chainedLogicalResourceVar).append(DOT).append(IS_DELETED_NO)
                                    ;

                    String referencedResourceType = null;
                    if (parmIndex == 0) {
                        referencedResourceType = resourceType.getSimpleName();
                    } else {
                        referencedResourceType = previousParm.getModifierResourceTypeName();
                    }
                    if (parmIndex < lastParmIndex - 1 && currentParm.getNextParameter().isReverseChained()) {
                        // Build inner select where clause:
                        // @formatter:off
                        //   WHERE
                        //     COALESCE(CPx.REF_VERSION_ID, CLR<x-1>.VERSION_ID) = CLR<x-1>.VERSION_ID
                        //     AND CPx.TOKEN_VALUE = CLR<x-1>.LOGICAL_ID
                        //     AND CPx.PARAMETER_NAME_ID = <parm-name-id>
                        //     AND CPx.CODE_SYSTEM_ID = <code-system-id>
                        //     AND
                        // @formatter:on
                        selectSegments.append(WHERE).append("COALESCE(").append(chainedParmVar).append(DOT).append("REF_VERSION_ID, ")
                                        .append(prevChainedLogicalResourceVar).append(DOT).append("VERSION_ID)").append(EQ)
                                        .append(prevChainedLogicalResourceVar).append(DOT).append("VERSION_ID").append(AND)
                                        .append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(EQ)
                                        .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID).append(AND);
                        populateReferenceNameAndCodeSystemIdSubSegment(selectSegments, currentParm.getCode(), referencedResourceType, chainedParmVar);
                        selectSegments.append(AND);
                    } else {
                        // Build final inner select where clause:
                        // @formatter:off
                        //   WHERE
                        //     COALESCE(CPx.REF_VERSION_ID, CLR<x-1>.VERSION_ID) = CLR<x-1>.VERSION_ID
                        //     AND CPx.TOKEN_VALUE = CLR<x-1>.LOGICAL_ID
                        //     AND CPx.PARAMETER_NAME_ID = <parm-name-id>
                        //     AND CPx.CODE_SYSTEM_ID = <code-system-id>
                        // @formatter:on
                        whereClauseSegment.append(WHERE).append("COALESCE(").append(chainedParmVar).append(DOT).append("REF_VERSION_ID, ")
                                            .append(prevChainedLogicalResourceVar).append(DOT).append("VERSION_ID)").append(EQ)
                                            .append(prevChainedLogicalResourceVar).append(DOT).append("VERSION_ID").append(AND)
                                            .append(chainedParmVar).append(DOT).append(TOKEN_VALUE).append(EQ)
                                            .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_ID).append(AND);
                        populateReferenceNameAndCodeSystemIdSubSegment(whereClauseSegment, currentParm.getCode(), referencedResourceType, chainedParmVar);
                    }

                    // Add closing right paren for EXISTS
                    whereClauseSegment.append(RIGHT_PAREN);
                } else if (currentParm.isChained()) {
                    // Build chained query
                    if (!chainedParmProcessed) {
                        // Build initial chain join and select:
                        //   SELECT CPx.LOGICAL_RESOURCE_ID FROM <modifierTypeResourceName>_TOKEN_VALUES_V AS CPx WHERE
                        selectSegments.append(" /* processReverseChainedReferenceParm-3 */ ");
                        selectSegments.append(JOIN ).append(LEFT_PAREN)
                                        .append(SELECT).append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(FROM)
                                        .append(previousParm.getModifierResourceTypeName()).append("_TOKEN_VALUES_V AS ").append(chainedParmVar)
                                        .append(WHERE);
                    }

                    // Build this piece: CPx.PARAMETER_NAME_ID = <code-id> AND CPx.STR_VALUE IN
                    appendMidChainParm(selectSegments, currentParm, chainedParmVar);

                    // Build this piece: (SELECT 'resource-type-name' || '/' || CLR<x+1>.LOGICAL_ID ...
                    selectSegments.append(LEFT_PAREN);
                    appendInnerSelect(selectSegments, currentParm, currentParm.getModifierResourceTypeName(),
                        nextChainedResourceVar, nextChainedLogicalResourceVar, nextChainedParmVar);
                    whereClauseSegment.append(RIGHT_PAREN);

                    if (!chainedParmProcessed) {
                        chainedParmProcessed = true;

                        // Builds ON clause for join: ) AS CPx ON CPx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        whereClauseSegment.append(RIGHT_PAREN).append(AS).append(chainedParmVar).append(ON)
                                            .append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(EQ)
                                            .append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID);
                    }
                }
            } else if (parmIndex == lastParmIndex) {
                // This logic processes the LAST parameter in the chain.
                SqlQueryData sqlQueryData;
                if ("_id".equals(currentParm.getCode())) {
                    if (!chainedParmProcessed) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_LOGICAL_RESOURCES AS CLRx
                        //     ON CLRx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(previousParm.getModifierResourceTypeName()).append(_LOGICAL_RESOURCES)
                                            .append(AS).append(chainedLogicalResourceVar).append(ON)
                                            .append(chainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: CLRx.LOGICAL_ID IN (?)
                    sqlQueryData = buildChainedIdClause(currentParm, chainedParmVar);
                } else if ("_lastUpdated".equals(currentParm.getCode())) {
                    if (!chainedParmProcessed) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_RESOURCES AS CRx
                        //     ON CRx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(previousParm.getModifierResourceTypeName()).append(_LOGICAL_RESOURCES)
                                            .append(AS).append(chainedLogicalResourceVar).append(ON)
                                            .append(chainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: (LAST_UPDATED <operator> ?)
                    LastUpdatedParmBehaviorUtil util = new LastUpdatedParmBehaviorUtil(chainedLogicalResourceVar);
                    StringBuilder lastUpdatedWhereClause = new StringBuilder();
                    util.executeBehavior(lastUpdatedWhereClause, currentParm);
                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString(), util.getBindVariables());
//                    sqlQueryData = new SqlQueryData(lastUpdatedWhereClause.toString()
//                        .replaceAll(LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME,
//                            chainedLogicalResourceVar + DOT + LastUpdatedParmBehaviorUtil.LAST_UPDATED_COLUMN_NAME),
//                        util.getBindVariables());
                } else {
                    if (!chainedParmProcessed && !Type.COMPOSITE.equals(currentParm.getType())) {
                        // Build this join:
                        // @formatter:off
                        //   JOIN <modifierTypeResourceName>_<type>_VALUES AS CPx
                        //     ON CPx.LOGICAL_RESOURCE_ID = CLR<x-1>.LOGICAL_RESOURCE_ID
                        //     AND
                        // @formatter:on
                        whereClauseSegment.append(JOIN).append(QuerySegmentAggregator.tableName(previousParm.getModifierResourceTypeName(), currentParm))
                                            .append(AS).append(chainedParmVar).append(ON).append(chainedParmVar).append(DOT).append(LOGICAL_RESOURCE_ID)
                                            .append(EQ).append(prevChainedLogicalResourceVar).append(DOT).append(LOGICAL_RESOURCE_ID).append(AND);
                    }
                    // Build the rest: (CPx.PARAMETER_NAME_ID=<code-id> AND (CPx.<type>_VALUE=<valueCode>))
                    processQueryParameter(domainModel, ModelSupport.getResourceType(previousParm.getModifierResourceTypeName()), currentParm, chainedParmVar, prevChainedLogicalResourceVar, true);
                }

//                if (log.isLoggable(Level.FINE)) {
//                    log.fine("chained sqlQueryData[" + chainedParmVar + "] = " + sqlQueryData.getQueryString());
//                }
//                whereClauseSegment.append(sqlQueryData.getQueryString());
//                bindVariables.addAll(sqlQueryData.getBindVariables());
           }

            // Insert where clause segment in whole
            whereClauseSegments.insert(0, whereClauseSegment.toString());

            previousParm = currentParm;
            parmIndex++;
        }

        SqlQueryData queryData = new SqlQueryData(whereClauseSegments.insert(0, selectSegments.toString()).toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME, queryData.getQueryString());
        return queryData;
    }

    /**
     * Populates the reference parameter name ID and code system ID sub-segment of the passed where clause segment.
     *
     * @param whereClauseSegment - the segment to which the sub-segment will be added
     * @param queryParmName - the search parameter name
     * @param resourceTypeName - the resource type of the reference being followed
     * @param parameterTableAlias - the alias for the parameter table e.g. CPx
     * @throws FHIRPersistenceException
     */
    private void populateReferenceNameAndCodeSystemIdSubSegment(StringBuilder whereClauseSegment, String queryParmName,
            String resourceTypeName, String parameterTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateReferenceNameAndCodeSystemIdSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParmName);

        Integer parameterNameId = identityCache.getParameterNameId(queryParmName);
        Integer codeSystemId = getCodeSystemId(resourceTypeName);

        // Build the segment:
        // CPx.PARAMETER_NAME_ID = <parameter-name-id> AND CPx.CODE_SYSTEM_ID = <code-system_id>
        whereClauseSegment.append(parameterTableAlias).append(DOT).append(PARAMETER_NAME_ID).append(EQ).append(nullCheck(parameterNameId))
                            .append(AND).append(parameterTableAlias).append(DOT).append(CODE_SYSTEM_ID).append(EQ).append(nullCheck(codeSystemId));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Populates the composite select statement(s) for OR'd or :missing composite searches.
     *
     * @param queryParm - the composite query parameter
     * @param resourceType - the search resource type
     * @param parameterTableAlias - the alias for the parameter table e.g. CPx
     * @param logicalResourceTableAlias - the alias for the logical resource table e.g. LR
     * @return SqlQueryData the populated composite select statement(s)
     * @throws FHIRPersistenceException
     */
    private void populateCompositeSelectSubSegment(SearchQuery domainModel, QueryParameter queryParm, Class<?> resourceType, String paramTableAlias,
        String logicalResourceTableAlias) throws FHIRPersistenceException {
        final String METHODNAME = "populateCompositeSelectSubSegment";
        log.entering(CLASSNAME, METHODNAME, queryParm.getCode());

        SqlQueryData queryData;
        List<Object> bindVariables = new ArrayList<>();
        StringBuilder whereClauseSegment = new StringBuilder();
        boolean missing = Modifier.MISSING.equals(queryParm.getModifier());
        String valueSeparator = "";

        try {
            for (QueryParameterValue compositeValue : queryParm.getValues()) {
                whereClauseSegment.append(valueSeparator).append("(SELECT 1 FROM ");
                List<QueryParameter> components = compositeValue.getComponent();

                // Loop through components to build FROM clause and WHERE clause
                StringBuilder fromClauseSegment = new StringBuilder();
                StringBuilder innerWhereClauseSegment = new StringBuilder();
                for (int componentNum = 1; componentNum <= components.size(); componentNum++) {
                    QueryParameter component = components.get(componentNum - 1);
                    String valuesTable = QuerySegmentAggregator.tableName(resourceType.getSimpleName(), component);
                    String componentTableAlias = paramTableAlias + "_p" + componentNum;

                    // Build table in FROM clause for this component:
                    // Observation_STR_VALUES param1
                    if (componentNum > 1) {
                        fromClauseSegment.append(COMMA);
                    }
                    fromClauseSegment.append(valuesTable).append(" ").append(componentTableAlias);

                    // Build WHERE clause predicates for this component
                    if (componentNum > 1) {
                        innerWhereClauseSegment.append(AND);
                    }
                    innerWhereClauseSegment.append(componentTableAlias).append(".LOGICAL_RESOURCE_ID = ")
                                            .append(logicalResourceTableAlias).append(".LOGICAL_RESOURCE_ID").append(AND);
                    if (missing) {
                        this.populateNameIdSubSegment(innerWhereClauseSegment, component.getCode(), componentTableAlias);
                        innerWhereClauseSegment.append(RIGHT_PAREN);
                    } else {
                        processQueryParameter(domainModel, resourceType, component, componentTableAlias, logicalResourceTableAlias, false);
//                        innerWhereClauseSegment.append(subQueryData.getQueryString());
//                        bindVariables.addAll(subQueryData.getBindVariables());
                    }
                    if (componentNum > 1) {
                        innerWhereClauseSegment.append(AND).append(componentTableAlias).append(".COMPOSITE_ID = " + paramTableAlias + "_p1.COMPOSITE_ID");
                    }
                }
                whereClauseSegment.append(fromClauseSegment.toString()).append(WHERE).append(innerWhereClauseSegment.toString()).append(RIGHT_PAREN);
                valueSeparator = " OR EXISTS ";
            }
        } catch (Exception e) {
            throw new FHIRPersistenceException("Error while creating subquery for parameter '" + queryParm.getCode() + "'", e);
        }

        queryData = new SqlQueryData(whereClauseSegment.toString(), bindVariables);
        log.exiting(CLASSNAME, METHODNAME);
    }

}
