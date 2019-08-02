/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.ibm.watsonhealth.fhir.model.format.Format;
import com.ibm.watsonhealth.fhir.model.generator.FHIRGenerator;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Coding;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.examples.CompleteAbsentDataCreator;
import com.ibm.watsonhealth.fhir.examples.DataCreatorBase;
import com.ibm.watsonhealth.fhir.examples.MinimalDataCreator;

public class ExamplesGenerator {
    private final Map<String, JsonObject> structureDefinitionMap;
    DataCreatorBase minimalDataCreator;
    DataCreatorBase completeAbsentDataCreator;
    DataCreatorBase completeMockDataCreator;
    FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, true);

    public ExamplesGenerator(Map<String, JsonObject> structureDefinitionMap) throws IOException {
        this.structureDefinitionMap = structureDefinitionMap;
        this.minimalDataCreator = new MinimalDataCreator();
        this.completeAbsentDataCreator = new CompleteAbsentDataCreator();
        this.completeMockDataCreator = new CompleteMockDataCreator();
    }
    
    public void generate(String basePath) {
        for (String key : structureDefinitionMap.keySet()) {
            JsonObject structureDefinition = structureDefinitionMap.get(key);
            
            try {
                generateResource(structureDefinition, minimalDataCreator, basePath + "/minimal", "ibm/minimal");
                generateResource(structureDefinition, completeMockDataCreator, basePath + "/complete-mock", "ibm/complete-mock");
                generateResource(structureDefinition, completeAbsentDataCreator, basePath + "/complete-absent", "ibm/complete-absent");
            } catch (Exception e) {
                System.err.println("Caught exception while processing resource for " + key);
                e.printStackTrace();
            }
        }
    }
    
    private void generateResource(JsonObject structureDefinition, DataCreatorBase creator, String basePath, String tag) throws Exception {
        String resourceName = titleCase(structureDefinition.getString("name"));
        String kind = structureDefinition.getString("kind");
        // ignore "logical" resource definitions and type definitions
        if ("resource".equals(kind) 
                && !"DomainResource".equals(resourceName)
                && !"Resource".equals(resourceName)) {
            
            int maxChoiceCount = creator.getMaxChoiceCount(resourceName);
            
            for (int i = 1; i <= maxChoiceCount; i++) {
                Resource resource = creator.createResource(resourceName, i);
                
                resource = tag(resource, tag);
            
                File file = new File(basePath + "/" + resourceName + "-" + i + ".json");
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try (FileWriter writer = new FileWriter(file)) {
                    generator.generate(resource, writer);
                } catch (Exception e) {
                    throw new Error(e);
                }
            }
        }
    }

    
    /**
     * Copy {@code resource} to a new resource and add the tag
     * @param resource the resource to tag
     * @param tag the tag to tag it with
     * @return
     */
    private Resource tag(Resource resource, String tag) {
        Meta.Builder metaBuilder = resource.getMeta() != null ? resource.getMeta().toBuilder() : Meta.builder();
        metaBuilder.tag(Coding.builder().code(Code.of(tag)).build());
        resource = resource.toBuilder().meta(metaBuilder.build()).build();
        return resource;
    }
    
    private String titleCase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    public static Map<String, JsonObject> buildResourceMap(String path, String resourceType) {
        try (JsonReader reader = Json.createReader(new FileReader(new File(path)))) {
            List<JsonObject> resources = new ArrayList<>();
            JsonObject bundle = reader.readObject();
    
            for (JsonValue entry : bundle.getJsonArray("entry")) {
                JsonObject resource = entry.asJsonObject().getJsonObject("resource");
                if (resourceType.equals(resource.getString("resourceType"))) {
                    resources.add(resource);
                }
            }
            
            Collections.sort(resources, new Comparator<JsonObject>() {
                @Override
                public int compare(JsonObject first, JsonObject second) {
                    return first.getString("name").compareTo(second.getString("name"));
                }
            });
            
            Map<String, JsonObject> resourceMap = new LinkedHashMap<>();
            for (JsonObject resource : resources) {
                if ("CodeSystem".equals(resourceType) || "ValueSet".equals(resourceType)) {
                    resourceMap.put(resource.getString("url"), resource);
                } else {
                    resourceMap.put(resource.getString("name"), resource);
                }
            }
            
            return resourceMap;
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Map<String, JsonObject> structureDefinitionMap = buildResourceMap("./definitions/profiles-resources.json", "StructureDefinition");
        
        ExamplesGenerator generator = new ExamplesGenerator(structureDefinitionMap);
        generator.generate("./src/test/resources");
    }

}
