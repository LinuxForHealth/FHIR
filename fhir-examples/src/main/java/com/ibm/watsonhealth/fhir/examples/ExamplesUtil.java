/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ExamplesUtil {
    /**
     * Return a reader for the specified example resource.
     * The resource should be an example resource such as "json/ibm/minimal/Account-1.json".
     * 
     * @param resource
     *          The relative path to the example resource within fhir-examples
     * @return reader A reader for reading the example resource
     * @throws IllegalStateException
     *          If the specified resource does not exist
     */
    public static Reader reader(String resource) throws IOException {
        InputStream is;
    
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        is = cl.getResourceAsStream(resource);
        if (is == null) {
            // Try the class's classloader instead
            is = ExamplesUtil.class.getResourceAsStream(resource);
        }

        if (is == null) {
            throw new IllegalStateException("resource not found: " + resource);
        }

        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
