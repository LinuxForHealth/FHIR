/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.plugin;

import java.io.File;
import java.util.Properties;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import org.linuxforhealth.fhir.model.plugin.generator.ModelFactory;
import org.linuxforhealth.fhir.model.plugin.generator.ModelGenerator;

/**
 * This class coordinates the calls to the fhir-tool plugin
 *
 * The phase is generate-sources. To find a list of phases -
 * https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference
 *
 * Run the following to setup the plugin:
 * <pre>
 * mvn clean package install -f fhir-tools/pom.xml
 * </pre>
 *
 * Run the following to create/update the classes in the fhir-model project:
 * <pre>
 * mvn org.linuxforhealth.fhir:fhir-tools:generate-model -f ./fhir-model/pom.xml
 * </pre>
 *
 * @requiresDependencyResolution runtime
 */
@Mojo(name = "generate-model", //$NON-NLS-1$
        requiresProject = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM,
        requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        requiresOnline = false,
        threadSafe = false)
@Execute(phase = LifecyclePhase.GENERATE_SOURCES)
public class ModelPlugin extends AbstractMojo {

    // used with overrides -Dgen.version=r4.1
    private static final String VERSION = "gen.version";
    private static final String DEFAULT_VERSION = "R4B";

    // used to limit to fhir-model. -Dlimit=false
    private static final String LIMIT = "limit";
    private static final String DEFAULT_LIMIT = "true";

    @Parameter(defaultValue = "${project}", required = true, readonly = true) //$NON-NLS-1$
    protected MavenProject mavenProject;

    @Parameter(defaultValue = "${session}")
    private MavenSession session;

    @Parameter(defaultValue = "${project.basedir}", required = true, readonly = true) //$NON-NLS-1$
    private File baseDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (baseDir == null || !baseDir.exists()) {
            throw new MojoFailureException("The Base Directory is not found.  Throwing failure. ");
        }

        // Grab the Properties (the correct way)
        // https://maven.apache.org/plugin-developers/common-bugs.html#Using_System_Properties
        Properties userProps = session.getUserProperties();
        String generatorVersion = userProps.getProperty(VERSION, DEFAULT_VERSION);
        String limit = userProps.getProperty(LIMIT, DEFAULT_LIMIT);

        // Converts Limit value to boolean value.
        boolean limitVal = Boolean.parseBoolean(limit);

        // Grab the right generator and set it up.
        ModelGenerator generator = ModelFactory.getModelGenerator(generatorVersion, getLog());

        // Get the base directory .
        generator.setTargetProjectBaseDirectory(baseDir.getAbsolutePath());

        // Set the limit value
        generator.setLimit(limitVal);

        // Processes the Model Code.
        generator.process(mavenProject, getLog());
    }
}
