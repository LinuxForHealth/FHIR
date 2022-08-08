/**
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout;

import static org.linuxforhealth.fhir.config.FHIRConfiguration.PROPERTY_UPDATE_CREATE_ENABLED;
import static org.linuxforhealth.fhir.model.type.String.string;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import javax.transaction.TransactionSynchronizationRegistry;

import com.datastax.oss.driver.api.core.CqlSession;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.core.FHIRUtilities;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.resource.SearchParameter.Component;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.model.visitor.Visitable;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathSystemValue;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceTransaction;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.MultiResourceResult;
import org.linuxforhealth.fhir.persistence.ResourceChangeLogRecord;
import org.linuxforhealth.fhir.persistence.ResourcePayload;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import org.linuxforhealth.fhir.persistence.payload.PayloadPersistenceResponse;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.ParameterBlock;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.StrValue;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.StrValueList;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.TokenValue;
import org.linuxforhealth.fhir.persistence.scout.SearchParameters.TokenValueList;
import org.linuxforhealth.fhir.persistence.scout.cql.DatasourceSessions;
import org.linuxforhealth.fhir.search.date.DateTimeHandler;
import org.linuxforhealth.fhir.search.util.SearchUtil;

/**
 * Scalable persistence layer, storing resources in Cassandra and the corresponding
 * indexes in Redis.
 */
public class FHIRPersistenceScoutImpl implements FHIRPersistence {
    private static final Logger logger = Logger.getLogger(FHIRPersistenceScoutImpl.class.getName());
    private static final String CLASSNAME = FHIRPersistenceScoutImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public static final String TRX_SYNCH_REG_JNDI_NAME = "java:comp/TransactionSynchronizationRegistry";

    // TODO. Shouldn't be necessary
    private static final int MAX_NUM_OF_COMPOSITE_COMPONENTS = 3;

    private TransactionSynchronizationRegistry trxSynchRegistry;

    private boolean updateCreateEnabled;

    private List<OperationOutcome.Issue> supplementalIssues = new ArrayList<>();


    /**
     * Constructor for use when running as web application in WLP.
     * @throws Exception
     */
    public FHIRPersistenceScoutImpl() throws Exception {
        super();
        final String METHODNAME = "FHIRPersistenceCloudantImpl()";
        log.entering(CLASSNAME, METHODNAME);

        PropertyGroup fhirConfig = FHIRConfiguration.getInstance().loadConfiguration();
        this.updateCreateEnabled = fhirConfig.getBooleanProperty(PROPERTY_UPDATE_CREATE_ENABLED, Boolean.TRUE);
        log.exiting(CLASSNAME, METHODNAME);
    }

    /**
     * Constructor for use when running standalone, outside of any web container.
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public FHIRPersistenceScoutImpl(Properties configProps) throws Exception {
        final String METHODNAME = "FHIRPersistenceCloudantImpl(Properties)";
        log.entering(CLASSNAME, METHODNAME);

        this.updateCreateEnabled = Boolean.parseBoolean(configProps.getProperty("updateCreateEnabled"));

        log.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public boolean isDeleteSupported() {
        return true;
    }

    /**
     * Obtain a session connected to Cassandra for the current tenant/datastore
     * @return
     */
    protected CqlSession getCqlSession() {
        return  DatasourceSessions.getSessionForTenantDatasource();
    }


    @Override
    public <T extends Resource> SingleResourceResult<T> create(FHIRPersistenceContext context, T resource) throws FHIRPersistenceException {
        final String METHODNAME = "create";
        log.entering(CLASSNAME, METHODNAME);

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            String logicalId;

            // We need to update the meta in the resource, so we need a modifiable version
            Resource.Builder resultResourceBuilder = resource.toBuilder();

            // This create() operation is only called by a REST create. If the given resource
            // contains an id, then for R4 we need to ignore it and replace it with our
            // system-generated value. For the update-or-create scenario, see update().
            // Default version is 1 for a brand new FHIR Resource.
            int newVersionNumber = 1;
            logicalId = UUID.randomUUID().toString();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Creating new FHIR Resource of type '" + resource.getClass().getSimpleName() + "'");
            }

            // Set the resource id and meta fields.
            Instant lastUpdated = Instant.now(ZoneOffset.UTC);
            resultResourceBuilder.id(logicalId);
            Meta meta = resource.getMeta();
            Meta.Builder metaBuilder = meta == null ? Meta.builder() : meta.toBuilder();
            metaBuilder.versionId(Id.of(Integer.toString(newVersionNumber)));
            metaBuilder.lastUpdated(lastUpdated);
            resultResourceBuilder.meta(metaBuilder.build());

            // rebuild the resource with updated meta
            @SuppressWarnings("unchecked")
            T updatedResource = (T) resultResourceBuilder.build();

