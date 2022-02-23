/*
 * (C) Copyright IBM Corp. 2018, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.openapi.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.examples.ExamplesUtil;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.ActivityDefinition;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.DeviceDefinition;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.EventDefinition;
import com.ibm.fhir.model.resource.EvidenceVariable;
import com.ibm.fhir.model.resource.GuidanceResponse;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.MedicationDispense;
import com.ibm.fhir.model.resource.MedicationKnowledge;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.MedicationStatement;
import com.ibm.fhir.model.resource.MedicinalProductContraindication;
import com.ibm.fhir.model.resource.MedicinalProductIndication;
import com.ibm.fhir.model.resource.MedicinalProductManufactured;
import com.ibm.fhir.model.resource.MedicinalProductPackaged;
import com.ibm.fhir.model.resource.MedicinalProductUndesirableEffect;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.PlanDefinition;
import com.ibm.fhir.model.resource.RequestGroup;
import com.ibm.fhir.model.resource.ResearchElementDefinition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.resource.StructureMap;
import com.ibm.fhir.model.resource.SubstancePolymer;
import com.ibm.fhir.model.resource.Task;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Population;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.ProductShelfLife;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SubstanceAmount;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.AbstractVisitable;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.util.SearchUtil;
import com.ibm.fhir.swagger.generator.APIConnectAdapter;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

/**
 * Generate OpenAPI 3.0 from the HL7 FHIR R4 artifacts and IBM FHIR object model.
 *
 * <p>
 * By default, this class will generate:
 * <ol>
 * <li> an "all-in-one" OpenAPI definition for the entire api
 * <li> a separate OpenAPI definition for each resource type and compartment, with all HTTP interactions enabled
 * </ol>
 *
 * <p>
 * To limit the output to a given set of resources and/or interactions, pass a set of semicolon-delimited
 * filter strings of the form {@code ResourceType1(interaction1,interaction2)}.
 *
 * For example:
 * <pre>
 * Patient(create,read,vread,history,search,update,delete);Contract(create,read,vread,history,search);RiskAssessment(read)
 * </pre>
 */
public class FHIROpenApiGenerator {
    /**
     * This should match the webApplication contextRoot in server.xml (unless the path is rewritten in front of the server)
     */
    private static final String CONTEXT_ROOT = "/fhir-server/api/v4";
    // TODO: make this configurable
    private static final String OUTDIR = "openapi";
    private static final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    private static final Map<Class<?>, StructureDefinition> structureDefinitionMap = buildStructureDefinitionMap();
    private static boolean includeDeleteOperation = true;
    public static final String TYPEPACKAGENAME = "com.ibm.fhir.model.type";
    public static final String RESOURCEPACKAGENAME = "com.ibm.fhir.model.resource";
    public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";
    private static List<String> classNamesList = null;

    private static final CompartmentUtil compartmentHelper = new CompartmentUtil();

