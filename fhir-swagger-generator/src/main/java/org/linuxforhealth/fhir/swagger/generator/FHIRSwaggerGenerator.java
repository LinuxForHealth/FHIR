/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.swagger.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.CapabilityStatement;
import org.linuxforhealth.fhir.model.resource.DomainResource;
import org.linuxforhealth.fhir.model.resource.Group;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.type.BackboneElement;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.ElementDefinition;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Identifier;
import org.linuxforhealth.fhir.model.type.Oid;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uuid;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.model.visitor.AbstractVisitable;
import org.linuxforhealth.fhir.openapi.generator.FHIROpenApiGenerator;
import org.linuxforhealth.fhir.search.compartment.CompartmentHelper;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.util.SearchHelper;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonStructure;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;

/**
 * Generate Swagger 2.0 from the HL7 FHIR R4 artifacts and the IBM FHIR object model.
 *
 * <p>
 * By default, this class will create a separate Swagger definition for each resource type and compartment,
 * with all applicable HTTP interactions enabled.
 *
 * <p>
 * To limit the output to a given set of resources and/or interactions, pass a set of semicolon-delimited
 * filter strings of the form {@code ResourceType1(interaction1,interaction2)}.
 *
 * For example:
 * <pre>
 * Patient(create,update,read,vread,history,search);Contract(create,read,vread,history,search);RiskAssessment(read)
 * </pre>
 */
public class FHIRSwaggerGenerator {
    private static final String OUTDIR = "swagger";
    private static final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    private static final Map<Class<?>, StructureDefinition> structureDefinitionMap = buildStructureDefinitionMap();
    private static boolean includeDeleteOperation = true;

    public static final String TYPEPACKAGENAME = "org.linuxforhealth.fhir.model.type";
    public static final String RESOURCEPACKAGENAME = "org.linuxforhealth.fhir.model.resource";
    public static final String APPLICATION_FORM = "application/x-www-form-urlencoded";

    private static final CompartmentHelper compartmentHelper = new CompartmentHelper();
    private static final SearchHelper searchHelper = new SearchHelper();

    public static void main(String[] args) throws Exception {
        File file = new File(OUTDIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("Generating swagger definitions at " + file.getCanonicalPath());

        Filter filter = null;
        if (args.length == 1) {
            filter = createFilter(args[0]);
        } else {
            filter = createAcceptAllFilter();
        }

        List<String> classNames = FHIROpenApiGenerator.getClassNames();
        for (String resourceClassName : classNames) {
            Class<?> resourceModelClass = Class.forName(FHIROpenApiGenerator.RESOURCEPACKAGENAME + "." + resourceClassName);
            if (DomainResource.class.isAssignableFrom(resourceModelClass)
                    && DomainResource.class != resourceModelClass) {
                if (filter.acceptResourceType(resourceModelClass)) {

                    JsonObjectBuilder swagger = factory.createObjectBuilder();
                    swagger.add("swagger", "2.0");

                    JsonObjectBuilder info = factory.createObjectBuilder();
                    info.add("title", resourceClassName + " API");
                    info.add("description", "A simplified version of the HL7 FHIR API for " + resourceClassName + " resources.");
                    info.add("version", "4.0.1");
                    swagger.add("info", info);

                    swagger.add("basePath", "/fhir-server/api/v4");

                    // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
                    // with a default ExecuteInvoke Assembly
                    APIConnectAdapter.addApiConnectStuff(swagger);

                    JsonArrayBuilder tags = factory.createArrayBuilder();
                    JsonObjectBuilder paths = factory.createObjectBuilder();
                    JsonObjectBuilder definitions = factory.createObjectBuilder();

                    // generate Resource and DomainResource definitions
                    generateDefinition(Resource.class, definitions);
                    generateDefinition(DomainResource.class, definitions);

                    generatePaths(resourceModelClass, paths, filter);
                    JsonObjectBuilder tag = factory.createObjectBuilder();
                    tag.add("name", resourceModelClass.getSimpleName());
                    tags.add(tag);
                    generateDefinition(resourceModelClass, definitions);
                    // for search response
                    generateDefinition(Bundle.class, definitions);
                    // for error response
                    generateDefinition(OperationOutcome.class, definitions);

                    // generate definition for all inner classes inside the top level resource.
                    for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
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

                    // generate definition for all the applicable defined Types.
                    for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
                        Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                        if (FHIROpenApiGenerator.isApplicableForClass(typeModelClass, resourceModelClass)) {
                            generateDefinition(typeModelClass, definitions);
                        }
                    }

                    swagger.add("tags", tags);
                    swagger.add("paths", paths);

                    JsonObjectBuilder parameters = factory.createObjectBuilder();
                    generateParameters(parameters, filter);
                    JsonObject parametersObject = parameters.build();
                    if (!parametersObject.isEmpty()) {
                        swagger.add("parameters", parametersObject);
                    }
                    swagger.add("definitions", definitions);

                    Map<String, Object> config = new HashMap<String, Object>();
                    config.put(JsonGenerator.PRETTY_PRINTING, true);
                    JsonWriterFactory factory = Json.createWriterFactory(config);

                    File outFile = new File(OUTDIR + File.separator +  resourceClassName + "-swagger.json");
                    try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
                        writer.writeObject(swagger.build());
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                }

                // generate file for compartment
                generateCompartmentSwagger(resourceModelClass, filter);
            }
        }

        generateMetadataSwagger();
        generateBatchTransactionSwagger();
        generateWholeSystemHistorySwagger();
        if (filter.acceptOperation("export")) {
            generateExportSwagger(filter);
        }
    }