            // Create the parameter block we will populate with all the parameters
            // extracted from the resource. This parameter block gets serialized
            // and pushed into Redis
            Timestamp timestamp = FHIRUtilities.convertToTimestamp(lastUpdated.getValue());
            ParameterBlock.Builder pb = ParameterBlock.newBuilder();

            pb.setLogicalId(logicalId);
            pb.setVersionId(newVersionNumber);
            pb.setLastUpdated(timestamp.toInstant().toEpochMilli());
            pb.setResourceType(updatedResource.getClass().getSimpleName());

            // Extract parameters into the ParameterBlock


            // Serialize and compress the Resource
            GZIPOutputStream zipStream = new GZIPOutputStream(stream);
            FHIRGenerator.generator( Format.JSON, false).generate(updatedResource, zipStream);
            zipStream.finish();
            byte[] payload = stream.toByteArray();
            zipStream.close();

            // Save the data
            List<Issue> supplementalIssues = new ArrayList<>();
            persist(pb.build(), payload);

            SingleResourceResult.Builder<T> resultBuilder = new SingleResourceResult.Builder<T>()
                    .success(true)
                    .resource(updatedResource);

            // Add supplemental issues to the OperationOutcome
            if (!supplementalIssues.isEmpty()) {
                resultBuilder.outcome(OperationOutcome.builder()
                    .issue(supplementalIssues)
                    .build());
            }

