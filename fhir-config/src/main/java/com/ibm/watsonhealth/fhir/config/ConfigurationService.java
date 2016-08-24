/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * The ConfigurationService is used by the FHIR Server to retrieve JSON-based configuration
 * data.
 * 
 * @author padams
 *
 */
public class ConfigurationService {
    
    /**
     * Loads the specified file as a JSON file and returns a PropertyGroup containing
     * the contents of the JSON file as the root property group.
     * @param filename the name of the JSON file to be loaded
     * @throws FileNotFoundException 
     */
    public static PropertyGroup loadConfiguration(String filename) throws FileNotFoundException {
        InputStream is = resolveFile(filename);
        return loadConfiguration(is);
    }
    
    /**
     * Loads the specified input stream as a JSON file and returns a PropertyGroup containing
     * the contents of the JSON file as the root property group.
     * @param is an InputStream to the input JSON file
     */
    public static PropertyGroup loadConfiguration(InputStream is) {
        JsonReader reader = Json.createReader(is);
        JsonObject jsonObj = reader.readObject();
        return new PropertyGroup(jsonObj);
    }
    
    /**
     * Returns an InputStream for the specified filename.   This function will
     * first try to open the file using the filename as a relative or absolute filename.
     * If that fails, then we'll try to find the file on the classpath.
     * @param filename the name of the file to search for
     * @return an InputStream to the file or throws a FileNotFoundException if not found
     * @throws FileNotFoundException
     */
    private static InputStream resolveFile(String filename) throws FileNotFoundException {
        // First, try to use the filename as-is.
        File f = new File(filename);
        if (f.exists()) {
            return new FileInputStream(f);
        }

        // Next, look on the classpath.
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("File '" + filename + "' was not found.");
    }
}
