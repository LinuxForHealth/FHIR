/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.graph;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.ibm.fhir.term.graph.impl.FHIRTermGraphImpl;

public final class FHIRTermGraphFactory {
    private FHIRTermGraphFactory() { }

    public static FHIRTermGraph open(String propFileName) {
        try {
            return open(new PropertiesConfiguration(propFileName));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    public static FHIRTermGraph open(Configuration configuration) {
        return new FHIRTermGraphImpl(configuration);
    }
}
