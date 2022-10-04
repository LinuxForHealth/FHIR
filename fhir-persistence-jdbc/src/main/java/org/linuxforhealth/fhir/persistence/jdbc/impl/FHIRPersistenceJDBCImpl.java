/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.impl;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS;
import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.util.ModelSupport.getResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.naming.InitialContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.linuxforhealth.fhir.config.DefaultFHIRConfigProvider;
import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfigProvider;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.MetricHandle;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.FHIRConstants;
import org.linuxforhealth.fhir.core.FHIRUtilities;
import org.linuxforhealth.fhir.core.context.FHIRPagingContext;
import org.linuxforhealth.fhir.core.trace.Trace;
import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.IConnectionProvider;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.api.SchemaType;
import org.linuxforhealth.fhir.database.utils.api.UndefinedNameException;
import org.linuxforhealth.fhir.database.utils.model.DbType;
import org.linuxforhealth.fhir.database.utils.query.Select;
import org.linuxforhealth.fhir.database.utils.schema.GetSchemaVersion;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.parser.FHIRJsonParser;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.resource.SearchParameter.Component;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.util.JsonSupport;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.model.visitor.Visitable;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathSystemValue;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.InteractionStatus;
import org.linuxforhealth.fhir.persistence.MultiResourceResult;
import org.linuxforhealth.fhir.persistence.MultiResourceResult.Builder;
import org.linuxforhealth.fhir.persistence.ResourceChangeLogRecord;
import org.linuxforhealth.fhir.persistence.ResourceEraseRecord;
import org.linuxforhealth.fhir.persistence.ResourcePayload;
import org.linuxforhealth.fhir.persistence.ResourceResult;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import org.linuxforhealth.fhir.persistence.erase.EraseDTO;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceDataAccessException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import org.linuxforhealth.fhir.persistence.index.DateParameter;
import org.linuxforhealth.fhir.persistence.index.FHIRRemoteIndexService;
import org.linuxforhealth.fhir.persistence.index.IndexProviderResponse;
import org.linuxforhealth.fhir.persistence.index.LocationParameter;
import org.linuxforhealth.fhir.persistence.index.NumberParameter;
import org.linuxforhealth.fhir.persistence.index.ProfileParameter;
import org.linuxforhealth.fhir.persistence.index.QuantityParameter;
import org.linuxforhealth.fhir.persistence.index.ReferenceParameter;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexData;
import org.linuxforhealth.fhir.persistence.index.SearchParametersTransport;
import org.linuxforhealth.fhir.persistence.index.SearchParametersTransportAdapter;
import org.linuxforhealth.fhir.persistence.index.SecurityParameter;
import org.linuxforhealth.fhir.persistence.index.StringParameter;
import org.linuxforhealth.fhir.persistence.index.TagParameter;
import org.linuxforhealth.fhir.persistence.index.TokenParameter;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import org.linuxforhealth.fhir.persistence.jdbc.FHIRResourceDAOFactory;
import org.linuxforhealth.fhir.persistence.jdbc.JDBCConstants;
import org.linuxforhealth.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheUtil;
import org.linuxforhealth.fhir.persistence.jdbc.connection.Action;
import org.linuxforhealth.fhir.persistence.jdbc.connection.CreateTempTablesAction;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbConnectionStrategy;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbTenantDatasourceConnectionStrategy;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRDbTestConnectionStrategy;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRTestTransactionAdapter;
import org.linuxforhealth.fhir.persistence.jdbc.connection.FHIRUserTransactionAdapter;
import org.linuxforhealth.fhir.persistence.jdbc.connection.IFHIRTransactionAdapterCallback;
import org.linuxforhealth.fhir.persistence.jdbc.connection.SchemaNameFromProps;
import org.linuxforhealth.fhir.persistence.jdbc.connection.SchemaNameImpl;
import org.linuxforhealth.fhir.persistence.jdbc.connection.SchemaNameSupplier;
import org.linuxforhealth.fhir.persistence.jdbc.connection.SetMultiShardModifyModeAction;
import org.linuxforhealth.fhir.persistence.jdbc.dao.EraseResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.ReindexResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.JDBCIdentityCache;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ResourceDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.api.ResourceIndexRecord;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.CommonValuesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.FetchResourceChangesDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.FetchResourcePayloadsDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.JDBCIdentityCacheImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ParameterDAOImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ParameterTransportVisitor;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceProfileRec;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceReferenceValueRec;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.ResourceTokenValueRec;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.RetrieveIndexDAO;
import org.linuxforhealth.fhir.persistence.jdbc.dao.impl.TransactionDataImpl;
import org.linuxforhealth.fhir.persistence.jdbc.dto.CompositeParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.DateParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ErasedResourceRec;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ExtractedParameterValue;
import org.linuxforhealth.fhir.persistence.jdbc.dto.NumberParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.QuantityParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.ReferenceParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.StringParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.dto.TokenParmVal;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import org.linuxforhealth.fhir.persistence.jdbc.exception.FHIRPersistenceFKVException;
import org.linuxforhealth.fhir.persistence.jdbc.util.ExtractedSearchParameters;
import org.linuxforhealth.fhir.persistence.jdbc.util.FHIRPersistenceJDBCMetric;
import org.linuxforhealth.fhir.persistence.jdbc.util.JDBCParameterBuildingVisitor;
import org.linuxforhealth.fhir.persistence.jdbc.util.JDBCParameterCacheAdapter;
import org.linuxforhealth.fhir.persistence.jdbc.util.NewQueryBuilder;
import org.linuxforhealth.fhir.persistence.jdbc.util.ParameterHashVisitor;
import org.linuxforhealth.fhir.persistence.jdbc.util.TimestampPrefixedUUID;
import org.linuxforhealth.fhir.persistence.params.api.IParamValueCollector;
import org.linuxforhealth.fhir.persistence.params.api.IParamValueProcessor;
import org.linuxforhealth.fhir.persistence.params.api.IParameterIdentityCache;
import org.linuxforhealth.fhir.persistence.params.api.ParamMetrics;
import org.linuxforhealth.fhir.persistence.params.batch.ParameterValueCollector;
import org.linuxforhealth.fhir.persistence.params.database.DistributedPostgresParamValueProcessor;
import org.linuxforhealth.fhir.persistence.params.database.PlainDerbyParamValueProcessor;
import org.linuxforhealth.fhir.persistence.params.database.PlainPostgresParamValueProcessor;
import org.linuxforhealth.fhir.persistence.payload.FHIRPayloadPersistence;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResult;
import org.linuxforhealth.fhir.persistence.util.InputOutputByteStream;
import org.linuxforhealth.fhir.persistence.util.LogicalIdentityProvider;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;
import org.linuxforhealth.fhir.schema.control.FhirSchemaVersion;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SummaryValueSet;
import org.linuxforhealth.fhir.search.TotalValueSet;
import org.linuxforhealth.fhir.search.compartment.CompartmentHelper;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;
import org.linuxforhealth.fhir.search.date.DateTimeHandler;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.parameters.InclusionParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.util.ReferenceValue;
import org.linuxforhealth.fhir.search.util.ReferenceValue.ReferenceType;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * The JDBC implementation of the FHIRPersistence interface,
 * providing implementations for CRUD APIs and search.
 *
 * @implNote This class is request-scoped;
 *           it must be initialized for each request to reset the supplementalIssues list
 */
