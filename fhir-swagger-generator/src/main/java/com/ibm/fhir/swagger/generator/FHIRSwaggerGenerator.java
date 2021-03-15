/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.swagger.generator;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.AbstractVisitable;
import com.ibm.fhir.openapi.generator.FHIROpenApiGenerator;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Generate Swagger 2.0 from the HL7 FHIR R4 artifacts and the IBM FHIR object model.
 *
 * <p>
 * By default, this class will create a separate Swagger definition for each and every resource type;
 * each with all HTTP interactions enabled.
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
    public static final String TYPEPACKAGENAME = "com.ibm.fhir.model.type";
    public static final String RESOURCEPACKAGENAME = "com.ibm.fhir.model.resource";

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

        List<String> classNames = getClassNames();
        for (String resourceClassName : classNames) {
            Class<?> resourceModelClass = Class.forName(FHIROpenApiGenerator.RESOURCEPACKAGENAME + "." + resourceClassName);
            if (DomainResource.class.isAssignableFrom(resourceModelClass)
                    && DomainResource.class != resourceModelClass
                    && filter.acceptResourceType(resourceModelClass)) {

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
        }

        generateMetadataSwagger();
        generateBatchTransactionSwagger();
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

        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();

        // FHIR capabilities interaction
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateMetadataPathItem(path);
        paths.add("/metadata", path);
        swagger.add("paths", paths);


        generateDefinition(CapabilityStatement.class, definitions);

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

    private static void generateBatchTransactionSwagger() throws Exception, ClassNotFoundException, Error {
        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("swagger", "2.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "Batch/Transaction");
        info.add("description", "The batch and transaction interactions submit a set of actions to perform on a server in a single HTTP request/response.");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        swagger.add("basePath", "/fhir-server/api/v4");

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
            for (SearchParameter searchParameter : SearchUtil.getApplicableSearchParameters(Resource.class.getSimpleName())) {
                JsonObjectBuilder parameter = factory.createObjectBuilder();
                String name = searchParameter.getName().getValue();
                parameter.add("name", name);
                parameter.add("description", searchParameter.getDescription().getValue());
                parameter.add("in", "query");
                parameter.add("required", false);
                parameter.add("type", "string");
                parameters.add(name + "Param", parameter);
            }
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
            generateSearchPathItem(modelClass, path);
        }
        JsonObject pathObject = path.build();
        if (!pathObject.isEmpty()) {
            paths.add("/" + modelClass.getSimpleName(), pathObject);
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

        // TODO: add patch
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

    private static void generateSearchPathItem(Class<?> modelClass, JsonObjectBuilder path) throws Exception {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Search for " + modelClass.getSimpleName() + " resources");
        get.add("operationId", "search" + modelClass.getSimpleName());

        JsonArrayBuilder produces = factory.createArrayBuilder();
        produces.add(FHIRMediaType.APPLICATION_FHIR_JSON);
        get.add("produces", produces);

        JsonArrayBuilder parameters = factory.createArrayBuilder();

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
        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>(
                SearchUtil.getApplicableSearchParameters(modelClass.getSimpleName()));
        for (SearchParameter searchParameter : searchParameters) {
            JsonObjectBuilder parameter = factory.createObjectBuilder();
            String name = searchParameter.getName().getValue();
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
                    List<String> typeNames = Arrays.stream(ResourceType.ValueSet.values()).map(ResourceType.ValueSet::value).collect(Collectors.toList());
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
                    && superClass.getPackage().getName().startsWith("com.ibm.fhir.model")
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
            property.add("pattern","(\\s*([0-9a-zA-Z\\+\\=]){4}\\s*)+");
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

    private static List<String> getClassNames() {
        return FHIRUtil.getResourceTypeNames();
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
            return filterMap.get(resourceType.getSimpleName()).contains(operation);
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
}
