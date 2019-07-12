/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.plugin;

import java.io.File;
import java.util.Map;

import javax.json.JsonObject;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.ibm.watsonhealth.fhir.tools.CodeGenerator;

/**
 * This class coordinates the calls to the fhir-tool plugin
 * 
 * The phase is initialize. To find a list of phases -
 * https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference
 * 
 * Run the following to setup the plugin: <code>
 * mvn clean package install -f fhir-tools/pom.xml
 * </code>
 * 
 * Run the following to setup the classes in the fhir-model: <code> 
 * mvn com.ibm.watsonhealth.fhir:fhir-tools:generate-model -f ./fhir-parent/pom.xml
 * </code>
 * 
 * @author PBastide
 * 
 * @requiresDependencyResolution runtime
 *
 */
@Mojo(name = "generate-model", //$NON-NLS-1$
        requiresProject = true, requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM, requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM, defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresOnline = false, threadSafe = false)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class ModelPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true) //$NON-NLS-1$
    protected MavenProject mavenProject;

    @Parameter(defaultValue = "${project.basedir}", required = true, readonly = true) //$NON-NLS-1$
    private File baseDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (baseDir == null || !baseDir.exists()) {
            throw new MojoFailureException("The Base Directory is not found.  Throwing failure. ");
        }
        String targetDir = baseDir + "/src/main/java";
        String definitionsDir = baseDir + "/definitions";

        // Only runs for the fhir-model, short-circuits otherwise.
        if (mavenProject.getArtifactId().contains("fhir-model")) {

            // Check the base directory
            if ((new File(definitionsDir)).exists()) {

                // injecting for the purposes of setting the header
                getLog().info("Setting the base dir -> " + baseDir);
                System.setProperty("BaseDir", baseDir.getAbsolutePath());

                Map<String, JsonObject> structureDefinitionMap =
                        CodeGenerator.buildResourceMap(definitionsDir + "/profiles-resources.json", "StructureDefinition");
                structureDefinitionMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/profiles-types.json", "StructureDefinition"));

                Map<String, JsonObject> codeSystemMap = CodeGenerator.buildResourceMap(definitionsDir + "/valuesets.json", "CodeSystem");
                codeSystemMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/v3-codesystems.json", "CodeSystem"));

                Map<String, JsonObject> valueSetMap = CodeGenerator.buildResourceMap(definitionsDir + "/valuesets.json", "ValueSet");
                valueSetMap.putAll(CodeGenerator.buildResourceMap(definitionsDir + "/v3-codesystems.json", "ValueSet"));

                getLog().info("[Started] generating the code for fhir-model");
                CodeGenerator generator = new CodeGenerator(structureDefinitionMap, codeSystemMap, valueSetMap);
                generator.generate(targetDir);

                getLog().info("[Finished] generating the code for fhir-model");
            } else {
                getLog().info("Skipping as the Definitions don't exist in this project " + baseDir);
            }
        } else {
            getLog().info("Skipping project as the artifact is not a model project -> " + mavenProject.getArtifactId());
        }
    }}
