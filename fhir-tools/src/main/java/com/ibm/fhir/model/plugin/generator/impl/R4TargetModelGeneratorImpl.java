/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.plugin.generator.impl;

import java.io.File;
import java.util.Map;

import jakarta.json.JsonObject;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import com.ibm.fhir.model.plugin.generator.ModelGenerator;
import com.ibm.fhir.tools.CodeGenerator;

/**
 * Runs in FHIR-MODEL 
 * 
 * @author pbastide@us.ibm.com
 *
 */
public class R4TargetModelGeneratorImpl implements ModelGenerator {

    private String baseDirectory;
    private boolean limit = true;

    /*
     * in this generator, we want to make sure we use the source from fhir-tools.
     */
    @Override
    public boolean useTargetProjectBaseDirectory() {
        return false;
    }

    /*
     * sets the base directory for later use
     */
    @Override
    public void setTargetProjectBaseDirectory(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    @Override
    public void setLimit(boolean limit) {
        this.limit = limit;
    }

    @Override
    public void process(MavenProject mavenProject, Log log) {
        // Only runs for the fhir-model, short-circuits otherwise.
        String targetDir = baseDirectory + "/src/main/java";
        String baseDirectoryJson = baseDirectory;
        String definitionsDir = baseDirectory + "/definitions";
        
        if (mavenProject.getArtifactId().contains("fhir-model") || !limit ) {

            // Check the base directory
            if ((new File(definitionsDir)).exists()) {

                // injecting for the purposes of setting the header
                log.info("Setting the base dir -> " + baseDirectory);
                System.setProperty("BaseDir", baseDirectory);
                System.setProperty("JsonBaseDir", baseDirectoryJson);

                Map<String, JsonObject> structureDefinitionMap =
                        CodeGenerator.buildResourceMap(definitionsDir + "/profiles-resources.json", "StructureDefinition");
                structureDefinitionMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/profiles-types.json", "StructureDefinition"));

                Map<String, JsonObject> codeSystemMap = CodeGenerator.buildResourceMap(definitionsDir + "/valuesets.json", "CodeSystem");
                codeSystemMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/v3-codesystems.json", "CodeSystem"));

                Map<String, JsonObject> valueSetMap = CodeGenerator.buildResourceMap(definitionsDir + "/valuesets.json", "ValueSet");
                valueSetMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/v3-codesystems.json", "ValueSet"));

                log.info("[Started] generating the code for fhir-model");
                CodeGenerator generator = new CodeGenerator(structureDefinitionMap, codeSystemMap, valueSetMap);
                generator.generate(targetDir);

                log.info("[Finished] generating the code for fhir-model");
            } else {
                log.info("Skipping as the Definitions don't exist in this project " + baseDirectory);
            }
        } else {
            log.info("Skipping project as the artifact is not a model project -> " + mavenProject.getArtifactId());
        }

    }

}
