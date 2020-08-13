/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.examples.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.ibm.fhir.examples.ExamplesGenerator;

/**
 * This class coordinates the calls to the fhir-sample-generator plugin
 * 
 * The phase is initialize. To find a list of phases -
 * https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference
 * 
 * Run the following to setup the plugin: 
 * <pre>
 * mvn clean package install -f fhir-examples-generator/pom.xml
 * </pre>
 * 
 * Run the following to create/update the classes in the fhir-model project: 
 * <pre> 
 * mvn com.ibm.fhir:fhir-examples-generator:generate-examples -f ./fhir-parent/pom.xml
 * </pre>
 * 
 * @author PBastide
 * 
 * @requiresDependencyResolution runtime
 *
 */
@Mojo(name = "generate-examples", //$NON-NLS-1$
        requiresProject = true, requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM, requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM, defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresOnline = false, threadSafe = false)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class ExamplesPlugin extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true) //$NON-NLS-1$
    protected MavenProject mavenProject;

    @Parameter(defaultValue = "${project.basedir}", required = true, readonly = true) //$NON-NLS-1$
    private File baseDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (baseDir == null || !baseDir.exists()) {
            throw new MojoFailureException("The Base Directory is not found.  Throwing failure. ");
        }
        String targetDir = baseDir + "/src/test/resources/JSON";
        String definitionsDir = baseDir + "/definitions";

        // Only runs for the fhir-examples-generator project since that is where the definitions directory exists, short-circuits otherwise.
        if (mavenProject.getArtifactId().contains("fhir-examples-generator")) {

            // Check the base directory
            if ((new File(definitionsDir)).exists()) {

                // injecting for the purposes of setting the header
                getLog().info("Setting the base dir -> " + baseDir);
                System.setProperty("BaseDir", baseDir.getAbsolutePath());

                getLog().info("[Started] generating the examples for fhir-model");
                ExamplesGenerator generator;
                try {
                    generator = new ExamplesGenerator();
                    getLog().info("Writing examples to " + targetDir);
                    generator.generate(targetDir);
                } catch (IOException e) {
                    getLog().error("An error ocurred while generating examples", e);
                }

                getLog().info("[Finished] generating the examples for fhir-model");
            } else {
                getLog().info("Skipping as the Definitions don't exist in this project " + baseDir);
            }
        } else {
            getLog().info("Skipping project as the artifact is not a model project -> " + mavenProject.getArtifactId());
        }
    }}
