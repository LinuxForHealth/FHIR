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
    
    public static PropertyGroup loadConfiguration(InputStream is) {
        JsonReader reader = Json.createReader(is);
        JsonObject jsonObj = reader.readObject();
        return new PropertyGroup(jsonObj);
    }
    
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