    private static void generateCompartmentSwagger(Class<?> compartmentModelClass, Filter filter) throws Exception, ClassNotFoundException, Error {
        String compartmentClassName = compartmentModelClass.getSimpleName();
        Set<String> resourceClassNames = getCompartmentClassNames(compartmentClassName);
        if (resourceClassNames == null || resourceClassNames.isEmpty()) {
            return;
        }

        JsonArrayBuilder tags = factory.createArrayBuilder();
        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();
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
                tag.add("name", resourceClassName);
                tags.add(tag);
                generateDefinition(resourceModelClass, definitions);

                // generate definition for all inner classes inside the top level resource.
                for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
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

                // generate definition for all the applicable defined Types.
                for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
                    Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
                    if (FHIROpenApiGenerator.isApplicableForClass(typeModelClass, resourceModelClass)) {
                        generateDefinition(typeModelClass, definitions);
                    }
                }
            }
        }

        if (!addedContent) {
            return;
        }

        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", compartmentClassName + " compartment API");
        info.add("description", "A simplified version of the HL7 FHIR API for " + compartmentClassName + " compartment.");
        info.add("version", "4.0.1");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

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

        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateCompartmentSearchParameters(parameters, filter);
        JsonObject parametersObject = parameters.build();
        if (!parametersObject.isEmpty()) {
            swagger.add("parameters", parametersObject);
        }
        swagger.add("definitions", definitions);

        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator +  compartmentClassName + "-compartment-swagger.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateMetadataSwagger() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Capabilities");
        info.add("description", "The capabilities interaction retrieves the information about a server's capabilities - which portions of the HL7 FHIR specification it supports.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR capabilities interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateMetadataPathItem(path);
        paths.add("/metadata", path);
        swagger.add("paths", paths);


        generateDefinition(CapabilityStatement.class, definitions);
        generateDefinition(Resource.class, definitions);
        generateDefinition(DomainResource.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (CapabilityStatement.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the applicable data types.
        for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            if (FHIROpenApiGenerator.isApplicableForClass(typeModelClass, CapabilityStatement.class)) {
                generateDefinition(typeModelClass, definitions);
            }
        }

        swagger.add("definitions", definitions);


        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "metadata-swagger.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateWholeSystemHistorySwagger() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Whole System History");
        info.add("description", "The whole system history interaction can be used to obtain a list of changes (create, update, delete) to resources in the IBM FHIR Server.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

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

        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateWholeSystemHistoryParameters(parameters, true);
        JsonObject parametersObject = parameters.build();
        if (!parametersObject.isEmpty()) {
            swagger.add("parameters", parametersObject);
        }

        swagger.add("definitions", definitions);


        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "wholeSystemHistory-swagger.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateBatchTransactionSwagger() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Batch/Transaction");
        info.add("description", "The batch and transaction interactions submit a set of actions to perform on a server in a single HTTP request/response.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR batch/transaction interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateBatchPathItem(path);
        paths.add("/", path);
        swagger.add("paths", paths);


        generateDefinition(Bundle.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (Bundle.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the defined Types.
        for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            generateDefinition(typeModelClass, definitions);
        }

        swagger.add("definitions", definitions);


        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "batch-swagger.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static void generateExportSwagger(Filter filter) throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Bulk Data Export");
        info.add("description", "Export Data from the FHIR server.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

        // Set the hostname in APIConnectAdapter and uncomment this to add "x-ibm-configuration"
        // with a default ExecuteInvoke Assembly
        APIConnectAdapter.addApiConnectStuff(swagger);

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // System export
        if (filter.acceptOperation(Resource.class, "export")) {
            JsonObjectBuilder path = factory.createObjectBuilder();
            generateExportPathItem(path, "System", "Export data from the FHIR server", false, "get", false);
            generateExportPathItem(path, "System", "Export data from the FHIR server", false, "post", false);
            paths.add("/$export", path);
        }

        // Patient and Group export
        for (Class<?> modelClass : FHIROpenApiGenerator.DATA_EXPORT_TYPES) {
            if (filter.acceptOperation(modelClass, "export")) {
                // generate definition for all inner classes inside the top level resources.
                for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
                    Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
                    if (Parameters.class.equals(parentClass)) {
                        Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                        generateDefinition(innerModelClass, definitions);
                    }
                }

                // Add Export
                String typeName = modelClass.getSimpleName();
                JsonObjectBuilder path = factory.createObjectBuilder();
                generateExportPathItem(path, typeName, "Export " + typeName + " data from the FHIR server", true, "get", "Group".equals(typeName));
                generateExportPathItem(path, typeName, "Export " + typeName + " data from the FHIR server", true, "post", "Group".equals(typeName));
                JsonObject pathObject = path.build();
                StringBuilder pathStringBuilder = new StringBuilder();
                pathStringBuilder.append("/");
                pathStringBuilder.append(typeName);
                if (modelClass.getSimpleName().equals("Group"))
                    pathStringBuilder.append("/{id}");
                pathStringBuilder.append("/$export");

                paths.add(pathStringBuilder.toString(), pathObject);
            }
        }

        // Add bulkdata-status
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateBulkDataStatusPathItem(path);
        paths.add("/$bulkdata-status", path);

        swagger.add("paths", paths);

        generateDefinition(Parameters.class, definitions);
        generateDefinition(Resource.class, definitions);

        // generate definition for all inner classes inside the top level resources.
        for (String innerClassName : FHIROpenApiGenerator.getAllResourceInnerClasses()) {
            Class<?> parentClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName.split("\\$")[0]);
            if (Parameters.class.equals(parentClass)) {
                Class<?> innerModelClass = Class.forName(RESOURCEPACKAGENAME + "." + innerClassName);
                generateDefinition(innerModelClass, definitions);
            }
        }

        // generate definition for all the applicable data types.
        for (String typeClassName : FHIROpenApiGenerator.getAllTypesList()) {
            Class<?> typeModelClass = Class.forName(TYPEPACKAGENAME + "." + typeClassName);
            if (FHIROpenApiGenerator.isApplicableForClass(typeModelClass, Parameters.class)) {
                generateDefinition(typeModelClass, definitions);
            }
        }

        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateExportParameters(parameters, filter.acceptOperation(Group.class, "export"));
        JsonObject parametersObject = parameters.build();
        if (!parametersObject.isEmpty()) {
            swagger.add("parameters", parametersObject);
        }

        swagger.add("definitions", definitions);


        Map<String, Object> config = new HashMap<String, Object>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory factory = Json.createWriterFactory(config);

        File outFile = new File(OUTDIR + File.separator + "export-swagger.json");
        try (JsonWriter writer = factory.createWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8)) {
            writer.writeObject(swagger.build());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    private static Map<Class<?>, StructureDefinition> buildStructureDefinitionMap() {
        Map<Class<?>, StructureDefinition> structureDefinitionMap = new HashMap<Class<?>, StructureDefinition>();
        try {
            FHIROpenApiGenerator.populateStructureDefinitionMap(structureDefinitionMap, "profiles-resources.json");
            FHIROpenApiGenerator.populateStructureDefinitionMap(structureDefinitionMap, "profiles-types.json");
        } catch (Exception e) {
            throw new Error(e);
        }
        return structureDefinitionMap;
    }

    private static void generateParameters(JsonObjectBuilder parameters, Filter filter) throws Exception {
        if (filter.acceptOperation("read") || filter.acceptOperation("vread") || filter.acceptOperation("update")
                || filter.acceptOperation("delete") || filter.acceptOperation("history")) {
            JsonObjectBuilder id = factory.createObjectBuilder();
            id.add("name", "id");
            id.add("description", "logical identifier");
            id.add("in", "path");
            id.add("required", true);
            id.add("type", "string");
            parameters.add("idParam", id);
        }
        if (filter.acceptOperation("history")) {
            generateWholeSystemHistoryParameters(parameters, false);
        }
        if (filter.acceptOperation("vread")) {
            JsonObjectBuilder vid = factory.createObjectBuilder();
            vid.add("name", "vid");
            vid.add("description", "version identifier");
            vid.add("in", "path");
            vid.add("required", true);
            vid.add("type", "string");
            parameters.add("vidParam", vid);
        }
        if (filter.acceptOperation("search")) {
            for (Entry<String, SearchParameter> entry : searchHelper.getSearchParameters(Resource.class.getSimpleName()).entrySet()) {
                SearchParameter searchParameter = entry.getValue();

                JsonObjectBuilder parameter = factory.createObjectBuilder();
                String name = entry.getKey();
                parameter.add("name", name);
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("required", false);
                parameter.add("type", "string");
                parameters.add(name + "Param", parameter);
            }
        }
    }

    private static void generateCompartmentSearchParameters(JsonObjectBuilder parameters, Filter filter) throws Exception {
        if (filter.acceptOperation("search")) {
            JsonObjectBuilder id = factory.createObjectBuilder();
            id.add("name", "id");
            id.add("description", "logical identifier");
            id.add("in", "path");
            id.add("required", true);
            id.add("type", "string");
            parameters.add("idParam", id);
            for (Entry<String, SearchParameter> entry : searchHelper.getSearchParameters(Resource.class.getSimpleName()).entrySet()) {
                SearchParameter searchParameter = entry.getValue();

                JsonObjectBuilder parameter = factory.createObjectBuilder();
                String name = searchParameter.getName().getValue();
                parameter.add("name", entry.getKey());
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("required", false);
                parameter.add("type", "string");
                parameters.add(name + "Param", parameter);
            }
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

        sort.add("type", "string");
        JsonArrayBuilder enums = factory.createArrayBuilder();
        enums.add("-_lastUpdated");
        enums.add("_lastUpdated");
        enums.add("none");
        sort.add("enum", enums);

        parameters.add("_sortParam", sort);

        // add _since=<timestamp>
        JsonObjectBuilder since = factory.createObjectBuilder();
        since.add("name", "_since");
        since.add("description", "Return changes since a known timestamp");
        since.add("in", "query");
        since.add("required", false);
        since.add("type", "string");

        parameters.add("_sinceParam", since);

        // add _before=<timestamp>
        JsonObjectBuilder before = factory.createObjectBuilder();
        before.add("name", "_before");
        before.add("description", "Return changes before a known timestamp");
        before.add("in", "query");
        before.add("required", false);
        before.add("type", "string");

        parameters.add("_beforeParam", before);

        // add _count
        JsonObjectBuilder count = factory.createObjectBuilder();
        count.add("name", "_count");
        count.add("description", "Specifies a maximum number of results that are required");
        count.add("in", "query");
        count.add("required", false);
        count.add("type", "integer");
        count.add("default", 100);

        parameters.add("_countParam", count);

        // add _type
        if (includeType) {
            JsonObjectBuilder type = factory.createObjectBuilder();
            type.add("name", "_type");
            type.add("description", "Limit which resource types are returned");
            type.add("in", "query");
            type.add("required", false);
            type.add("type", "string");

            parameters.add("_typeParam", type);
        }
    }

    private static void generateExportParameters(JsonObjectBuilder parameters, boolean includePathIdParam) throws Exception {
        JsonObjectBuilder outputFormat = factory.createObjectBuilder();
        outputFormat.add("name", "_outputFormat");
        outputFormat.add("description", "The format for the requested bulk data files to be generated");
        outputFormat.add("in", "query");
        outputFormat.add("required", false);
        outputFormat.add("type", "string");

        parameters.add("_outputFormatParam", outputFormat);

        // add _since=<timestamp>
        JsonObjectBuilder since = factory.createObjectBuilder();
        since.add("name", "_since");
        since.add("description", "Resources updated after this period will be included in the response.");
        since.add("in", "query");
        since.add("required", false);
        since.add("type", "string");

        parameters.add("_sinceParam", since);

        JsonObjectBuilder type = factory.createObjectBuilder();
        type.add("name", "_type");
        type.add("description", "Limit which resource types are returned");
        type.add("in", "query");
        type.add("required", false);
        type.add("type", "string");

        parameters.add("_typeParam", type);

        if (includePathIdParam) {
            JsonObjectBuilder id = factory.createObjectBuilder();
            id.add("name", "id");
            id.add("description", "logical identifier");
            id.add("in", "path");
            id.add("required", true);
            id.add("type", "string");
            parameters.add("idParam", id);
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

        // Add Patient Everything
        path = factory.createObjectBuilder();
        if (filter.acceptOperation(modelClass, "everything") && "Patient".equals(modelClass.getSimpleName())) {
            generatePatientEverythingPathItem(path, false);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/Patient/$everything", pathObject);
        }

        path = factory.createObjectBuilder();
        if (filter.acceptOperation(modelClass, "everything") && "Patient".equals(modelClass.getSimpleName())) {
            generatePatientEverythingPathItem(path, true);
        }
        pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/Patient/{id}/$everything", pathObject);
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

        JsonArrayBuilder consumes = factory.createArrayBuilder();
        consumes.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        post.add("consumes", consumes);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder bodyParameter = factory.createObjectBuilder();

        bodyParameter.add("name", "body");
        bodyParameter.add("in", "body");
        bodyParameter.add("required", true);

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/" + modelClass.getSimpleName());
        bodyParameter.add("schema", schema);

        parameters.add(bodyParameter);
        post.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Create " + modelClass.getSimpleName() + " operation successful");
        responses.add("201", response);
        post.add("responses", responses);

        path.add("post", post);
    }

    private static void generateReadPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Read" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "read" + modelClass.getSimpleName());

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/parameters/idParam");
        parameters.add(idParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Read " + modelClass.getSimpleName() + " operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/" + modelClass.getSimpleName());

        response.add("schema", schema);
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

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/parameters/idParam");
        parameters.add(idParamRef);

        JsonObjectBuilder vidParamRef = factory.createObjectBuilder();
        vidParamRef.add("$ref", "#/parameters/vidParam");
        parameters.add(vidParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Versioned read " + modelClass.getSimpleName() + " operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/" + modelClass.getSimpleName());

        response.add("schema", schema);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateUpdatePathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder put = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        put.add("tags", tags);
        put.add("summary", "Update an existing " + modelClass.getSimpleName() + " resource");
        put.add("operationId", "update" + modelClass.getSimpleName());

        JsonArrayBuilder consumes = factory.createArrayBuilder();
        consumes.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        put.add("consumes", consumes);

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/parameters/idParam");
        parameters.add(idParamRef);

        JsonObjectBuilder bodyParameter = factory.createObjectBuilder();
        bodyParameter.add("name", "body");
        bodyParameter.add("in", "body");
        bodyParameter.add("required", true);

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/" + modelClass.getSimpleName());
        bodyParameter.add("schema", schema);

        parameters.add(bodyParameter);
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
        idParamRef.add("$ref", "#/parameters/idParam");
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

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // For compartment search, include parameter for {id}
        if (compartmentModelClass != null) {
            JsonObjectBuilder idParamRef = factory.createObjectBuilder();
            idParamRef.add("$ref", "#/parameters/idParam");
            parameters.add(idParamRef);
        }
        generateSearchParameters(modelClass, parameters);
        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Search " + modelClass.getSimpleName() + " operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
        responses.add("200", response);
        get.add("responses", responses);

        path.add("get", get);
    }

    private static void generateSearchParameters(Class<?> modelClass, JsonArrayBuilder parameters) throws Exception {
        for (Entry<String, SearchParameter> entry : searchHelper.getSearchParameters(modelClass.getSimpleName()).entrySet()) {
            SearchParameter searchParameter = entry.getValue();

            JsonObjectBuilder parameter = factory.createObjectBuilder();
            String name = entry.getKey();
            if (name.startsWith("_")) {
                parameter.add("$ref", "#/parameters/" + name + "Param");
            } else {
                parameter.add("name", name);
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("type", "string");
                parameter.add("required", false);
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

        JsonArrayBuilder consumes = factory.createArrayBuilder();
        consumes.add(APPLICATION_FORM);
        post.add("consumes", consumes);

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        post.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // For compartment search, include parameter for {id}
        if (compartmentModelClass != null) {
            JsonObjectBuilder idParamRef = factory.createObjectBuilder();
            idParamRef.add("$ref", "#/parameters/idParam");
            parameters.add(idParamRef);
        }
        generateSearchParameters(modelClass, parameters);
        generateSearchFormParameters(modelClass, parameters);
        post.add("parameters", parameters);

        JsonObjectBuilder schema = factory.createObjectBuilder();
        JsonObjectBuilder responses = factory.createObjectBuilder();
        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Search " + modelClass.getSimpleName() + " operation successful");

        schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
        responses.add("200", response);
        post.add("responses", responses);

        path.add("post", post);
    }

    private static void generateSearchFormParameters(Class<?> modelClass, JsonArrayBuilder parameters) throws Exception {
        for (Entry<String, SearchParameter> entry : searchHelper.getSearchParameters(modelClass.getSimpleName()).entrySet()) {
            SearchParameter searchParameter = entry.getValue();

            JsonObjectBuilder parameter = factory.createObjectBuilder();
            String name = entry.getKey();
            parameter.add("name", name);
            parameter.add("description", searchParameter.getDescription().getValue());
            parameter.add("in", "formData");
            parameter.add("type", "string");
            parameters.add(parameter);
        }
    }

    private static void generateHistoryPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Return the history of" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "history" + modelClass.getSimpleName());

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/parameters/idParam");
        parameters.add(idParamRef);

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "History " + modelClass.getSimpleName() + " operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
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

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "metadata operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/CapabilityStatement");

        response.add("schema", schema);
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

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        // Add parameters to api call
        JsonObjectBuilder sortParameter = factory.createObjectBuilder();
        sortParameter.add("$ref", "#/parameters/_sortParam");
        parameters.add(sortParameter);

        JsonObjectBuilder sinceParameter = factory.createObjectBuilder();
        sinceParameter.add("$ref", "#/parameters/_sinceParam");
        parameters.add(sinceParameter);

        JsonObjectBuilder beforeParameter = factory.createObjectBuilder();
        beforeParameter.add("$ref", "#/parameters/_beforeParam");
        parameters.add(beforeParameter);

        JsonObjectBuilder countParameter = factory.createObjectBuilder();
        countParameter.add("$ref", "#/parameters/_countParam");
        parameters.add(countParameter);

        if (!resourceSpecific) {
            JsonObjectBuilder typeParameter = factory.createObjectBuilder();
            typeParameter.add("$ref", "#/parameters/_typeParam");
            parameters.add(typeParameter);
        }

        get.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "whole system history operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
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

        JsonArrayBuilder consumes = factory.createArrayBuilder();
        consumes.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        post.add("consumes", consumes);

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder bodyParameter = factory.createObjectBuilder();

        bodyParameter.add("name", "body");
        bodyParameter.add("in", "body");
        bodyParameter.add("required", true);

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");
        bodyParameter.add("schema", schema);

        parameters.add(bodyParameter);
        post.add("parameters", parameters);

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        post.add("produces", produces);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "batch operation successful");

        schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
        responses.add("200", response);
        post.add("responses", responses);

        path.add("post", post);
    }

    /**
     * Common method used for both the general export function and the Patient or Group specific function.
     * Expected tag parameter is set to "other" for non resource specific generation.  Tags "Patient" or "Group" for resource specific generation.
     * HttpMethod parameter is "get" or "post".
     *
     * @param path
     * @param type
     * @param summary
     * @param resourceSpecific
     * @param httpMethod
     */
    private static void generateExportPathItem(JsonObjectBuilder path, String type, String summary, boolean resourceSpecific, String httpMethod, boolean addIdParam) {
        JsonObjectBuilder httpMethodBuilder = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add("Bulk Data");

        httpMethodBuilder.add("tags", tags);
        httpMethodBuilder.add("summary", summary);

        String operationId = httpMethod + "Export";
        if (resourceSpecific) {
            operationId = operationId + type;
        }
        httpMethodBuilder.add("operationId", operationId);

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        if (addIdParam) {
            JsonObjectBuilder idParameter = factory.createObjectBuilder();
            idParameter.add("$ref", "#/parameters/idParam");
            parameters.add(idParameter);
        }

        if ("post".equalsIgnoreCase(httpMethod)) {
            JsonArrayBuilder consumes = factory.createArrayBuilder();
            consumes.add(FHIRMediaType.APPLICATION_FHIR_JSON);
            httpMethodBuilder.add("consumes", consumes);

            // Generate POST Parameters
            JsonObjectBuilder body = factory.createObjectBuilder();
            body.add("name", "body");
            body.add("description", "The Parameters for the bulk data export request.");
            body.add("in", "body");
            body.add("required", true);
            JsonObjectBuilder schema = factory.createObjectBuilder();
            schema.add("$ref", "#/definitions/Parameters");
            body.add("schema", schema);

            parameters.add(body);
       } else {
            // Generate GET parameters
            JsonObjectBuilder outputFormatParameter = factory.createObjectBuilder();
            outputFormatParameter.add("$ref", "#/parameters/_outputFormatParam");
            parameters.add(outputFormatParameter);

            JsonObjectBuilder sinceParameter = factory.createObjectBuilder();
            sinceParameter.add("$ref", "#/parameters/_sinceParam");
            parameters.add(sinceParameter);

            JsonObjectBuilder typeParameter = factory.createObjectBuilder();
            typeParameter.add("$ref", "#/parameters/_typeParam");
            parameters.add(typeParameter);
        }

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_NDJSON);
        httpMethodBuilder.add("produces", produces);

        httpMethodBuilder.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Export job is initiated");

        // Add Response headers
        JsonObjectBuilder headers = factory.createObjectBuilder();
        JsonObjectBuilder contentLocation = factory.createObjectBuilder();
        contentLocation.add("description", "The polling location");
        contentLocation.add("type", "string");

        headers.add("Content-Location", contentLocation);
        response.add("headers", headers);

        responses.add("202", response);
        httpMethodBuilder.add("responses", responses);

        path.add(httpMethod, httpMethodBuilder);
    }

    /**
     *
     * @param path
     */
    private static void generateBulkDataStatusPathItem(JsonObjectBuilder path) {
        JsonObjectBuilder httpMethodBuilder = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add("Bulk Data");

        httpMethodBuilder.add("tags", tags);
        httpMethodBuilder.add("summary", "Retrieves the status of the buld data request");
        httpMethodBuilder.add("operationId", "bulkDataStatus");

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        // Generate GET parameters
        JsonObjectBuilder jobParameter = factory.createObjectBuilder();
        jobParameter.add("name", "job");
        jobParameter.add("description", "Job id from the Content-Location header response value after the export request");
        jobParameter.add("in", "query");
        jobParameter.add("required", true);
        jobParameter.add("type", "string");
        parameters.add(jobParameter);

        httpMethodBuilder.add("parameters", parameters);

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add("application/json");
        httpMethodBuilder.add("produces", produces);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        // 202 not completed
        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Export job is queued and not yet complete");
        responses.add("202", response);

        // Add 200 completed
        response = factory.createObjectBuilder();
        response.add("description", "Export job completed");

        // add response body
        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("type", "object");
        JsonObjectBuilder properties = factory.createObjectBuilder();
        JsonObjectBuilder transactionTime = factory.createObjectBuilder();
        transactionTime.add("type", "string");
        properties.add("transactionTime", transactionTime);

        JsonObjectBuilder request = factory.createObjectBuilder();
        request.add("type", "string");
        properties.add("request", request);

        JsonObjectBuilder requiresAccessToken = factory.createObjectBuilder();
        requiresAccessToken.add("type", "boolean");
        properties.add("requiresAccessToken", requiresAccessToken);

        JsonObjectBuilder output = factory.createObjectBuilder();
        output.add("type", "array");
        JsonObjectBuilder items = factory.createObjectBuilder();
        items.add("type", "object");
        JsonObjectBuilder itemProperties = factory.createObjectBuilder();

        JsonObjectBuilder outputArrayEntryTypeProperty = factory.createObjectBuilder();
        outputArrayEntryTypeProperty.add("type", "string");
        itemProperties.add("type", outputArrayEntryTypeProperty);

        JsonObjectBuilder outputArrayEntryUrlProperty = factory.createObjectBuilder();
        outputArrayEntryUrlProperty.add("type", "string");
        itemProperties.add("url", outputArrayEntryUrlProperty);

        JsonObjectBuilder outputArrayEntryCountProperty = factory.createObjectBuilder();
        outputArrayEntryCountProperty.add("type", "integer");
        itemProperties.add("count", outputArrayEntryCountProperty);

        items.add("properties", itemProperties);
        output.add("items", items);
        properties.add("output", output);

        schema.add("properties", properties);
        response.add("schema", schema);
        responses.add("200", response);
        httpMethodBuilder.add("responses", responses);

        path.add("get", httpMethodBuilder);
    }

    /**
     * Generates the $everything path for a Patient resource
     *
     * @param path
     * @param addIdParam
     */
    private static void generatePatientEverythingPathItem(JsonObjectBuilder path, boolean addIdParam) {
        JsonObjectBuilder httpMethodBuilder = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add("Patient");
        httpMethodBuilder.add("tags", tags);

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        if (addIdParam) {
            httpMethodBuilder.add("summary", "Return all the information related to a given patient");
            httpMethodBuilder.add("operationId", "everythingPatient");

            JsonObjectBuilder idParameter = factory.createObjectBuilder();
            idParameter.add("$ref", "#/parameters/idParam");
            parameters.add(idParameter);
        } else {
            httpMethodBuilder.add("summary", "Return all the information related to one or more patients");
            httpMethodBuilder.add("operationId", "everythingAllPatients");
        }

        JsonObjectBuilder startParam = factory.createObjectBuilder();
        startParam.add("name", "start");
        startParam.add("description", "All records relating to care provided in a certain date range");
        startParam.add("in", "query");
        startParam.add("required", false);
        startParam.add("type", "string");
        parameters.add(startParam);

        JsonObjectBuilder endParam = factory.createObjectBuilder();
        endParam.add("name", "end");
        endParam.add("description", "All records relating to care provided in a certain date range");
        endParam.add("in", "query");
        endParam.add("required", false);
        endParam.add("type", "string");
        parameters.add(endParam);

        JsonObjectBuilder type = factory.createObjectBuilder();
        type.add("name", "_type");
        type.add("description", "Limit which resource types are returned");
        type.add("in", "query");
        type.add("required", false);
        type.add("type", "string");

        parameters.add(type);

        httpMethodBuilder.add("parameters", parameters);

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        httpMethodBuilder.add("produces", produces);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "everything operation successful");

        JsonObjectBuilder schema = factory.createObjectBuilder();
        schema.add("$ref", "#/definitions/Bundle");

        response.add("schema", schema);
        responses.add("200", response);
        httpMethodBuilder.add("responses", responses);

        path.add("get", httpMethodBuilder);
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
                // add the 'resourceType' property
                JsonObjectBuilder property = factory.createObjectBuilder();

                // Convert all the primitive types to json types.
                property.add("type", "string");
                if (Resource.class == modelClass) {
                    // TODO: when a filter was passed, limit this to just the resource types included in the filter
                    List<String> typeNames = FHIROpenApiGenerator.getClassNames();
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
                    if (!ModelSupport.isChoiceElement(modelClass, ModelSupport.getElementName(field)) && field.isAnnotationPresent(Required.class)) {
                        required.add(ModelSupport.getElementName(field));
                    }
                    generateProperties(structureDefinition, modelClass, field, properties);
                }
            }

            JsonArray requiredArray = required.build();

            Class<?> superClass = modelClass.getSuperclass();
            if (superClass != null
                    && superClass.getPackage().getName().startsWith("org.linuxforhealth.fhir.model")
                    && !superClass.equals(AbstractVisitable.class)) {
                JsonArrayBuilder allOf = factory.createArrayBuilder();

                JsonObjectBuilder ref = factory.createObjectBuilder();
                ref.add("$ref", "#/definitions/" + superClass.getSimpleName());
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
                    definition.add("discriminator", "resourceType");
                }
                definition.add("properties", properties);
                if (!requiredArray.isEmpty()) {
                    definition.add("required", requiredArray);
                }
            }

            if (Resource.class.isAssignableFrom(modelClass)) {
                FHIROpenApiGenerator.addExamples(modelClass, definition);
            }

            definitions.add(getSimpleNameWithEnclosingNames(modelClass), definition);
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
                structureDefinition = structureDefinitionMap.get(Class.forName(FHIROpenApiGenerator.TYPEPACKAGENAME + "." + "Code"));
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
            if (modelClass.getName().contains(FHIROpenApiGenerator.TYPEPACKAGENAME)) {
                modelClassName = modelClass.getName().substring(FHIROpenApiGenerator.TYPEPACKAGENAME.length()+1);
            } else {
                modelClassName = modelClass.getName().substring(FHIROpenApiGenerator.RESOURCEPACKAGENAME.length()+1);
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
                if (FHIROpenApiGenerator.isApplicableForClass(choiceType, modelClass)) {
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
        } else if (org.linuxforhealth.fhir.model.type.String.class.isAssignableFrom(fieldClass)) {
            property.add("type", "string");
            if (org.linuxforhealth.fhir.model.type.Code.class.isAssignableFrom(fieldClass)) {
                property.add("pattern", "[^\\s]+(\\s[^\\s]+)*");
            } else if (org.linuxforhealth.fhir.model.type.Id.class.equals(fieldClass)) {
                property.add("pattern", "[A-Za-z0-9\\-\\.]{1,64}");
            } else {
                property.add("pattern", "[ \\r\\n\\t\\S]+");
            }
        } else if (org.linuxforhealth.fhir.model.type.Uri.class.isAssignableFrom(fieldClass)) {
            property.add("type", "string");
            if (Oid.class.equals(fieldClass)) {
                property.add("pattern", "urn:oid:[0-2](\\.(0|[1-9][0-9]*))+");
            } else if (Uuid.class.equals(fieldClass)) {
                property.add("pattern", "urn:uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
            } else {
                property.add("pattern", "\\S*");
            }
        } else if (org.linuxforhealth.fhir.model.type.Boolean.class.equals(fieldClass)) {
            property.add("type", "boolean");
        } else if (org.linuxforhealth.fhir.model.type.Instant.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern", "([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)-(0[1-9]"
                    + "|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                    + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))");
        } else if (org.linuxforhealth.fhir.model.type.Time.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?");
        } else if (org.linuxforhealth.fhir.model.type.Date.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)"
                    + "|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?");
        } else if (org.linuxforhealth.fhir.model.type.DateTime.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]"
                    + "|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                    + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?");
        } else if (org.linuxforhealth.fhir.model.type.Decimal.class.equals(fieldClass)) {
            property.add("type", "number");
        } else if (org.linuxforhealth.fhir.model.type.Integer.class.isAssignableFrom(fieldClass)) {
            property.add("type", "integer");
            property.add("format", "int32");
        } else if (org.linuxforhealth.fhir.model.type.Base64Binary.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","(\\s*([0-9a-zA-Z\\+/=]){4}\\s*)+");
        } else if (String.class.equals(fieldClass)) {
            property.add("type", "string");
            if ("id".equals(elementName)) {
                property.add("pattern", "[A-Za-z0-9\\-\\.]{1,64}");
            } else if ("url".equals(elementName)) {
                property.add("pattern", "\\S*");
            }
        } else if (org.linuxforhealth.fhir.model.type.Xhtml.class.equals(fieldClass)) {
            property.add("type", "string");
        } else {
            // For non-primitives, point to the corresponding definition
            property.add("$ref", "#/definitions/" + getSimpleNameWithEnclosingNames(fieldClass));
        }

        if (description != null) {
            property.add("description", description);
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

    private static Set<String> getCompartmentClassNames(String compartment) {
        try {
            return compartmentHelper.getCompartmentResourceTypes(compartment);
        } catch (FHIRSearchException e) {
            return Collections.emptySet();
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

        /**
         * @return true if the operation is enables for the specified resourceType, otherwise false
         */
        public boolean acceptResourceType(Class<?> resourceType) {
            return filterMap.containsKey(resourceType.getSimpleName());
        }

        /**
         * @return true if one or more of the resources in the filterMap includes the passed operation, otherwise false
         */
        public boolean acceptOperation(Class<?> resourceType, String operation) {
            List<String> interactions = filterMap.get(resourceType.getSimpleName());
            return interactions != null && interactions.contains(operation);
        }

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
        for (String className : FHIROpenApiGenerator.getClassNames()) {
            Class<?> modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
            if (Resource.class == modelClass) {
                filterMap.put(className, List.of("search", "history", "batch", "transaction", "export"));
            } else if (DomainResource.class.isAssignableFrom(modelClass)) {
                String resourceType = className;
                // TODO: add patch
                List<String> operationList = Arrays.asList("create", "read", "vread", "update", "delete", "search",
                        "history", "export", "everything");
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
}
