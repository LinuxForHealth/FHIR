/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExamplesUtil {
    /**
     * Read the given resource as an {@link InputStream}
     * @param resource
     * @return
     */
    public static InputStream getInputStream(String resource) throws IOException {
        InputStream result;
    
        if (resource.startsWith("file:")) {
            result = new FileInputStream(resource.substring(5));
        }
        else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            result = cl.getResourceAsStream(resource);
            if (result == null) {
                // Try the class's classloader instead
                result = ExamplesUtil.class.getResourceAsStream(resource);
            }
            
            if (result == null) {
                throw new IllegalStateException("resource not found: " + resource);
            }
        }

        return result;
    }
}
