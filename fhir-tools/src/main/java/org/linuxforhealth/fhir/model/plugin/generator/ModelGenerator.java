/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.plugin.generator;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * @author pbastide@us.ibm.com
 *
 */
public interface ModelGenerator {

    /**
     * each model generator may choose which directory to use local or embedded
     * 
     * @return
     */
    public default boolean useTargetProjectBaseDirectory() {
        return true;
    }

    /**
     * sets the base directory for the generator.
     * 
     * @param baseDirectory
     */
    public void setTargetProjectBaseDirectory(String baseDirectory);
    
    /**
     * set the limiting of the project 
     */
    public void setLimit(boolean limit);
    
    /**
     * processes the output of the code generator. 
     * @param mavenProject
     * @param log
     */
    public void process(MavenProject mavenProject, Log log);

}
