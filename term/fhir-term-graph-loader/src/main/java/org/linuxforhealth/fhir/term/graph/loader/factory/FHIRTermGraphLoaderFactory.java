/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.graph.loader.factory;

import java.util.Map;

import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.FHIRTermGraphLoader.Type;
import org.linuxforhealth.fhir.term.graph.loader.impl.CodeSystemTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.impl.SnomedTermGraphLoader;
import org.linuxforhealth.fhir.term.graph.loader.impl.UMLSTermGraphLoader;

/*
 * Factory class used to create FHIRTermGraphLoader instances
 */
public class FHIRTermGraphLoaderFactory {
    /**
     * Create {@link FHIRTermGraphLoader} instance using the provided type and options map.
     *
     * @param type
     *     the type
     * @param options
     *     the options map
     * @return
     *     a {@link FHIRTermGraphLoader} instance
     */
    public static FHIRTermGraphLoader create(Type type, Map<String, String> options) {
        switch (type) {
        case CODESYSTEM:
            return new CodeSystemTermGraphLoader(options);
        case SNOMED:
            return new SnomedTermGraphLoader(options);
        case UMLS:
            return new UMLSTermGraphLoader(options);
        default:
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
