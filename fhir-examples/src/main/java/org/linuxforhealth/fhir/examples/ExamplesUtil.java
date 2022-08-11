/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.examples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class ExamplesUtil {
    /**
     * Return a reader for the specified resource index.
     * 
     * @param index
     *          The index file to read
     * @return reader A reader for reading the index file
     * @throws FileNotFoundException
     *          If the specified index could not be found
     */
    public static Reader indexReader(Index index) throws IOException {
        StringBuilder allLines = new StringBuilder();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        for (String path : index.paths()) {
            InputStream is = cl.getResourceAsStream(path);
            if (is == null) {
                // Try the class's classloader instead
                is = ExamplesUtil.class.getResourceAsStream(path);
            }
            if (is == null) {
                throw new FileNotFoundException("resource not found: " + path);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    allLines.append(line).append(System.lineSeparator());
                }
            }
        }

        return new StringReader(allLines.toString());
    }
    
    /**
     * Return a reader for the specified example resource.
     * The resource should be an example resource such as "json/minimal/Account-1.json".
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
            is = ExamplesUtil.class.getResourceAsStream(resource);
        }

        if (is == null) {
            throw new FileNotFoundException("resource not found: " + resource);
        }

        return new InputStreamReader(is, StandardCharsets.UTF_8);
    }
}
