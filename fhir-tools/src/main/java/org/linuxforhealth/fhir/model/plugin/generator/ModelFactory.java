/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.plugin.generator;

import org.apache.maven.plugin.logging.Log;

import org.linuxforhealth.fhir.model.plugin.generator.impl.R4BModelGeneratorImpl;
import org.linuxforhealth.fhir.model.plugin.generator.impl.R4ModelGeneratorImpl;

/**
 * Controls the access to the modelgenerator tools
 *
 * @author pbastide
 *
 */
public class ModelFactory {

    private ModelFactory() {
        // No Operation
    }

    /**
     * uses generator version to find the specific implementation
     * if the version is not found based on class name, it defaults to the r4 version.
     *
     * @param generatorVersion
     * @param log
     * @return
     */
    public static ModelGenerator getModelGenerator(String generatorVersion, Log log) {

        //Default is R4
        ModelGenerator generator = null;
        if("r4b".equalsIgnoreCase(generatorVersion)) {
            generator = new R4BModelGeneratorImpl();
        } else if ("r4".equalsIgnoreCase(generatorVersion)) {
            generator = new R4ModelGeneratorImpl();
        } else {
            try {
                generator = (ModelGenerator) Class.forName(generatorVersion).newInstance();
            } catch (InstantiationException e) {
                log.warn("defaulting to the default version as unable to create instance");
            } catch (IllegalAccessException e) {
                log.warn("class path violation and unable to access desired instance");
            } catch (ClassNotFoundException e) {
                log.warn("class is not found");
            }
        }

        return generator;
    }
}
