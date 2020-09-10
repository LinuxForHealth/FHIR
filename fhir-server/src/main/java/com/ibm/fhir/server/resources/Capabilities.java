/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_CAPABILITY_STATEMENT_CACHE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_AUTHURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_REGURL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_OAUTH_TOKENURL;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Operation;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.ConditionalDeleteStatus;
import com.ibm.fhir.model.type.code.ConditionalReadStatus;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.RestfulCapabilityMode;
import com.ibm.fhir.model.type.code.SystemRestfulInteraction;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.FHIRBuildIdentifier;
import com.ibm.fhir.server.operation.FHIROperationRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperation;
import com.ibm.fhir.server.util.RestAuditLogger;

@Path("/")
@Consumes({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON, FHIRMediaType.APPLICATION_FHIR_XML,
        MediaType.APPLICATION_XML })
@Produces({ FHIRMediaType.APPLICATION_FHIR_JSON, MediaType.APPLICATION_JSON, FHIRMediaType.APPLICATION_FHIR_XML,
        MediaType.APPLICATION_XML })
public class Capabilities extends FHIRResource {
    private static final Logger log = java.util.logging.Logger.getLogger(Capabilities.class.getName());

    // Constants
    private static final String FHIR_SERVER_NAME = "IBM FHIR Server";
    private static final String FHIR_COPYRIGHT = "(C) Copyright IBM Corporation 2016, 2020";
    private static final String EXTENSION_URL = "http://ibm.com/fhir/extension";

    // Error Messages
    private static final String ERROR_MSG = "Caught exception while processing 'metadata' request.";
    private static final String ERROR_CONSTRUCTING = "An error occurred while constructing the Conformance statement.";

    // Capability Statement Cache per Tenant
    private static ConcurrentHashMap<String, CapabilityStatement> CAPABILITY_STATEMENT_CACHE_PER_TENANT =
            new ConcurrentHashMap<>();

    // Constructor
    public Capabilities() throws Exception {
        super();
    }

    @GET
    @Path("metadata")
    public Response capabilities() {
        log.entering(this.getClass().getName(), "capabilities()");
        try {
            Date startTime = new Date();
            checkInitComplete();

            FHIRRequestContext ctx = FHIRRequestContext.get();
            String tenantId = ctx.getTenantId();

            // Defaults to 60 minutes (or what's in the fhirConfig)
            int cacheLength = fhirConfig.getIntProperty(PROPERTY_CAPABILITY_STATEMENT_CACHE, 60);

            CapabilityStatement capabilityStatement = CAPABILITY_STATEMENT_CACHE_PER_TENANT.compute(tenantId, (k,v) -> getOrCreateCapabilityStatement(v, cacheLength));
            RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), Response.Status.OK);