    public static void main(String[] args) throws Exception {
        File file = new File(OUTDIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("Generating openapi definitions at " + file.getCanonicalPath());

        Filter filter = null;
        if (args.length == 1) {
            filter = createFilter(args[0]);
        } else {
            filter = createAcceptAllFilter();
        }

        generateAllInOne(filter);  // outputs to all-openapi.json

        generateOneFilePerResource(filter);  // outputs to ResourceType-openapi.json
        generateOneFilePerCompartment(filter);  // outputs to ResourceType-compartment-openapi.json
        generateMetadataOpenApi();
        generateBatchTransactionOpenApi(filter);
        generateWholeSystemHistoryOpenApi();
    }

    private static void generateAllInOne(Filter filter) throws Exception {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("openapi", "3.0.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Simplified FHIR API");
        info.add("description", "A simplified version of the HL7 FHIR API");
        info.add("version", "4.0.1");
        swagger.add("info", info);

        JsonArrayBuilder servers = factory.createArrayBuilder();
        JsonObjectBuilder server = factory.createObjectBuilder();
        server.add("url", CONTEXT_ROOT);
        servers.add(server);
        swagger.add("servers", servers);

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        // LinkedHashSet to preserve the order but still avoid duplicates
        Set<Class<?>> tagsToAdd = new LinkedHashSet<>();
        Set<Class<?>> requestBodiesToAdd = new LinkedHashSet<>();
        Set<Class<?>> definitionsToAdd = new LinkedHashSet<>();
        // common entries for all types
        definitionsToAdd.add(Resource.class);
        definitionsToAdd.add(DomainResource.class);
        // for search response
        if (filter.acceptOperation("search")) {
            definitionsToAdd.add(Bundle.class);
            definitionsToAdd.add(Bundle.Link.class);
            definitionsToAdd.add(Bundle.Entry.class);
            definitionsToAdd.add(Bundle.Entry.Request.class);
            definitionsToAdd.add(Bundle.Entry.Response.class);
            definitionsToAdd.add(Bundle.Entry.Search.class);
        }
        // for /metadata
        definitionsToAdd.add(CapabilityStatement.class);
        definitionsToAdd.add(CapabilityStatement.Software.class);
        definitionsToAdd.add(CapabilityStatement.Implementation.class);
        definitionsToAdd.add(CapabilityStatement.Rest.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Security.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Resource.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Resource.Interaction.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Resource.SearchParam.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Resource.Operation.class);
        definitionsToAdd.add(CapabilityStatement.Rest.Interaction.class);
        definitionsToAdd.add(CapabilityStatement.Messaging.class);
        definitionsToAdd.add(CapabilityStatement.Messaging.Endpoint.class);
        definitionsToAdd.add(CapabilityStatement.Messaging.SupportedMessage.class);
        definitionsToAdd.add(CapabilityStatement.Document.class);
        // for error response
        definitionsToAdd.add(OperationOutcome.class);
        definitionsToAdd.add(OperationOutcome.Issue.class);

        JsonArrayBuilder tags = factory.createArrayBuilder();
        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder requestBodies = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        for (String resourceClassName : getClassNames()) {
            Class<?> resourceModelClass = Class.forName(RESOURCEPACKAGENAME + "." + resourceClassName);
            if (DomainResource.class.isAssignableFrom(resourceModelClass)
                    && DomainResource.class != resourceModelClass) {
                if (filter.acceptResourceType(resourceModelClass)) {

                    generatePaths(resourceModelClass, paths, filter);
                    tagsToAdd.add(resourceModelClass);
                    requestBodiesToAdd.add(resourceModelClass);
                    definitionsToAdd.add(resourceModelClass);

                    // generate definition for all inner classes inside the top level resources.
                    for (String innerClassName : getAllResourceInnerClasses()) {
                        String parentClassName = innerClassName.split("\\$")[0];
                        if (resourceClassName.equals(parentClassName)) {
                            Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                            definitionsToAdd.add(innerModelClass);
                        }
                    }

                    // add all the applicable data types to the set of definitions
                    for (String typeClassName : getAllTypesList()) {
                        Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                        if (isApplicableForClass(typeModelClass, resourceModelClass)) {
                            definitionsToAdd.add(typeModelClass);
                        }
                    }
                }

                // Within compartments, only include resource types that are accepted by the filter
                for (String withinCompartmentResourceClassName : getCompartmentClassNames(resourceClassName)) {
                    Class<?> withinCompartmentResourceModelClass = Class.forName(FHIROpenApiGenerator.RESOURCEPACKAGENAME + "." + withinCompartmentResourceClassName);
                    if (DomainResource.class.isAssignableFrom(withinCompartmentResourceModelClass)
                            && DomainResource.class != withinCompartmentResourceModelClass
                            && filter.acceptResourceType(withinCompartmentResourceModelClass)) {

                        generateCompartmentPaths(resourceModelClass, withinCompartmentResourceModelClass, paths, filter);
                        tagsToAdd.add(withinCompartmentResourceModelClass);
                        requestBodiesToAdd.add(withinCompartmentResourceModelClass);
                        definitionsToAdd.add(withinCompartmentResourceModelClass);

                        // generate definition for all inner classes inside the top level resources.
                        for (String innerClassName : getAllResourceInnerClasses()) {
                            String parentClassName = innerClassName.split("\\$")[0];
                            if (withinCompartmentResourceClassName.equals(parentClassName)) {
                                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                                definitionsToAdd.add(innerModelClass);
                            }
                        }

                        // add all the applicable data types to the set of definitions
                        for (String typeClassName : getAllTypesList()) {
                            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                            if (isApplicableForClass(typeModelClass, withinCompartmentResourceModelClass)) {
                                definitionsToAdd.add(typeModelClass);
                            }
                        }
                    }
                }
            }
        }

        for (Class<?> clazz : tagsToAdd) {
            JsonObjectBuilder tag = factory.createObjectBuilder();
            tag.add("name", clazz.getSimpleName());
            tags.add(tag);
        }

        for (Class<?> clazz : requestBodiesToAdd) {
            generateRequestBody(clazz, requestBodies);
        }

        for (Class<?> clazz : definitionsToAdd) {
            generateDefinition(clazz, definitions);
        }

        JsonObjectBuilder components = factory.createObjectBuilder();
        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateSearchParameters(parameters, filter);
        generateWholeSystemHistoryParameters(parameters, true);
        JsonObject parametersObject = parameters.build();
        if (!parametersObject.isEmpty()) {
            components.add("parameters", parametersObject);
        }

        // FHIR metadata operation
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateMetadataPathItem(path);
        paths.add("/metadata", path);

        // FHIR batch/transaction operation
        path = factory.createObjectBuilder();
        generateBatchPathItem(path);
        paths.add("/", path);

        // FHIR Whole System History operation
        path = factory.createObjectBuilder();
        generateWholeSystemHistoryPathItem(path, "Other", "Get the whole system history", false);
        paths.add("/_history", path);

        components.add("requestBodies", requestBodies);
        components.add("schemas", definitions);

        swagger.add("tags", tags);
        swagger.add("paths", paths);
        swagger.add("components", components);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "all-openapi.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateOneFilePerResource(Filter filter) throws Exception {
        List<String> classNames = getClassNames();
        for (String resourceClassName : classNames) {
            Class<?> resourceModelClass = Class.forName(RESOURCEPACKAGENAME + "." + resourceClassName);
            if (DomainResource.class.isAssignableFrom(resourceModelClass)
                    && DomainResource.class != resourceModelClass
                    && filter.acceptResourceType(resourceModelClass)) {

                JsonObjectBuilder swagger = factory.createObjectBuilder();
                swagger.add("openapi", "3.0.0");

                JsonObjectBuilder info = factory.createObjectBuilder();
                info.add("title", resourceClassName + " API");
                info.add("description", "A simplified version of the HL7 FHIR API for " + resourceClassName + " resources.");
                info.add("version", "4.0.1");
                swagger.add("info", info);

                JsonArrayBuilder servers = factory.createArrayBuilder();
                JsonObjectBuilder server = factory.createObjectBuilder();
                server.add("url", CONTEXT_ROOT);
                servers.add(server);
                swagger.add("servers", servers);

                // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
                // with a default ExecuteInvoke Assembly
                APIConnectAdapter.addApiConnectStuff(swagger);

                JsonArrayBuilder tags = factory.createArrayBuilder();
                JsonObjectBuilder paths = factory.createObjectBuilder();
                JsonObjectBuilder definitions = factory.createObjectBuilder();
                JsonObjectBuilder requestBodies = factory.createObjectBuilder();

                // generate Resource and DomainResource definitions
                generateDefinition(Resource.class, definitions);
                generateDefinition(DomainResource.class, definitions);

                generatePaths(resourceModelClass, paths, filter);
                JsonObjectBuilder tag = factory.createObjectBuilder();
                tag.add("name", resourceModelClass.getSimpleName());
                tags.add(tag);
                generateRequestBody(resourceModelClass, requestBodies);
                generateDefinition(resourceModelClass, definitions);
                // for search response
                generateDefinition(Bundle.class, definitions);
                // for error response
                generateDefinition(OperationOutcome.class, definitions);

                // generate definition for all inner classes inside the top level resources.
                for (String innerClassName : getAllResourceInnerClasses()) {
                    String parentClassName = innerClassName.split("\\$")[0];
                    if (resourceClassName.equals(parentClassName) ||
                            "DomainResource".equals(parentClassName) ||
                            "Resource".equals(parentClassName) ||
                            "Bundle".equals(parentClassName) ||
                            "OperationOutcome".equals(parentClassName)) {
                        Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                        generateDefinition(innerModelClass, definitions);
                    }
                }

                // generate definition for all the applicable data types.
                for (String typeClassName : getAllTypesList()) {
                    Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                    if (isApplicableForClass(typeModelClass, resourceModelClass)) {
                        generateDefinition(typeModelClass, definitions);
                    }
                }

                swagger.add("tags", tags);
                swagger.add("paths", paths);

                JsonObjectBuilder components = factory.createObjectBuilder();
                JsonObjectBuilder parameters = factory.createObjectBuilder();
                generateSearchParameters(parameters, filter);
                generateWholeSystemHistoryParameters(parameters, filter);
                JsonObject parametersObject = parameters.build();
                if (!parametersObject.isEmpty()) {
                    components.add("parameters", parametersObject);
                }
                components.add("requestBodies", requestBodies);
                components.add("schemas", definitions);

                swagger.add("components", components);

                Map<String, Object> config = new HashMap<String, Object>();
                config.put(JsonGenerator.PRETTY_PRINTING, true);
                JsonWriterFactory factory = Json.createWriterFactory(config);

                File outFile = new File(OUTDIR + File.separator + resourceClassName + "-openapi.json");
                try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
                    writer.writeObject(swagger.build());
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }

    private static void generateOneFilePerCompartment(Filter filter) throws Exception {
        List<String> classNames = getClassNames();
        for (String compartmentClassName : classNames) {
            Class<?> compartmentModelClass = Class.forName(RESOURCEPACKAGENAME + "." + compartmentClassName);
            if (DomainResource.class.isAssignableFrom(compartmentModelClass)
                && DomainResource.class != compartmentModelClass) {

                List<String> resourceClassNames = getCompartmentClassNames(compartmentClassName);
                if (resourceClassNames == null || resourceClassNames.isEmpty()) {
                    continue;
                }

                JsonArrayBuilder tags = factory.createArrayBuilder();
                JsonObjectBuilder paths = factory.createObjectBuilder();
                JsonObjectBuilder definitions = factory.createObjectBuilder();
                JsonObjectBuilder requestBodies = factory.createObjectBuilder();
                boolean addedContent = false;

                // Only include resource types that are accepted by the filter
                for (String resourceClassName : resourceClassNames) {
                    Class<?> resourceModelClass = Class.forName(FHIROpenApiGenerator.RESOURCEPACKAGENAME + "." + resourceClassName);
                    if (DomainResource.class.isAssignableFrom(resourceModelClass)
                            && DomainResource.class != resourceModelClass
                            && filter.acceptResourceType(resourceModelClass)) {
                        addedContent = true;

                        generateCompartmentPaths(compartmentModelClass, resourceModelClass, paths, filter);
                        JsonObjectBuilder tag = factory.createObjectBuilder();
                        tag.add("name", resourceModelClass.getSimpleName());
                        tags.add(tag);
                        generateRequestBody(resourceModelClass, requestBodies);
                        generateDefinition(resourceModelClass, definitions);

                        // generate definition for all inner classes inside the top level resources.
                        for (String innerClassName : getAllResourceInnerClasses()) {
                            String parentClassName = innerClassName.split("\\$")[0];
                            if (resourceClassName.equals(parentClassName) ||
                                    "DomainResource".equals(parentClassName) ||
                                    "Resource".equals(parentClassName) ||
                                    "Bundle".equals(parentClassName) ||
                                    "OperationOutcome".equals(parentClassName)) {
                                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                                generateDefinition(innerModelClass, definitions);
                            }
                        }

                        // generate definition for all the applicable data types.
                        for (String typeClassName : getAllTypesList()) {
                            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                            if (isApplicableForClass(typeModelClass, resourceModelClass)) {
                                generateDefinition(typeModelClass, definitions);
                            }
                        }
                    }
                }

                if (!addedContent) {
                    continue;
                }

                JsonObjectBuilder swagger = factory.createObjectBuilder();
                swagger.add("openapi", "3.0.0");

                JsonObjectBuilder info = factory.createObjectBuilder();
                info.add("title", compartmentClassName + " compartment API");
                info.add("description", "A simplified version of the HL7 FHIR API for " + compartmentClassName + " compartment.");
                info.add("version", "4.0.1");
                swagger.add("info", info);

                JsonArrayBuilder servers = factory.createArrayBuilder();
                JsonObjectBuilder server = factory.createObjectBuilder();
                server.add("url", CONTEXT_ROOT);
                servers.add(server);
                swagger.add("servers", servers);

                // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
                // with a default ExecuteInvoke Assembly
                APIConnectAdapter.addApiConnectStuff(swagger);

                // generate Resource and DomainResource definitions
                generateDefinition(Resource.class, definitions);
                generateDefinition(DomainResource.class, definitions);
                // for search response
                generateDefinition(Bundle.class, definitions);
                // for error response
                generateDefinition(OperationOutcome.class, definitions);

                swagger.add("tags", tags);
                swagger.add("paths", paths);

                JsonObjectBuilder components = factory.createObjectBuilder();
                JsonObjectBuilder parameters = factory.createObjectBuilder();
                generateSearchParameters(parameters, filter);
                generateWholeSystemHistoryParameters(parameters, filter);
                JsonObject parametersObject = parameters.build();
                if (!parametersObject.isEmpty()) {
                    components.add("parameters", parametersObject);
                }
                components.add("requestBodies", requestBodies);
                components.add("schemas", definitions);

                swagger.add("components", components);

                Map<String, Object> config = new HashMap<String, Object>();
                config.put(JsonGenerator.PRETTY_PRINTING, true);
                JsonWriterFactory factory = Json.createWriterFactory(config);

                File outFile = new File(OUTDIR + File.separator + compartmentClassName + "-compartment-openapi.json");
                try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
                    writer.writeObject(swagger.build());
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }

    private static void generateMetadataOpenApi() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("openapi", "3.0.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Capabilities");
        info.add("description", "The capabilities interaction retrieves the information about a server's capabilities - which portions of the HL7 FHIR specification it supports.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        JsonArrayBuilder servers = factory.createArrayBuilder();
        JsonObjectBuilder server = factory.createObjectBuilder();
        server.add("url", CONTEXT_ROOT);
        servers.add(server);
        swagger.add("servers", servers);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR capabilities interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateMetadataPathItem(path);
        paths.add("/metadata", path);
        swagger.add("paths", paths);

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        generateDefinition(CapabilityStatement.class, definitions);
        generateDefinition(Resource.class, definitions);
        generateDefinition(DomainResource.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (CapabilityStatement.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the applicable defined Types.
        for (String typeClassName : getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            if (isApplicableForClass(typeModelClass, CapabilityStatement.class)) {
                generateDefinition(typeModelClass, definitions);
            }
        }

        JsonObjectBuilder components = factory.createObjectBuilder();

        components.add("schemas", definitions);

        swagger.add("components", components);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "metadata-openapi.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateWholeSystemHistoryOpenApi() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("openapi", "3.0.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Whole System History");
        info.add("description", "The whole system history interaction can be used to obtain a list of changes (create, update, delete) to resources in the IBM FHIR Server.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        JsonArrayBuilder servers = factory.createArrayBuilder();
        JsonObjectBuilder server = factory.createObjectBuilder();
        server.add("url", CONTEXT_ROOT);
        servers.add(server);
        swagger.add("servers", servers);

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR _history interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateWholeSystemHistoryPathItem(path, "Other", "Get the whole system history", false);
        paths.add("/_history", path);
        swagger.add("paths", paths);

        generateDefinition(Bundle.class, definitions);
        generateDefinition(Resource.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (Bundle.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the applicable data types.
        for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            if (FHIROpenApiGenerator.isApplicableForClass(typeModelClass, Bundle.class)) {
                generateDefinition(typeModelClass, definitions);
            }
        }

        JsonObjectBuilder components = factory.createObjectBuilder();

        components.add("schemas", definitions);

        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateWholeSystemHistoryParameters(parameters, true);
        JsonObject parametersObject = parameters.build();
        if (!parametersObject.isEmpty()) {
            components.add("parameters", parametersObject);
        }

        swagger.add("components", components);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "wholeSystemHistory-openapi.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateBatchTransactionOpenApi(Filter filter) throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("openapi", "3.0.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Batch/Transaction");
        info.add("description", "The batch and transaction interactions submit a set of actions to perform on a server in a single HTTP request/response.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        JsonArrayBuilder servers = factory.createArrayBuilder();
        JsonObjectBuilder server = factory.createObjectBuilder();
        server.add("url", CONTEXT_ROOT);
        servers.add(server);
        swagger.add("servers", servers);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR batch/transaction interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateBatchPathItem(path);
        paths.add("/", path);
        swagger.add("paths", paths);

        generateDefinition(Bundle.class, definitions);
        generateDefinition(Resource.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (Bundle.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the defined Types.
        for (String typeClassName : getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            generateDefinition(typeModelClass, definitions);
        }

        JsonObjectBuilder components = factory.createObjectBuilder();

        components.add("schemas", definitions);

        swagger.add("components", components);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "batch-openapi.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static Map<Class<?>, StructureDefinition> buildStructureDefinitionMap() {
        Map<Class<?>, StructureDefinition> structureDefinitionMap = new HashMap<Class<?>, StructureDefinition>();
        try {
            populateStructureDefinitionMap(structureDefinitionMap, "profiles-resources.json");
            populateStructureDefinitionMap(structureDefinitionMap, "profiles-types.json");
        } catch (Exception e) {
            throw new Error(e);
        }
        return structureDefinitionMap;
    }

    public static void populateStructureDefinitionMap(Map<Class<?>, StructureDefinition> structureDefinitionMap,
            String structureDefinitionFile) throws Exception {

        Bundle bundle;
        try (InputStream stream = FHIROpenApiGenerator.class.getClassLoader().getResourceAsStream(structureDefinitionFile)) {
            bundle = FHIRParser.parser(Format.JSON).parse(stream);
        }
        for (Entry entry : bundle.getEntry()) {
            if (entry.getResource() instanceof StructureDefinition) {
                StructureDefinition structureDefinition = (StructureDefinition) entry.getResource();
                if (structureDefinition != null) {
                    String className = structureDefinition.getName().getValue();
                    className = className.substring(0, 1).toUpperCase() + className.substring(1);
                    Class<?> modelClass = null;
                    try {
                        modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
                    } catch (ClassNotFoundException e1) {
                        try {
                            // special case for MetadataResource which has a structuredefinition but is not a legal resource type
                            if (!className.equals("MetadataResource")) {
                                modelClass = Class.forName(TYPEPACKAGENAME + "." + className);
                            }
                        } catch (ClassNotFoundException e2) {
                            modelClass = null;
                            System.err.println(" -- PopulateStructureDefinition failed: " + className);
                        }
                    } finally {
                        if (modelClass != null) {
                            structureDefinitionMap.put(modelClass, structureDefinition);
                        }
                    }
                }
            }
        }
    }

    private static void generateSearchParameters(JsonObjectBuilder parameters, Filter filter) throws Exception {
        if (filter.acceptOperation("search")) {
            for (Map.Entry<String, SearchParameter> entry : SearchUtil.getSearchParameters(Resource.class.getSimpleName()).entrySet()) {
                SearchParameter searchParameter = entry.getValue();

                JsonObjectBuilder parameter = factory.createObjectBuilder();
                String name = entry.getKey();
                parameter.add("name", name);
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("required", false);

                JsonObjectBuilder schema = factory.createObjectBuilder();
                schema.add("type", "string");
                parameter.add("schema", schema);

                parameters.add(name + "Param", parameter);
            }
        }

    }

    private static void generateWholeSystemHistoryParameters(JsonObjectBuilder parameters, Filter filter) throws Exception {
        if (filter.acceptOperation("history")) {
            generateWholeSystemHistoryParameters(parameters, false);
        }

    }

    private static void generateWholeSystemHistoryParameters(JsonObjectBuilder parameters, boolean includeType) throws Exception {
        // Add the _sort parameter (_sort=[-_lastUpdated, _lastUpdated, none])
        // curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_since=2021-02-21T00:00:00Z&_sort=_lastUpdated'

        JsonObjectBuilder sort = factory.createObjectBuilder();
        sort.add("name", "_sort");
        sort.add("description", "Sort the returned data using one of three options: \r\n\r\n* -_lastUpdated: [Default] Decreasing time - most recent changes first\r\n* _lastUpdated: Increasing time - oldest changes first\r\n* none: Increasing id order - oldest changes first\r\n");
        sort.add("in", "query");
        sort.add("required", false);

        JsonArrayBuilder enums = factory.createArrayBuilder();
        enums.add("-_lastUpdated");
        enums.add("_lastUpdated");
        enums.add("none");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("type", "string");
        schema.add("enum", enums);
        sort.add("schema", schema);

        parameters.add("_sortParam", sort);

        // add _since=<timestamp>
        JsonObjectBuilder since = factory.createObjectBuilder();
        since.add("name", "_since");
        since.add("description", "Return changes since a known timestamp");
        since.add("in", "query");
        since.add("required", false);
        schema = factory.createObjectBuilder();
        schema.add("type", "string");
        since.add("schema", schema);

        parameters.add("_sinceParam", since);

        // add _before=<timestamp>
        JsonObjectBuilder before = factory.createObjectBuilder();
        before.add("name", "_before");
        before.add("description", "Return changes before a known timestamp");
        before.add("in", "query");
        before.add("required", false);
        schema = factory.createObjectBuilder();
        schema.add("type", "string");
        before.add("schema", schema);

        parameters.add("_beforeParam", before);

        // add _count
        JsonObjectBuilder count = factory.createObjectBuilder();
        count.add("name", "_count");
        count.add("description", "Specifies a maximum number of results that are required");
        count.add("in", "query");
        count.add("required", false);
        schema = factory.createObjectBuilder();
        schema.add("type", "integer");
        schema.add("default", 100);
        count.add("schema", schema);

        parameters.add("_countParam", count);

        // add _type
        if (includeType) {
            JsonObjectBuilder type = factory.createObjectBuilder();
            type.add("name", "_type");
            type.add("description", "Limit which resource types are returned");
            type.add("in", "query");
            type.add("required", false);
            schema = factory.createObjectBuilder();
            schema.add("type", "string");
            type.add("schema", schema);

            parameters.add("_typeParam", type);
        }
    }

    private static void generatePaths(Class<?> modelClass, JsonObjectBuilder paths, Filter filter) throws Exception {
        JsonObjectBuilder path = factory.createObjectBuilder();
        // FHIR create operation
        if (filter.acceptOperation(modelClass, "create")) {
            generateCreatePathItem(modelClass, path);
        }
        // FHIR search operation
        if (filter.acceptOperation(modelClass, "search")) {
            generateSearchPathItem(null, modelClass, path);
        }
        JsonObject pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName(), pathObject);
        }

        path = factory.createObjectBuilder();
        // FHIR search (via POST) operation
        if (filter.acceptOperation(modelClass, "search")) {
            generateSearchViaPostPathItem(null, modelClass, path);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName() + "/_search", pathObject);
        }

        path = factory.createObjectBuilder();
        // FHIR vread operation
        if (filter.acceptOperation(modelClass, "vread")) {
            generateVreadPathItem(modelClass, path);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName() + "/{id}/_history/{vid}", pathObject);
        }

        path = factory.createObjectBuilder();
        // FHIR read operation
        if (filter.acceptOperation(modelClass, "read")) {
            generateReadPathItem(modelClass, path);
        }
        // FHIR update operation
        if (filter.acceptOperation(modelClass, "update")) {
            generateUpdatePathItem(modelClass, path);
        }
        // FHIR delete operation
        if (filter.acceptOperation(modelClass, "delete") && includeDeleteOperation) {
            generateDeletePathItem(modelClass, path);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName() + "/{id}", pathObject);
        }

        // FHIR history operation
        path = factory.createObjectBuilder();
        if (filter.acceptOperation(modelClass, "history")) {
            generateHistoryPathItem(modelClass, path);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName() + "/{id}/_history", pathObject);
        }

        // Add Whole system History for type
        path = factory.createObjectBuilder();
        if (filter.acceptOperation(modelClass, "history")) {
            generateWholeSystemHistoryPathItem(path, modelClass.getSimpleName(), "Get the whole system history for " + modelClass.getSimpleName() + " resources", true);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName() + "/_history", pathObject);
        }

        // TODO: add patch
    }

    private static void generateCompartmentPaths(Class<?> compartmentModelClass, Class<?> resourceModelClass, JsonObjectBuilder paths, Filter filter) throws Exception {
        JsonObjectBuilder path = factory.createObjectBuilder();

        // FHIR search operation
        if (filter.acceptOperation(resourceModelClass, "search")) {
            generateSearchPathItem(compartmentModelClass, resourceModelClass, path);
        }
        JsonObject pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + compartmentModelClass.getSimpleName() + "/{id}/" + resourceModelClass.getSimpleName(), pathObject);
        }

        path = factory.createObjectBuilder();

        // FHIR search (via POST) operation
        if (filter.acceptOperation(resourceModelClass, "search")) {
            generateSearchViaPostPathItem(compartmentModelClass, resourceModelClass, path);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + compartmentModelClass.getSimpleName() + "/{id}/" + resourceModelClass.getSimpleName() + "/_search", pathObject);
        }
    }

    private static void generateCreatePathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder post = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        post.add("tags", tags);
        post.add("summary", "Create" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        post.add("operationId", "create" + modelClass.getSimpleName());

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Create " + modelClass.getSimpleName() + " operation successful");
        responses.add("201", response);
        post.add("responses", responses);

        /**
         * "requestBody": { "$ref": "#/components/requestBodies/Account" }
         */
        JsonObjectBuilder requestBody = factory.createObjectBuilder();
        requestBody.add("$ref", "#/components/requestBodies/" + modelClass.getSimpleName());
        post.add("requestBody", requestBody);

        path.add("post", post);
    }

    private static void generateReadPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Read" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "read" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        addIdPathParam(idParamRef);
        parameters.add(idParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Read " + modelClass.getSimpleName() + " operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/" + modelClass.getSimpleName());
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateVreadPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary",
                "Read specific version of" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "vread" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        addIdPathParam(idParamRef);
        parameters.add(idParamRef);

        JsonObjectBuilder vidParamRef = factory.createObjectBuilder();
        addVidParamRef(vidParamRef);
        parameters.add(vidParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Versioned read " + modelClass.getSimpleName() + " operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/" + modelClass.getSimpleName());
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void addIdPathParam(JsonObjectBuilder idParamRef) {
        idParamRef.add("name", "id");
        idParamRef.add("in", "path");
        idParamRef.add("required", true);
        idParamRef.add("description", "logical identifier");
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("type", "string");
        idParamRef.add("schema", schema);
    }

    private static void addVidParamRef(JsonObjectBuilder vidParamRef) {
        vidParamRef.add("name", "vid");
        vidParamRef.add("in", "path");
        vidParamRef.add("required", true);
        vidParamRef.add("description", "version identifier");
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("type", "string");
        vidParamRef.add("schema", schema);
    }

    private static void generateUpdatePathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder put = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        put.add("tags", tags);
        put.add("summary", "Update an existing " + modelClass.getSimpleName() + " resource");
        put.add("operationId", "update" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        addIdPathParam(idParamRef);
        parameters.add(idParamRef);

        put.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response1 = factory.createObjectBuilder();
        response1.add("description", "Update " + modelClass.getSimpleName() + " operation successful");

        JsonObjectBuilder response2 = factory.createObjectBuilder();
        response2.add("description", "Create " + modelClass.getSimpleName()
                + " operation successful (requires 'updateCreateEnabled' configuration option)");

        responses.add("200", response1);
        responses.add("201", response2);

        put.add("responses", responses);

        /**
         * "requestBody": { "$ref": "#/components/requestBodies/Account" }
         */
        JsonObjectBuilder requestBody = factory.createObjectBuilder();
        requestBody.add("$ref", "#/components/requestBodies/" + modelClass.getSimpleName());
        put.add("requestBody", requestBody);

        path.add("put", put);
    }

    private static void generateDeletePathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder delete = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        delete.add("tags", tags);
        delete.add("summary", "Delete" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        delete.add("operationId", "delete" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        addIdPathParam(idParamRef);
        parameters.add(idParamRef);

        delete.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Delete " + modelClass.getSimpleName() + " operation successful");
        responses.add("204", response);
        delete.add("responses", responses);

        path.add("delete", delete);
    }

    private static void generateSearchPathItem(Class<?> compartmentModelClass, Class<?> modelClass, JsonObjectBuilder path) throws Exception {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());
        get.add("tags", tags);

        if (compartmentModelClass != null) {
            get.add("summary", "Search for " + modelClass.getSimpleName() + " resources within " + compartmentModelClass.getSimpleName() + " compartment");
            get.add("operationId", "search" + compartmentModelClass.getSimpleName() + "Compartment"+ modelClass.getSimpleName());
        } else {
            get.add("summary", "Search for " + modelClass.getSimpleName() + " resources");
            get.add("operationId", "search" + modelClass.getSimpleName());
        }

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // For compartment search, include parameter for {id}
        if (compartmentModelClass != null) {
            JsonObjectBuilder idParamRef = factory.createObjectBuilder();
            addIdPathParam(idParamRef);
            parameters.add(idParamRef);
        }
        generateSearchParameters(modelClass, parameters);
        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Search " + modelClass.getSimpleName() + " operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateSearchParameters(Class<?> modelClass, JsonArrayBuilder parameters) throws Exception {
        for (Map.Entry<String, SearchParameter> entry : SearchUtil.getSearchParameters(modelClass.getSimpleName()).entrySet()) {
            SearchParameter searchParameter = entry.getValue();

            JsonObjectBuilder parameter = factory.createObjectBuilder();
            String name = entry.getKey();
            if (name.startsWith("_")) {
                parameter.add("$ref", "#/components/parameters/" + name + "Param");
            } else {
                parameter.add("name", name);
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("required", false);

                /**
                 * "schema": { "type": "string" }
                 */
                JsonObjectBuilder schema = factory.createObjectBuilder();
                schema.add("type", "string");
                parameter.add("schema", schema);
            }
            parameters.add(parameter);
        }
    }

    private static void generateSearchViaPostPathItem(Class<?> compartmentModelClass, Class<?> modelClass, JsonObjectBuilder path) throws Exception {
        JsonObjectBuilder post = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());
        post.add("tags", tags);

        if (compartmentModelClass != null) {
            post.add("summary", "Search for " + modelClass.getSimpleName() + " resources within " + compartmentModelClass.getSimpleName() + " compartment");
            post.add("operationId", "searchViaPost" + compartmentModelClass.getSimpleName() + "Compartment"+ modelClass.getSimpleName());
        } else {
            post.add("summary", "Search for " + modelClass.getSimpleName() + " resources");
            post.add("operationId", "searchViaPost" + modelClass.getSimpleName());
        }

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // For compartment search, include parameter for {id}
        if (compartmentModelClass != null) {
            JsonObjectBuilder idParamRef = factory.createObjectBuilder();
            addIdPathParam(idParamRef);
            parameters.add(idParamRef);
        }
        generateSearchParameters(modelClass, parameters);
        post.add("parameters", parameters);

        JsonObjectBuilder requestBody = factory.createObjectBuilder();

        /**
         * "content": { "application/x-www-form-urlencoded": { "schema": { "type": "object" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        JsonObjectBuilder formParameters = factory.createObjectBuilder();
        schema.add("type", "object");
        generateSearchFormParameters(modelClass, formParameters);
        schema.add("properties", formParameters);
        contentType.add("schema", schema);
        content.add(APPLICATION_FORM, contentType);

        requestBody.add("content", content);
        post.add("requestBody", requestBody);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Search " + modelClass.getSimpleName() + " operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        content = factory.createObjectBuilder();
        contentType = factory.createObjectBuilder();
        schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        post.add("responses", responses);

        path.add("post", post);
    }

    private static void generateSearchFormParameters(Class<?> modelClass, JsonObjectBuilder parameters) throws Exception {
        //
        // As of OpenAPI 3.0.0, empty form fields are still passed to the REST API, which is not valid
        // for the FHIR search API.
        //
        // For example: curl -X POST "https://localhost:9443/fhir-server/api/v4/Appointment/_search"
        // -H  "accept: application/fhir+json" -H  "Content-Type: application/x-www-form-urlencoded"
        // -d "reason-reference=&patient=&_lastUpdated=&supporting-info=&appointment-type=&identifier=
        // &_profile=&part-status=&date=&service-type=&status=&actor=&location=&service-category=
        // &reason-code=&based-on=&_source=&_id=&practitioner=&_tag=&specialty=&slot=&_security="
        //
        // So for now, do not generate form parameters for OpenAPI 3.0.
        // Swagger does not pass empty form fields, so form parameters are generated for Swagger.
        //
//        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>(
//                SearchUtil.getApplicableSearchParameters(modelClass.getSimpleName()));
//        for (SearchParameter searchParameter : searchParameters) {
//            String name = searchParameter.getName().getValue();
//
//            JsonObjectBuilder propertyValues = factory.createObjectBuilder();
//            /**
//             * "<name>": { "type": "string" }
//             */
//            propertyValues.add("type", "string");
//            propertyValues.add("description", searchParameter.getDescription().getValue());
//
//            parameters.add(name, propertyValues);
//        }
    }

    private static void generateHistoryPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Return the history of" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "history" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        addIdPathParam(idParamRef);
        parameters.add(idParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "History " + modelClass.getSimpleName() + " operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateMetadataPathItem(JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add("Other");

        get.add("tags", tags);
        get.add("summary", "Get the FHIR Capability statement for this endpoint");
        get.add("operationId", "metadata");

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "metadata operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/CapabilityStatement");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    /**
     * This method is used by both the resource specific whole system history path generation and the general whole system history path generation.
     * If it is resource specific, the type parameter will not be generated and the resource type will be included in the operationId.
     * @param path
     * @param tag
     * @param summary
     * @param resourceSpecific
     */
    private static void generateWholeSystemHistoryPathItem(JsonObjectBuilder path, String tag, String summary, boolean resourceSpecific) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(tag);

        get.add("tags", tags);
        get.add("summary", summary);

        String operationId = "wholeSystemHistory";
        if (resourceSpecific) {
            operationId = operationId + tag;
        }
        get.add("operationId", operationId);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // Add parameters to api call
        JsonObjectBuilder sortParameter = factory.createObjectBuilder();
        sortParameter.add("$ref", "#/components/parameters/_sortParam");
        parameters.add(sortParameter);

        JsonObjectBuilder sinceParameter = factory.createObjectBuilder();
        sinceParameter.add("$ref", "#/components/parameters/_sinceParam");
        parameters.add(sinceParameter);

        JsonObjectBuilder beforeParameter = factory.createObjectBuilder();
        beforeParameter.add("$ref", "#/components/parameters/_beforeParam");
        parameters.add(beforeParameter);

        JsonObjectBuilder countParameter = factory.createObjectBuilder();
        countParameter.add("$ref", "#/components/parameters/_countParam");
        parameters.add(countParameter);

        // Add the type parameter if non resource specific
        if (!resourceSpecific) {
            JsonObjectBuilder typeParameter = factory.createObjectBuilder();
            typeParameter.add("$ref", "#/components/parameters/_typeParam");
            parameters.add(typeParameter);
        }

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "whole system history operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);

        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateBatchPathItem(JsonObjectBuilder path) {
        JsonObjectBuilder post = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add("Other");

        post.add("tags", tags);
        post.add("summary", "Perform a batch operation");
        post.add("operationId", "batch");

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "batch operation successful");

        /**
         * "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }
         */
        JsonObjectBuilder content = factory.createObjectBuilder();
        JsonObjectBuilder contentType = factory.createObjectBuilder();
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        response.add("content", content);
        responses.add("200", response);
        post.add("responses", responses);

        /**
         * "requestBody": { "content": { "application/fhir+json": { "schema": { "$ref":
         * "#/components/schemas/Bundle" } } }, "required": true }
         */
        JsonObjectBuilder requestBody = factory.createObjectBuilder();
        content = factory.createObjectBuilder();
        contentType = factory.createObjectBuilder();
        schema = factory.createObjectBuilder();
        schema.add("$ref", "#/components/schemas/Bundle");
        contentType.add("schema", schema);
        content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

        requestBody.add("content", content.build());
        requestBody.add("required", true);
        post.add("requestBody", requestBody);

        path.add("post", post);
    }

    private static void generateRequestBody(Class<?> modelClass, JsonObjectBuilder requestBodies) {
        if (ModelSupport.isResourceType(modelClass.getSimpleName())) {
//        "Coverage": {
//            "content": {
//                "application/fhir+json": {
//                    "schema": {
//                        "$ref": "#/components/schemas/Coverage"
//                    }
//                }
//            },
//            "required": true
//        },
            JsonObjectBuilder requestBody = factory.createObjectBuilder();

            JsonObjectBuilder content = factory.createObjectBuilder();
            JsonObjectBuilder contentType = factory.createObjectBuilder();
            JsonObjectBuilder schema = factory.createObjectBuilder();
            schema.add("$ref", "#/components/schemas/" + modelClass.getSimpleName());
            contentType.add("schema", schema);
            content.add(FHIRMediaType.APPLICATION_FHIR_JSON, contentType);

            requestBody.add("content", content);
            requestBody.add("required", true);
            requestBodies.add(modelClass.getSimpleName(), requestBody);
        }
    }

    private static void generateDefinition(Class<?> modelClass, JsonObjectBuilder definitions) throws Exception {
        if (!ModelSupport.isPrimitiveType(modelClass)) {
            JsonObjectBuilder definition = factory.createObjectBuilder();
            JsonObjectBuilder properties = factory.createObjectBuilder();
            JsonArrayBuilder required = factory.createArrayBuilder();

            StructureDefinition structureDefinition = getStructureDefinition(modelClass);

            if (structureDefinition == null) {
                System.err.println("Failed generateDefinition for: " + modelClass.getName());
                return;
            }

            if (Resource.class.isAssignableFrom(modelClass)) {
                // if the modelClass is a resource, then add the 'resourceType' property
                JsonObjectBuilder property = factory.createObjectBuilder();

                // Convert all the primitive types to json types.
                property.add("type", "string");
                if (Resource.class == modelClass) {
                    // TODO: when a filter was passed, limit this to just the resource types included in the filter
                    List<String> typeNames = getClassNames();
                    JsonArrayBuilder enumValues = factory.createArrayBuilder(typeNames);
                    property.add("enum", enumValues);
                    properties.add("resourceType", property.build());
                    required.add("resourceType");
                } else {
                    // TODO how to "overwrite" the Resource definition and say that the value is fixed?
                    // https://github.com/OAI/OpenAPI-Specification/issues/1313
//                    property.add("enum", modelClass.getSimpleName());
                }
            }

            for (Field field : modelClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isVolatile(field.getModifiers())) {
                    if (!ModelSupport.isChoiceElement(modelClass, ModelSupport.getElementName(field)) &&
                            field.isAnnotationPresent(Required.class)) {
                        required.add(ModelSupport.getElementName(field));
                    }
                    generateProperties(structureDefinition, modelClass, field, properties);
                }
            }

            JsonArray requiredArray = required.build();

            Class<?> superClass = modelClass.getSuperclass();
            if (superClass != null && superClass.getPackage().getName().startsWith("com.ibm.fhir.model")
                    && !superClass.equals(AbstractVisitable.class)) {
                JsonArrayBuilder allOf = factory.createArrayBuilder();

                JsonObjectBuilder ref = factory.createObjectBuilder();
                ref.add("$ref", "#/components/schemas/" + superClass.getSimpleName());
                allOf.add(ref);

                JsonObjectBuilder wrapper = factory.createObjectBuilder();
                wrapper.add("type", "object");
                wrapper.add("properties", properties);
                if (!requiredArray.isEmpty()) {
                    wrapper.add("required", requiredArray);
                }
                allOf.add(wrapper);

                definition.add("allOf", allOf);
            } else {
                definition.add("type", "object");
                if (Resource.class.equals(modelClass)) {
                    JsonObject discriminator = factory.createObjectBuilder().add("propertyName", "resourceType").build();
                    definition.add("discriminator", discriminator);
                }
                definition.add("properties", properties);
                if (!requiredArray.isEmpty()) {
                    definition.add("required", requiredArray);
                }
            }

            if (Resource.class.isAssignableFrom(modelClass)) {
                addExamples(modelClass, definition);
            }

            definitions.add(getSimpleNameWithEnclosingNames(modelClass), definition);
        }
    }

    public static void addExamples(Class<?> modelClass, JsonObjectBuilder definition) throws IOException {
        if (!Modifier.isAbstract(modelClass.getModifiers())) {
            // Change this from "complete-mock" to "minimal" to reduce the size of the generated definition
            Reader example = ExamplesUtil.resourceReader("json/ibm/minimal/" + modelClass.getSimpleName() + "-1.json");
            JsonReader jsonReader = Json.createReader(example);
            definition.add("example", jsonReader.readObject());
        }
    }

    private static String getSimpleNameWithEnclosingNames(Class<?> modelClass) {
        StringBuilder fullName = new StringBuilder(modelClass.getSimpleName());
        while (modelClass.isMemberClass()) {
            modelClass = modelClass.getEnclosingClass();
            fullName.insert(0, modelClass.getSimpleName() + "_");
        }
        return fullName.toString();
    }

    private static StructureDefinition getStructureDefinition(Class<?> modelClass) {
        StructureDefinition structureDefinition = null;
        // Use Code definition for all its sub-classes for now
        if (Code.class.isAssignableFrom(modelClass)) {
            try {
                structureDefinition = structureDefinitionMap.get(Class.forName(TYPEPACKAGENAME + "." + "Code"));
            } catch (ClassNotFoundException e) {
                structureDefinition = null;
            }
        } else {
            structureDefinition = structureDefinitionMap.get(modelClass);
            structureDefinition = (structureDefinition != null)
                    ? structureDefinition : getEnclosingStructureDefinition(modelClass);
        }

        return structureDefinition;
    }

    private static StructureDefinition getEnclosingStructureDefinition(Class<?> modelClass) {
        StructureDefinition structureDefinition = null;
        int nameLength = 0;
        for (Class<?> key : structureDefinitionMap.keySet()) {
            String modelClassName;
            if (modelClass.getName().contains(TYPEPACKAGENAME)) {
                modelClassName = modelClass.getName().substring(TYPEPACKAGENAME.length()+1);
            } else {
                modelClassName = modelClass.getName().substring(RESOURCEPACKAGENAME.length()+1);
            }

            if (modelClassName.startsWith(key.getSimpleName())
                    && key.getSimpleName().length() > nameLength) {
                structureDefinition = structureDefinitionMap.get(key);
                nameLength = key.getSimpleName().length();
            }
        }
        return structureDefinition;
    }

    private static void generateProperties(StructureDefinition structureDefinition, Class<?> modelClass, Field field,
        JsonObjectBuilder properties) throws Exception {

        boolean many = false;

        Type fieldType = field.getType();
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            fieldType = parameterizedType.getActualTypeArguments()[0];
            many = true;
        }

        String elementName = ModelSupport.getElementName(field);

        if (ModelSupport.isChoiceElement(modelClass, elementName)) {
            Set<Class<?>> choiceElementTypes = ModelSupport.getChoiceElementTypes(modelClass, elementName);
            ElementDefinition elementDefinition = getElementDefinition(structureDefinition, modelClass, elementName + "[x]");
            String description = elementDefinition.getDefinition().getValue();
            Integer min = elementDefinition.getMin() != null ? elementDefinition.getMin().getValue() : null;
            for (Class<?> choiceType : choiceElementTypes) {
                if (isApplicableForClass(choiceType, modelClass)) {
                    String choiceElementName = ModelSupport.getChoiceElementName(elementName, choiceType);
                    generateProperty(structureDefinition, modelClass, field, properties, choiceElementName, choiceType, many, description, min);
                }
            }
        } else {
            ElementDefinition elementDefinition = getElementDefinition(structureDefinition, modelClass, elementName);
            String description = elementDefinition.getDefinition().getValue();
            Integer min = elementDefinition.getMin() != null ? elementDefinition.getMin().getValue() : null;
            generateProperty(structureDefinition, modelClass, field, properties, elementName, (Class<?>)fieldType, many, description, min);
        }
    }

    private static void generateProperty(StructureDefinition structureDefinition, Class<?> modelClass, Field field,
            JsonObjectBuilder properties, String elementName, Class<?> fieldClass, boolean many, String description, Integer min)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        JsonObjectBuilder property = factory.createObjectBuilder();

        // Convert all the primitive types to json types.
        if (isEnumerationWrapperClass(fieldClass)) {
            property.add("type", "string");
            JsonArrayBuilder constants = factory.createArrayBuilder();
            Class<?> enumClass = Class.forName(fieldClass.getName() + "$ValueSet");
            for (Object constant : enumClass.getEnumConstants()) {
                Method method = constant.getClass().getMethod("value");
                String value = (String) method.invoke(constant);
                constants.add(value);
            }
            property.add("enum", constants);
        } else if (com.ibm.fhir.model.type.String.class.isAssignableFrom(fieldClass)) {
            property.add("type", "string");
            if (com.ibm.fhir.model.type.Code.class.isAssignableFrom(fieldClass)) {
                property.add("pattern", "[^\\s]+(\\s[^\\s]+)*");
            } else if (com.ibm.fhir.model.type.Id.class.equals(fieldClass)) {
                property.add("pattern", "[A-Za-z0-9\\-\\.]{1,64}");
            } else {
                property.add("pattern", "[ \\r\\n\\t\\S]+");
            }
        } else if (com.ibm.fhir.model.type.Uri.class.isAssignableFrom(fieldClass)) {
            property.add("type", "string");
            if (Oid.class.equals(fieldClass)) {
                property.add("pattern", "urn:oid:[0-2](\\.(0|[1-9][0-9]*))+");
            } else if (Uuid.class.equals(fieldClass)) {
                property.add("pattern", "urn:uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
            } else {
                property.add("pattern", "\\S*");
            }
        } else if (com.ibm.fhir.model.type.Boolean.class.equals(fieldClass)) {
            property.add("type", "boolean");
        } else if (com.ibm.fhir.model.type.Instant.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern", "([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)-(0[1-9]"
                    + "|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                    + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))");
        } else if (com.ibm.fhir.model.type.Time.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?");
        } else if (com.ibm.fhir.model.type.Date.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)"
                    + "|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?");
        } else if (com.ibm.fhir.model.type.DateTime.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]"
                    + "|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                    + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?");
        } else if (com.ibm.fhir.model.type.Decimal.class.equals(fieldClass)) {
            property.add("type", "number");
        } else if (com.ibm.fhir.model.type.Integer.class.isAssignableFrom(fieldClass)) {
            property.add("type", "integer");
            property.add("format", "int32");
        } else if (com.ibm.fhir.model.type.Base64Binary.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","(\\s*([0-9a-zA-Z\\+/=]){4}\\s*)+");
        } else if (String.class.equals(fieldClass)) {
            property.add("type", "string");
            if ("id".equals(elementName)) {
                property.add("pattern", "[A-Za-z0-9\\-\\.]{1,64}");
            } else if ("url".equals(elementName)) {
                property.add("pattern", "\\S*");
            }
        } else if (com.ibm.fhir.model.type.Xhtml.class.equals(fieldClass)) {
            property.add("type", "string");
        } else {
            // For non-primitives, point to the corresponding definition
            property.add("$ref", "#/components/schemas/" + getSimpleNameWithEnclosingNames(fieldClass));
        }

        if (description != null) {
            property.add("description", cleanse(description));
        }

        // Include select examples to help tools avoid bumping into infinite recursion (if they try generate examples)
        JsonStructure example = null;
        if (Element.class.equals(modelClass) && Extension.class.equals(fieldClass)) {
            // "example": [{"url":"http://example.com","valueString":"textValue"}]
            JsonObject obj = factory.createObjectBuilder()
                .add("url", "http://example.com")
                .add("valueString", "text value")
                .build();
            example = factory.createArrayBuilder().add(obj).build();
        } else if (Identifier.class.equals(modelClass) && Reference.class.equals(fieldClass)) {
            // "example": {"reference":"Organization/123","type":"Organization","display":"The Assigning Organization"}
            example = factory.createObjectBuilder()
                    .add("reference", "Organization/123")
                    .add("type", "Organization")
                    .add("display", "The Assigning Organization")
                    // skip assigner to break the recursion
                    .build();
        }

        if (many) {
            JsonObjectBuilder wrapper = factory.createObjectBuilder();
            wrapper.add("type", "array");
            wrapper.add("items", property);
            if (min != null && min > 0) {
                wrapper.add("minItems", min.intValue());
            }
            if (example != null) {
                wrapper.add("example", example);
            }
            properties.add(elementName, wrapper);
        } else {
            if (example != null) {
                property.add("example", example);
            }
            properties.add(elementName, property);
        }
    }

    /*
     * clean up description to make valid unicode encoded output.
     */
    private static String cleanse(String description) {
        String cleansed = description.replaceAll("“", "&ldquo;")
                .replaceAll("”", "&rdquo;")
                .replaceAll("…", "&hellip;")
                .replaceAll("’", "&rsquo;")
                .replaceAll("‘", "&lsquo;")
                .replaceAll("-", "&ndash;")
                .replaceAll("—", "&mdash;")
                .replaceAll("‑", "&#8209;"); // Non-Breaking Hyphen

        if (!Charset.forName("US-ASCII").newEncoder().canEncode(cleansed)) {
            // If a new character sneaks in - it'll output here.
            // Best place to look up entity is https://unicodelookup.com
            System.out.println("[Failure to cleanse the description] - " + cleansed);
        }

        return cleansed;
    }

    /**
     * Returns the ElementDefinition for the given elementName in the Type represented by modelClass
     */
    private static ElementDefinition getElementDefinition(StructureDefinition structureDefinition, Class<?> modelClass, String elementName) {
        String structureDefinitionName = structureDefinition.getName().getValue();
        String path = structureDefinitionName;

        String pathEnding = elementName;
        if (BackboneElement.class.isAssignableFrom(modelClass) && !BackboneElement.class.equals(modelClass) && modelClass.isMemberClass()) {
            String modelClassName = modelClass.getSimpleName();
            modelClassName = modelClassName.substring(0, 1).toLowerCase() + modelClassName.substring(1);

            if (Character.isDigit(modelClassName.charAt(modelClassName.length() - 1))) {
                modelClassName = modelClassName.substring(0, modelClassName.length() - 1);
            }

            path += "." + modelClassName;
            pathEnding = modelClassName + "." + elementName;
        }

        path += "." + elementName;

        for (ElementDefinition elementDefinition : structureDefinition.getDifferential().getElement()) {
            String elementDefinitionPath = elementDefinition.getPath().getValue();
            if (elementDefinitionPath.equals(path) || (elementDefinitionPath.startsWith(structureDefinitionName)
                    && elementDefinitionPath.endsWith(pathEnding))) {
                return elementDefinition;
            }
        }

        throw new RuntimeException("Unable to retrieve element definition for " + elementName + " in " + modelClass.getName());
    }

    public static List<String> getClassNames() {
        if (classNamesList == null) {
            classNamesList = ModelSupport.getResourceTypes().stream().map(r -> ModelSupport.getTypeName(r)).collect(Collectors.toList());
            classNamesList = Collections.unmodifiableList(classNamesList);
        }

        return classNamesList;
    }

    private static List<String> getCompartmentClassNames(String compartment) {
        try {
            return compartmentHelper.getCompartmentResourceTypes(compartment);
        } catch (FHIRSearchException e) {
            return Collections.emptyList();
        }
    }

    private static boolean isEnumerationWrapperClass(Class<?> type) {
        try {
            Class.forName(type.getName() + "$ValueSet");
            return true;
        } catch (Exception e) {
            // do nothing
        }
        return false;
    }

    private static class Filter {
        private Map<String, List<String>> filterMap = null;

        public Filter(Map<String, List<String>> filterMap) {
            this.filterMap = filterMap;
        }

        public boolean acceptResourceType(Class<?> resourceType) {
            return filterMap.containsKey(resourceType.getSimpleName());
        }

        /**
         * @return true if the operation is enables for the specified resourceType, otherwise false
         */
        public boolean acceptOperation(Class<?> resourceType, String operation) {
            return filterMap.get(resourceType.getSimpleName()).contains(operation);
        }

        /**
         * @return true if one or more of the resources in the filterMap includes the passed operation, otherwise false
         */
        public boolean acceptOperation(String operation) {
            for (String resourceType : filterMap.keySet()) {
                List<String> operationList = filterMap.get(resourceType);
                if (operationList.contains(operation)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static Filter createFilter(String filterString) {
        return new Filter(parseFilterString(filterString));
    }

    private static Map<String, List<String>> parseFilterString(String filterString) {
        Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
        for (String component : filterString.split(";")) {
            String resourceType = component.substring(0, component.indexOf("("));
            String operations = component.substring(component.indexOf("(") + 1, component.indexOf(")"));
            List<String> operationList = new ArrayList<String>();
            for (String operation : operations.split(",")) {
                operationList.add(operation);
            }
            filterMap.put(resourceType, operationList);
        }
        return filterMap;
    }

    private static Filter createAcceptAllFilter() throws Exception {
        return new Filter(buildAcceptAllFilterMap());
    }

    // build filter map for all domain resources and operations
    private static Map<String, List<String>> buildAcceptAllFilterMap() throws Exception {
        Map<String, List<String>> filterMap = new HashMap<String, List<String>>();
        for (String className : getClassNames()) {
            Class<?> modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
            if (DomainResource.class.isAssignableFrom(modelClass)) {
                String resourceType = className;
                // TODO: add patch
                List<String> operationList = Arrays.asList("create", "read", "vread", "update", "delete", "search",
                        "history", "batch", "transaction");
                filterMap.put(resourceType, operationList);
            }
        }
        return filterMap;
    }

    private static String getArticle(Class<?> modelClass) {
        List<String> prefixes = Arrays.asList("A", "E", "I", "O", "Un");
        for (String prefix : prefixes) {
            if (modelClass.getSimpleName().startsWith(prefix)) {
                return " an ";
            }
        }
        return " a ";
    }

    private static void addInnerClassNames(Class<?> classToIterate, List<String> resultList) {
        try {
            Class<?>[] InnerClassArray = classToIterate.getDeclaredClasses();

            for (Class<?> InnerClass : InnerClassArray) {
                if (InnerClass.getSimpleName().equals("Builder") || InnerClass.getSimpleName().equals("ValueSet")) {
                    continue;
                }
                int classNameOffset;
                if (InnerClass.getName().contains(RESOURCEPACKAGENAME)) {
                    classNameOffset = RESOURCEPACKAGENAME.length() +1;
                } else {
                    classNameOffset = TYPEPACKAGENAME.length() +1;
                }

                resultList.add(InnerClass.getName().substring(classNameOffset));

                if (InnerClass.getDeclaredClasses() != null) {
                    addInnerClassNames(InnerClass, resultList);
                }
            }

        } catch (Exception e) {
            System.err.println("Failed to Iterate class: " + classToIterate.getSimpleName());
        }
    }

    public static List<String> getAllTypesList() {
        Set<Class<? extends Element>> allTypes = ModelSupport.getDataTypes();

        List<String> toAddTypesList = new ArrayList<String>();

        // Explicitly add the abstract supertypes
        toAddTypesList.add("Element");
        toAddTypesList.add("BackboneElement");

        for (Class<? extends Element> resourceTypeClass : allTypes) {
            String typeName = resourceTypeClass.getSimpleName();
            toAddTypesList.add(typeName);
            addInnerClassNames(resourceTypeClass, toAddTypesList);
        }

        return toAddTypesList;
    }

    public static List<String> getAllResourceInnerClasses() {
        List<String> allResourceInnerClassList = new ArrayList<String>();

        for (Class<?> resourceTypeClass : ModelSupport.getResourceTypes()) {
            try {
                addInnerClassNames(resourceTypeClass, allResourceInnerClassList);
            } catch (Exception e) {
                System.err.println("Failed to get resource: " + resourceTypeClass.getSimpleName());
            }
        }
        return allResourceInnerClassList;
    }

    /**
     * @return true if the type model class is applicable for the outer model class
     */
    public static boolean isApplicableForClass(Class<?> typeModelClass, Class<?> outerModelClass) {
        // if the resource class has a choice element (other than Extension.value[x])
        // with a type of "*" (any), then include it
        if (StructureMap.class == outerModelClass ||
                Task.class == outerModelClass) {
            return true;
        }

        boolean isApplicable = true;

        if (Dosage.class == typeModelClass ||
                Dosage.class == typeModelClass.getEnclosingClass()) {
            // Special case for Dosage
            if (outerModelClass != ActivityDefinition.class &&
                    outerModelClass != MedicationDispense.class &&
                    outerModelClass != MedicationKnowledge.class &&
                    outerModelClass != MedicationRequest.class &&
                    outerModelClass != MedicationStatement.class) {
                isApplicable = false;
            }
        } else if (ElementDefinition.class == typeModelClass ||
                ElementDefinition.class.equals(typeModelClass.getEnclosingClass()) ||
                ElementDefinition.Slicing.Discriminator.class == typeModelClass) {
            if (outerModelClass != StructureDefinition.class) {
                isApplicable = false;
            }
        } else if (ContactDetail.class == typeModelClass) {
            // TODO: introspect the resourceModelClass to tell if its truly applicable or not
            isApplicable = true;
        } else if (Contributor.class == typeModelClass) {
            // Not used anywhere yet
            isApplicable = false;
        } else if (DataRequirement.class == typeModelClass ||
                DataRequirement.class == typeModelClass.getEnclosingClass()) {
            if (outerModelClass != TriggerDefinition.class &&
                    outerModelClass != EvidenceVariable.class &&
                    outerModelClass != GuidanceResponse.class &&
                    outerModelClass != Library.class &&
                    outerModelClass != PlanDefinition.class &&
                    outerModelClass != ResearchElementDefinition.class) {
                isApplicable = false;
            }
        } else if (ParameterDefinition.class == typeModelClass ||
                ParameterDefinition.class == typeModelClass.getEnclosingClass()) {
            if (outerModelClass != ActivityDefinition.class &&
                    outerModelClass != MedicationDispense.class &&
                    outerModelClass != MedicationKnowledge.class &&
                    outerModelClass != MedicationRequest.class &&
                    outerModelClass != MedicationStatement.class) {
                isApplicable = false;
            }
        } else if (RelatedArtifact.class == typeModelClass) {
            // TODO: introspect the resourceModelClass to tell if its truly applicable or not
            isApplicable = true;
        } else if (TriggerDefinition.class == typeModelClass ||
                TriggerDefinition.class == typeModelClass.getEnclosingClass()) {
            if (outerModelClass != EventDefinition.class &&
                    outerModelClass != EvidenceVariable.class &&
                    outerModelClass != PlanDefinition.class) {
                isApplicable = false;
            }
        } else if (Expression.class == typeModelClass ||
                Expression.class == typeModelClass.getEnclosingClass()) {
            if (outerModelClass != ActivityDefinition.class &&
                    outerModelClass != TriggerDefinition.class &&
                    outerModelClass != ActivityDefinition.class &&
                    outerModelClass != EvidenceVariable.class &&
                    outerModelClass != Measure.class &&
                    outerModelClass != PlanDefinition.class &&
                    outerModelClass != RequestGroup.class &&
                    outerModelClass != ResearchElementDefinition.class) {
                isApplicable = false;
            }
        } else if (UsageContext.class == typeModelClass ||
                UsageContext.class == typeModelClass.getEnclosingClass()) {
            // TODO: introspect the resourceModelClass to tell if its truly applicable or not
            isApplicable = true;
        } else if (SubstanceAmount.class == typeModelClass ||
                SubstanceAmount.class == typeModelClass.getEnclosingClass()) {
            if (SubstancePolymer.class != outerModelClass) {
                isApplicable = false;
            }
        } else if (ProdCharacteristic.class == typeModelClass ||
                ProdCharacteristic.class == typeModelClass.getEnclosingClass()) {
            if (DeviceDefinition.class != outerModelClass &&
                    MedicinalProductManufactured.class != outerModelClass &&
                    MedicinalProductPackaged.class != outerModelClass) {
                isApplicable = false;
            }
        } else if (ProductShelfLife.class == typeModelClass ||
                ProductShelfLife.class == typeModelClass.getEnclosingClass()) {
            if (DeviceDefinition.class != outerModelClass &&
                    MedicinalProductPackaged.class != outerModelClass) {
                isApplicable = false;
            }
        } else if (MarketingStatus.class == typeModelClass ||
                MarketingStatus.class == typeModelClass.getEnclosingClass()) {
            if (DeviceDefinition.class != outerModelClass &&
                    MedicinalProductPackaged.class != outerModelClass) {
                isApplicable = false;
            }
        } else if (Population.class == typeModelClass ||
                Population.class == typeModelClass.getEnclosingClass()) {
            if (MedicinalProductContraindication.class != outerModelClass &&
                    MedicinalProductIndication.class != outerModelClass &&
                    MedicinalProductUndesirableEffect.class != outerModelClass) {
                isApplicable = false;
            }
        }

        return isApplicable;
    }
}
