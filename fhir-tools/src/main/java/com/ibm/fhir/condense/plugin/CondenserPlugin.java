/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.condense.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * This class coordinates the calls to the fhir-tool plugin
 *
 * The phase is process-resources. To find a list of phases -
 * https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Lifecycle_Reference
 *
 * Run the following to setup the plugin:
 * <pre>
 * mvn clean install -f fhir-tools/pom.xml
 * </pre>
 *
 * @requiresDependencyResolution runtime
 */
@Mojo(name = "condense-json", //$NON-NLS-1$
        requiresProject = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME_PLUS_SYSTEM,
        requiresDependencyCollection = ResolutionScope.RUNTIME_PLUS_SYSTEM,
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        requiresOnline = false,
        threadSafe = false)
@Execute(phase = LifecyclePhase.PROCESS_RESOURCES)
public class CondenserPlugin extends AbstractMojo {

    @Parameter( defaultValue = "${project.build.outputDirectory}", required = true )
    private File outputDirectory;

    @Parameter( defaultValue = "${project.resources}", required = true, readonly = true )
    private List<Resource> resources;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        for (Resource resource : resources) {
            try {
                Path resourcesDir = Paths.get(resource.getDirectory());
                if (Files.isDirectory(resourcesDir)) {
                    CondenseAndCopyFileVisitor visitor = new CondenseAndCopyFileVisitor(resourcesDir, outputDirectory.toPath());
                    Files.walkFileTree(resourcesDir, visitor);

                    getLog().info("processed " + visitor.getCount() + " JSON files");
                } else {
                    getLog().info("skipping non-directory " + resourcesDir);
                }
            } catch (IOException e) {
                throw new MojoExecutionException("Error while condensing and copying resources", e);
            }
        }
    }
}