public class FHIRPersistenceJDBCImpl implements FHIRPersistence, SchemaNameSupplier, IFHIRTransactionAdapterCallback {
    private static final String CLASSNAME = FHIRPersistenceJDBCImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);
    private static final int DATA_BUFFER_INITIAL_SIZE = 10*1024; // 10KiB
    private static final Integer IF_NONE_MATCH_NULL = null;

    protected static final String TXN_JNDI_NAME = "java:comp/UserTransaction";
    public static final String TRX_SYNCH_REG_JNDI_NAME = "java:comp/TransactionSynchronizationRegistry";
    private static final String TXN_DATA_KEY = "transactionDataKey/" + CLASSNAME;

    // The following are filtered as they are handled specifically by the persistence layer:
    private static final List<String> SPECIAL_HANDLING = Arrays.asList("_id", "_lastUpdated");

    private final TransactionSynchronizationRegistry trxSynchRegistry;
    private List<OperationOutcome.Issue> supplementalIssues = new ArrayList<>();

    protected UserTransaction userTransaction = null;
    protected Boolean updateCreateEnabled = null;

    // The strategy used to obtain database connections
    private final FHIRDbConnectionStrategy connectionStrategy;

    // A strategy for finding the schema name
    private final SchemaNameSupplier schemaNameSupplier;

    // Handles transaction lifecycle for this persistence object
    private final FHIRPersistenceTransaction transactionAdapter;

    // Strategy for accessing FHIR configuration data
    private final FHIRConfigProvider configProvider;

    // Logical identity string provider
    private final LogicalIdentityProvider logicalIdentityProvider = new TimestampPrefixedUUID();

    // The shared cache, used by all requests for the same tenant/datasource
    private final FHIRPersistenceJDBCCache cache;

    // When set, use this interface to persist the payload object. Can be null.
    private final FHIRPayloadPersistence payloadPersistence;

    // A helper for processing search requests
    private final SearchHelper searchHelper;

    // The transactionDataImpl for use when collecting data across multiple resources in a transaction bundle
    private TransactionDataImpl<ParameterTransactionDataImpl> transactionDataImpl;

    // Enable use of legacy whole-system search parameters for the search request
    private final boolean legacyWholeSystemSearchParamsEnabled;

    // A list of payload persistence responses in case we have a rollback to clean up
    private final List<PayloadPersistenceResponse> payloadPersistenceResponses = new ArrayList<>();

    // A list of EraseResourceRec referencing offload resource records to erase if the current transaction commits
    private final List<ErasedResourceRec> eraseResourceRecs = new ArrayList<>();

    // A list of the remote index messages we need to check we get ACKs for
    private final List<IndexProviderResponse> remoteIndexMessageList = new ArrayList<>();

    // The collector used to accumulate all the search parameter values before we insert them just before commit
    private final IParamValueCollector paramValueCollector;

    /**
     * Constructor for use when running as web application in WLP.
     * @throws Exception
     */
    public FHIRPersistenceJDBCImpl(FHIRPersistenceJDBCCache cache, FHIRPayloadPersistence payloadPersistence, SearchHelper searchHelper) throws Exception {
        final String METHODNAME = "FHIRPersistenceJDBCImpl()";
        log.entering(CLASSNAME, METHODNAME);

        // The cache holding ids (private to the current tenant).
        this.cache = cache;
        this.payloadPersistence = payloadPersistence;
        this.searchHelper = searchHelper;

        PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        if (fhirConfig == null) {
            throw new IllegalStateException("Unable to load the default fhir-server-config.json");
        }
        this.updateCreateEnabled = fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
        this.userTransaction = retrieveUserTransaction(TXN_JNDI_NAME);

        if (userTransaction != null) {
            this.trxSynchRegistry = getTrxSynchRegistry();
        } else {
            this.trxSynchRegistry = null;
        }

        // Set up the connection strategy for use within a JEE container. The actions
        // are processed the first time a connection is established to a particular tenant/datasource.
        this.configProvider = new DefaultFHIRConfigProvider(); // before buildActionChain()
        this.schemaNameSupplier = new SchemaNameImpl(this);

        // Use separate JNDI datasources for each tenant/dsId
        boolean enableReadOnlyReplicas = fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_JDBC_ENABLE_READ_ONLY_REPLICAS, Boolean.FALSE);
        this.connectionStrategy = new FHIRDbTenantDatasourceConnectionStrategy(trxSynchRegistry, buildActionChain(), enableReadOnlyReplicas);

        this.transactionAdapter = new FHIRUserTransactionAdapter(userTransaction, trxSynchRegistry, cache, TXN_DATA_KEY, (committed) -> transactionCompleted(committed));

        // Use of legacy whole-system search parameters disabled by default
        this.legacyWholeSystemSearchParamsEnabled =
                fhirConfig.getBooleanProperty(PROPERTY_SEARCH_ENABLE_LEGACY_WHOLE_SYSTEM_SEARCH_PARAMS, false);

        this.paramValueCollector = new ParameterValueCollector(new JDBCParameterCacheAdapter(cache));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Constructor for use when running standalone, outside of any web container. The
     * IConnectionProvider should be a pooling implementation which supports an
     * ITransactionProvider. Uses the default adapter for reading FHIR configurations
     * and a new SearchHelper instance, which works OK for unit-tests.
     *
     * @param configProps
     * @param cp
     * @param cache
     * @throws Exception
     */
    public FHIRPersistenceJDBCImpl(Properties configProps, IConnectionProvider cp, FHIRPersistenceJDBCCache cache) throws Exception {
        this(configProps, cp, new DefaultFHIRConfigProvider(), cache, new SearchHelper());
    }

    /**
     * Constructor for use when running standalone, outside of any web container. The
     * IConnectionProvider should be a pooling implementation which supports an
     * ITransactionProvider.
     *
     * @implNote A custom implementation of the FHIRConfigProvider interface can be
     * specified to provide configuration properties without relying on
     * fhir-server-config.json files (FHIRConfiguration). This is useful for
     * some utility/test programs which may specify certain properties (like
     * TENANT_KEY) using their command-line.
     *
     * @param configProps
     * @param cp
     * @param configProvider adapter to provide access to FHIR configuration
     * @param cache
     * @param searchHelper
     * @throws Exception
     */
    public FHIRPersistenceJDBCImpl(Properties configProps, IConnectionProvider cp, FHIRConfigProvider configProvider,
            FHIRPersistenceJDBCCache cache, SearchHelper searchHelper) throws Exception {
        final String METHODNAME = "FHIRPersistenceJDBCImpl(Properties, IConnectionProvider, FHIRConfigProvider)";
        log.entering(CLASSNAME, METHODNAME);

        this.cache = cache;
        this.payloadPersistence = null;
        this.searchHelper = searchHelper;
        this.updateCreateEnabled = Boolean.parseBoolean(configProps.getProperty("updateCreateEnabled"));

        // not running inside a JEE container
        this.trxSynchRegistry = null;

        // caller provides an adapter we use to obtain configuration information
        this.configProvider = configProvider;

        // use the schema name from the configProps, or the connection.getSchema if we have to
        this.schemaNameSupplier = new SchemaNameImpl(new SchemaNameFromProps(configProps));

        // Obtain connections from the IConnectionProvider (typically used in Derby-based test-cases)
        this.connectionStrategy = new FHIRDbTestConnectionStrategy(cp, buildActionChain());

        // For unit tests (outside of JEE), we also need our own mechanism for handling transactions
        this.transactionAdapter = new FHIRTestTransactionAdapter(cp, this);

        // TODO connect the transactionAdapter to our cache so that we can handle tx events in a non-JEE world
        this.transactionDataImpl = null;

        // Always want to be testing with legacy whole-system search parameters disabled
        this.legacyWholeSystemSearchParamsEnabled = false;

        this.paramValueCollector = new ParameterValueCollector(new JDBCParameterCacheAdapter(cache));

        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Build a chain of actions we want to apply to new connections. Current the
     * only action we need is setting the tenant if we're in multi-tenant mode.
     * @return the chain of actions to be applied
     */
    protected Action buildActionChain() {
        // Note: do not call setSchema on a connection. It exposes a bug in Liberty.

        // For Derby, we need to make sure that the declared global temporary tables
        // are created for the current session (connection). TODO. discuss if we only
        // want to invoke this for ingestion calls. These tables are not required for
        // reads/searches.
        Action result = new CreateTempTablesAction();

        // For Citus SET LOCAL citus.multi_shard_modify_mode TO 'sequential'
        result = new SetMultiShardModifyModeAction(result);

        return result;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T updatedResource) throws FHIRPersistenceException  {
        final String METHODNAME = "create";
        log.entering(CLASSNAME, METHODNAME);

        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_CREATE.name())) {
            doCachePrefill(context, connection);

            if (context.getOffloadResponse() != null) {
                // Remember this payload offload response as part of the current transaction
                this.payloadPersistenceResponses.add(context.getOffloadResponse());
            }

            // This create() operation is only called by a REST create. If the given resource
            // contains an id, then for R4 we need to ignore it and replace it with our
            // system-generated value. For the update-or-create scenario, see update().
            // Default version is 1 for a brand new FHIR Resource.
            if (log.isLoggable(Level.FINE)) {
                log.fine("Creating new FHIR Resource of type '" + updatedResource.getClass().getSimpleName() + "'");
            }

            // The identity and meta fields must already be in the resource
            final String logicalId = updatedResource.getId();
            final int newVersionNumber = Integer.parseInt(updatedResource.getMeta().getVersionId().getValue());
            final Instant lastUpdated = updatedResource.getMeta().getLastUpdated();

            // Create the new Resource DTO instance.
            org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO =
                    createResourceDTO(updatedResource.getClass(), logicalId, newVersionNumber, lastUpdated, updatedResource,
                        getResourcePayloadKeyFromContext(context));

            // The DAO objects are now created on-the-fly (not expensive to construct) and
            // given the connection to use while processing this request
            ResourceDAO resourceDao = makeResourceDAO(context, connection);
            ParameterDAO parameterDao = makeParameterDAO(connection);

            // Persist the Resource DTO.
            resourceDao.setPersistenceContext(context);
            final ExtractedSearchParameters searchParameters;
            try (MetricHandle mh2 = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_EXTRACT_SEARCH_PARAMS.name())) {
                searchParameters = this.extractSearchParameters(updatedResource, resourceDTO);
            }
            resourceDao.insert(resourceDTO, searchParameters.getParameters(), searchParameters.getParameterHashB64(), parameterDao, context.getIfNoneMatch());
            if (log.isLoggable(Level.FINE)) {
                log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' logicalResourceId=" + resourceDTO.getLogicalResourceId()
                            + ", version=" + resourceDTO.getVersionId());
            }

            if (resourceDTO.getInteractionStatus() == InteractionStatus.MODIFIED && searchParameters != null) {
                storeSearchParameterValues(resourceDTO.getResourceType(), resourceDTO.getLogicalId(), resourceDTO.getLogicalResourceId(),
                    resourceDTO.getVersionId(), resourceDTO.getLastUpdated().toInstant(), context.getRequestShard(), searchParameters,
                    resourceDTO.getCurrentParameterHash());
            }
            SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .interactionStatus(resourceDTO.getInteractionStatus())
                    .ifNoneMatchVersion(resourceDTO.getIfNoneMatchVersion())
                    .resource(updatedResource);

            // Add supplemental issues to the OperationOutcome
            if (!supplementalIssues.isEmpty()) {
                resultBuilder.outcome(OperationOutcome.builder()
                    .issue(supplementalIssues)
                    .build());
            }

            return resultBuilder.build();
        }
        catch(FHIRPersistenceFKVException e) {
            log.log(Level.SEVERE, "FK violation", e);
//            log.log(Level.SEVERE, this.performCacheDiagnostics());
            throw e;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a create operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
           log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Convert the extracted parameters into a SearchParametersTransport and either
     * collect the values to commit with the local transaction or send it off to a
     * remote indexing service (if so configured).
     * @param resourceType the resource type name
     * @param logicalId the logical id of the resource
     * @param logicalResourceId the database logical_resource_id returned from add_any_resource
     * @param versionId the latest version number of the resource
     * @param lastUpdated the last updated time of the resource
     * @param requestShard the shard in which to store the resource (when using the SHARDED schema variant, null otherwise)
     * @param searchParameters the search parameters extracted from the resource
     * @param currentParameterHash the current parameter hash returned from add_any_resource (null if this is a new resource)
     */
    private void storeSearchParameterValues(String resourceType, String logicalId, long logicalResourceId,
            int versionId, java.time.Instant lastUpdated, String requestShard,
            ExtractedSearchParameters searchParameters, String currentParameterHash) throws FHIRPersistenceException {

        final SearchParametersTransportAdapter adapter = buildSearchParametersTransportAdapter(resourceType, logicalId, logicalResourceId, versionId, lastUpdated, requestShard, searchParameters, currentParameterHash);

        // store locally or remotely?
        FHIRRemoteIndexService remoteIndexService = FHIRRemoteIndexService.getServiceInstance();
        if (remoteIndexService == null) {
            // Store the search parameters locally as part of the current transaction
            final String parameterHashB64 = searchParameters.getParameterHashB64();
            if (parameterHashB64 == null || parameterHashB64.isEmpty()
                  || !parameterHashB64.equals(currentParameterHash)) {
                accumulateSearchParameterValues(adapter.build());
            }
        } else {
            // Send the search parameter values over Kafka so that they can be processed using a remote service

            // Note that the remote index service is supposed to be multi-tenant, using
            // the tenantId from the request context on this thread, so we don't need
            // to pass that here
            final String kafkaPartitionKey = resourceType + "/" + logicalId;
            IndexProviderResponse ipr = remoteIndexService.submit(new RemoteIndexData(kafkaPartitionKey, adapter.build()));
            remoteIndexMessageList.add(ipr); // we'll check for an ACK just before we commit the transaction
        }
    }

    /**
     * Process the searchParameters and gather the unique set of underlying values we intend to store
     * in the search parameter value tables
     *
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param versionId
     * @param lastUpdated
     * @param requestShard
     * @param searchParameters
     * @param currentParameterHash
     * @return
     */
    private SearchParametersTransportAdapter buildSearchParametersTransportAdapter(String resourceType, String logicalId, long logicalResourceId,
            int versionId, java.time.Instant lastUpdated, String requestShard,
            ExtractedSearchParameters searchParameters, String currentParameterHash) throws FHIRPersistenceException {

        SearchParametersTransportAdapter adapter = new SearchParametersTransportAdapter(resourceType, logicalId, logicalResourceId,
            versionId, lastUpdated, requestShard, searchParameters.getParameterHashB64());
        ParameterTransportVisitor visitor = new ParameterTransportVisitor(adapter);
        for (ExtractedParameterValue pv: searchParameters.getParameters()) {
            pv.accept(visitor);
        }
        return adapter;
    }

    /**
     * Add the set of search parameter values to the overall list being maintained for the current
     * transaction. All the values are collected first and then stored just prior to the transaction
     * being committed
     * @param build
     */
    private void accumulateSearchParameterValues(SearchParametersTransport params) throws FHIRPersistenceException {
        final String tenantId = FHIRRequestContext.get().getTenantId();

        if (params.getStringValues() != null) {
            for (StringParameter p: params.getStringValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getDateValues() != null) {
            for (DateParameter p: params.getDateValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getNumberValues() != null) {
            for (NumberParameter p: params.getNumberValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getQuantityValues() != null) {
            for (QuantityParameter p: params.getQuantityValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getTokenValues() != null) {
            for (TokenParameter p: params.getTokenValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getLocationValues() != null) {
            for (LocationParameter p: params.getLocationValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getTagValues() != null) {
            for (TagParameter p: params.getTagValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getProfileValues() != null) {
            for (ProfileParameter p: params.getProfileValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getSecurityValues() != null) {
            for (SecurityParameter p: params.getSecurityValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }

        if (params.getRefValues() != null) {
            for (ReferenceParameter p: params.getRefValues()) {
                paramValueCollector.collect(tenantId, params.getRequestShard(), params.getResourceType(), params.getLogicalId(), params.getLogicalResourceId(), p);
            }
        }
    }

    /**
     * Prefill the cache if required
     * @throws FHIRPersistenceException
     */
    private void doCachePrefill() throws FHIRPersistenceException {
        if (cache.needToPrefill()) {
            try (Connection connection = openConnection()) {
                doCachePrefill(/*context=*/null, connection);
            } catch(FHIRPersistenceException e) {
                throw e;
            } catch(Throwable e) {
                FHIRPersistenceException fx = new FHIRPersistenceException("Cache prefill - unexpected error");
                log.log(Level.SEVERE, fx.getMessage(), e);
                throw fx;
            }
        }
    }

    /**
     * Creates and returns a data transfer object (DTO) with the contents of the passed arguments.
     *
     * @param resourceType
     * @param logicalId
     * @param newVersionNumber
     * @param lastUpdated
     * @param updatedResource
     * @param resourcePayloadKey
     * @return
     * @throws IOException
     * @throws FHIRGeneratorException
     */
    private org.linuxforhealth.fhir.persistence.jdbc.dto.Resource createResourceDTO(Class<? extends Resource> resourceType,
            String logicalId, int newVersionNumber,
            Instant lastUpdated, Resource updatedResource, String resourcePayloadKey) throws IOException, FHIRGeneratorException {

        Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());

        org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = new org.linuxforhealth.fhir.persistence.jdbc.dto.Resource();
        resourceDTO.setLogicalId(logicalId);
        resourceDTO.setVersionId(newVersionNumber);
        resourceDTO.setLastUpdated(timestamp);
        resourceDTO.setResourceType(resourceType.getSimpleName());
        resourceDTO.setResourcePayloadKey(resourcePayloadKey);

        // Are storing the payload in our RDBMS, or offloading to another store?
        if (this.payloadPersistence == null && updatedResource != null) {
            // Most resources are well under 10K after being serialized and compressed
            InputOutputByteStream ioStream = new InputOutputByteStream(DATA_BUFFER_INITIAL_SIZE);

            // Serialize and compress the Resource
            try (GZIPOutputStream zipStream = new GZIPOutputStream(ioStream.outputStream())) {
                FHIRGenerator.generator(Format.JSON, false).generate(updatedResource, zipStream);
                zipStream.finish();
                resourceDTO.setDataStream(ioStream);
            }
        } else {
            // just to make the point that the payload isn't stored here
            resourceDTO.setDataStream(null);
        }

        return resourceDTO;
    }

    /**
     * To minimize the impact of using an additional column for explicit sharding,
     * we encode the requestShard into a short value which is stored in the database.
     * This provides sufficient spread across nodes, but minimizes the amount of extra
     * space required.
     * @param requestShard
     * @return
     */
    private Short encodeRequestShard(String requestShard) {
        if (requestShard != null) {
            return Short.valueOf((short)requestShard.hashCode());
        } else {
            return null;
        }
    }

    /**
     * Convenience method to construct a new instance of the {@link ResourceDAO}
     * @param persistenceContext the persistence context for the current request
     * @param connection the connection to the database for the DAO to use
     * @return a properly constructed implementation of a ResourceDAO
     * @throws IllegalArgumentException
     * @throws FHIRPersistenceException
     * @throws FHIRPersistenceDataAccessException
     */
    private ResourceDAO makeResourceDAO(FHIRPersistenceContext persistenceContext, Connection connection)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceException, IllegalArgumentException {
        Short shardKey = null;
        if (connectionStrategy.getFlavor().getSchemaType() == SchemaType.SHARDED) {
            if (persistenceContext == null) {
                throw new FHIRPersistenceException("persistenceContext is always required for SHARDED schemas");
            }
            shardKey = encodeRequestShard(persistenceContext.getRequestShard());
            if (shardKey == null) {
                throw new FHIRPersistenceException("shardKey value is required for SHARDED schemas");
            }
        }

        if (this.trxSynchRegistry != null) {
            String datastoreId = FHIRRequestContext.get().getDataStoreId();
            return FHIRResourceDAOFactory.getResourceDAO(connection, FhirSchemaConstants.FHIR_ADMIN,
                    schemaNameSupplier.getSchemaForRequestContext(connection), connectionStrategy.getFlavor(),
                    this.trxSynchRegistry, this.cache, this.getTransactionDataForDatasource(datastoreId), shardKey);
        } else {
            return FHIRResourceDAOFactory.getResourceDAO(connection, FhirSchemaConstants.FHIR_ADMIN,
                    schemaNameSupplier.getSchemaForRequestContext(connection), connectionStrategy.getFlavor(),
                    this.cache, shardKey);
        }
    }

    /**
     * Create an instance of the ResourceReferenceDAO used to manage common_token_values
     * @param connection
     * @return
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceException
     * @throws IllegalArgumentException
     */
    private CommonValuesDAO makeCommonValuesDAO(Connection connection)
            throws FHIRPersistenceDataAccessException, FHIRPersistenceException, IllegalArgumentException {
        return FHIRResourceDAOFactory.getCommonValuesDAO(connection, FhirSchemaConstants.FHIR_ADMIN,
                schemaNameSupplier.getSchemaForRequestContext(connection), connectionStrategy.getFlavor());
    }

    /**
     * Convenience method to construct a new instance of the {@link ParameterDAO}
     * @param connection
     * @return
     * @throws FHIRPersistenceException
     * @throws FHIRPersistenceDataAccessException
     */
    private ParameterDAO makeParameterDAO(Connection connection) throws FHIRPersistenceDataAccessException, FHIRPersistenceException {
        if (this.trxSynchRegistry != null) {
            return new ParameterDAOImpl(connection, schemaNameSupplier.getSchemaForRequestContext(connection),
                    connectionStrategy.getFlavor(), trxSynchRegistry);
        } else {
            return new ParameterDAOImpl(connection, schemaNameSupplier.getSchemaForRequestContext(connection),
                    connectionStrategy.getFlavor());
        }
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, T resource)
            throws FHIRPersistenceException {
        final String METHODNAME = "update";
        log.entering(CLASSNAME, METHODNAME);

        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_UPDATE.name())) {
            doCachePrefill(context, connection);

            if (context.getOffloadResponse() != null) {
                // Remember this payload offload response as part of the current transaction
                this.payloadPersistenceResponses.add(context.getOffloadResponse());
            }

            ResourceDAO resourceDao = makeResourceDAO(context, connection);
            ParameterDAO parameterDao = makeParameterDAO(connection);

            // Since 1869, the resource is already correctly configured so no need to modify it
            int newVersionNumber = Integer.parseInt(resource.getMeta().getVersionId().getValue());

            // Create the new Resource DTO instance.
            org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO =
                    createResourceDTO(resource.getClass(), resource.getId(), newVersionNumber,
                        resource.getMeta().getLastUpdated(), resource,
                        getResourcePayloadKeyFromContext(context));

            // Persist the Resource DTO.
            resourceDao.setPersistenceContext(context);
            ExtractedSearchParameters searchParameters = this.extractSearchParameters(resource, resourceDTO);
            resourceDao.insert(resourceDTO, searchParameters.getParameters(), searchParameters.getParameterHashB64(),
                    parameterDao, context.getIfNoneMatch());

            if (log.isLoggable(Level.FINE)) {
                if (resourceDTO.getInteractionStatus() == InteractionStatus.IF_NONE_MATCH_EXISTED) {
                    log.fine("If-None-Match: Existing FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' logicalResourceId=" + resourceDTO.getLogicalResourceId()
                    + ", version=" + resourceDTO.getVersionId());
                } else {
                    log.fine("Persisted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' logicalResourceId=" + resourceDTO.getLogicalResourceId()
                                + ", version=" + resourceDTO.getVersionId());
                }
            }

            // If configured, send the extracted parameters to the remote indexing service
            if (resourceDTO.getInteractionStatus() == InteractionStatus.MODIFIED && searchParameters != null) {
                storeSearchParameterValues(resourceDTO.getResourceType(), resourceDTO.getLogicalId(), resourceDTO.getLogicalResourceId(),
                    resourceDTO.getVersionId(), resourceDTO.getLastUpdated().toInstant(), context.getRequestShard(), searchParameters,
                    resourceDTO.getCurrentParameterHash()
                    );
            }

            SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .interactionStatus(resourceDTO.getInteractionStatus())
                    .ifNoneMatchVersion(resourceDTO.getIfNoneMatchVersion())
                    .resource(resource);

            // Add supplemental issues to an OperationOutcome
            if (!supplementalIssues.isEmpty()) {
                resultBuilder.outcome(OperationOutcome.builder()
                    .issue(supplementalIssues)
                    .build());
            }

            return resultBuilder.build();
        }
        catch(FHIRPersistenceFKVException e) {
            throw e;
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            // don't chain the exception to avoid leaking secrets
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing an update operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Search query implementation based on the 1385 new query builder.
     * @param context
     * @param resourceType
     * @return
     * @throws FHIRPersistenceException
     */
    @Override
    public MultiResourceResult search(FHIRPersistenceContext context, Class<? extends Resource> resourceType)
            throws FHIRPersistenceException {
        final String METHODNAME = "search";
        log.entering(CLASSNAME, METHODNAME);

        MultiResourceResult.Builder resultBuilder = MultiResourceResult.builder();
        FHIRSearchContext searchContext = context.getSearchContext();
        NewQueryBuilder queryBuilder;
        Integer searchResultCount = null;
        Select countQuery;
        Select query;

        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);
            // For PostgreSQL search queries we need to set some options to ensure better plans
            connectionStrategy.applySearchOptimizerOptions(connection, SearchHelper.isCompartmentSearch(searchContext));
            ResourceDAO resourceDao = makeResourceDAO(context, connection);
            ParameterDAO parameterDao = makeParameterDAO(connection);
            CommonValuesDAO rrd = makeCommonValuesDAO(connection);
            JDBCIdentityCache identityCache = new JDBCIdentityCacheImpl(cache, resourceDao, parameterDao, rrd);
            List<ResourceResult<? extends Resource>> resourceResults = null;

            checkModifiers(searchContext, isSystemLevelSearch(resourceType));
            IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());
            final SchemaType schemaType = connectionStrategy.getFlavor().getSchemaType();
            queryBuilder = new NewQueryBuilder(translator, connectionStrategy.getQueryHints(), identityCache);

            // Skip count query if _total=none
            if (!TotalValueSet.NONE.equals(searchContext.getTotalParameter())) {
                countQuery = queryBuilder.buildCountQuery(resourceType, searchContext, schemaType);
                if (countQuery != null) {
                    searchResultCount = resourceDao.searchCount(countQuery);
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("searchResultCount = " + searchResultCount);
                    }
                    searchContext.setTotalCount(searchResultCount);
                }
            }

            List<OperationOutcome.Issue> issues = validatePagingContext(searchContext);
            if (!issues.isEmpty()) {
                resultBuilder.outcome(OperationOutcome.builder()
                    .issue(issues)
                    .build());
                if (!searchContext.isLenient()) {
                    return resultBuilder.success(false).build();
                }
            }

            // For _summary=count or pageSize == 0, we return only the count
            if ((searchResultCount == null || searchResultCount > 0)
                    && !SummaryValueSet.COUNT.equals(searchContext.getSummaryParameter())
                    && searchContext.getPageSize() > 0) {
                query = queryBuilder.buildQuery(resourceType, searchContext, schemaType);

                List<String> elements = searchContext.getElementsParameters();

                // Only consider _summary if _elements parameter is empty
                if (elements == null && searchContext.hasSummaryParameter()) {
                    Set<String> summaryElements = null;
                    SummaryValueSet summary = searchContext.getSummaryParameter();

                    switch (summary) {
                    case TRUE:
                        summaryElements = JsonSupport.getSummaryElementNames(resourceType);
                        break;
                    case TEXT:
                        summaryElements = SearchHelper.getSummaryTextElementNames(resourceType);
                        break;
                    case DATA:
                        summaryElements = JsonSupport.getSummaryDataElementNames(resourceType);
                        break;
                    default:
                        break;
                    }

                    if (summaryElements != null) {
                        elements = new ArrayList<>();
                        elements.addAll(summaryElements);
                    }
                }

                // Sorting results of a system-level search is limited, and has a different logic
                // path than other sorted searches. Since _include and _revinclude are not supported
                // with system-level search, no special logic to handle it differently is needed here.
                List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
                if (isSystemLevelSearch(resourceType)) {
                    // If search parameters were specified other than those whose values get indexed
                    // in global values tables, then we will execute the old-style UNION'd query that
                    // was built. Otherwise, we need to execute the new whole-system filter query and
                    // then build and execute the new whole-system data query.
                    if (!allSearchParmsAreGlobal(searchContext.getSearchParameters())) {
                        resourceDTOList = resourceDao.search(query);
                    } else {
                        Map<Integer, List<Long>> resourceTypeIdToLogicalResourceIdMap = resourceDao.searchWholeSystem(query);
                        Select wholeSystemDataQuery = queryBuilder.buildWholeSystemDataQuery(searchContext,
                                resourceTypeIdToLogicalResourceIdMap, schemaType);
                        resourceDTOList = resourceDao.search(wholeSystemDataQuery);
                    }
                } else if (searchContext.hasSortParameters()) {
                    resourceDTOList = this.buildSortedResourceDTOList(resourceDao, resourceType, resourceDao.searchForIds(query), searchContext.isIncludeResourceData());
                } else {
                    resourceDTOList = resourceDao.search(query);
                }
                resourceDTOList = validateExpectedSearchPagingResults(resourceDTOList, searchContext, resultBuilder);
                resourceResults = this.convertResourceDTOList(resourceDao, resourceDTOList, resourceType, elements, searchContext.isIncludeResourceData());
                searchContext.setMatchCount(resourceResults.size());

                // Check if _include or _revinclude search. If so, generate queries for each _include or
                // _revinclude parameter and add the returned 'include' resources to the 'match' resource
                // list. All duplicates in the 'include' resources (duplicates of both 'match' and 'include'
                // resources) will be removed and _elements processing will not be done for 'include' resources.
                if (resourceResults.size() > 0 && (searchContext.hasIncludeParameters() || searchContext.hasRevIncludeParameters())) {
                    List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> includeDTOList =
                            newSearchForIncludeResources(searchContext, resourceType, queryBuilder, resourceDao, resourceDTOList, schemaType);
                    List<ResourceResult<? extends Resource>> includeResult = this.convertResourceDTOList(resourceDao, includeDTOList, resourceType, null, searchContext.isIncludeResourceData());
                    // erased version referenced via a versioned reference will be missing the resource
                    // data field, so we need to filter those out here if resource data is being requested
                    if (searchContext.isIncludeResourceData()) {
                        includeResult = includeResult.stream().filter(rr -> rr.getResource() != null).collect(Collectors.toList());
                    }
                    resourceResults.addAll(includeResult);
                }
            }

            return resultBuilder
                    .success(true)
                    .addResourceResults(resourceResults)
                    .build();
        } catch (FHIRPersistenceException e) {
            throw e;
        } catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a search operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Remove the additional resources fetched at the beginning and end of the search results for validating the expected first and last search results. 
     * Validate the input expected first resource Id and last resource Id with the search results.
     * If the expected first resource Id or last resource Id do not match with the search results then add a OperationOutcome with Issue Severity Warning to the result Builder.
     * @param resourceDTOList the list of 'match' resources
     * @param searchContext the current search context
     * @param resultBuilder The MultiResourceResult builder
     * @return the list of filtered resources after removing the additional resources.
     */
    private List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource>
        validateExpectedSearchPagingResults(List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList, FHIRSearchContext searchContext, Builder resultBuilder) {
        org.linuxforhealth.fhir.persistence.jdbc.dto.Resource firstResourceResult = null;
        org.linuxforhealth.fhir.persistence.jdbc.dto.Resource lastResourceResult = null;
        if(resourceDTOList != null) {
            if (resourceDTOList.size() > 0 && searchContext.getPageNumber() != 1 && (resourceDTOList.size() > searchContext.getPageSize() || resourceDTOList.size() <= 1)){
                firstResourceResult = resourceDTOList.get(0);
                resourceDTOList.remove(0);
                resultBuilder.expectedPreviousId(firstResourceResult.getLogicalId());
                
            } 
            if (resourceDTOList.size() > 0 && (resourceDTOList.size() > searchContext.getPageSize())) {
                lastResourceResult = resourceDTOList.get(resourceDTOList.size() - 1);
                resourceDTOList.remove(resourceDTOList.size() - 1);
                resultBuilder.expectedNextId(lastResourceResult.getLogicalId());
            }
            
            if (firstResourceResult != null && searchContext.getLastId() != null && !searchContext.getLastId().equals(firstResourceResult.getLogicalId())) {
                searchContext.addOutcomeIssue(OperationOutcome.Issue.builder()
                    .severity(IssueSeverity.WARNING)
                    .code(IssueType.CONFLICT)
                    .details(CodeableConcept.builder()
                        .text(string("Pages have shifted; check next pages for changed results"))
                        .build())
                    .build());
                
                
            }
            if (lastResourceResult != null && searchContext.getFirstId() != null && !searchContext.getFirstId().equals(lastResourceResult.getLogicalId())) {
                searchContext.addOutcomeIssue(OperationOutcome.Issue.builder()
                    .severity(IssueSeverity.WARNING)
                    .code(IssueType.CONFLICT)
                    .details(CodeableConcept.builder()
                        .text(string("Pages have shifted; check prior pages for changed results"))
                        .build())
                    .build());
                
            }
            
        }
        
        return resourceDTOList;
    }

    /**
     * Process the inclusion parameters. Build and execute a query for each parameter, and
     * collect the resulting 'include' resources to be returned with the 'match' resources.
     *
     * @param searchContext - the current search context
     * @param resourceType - the search resource type
     * @param queryBuilder - the query builder
     * @param resourceDao - the resource data access object
     * @param resourceDTOList - the list of 'match' resources
     * @param schemaType - the type of schema we are querying
     * @return the list of 'include' resources
     * @throws Exception
     */
    private List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> newSearchForIncludeResources(FHIRSearchContext searchContext,
        Class<? extends Resource> resourceType, NewQueryBuilder queryBuilder, ResourceDAO resourceDao,
        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList, SchemaType schemaType) throws Exception {

        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> allIncludeResources = new ArrayList<>();

        // Used for de-duplication
        Set<Long> allResourceIds = resourceDTOList.stream().map(r -> r.getResourceId()).collect(Collectors.toSet());

        // This is a map of iterations to query results. The query results is a map of
        // search resource type to returned logical resource IDs. The logical resource IDs
        // are used in the include queries.
        Map<Integer, Map<String, Set<String>>> queryResultMap = new HashMap<>();

        // Add base query result to map
        String resourceTypeString = resourceType.getSimpleName();
        Set<String> baseLogicalResourceIds = resourceDTOList.stream()
                .map(r -> Long.toString(r.getLogicalResourceId())).collect(Collectors.toSet());
        queryResultMap.put(0, Collections.singletonMap(resourceTypeString, baseLogicalResourceIds));

        // Process non-iterative _include parameters. These are only run against 'match' search
        // results.
        for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
            if (!includeParm.isIterate()) {
                // Build and run the query
                List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> includeResources =
                        this.runIncludeQuery(resourceType, searchContext, queryBuilder, includeParm, SearchConstants.INCLUDE,
                            baseLogicalResourceIds, queryResultMap, resourceDao, 1, allResourceIds, schemaType);

                // Add new ids to de-dup list
                allResourceIds.addAll(includeResources.stream().map(r -> r.getResourceId()).collect(Collectors.toSet()));

                // Add resources to list
                allIncludeResources.addAll(includeResources);

                // Check if max size exceeded. If so, return results and let rest helper throw exception.
                if (allIncludeResources.size() > searchContext.getMaxPageIncludeCount()) {
                    return allIncludeResources;
                }
            }
        }

        // Process non-iterative _revinclude parameters. These are only run against 'match' search
        // results.
        for (InclusionParameter revincludeParm : searchContext.getRevIncludeParameters()) {
            if (!revincludeParm.isIterate()) {
                // Build and run the query
                List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> revincludeResources =
                        this.runIncludeQuery(resourceType, searchContext, queryBuilder, revincludeParm, SearchConstants.REVINCLUDE,
                            baseLogicalResourceIds, queryResultMap, resourceDao, 1, allResourceIds, schemaType);

                // Add new ids to de-dup list
                allResourceIds.addAll(revincludeResources.stream().map(r -> r.getResourceId()).collect(Collectors.toSet()));

                // Add resources to list
                allIncludeResources.addAll(revincludeResources);

                // Check if max size exceeded. If so, return results and let rest helper throw exception.
                if (allIncludeResources.size() > searchContext.getMaxPageIncludeCount()) {
                    return allIncludeResources;
                }
            }
        }

        // Process iterative parameters.
        // - Iteration 0 is a special iteration. It will only process against resources returned by primary search
        //   if the iterative parameter's target type is the same as the primary search resource type.
        // - Iteration 1 processes against resources returned by primary search or by non-iterative
        //   _include and _revinclude search.
        // - Iteration 2 and above processes only against resources returned by the previous iteration. Note
        //   that we currently have a max of only one iteration (not including special iteration 0).
        //
        for (int i=0; i<=SearchConstants.MAX_INCLUSION_ITERATIONS; ++i) {
            // Get the map of resourceTypes for current iteration level
            Map<String, Set<String>> resourceTypeMap = queryResultMap.get(i);
            if (resourceTypeMap != null) {
                if (i == 1) {
                    // For this iteration only, include both base and included resources
                    Set<String> ids = resourceTypeMap.computeIfAbsent(resourceTypeString, k -> new HashSet<>());
                    ids.addAll(queryResultMap.get(0).get(resourceTypeString));
                }

                // Process iterative _include parameters
                for (InclusionParameter includeParm : searchContext.getIncludeParameters()) {
                    if (includeParm.isIterate() && resourceTypeMap.keySet().contains(includeParm.getJoinResourceType())) {
                        // For iteration 0, we only process if target type is same as join type
                        if (i > 0 || includeParm.getJoinResourceType().equals(includeParm.getSearchParameterTargetType())) {
                            // Get ids to query against
                            Set<String> queryIds = resourceTypeMap.get(includeParm.getJoinResourceType());

                            // Build and run the query
                            List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> includeResources =
                                    this.runIncludeQuery(resourceType, searchContext, queryBuilder, includeParm,
                                        SearchConstants.INCLUDE, queryIds, queryResultMap, resourceDao, i+1, allResourceIds, schemaType);

                            // Add new ids to de-dup list
                            allResourceIds.addAll(includeResources.stream().map(r -> r.getResourceId()).collect(Collectors.toSet()));

                            // Add resources to list
                            allIncludeResources.addAll(includeResources);

                            // Check if max size exceeded. If so, return results and let rest helper throw exception.
                            if (allIncludeResources.size() > searchContext.getMaxPageIncludeCount()) {
                                return allIncludeResources;
                            }
                        }
                    }
                }
                for (InclusionParameter revincludeParm : searchContext.getRevIncludeParameters()) {
                    if (revincludeParm.isIterate() && resourceTypeMap.keySet().contains(revincludeParm.getSearchParameterTargetType())) {
                        // For iteration 0, we only process if target type is same as join type
                        if (i > 0 || revincludeParm.getJoinResourceType().equals(revincludeParm.getSearchParameterTargetType())) {
                            // Get ids to query against
                            Set<String> queryIds = resourceTypeMap.get(revincludeParm.getSearchParameterTargetType());

                            // Build and run the query
                            List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> revincludeResources =
                                    this.runIncludeQuery(resourceType, searchContext, queryBuilder, revincludeParm,
                                        SearchConstants.REVINCLUDE, queryIds, queryResultMap, resourceDao, i+1, allResourceIds, schemaType);

                            // Add new ids to de-dup list
                            allResourceIds.addAll(revincludeResources.stream().map(r -> r.getResourceId()).collect(Collectors.toSet()));

                            // Add resources to list
                            allIncludeResources.addAll(revincludeResources);

                            // Check if max size exceeded. If so, return results and let rest helper throw exception.
                            if (allIncludeResources.size() > searchContext.getMaxPageIncludeCount()) {
                                return allIncludeResources;
                            }
                        }
                    }
                }
            }
        }

        return allIncludeResources;
    }

    /**
     * Build and execute a single query for a single inclusion parameter.
     *
     * @param resourceType - the search resource type
     * @param searchContext - the current search context
     * @param queryBuilder - the query builder
     * @param inclusionParm - the inclusion parameter for which the query is being
     *                        built and executed
     * @param includeType - either INCLUDE or REVINCLUDE
     * @param queryIds - the list of logical resource IDs of the target resources
     *                   the query is running against
     * @param queryResultMap - the map of prior query results
     * @param resourceDao - the resource data access object
     * @param iterationLevel - the current iteration level
     * @param allResourceIds - the list of all resource IDs being returned - used
     *                         for de-duplication
     * @param schemaType     - the type of schema we are querying
     * @return the list of resources returned from the query
     * @throws Exception
     */
    private List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> runIncludeQuery(Class<? extends Resource> resourceType,
        FHIRSearchContext searchContext, NewQueryBuilder queryBuilder, InclusionParameter inclusionParm,
        String includeType, Set<String> queryIds, Map<Integer, Map<String, Set<String>>> queryResultMap,
        ResourceDAO resourceDao, int iterationLevel, Set<Long> allResourceIds, SchemaType schemaType) throws Exception {

        if (queryIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Build the query. For the new query builder, we work in the actual long logical_resource_id
        // values, not strings. TODO keep the values as longs to avoid unnecessary overhead
        List<Long> logicalResourceIds = queryIds.stream().map(Long::parseLong).collect(Collectors.toList());
        Select includeQuery = queryBuilder.buildIncludeQuery(resourceType, searchContext, inclusionParm, logicalResourceIds, includeType, schemaType);

        // Execute the query and filter out duplicates
        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> includeDTOs =
                resourceDao.search(includeQuery).stream().filter(r -> !allResourceIds.contains(r.getResourceId())).collect(Collectors.toList());

        // Add query result to map.
        // The logical resource IDs are pulled from the returned DTOs and saved in a
        // map of resource type to logical resource IDs. This map is then saved in a
        // map of iteration # to resource type map.
        // On subsequent iterations, _include and _revinclude parameters which target
        // this resource type will use the associated logical resource IDs in their queries.
        if (!includeDTOs.isEmpty()) {
            Set<String> lrIds = includeDTOs.stream()
                    .map(r -> Long.toString(r.getLogicalResourceId())).collect(Collectors.toSet());
            Map<String, Set<String>> resultMap = queryResultMap.computeIfAbsent(iterationLevel, k -> new HashMap<>());

            final String targetResourceType = SearchConstants.INCLUDE.equals(includeType) ?
                    inclusionParm.getSearchParameterTargetType() : inclusionParm.getJoinResourceType();
            Set<String> resultLogicalResourceIds = resultMap.computeIfAbsent(targetResourceType, k -> new HashSet<>());
            resultLogicalResourceIds.addAll(lrIds);

            // Because the resultLogicalResourceIds may contain resources of different types, we need
            // to make sure the resourceTypeId is properly marked on each DTO. We could've selected
            // that from the database, but we have the info here, so it's easy to inject it and avoid
            // pulling another column from the database we don't actually need.
            int targetResourceTypeId = getResourceTypeId(targetResourceType);
            includeDTOs.forEach(dto -> dto.setResourceTypeId(targetResourceTypeId));
        }

        return includeDTOs;
    }

    /**
     * @return true if this instance represents a FHIR system level search
     */
    private boolean isSystemLevelSearch(Class<? extends Resource> resourceType) {
        return Resource.class.equals(resourceType);
    }

    /**
     * @param resourceType
     * @throws FHIRPersistenceNotSupportedException if the search context contains one or more unsupported modifiers
     */
    private void checkModifiers(FHIRSearchContext searchContext, boolean isSystemLevelSearch) throws FHIRPersistenceNotSupportedException {
        for (QueryParameter param : searchContext.getSearchParameters()) {
            do {
                if (param.getModifier() != null &&
                        !JDBCConstants.supportedModifiersMap.get(param.getType()).contains(param.getModifier())) {
                    throw buildNotSupportedException("Found unsupported modifier ':" + param.getModifier().value() + "'"
                            + " for search parameter '" + param.getCode() + "' of type '" + param.getType() + "'");
                }
                param = param.getNextParameter();
            } while (param != null);
        }
    }

    private FHIRPersistenceNotSupportedException buildNotSupportedException(String msg) {
        return new FHIRPersistenceNotSupportedException(msg).withIssue(OperationOutcome.Issue.builder()
                .severity(IssueSeverity.FATAL)
                .code(IssueType.NOT_SUPPORTED.toBuilder()
                        .extension(Extension.builder()
                            .url(FHIRConstants.EXT_BASE + "not-supported-detail")
                            .value(Code.of("interaction"))
                            .build())
                        .build())
                .details(CodeableConcept.builder().text(string(msg)).build())
                .build());
    }

    @Override
    public <T extends Resource> void delete(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, int versionId,
            org.linuxforhealth.fhir.model.type.Instant lastUpdated) throws FHIRPersistenceException {
        final String METHODNAME = "delete";
        log.entering(CLASSNAME, METHODNAME);

        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);

            if (context.getOffloadResponse() != null) {
                // Remember this payload offload response as part of the current transaction
                this.payloadPersistenceResponses.add(context.getOffloadResponse());
            }

            ResourceDAO resourceDao = makeResourceDAO(context, connection);

            // Create a new Resource DTO instance to represent the deletion marker.
            final int newVersionId = versionId + 1;
            org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO =
                    createResourceDTO(resourceType, logicalId, newVersionId, lastUpdated, null, null);
            resourceDTO.setDeleted(true);

            // Persist the logically deleted Resource DTO.
            resourceDao.setPersistenceContext(context);
            resourceDao.insert(resourceDTO, null, null, null, IF_NONE_MATCH_NULL);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Deleted FHIR Resource '" + resourceDTO.getResourceType() + "/" + resourceDTO.getLogicalId() + "' logicalResourceId=" + resourceDTO.getLogicalResourceId()
                            + ", version=" + resourceDTO.getVersionId());
            }
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a delete operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
                            throws FHIRPersistenceException {
        final String METHODNAME = "read";
        log.entering(CLASSNAME, METHODNAME);

        final org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO;
        List<String> elements = null;
        FHIRSearchContext searchContext = context.getSearchContext();

        if (searchContext != null) {
            elements = searchContext.getElementsParameters();

            // Only consider _summary if _elements parameter is empty
            if (elements == null && searchContext.hasSummaryParameter()) {
                Set<String> summaryElements = null;
                SummaryValueSet summary = searchContext.getSummaryParameter();

                switch (summary) {
                case TRUE:
                    summaryElements = JsonSupport.getSummaryElementNames(resourceType);
                    break;
                case TEXT:
                    summaryElements = SearchHelper.getSummaryTextElementNames(resourceType);
                    break;
                case DATA:
                    summaryElements = JsonSupport.getSummaryDataElementNames(resourceType);
                    break;
                default:
                    break;
                }

                if (summaryElements != null) {
                    elements = new ArrayList<String>();
                    elements.addAll(summaryElements);
                }
            }
        }

        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_READ.name())) {
            doCachePrefill(context, connection);
            ResourceDAO resourceDao = makeResourceDAO(context, connection);

            resourceDTO = resourceDao.read(logicalId, resourceType.getSimpleName());
            boolean resourceIsDeleted = resourceDTO != null && resourceDTO.isDeleted();

            // Fetch the resource payload if needed and convert to a model object
            final T resource = convertResourceDTO(resourceDTO, resourceType, elements);

            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(resourceDTO != null) // we didn't read anything from the DB
                    .resource(resource)
                    .deleted(resourceIsDeleted) // true if we read something and the is_deleted flag was set
                    .version(resourceDTO != null ? resourceDTO.getVersionId() : 0)
                    .lastUpdated(resourceDTO != null ? resourceDTO.getLastUpdated().toInstant() : null)
                    .interactionStatus(InteractionStatus.READ)
                    .outcome(getOutcomeIfResourceNotFound(resourceDTO, resourceType.getSimpleName(), logicalId))
                    .build();

            return result;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a read operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * This method builds an OperationOutcome required by SingleResourceResult when the
     * resource was not read from the database
     * @param resourceDTO
     * @param resourceType
     * @param logicalId
     * @return
     */
    private OperationOutcome getOutcomeIfResourceNotFound(org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO,
            String resourceType, String logicalId) {
        if (resourceDTO != null) {
            return null;
        } else {
            // Note that it's possible that the resource has been deleted, but we don't know
            // that here, because we may have asked the database to exclude deleted resources
            final String diag = "Resource not found: '" + resourceType + "/" + logicalId + "'";
            return OperationOutcome.builder()
                    .issue(Issue.builder().code(IssueType.NOT_FOUND).severity(IssueSeverity.WARNING).diagnostics(string(diag)).build())
                    .build();
        }
    }

    @Override
    public MultiResourceResult history(FHIRPersistenceContext context, Class<? extends Resource> resourceType,
            String logicalId) throws FHIRPersistenceException {
        final String METHODNAME = "history";
        log.entering(CLASSNAME, METHODNAME);

        MultiResourceResult.Builder resultBuilder = MultiResourceResult.builder();
        List<ResourceResult<? extends Resource>> resourceResults = new ArrayList<>();
        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
        FHIRHistoryContext historyContext;
        int resourceCount;
        Instant since;
        Timestamp fromDateTime = null;
        int offset;

        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_HISTORY.name())) {
            doCachePrefill(context, connection);
            ResourceDAO resourceDao = makeResourceDAO(context, connection);

            historyContext = context.getHistoryContext();
            since = historyContext.getSince();
            if (since != null) {
                fromDateTime = FHIRUtilities.convertToTimestamp(since.getValue());
            }

            resourceCount = resourceDao.historyCount(resourceType.getSimpleName(), logicalId, fromDateTime);
            historyContext.setTotalCount(resourceCount);

            List<OperationOutcome.Issue> issues = validatePagingContext(historyContext);

            if (!issues.isEmpty()) {
                resultBuilder.outcome(OperationOutcome.builder()
                    .issue(issues)
                    .build());
                if (!historyContext.isLenient()) {
                    return resultBuilder.success(false).build();
                }
            }

            if (resourceCount > 0) {
                offset = (historyContext.getPageNumber() - 1) * historyContext.getPageSize();
                resourceDTOList = resourceDao.history(resourceType.getSimpleName(), logicalId, fromDateTime, offset, historyContext.getPageSize());
                resourceResults = this.convertResourceDTOList(resourceDao, resourceDTOList, resourceType, null, true);
            }

            return resultBuilder
                    .success(true)
                    .addResourceResults(resourceResults)
                    .build();
        }
        catch(FHIRPersistenceException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a history operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Validate pageSize and pageNumber in the FHIRPagingContext instance and update
     * paging context parameters accordingly.
     *
     * @param pagingContext
     *     the FHIRPagingContext instance (FHIRSearchContext or FHIRHistoryContext)
     * @return
     *     a list of operation outcome issues if the paging context has invalid parameters
     */
    private List<OperationOutcome.Issue> validatePagingContext(FHIRPagingContext pagingContext) {
        List<OperationOutcome.Issue> issues = new ArrayList<>();

        int pageSize = pagingContext.getPageSize();
        if (pageSize < 0) {
            issues.add(OperationOutcome.Issue.builder()
                .severity(pagingContext.isLenient() ? IssueSeverity.WARNING : IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text(string("Invalid page size: " + pageSize))
                    .build())
                .build());
            // Pick a valid default if lenient
            if (pagingContext.isLenient()) {
                pagingContext.setPageSize(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT);
            }
        }

        if (pagingContext.getTotalCount() != null) {
            pagingContext.setLastPageNumber(Math.max(((pagingContext.getTotalCount() + pageSize - 1) / pageSize), 1));
        }
        int lastPageNumber = pagingContext.getLastPageNumber();

        int pageNumber = pagingContext.getPageNumber();
        if (pageNumber < 1) {
            issues.add(OperationOutcome.Issue.builder()
                .severity(pagingContext.isLenient() ? IssueSeverity.WARNING : IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text(string("Invalid page number: " + pageNumber))
                    .build())
                .build());
            // Pick a valid default if lenient
            if (pagingContext.isLenient()) {
                pagingContext.setPageNumber(FHIRConstants.FHIR_PAGE_NUMBER_DEFAULT);
            }
        } else if (pageNumber > lastPageNumber) {
            issues.add(OperationOutcome.Issue.builder()
                .severity(pagingContext.isLenient() ? IssueSeverity.WARNING : IssueSeverity.ERROR)
                .code(IssueType.INVALID)
                .details(CodeableConcept.builder()
                    .text(string("Specified page number: " + pageNumber + " is greater than last page number: " + lastPageNumber))
                    .build())
                .build());
            // Set it to the last page if lenient
            if (pagingContext.isLenient()) {
                pagingContext.setPageNumber(lastPageNumber);
            }
        }

        return issues;
    }

    /**
     * @throws FHIRPersistenceResourceDeletedException if the resource being read is currently in a deleted state and
     *         FHIRPersistenceContext.includeDeleted() is set to false
     */
    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
                        throws FHIRPersistenceException {
        final String METHODNAME = "vread";
        log.entering(CLASSNAME, METHODNAME);

        T resource = null;
        org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO = null;
        int version;
        List<String> elements = null;
        FHIRSearchContext searchContext = context.getSearchContext();

        if (searchContext != null) {
            elements = searchContext.getElementsParameters();

            // Only consider _summary if _elements parameter is empty
            if (elements == null && searchContext.hasSummaryParameter()) {
                Set<String> summaryElements = null;
                SummaryValueSet summary = searchContext.getSummaryParameter();

                switch (summary) {
                case TRUE:
                    summaryElements = JsonSupport.getSummaryElementNames(resourceType);
                    break;
                case TEXT:
                    summaryElements = SearchHelper.getSummaryTextElementNames(resourceType);
                    break;
                case DATA:
                    summaryElements = JsonSupport.getSummaryDataElementNames(resourceType);
                    break;
                default:
                    break;
                }

                if (summaryElements != null) {
                    elements = new ArrayList<String>();
                    elements.addAll(summaryElements);
                }
            }
        }

        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_VREAD.name())) {
            doCachePrefill(context, connection);
            ResourceDAO resourceDao = makeResourceDAO(context, connection);

            version = Integer.parseInt(versionId);
            resourceDTO = resourceDao.versionRead(logicalId, resourceType.getSimpleName(), version);
            resource = this.convertResourceDTO(resourceDTO, resourceType, elements);

            SingleResourceResult<T> result = new SingleResourceResult.Builder<T>()
                    .success(resourceDTO != null)
                    .interactionStatus(InteractionStatus.READ)
                    .deleted(resourceDTO != null && resourceDTO.isDeleted())
                    .version(resourceDTO != null ? resourceDTO.getVersionId() : 0)
                    .resource(resource)
                    .outcome(getOutcomeIfResourceNotFound(resourceDTO, resourceType.getSimpleName(), logicalId))
                    .build();

            return result;
        } catch (NumberFormatException e) {
            throw new FHIRPersistenceException("Invalid version id specified for vread operation: " + versionId);
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a version read operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * This method takes the passed list of sorted Resource ids, acquires the ResourceDTO corresponding to each id,
     * and returns those ResourceDTOs in a List, sorted according to the input sorted ids.
     * @param resourceDao - The resource DAO.
     * @param resourceType - The type of Resource that each id in the passed list represents.
     * @param sortedIdList - A list of Resource ids representing the proper sort order for the list of Resources to be returned.
     * @param includeResourceData include the resource DATA value
     * @return List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> - A list of ResourcesDTOs of the passed resourceType,
     * sorted according the order of ids in the passed sortedIdList.
     * @throws FHIRPersistenceException
     * @throws IOException
     */
    protected List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> buildSortedResourceDTOList(ResourceDAO resourceDao, Class<? extends Resource> resourceType, List<Long> sortedIdList,
            boolean includeResourceData)
            throws FHIRException, FHIRPersistenceException, IOException {
        final String METHOD_NAME = "buildSortedResourceDTOList";
        log.entering(this.getClass().getName(), METHOD_NAME);

        long resourceId;
        org.linuxforhealth.fhir.persistence.jdbc.dto.Resource[] sortedResourceDTOs = new org.linuxforhealth.fhir.persistence.jdbc.dto.Resource[sortedIdList.size()];
        int sortIndex;
        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> sortedResourceDTOList = new ArrayList<>();
        List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList;
        Map<Long, Integer> idPositionMap = new HashMap<>();

        // This loop builds a Map where key=resourceId, and value=its proper position in the returned sorted collection.
        for(int i = 0; i < sortedIdList.size(); i++) {
            resourceId = sortedIdList.get(i);
            idPositionMap.put(resourceId, i);
        }

        resourceDTOList = this.getResourceDTOs(resourceDao, resourceType, sortedIdList, includeResourceData);

        // Store each ResourceDTO in its proper position in the returned sorted list.
        for (org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
            sortIndex = idPositionMap.get(resourceDTO.getResourceId());
            sortedResourceDTOs[sortIndex] = resourceDTO;
        }

        for (int i = 0; i <sortedResourceDTOs.length; i++) {
            if (sortedResourceDTOs[i] != null) {
                sortedResourceDTOList.add(sortedResourceDTOs[i]);
            }
        }

        log.exiting(this.getClass().getName(), METHOD_NAME);
        return sortedResourceDTOList;
    }

    /**
     * Returns a List of Resource DTOs corresponding to the passed list of Resource IDs.
     * @param resourceDao - The resource DAO.
     * @param resourceType The type of resource being queried.
     * @param sortedIdList A sorted list of Resource IDs.
     * @param includeResourceData Include the resource DATA value
     * @return List - A list of ResourceDTOs
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    private List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> getResourceDTOs(ResourceDAO resourceDao,
            Class<? extends Resource> resourceType, List<Long> sortedIdList, boolean includeResourceData)
                    throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {

        return resourceDao.searchByIds(resourceType.getSimpleName(), sortedIdList, includeResourceData);
    }

    /**
     * Converts the passed Resource Data Transfer Object collection to a collection of FHIR Resource objects.
     * Note that if the resource has been erased or was not fetched on purpose, ResourceResult.resource
     * will be null and the caller will need to take this into account
     *
     * @param resourceDao
     * @param resourceDTOList
     * @param resourceType
     * @param elements
     * @param includeResourceData
     * @return
     * @throws FHIRException
     * @throws IOException
     */
    protected List<ResourceResult<? extends Resource>> convertResourceDTOList(ResourceDAO resourceDao, List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList,
            Class<? extends Resource> resourceType, List<String> elements, boolean includeResourceData) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO List";
        log.entering(CLASSNAME, METHODNAME);

        List<ResourceResult<? extends Resource>> resourceResults = new ArrayList<>(resourceDTOList.size());
        try {
            if (this.payloadPersistence != null) {
                // With offloading, we can use async reads to fetch the payloads in parallel, making this
                // significantly quicker for large bundles (search, history etc)
                fetchPayloadsForDTOList(resourceResults, resourceDTOList, resourceType, elements, includeResourceData);
            } else {
                for (org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                    ResourceResult<? extends Resource> resourceResult = convertResourceDTOToResourceResult(resourceDTO, resourceType, elements, includeResourceData);
                    resourceResults.add(resourceResult);
                }
            }

            // Check to make sure we got a Resource if we asked for it and expect there to be one
            if (includeResourceData) {
                for (ResourceResult<? extends Resource> resourceResult: resourceResults) {
                    if (resourceResult.getResource() == null && !resourceResult.isDeleted()) {
                        String resourceTypeName = resourceResult.getResourceTypeName();
                        if (resourceTypeName == null) {
                            resourceTypeName = resourceType.getSimpleName();
                        }
                        throw new FHIRPersistenceException("convertResourceDTO returned no resource for '"
                                + resourceTypeName + "/" + resourceResult.getLogicalId() + "'");
                    }
                }
            }

        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resourceResults;
    }

    /**
     * Fetch the payload for each of the resources in the resourceDTOList and add the
     * result to the given resourceResults list. The resourceResults list will contain
     * exactly one entry per resourceDTOList, even if the payload fetch returns nothing
     * for one or more entries.
     *
     * @param resourceResults
     * @param resourceDTOList
     * @param resourceType
     * @param elements
     * @param includeResourceData
     */
    private void fetchPayloadsForDTOList(List<ResourceResult<? extends Resource>> resourceResults,
            List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList,
            Class<? extends Resource> resourceType, List<String> elements, boolean includeResourceData)
            throws FHIRPersistenceException {
        // Our offloading API supports async, so the first task is to dispatch
        // all of our requests and then wait for everything to come back. We could
        // make this fully reactive for even better performance, but for now the dispatch/wait
        // pattern is much simpler and quick enough
        List<CompletableFuture<ResourceResult<? extends Resource>>> futures = new ArrayList<>(resourceDTOList.size());
        for (org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO: resourceDTOList) {
            // The payload needs to be read from the FHIRPayloadPersistence impl. If this is
            // a form of whole-system query (search or history), then the resource type needs
            // to come from the DTO itself
            String rowResourceTypeName = getResourceTypeInfo(resourceDTO);
            int resourceTypeId;
            if (rowResourceTypeName != null) {
                resourceTypeId = getResourceTypeId(rowResourceTypeName);
            } else {
                rowResourceTypeName = resourceType.getSimpleName();
                resourceTypeId = getResourceTypeId(resourceType);
            }

            // If the resource has been deleted, no payload has been stored so there's no
            // need to try and fetch it
            CompletableFuture<ResourceResult<? extends Resource>> cf;
            if (!resourceDTO.isDeleted()) {
                // Trigger the read and stash the async response
                cf = payloadPersistence.readResourceAsync(resourceType, rowResourceTypeName, resourceTypeId, resourceDTO.getLogicalId(), resourceDTO.getVersionId(), resourceDTO.getResourcePayloadKey(), resourceDTO.getLastUpdated().toInstant(), elements);
            } else {
                // Knock up a new ResourceResult to represent the deleted resource in the result list
                ResourceResult.Builder<? extends Resource> builder = new ResourceResult.Builder<>();
                builder.logicalId(resourceDTO.getLogicalId());
                builder.resourceTypeName(rowResourceTypeName);
                builder.deleted(resourceDTO.isDeleted()); // true in this case
                builder.resource(null); // null because deleted resources do not have a payload
                builder.version(resourceDTO.getVersionId());
                builder.lastUpdated(resourceDTO.getLastUpdated().toInstant());
                cf = CompletableFuture.completedFuture(builder.build());
            }
            futures.add(cf);
        }

        // Now collect all the responses. This would be better (slightly faster) if it were
        // a fully reactive solution, but for now it's OK - we still benefit from the fetches
        // being initiated concurrently
        log.fine("Collecting async payload responses");
        for (CompletableFuture<ResourceResult<? extends Resource>> futureResult: futures) {
            try {
                // will block here waiting for the request to complete
                resourceResults.add(futureResult.get());
            } catch (ExecutionException e) {
                // Unwrap the exceptions to avoid over-nesting
                // ExecutionException -> RuntimeException -> FHIRPersistenceException
                if (e.getCause() != null) {
                    if (e.getCause() instanceof FHIRPersistenceException) {
                        throw (FHIRPersistenceException)e.getCause();
                    } else if (e.getCause().getCause() != null && e.getCause().getCause() instanceof FHIRPersistenceException) {
                        throw (FHIRPersistenceException)e.getCause().getCause();
                    } else {
                        // unwrap the ExecutionException
                        throw new FHIRPersistenceException("Unexpected blob read error", e.getCause());
                    }
                } else {
                    throw new FHIRPersistenceException("execution failed", e);
                }
            } catch (InterruptedException e) {
                throw new FHIRPersistenceException("fetch interrupted", e);
            }
        }
    }

    /**
     * Looks up and returns an instance of TransactionSynchronizationRegistry, which is used in support of writing committed
     * data to JDBC PL in-memory caches.
     * @return TransactionSynchronizationRegistry
     * @throws FHIRPersistenceException
     */
    private TransactionSynchronizationRegistry getTrxSynchRegistry() throws FHIRPersistenceException {
        InitialContext ctxt;

        try {
            ctxt = new InitialContext();
            return (TransactionSynchronizationRegistry) ctxt.lookup(TRX_SYNCH_REG_JNDI_NAME);
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Failed to acquire TrxSynchRegistry service");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /**
     * Extracts search parameters for the passed FHIR Resource.
     * @param fhirResource - A FHIR Resource.
     * @param resourceDTOx - A Resource DTO representation of the passed FHIR Resource.
     * @return list of extracted search parameters
     * @throws Exception
     */
    private ExtractedSearchParameters extractSearchParameters(Resource fhirResource, org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTOx)
             throws Exception {
        final String METHODNAME = "extractSearchParameters";
        log.entering(CLASSNAME, METHODNAME);

        Map<SearchParameter, List<FHIRPathNode>> map;
        String code;
        String url;
        String version;
        String type;
        String expression;
        boolean isForStoring;
        boolean isCompartmentInclusionParam;

        // LinkedList because we don't need random access, we might remove from it later, and we want this fast
        List<ExtractedParameterValue> allParameters = new LinkedList<>();
        String parameterHashB64 = null;

        try {
            if (fhirResource != null) {

                map = searchHelper.extractParameterValues(fhirResource);

                for (Entry<SearchParameter, List<FHIRPathNode>> entry : map.entrySet()) {
                    SearchParameter sp = entry.getKey();
                    code = sp.getCode().getValue();
                    url = sp.getUrl().getValue();
                    version = sp.getVersion() != null ? sp.getVersion().getValue(): null;
                    final boolean wholeSystemParam = isWholeSystem(sp);
                    isForStoring = !FHIRUtil.hasTag(sp, SearchConstants.TAG_DO_NOT_STORE);
                    isCompartmentInclusionParam = FHIRUtil.hasTag(sp, SearchConstants.TAG_COMPARTMENT_INCLUSION_PARAM);

                    // As not to inject any other special handling logic, this is a simple inline check to see if
                    // _id or _lastUpdated are used, and ignore those extracted values.
                    if (SPECIAL_HANDLING.contains(code)) {
                        continue;
                    }

                    Set<String> compartments = new HashSet<>();
                    if (isCompartmentInclusionParam) {
                        for (Extension e : sp.getExtension()) {
                            if (SearchConstants.COMPARTMENT_EXT_URL.equals(e.getUrl())) {
                                compartments.add(e.getValue().as(ModelSupport.FHIR_STRING).getValue());
                            }
                        }
                    }

                    type = sp.getType().getValue();
                    expression = sp.getExpression().getValue();

                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Processing SearchParameter resource: " + fhirResource.getClass().getSimpleName() + ", code: " + code + ", type: " + type + ", expression: " + expression);
                    }

                    List<FHIRPathNode> values = entry.getValue();

                    if (SearchParamType.COMPOSITE.equals(sp.getType())) {
                        List<Component> components = sp.getComponent();
                        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();

                        for (FHIRPathNode value : values) {
                            Visitable fhirNode;
                            EvaluationContext context;
                            if (value.isResourceNode()) {
                                fhirNode = value.asResourceNode().resource();
                                context = new EvaluationContext((Resource) fhirNode);
                            } else if (value.isElementNode()) {
                                fhirNode = value.asElementNode().element();
                                context = new EvaluationContext((Element) fhirNode);
                            } else {
                                throw new IllegalStateException("Composite parameter expression must select one or more FHIR elements");
                            }

                            CompositeParmVal p = new CompositeParmVal();
                            p.setName(code);
                            p.setUrl(url);
                            p.setVersion(version);
                            p.setResourceType(fhirResource.getClass().getSimpleName());

                            for (int i = 0; i < components.size(); i++) {
                                Component component = components.get(i);
                                Collection<FHIRPathNode> nodes = evaluator.evaluate(context, component.getExpression().getValue());
                                if (nodes.isEmpty()){
                                    if (log.isLoggable(Level.FINER)) {
                                        log.finer("Component expression '" + component.getExpression().getValue() + "' resulted in 0 nodes; "
                                                + "skipping composite parameter '" + code + "'.");
                                    }
                                    continue;
                                }

                                // Alternatively, we could pull the search parameter from the FHIRRegistry so we can use versioned references.
                                // However, that would bypass search parameter filtering and so we favor the SeachUtil method here instead.
                                SearchParameter compSP = searchHelper.getSearchParameter(p.getResourceType(), component.getDefinition());
                                JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(p.getResourceType(), compSP);
                                FHIRPathNode node = nodes.iterator().next();
                                if (nodes.size() > 1 ) {
                                    // TODO: support component expressions that result in multiple nodes
                                    // On the current schema, this means creating a different CompositeParmValue for each ordered set of component values.
                                    // For example, if a composite has two components and each one's expression results in two nodes
                                    // ([Code1,Code2] and [Quantity1,Quantity2]) and each node results in a single ExtractedParameterValue,
                                    // then we need to generate CompositeParmVal objects for [Code1,Quantity1], [Code1,Quantity2],
                                    // [Code2,Quantity1], and [Code2,Quantity2].
                                    // Assumption: this should be rare.
                                    if (log.isLoggable(Level.FINE)) {
                                        log.fine("Component expression '" + component.getExpression().getValue() + "' resulted in multiple nodes; "
                                                + "proceeding with randomly chosen node '" + node.path() + "' for search parameter '" + code + "'.");
                                    }
                                }

                                try {
                                    if (node.isElementNode()) {
                                        // parameterBuilder aggregates the results for later retrieval
                                        node.asElementNode().element().accept(parameterBuilder);
                                        // retrieve the list of parameters built from all the FHIRPathElementNode values
                                        List<ExtractedParameterValue> parameters = parameterBuilder.getResult();
                                        if (parameters.isEmpty()){
                                            if (log.isLoggable(Level.FINE)) {
                                                log.fine("Selected element '" + node.path() + "' resulted in 0 extracted parameter values; "
                                                        + "skipping composite parameter '" + code + "'.");
                                            }
                                            continue;
                                        }

                                        if (parameters.size() > 1) {
                                            // TODO: support component searchParms that lead to multiple ExtractedParameterValues
                                            // On the current schema, this means creating a different CompositeParmValue for each ordered set of component values.
                                            // For example:
                                            // If a composite has two components and each results in two extracted parameters ([A,B] and [1,2] respectively)
                                            // then we need to generate CompositeParmVal objects for [A,1], [A,2], [B,1], and [B,2]
                                            // Assumption: this should only be common for Quantity search parameters with both a coded unit and a display unit,
                                            // and in these cases, the coded unit is almost always the preferred value for search.
                                            if (log.isLoggable(Level.FINE)) {
                                                log.fine("Selected element '" + node.path() + "' resulted in multiple extracted parameter values; "
                                                        + "proceeding with the first extracted value for composite parameter '" + code + "'.");
                                            }
                                        }
                                        ExtractedParameterValue componentParam = parameters.get(0);
                                        // override the component parameter name with the composite parameter name
                                        componentParam.setName(SearchHelper.makeCompositeSubCode(code, componentParam.getName()));
                                        componentParam.setUrl(url);
                                        componentParam.setVersion(version);
                                        p.addComponent(componentParam);
                                    } else if (node.isSystemValue()){
                                        ExtractedParameterValue primitiveParam = processPrimitiveValue(node.asSystemValue());
                                        primitiveParam.setName(code);
                                        primitiveParam.setUrl(url);
                                        primitiveParam.setVersion(version);
                                        primitiveParam.setResourceType(fhirResource.getClass().getSimpleName());

                                        if (log.isLoggable(Level.FINE)) {
                                            log.fine("Extracted Parameter '" + p.getName() + "' from Resource.");
                                        }
                                        p.addComponent(primitiveParam);
                                    } else {
                                        // log and continue
                                        String msg = "Unable to extract value from '" + value.path() +
                                                "'; search parameter value extraction can only be performed on Elements and primitive values.";
                                        if (log.isLoggable(Level.FINE)) {
                                            log.fine(msg);
                                        }
                                        addWarning(IssueType.INVALID, msg);
                                        continue;
                                    }
                                } catch (IllegalArgumentException e) {
                                    // log and continue with the other parameters
                                    StringBuilder msg = new StringBuilder("Skipped search parameter '" + code + "'");
                                    if (sp.getId() != null) {
                                        msg.append(" with id '" + sp.getId() + "'");
                                    }
                                    msg.append(" for resource type " + fhirResource.getClass().getSimpleName());
                                    // just use the message...no need for the whole stack trace
                                    msg.append(" due to \n" + e.getMessage());
                                    if (log.isLoggable(Level.FINE)) {
                                        log.fine(msg.toString());
                                    }
                                    addWarning(IssueType.INVALID, msg.toString());
                                }
                            }
                            if (components.size() == p.getComponent().size()) {

                                // only add the parameter if all of the components are present and accounted for
                                p.setForStoring(isForStoring);
                                allParameters.add(p);
                            }
                        }
                    } else { // ! SearchParamType.COMPOSITE.equals(sp.getType())
                        JDBCParameterBuildingVisitor parameterBuilder = new JDBCParameterBuildingVisitor(fhirResource.getClass().getSimpleName(), sp);

                        for (FHIRPathNode value : values) {

                            try {
                                if (value.isElementNode()) {
                                    // parameterBuilder aggregates the results for later retrieval
                                    value.asElementNode().element().accept(parameterBuilder);
                                } else if (value.isSystemValue()){
                                    ExtractedParameterValue p = processPrimitiveValue(value.asSystemValue());
                                    p.setName(code);
                                    p.setUrl(url);
                                    p.setVersion(version);
                                    p.setResourceType(fhirResource.getClass().getSimpleName());

                                    if (wholeSystemParam) {
                                        p.setWholeSystem(true);
                                    }
                                    p.setForStoring(isForStoring);
                                    allParameters.add(p);
                                    if (log.isLoggable(Level.FINE)) {
                                        log.fine("Extracted Parameter '" + p.getName() + "' from Resource.");
                                    }
                                } else {
                                    // log and continue
                                    String msg = "Unable to extract value from '" + value.path() +
                                            "'; search parameter value extraction can only be performed on Elements and primitive values.";
                                    if (log.isLoggable(Level.FINE)) {
                                        log.fine(msg);
                                    }
                                    addWarning(IssueType.INVALID, msg);
                                    continue;
                                }
                            } catch (IllegalArgumentException e) {
                                // log and continue with the other parameters
                                StringBuilder msg = new StringBuilder("Skipping search parameter '" + code + "'");
                                if (sp.getId() != null) {
                                    msg.append(" with id '" + sp.getId() + "'");
                                }
                                msg.append(" for resource type " + fhirResource.getClass().getSimpleName());
                                // just use the message...no need for the whole stack trace
                                msg.append(" due to \n" + e.getMessage());
                                if (log.isLoggable(Level.FINE)) {
                                    log.fine(msg.toString());
                                }
                                addWarning(IssueType.INVALID, msg.toString());
                            }
                        }
                        // retrieve the list of parameters built from all the FHIRPathElementNode values
                        List<ExtractedParameterValue> parameters = parameterBuilder.getResult();
                        for (ExtractedParameterValue p : parameters) {
                            if (wholeSystemParam) {
                                p.setWholeSystem(true);
                            }
                            p.setForStoring(isForStoring);
                            if (p instanceof ReferenceParmVal) {
                                p.setCompartments(compartments);
                            }
                            allParameters.add(p);
                            if (log.isLoggable(Level.FINE)) {
                                log.fine("Extracted Parameter '" + p.getName() + "' from Resource.");
                            }
                        }
                    }
                }

                // Augment the extracted parameter list with special values we use to represent compartment relationships.
                // These references are stored as tokens and are used by the search query builder
                // for compartment-based searches
                addCompartmentParams(allParameters, fhirResource.getClass().getSimpleName(), fhirResource.getId());

                // If this is a definitional resource, augment the extracted parameter list with a composite
                // parameter that will be used for canonical searches. It will contain the url and version
                // values.
                addCanonicalCompositeParam(allParameters);
            }

            // Remove parameters that aren't to be stored
            boolean anyRemoved = allParameters.removeIf(value -> !value.isForStoring());
            if (anyRemoved) {
                log.warning("The set of extracted parameters values unexpectedly contained values "
                        + "that weren't for storing; these have been removed");
            }

            // Generate the hash which is used to quickly determine whether the extracted parameters
            // are different than the extracted parameters that currently exist in the database.
            // Sort extracted parameter values in natural order first, to ensure the hash generated by
            // this visitor is deterministic.
            sortExtractedParameterValues(allParameters);
            ParameterHashVisitor phv = new ParameterHashVisitor(legacyWholeSystemSearchParamsEnabled);
            for (ExtractedParameterValue p: allParameters) {
                p.accept(phv);
            }
            parameterHashB64 = phv.getBase64Hash();

        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return new ExtractedSearchParameters(allParameters, parameterHashB64);
    }

    /**
     * Sorts the extracted parameter values in natural order. If the list contains any composite parameter values,
     * those are sorted before the list itself is sorted. Since composite parameters cannot themselves contain composites,
     * doing this with a recursive call is ok.
     * @param extractedParameterValues the extracted parameter values
     */
    private void sortExtractedParameterValues(List<ExtractedParameterValue> extractedParameterValues) {
        for (ExtractedParameterValue extractedParameterValue : extractedParameterValues) {
            if (extractedParameterValue instanceof CompositeParmVal) {
                CompositeParmVal compositeParmVal = (CompositeParmVal) extractedParameterValue;
                sortExtractedParameterValues(compositeParmVal.getComponent());
            }
        }
        Collections.sort(extractedParameterValues);
    }

    /**
     * Should we also store values for this {@link SearchParameter} in the special whole-system
     * param tables (for more efficient whole-system search queries).
     * @param sp
     * @return
     */
    private boolean isWholeSystem(SearchParameter sp) {

        // Strip off any :text suffix before we check to see if this is in the
        // whole-system search parameter list
        String parameterName = sp.getCode().getValue();
        if (parameterName.endsWith(SearchConstants.TEXT_MODIFIER_SUFFIX)) {
            parameterName = parameterName.substring(0, parameterName.length() - SearchConstants.TEXT_MODIFIER_SUFFIX.length());
        }

        return SearchConstants.SYSTEM_LEVEL_GLOBAL_PARAMETER_NAMES.contains(parameterName);
    }

    /**
     * Augment the given allParameters list with internal parameters that represent the relationship
     * between the fhirResource and its compartments. These parameter values are subsequently used
     * to improve the performance of compartment-based FHIR search queries. See
     * {@link CompartmentHelper#makeCompartmentParamName(String)} for details on how the
     * parameter name is composed for each relationship.
     * @param allParameters
     * @param resourceType the resource type of the resource we are extracting parameter values from
     * @param resourceId the resource id of the resource we are extracting parameter values from
     */
    protected void addCompartmentParams(List<ExtractedParameterValue> allParameters, String resourceType, String resourceId) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Processing compartment parameters for resourceType: " + resourceType);
        }

        Set<ExtractedParameterValue> compartmentMemberships = new HashSet<>();
        for (ExtractedParameterValue epv : allParameters) {
            if (!epv.isCompartmentInclusionParam()) {
                continue;
            }

            if (!(epv instanceof ReferenceParmVal)) {
                log.warning("Skipping extracted value for compartment inclusion param " + epv.getName() + "; "
                        + "expected ReferenceParmVal but found " + epv.getClass().getSimpleName());
                continue;
            }

            ReferenceValue rv = ((ReferenceParmVal) epv).getRefValue();
            if (rv != null && rv.getType() == ReferenceType.LITERAL_RELATIVE
                    && epv.getCompartments().contains(rv.getTargetResourceType())) {
                String internalCompartmentParamName = CompartmentHelper.makeCompartmentParamName(rv.getTargetResourceType());

                if (epv.isForStoring()) {
                    // create a copy of the extracted parameter value but with the new internal compartment parameter name
                    ReferenceParmVal pv = new ReferenceParmVal();
                    pv.setName(internalCompartmentParamName);
                    pv.setResourceType(resourceType);
                    pv.setCompartments(Collections.singleton(rv.getTargetResourceType()));
                    pv.setRefValue(rv);

                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Adding compartment reference parameter: [" + resourceType + "] " +
                                internalCompartmentParamName + " = " + rv.getTargetResourceType() + "/" + rv.getValue());
                    }
                    compartmentMemberships.add(pv);
                } else {
                    // since the extracted parameter value isn't going to be stored,
                    // just rename it with our internal compartment param name and mark that for storing
                    epv.setName(internalCompartmentParamName);
                    epv.setForStoring(true);
                }
            }
        }

        // as of https://github.com/LinuxForHealth/FHIR/issues/3091 we flag a resource as a member of its own compartment
        if (searchHelper.isCompartmentType(resourceType)) {
            ReferenceParmVal pv = new ReferenceParmVal();
            pv.setName(CompartmentHelper.makeCompartmentParamName(resourceType));
            pv.setResourceType(resourceType);
            pv.setCompartments(Collections.singleton(resourceType));
            pv.setRefValue(new ReferenceValue(resourceType, resourceId, ReferenceType.LITERAL_RELATIVE, null));

            compartmentMemberships.add(pv);
        }

        allParameters.addAll(compartmentMemberships);
    }

    /**
     * Augment the given allParameters list with internal parameters that represent the relationship
     * between the url and version parameters. These parameter values are subsequently used in
     * canonical reference searches. See {@link CompartmentHelper#makeCompartmentParamName(String)} for
     * details on how the parameter name is composed.
     * @param allParameters
     */
    protected void addCanonicalCompositeParam(List<ExtractedParameterValue> allParameters) throws FHIRSearchException {
        StringParmVal urlParm = null;
        TokenParmVal versionParm = null;

        // Look for url and version parameters
        for (ExtractedParameterValue parameter : allParameters) {
            if (parameter.getName().equals(SearchConstants.URL) && parameter instanceof StringParmVal) {
                urlParm = (StringParmVal) parameter;
            } else if (parameter.getName().equals(SearchConstants.VERSION) && parameter instanceof TokenParmVal) {
                versionParm = (TokenParmVal) parameter;
            }
        }

        // If we found a url parameter, create the composite parameter. The version parameter
        // can be null.
        if (urlParm != null) {
            // Create a canonical composite parameter
            CompositeParmVal cp = new CompositeParmVal();
            cp.setResourceType(urlParm.getResourceType());
            cp.setName(SearchConstants.URL + SearchConstants.CANONICAL_SUFFIX);
            cp.setUrl(urlParm.getUrl());
            cp.setVersion(urlParm.getVersion());

            // url
            StringParmVal up = new StringParmVal();
            up.setResourceType(cp.getResourceType());
            up.setName(SearchHelper.makeCompositeSubCode(cp.getName(), SearchConstants.CANONICAL_COMPONENT_URI));
            up.setUrl(cp.getUrl());
            up.setVersion(cp.getVersion());
            up.setValueString(urlParm.getValueString());
            cp.addComponent(up);

            // version
            StringParmVal vp = new StringParmVal();
            vp.setResourceType(cp.getResourceType());
            vp.setName(SearchHelper.makeCompositeSubCode(cp.getName(), SearchConstants.CANONICAL_COMPONENT_VERSION));
            vp.setUrl(cp.getUrl());
            vp.setVersion(cp.getVersion());
            vp.setValueString(versionParm != null ? versionParm.getValueCode() : null);
            cp.addComponent(vp);

            if (log.isLoggable(Level.FINE)) {
                log.fine("Adding canonical composite parameter: [" + cp.getResourceType() + "] " +
                            up.getName() + " = " + up.getValueString() + ", " +
                            vp.getName() + " = " + vp.getValueString());
            }

            allParameters.add(cp);
        }
    }

    /**
     * Create a Parameter DTO from the primitive value.
     * Note: this method only sets the value;
     * caller is responsible for setting all other fields on the created Parameter.
     */
    private ExtractedParameterValue processPrimitiveValue(FHIRPathSystemValue systemValue) {
        ExtractedParameterValue parameter = null;
        if (systemValue.isBooleanValue()) {
            TokenParmVal p = new TokenParmVal();
            if (systemValue.asBooleanValue()._boolean()) {
                p.setValueCode("true");
            } else {
                p.setValueCode("false");
            }
            parameter = p;
        } else if (systemValue.isTemporalValue()) {
            DateParmVal p = new DateParmVal();
            TemporalAccessor v = systemValue.asTemporalValue().temporal();
            java.time.Instant inst = DateTimeHandler.generateValue(v);
            p.setValueDateStart(DateTimeHandler.generateTimestamp(inst));
            p.setValueDateEnd(DateTimeHandler.generateTimestamp(inst));
            parameter = p;
        } else if (systemValue.isStringValue()) {
            StringParmVal p = new StringParmVal();
            p.setValueString(systemValue.asStringValue().string());
            parameter = p;
        } else if (systemValue.isNumberValue()) {
            NumberParmVal p = new NumberParmVal();
            p.setValueNumber(systemValue.asNumberValue().decimal());
            parameter = p;
        } else if (systemValue.isQuantityValue()) {
            QuantityParmVal p = new QuantityParmVal();
            p.setValueNumber(systemValue.asQuantityValue().value());
            p.setValueSystem("http://unitsofmeasure.org"); // FHIRPath Quantity requires UCUM units
            p.setValueCode(systemValue.asQuantityValue().unit());
            parameter = p;
        }
        return parameter;
    }

    /**
     * Open a connection to the database and pass to the data access objects.
     * Caller must close the returned connection after use (before the
     * transaction completes
     * @return
     * @throws FHIRPersistenceDBConnectException
     */
    private Connection openConnection() throws FHIRPersistenceDBConnectException {
        final String METHODNAME = "openConnection";
        log.entering(CLASSNAME, METHODNAME);
        try {
            return connectionStrategy.getConnection();
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {

        try (Connection connection = connectionStrategy.getConnection()) {
            if (connection.isValid(2)) {
                // Check the schema version
                return checkSchemaVersion(connection);
            } else {
                String msg = "The database connection was not valid";
                log.severe(msg);
                return buildSchemaVersionErrorOperationOutcome(msg);
            }
        } catch (SQLException e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Error while validating the database connection");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /**
     * Checks to make sure the installed schema matches the version we expect
     * @param connection
     * @return an OperationOutcome
     */
    private OperationOutcome checkSchemaVersion(Connection connection) throws SQLException, FHIRPersistenceException {
        final String schemaName = this.schemaNameSupplier.getSchemaForRequestContext(connection);
        IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());

        int versionId = -1;
        try {
            GetSchemaVersion dao = new GetSchemaVersion(schemaName);
            versionId = dao.run(translator, connection);
        } catch (UndefinedNameException x) {
            // definitely the wrong version because our schema_versions tracking table
            // doesn't exist
            versionId = -1;
        }

        // compare what's in the database with the latest FhirSchemaVersion. For now,
        // we allow the database schema to be equal to or ahead of the latest schema known
        // to this instance. This helps with rolling deploys.
        FhirSchemaVersion latest = FhirSchemaVersion.getLatestFhirSchemaVersion();
        if (versionId < 0) {
            // the new server code is running against a database which hasn't been
            // updated to include the whole-schema-version and control tables
            String msg = "Schema update required: whole-schema-version not supported";
            log.severe(msg);
            // not supported - database needs to be updated
            return buildSchemaVersionErrorOperationOutcome(msg);
        } else if (versionId > latest.vid()) {
            // the database has been updated, but this is the old code still running
            String msg = "Deployment update required: database schema version [" + versionId
                    + "] is newer than code schema version [" + latest.vid() + "]";
            log.warning(msg);
            // this is OK - code needs to be updated - return a warning
            return buildSchemaVersionWarningOperationOutcome(msg);
        } else if (versionId < latest.vid()) {
            // the code is newer than the database schema
            String msg = "Schema update required: database schema version [" + versionId
                    + "] is older than code schema version [" + latest.vid() + "]";
            log.severe(msg);
            // not supported - database needs to be updated
            return buildSchemaVersionErrorOperationOutcome(msg);
        } else {
            // perfect match
            return buildOKOperationOutcome();
        }
    }

    /**
     * Retrieves (via a JNDI lookup) a reference to the UserTransaction. If the JNDI lookup fails, we'll assume that
     * we're not running inside the container.
     */
    protected UserTransaction retrieveUserTransaction(String jndiName) {
        UserTransaction txn = null;
        try {
            InitialContext ctx = new InitialContext();
            txn = (UserTransaction) ctx.lookup(jndiName);
        } catch (Throwable t) {
            // ignore any exceptions here.
        }

        return txn;
    }

    /**
     * Converts the passed Resource Data Transfer Object collection to a collection of FHIR Resource objects.
     * @param resourceDTOList
     * @param resourceType
     * @return
     * @throws FHIRException
     * @throws IOException
     */
    protected <T extends Resource> List<T> convertResourceDTOList(List<org.linuxforhealth.fhir.persistence.jdbc.dto.Resource> resourceDTOList,
            Class<T> resourceType) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTOList";
        log.entering(CLASSNAME, METHODNAME);

        List<T> resources = new ArrayList<>();
        try {
            for (org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO : resourceDTOList) {
                resources.add(this.convertResourceDTO(resourceDTO, resourceType, null));
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
        return resources;
    }

    /**
     * Converts the passed Resource Data Transfer Object to a FHIR Resource object. The result
     * will be null if the resourceDTO passed in is null.
     *
     * @param resourceDTO - The resource read from the database, or null if the resource doesn't exist
     * @param resourceType - The FHIR type of resource to be converted.
     * @param elements - An optional filter for including only specified elements inside a Resource.
     * @return Resource - A FHIR Resource object representation of the data portion of the passed Resource DTO.
     * @throws FHIRException
     * @throws IOException
     */
    @Deprecated
    private <T extends Resource> T convertResourceDTO(org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO,
            Class<T> resourceType, List<String> elements) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO";
        log.entering(CLASSNAME, METHODNAME);
        T result;
        if (resourceDTO != null) {
            if (isOffloadingSupported()) {
                // The payload needs to be read from the FHIRPayloadPersistence impl. If this is
                // a form of whole-system query (search or history), then the resource type needs
                // to come from the DTO itself
                String rowResourceTypeName = getResourceTypeInfo(resourceDTO);
                int resourceTypeId;
                if (rowResourceTypeName != null) {
                    resourceTypeId = getResourceTypeId(rowResourceTypeName);
                } else {
                    rowResourceTypeName = resourceType.getSimpleName();
                    resourceTypeId = getResourceTypeId(resourceType);
                }

                // If a specific version of a resource has been deleted using $erase, it
                // is possible for the result here to be null.
                if (!resourceDTO.isDeleted()) {
                    result = payloadPersistence.readResource(resourceType, rowResourceTypeName, resourceTypeId, resourceDTO.getLogicalId(), resourceDTO.getVersionId(), resourceDTO.getResourcePayloadKey(), elements);
                } else {
                    result = null; // we no longer store payloads for deleted resources, so have nothing to return
                }
            } else {
                // original impl - the resource, if any, was read from the RDBMS
                if (resourceDTO.getDataStream() != null) {
                    try (InputStream in = new GZIPInputStream(resourceDTO.getDataStream().inputStream())) {
                        FHIRParser parser = FHIRParser.parser(Format.JSON);
                        parser.setValidating(false);
                        if (elements != null) {
                            // parse/filter the resource using elements
                            result = parser.as(FHIRJsonParser.class).parseAndFilter(in, elements);
                            if (resourceType.equals(result.getClass()) && !FHIRUtil.hasTag(result, SearchConstants.SUBSETTED_TAG)) {
                                // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                                result = FHIRUtil.addTag(result, SearchConstants.SUBSETTED_TAG);
                            }
                        } else {
                            result = parser.parse(in);
                        }
                    }
                } else {
                    // Null DATA column means that this resource version was probably removed
                    // by $erase
                    result = null;
                }
            }
        } else {
            // resource doesn't exist
            result = null;
        }

        log.exiting(CLASSNAME, METHODNAME);
        return result;
    }

    private <T extends Resource> ResourceResult<T> convertResourceDTOToResourceResult(org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO,
        Class<T> resourceType, List<String> elements, boolean includeData) throws FHIRException, IOException {
        final String METHODNAME = "convertResourceDTO";
        log.entering(CLASSNAME, METHODNAME);
        Objects.requireNonNull(resourceDTO, "resourceDTO must be not null");
        T resource;
        boolean treatErasedAsDeleted = false;

        if (includeData) {
            // original impl - the resource, if any, was read from the RDBMS
            if (resourceDTO.getDataStream() != null) {
                try (InputStream in = new GZIPInputStream(resourceDTO.getDataStream().inputStream())) {
                    FHIRParser parser = FHIRParser.parser(Format.JSON);
                    parser.setValidating(false);
                    if (elements != null) {
                        // parse/filter the resource using elements
                        resource = parser.as(FHIRJsonParser.class).parseAndFilter(in, elements);
                        if (resourceType.equals(resource.getClass()) && !FHIRUtil.hasTag(resource, SearchConstants.SUBSETTED_TAG)) {
                            // add a SUBSETTED tag to this resource to indicate that its elements have been filtered
                            resource = FHIRUtil.addTag(resource, SearchConstants.SUBSETTED_TAG);
                        }
                    } else {
                        resource = parser.parse(in);
                    }
                }
            } else {
                // Queries may return a NULL for the DATA column if the resource has been erased.
                resource = null;
                treatErasedAsDeleted = true;
            }
        } else {
            // Because we never selected the data column, we don't know if this resource
            // version has been erased or not. The only thing we can do is return a null
            // resource.
            resource = null;
        }

        // Note that resource may be null. We return a ResourceResult so we can
        // communicate back the type/id/version information even if we didn't get
        // an actual resource object
        String resourceTypeName = getResourceTypeInfo(resourceDTO);
        if (resourceTypeName == null) {
            // By default we simply use the requested type name. This makes the ResourceResult
            // easier to consume by the caller
            resourceTypeName = resourceType.getSimpleName();
        }
        ResourceResult.Builder<T> builder = new ResourceResult.Builder<>();
        builder.logicalId(resourceDTO.getLogicalId());
        builder.resourceTypeName(resourceTypeName);
        builder.deleted(resourceDTO.isDeleted());
        builder.resource(resource); // can be null
        builder.version(resourceDTO.getVersionId());
        builder.lastUpdated(resourceDTO.getLastUpdated().toInstant());

        // If we encounter a resource version that was erased, its data column will be
        // null so we can't parse a resource value. We need to treat it as a deleted
        // resource because otherwise the REST layer will expect the resource value to
        // be present
        if (treatErasedAsDeleted && !resourceDTO.isDeleted()) {
            builder.deleted(true);
        }

        log.exiting(CLASSNAME, METHODNAME);
        return builder.build();
    }

    /**
     * Get the resource type name of the resource represented by the from the
     * given resourceDTO. This is only done if the resourceTypeId field in the
     * resourceDTO has been set. If not, returns null.
     * @param resourceDTO
     * @throws FHIRPersistenceException if the resourceTypeId is set
     *      but the value cannot be found in the cache
     * @return
     */
    private String getResourceTypeInfo(org.linuxforhealth.fhir.persistence.jdbc.dto.Resource resourceDTO)
                throws FHIRPersistenceException {
        return getResourceTypeInfo(resourceDTO.getResourceTypeId());
    }

    /**
     * Get the resource type name for the given resourceTypeId if it is a valid
     * key (>=0). Returns null otherwise
     * @param resourceTypeId
     * @return
     * @throws FHIRPersistenceException
     */
    private String getResourceTypeInfo(int resourceTypeId) throws FHIRPersistenceException {
        final String result;
        // resource type name needs to be derived from the resourceTypeId returned by the DB select query
        log.fine(() -> "getResourceTypeInfo(" + resourceTypeId + ")");
        if (resourceTypeId >= 0) {
            result = cache.getResourceTypeNameCache().getName(resourceTypeId);
            if (result == null) {
                // the cache is preloaded, so this should never happen
                log.severe("No entry found in cache for resourceTypeId = " + resourceTypeId);
                throw new FHIRPersistenceException("Resource type not found in cache");
            }
        } else {
            result = null;
        }

        return result;
    }

    /**
     * Get the database resourceTypeId from the cache.
     * @param resourceType
     * @return
     * @throws FHIRPersistenceException if the resource type is not found in the cache.
     */
    private int getResourceTypeId(Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        return getResourceTypeId(resourceType.getSimpleName());
    }

    /**
     * Get the database resourceTypeId from the cache.
     * @param resourceTypeName
     * @return
     * @throws FHIRPersistenceException if the resource type is not found in the cache.
     */
    private int getResourceTypeId(String resourceTypeName) throws FHIRPersistenceException {
        final Integer resourceTypeId = cache.getResourceTypeCache().getId(resourceTypeName);
        if (resourceTypeId == null) {
            // the cache is preloaded, so this should never happen
            log.severe("Resource type missing from resource type cache: '" + resourceTypeName + "'");
            throw new FHIRPersistenceException("Resource type id not found in resource type cache");
        }
        return resourceTypeId;
    }

    @Override
    public boolean isTransactional() {
        return true;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        // after 4.3 we no longer return `this` and FHIRPersistenceJDBCImpl no longer implements
        // FHIRPersistenceTransaction. Instead, transaction handling is managed inside
        // the new FHIRTransactionImpl, which also now includes the logic to determine
        // which instance started the transaction, and therefore which instance should
        // issue the commit (to handle cases where FHIRPersistenceJDBCImpl might be nested.
        return this.transactionAdapter;
    }

    @Override
    public boolean isDeleteSupported() {
        return true;
    }

    private OperationOutcome buildOKOperationOutcome() {
        return FHIRUtil.buildOperationOutcome("All OK", IssueType.INFORMATIONAL, IssueSeverity.INFORMATION);
    }

    private OperationOutcome buildSchemaVersionErrorOperationOutcome(String msg) {
        return FHIRUtil.buildOperationOutcome(msg, IssueType.NO_STORE, IssueSeverity.ERROR);
    }

    private OperationOutcome buildSchemaVersionWarningOperationOutcome(String msg) {
        return FHIRUtil.buildOperationOutcome(msg, IssueType.INFORMATIONAL, IssueSeverity.WARNING);
    }

    /**
     * Associate a supplemental warning with the current request
     */
    private void addWarning(IssueType issueType, String message, String... expression) {
        supplementalIssues.add(OperationOutcome.Issue.builder()
                .severity(IssueSeverity.WARNING)
                .code(issueType)
                .details(CodeableConcept.builder()
                    .text(string(message))
                    .build())
                .expression(Arrays.stream(expression).map(org.linuxforhealth.fhir.model.type.String::string).collect(Collectors.toList()))
                .build());
    }

    @Override
    public String getSchemaForRequestContext(Connection connection) throws FHIRPersistenceDBConnectException {
        String datastoreId = FHIRRequestContext.get().getDataStoreId();

        // Retrieve the property group pertaining to the specified datastore.
        // Backup plan. Since release 4.5.0 we are no longer using the FHIR
        // proxy datasource mechanism which means there won't be a connectionProperties
        // property group in the datasource configuration. The currentSchema must
        // therefore be provided as the datasource property group level.
        String dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId;
        PropertyGroup dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);

        if (dsPG != null) {
            try {
                String currentSchema = dsPG.getStringProperty("currentSchema", null);
                if (currentSchema == null) {
                    // If we're using legacy proxy datastore configuration, then the schema
                    // name might come from the connectionProperties group
                    dsPropertyName = FHIRConfiguration.PROPERTY_DATASOURCES + "/" + datastoreId + "/connectionProperties";
                    dsPG = FHIRConfigHelper.getPropertyGroup(dsPropertyName);
                    if (dsPG != null) {
                        currentSchema = dsPG.getStringProperty("currentSchema", null);
                    }
                }

                if (currentSchema == null) {
                    log.log(Level.SEVERE, "Mandatory currentSchema value missing for datastore '" + datastoreId + "'");
                    throw new FHIRPersistenceDBConnectException("Datastore configuration issue. Details in server logs");
                }

                // never null at this point
                return currentSchema;
            } catch (Exception x) {
                log.log(Level.SEVERE, "Datastore configuration issue for '" + datastoreId + "'", x);
                throw new FHIRPersistenceDBConnectException("Datastore configuration issue. Details in server logs");
            }
        } else {
            log.fine("there are no datasource properties found for : [" + dsPropertyName + "]");
            throw new FHIRPersistenceDBConnectException("Datastore configuration issue. Details in server logs");
        }
    }

    /**
     * Prefill the caches
     */
    public void doCachePrefill(FHIRPersistenceContext context, Connection connection) throws FHIRPersistenceException {
        // Perform the cache prefill just once (for a given tenant). This isn't synchronous, so
        // there's a chance for other threads to slip in before the prefill completes. Those threads
        // just end up repeating the prefill - a little extra work one time to avoid unnecessary locking
        // Note - this is done as the first thing in a transaction so there's no concern about reading
        // uncommitted values.
        if (cache.needToPrefill()) {
            ResourceDAO resourceDao = makeResourceDAO(context, connection);
            ParameterDAO parameterDao = makeParameterDAO(connection);
            FHIRPersistenceJDBCCacheUtil.prefill(resourceDao, parameterDao, cache);
            cache.clearNeedToPrefill();
        }
    }

    @Override
    public boolean isReindexSupported() {
        return true;
    }

    @Override
    public boolean isOffloadingSupported() {
        return this.payloadPersistence != null;
    }

    @Override
    public int reindex(FHIRPersistenceContext context, OperationOutcome.Builder operationOutcomeResult, java.time.Instant tstamp, List<Long> indexIds,
            String resourceLogicalId, boolean force) throws FHIRPersistenceException {
        final String METHODNAME = "reindex";
        log.entering(CLASSNAME, METHODNAME);

        int result = 0;
        ResourceIndexRecord rir = null;

        if (log.isLoggable(Level.FINE)) {
            log.fine("reindex tstamp=" + tstamp.toString());
        }

        if (tstamp.isAfter(java.time.Instant.now())) {
            // protect against setting a future timestamp, which could otherwise
            // disable the ability to reindex anything
            throw new FHIRPersistenceException("Reindex tstamp cannot be in the future");
        }

        if (indexIds != null) {
            log.info("Reindex requested for index IDs " + indexIds);
        }

        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);
            ResourceDAO resourceDao = makeResourceDAO(context, connection);
            ParameterDAO parameterDao = makeParameterDAO(connection);
            ReindexResourceDAO reindexDAO = FHIRResourceDAOFactory.getReindexResourceDAO(connection, FhirSchemaConstants.FHIR_ADMIN, schemaNameSupplier.getSchemaForRequestContext(connection), connectionStrategy.getFlavor(), this.trxSynchRegistry, this.cache, parameterDao);
            // Obtain a resource we will reindex in this request/transaction. The record is locked as part
            // of its selection, so we avoid a lot of (but not all) deadlock issues
            Integer resourceTypeId = null;
            String resourceType = null;
            String logicalId = null;
            if (resourceLogicalId != null) {
                // Restrict reindex to a specific resource type or resource e.g. "Patient" or "Patient/abc123"
                String[] parts = resourceLogicalId.split("/");
                if (parts.length == 1) {
                    // Limit to resource type
                    resourceType = parts[0];
                } else if (parts.length == 2) {
                    // Limit to a single resource
                    resourceType = parts[0];
                    logicalId = parts[1];
                }

                // Look up the optional resourceTypeId for the given resourceType parameter
                resourceTypeId = cache.getResourceTypeCache().getId(resourceType);
            }
            int indexIdsProcessed = 0;

            // If list of indexIds was specified, loop over those. Otherwise, since we skip over
            // deleted resources we have to loop until we find something not deleted, or reach the end.
            do {
                long start = System.nanoTime();
                rir = reindexDAO.getResourceToReindex(tstamp, indexIds != null ? indexIds.get(indexIdsProcessed++) : null, resourceTypeId, logicalId);
                long end = System.nanoTime();

                if (log.isLoggable(Level.FINER)) {
                    double elapsed = (end-start)/1e6;
                    log.finer(String.format("Selected %d resource for reindexing in %.3f ms ", rir != null ? 1 : 0, elapsed));
                }

                if (rir != null) {

                    if (log.isLoggable(Level.FINE)) {
                        log.fine("Reindexing FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'");
                    }

                    // Read the current resource
                    org.linuxforhealth.fhir.persistence.jdbc.dto.Resource existingResourceDTO = resourceDao.read(rir.getLogicalId(), rir.getResourceType());
                    if (existingResourceDTO != null && !existingResourceDTO.isDeleted()) {
                        rir.setDeleted(false); // just to be clear
                        Class<? extends Resource> resourceTypeClass = getResourceType(rir.getResourceType());
                        reindexDAO.setPersistenceContext(context);
                        updateParameters(rir, resourceTypeClass, existingResourceDTO, reindexDAO, operationOutcomeResult, force);

                        // result is only 0 if getResourceToReindex doesn't give us anything because this indicates
                        // there's nothing left to do
                        result++;
                    } else {
                        // Skip this particular resource because it has been deleted
                        if (log.isLoggable(Level.FINE)) {
                            log.fine("Skipping reindex for deleted FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'");
                        }
                        rir.setDeleted(true);
                    }
                }
            } while ((indexIds != null && indexIdsProcessed < indexIds.size()) || (indexIds == null && rir != null && rir.isDeleted()));

        } catch(FHIRPersistenceFKVException e) {
            getTransaction().setRollbackOnly();
            log.log(Level.SEVERE, "Unexpected error while performing reindex" + (rir != null ? (" of FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'") : ""), e);
            throw e;
        } catch(FHIRPersistenceException e) {
            getTransaction().setRollbackOnly();
            log.log(Level.SEVERE, "Unexpected error while performing reindex" + (rir != null ? (" of FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'") : ""), e);
            throw e;
        } catch (DataAccessException dax) {
            getTransaction().setRollbackOnly();

            // It's possible this is a deadlock exception, in which case it could be considered retryable
            if (dax.isTransactionRetryable()) {
                log.log(Level.WARNING, "Retryable error while performing reindex" + (rir != null ? (" of FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'") : ""), dax);
                FHIRPersistenceDataAccessException fpx = new FHIRPersistenceDataAccessException("Data access error while performing a reindex operation.", dax);
                fpx.setTransactionRetryable(true);
                throw fpx;
            } else {
                log.log(Level.SEVERE, "Non-retryable error while performing reindex" + (rir != null ? (" of FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'") : ""), dax);
                throw new FHIRPersistenceDataAccessException("Data access error while performing a reindex operation.");
            }
        } catch(Throwable e) {
            getTransaction().setRollbackOnly();
            log.log(Level.SEVERE, "Unexpected error while performing a reindex" + (rir != null ? (" of FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'") : ""), e);
            // don't chain the exception to avoid leaking secrets
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a reindex operation.");
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return result;
    }

    /**
     * Update the parameters for the resource described by the given DTO
     * @param <T>
     * @param rir the resource index record
     * @param resourceTypeClass the resource type class
     * @param existingResourceDTO the existing resource DTO
     * @param reindexDAO the reindex resource DAO
     * @param operationOutcomeResult the operation outcome result
     * @param force
     * @throws Exception
     */
    public <T extends Resource> void updateParameters(ResourceIndexRecord rir, Class<T> resourceTypeClass, org.linuxforhealth.fhir.persistence.jdbc.dto.Resource existingResourceDTO,
        ReindexResourceDAO reindexDAO, OperationOutcome.Builder operationOutcomeResult, boolean force) throws Exception {
        if (existingResourceDTO != null && !existingResourceDTO.isDeleted()) {
            T existingResource = this.convertResourceDTO(existingResourceDTO, resourceTypeClass, null);

            // Extract parameters from the resource payload.
            ExtractedSearchParameters searchParameters = this.extractSearchParameters(existingResource, existingResourceDTO);

            // Compare the hash of the extracted parameters with the hash in the index record.
            // If hash in the index record is not null and it matches the hash of the extracted parameters, then no need to replace the
            // extracted search parameters in the database tables for this resource, which helps with performance during reindex.
            if (force || rir.getParameterHash() == null || !rir.getParameterHash().equals(searchParameters.getParameterHashB64())) {
                reindexDAO.updateParameters(rir.getResourceType(), searchParameters.getParameters(), searchParameters.getParameterHashB64(), rir.getLogicalId(), rir.getLogicalResourceId());
                // Instead of storing the parameter values directly, we accumulate all the values first
                // then store just before the commit. This reduces the amount of time we hold locks
                // allows us to implement more efficient batch-based SQL/DML.
                final java.time.Instant lastUpdated = existingResourceDTO.getLastUpdated().toInstant();
                final SearchParametersTransportAdapter adapter = buildSearchParametersTransportAdapter(resourceTypeClass.getSimpleName(), rir.getLogicalId(), rir.getLogicalResourceId(), existingResourceDTO.getVersionId(), lastUpdated, null, searchParameters, rir.getParameterHash());
                accumulateSearchParameterValues(adapter.build());
            } else {
                log.fine(() -> "Skipping update of unchanged parameters for FHIR Resource '" + rir.getResourceType() + "/" + rir.getLogicalId() + "'");
            }

            // Use an OperationOutcome Issue to let the caller know that some work was performed
            final String diag = "Processed " + rir.getResourceType() + "/" + rir.getLogicalId();
            operationOutcomeResult.issue(Issue.builder().code(IssueType.INFORMATIONAL).severity(IssueSeverity.INFORMATION).diagnostics(org.linuxforhealth.fhir.model.type.String.of(diag)).build());
        } else {
            // Reasonable to assume that this resource was deleted because we can't read it
            final String diag = "Failed to read resource: " + rir.getResourceType() + "/" + rir.getLogicalId();
            operationOutcomeResult.issue(Issue.builder().code(IssueType.NOT_FOUND).severity(IssueSeverity.WARNING).diagnostics(string(diag)).build());
        }
    }

    @Override
    public String generateResourceId() {
        return logicalIdentityProvider.createNewIdentityValue();
    }

    /**
     * Each datasource involved in a transaction gets its own TransactionData impl object
     * which is used to hold parameter data to be inserted into the datbase just prior to
     * commit.
     * @param datasourceId
     * @return the ParameterTransactionDataImpl used to hold onto data to persist at the end of the current transaction
     */
    @SuppressWarnings("unchecked")
    private ParameterTransactionDataImpl getTransactionDataForDatasource(String datasourceId) {
        ParameterTransactionDataImpl result = null;

        // The trxSyncRegistry is transaction-scope, and may span multiple instances of this class. Only
        // add the TransactionData impl if it doesn't already exist. No synchronization necessary...this is
        // all in one thread
        if (this.trxSynchRegistry != null) {
            Object tdi = this.trxSynchRegistry.getResource(TXN_DATA_KEY);
            if (tdi == null) {
                this.transactionDataImpl = new TransactionDataImpl<ParameterTransactionDataImpl>(k -> createTransactionData(k));
                this.trxSynchRegistry.putResource(TXN_DATA_KEY, this.transactionDataImpl);
            } else if (tdi instanceof TransactionDataImpl<?>) {
                this.transactionDataImpl = (TransactionDataImpl<ParameterTransactionDataImpl>)tdi;
            } else {
                throw new IllegalStateException(TXN_DATA_KEY + " invalid class"); // basic coding error
            }

            // Now ask the TransactionDataImpl to return the ParameterTransactionDataImpl for the
            // current transaction, or create and stash a new instance if one doesn't yet exist
            result = transactionDataImpl.findOrCreate(datasourceId);
        }

        return result;
    }

    /**
     * Callback from TransactionData when a transaction has ended (after commit or rollback)
     * @param committed true if the transaction completed, or false if it rolled back
     */
    private void transactionCompleted(Boolean committed) {
        // Because of how this is called, committed should never be null
        // but we check just to be safe
        Objects.requireNonNull(committed, "committed must be non-null");

        try {
            if (committed) {
                // because the transaction has commited, we can publish any ids generated
                // during parameter storage
                paramValueCollector.publishValuesToCache();
    
                // See if we have any erase resources to clean up
                for (ErasedResourceRec err: this.eraseResourceRecs) {
                    try {
                        erasePayload(err);
                    } catch (Exception x) {
                        // The transaction has already committed, so we don't want to fail
                        // the request. This is a server-side issue now so all we can do is
                        // log.
                        log.log(Level.SEVERE, "failed to erase offload payload for '"
                                + err.toString()
                                + "'. Run reconciliation to ensure this record is removed.", x);
                    }
                }
            } else {
                // Try to delete each of the payload objects we've stored in this
                // transaction because the transaction has been rolled back
                if (payloadPersistenceResponses.size() > 0 && payloadPersistence == null) {
                    throw new IllegalStateException("payloadPersistenceResponses contains items but payloadPersistence is not configured");
                }

                if (payloadPersistenceResponses.size() > 0) {
                    log.fine("starting rollback handling for PayloadPersistenceResponse data");
                }
                for (PayloadPersistenceResponse ppr: payloadPersistenceResponses) {
                    try {
                        log.fine(() -> "tx rollback - deleting payload: " + ppr.toString());
                        payloadPersistence.deletePayload(ppr.getResourceTypeName(), ppr.getResourceTypeId(),
                                ppr.getLogicalId(), ppr.getVersionId(), ppr.getResourcePayloadKey());
                    } catch (Exception x) {
                        // Nothing more we can do other than log the issue. Any rows we can't process
                        // here (e.g. network outage) will be orphaned. These orphaned rows
                        // will be removed by the reconciliation process which scans the payload
                        // persistence repository and looks for missing RDBMS records.
                        log.log(Level.SEVERE, "rollback failed to delete payload: " + ppr.toString(), x);
                    }
                }
            }
        } finally {
            // important to clear this list after each transaction because batch bundles
            // use the same FHIRPersistenceJDBCImpl instance for each entry
            remoteIndexMessageList.clear();
            payloadPersistenceResponses.clear();
            eraseResourceRecs.clear();
            paramValueCollector.reset();
        }
    }

    /**
     * Factory function to create a new instance of the TransactionData implementation
     * used to store parameter data collected during this transaction
     * @param datasourceId
     * @return
     */
    private ParameterTransactionDataImpl createTransactionData(String datasourceId) {
        return new ParameterTransactionDataImpl(datasourceId, this, this.userTransaction);
    }

    /**
     * Build an {@link IParamValueProcessor} suitable for the current database and schema type.
     * @param connection the database connection
     * @return
     */
    private IParamValueProcessor makeParamValueProcessor(Connection connection) throws FHIRPersistenceException {
        final IParamValueProcessor result;
        final String schemaName = schemaNameSupplier.getSchemaForRequestContext(connection);
        final IParameterIdentityCache identityCache = new JDBCParameterCacheAdapter(this.cache);
        switch (this.connectionStrategy.getFlavor().getSchemaType()) {
        case PLAIN:
            if (this.connectionStrategy.getFlavor().getType() == DbType.DERBY) {
                result = new PlainDerbyParamValueProcessor(connection, schemaName, identityCache);
            } else {
                result = new PlainPostgresParamValueProcessor(connection, schemaName, identityCache);
            }
            break;
        case DISTRIBUTED:
            result = new DistributedPostgresParamValueProcessor(connection, schemaName, identityCache);
            break;
        default:
            throw new FHIRPersistenceException("Schema type not supported: " + connectionStrategy.getFlavor().getSchemaType().name());
        }
        return result;
    }

    @Override
    public void beforeCommit() throws FHIRPersistenceException {
        // callback made via the FHIRTestTransactionAdapter which is used for unit tests which do
        // not have access to the global UserTransaction stuff
        log.entering(CLASSNAME, "beforeCommit");
        try {
            flush();
        } finally {
            log.exiting(CLASSNAME, "beforeCommit");
        }
    }

    /**
     * Tell the persistence layer it should flush any data it has accumulated in
     * the current transaction. Can be called prior to a commit, but the underlying
     * implementation MUST flush before commit. Useful for unit tests which may want
     * to interrogate the database before the transaction ends
     * @throws FHIRPersistenceException
     */
    private void flush() throws FHIRPersistenceException {
        try (Connection connection = openConnection();
                MetricHandle mh = FHIRRequestContext.get().getMetricHandle(FHIRPersistenceJDBCMetric.M_JDBC_FLUSH_TX_DATA.name())) {
            IParamValueProcessor paramValueProcessor = makeParamValueProcessor(connection);
            try {
                // publish all the values collected in this transaction using the paramValueProcessor
                this.paramValueCollector.publish(paramValueProcessor);

                try (MetricHandle pushMetric = FHIRRequestContext.get().getMetricHandle(ParamMetrics.M_PARAM_PUSH_BATCH.name())) {
                    // tell the paramValueProcess to push everything to the database
                    paramValueProcessor.pushBatch();
                }
            } finally {
                paramValueProcessor.close();
            }
        } catch(FHIRPersistenceFKVException e) {
            log.log(Level.SEVERE, "FK violation", e);
            throw e;
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while processing token value records.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    /**
     * Called just prior to commit so that we can persist all the token value records
     * that have been accumulated during the transaction. This collection therefore
     * contains multiple resource types, which have to be processed separately.
     * @param records
     * @param referenceRecords
     * @param profileRecs
     * @param tagRecs
     * @param securityRecs
     * @throws FHIRPersistenceException
     */
    @Trace
    public void onCommit(Collection<ResourceTokenValueRec> records, Collection<ResourceReferenceValueRec> referenceRecords, Collection<ResourceProfileRec> profileRecs, Collection<ResourceTokenValueRec> tagRecs, Collection<ResourceTokenValueRec> securityRecs) throws FHIRPersistenceException {
        // new way...all param data is collected using the paramValueCollector instance
        flush();

        // At this stage we also need to check that any (async) payload offload operations related
        // to the current transaction have completed
        for (PayloadPersistenceResponse ppr: this.payloadPersistenceResponses) {
            try {
                log.fine(() -> "Getting storePayload() async result for: " + ppr.toString());
                PayloadPersistenceResult result = ppr.getResult().get();
                if (result.getStatus() == PayloadPersistenceResult.Status.FAILED) {
                    log.warning("Payload persistence returned unexpected value for: " + ppr.toString());
                    throw new FHIRPersistenceException("Payload persistence returned unexpected value");
                }
            } catch (InterruptedException e) {
                log.warning("Payload persistence was interrupted for: " + ppr.toString());
                throw new FHIRPersistenceException("Interrupted waiting for storePayload");
            } catch (ExecutionException e) {
                log.warning("Payload persistence failed for: " + ppr.toString());
                throw new FHIRPersistenceException("storePayload failed", e);

            }
        }

        // If we're using a remote index service, check that all the messages sent to the
        // remote service have been acknowledged
        for (IndexProviderResponse ipr: this.remoteIndexMessageList) {
            try {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Check remote index message ACK for: " + ipr.getData());
                }
                ipr.getAck(); // wait for the ACK
            } catch (InterruptedException x) {
                throw new FHIRPersistenceException("Interrupted waiting for remote index message ACK");
            } catch (ExecutionException x) {
                throw new FHIRPersistenceException("Failed to send remote index message", x);
            }
        }
        this.remoteIndexMessageList.clear();

        // NOTE: we do not clear the payloadPersistenceResponses list here on purpose. We want to keep
        // the list intact until the transaction actually commits, just in case there's a rollback, in which
        // case the rollback handling will attempt to call delete for all of the records in the list.
    }

    @Override
    public ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType, java.time.Instant fromLastModified,
        java.time.Instant toLastModified, Function<ResourcePayload, Boolean> processor) throws FHIRPersistenceException {
        try (Connection connection = openConnection()) {
            doCachePrefill(null, connection);
            // translator is required to handle some simple SQL syntax differences. This is easier
            // than creating separate DAO implementations for each database type
            IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());
            FetchResourcePayloadsDAO dao = new FetchResourcePayloadsDAO(translator, schemaNameSupplier.getSchemaForRequestContext(connection), resourceType.getSimpleName(), fromLastModified, toLastModified, processor);

            if (log.isLoggable(Level.FINEST)) {
                int count = dao.count(connection);
                log.finest("resource count for range: " + count);
            }
            return dao.run(connection);
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while processing token value records.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    @Override
    public List<ResourceChangeLogRecord> changes(FHIRPersistenceContext context, int resourceCount, java.time.Instant sinceLastModified, java.time.Instant beforeLastModified,
            Long changeIdMarker, List<String> resourceTypeNames, boolean excludeTransactionTimeoutWindow, HistorySortOrder historySortOrder)
            throws FHIRPersistenceException {
        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);
            // translator is required to handle some simple SQL syntax differences. This is easier
            // than creating separate DAO implementations for each database type
            final List<Integer> resourceTypeIds;
            if (resourceTypeNames != null && resourceTypeNames.size() > 0) {
                // convert the list of type names to the corresponding list of resourceTypeId values
                // the REST layer already has checked these names are valid, so no need to worry about failures
                resourceTypeIds = resourceTypeNames.stream()
                        .map(n -> cache.getResourceTypeCache().getId(n))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (resourceTypeIds.size() != resourceTypeNames.size()) {
                    throw new FHIRPersistenceException("Unexpected error converting resource type name(s) to id(s); " + resourceTypeNames);
                }
            } else {
                resourceTypeIds = null; // no filter on resource type
            }
            IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());
            FetchResourceChangesDAO dao = new FetchResourceChangesDAO(translator, schemaNameSupplier.getSchemaForRequestContext(connection),
                    resourceCount, sinceLastModified, beforeLastModified, changeIdMarker, resourceTypeIds, excludeTransactionTimeoutWindow,
                    historySortOrder);
            return dao.run(connection);
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while processing token value records.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
    }

    @Override
    public ResourceEraseRecord erase(FHIRPersistenceContext context, EraseDTO eraseDto) throws FHIRPersistenceException {
        final String METHODNAME = "erase";
        log.entering(CLASSNAME, METHODNAME);

        ResourceEraseRecord eraseRecord = new ResourceEraseRecord();
        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);
            IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());
            EraseResourceDAO eraseDao = new EraseResourceDAO(connection, FhirSchemaConstants.FHIR_ADMIN, translator,
                    schemaNameSupplier.getSchemaForRequestContext(connection),
                    connectionStrategy.getFlavor(), this.cache);
            long eraseResourceGroupId = eraseDao.erase(eraseRecord, eraseDto);

            // If offloading is enabled, we need to remove the corresponding offloaded resource payloads
            if (isOffloadingSupported()) {
                erasePayloads(eraseDao, eraseResourceGroupId);
            } else {
                // clean up the erased_resources records because they're no longer needed
                eraseDao.clearErasedResourcesInGroup(eraseResourceGroupId);
            }

        } catch(FHIRPersistenceException e) {
            // Other Peristence exceptions are implied, such as FHIRPersistenceFKVException.
            getTransaction().setRollbackOnly();
            throw e;
        } catch (DataAccessException dax) {
            getTransaction().setRollbackOnly();

            // It's possible this is a deadlock exception, in which case it could be considered retryable
            if (dax.isTransactionRetryable()) {
                log.log(Level.WARNING, "retryable error", dax);
                FHIRPersistenceDataAccessException fpx = new FHIRPersistenceDataAccessException("Data access error while performing a erase operation.");
                fpx.setTransactionRetryable(true);
                throw fpx;
            } else {
                log.log(Level.SEVERE, "non-retryable error", dax);
                throw new FHIRPersistenceDataAccessException("Data access error while performing a erase operation.");
            }
        } catch(Throwable e) {
            getTransaction().setRollbackOnly();
            // don't chain the exception to avoid leaking secrets
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a erase operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }

        return eraseRecord;
    }

    /**
     * Delete all the offloaded payload entries which have been identified for deletion
     * @param dao
     * @param erasedResourceGroupId
     */
    private void erasePayloads(EraseResourceDAO dao, long erasedResourceGroupId) throws FHIRPersistenceException {
        List<ErasedResourceRec> recs = dao.getErasedResourceRecords(erasedResourceGroupId);

        // Stash this list so that we can do the erase after the transaction commits
        eraseResourceRecs.addAll(recs);

        // If the above loop completed without throwing an exception, we can safely
        // remove all the records in the group. If an exception was thrown (because
        // the offload persistence layer was not accessible), don't delete right now
        // just in case we want the tx to commit anyway, allowing for async cleanup
        // by the reconciliation process
        dao.clearErasedResourcesInGroup(erasedResourceGroupId);
    }

    /**
     * Erase the payload for the resource described by rec
     * @param rec
     */
    private void erasePayload(ErasedResourceRec rec) throws FHIRPersistenceException {
        String resourceType = cache.getResourceTypeNameCache().getName(rec.getResourceTypeId());
        if (resourceType == null) {
            throw new FHIRPersistenceException("Resource type not found in cache for resourceTypeId=" + rec.getResourceTypeId());
        }

        // Note that if versionId is null, it means delete all known versions
        // The resourcePayloadKey is always null here, because the intention
        // for erase is to delete all instances of the record (in the rare case
        // there may be orphaned records from failed transactions)
        payloadPersistence.deletePayload(resourceType, rec.getResourceTypeId(), rec.getLogicalId(), rec.getVersionId(), null);
    }

    private boolean allSearchParmsAreGlobal(List<QueryParameter> queryParms) {
        for (QueryParameter queryParm : queryParms) {
            if (!SearchConstants.SYSTEM_LEVEL_GLOBAL_PARAMETER_NAMES.contains(queryParm.getCode())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Long> retrieveIndex(FHIRPersistenceContext context, int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException {
        final String METHODNAME = "retrieveIndex";
        log.entering(CLASSNAME, METHODNAME);

        try (Connection connection = openConnection()) {
            doCachePrefill(context, connection);
            IDatabaseTranslator translator = FHIRResourceDAOFactory.getTranslatorForFlavor(connectionStrategy.getFlavor());
            RetrieveIndexDAO dao = new RetrieveIndexDAO(translator, schemaNameSupplier.getSchemaForRequestContext(connection), resourceTypeName, count, notModifiedAfter, afterIndexId, this.cache);
            return dao.run(connection);
        } catch(FHIRPersistenceException e) {
            throw e;
        } catch(Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while retrieving logical resource IDs.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    @Override
    public boolean isUpdateCreateEnabled() {
        return this.updateCreateEnabled;
    }

    @Override
    public PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber, String resourcePayloadKey) throws FHIRPersistenceException {
        if (isOffloadingSupported()) {
            doCachePrefill(); // just in case we're called before any other database interaction (can happen)
            final String resourceTypeName = resource.getClass().getSimpleName();
            int resourceTypeId = getResourceTypeId(resourceTypeName);

            // Delegate the serialization and any compression to the FHIRPayloadPersistence implementation
            PayloadPersistenceResponse response = payloadPersistence.storePayload(resourceTypeName, resourceTypeId, logicalId, newVersionNumber, resourcePayloadKey, resource);

            // We don't record the response in the payloadPersistenceResponses list yet because
            // for batch bundles, there are multiple transactions and the list should contain
            // only those responses relevant to the current transaction
            return response;
        } else {
            // Offloading not supported by the plain JDBC persistence implementation, so return null
            return null;
        }
    }

    /**
     * Get the resource payload key value from the given context if offloading
     * is supported and configured. Returns null otherwise.
     * @param context
     * @return
     */
    private String getResourcePayloadKeyFromContext(FHIRPersistenceContext context) {
        return context.getOffloadResponse() == null ? null : context.getOffloadResponse().getResourcePayloadKey();
    }

    @Override
    public List<Resource> readResourcesForRecords(List<ResourceChangeLogRecord> records) throws FHIRPersistenceException {
        // TODO support async read from payloadPersistence after issue #2900 is merged.

        // Make sure we read deleted resources...this is important because the result list must
        // line up row-for-row with the provided records list
        FHIRPersistenceContext readContext = FHIRPersistenceContextFactory.createPersistenceContext(null);
        List<Resource> result = new ArrayList<>(records.size());
        for (ResourceChangeLogRecord r: records) {
            Class<? extends Resource> resourceType = ModelSupport.getResourceType(r.getResourceTypeName());
            Resource resource = readResourceForRecord(readContext, r, resourceType);

            // We add the resource even if it's null because we want to keep the
            // list in alignment with the records list. A null might be returned
            // if the resource has been erased (hard delete).
            result.add(resource);
        }
        return result;
    }

    /**
     * Read the resource version for the given ResourceChangeLogRecord
     * @param <T>
     * @param record
     * @param resourceType
     * @return
     * @throws FHIRPersistenceException
     */
    private <T extends Resource> T readResourceForRecord(FHIRPersistenceContext context, ResourceChangeLogRecord record, Class<T> resourceType) throws FHIRPersistenceException {
        SingleResourceResult<T> result = vread(context, resourceType, record.getLogicalId(), Integer.toString(record.getVersionId()));
        return result.getResource();
    }
}
