/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.Test;

/**
 * Test to check that we can read a resource as a stream from another jar
 * @author rarnold
 */
public class ReadResourceStreamTest {

    @Test
    public void testRead() throws IOException {
        final String filename = "json/all.txt";
        
        try (InputStream is = getResourceAsStream(filename)) {
            System.out.println("Success! Stream opened for " + filename);
        }
    }

    /**
     * Open the named resource file as a stream
     * @param resource
     * @return
     */
    protected InputStream getResourceAsStream(String resource) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream result = cl.getResourceAsStream(resource);
        if (result == null) {
            // Try the class's classloader instead
            result = getClass().getResourceAsStream(resource);
        }
        
        if (result == null) {
            throw new IllegalStateException("resource not found: " + resource);
        }
        
        return result;
    }

}