            return resultBuilder.build();
        } catch (Throwable e) {
            FHIRPersistenceException fx = new FHIRPersistenceException("Unexpected error while performing a create operation.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;
        }
        finally {
           log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Save the resource
     * @param pb
     * @param payload
     */
    protected void persist(ParameterBlock pb, byte[] payload) {
        try (CqlSession session = getCqlSession()) {

        }
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> read(FHIRPersistenceContext context, Class<T> resourceType, String logicalId)
        throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> vread(FHIRPersistenceContext context, Class<T> resourceType, String logicalId, String versionId)
        throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> update(FHIRPersistenceContext context, String logicalId, T resource) throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isTransactional() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public OperationOutcome getHealth() throws FHIRPersistenceException {

        StrValue.Builder builder = StrValue.newBuilder();
        builder.setStrValue("hello");

        StrValueList.Builder slBuilder = StrValueList.newBuilder();
        slBuilder.addStringValues(builder.build());

        SearchParameters.ParameterBlock.Builder pb = SearchParameters.ParameterBlock.newBuilder();

        pb.putStringValues("strParam", slBuilder.build());

        // Let's try creating a token
        TokenValue.Builder tokenBuilder = TokenValue.newBuilder();
        tokenBuilder.setCodeSystem("system");
        tokenBuilder.setTokenValue("token1");
        TokenValueList.Builder tvListBuilder = TokenValueList.newBuilder();
        tvListBuilder.addTokenValues(tokenBuilder.build());

        pb.putTokenValues("token1", tvListBuilder.build());

        // TODO Check that we can connect to Redis and Cassandra
        return buildOKOperationOutcome();
        // return buildErrorOperationOutcome();
    }

    private OperationOutcome buildOKOperationOutcome() {
        return FHIRUtil.buildOperationOutcome("All OK", IssueType.INFORMATIONAL, IssueSeverity.INFORMATION);
    }

    private OperationOutcome buildErrorOperationOutcome() {
        return FHIRUtil.buildOperationOutcome("The database connection was not valid", IssueType.NO_STORE, IssueSeverity.ERROR);
    }


    @Override
    public FHIRPersistenceTransaction getTransaction() {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Extracts search parameters for the passed FHIR Resource.
     * @param fhirResource - Some FHIR Resource
     * @param resourceDTO - A Resource DTO representation of the passed FHIR Resource.
     * @throws Exception
     */
    private void extractSearchParameters(ParameterBlockBuilderHelper parameters, Resource fhirResource)
                 throws Exception {
        final String METHODNAME = "extractSearchParameters";
        log.entering(CLASSNAME, METHODNAME);

        String code;
        String type;
        String expression;

        try {
            Map<SearchParameter, List<FHIRPathNode>> map = SearchUtil.extractParameterValues(fhirResource);

            for (Entry<SearchParameter, List<FHIRPathNode>> entry : map.entrySet()) {
                SearchParameter sp = entry.getKey();
                code = sp.getCode().getValue();
                type = sp.getType().getValue();
                expression = sp.getExpression().getValue();

                if (log.isLoggable(Level.FINE)) {
                    log.fine("Processing SearchParameter code: " + code + ", type: " + type + ", expression: " + expression);
                }

                List<FHIRPathNode> values = entry.getValue();

                if (SearchParamType.COMPOSITE.equals(sp.getType())) {
                    List<Component> components = sp.getComponent();
                    if (components.size() > MAX_NUM_OF_COMPOSITE_COMPONENTS) {
                        throw new UnsupportedOperationException(String.format("Found %d components for search parameter '%s', "
                                + "but this persistence layer can only support composites of %d or fewer components",
                                components.size(), code, MAX_NUM_OF_COMPOSITE_COMPONENTS));
                    }
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
                    }
                } else { // ! SearchParamType.COMPOSITE.equals(sp.getType())
                    ParameterExtractionVisitor parameterBuilder = new ParameterExtractionVisitor(parameters, sp);

                    for (FHIRPathNode value : values) {

                        try {
                            if (value.isElementNode()) {
                                // parameterBuilder aggregates the results for later retrieval
                                value.asElementNode().element().accept(parameterBuilder);
                            } else if (value.isSystemValue()) {
                                processPrimitiveValue(parameters, value.name(), value.asSystemValue(), code, fhirResource.getClass().getSimpleName());
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
                }
            }
        } finally {
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

    /**
     * Create a Parameter DTO from the primitive value.
     * Note: this method only sets the value;
     * caller is responsible for setting all other fields on the created Parameter.
        builder, value.asSystemValue(), code, fhirResource.getClass().getSimpleName());
     */
    private void processPrimitiveValue(ParameterBlockBuilderHelper parameters, String name, FHIRPathSystemValue systemValue, String code, String resourceType) {

        // For now we have to add the parameter to the helper
        if (systemValue.isBooleanValue()) {
            final String system = "http://terminology.hl7.org/CodeSystem/special-values";
            if (systemValue.asBooleanValue()._boolean()) {
                parameters.addTokenParam(name, "true", system);
            } else {
                parameters.addTokenParam(name, "false", system);
            }
        } else if (systemValue.isTemporalValue()) {

            TemporalAccessor v = systemValue.asTemporalValue().temporal();
            java.time.Instant inst = DateTimeHandler.generateValue(v);
            long t = DateTimeHandler.generateTimestamp(inst).getTime();
            parameters.addDateParam(name, t, t);

        } else if (systemValue.isStringValue()) {
            parameters.addStrParam(name, systemValue.asStringValue().string());
        } else if (systemValue.isNumberValue()) {
            parameters.addNumberParam(name, systemValue.asNumberValue().decimal());
        } else if (systemValue.isQuantityValue()) {
            parameters.addQuantityParam(name, systemValue.asQuantityValue().value(), null, null, systemValue.asQuantityValue().unit(), "http://unitsofmeasure.org");
        }
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
    public String generateResourceId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public int reindex(FHIRPersistenceContext context, OperationOutcome.Builder oob, java.time.Instant tstamp, List<Long> indexIds,
        String resourceLogicalId) throws FHIRPersistenceException {
        return 0;
    }

    @Override
    public ResourcePayload fetchResourcePayloads(Class<? extends Resource> resourceType, java.time.Instant fromLastModified,
        java.time.Instant toLastModified, Function<ResourcePayload, Boolean> process) throws FHIRPersistenceException {

        throw new FHIRPersistenceNotSupportedException("API not supported at this time");
    }

    @Override
    public List<ResourceChangeLogRecord> changes(int resourceCount, java.time.Instant fromLastModified, Long afterResourceId, String resourceTypeName)
        throws FHIRPersistenceException {
        throw new FHIRPersistenceNotSupportedException("API not supported at this time");
    }

    @Override
    public List<Long> retrieveIndex(int count, java.time.Instant notModifiedAfter, Long afterIndexId, String resourceTypeName) throws FHIRPersistenceException {
        throw new FHIRPersistenceNotSupportedException("API not supported at this time");
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> createWithMeta(FHIRPersistenceContext context, T resource)
            throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Resource> SingleResourceResult<T> updateWithMeta(FHIRPersistenceContext context, T resource)
            throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Resource> readResourcesForRecords(List<ResourceChangeLogRecord> records)
            throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ResourceChangeLogRecord> changes(int resourceCount, java.time.Instant sinceLastModified,
            java.time.Instant beforeLastModified, Long changeIdMarker, List<String> resourceTypeNames,
            boolean excludeTransactionTimeoutWindow, HistorySortOrder historySortOrder)
            throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PayloadPersistenceResponse storePayload(Resource resource, String logicalId, int newVersionNumber,
            String resourcePayloadKey) throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultiResourceResult history(FHIRPersistenceContext context, Class<? extends Resource> resourceType,
            String logicalId) throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultiResourceResult search(FHIRPersistenceContext context, Class<? extends Resource> resourceType)
            throws FHIRPersistenceException {
        // TODO Auto-generated method stub
        return null;
    }
}