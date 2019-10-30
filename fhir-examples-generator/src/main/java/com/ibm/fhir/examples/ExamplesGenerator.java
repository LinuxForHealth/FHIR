/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.ResourceType;

public class ExamplesGenerator {
    DataCreatorBase minimalDataCreator;
    DataCreatorBase completeAbsentDataCreator;
    DataCreatorBase completeMockDataCreator;
    FHIRGenerator json = FHIRGenerator.generator(Format.JSON, true);
    FHIRGenerator xml = FHIRGenerator.generator(Format.XML, true);

    public ExamplesGenerator() throws IOException {
        this.minimalDataCreator = new MinimalDataCreator();
        this.completeAbsentDataCreator = new CompleteAbsentDataCreator();
        this.completeMockDataCreator = new CompleteMockDataCreator();
    }
    
    public void generate(String basePath) {
        for (ResourceType.ValueSet type : ResourceType.ValueSet.values()) {
            if (type == ResourceType.ValueSet.RESOURCE || type == ResourceType.ValueSet.DOMAIN_RESOURCE) {
                continue;
            }
            String resourceName = type.value();
            try {
                generateResource(resourceName, minimalDataCreator, basePath, "ibm/minimal");
                generateResource(resourceName, completeMockDataCreator, basePath, "ibm/complete-mock");
                generateResource(resourceName, completeAbsentDataCreator, basePath, "ibm/complete-absent");
            } catch (Exception e) {
                System.err.println("Caught exception while generating resource of type " + resourceName);
                e.printStackTrace();
            }
        }
    }
    
    private void generateResource(String resourceName, DataCreatorBase creator, String basePath, String tag) throws Exception {
        int maxChoiceCount = creator.getMaxChoiceCount(resourceName);

        for (int i = 1; i <= maxChoiceCount; i++) {
            Resource resource = creator.createResource(resourceName, i);
            resource = tag(resource, tag);

            File jsonFile = new File(basePath + "/json/" + tag + "/" + resourceName + "-" + i + ".json");
            if (!jsonFile.getParentFile().exists()) {
                jsonFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(jsonFile)) {
                json.generate(resource, writer);
            } catch (Exception e) {
                throw new Error(e);
            }
            
            File xmlFile = new File(basePath + "/xml/" + tag + "/" + resourceName + "-" + i + ".xml");
            if (!xmlFile.getParentFile().exists()) {
                xmlFile.getParentFile().mkdirs();
            }
            try (FileWriter writer = new FileWriter(xmlFile)) {
                xml.generate(resource, writer);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    
    /**
     * Copy {@code resource} to a new resource and add the tag
     * 
     * @param resource 
     *      the resource to tag
     * @param tag 
     *      the tag to tag it with
     * @return
     */
    private Resource tag(Resource resource, String tag) {
        Meta.Builder metaBuilder = resource.getMeta() != null ? resource.getMeta().toBuilder() : Meta.builder();
        metaBuilder.tag(Coding.builder().code(Code.of(tag)).build());
        resource = resource.toBuilder().meta(metaBuilder.build()).build();
        return resource;
    }
    
    public static void main(String[] args) throws Exception {
        ExamplesGenerator generator = new ExamplesGenerator();
        generator.generate("./src/test/resources");
    }

}
