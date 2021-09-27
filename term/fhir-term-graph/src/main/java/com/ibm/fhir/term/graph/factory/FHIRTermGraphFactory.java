/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph.factory;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import com.ibm.fhir.term.graph.FHIRTermGraph;
import com.ibm.fhir.term.graph.impl.FHIRTermGraphImpl;

/*
 * Factory class used to create FHIRTermGraph instances
 */
public final class FHIRTermGraphFactory {
    private FHIRTermGraphFactory() { }

    /**
     * Create a {@link FHIRTermGraph} instance using the given configuration properties file.
     *
     * @param propFileName
     *     the configuration properties file
     * @return
     *     the {@link FHIRTermGraph} instance
     */
    public static FHIRTermGraph open(String propFileName) {
        try {
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(new Parameters().properties().setFileName(propFileName));
            return open(builder.getConfiguration());
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Create a {@link FHIRTermGraph} instance using the given configuration object.
     *
     * @param configuration
     *     the configuration object
     * @return
     *     the {@link FHIRTermGraph} instance
     */
    public static FHIRTermGraph open(Configuration configuration) {
        return new FHIRTermGraphImpl(configuration);
    }
}