            CacheControl cacheControl = new CacheControl();
            cacheControl.setPrivate(true);
            cacheControl.setMaxAge(60 * cacheLength);
            return Response.ok().entity(capabilityStatement).cacheControl(cacheControl).build();
        } catch (IllegalArgumentException e) {
            FHIROperationException foe = buildRestException(ERROR_CONSTRUCTING, IssueType.EXCEPTION);
            log.log(Level.SEVERE, ERROR_MSG, foe);
            return exceptionResponse(e, issueListToStatus(foe.getIssues()));
        } catch (Exception e) {
            log.log(Level.SEVERE, ERROR_MSG, e);
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "capabilities()");
        }
    }

    /*
     * get or create capability statement
     */
    private CapabilityStatement getOrCreateCapabilityStatement(CapabilityStatement statement, int cacheLength) {
        try {
            if (statement == null) {
                statement = buildCapabilityStatement();
            } else {
                TemporalAccessor acc = statement.getDate().getValue();
                ZonedDateTime cachedTime = ZonedDateTime.from(acc);

                // If UTC now is after the time the statement was generated
                // plus a cacheLength then, rebuild the statement.
                if (ZonedDateTime.now(ZoneOffset.UTC).isAfter(cachedTime.plusMinutes(cacheLength))) {
                    statement = buildCapabilityStatement();
                }
            }
            return statement;
        } catch (Throwable t) {
            // We pack it in an IllegalArgument so it's used cleanly in compute.
            throw new IllegalArgumentException(t);
        }
    }

    /**
     * Builds a CapabilityStatement resource instance which describes this server.
     *
     * @throws Exception
     */
    private CapabilityStatement buildCapabilityStatement() throws Exception {
        // Build the list of interactions that are supported for each resource type.
        List<Rest.Resource.Interaction> interactions = new ArrayList<>();
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.CREATE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.UPDATE));
        if (isDeleteSupported()) {
            interactions.add(buildInteractionStatement(TypeRestfulInteraction.DELETE));
        }
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.READ));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.VREAD));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.HISTORY_INSTANCE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.SEARCH_TYPE));
        interactions.add(buildInteractionStatement(TypeRestfulInteraction.PATCH));


        // Build the lists of operations that are supported
        List<OperationDefinition> systemOps = new ArrayList<>();
        Map<ResourceType.ValueSet, List<OperationDefinition>> typeOps = new HashMap<>();

        FHIROperationRegistry opRegistry = FHIROperationRegistry.getInstance();
        List<String> operationNames = opRegistry.getOperationNames();
        for (String opName : operationNames) {
            FHIROperation operation = opRegistry.getOperation(opName);
            OperationDefinition opDef = operation.getDefinition();
            if (opDef.getSystem().getValue()) {
                systemOps.add(opDef);
            }
            for (ResourceType resourceType : opDef.getResource()) {
                ResourceType.ValueSet typeValue = resourceType.getValueAsEnumConstant();
                if (typeOps.containsKey(typeValue)) {
                    typeOps.get(typeValue).add(opDef);
                } else {
                    List<OperationDefinition> typeOpList = new ArrayList<>();
                    typeOpList.add(opDef);
                    typeOps.put(typeValue, typeOpList);
                }
            }
        }

        com.ibm.fhir.model.type.Boolean isUpdateCreate = com.ibm.fhir.model.type.Boolean.of(isUpdateCreateEnabled());
        // Build the list of supported resources.
        List<Rest.Resource> resources = new ArrayList<>();
        ResourceType.ValueSet[] resourceTypes = ResourceType.ValueSet.values();
        for (ResourceType.ValueSet resourceType : resourceTypes) {
            String resourceTypeName = resourceType.value();
            // Build the set of ConformanceSearchParams for this resource type.
            List<Rest.Resource.SearchParam> conformanceSearchParams = new ArrayList<>();
            List<SearchParameter> searchParameters = SearchUtil.getApplicableSearchParameters(resourceTypeName);
            if (searchParameters != null) {
                for (SearchParameter searchParameter : searchParameters) {
                    Rest.Resource.SearchParam.Builder conformanceSearchParamBuilder =
                            Rest.Resource.SearchParam.builder()
                                .name(searchParameter.getCode())
                                .type(searchParameter.getType());
                    if (searchParameter.getDescription() != null) {
                        conformanceSearchParamBuilder.documentation(searchParameter.getDescription());
                    }

                    Rest.Resource.SearchParam conformanceSearchParam =
                            conformanceSearchParamBuilder.build();
                    conformanceSearchParams.add(conformanceSearchParam);
                }
            }

            List<Operation> ops = mapOperationDefinitionsToRestOperations(typeOps.get(resourceType));
            // If the type is an abstract resource ("Resource" or "DomainResource")
            // then the operation can be invoked on any concrete specialization.
            ops.addAll(mapOperationDefinitionsToRestOperations(typeOps.get(ResourceType.ValueSet.RESOURCE)));
            if (DomainResource.class.isAssignableFrom(ModelSupport.getResourceType(resourceType.value()))) {
                ops.addAll(mapOperationDefinitionsToRestOperations(typeOps.get(ResourceType.ValueSet.DOMAIN_RESOURCE)));
            }

            // Build the ConformanceResource for this resource type.
            Rest.Resource cr = Rest.Resource.builder()
                    .type(ResourceType.of(resourceType))
                    .profile(Canonical.of("http://hl7.org/fhir/profiles/" + resourceTypeName))
                    .interaction(interactions)
                    .operation(ops)
                    .conditionalCreate(com.ibm.fhir.model.type.Boolean.TRUE)
                    .conditionalUpdate(com.ibm.fhir.model.type.Boolean.TRUE)
                    .updateCreate(isUpdateCreate)
                    .conditionalDelete(ConditionalDeleteStatus.MULTIPLE)
                    .conditionalRead(ConditionalReadStatus.FULL_SUPPORT)
                    .searchParam(conformanceSearchParams)
                    .build();

            resources.add(cr);
        }

        // Determine if transactions are supported for this FHIR Server configuration.
        SystemRestfulInteraction transactionMode = SystemRestfulInteraction.BATCH;
        try {
            boolean txnSupported = getPersistenceImpl().isTransactional();
            transactionMode = (txnSupported ? SystemRestfulInteraction.TRANSACTION
                    : SystemRestfulInteraction.BATCH);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Unexpected error while reading server transaction mode setting", t);
        }

        String actualHost = new URI(getRequestUri()).getHost();

        String regURLTemplate = null;
        String authURLTemplate = null;
        String tokenURLTemplate = null;
        try {
            regURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_REGURL, "");
            authURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_AUTHURL, "");
            tokenURLTemplate = fhirConfig.getStringProperty(PROPERTY_OAUTH_TOKENURL, "");
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding OAuth URLs to the conformance statement", e);
        }
        String tokenURL = tokenURLTemplate.replaceAll("<host>", actualHost);

        String authURL = authURLTemplate.replaceAll("<host>", actualHost);

        String regURL = regURLTemplate.replaceAll("<host>", actualHost);

        CapabilityStatement.Rest.Security restSecurity = CapabilityStatement.Rest.Security.builder()
                .service(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .code(Code.of("SMART-on-FHIR"))
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service"))
                        .build())
                    .text(string("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)"))
                    .build())
                .extension(Extension.builder()
                    .url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris")
                    .extension(
                        Extension.builder().url("token").value(Uri.of(tokenURL)).build(),
                        Extension.builder().url("authorize").value(Uri.of(authURL)).build(),
                        Extension.builder().url("register").value(Uri.of(regURL)).build())
                    .build())
                .build();

        CapabilityStatement.Rest rest = CapabilityStatement.Rest.builder()
                .mode(RestfulCapabilityMode.SERVER)
                .security(restSecurity)
                .resource(addSupportedProfilesToResources(resources))
                .interaction(CapabilityStatement.Rest.Interaction.builder()
                    .code(transactionMode)
                    .build())
                .operation(mapOperationDefinitionsToRestOperations(systemOps))
                .build();

        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion()
                + " build id " + buildInfo.getBuildId() + "";

        List<Code> format = new ArrayList<Code>();
        format.add(Code.of(Format.JSON.toString().toLowerCase()));
        format.add(Code.of(Format.XML.toString().toLowerCase()));
        format.add(Code.of(FHIRMediaType.APPLICATION_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_XML));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_XML));

        // Finally, create the CapabilityStatement resource itself.
        CapabilityStatement conformance = CapabilityStatement.builder()
                .status(PublicationStatus.ACTIVE)
                .date(DateTime.now(ZoneOffset.UTC))
                .kind(CapabilityStatementKind.CAPABILITY)
                .fhirVersion(FHIRVersion.VERSION_4_0_1)
                .format(format)
                .patchFormat(Code.of(FHIRMediaType.APPLICATION_JSON_PATCH),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_JSON),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_XML))
                .version(string(buildInfo.getBuildVersion()))
                .name(string(FHIR_SERVER_NAME))
                .description(Markdown.of(buildDescription))
                .copyright(Markdown.of(FHIR_COPYRIGHT))
                .publisher(string("IBM Corporation"))
                .software(CapabilityStatement.Software.builder()
                          .name(string(FHIR_SERVER_NAME))
                          .version(string(buildInfo.getBuildVersion()))
                          .id(buildInfo.getBuildId())
                          .build())
                .rest(rest)
                .instantiates(Canonical.of("http://www.hl7.org/fhir/bulk-data/CapabilityStatement-bulk-data.html"))
                .build();

        try {
            conformance = addExtensionElements(conformance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding extension elements to the conformance statement", e);
        }

        return conformance;
    }

    private List<Rest.Resource> addSupportedProfilesToResources(List<Rest.Resource> resources){
        Map<String,Set<Canonical>> resourceProfiles = FHIRRegistry.getInstance().getProfiles();
        return resources.stream().map(r -> processResource(r,resourceProfiles)).collect(Collectors.toList());
    }

    private Rest.Resource processResource(Rest.Resource resource, Map<String,Set<Canonical>> resourceProfiles){
        Set<Canonical> supportedProfiles = resourceProfiles.get(resource.getType().getValue());
        if(supportedProfiles != null) {
            return resource.toBuilder().supportedProfile(new ArrayList<>(supportedProfiles)).build();
        } else {
            return resource;
        }
    }

    private List<Rest.Resource.Operation> mapOperationDefinitionsToRestOperations(List<OperationDefinition> opDefs) {
        if (opDefs == null) {
            return new ArrayList<>();
        }

        List<Rest.Resource.Operation> ops = new ArrayList<>();

        for (OperationDefinition opDef : opDefs) {
            if (opDef.getUrl() == null || !opDef.getUrl().hasValue()) {
                // The FHIROperationRegistry requires OperationDefinitions to have a url, so we shouldn't ever get here
                throw new IllegalStateException("Operation " + opDef.getCode().getValue() + " has no url");
            }

            ops.add(Rest.Resource.Operation.builder()
                    .name(opDef.getCode())
                    .definition(Canonical.of(opDef.getUrl().getValue(), opDef.getVersion() == null ? null : opDef.getVersion().getValue()))
                    .documentation(opDef.getDescription())
                    .build());
        }

        return ops;
    }

    private CapabilityStatement addExtensionElements(CapabilityStatement capabilityStatement)
        throws Exception {
        List<Extension> extentions = new ArrayList<Extension>();
        Extension extension = Extension.builder()
                .url(EXTENSION_URL + "/defaultTenantId")
                .value(string(fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/websocketNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/kafkaNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/natsNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_NATS_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        String notificationResourceTypes = getNotificationResourceTypes();
        if ("".equals(notificationResourceTypes)) {
            notificationResourceTypes = "<not specified - all resource types>";
        }

        extension = Extension.builder()
                .url(EXTENSION_URL + "/notificationResourceTypes")
                .value(string(notificationResourceTypes))
                .build();
        extentions.add(extension);

        String auditLogServiceName =
                fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_CLASS_NAME);

        if (auditLogServiceName == null || "".equals(auditLogServiceName)) {
            auditLogServiceName = "<not specified>";
        } else {
            int lastDelimeter = auditLogServiceName.lastIndexOf(".");
            auditLogServiceName = auditLogServiceName.substring(lastDelimeter + 1);
        }

        extension = Extension.builder()
                .url(EXTENSION_URL + "/auditLogServiceName")
                .value(string(auditLogServiceName))
                .build();
        extentions.add(extension);

        PropertyGroup auditLogProperties =
                fhirConfig.getPropertyGroup(FHIRConfiguration.PROPERTY_AUDIT_SERVICE_PROPERTIES);
        String auditLogPropertiesString =
                auditLogProperties != null ? auditLogProperties.toString() : "<not specified>";
        extension = Extension.builder()
                .url(EXTENSION_URL + "/auditLogProperties")
                .value(string(auditLogPropertiesString))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXTENSION_URL + "/persistenceType")
                .value(string(getPersistenceImpl().getClass().getSimpleName()))
                .build();
        extentions.add(extension);

        return capabilityStatement.toBuilder().extension(extentions).build();

    }

    private String getNotificationResourceTypes() throws Exception {
        Object[] notificationResourceTypes =
                fhirConfig.getArrayProperty(FHIRConfiguration.PROPERTY_NOTIFICATION_RESOURCE_TYPES);
        if (notificationResourceTypes == null) {
            notificationResourceTypes = new Object[0];
        }
        return Arrays.asList(notificationResourceTypes).toString().replace("[", "").replace("]", "").replace(" ", "");
    }

    private Interaction buildInteractionStatement(TypeRestfulInteraction value) {
        Interaction ci = Interaction.builder().code(value).build();
        return ci;
    }
}