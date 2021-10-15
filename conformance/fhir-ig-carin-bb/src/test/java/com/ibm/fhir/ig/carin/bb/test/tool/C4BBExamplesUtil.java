/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Resource;

/**
 * Profile Specific Examples Util
 */
public class C4BBExamplesUtil {

    /**
     * reads a local JSON into a resource
     * @param <T>
     * @param version
     * @param fileName
     * @return
     * @throws Exception
     */
    public static <T extends Resource> T readLocalJSONResource(String version, String fileName) throws Exception {
        try (Reader reader = resourceReader("/JSON/" + version + "/" + fileName)) {
            T resource = FHIRParser.parser(Format.JSON).parse(reader);
            return resource;
        }
    }

    /**
     * Return a reader for the specified example resource.
     * The resource should be an example resource such as "JSON/400/Observation.json".
     *
     * @param resource
     *          The relative path to the example resource within fhir-examples
     * @return reader A reader for reading the example resource
     * @throws FileNotFoundException
     *          If the specified resource does not exist
     */
    public static Reader resourceReader(String resource) throws IOException {
        InputStream is;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        is = cl.getResourceAsStream(resource);
        if (is == null) {
            // Try the class's classloader instead
            is = C4BBExamplesUtil.class.getResourceAsStream(resource);
        }

        if (is == null) {
            throw new FileNotFoundException("resource not found: " + resource);
        }

        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
