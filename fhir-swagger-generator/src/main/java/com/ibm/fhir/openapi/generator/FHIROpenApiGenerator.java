/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.openapi.generator;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.visitor.AbstractVisitable;
import com.ibm.fhir.search.util.SearchUtil;

public class FHIROpenApiGenerator {
    private static final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    private static final Map<Class<?>, StructureDefinition> structureDefinitionMap = buildStructureDefinitionMap();
    private static boolean includeDeleteOperation = false;
    public static final String TYPEPACKAGENAME = "com.ibm.fhir.model.type";
    public static final String RESOURCEPACKAGENAME = "com.ibm.fhir.model.resource";

    public static void main(String[] args) throws Exception {
        /*
         * for (Class<?> key : structureDefinitionMap.keySet()) { StructureDefinition
         * structureDefinition = structureDefinitionMap.get(key);
         * System.out.println("key: " + key + ", value: " +
         * structureDefinition.getUrl().getValue()); }
         */

        Filter filter = null;
        if (args.length == 1) {
            filter = createFilter(args[0]);
        } else {
            filter = createAcceptAllFilter();
        }
        // filter =
        // createFilter("Patient(create,read);Contract(create,read);Questionnaire(create,read),QuestionnaireResponse(create,read);RiskAssessment(read,search)");
        // filter =
        // createFilter("Patient(create,read,vread,update,delete,search,history)");

        JsonObjectBuilder swagger = factory.createObjectBuilder();
        swagger.add("openapi", "3.0.0");

        JsonObjectBuilder info = factory.createObjectBuilder();
        info.add("title", "FHIR REST API");
        info.add("description", "IBM Server for HL7 FHIR API");
        info.add("version", "4.0.0");
        swagger.add("info", info);

        /**
         * "servers": [ { "url": "/fhir-server/api/v4" } ]
         */
        JsonArrayBuilder servers = factory.createArrayBuilder();
        JsonObjectBuilder server = factory.createObjectBuilder();
        server.add("url", "/fhir-server/api/v4");
        servers.add(server);
        swagger.add("servers", servers);

        JsonArrayBuilder tags = factory.createArrayBuilder();
        JsonObjectBuilder paths = factory.createObjectBuilder();
        JsonObjectBuilder definitions = factory.createObjectBuilder();
        JsonObjectBuilder requestBodies = factory.createObjectBuilder();

        List<String> classNames = getClassNames();
        // generation for all the top level resource.
        for (String className : classNames) {
            Class<?> modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
            if (DomainResource.class.isAssignableFrom(modelClass) && filter.acceptResourceType(modelClass)) {
                generatePaths(modelClass, paths, filter);
                JsonObjectBuilder tag = factory.createObjectBuilder();
                tag.add("name", modelClass.getSimpleName());
                tags.add(tag);
            }
            generateRequestBody(modelClass, requestBodies);
            generateDefinition(modelClass, definitions);
        }
        
        // generate definition for all the defined Types.
        classNames = getAllTypesList();
        for (String className : classNames) {
            Class<?> modelClass = Class.forName(TYPEPACKAGENAME + "." + className);
            // System.out.println("Type:  " + className);
            generateDefinition(modelClass, definitions);
        }
        
        // generate definition for all inner classes inside the top level resources.
        for (String className : getAllResourceInnerClasses()) {
            Class<?> modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
            // System.out.println("Resource:  " + className);
            generateDefinition(modelClass, definitions);
        }

        JsonObjectBuilder tag = factory.createObjectBuilder();
        tag.add("name", "Other");
        tags.add(tag);

        // FHIR metadata operation
        JsonObjectBuilder path = factory.createObjectBuilder();
        generateMetadataPathItem(path);
        paths.add("/metadata", path);

        // FHIR batch operation
        path = factory.createObjectBuilder();
        generateBatchPathItem(path);
        paths.add("/", path);

        // TODO: FHIR transaction operation

        swagger.add("tags", tags);
        swagger.add("paths", paths);

        JsonObjectBuilder components = factory.createObjectBuilder();
        JsonObjectBuilder parameters = factory.createObjectBuilder();
        generateParameters(parameters, filter);
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
        JsonWriter writer = factory.createWriter(System.out);
        writer.writeObject(swagger.build());
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

    private static void populateStructureDefinitionMap(Map<Class<?>, StructureDefinition> structureDefinitionMap,
            String structureDefinitionFile) throws Exception {
        InputStream stream = FHIROpenApiGenerator.class.getClassLoader().getResourceAsStream(structureDefinitionFile);
        Bundle bundle = FHIRParser.parser(Format.JSON).parse(stream);
        for (Entry entry : bundle.getEntry()) {
            if (entry.getResource() instanceof StructureDefinition) {
                StructureDefinition structureDefinition = (StructureDefinition) entry.getResource();
                if (structureDefinition != null) {
                    String className = structureDefinition.getName().getValue();

                    // System.out.println("PopulateStructureDefinition adding: " + className);

                    className = className.substring(0, 1).toUpperCase() + className.substring(1);
                    Class<?> modelClass = null;
                    try {
                        modelClass = Class.forName(RESOURCEPACKAGENAME + "." + className);
                    } catch (ClassNotFoundException e1) {
                        try {
                            modelClass = Class.forName(TYPEPACKAGENAME + "." + className);
                        } catch (ClassNotFoundException e2) {
                            modelClass = null;
                            // System.out.println(" -- PopulateStructureDefinition failed: " + className);
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

    private static void generateParameters(JsonObjectBuilder parameters, Filter filter) throws Exception {
        if (filter.acceptOperation("read") || filter.acceptOperation("vread") || filter.acceptOperation("update")
                || filter.acceptOperation("delete") || filter.acceptOperation("history")) {
            JsonObjectBuilder id = factory.createObjectBuilder();
            id.add("name", "id");
            id.add("description", "logical identifier");
            id.add("in", "path");
            id.add("required", true);

            JsonObjectBuilder schema = factory.createObjectBuilder();
            schema.add("type", "string");
            id.add("schema", schema);

            parameters.add("idParam", id);
        }
        if (filter.acceptOperation("vread")) {
            JsonObjectBuilder vid = factory.createObjectBuilder();
            vid.add("name", "vid");
            vid.add("description", "version identifier");
            vid.add("in", "path");
            vid.add("required", true);

            JsonObjectBuilder schema = factory.createObjectBuilder();
            schema.add("type", "string");
            vid.add("schema", schema);

            parameters.add("vidParam", vid);
        }
        if (filter.acceptOperation("search")) {
            for (SearchParameter searchParameter : SearchUtil.getSearchParameters(Resource.class)) {
                JsonObjectBuilder parameter = factory.createObjectBuilder();
                String name = searchParameter.getName().getValue();
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
        idParamRef.add("$ref", "#/components/parameters/idParam");
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
        idParamRef.add("$ref", "#/components/parameters/idParam");
        parameters.add(idParamRef);

        JsonObjectBuilder vidParamRef = factory.createObjectBuilder();
        vidParamRef.add("$ref", "#/components/parameters/vidParam");
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

    private static void generateUpdatePathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder put = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        put.add("tags", tags);
        put.add("summary", "Update an existing " + modelClass.getSimpleName() + " resource");
        put.add("operationId", "update" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();

        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/components/parameters/idParam");
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
        idParamRef.add("$ref", "#/parameters/idParam");
        parameters.add(idParamRef);

        delete.add("parameters", parameters);

        JsonObjectBuilder responses = factory.createObjectBuilder();

        JsonObjectBuilder response = factory.createObjectBuilder();
        response.add("description", "Delete " + modelClass.getSimpleName() + " operation successful");
        responses.add("200", response);
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

        JsonArrayBuilder parameters = factory.createArrayBuilder();

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

    @SuppressWarnings("unchecked")
    private static void generateSearchParameters(Class<?> modelClass, JsonArrayBuilder parameters) throws Exception {
        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>(
                SearchUtil.getSearchParameters((Class<? extends Resource>) modelClass));
        for (SearchParameter searchParameter : searchParameters) {
            JsonObjectBuilder parameter = factory.createObjectBuilder();
            String name = searchParameter.getName().getValue();
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

    private static void generateHistoryPathItem(Class<?> modelClass, JsonObjectBuilder path) {
        JsonObjectBuilder get = factory.createObjectBuilder();

        JsonArrayBuilder tags = factory.createArrayBuilder();
        tags.add(modelClass.getSimpleName());

        get.add("tags", tags);
        get.add("summary", "Return the history of" + getArticle(modelClass) + modelClass.getSimpleName() + " resource");
        get.add("operationId", "history" + modelClass.getSimpleName());

        JsonArrayBuilder parameters = factory.createArrayBuilder();
        JsonObjectBuilder idParamRef = factory.createObjectBuilder();
        idParamRef.add("$ref", "#/components/parameters/idParam");
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
        if (!isEnumerationWrapperClass(modelClass)) {
            JsonObjectBuilder definition = factory.createObjectBuilder();
            JsonObjectBuilder properties = factory.createObjectBuilder();
            JsonArrayBuilder required = factory.createArrayBuilder();

            StructureDefinition structureDefinition = getStructureDefinition(modelClass);
            
            if (structureDefinition == null) {
                 System.out.println("Failed generateDefinition for: " + modelClass.getName());
                return;
            }
            
            for (Field field : modelClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isVolatile(field.getModifiers())) {
                    if (!ModelSupport.isChoiceElement(modelClass, ModelSupport.getElementName(field)) && field.isAnnotationPresent(Required.class)) {
                        required.add(ModelSupport.getElementName(field));
                    }
                    generateProperties(structureDefinition, modelClass, field, properties);
                }
            }

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
                allOf.add(wrapper);

                definition.add("allOf", allOf);
            } else {
                definition.add("type", "object");
                if (Resource.class.equals(modelClass)) {
                    definition.add("discriminator", "resourceType");
                }
                definition.add("properties", properties);
            }

            JsonArray requiredArray = required.build();
            if (!requiredArray.isEmpty()) {
                definition.add("required", requiredArray);
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
            for (Class<?> choiceType : choiceElementTypes) {
                String choiceElementName = ModelSupport.getChoiceElementName(elementName, choiceType);
                generateProperty(structureDefinition, modelClass, field, properties, choiceElementName, choiceType, many, description);
            }
        } else {
            ElementDefinition elementDefinition = getElementDefinition(structureDefinition, modelClass, elementName);
            String description = elementDefinition.getDefinition().getValue();
            generateProperty(structureDefinition, modelClass, field, properties, elementName, (Class<?>)fieldType, many, description);
        }
    }

    private static void generateProperty(StructureDefinition structureDefinition, Class<?> modelClass, Field field, JsonObjectBuilder properties,
        String elementName, Class<?> fieldClass, boolean many, String description) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        JsonObjectBuilder property = factory.createObjectBuilder();

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
        // Convert all the java types to according json types based on FHIR spec.
        } else if (String.class.equals(fieldClass)) {
            property.add("type", "string");
        } else if (Boolean.class.equals(fieldClass)) {
            property.add("type", "boolean");
        } else if (ZonedDateTime.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern", "([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)-(0[1-9]"
                    + "|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                    + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))");
        } else if (BigDecimal.class.equals(fieldClass)) {
            property.add("type", "number");
            property.add("pattern","-?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][+-]?[0-9]+)?");
        } else if (LocalTime.class.equals(fieldClass)) {
            property.add("type", "string");
            property.add("pattern","([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?");
        } else if (TemporalAccessor.class.equals(fieldClass)) {
            property.add("type", "string");
            if (Date.class.equals(modelClass)) {
                property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)"
                        + "|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1]))?)?");
            } else if (DateTime.class.equals(modelClass)) {
                property.add("pattern","([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]"
                        + "|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]"
                        + "|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?");
            }
        } else if (fieldClass.getSimpleName().equalsIgnoreCase("byte[]")) {
            property.add("type", "string");
            property.add("pattern","(\\s*([0-9a-zA-Z\\+\\=]){4}\\s*)+");
        } else if (fieldClass.getSimpleName().equalsIgnoreCase("int")) {
            property.add("type", "integer");
            property.add("pattern","[0]|[-+]?[1-9][0-9]*");
        } else {
            property.add("$ref", "#/components/schemas/" + getSimpleNameWithEnclosingNames(fieldClass));
        }

        if (description != null) {
            property.add("description", description);
        }

        if (many) {
            JsonObjectBuilder wrapper = factory.createObjectBuilder();
            wrapper.add("type", "array");
            wrapper.add("items", property);
            properties.add(elementName, wrapper);
        } else {
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

        public boolean acceptResourceType(Class<?> resourceType) {
            return filterMap.containsKey(resourceType.getSimpleName());
        }

        /*
         * public boolean acceptOperation(String resourceType, String operation) {
         * return filterMap.get(resourceType).contains(operation); }
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

    private static void getAllInnerClassNames(Class<?> classToIterat, List<String> resultList) {
        try {
            Class<?>[] InnerClassArray = classToIterat.getDeclaredClasses();

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
                // System.out.println(" -- Added: " + InnerClass.getName().substring(classNameOffset));

                if (InnerClass.getDeclaredClasses() != null) {
                    getAllInnerClassNames(InnerClass, resultList);
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to Iterate class: " + classToIterat.getSimpleName());
        }
    }

    public static List<String> getAllTypesList() {
        List<String> allTypesList = new ArrayList<String>();

        File typeFolder = new File(
                System.getProperty("user.dir") + "/../fhir-model/src/main/java/com/ibm/fhir/model/type");
        File[] listOfFiles = typeFolder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                allTypesList.add(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 5));
            }
        }

        List<String> toAddTypesList = new ArrayList<String>();

        for (String typeName : allTypesList) {
            // System.out.println(typeName);
            try {
                Class<?> resourceTypeClass = Class.forName(TYPEPACKAGENAME + "." + typeName);
                getAllInnerClassNames(resourceTypeClass, toAddTypesList);
            } catch (Exception e) {
                System.out.println("Failed to get type: " + typeName);
            }
        }
        
        allTypesList.addAll(toAddTypesList);

        return allTypesList;
    }

    public static List<String> getAllResourceInnerClasses() {
        List<String> allResourceInnerClassList = new ArrayList<String>();

        for (String ResourceName : FHIRUtil.getResourceTypeNames()) {
            // System.out.println(ResourceName);
            try {
                Class<?> resourceTypeClass = Class.forName(RESOURCEPACKAGENAME + "." + ResourceName);
                getAllInnerClassNames(resourceTypeClass, allResourceInnerClassList);
            } catch (Exception e) {
                System.out.println("Failed to get resource: " + ResourceName);
            }
        }
        return allResourceInnerClassList;
    }
    

}
