/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.util.ModelSupport;

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

    public void generate(Path basePath) {
        for (Class<?> type : ModelSupport.getResourceTypes(false)) {
            generate(basePath, ModelSupport.getTypeName(type));
        }
    }

    private void generate(Path basePath, String resourceName) {
        try {
            generateResource(resourceName, minimalDataCreator, basePath, "ibm/minimal");
            generateResource(resourceName, completeMockDataCreator, basePath, "ibm/complete-mock");
            generateResource(resourceName, completeAbsentDataCreator, basePath, "ibm/complete-absent");
        } catch (Exception e) {
            System.err.println("Caught exception while generating resource of type " + resourceName);
            e.printStackTrace();
        }
    }

    private void generateResource(String resourceName, DataCreatorBase creator, Path basePath, String tag) throws Exception {
        int maxChoiceCount = creator.getMaxChoiceCount(resourceName);

        for (int i = 1; i <= maxChoiceCount; i++) {
            Resource resource = creator.createResource(resourceName, i);
            resource = tag(resource, tag);

            Path jsonPath = basePath.resolve(Paths.get("json", tag, resourceName + "-" + i + ".json"));
            Files.createDirectories(jsonPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(jsonPath)) {
                json.generate(resource, writer);
            } catch (Exception e) {
                throw new Error(e);
            }

            Path xmlPath = basePath.resolve(Paths.get("xml", tag, resourceName + "-" + i + ".xml"));
            Files.createDirectories(xmlPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(xmlPath)) {
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
        Coding tagCode = Coding.builder().code(Code.of(tag)).build();
        if (resource.getMeta() != null && !resource.getMeta().getTag().isEmpty()) {
            if (resource.getMeta().getTag().contains(tagCode)) {
                // resource already has the tag, so just return
                return resource;
            }
        }
        Meta.Builder metaBuilder = resource.getMeta() != null ? resource.getMeta().toBuilder() : Meta.builder();
        metaBuilder.tag(Coding.builder().code(Code.of(tag)).build());
        resource = resource.toBuilder().meta(metaBuilder.build()).build();
        return resource;
    }

    public static void main(String[] args) throws Exception {
        ExamplesGenerator generator = new ExamplesGenerator();
        generator.generate(Paths.get("src/test/resources"));
        // User this flavor instead to generate examples for a single type
//        generator.generate(Paths.get("src/test/resources"), "EventDefinition");
    }

}
