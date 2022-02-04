/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.resources;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_CAPABILITY_STATEMENT_CACHE;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_AUTH_URL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_INTROSPECT_URL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_MANAGE_URL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_REG_URL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_REVOKE_URL;
import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_SECURITY_OAUTH_TOKEN_URL;
import static com.ibm.fhir.core.FHIRConstants.EXT_BASE;
import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.server.util.IssueTypeToHttpStatusMapper.issueListToStatus;

import java.net.URI;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.fhir.cache.CacheManager;
import com.ibm.fhir.cache.CacheManager.Configuration;
import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.ResourcesConfigAdapter;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.FHIRVersionParam;
import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Interaction;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource.Operation;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.TerminologyCapabilities;
import com.ibm.fhir.model.resource.TerminologyCapabilities.Closure;
import com.ibm.fhir.model.resource.TerminologyCapabilities.Expansion;
import com.ibm.fhir.model.resource.TerminologyCapabilities.Translation;
import com.ibm.fhir.model.resource.TerminologyCapabilities.ValidateCode;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CapabilityStatementKind;
import com.ibm.fhir.model.type.code.CodeSearchSupport;
import com.ibm.fhir.model.type.code.ConditionalDeleteStatus;
import com.ibm.fhir.model.type.code.ConditionalReadStatus;
import com.ibm.fhir.model.type.code.FHIRVersion;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.type.code.ResourceVersionPolicy;
import com.ibm.fhir.model.type.code.RestfulCapabilityMode;
import com.ibm.fhir.model.type.code.SystemRestfulInteraction;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction.Value;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource.Version;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.server.FHIRBuildIdentifier;
import com.ibm.fhir.server.operation.FHIROperationRegistry;
import com.ibm.fhir.server.spi.operation.FHIROperation;
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
    private static final String FHIR_COPYRIGHT = "(C) Copyright IBM Corporation 2016, 2022";
    private static final String FHIR_PUBLISHER = "IBM Corporation";
    private static final String BASE_CAPABILITY_URL = "http://hl7.org/fhir/CapabilityStatement/base";
    private static final String BASE_2_CAPABILITY_URL = "http://hl7.org/fhir/CapabilityStatement/base2";
    private static final List<String> ALL_INTERACTIONS = Arrays.asList("create", "read", "vread", "update", "patch", "delete", "history", "search");

    private static final Set<ResourceType> R4B_ONLY_RESOURCES = new HashSet<>();
    {
        R4B_ONLY_RESOURCES.add(ResourceType.ADMINISTRABLE_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.CITATION);
        R4B_ONLY_RESOURCES.add(ResourceType.CLINICAL_USE_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.EVIDENCE_REPORT);
        R4B_ONLY_RESOURCES.add(ResourceType.INGREDIENT);
        R4B_ONLY_RESOURCES.add(ResourceType.MANUFACTURED_ITEM_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.MEDICINAL_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.NUTRITION_PRODUCT);
        R4B_ONLY_RESOURCES.add(ResourceType.PACKAGED_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.REGULATED_AUTHORIZATION);
        R4B_ONLY_RESOURCES.add(ResourceType.SUBSCRIPTION_STATUS);
        R4B_ONLY_RESOURCES.add(ResourceType.SUBSCRIPTION_TOPIC);
        R4B_ONLY_RESOURCES.add(ResourceType.SUBSTANCE_DEFINITION);
        // The following resource types existed in R4, but have breaking changes in R4B.
        // Because we only support the R4B version, we don't want to advertise these in our 4.0.1 statement.
        R4B_ONLY_RESOURCES.add(ResourceType.DEVICE_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.EVIDENCE);
        R4B_ONLY_RESOURCES.add(ResourceType.EVIDENCE_VARIABLE);
    }

    // Error Messages
    private static final String ERROR_MSG = "Caught exception while processing 'metadata' request.";
    private static final String ERROR_CONSTRUCTING = "An error occurred while constructing the Conformance statement.";

    private static final String CAPABILITY_STATEMENT_CACHE_NAME = "com.ibm.fhir.server.resources.Capabilities.statementCache";

    // Constructor
    public Capabilities() throws Exception {
        super();
    }

    @GET
    @Path("metadata")
    public Response capabilities(@QueryParam("mode") @DefaultValue("full") String mode) {
        log.entering(this.getClass().getName(), "capabilities()");
        try {
            Date startTime = new Date();
            checkInitComplete();

            if (!isValidMode(mode)) {
                throw new IllegalArgumentException("Invalid mode parameter: must be one of [full, normative, terminology]");
            }

            // Defaults to 60 minutes (or what's in the fhirConfig)
            int cacheTimeout = FHIRConfigHelper.getIntProperty(PROPERTY_CAPABILITY_STATEMENT_CACHE, 60);
            Configuration configuration = Configuration.of(Duration.of(cacheTimeout, ChronoUnit.MINUTES));

            Map<String, Resource> cacheAsMap = CacheManager.getCacheAsMap(CAPABILITY_STATEMENT_CACHE_NAME, configuration);
            CacheManager.reportCacheStats(log, CAPABILITY_STATEMENT_CACHE_NAME);

            FHIRVersionParam fhirVersion = getFhirVersion();
            String cacheKey = mode + "-" + fhirVersion.value();
            Resource capabilityStatement = cacheAsMap.computeIfAbsent(cacheKey, k -> computeCapabilityStatement(mode, fhirVersion));

            RestAuditLogger.logMetadata(httpServletRequest, startTime, new Date(), Response.Status.OK);

            CacheControl cacheControl = new CacheControl();
            cacheControl.setPrivate(true);
            cacheControl.setMaxAge(60 * cacheTimeout);
            return Response.ok().entity(capabilityStatement).cacheControl(cacheControl).build();
        } catch (IllegalArgumentException e) {
            return exceptionResponse(e, Response.Status.BAD_REQUEST);
        } catch (RuntimeException e) {
            FHIROperationException foe = buildRestException(ERROR_CONSTRUCTING, IssueType.EXCEPTION);
            if (e.getCause() != null) {
                log.log(Level.SEVERE, ERROR_MSG, e.getCause());
            } else {
                log.log(Level.SEVERE, ERROR_MSG, foe);
            }
            return exceptionResponse(e, issueListToStatus(foe.getIssues()));
        } catch (Exception e) {
            log.log(Level.SEVERE, ERROR_MSG, e);
            return exceptionResponse(e, Response.Status.INTERNAL_SERVER_ERROR);
        } finally {
            log.exiting(this.getClass().getName(), "capabilities()");
        }
    }

    private boolean isValidMode(String mode) {
        return "full".equals(mode) || "normative".equals(mode) || "terminology".equals(mode);
    }

    private Resource computeCapabilityStatement(String mode, FHIRVersionParam fhirVersion) {
        try {
            switch (mode) {
            case "terminology":
                return buildTerminologyCapabilities();
            case "full":
            case "normative":
            default:
                return buildCapabilityStatement(fhirVersion);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TerminologyCapabilities buildTerminologyCapabilities() {
        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion() + " build id " + buildInfo.getBuildId() + "";

        /*
         * The following checks to see if there is a Terminology Service URL that we want to inline into the Terminology Capabilities Statement
         * else the minimal implementation.description.
         */
        String customTerminologyImpl = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_TERM_SERVICE_CAPABILITIES_URL, null);
        TerminologyCapabilities.Implementation impl;
        if (customTerminologyImpl != null) {
            impl = TerminologyCapabilities.Implementation.builder()
                    .description(string(buildDescription))
                    .url(com.ibm.fhir.model.type.Url.of(customTerminologyImpl))
                    .build();
        } else {
            impl = TerminologyCapabilities.Implementation.builder()
                    .description(string(buildDescription))
                    .build();
        }

        return TerminologyCapabilities.builder()
            .status(PublicationStatus.ACTIVE)
            .experimental(com.ibm.fhir.model.type.Boolean.TRUE)
            .date(DateTime.now(ZoneOffset.UTC))
            .kind(CapabilityStatementKind.INSTANCE)
            .version(string(buildInfo.getBuildVersion()))
            .name(string(FHIR_SERVER_NAME))
            .description(Markdown.of(buildDescription))
            .copyright(Markdown.of(FHIR_COPYRIGHT))
            .publisher(string(FHIR_PUBLISHER))
            .software(TerminologyCapabilities.Software.builder()
                .name(string(FHIR_SERVER_NAME))
                .version(string(buildInfo.getBuildVersion()))
                .id(buildInfo.getBuildId())
                .build())
            .implementation(impl)
            .codeSystem(buildCodeSystem())
            .expansion(Expansion.builder()
                .hierarchical(com.ibm.fhir.model.type.Boolean.FALSE)
                .paging(com.ibm.fhir.model.type.Boolean.FALSE)
                .incomplete(com.ibm.fhir.model.type.Boolean.FALSE)
                .textFilter(Markdown.of("Text searching is not supported"))
                .build())
            .codeSearch(CodeSearchSupport.ALL)
            .validateCode(ValidateCode.builder()
                .translations(com.ibm.fhir.model.type.Boolean.FALSE)
                .build())
            .translation(Translation.builder()
                .needsMap(com.ibm.fhir.model.type.Boolean.TRUE)
                .build())
            .closure(Closure.builder()
                .translation(com.ibm.fhir.model.type.Boolean.FALSE)
                .build())
            .build();
    }

    private List<TerminologyCapabilities.CodeSystem> buildCodeSystem() {
        Map<String, List<TerminologyCapabilities.CodeSystem.Version>> versionMap = new LinkedHashMap<>();

        for (FHIRRegistryResource registryResource : FHIRRegistry.getInstance().getRegistryResources(CodeSystem.class)) {
            String url = registryResource.getUrl();
            FHIRRegistryResource.Version version = registryResource.getVersion();

            List<TerminologyCapabilities.CodeSystem.Version> versions = versionMap.computeIfAbsent(url, k -> new ArrayList<>());
            if (!Version.NO_VERSION.equals(version)) {
                versions.add(TerminologyCapabilities.CodeSystem.Version.builder()
                    .code(string(version.toString()))
                    .isDefault(registryResource.isDefaultVersion() ? com.ibm.fhir.model.type.Boolean.TRUE : null)
                    .build());
            }
        }

        List<TerminologyCapabilities.CodeSystem> codeSystems = new ArrayList<>(versionMap.keySet().size());

        for (String url : versionMap.keySet()) {
            List<TerminologyCapabilities.CodeSystem.Version> versions = versionMap.get(url);
            codeSystems.add(TerminologyCapabilities.CodeSystem.builder()
                .uri(Canonical.of(url))
                .version(versions)
                .build());
        }

        return codeSystems;
    }

    /**
     * Builds a CapabilityStatement resource instance which describes this server.
     *
     * @throws Exception
     */
    private CapabilityStatement buildCapabilityStatement(FHIRVersionParam fhirVersion) throws Exception {
        // Retrieve the "resources" config property group.
        PropertyGroup rsrcsGroup = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);

        // Build the list of interactions, searchIncludes, and searchRevIncludes supported for each resource type by default.
        List<Rest.Interaction> systemInteractions = buildSystemInteractions(ALL_INTERACTIONS);
        List<Rest.Resource.Interaction> defaultTypeInteractions = buildTypeInteractions(ALL_INTERACTIONS);
        List<com.ibm.fhir.model.type.String> defaultSearchIncludes = Collections.emptyList();
        List<com.ibm.fhir.model.type.String> defaultSearchRevIncludes = Collections.emptyList();
        if (rsrcsGroup != null) {
            PropertyGroup parentResourcePropGroup = rsrcsGroup.getPropertyGroup(ResourceType.RESOURCE.value());
            if (parentResourcePropGroup != null) {
                List<String> interactionConfig = parentResourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
                if (interactionConfig != null) {
                    systemInteractions = buildSystemInteractions(interactionConfig);
                    defaultTypeInteractions = buildTypeInteractions(interactionConfig);
                }
                List<String> searchIncludeConfig = parentResourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_INCLUDES);
                if (searchIncludeConfig != null) {
                    defaultSearchIncludes = convertStringList(searchIncludeConfig);
                }
                List<String> searchRevIncludeConfig =
                        parentResourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_REV_INCLUDES);
                if (searchRevIncludeConfig != null) {
                    defaultSearchRevIncludes = convertStringList(searchRevIncludeConfig);
                }
            }
        }

        // Build the lists of operations that are supported
        Set<OperationDefinition> systemOps = new LinkedHashSet<>();
        Map<String, Set<OperationDefinition>> typeOps = new HashMap<>();

        FHIROperationRegistry opRegistry = FHIROperationRegistry.getInstance();
        List<String> operationNames = opRegistry.getOperationNames();
        for (String opName : operationNames) {
            FHIROperation operation = opRegistry.getOperation(opName);
            OperationDefinition opDef = operation.getDefinition();
            if (Boolean.TRUE.equals(opDef.getSystem().getValue())) {
                systemOps.add(opDef);
            }
            for (ResourceTypeCode resourceType : opDef.getResource()) {
                String resourceTypeName = resourceType.getValue();
                if (typeOps.containsKey(resourceTypeName)) {
                    typeOps.get(resourceTypeName).add(opDef);
                } else {
                    Set<OperationDefinition> typeOpList = new LinkedHashSet<>();
                    typeOpList.add(opDef);
                    typeOps.put(resourceTypeName, typeOpList);
                }
            }
        }

        com.ibm.fhir.model.type.Boolean isUpdateCreate = com.ibm.fhir.model.type.Boolean.of(isUpdateCreateEnabled());

        // Build the list of supported resources.
        List<Rest.Resource> resources = new ArrayList<>();

        ResourcesConfigAdapter configAdapter = new ResourcesConfigAdapter(rsrcsGroup, fhirVersion);
        Set<String> resourceTypeNames = configAdapter.getSupportedResourceTypes();

        for (String resourceTypeName : resourceTypeNames) {
            // Build the set of ConformanceSearchParams for this resource type.
            List<Rest.Resource.SearchParam> conformanceSearchParams = new ArrayList<>();
            Map<String, SearchParameter> searchParameters = SearchUtil.getSearchParameters(resourceTypeName);
            for (Entry<String, SearchParameter> entry : searchParameters.entrySet()) {
                String code = entry.getKey();
                SearchParameter searchParameter = entry.getValue();

                Rest.Resource.SearchParam.Builder conformanceSearchParamBuilder =
                        Rest.Resource.SearchParam.builder()
                            .name(code)
                            .type(searchParameter.getType());
                if (searchParameter.getDescription() != null) {
                    conformanceSearchParamBuilder.documentation(searchParameter.getDescription());
                }

                Rest.Resource.SearchParam conformanceSearchParam =
                        conformanceSearchParamBuilder.build();
                conformanceSearchParams.add(conformanceSearchParam);
            }

            List<Operation> ops = mapOperationDefinitionsToRestOperations(typeOps.get(resourceTypeName));
            // If the type is an abstract resource ("Resource" or "DomainResource")
            // then the operation can be invoked on any concrete specialization.
            ops.addAll(mapOperationDefinitionsToRestOperations(typeOps.get(ResourceType.RESOURCE.value())));
            if (DomainResource.class.isAssignableFrom(ModelSupport.getResourceType(resourceTypeName))) {
                ops.addAll(mapOperationDefinitionsToRestOperations(typeOps.get(ResourceType.DOMAIN_RESOURCE.value())));
            }

            // Build the list of interactions, searchIncludes, and searchRevIncludes supported for the resource type.
            List<Interaction> interactions = defaultTypeInteractions;
            List<com.ibm.fhir.model.type.String> searchIncludes = defaultSearchIncludes;
            List<com.ibm.fhir.model.type.String> searchRevIncludes = defaultSearchRevIncludes;
            if (rsrcsGroup != null) {
                PropertyGroup resourcePropGroup = rsrcsGroup.getPropertyGroup(resourceTypeName);
                if (resourcePropGroup != null) {
                    List<String> resourceInteractionConfig =
                            resourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_INTERACTIONS);
                    if (resourceInteractionConfig != null) {
                        interactions = buildTypeInteractions(resourceInteractionConfig);
                    }
                    List<String> searchIncludeConfig = resourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_INCLUDES);
                    if (searchIncludeConfig != null) {
                        searchIncludes = convertStringList(searchIncludeConfig);
                    }
                    List<String> searchRevIncludeConfig =
                            resourcePropGroup.getStringListProperty(FHIRConfiguration.PROPERTY_FIELD_RESOURCES_SEARCH_REV_INCLUDES);
                    if (searchRevIncludeConfig != null) {
                        searchRevIncludes = convertStringList(searchRevIncludeConfig);
                    }
                }
            }

            // Build the ConformanceResource for this resource type.
            Rest.Resource.Builder crb = Rest.Resource.builder()
                    .type(ResourceTypeCode.of(resourceTypeName))
                    .profile(Canonical.of("http://hl7.org/fhir/profiles/" + resourceTypeName))
                    .interaction(interactions)
                    .operation(ops)
                    .versioning(ResourceVersionPolicy.VERSIONED_UPDATE)
                    .conditionalCreate(com.ibm.fhir.model.type.Boolean.TRUE)
                    .conditionalUpdate(com.ibm.fhir.model.type.Boolean.TRUE)
                    .updateCreate(isUpdateCreate)
                    .conditionalDelete(ConditionalDeleteStatus.MULTIPLE)
                    .conditionalRead(ConditionalReadStatus.FULL_SUPPORT)
                    .searchParam(conformanceSearchParams)
                    .searchInclude(searchIncludes)
                    .searchRevInclude(searchRevIncludes);
            // Set readHistory to true if vread is supported for this resource type; otherwise leave it null
            if (interactions.stream().anyMatch(i -> i.getCode().getValueAsEnum() == Value.VREAD)) {
                crb.readHistory(true);
            }
            resources.add(crb.build());
        }

        CapabilityStatement.Rest.Security.Builder securityBuilder = CapabilityStatement.Rest.Security.builder()
                .cors(com.ibm.fhir.model.type.Boolean.of(FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_CORS, true)));

        if (FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_BASIC_ENABLED, false)) {
            securityBuilder.service(CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("Basic"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service"))
                    .build())
                .build());
        }
        if (FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_CERT_ENABLED, false)) {
            securityBuilder.service(CodeableConcept.builder()
                .coding(Coding.builder()
                    .code(Code.of("Certificates"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service"))
                    .build())
                .build());
        }
        if (FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_OAUTH_ENABLED, false)) {
            String actualHost = new URI(getRequestUri()).getHost();

            String authURLTemplate = null;
            String tokenURLTemplate = null;
            String regURLTemplate = null;
            String manageURLTemplate = null;
            String introspectURLTemplate = null;
            String revokeURLTemplate = null;

            try {
                authURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_AUTH_URL, "");
                tokenURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_TOKEN_URL, "");
                regURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_REG_URL, "");
                manageURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_MANAGE_URL, "");
                introspectURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_INTROSPECT_URL, "");
                revokeURLTemplate = FHIRConfigHelper.getStringProperty(PROPERTY_SECURITY_OAUTH_REVOKE_URL, "");
            } catch (Exception e) {
                log.log(Level.SEVERE, "An error occurred while adding OAuth URLs to the conformance statement", e);
            }
            String tokenURL = tokenURLTemplate.replaceAll("<host>", actualHost);
            String authURL = authURLTemplate.replaceAll("<host>", actualHost);
            String regURL = regURLTemplate.replaceAll("<host>", actualHost);
            String manageURL = manageURLTemplate.replaceAll("<host>", actualHost);
            String introspectURL = introspectURLTemplate.replaceAll("<host>", actualHost);
            String revokeURL = revokeURLTemplate.replaceAll("<host>", actualHost);

            Boolean smartEnabled = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_SECURITY_OAUTH_ENABLED, false);
            securityBuilder.service(CodeableConcept.builder()
                    .coding(Coding.builder()
                        .code(Code.of(smartEnabled ? "SMART-on-FHIR" : "OAuth"))
                        .system(Uri.of("http://terminology.hl7.org/CodeSystem/restful-security-service"))
                        .build())
                    .text(smartEnabled ? string("OAuth") : string("OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org)"))
                    .build())
                .extension(buildOAuthURIsExtension(authURL, tokenURL, regURL, manageURL, introspectURL, revokeURL));
        }

        CapabilityStatement.Rest rest = CapabilityStatement.Rest.builder()
                .mode(RestfulCapabilityMode.SERVER)
                .security(securityBuilder.build())
                .resource(addSupportedProfilesToResources(resources))
                .interaction(systemInteractions)
                .operation(mapOperationDefinitionsToRestOperations(systemOps))
                .build();

        FHIRBuildIdentifier buildInfo = new FHIRBuildIdentifier();
        String buildDescription = FHIR_SERVER_NAME + " version " + buildInfo.getBuildVersion()
                + " build id " + buildInfo.getBuildId() + "";

        List<Code> format = new ArrayList<>();
        format.add(Code.of(Format.JSON.toString().toLowerCase()));
        format.add(Code.of(Format.XML.toString().toLowerCase()));
        format.add(Code.of(FHIRMediaType.APPLICATION_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_JSON));
        format.add(Code.of(FHIRMediaType.APPLICATION_XML));
        format.add(Code.of(FHIRMediaType.APPLICATION_FHIR_XML));

        /*
         * The following checks to see if there is a IBM FHIR Server Service URL that we want to inline into the Capabilities Statement
         * else the minimal implementation.description.
         */
        String customImpl = FHIRConfigHelper.getStringProperty(FHIRConfiguration.PROPERTY_CAPABILITIES_URL, null);
        CapabilityStatement.Implementation impl;
        if (customImpl != null) {
            impl = CapabilityStatement.Implementation.builder()
                    .description(string(buildDescription))
                    .url(com.ibm.fhir.model.type.Url.of(customImpl))
                    .build();
        } else {
            impl = CapabilityStatement.Implementation.builder()
                    .description(string(buildDescription))
                    .build();
        }

        // Finally, create the CapabilityStatement resource itself.
        CapabilityStatement conformance = CapabilityStatement.builder()
                .status(PublicationStatus.ACTIVE)
                .date(DateTime.now(ZoneOffset.UTC))
                .kind(CapabilityStatementKind.INSTANCE)
                .fhirVersion(fhirVersion == FHIRVersionParam.VERSION_43 ? FHIRVersion.VERSION_4_3_0_CIBUILD : FHIRVersion.VERSION_4_0_1)
                .format(format)
                .patchFormat(Code.of(FHIRMediaType.APPLICATION_JSON_PATCH),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_JSON),
                             Code.of(FHIRMediaType.APPLICATION_FHIR_XML))
                .version(string(buildInfo.getBuildVersion()))
                .name(string(FHIR_SERVER_NAME))
                .description(Markdown.of(buildDescription))
                .copyright(Markdown.of(FHIR_COPYRIGHT))
                .publisher(string(FHIR_PUBLISHER))
                .software(CapabilityStatement.Software.builder()
                          .name(string(FHIR_SERVER_NAME))
                          .version(string(buildInfo.getBuildVersion()))
                          .id(buildInfo.getBuildId())
                          .build())
                .rest(rest)
                .instantiates(buildInstantiates())
                .implementation(impl)
                .build();

        try {
            conformance = addExtensionElements(conformance);
        } catch (Exception e) {
            log.log(Level.SEVERE, "An error occurred while adding extension elements to the conformance statement", e);
        }

        return conformance;
    }

    /**
     * @param interactionConfig a list of strings that represent the RESTful interactions to support at the system level
     *                          (create, read, vread, update, patch, delete, history, and/or search)
     * @return a list of Rest.Resource.Interaction objects to include in the CapabilityStatement
     * @throws FHIRPersistenceException
     */
    private List<Rest.Interaction> buildSystemInteractions(List<String> interactionConfig) throws Exception {
        List<Rest.Interaction> interactions = new ArrayList<>();
        interactions.add(buildSystemInteractionStatement(SystemRestfulInteraction.BATCH));
        try {
            // If transactions are supported for this FHIR Server configuration
            if (getPersistenceImpl().isTransactional()) {
                interactions.add(buildSystemInteractionStatement(SystemRestfulInteraction.TRANSACTION));
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Unexpected error while reading server transaction mode setting", t);
        }

        if (interactionConfig != null) {
            for (String interactionString : interactionConfig) {
                if ("search".equals(interactionString)) {
                    // special case for search since the value set uses "search-system" instead of just "search"
                    interactions.add(buildSystemInteractionStatement(SystemRestfulInteraction.SEARCH_SYSTEM));
                } else if ("history".equals(interactionString)){
                    // special case for search since the value set uses "history-system" instead of just "history"
                    interactions.add(buildSystemInteractionStatement(SystemRestfulInteraction.HISTORY_SYSTEM));
                }
            }
        }
        return interactions;
    }

    /**
     * @param interactionConfig a list of strings that represent the RESTful interactions to support for this resource type
     *                          (create, read, vread, update, patch, delete, history, and/or search)
     * @return a list of Rest.Resource.Interaction objects to include in the CapabilityStatement
     * @throws FHIRPersistenceException
     */
    private List<Rest.Resource.Interaction> buildTypeInteractions(List<String> interactionConfig) throws Exception {
        if (interactionConfig == null) return null;

        List<Rest.Resource.Interaction> interactions = new ArrayList<>();
        for (String interactionString : interactionConfig) {
            if ("search".equals(interactionString)) {
                // special case for search since the value set uses "search-type" instead of just "search"
                interactions.add(buildTypeInteractionStatement(TypeRestfulInteraction.SEARCH_TYPE));
            } else if ("history".equals(interactionString)){
                // special case for search since the value set has "history-type" and "history-instance" instead of just "history"
                interactions.add(buildTypeInteractionStatement(TypeRestfulInteraction.HISTORY_TYPE));
                interactions.add(buildTypeInteractionStatement(TypeRestfulInteraction.HISTORY_INSTANCE));
            } else if ("delete".equals(interactionString)) {
                // special case for delete since we shouldn't advertise it if the PL doesn't support it
                interactions.add(buildTypeInteractionStatement(TypeRestfulInteraction.DELETE));
            } else {
                interactions.add(buildTypeInteractionStatement(TypeRestfulInteraction.of(interactionString)));
            }
        }
        return interactions;
    }

    /**
     * Convert list of Java strings to list of FHIR strings.
     * @param stringList a list of Java string, or null
     * @return a list of FHIR strings, or null
     */
    private List<com.ibm.fhir.model.type.String> convertStringList(List<String> stringList) {
        if (stringList != null) {
            return stringList.stream().map(k -> com.ibm.fhir.model.type.String.of(k)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Builds the list of canonicals for the instantiates field based on the capability statements (except FHIR core)
     * found in the FHIR registry.
     *
     * @return list of canonicals
     */
    private List<Canonical> buildInstantiates() {
        Collection<CapabilityStatement> registeredCapabilities = FHIRRegistry.getInstance().getResources(CapabilityStatement.class);

        List<Canonical> instantiates = new ArrayList<>();
        for (CapabilityStatement registeredCapability : registeredCapabilities) {
            if (registeredCapability != null && registeredCapability.getUrl() != null) {
                boolean isServerCS = false;
                for(Rest r : registeredCapability.getRest()) {
                    if (RestfulCapabilityMode.Value.SERVER == r.getMode().getValueAsEnum()) {
                        isServerCS = true;
                    }
                }
                String url = registeredCapability.getUrl().getValue();
                // BASE_CAPABILITY_URL and BASE_2_CAPABILITY_URL come from the core spec and shouldn't be advertised
                if (url != null && isServerCS && !BASE_CAPABILITY_URL.equals(url) && !BASE_2_CAPABILITY_URL.equals(url)) {
                    String canonicalValue = url;
                    if (registeredCapability.getVersion() != null && registeredCapability.getVersion().getValue() != null) {
                        canonicalValue = canonicalValue + "|" + registeredCapability.getVersion().getValue();
                    }
                    instantiates.add(Canonical.builder().value(canonicalValue).build());
                }
            }
        }

        return instantiates;
    }



    private Extension buildOAuthURIsExtension(String authURL, String tokenURL, String regURL, String manageURL, String introspectURL, String revokeURL) {
         Extension.Builder builder = Extension.builder().url("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris");

         builder.extension(Extension.builder().url("authorize").value(Uri.of(authURL)).build());
         builder.extension(Extension.builder().url("token").value(Uri.of(tokenURL)).build());

         if (regURL != null && !regURL.isEmpty()) {
             builder.extension(Extension.builder().url("register").value(Uri.of(regURL)).build());
         }
         if (manageURL != null && !manageURL.isEmpty()) {
             builder.extension(Extension.builder().url("register").value(Uri.of(manageURL)).build());
         }
         if (introspectURL != null && !introspectURL.isEmpty()) {
             builder.extension(Extension.builder().url("register").value(Uri.of(introspectURL)).build());
         }
         if (revokeURL != null && !revokeURL.isEmpty()) {
             builder.extension(Extension.builder().url("register").value(Uri.of(revokeURL)).build());
         }

         return builder.build();
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

    private List<Rest.Resource.Operation> mapOperationDefinitionsToRestOperations(Set<OperationDefinition> inOpDefs) {
        List<OperationDefinition> opDefs = new ArrayList<>();
        if (inOpDefs != null) {
            opDefs = new ArrayList<>(inOpDefs);
        } else {
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
        List<Extension> extentions = new ArrayList<>();
        Extension extension = Extension.builder()
                .url(EXT_BASE + "defaultTenantId")
                .value(string(fhirConfig.getStringProperty(FHIRConfiguration.PROPERTY_DEFAULT_TENANT_ID, FHIRConfiguration.DEFAULT_TENANT_ID)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXT_BASE + "websocketNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_WEBSOCKET_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXT_BASE + "kafkaNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXT_BASE + "natsNotificationsEnabled")
                .value(com.ibm.fhir.model.type.Boolean.of(fhirConfig.getBooleanProperty(FHIRConfiguration.PROPERTY_NATS_ENABLED, Boolean.FALSE)))
                .build();
        extentions.add(extension);

        String notificationResourceTypes = getNotificationResourceTypes();
        if ("".equals(notificationResourceTypes)) {
            notificationResourceTypes = "<not specified - all resource types>";
        }

        extension = Extension.builder()
                .url(EXT_BASE + "notificationResourceTypes")
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
                .url(EXT_BASE + "auditLogServiceName")
                .value(string(auditLogServiceName))
                .build();
        extentions.add(extension);

        extension = Extension.builder()
                .url(EXT_BASE + "persistenceType")
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

    private Rest.Resource.Interaction buildTypeInteractionStatement(TypeRestfulInteraction value) {
        Rest.Resource.Interaction ci = Rest.Resource.Interaction.builder().code(value).build();
        return ci;
    }

    private Rest.Interaction buildSystemInteractionStatement(SystemRestfulInteraction value) {
        Rest.Interaction ci = Rest.Interaction.builder().code(value).build();
        return ci;
    }
}
