/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;

/**
 * The ConfigurationService is used by the FHIR Server to retrieve JSON-based configuration data.
 * 
 * @author padams
 *
 */
public class ConfigurationService {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    /**
     * This property can be used to set the name of the "property group" class that should be instantiated with the
     * top-level JsonObject representing the configuration.
     */
    public static final String PROPERTY_GROUP_CLASSNAME = "com.ibm.fhir.config.PROPERTY_GROUP_ClASSNAME";

    /**
     * Loads the specified file as a JSON file and returns a PropertyGroup containing the contents of the JSON file as
     * the root property group.
     * 
     * @param filename
     *            the name of the JSON file to be loaded
     * @throws FileNotFoundException
     */
    public static PropertyGroup loadConfiguration(String filename) throws Exception {
        try (InputStream is = resolveFile(filename)) {
            return loadConfiguration(is);
        }
    }

    /**
     * Loads the specified input stream as a JSON file and returns a PropertyGroup containing the contents of the JSON
     * file as the root property group.
     * 
     * @param is
     *            an InputStream to the input JSON file
     */
    public static PropertyGroup loadConfiguration(InputStream is) throws Exception {
        String templatedJson = IOUtils.toString(is, StandardCharsets.UTF_8);
        String resolvedJson = StringSubstitutor.replace(templatedJson, EnvironmentVariables.get());
        try (JsonReader reader = JSON_READER_FACTORY.createReader(new StringReader(resolvedJson))) {
            JsonObject jsonObj = reader.readObject();
            reader.close();
            return instantiatePropertyGroup(jsonObj);
        }
    }

    /**
     * Utility class that allows mocking system environment variables retrieval in test classes (as Mockito disallows
     * mocking static methods of {@link System}).
     */
    public static class EnvironmentVariables {
        /**
         * Simple proxy method for {@link System#getenv()} that returns an unmodifiable string map view of the current
         * system environment.
         *
         * @return the environment as a map of variable names to values
         */
        public static Map<String,String> get() {
            return System.getenv();
        }
    }

    /**
     * This function will instantiate the appropriate "property group" class, depending on the setting of the
     * PROPERTY_GROUP_CLASSNAME property.
     * 
     * @param jsonObj
     *            the JsonObject containing the configuration data
     */
    @SuppressWarnings("unchecked")
    private static PropertyGroup instantiatePropertyGroup(JsonObject jsonObj) throws Exception {
        try {
            // Our default will be the PropertyGroup class.
            Class<? extends PropertyGroup> clazz = PropertyGroup.class;

            // Try to retrieve our classname property.
            String className = System.getProperty(PROPERTY_GROUP_CLASSNAME);

            // If specified, then load the class.
            if (className != null && !className.isEmpty()) {
                Class<?> c = Class.forName(className);
                if (c.isAssignableFrom(PropertyGroup.class)) {
                    throw new IllegalArgumentException("Class '" + className + "' is not a subclass of PropertyGroup");
                }
                clazz = (Class<? extends PropertyGroup>) c;
            }

            // Retrieve the 1-arg ctor.
            Constructor<? extends PropertyGroup> ctor = clazz.getConstructor(JsonObject.class);
            if (ctor == null) {
                throw new IllegalArgumentException("Could not retrieve required constructor from class '" + className + "'");
            }

            return ctor.newInstance(jsonObj);
        } catch (Throwable t) {
            throw new IllegalArgumentException("Invalid class specified for PROPERTY_GROUP_CLASSNAME property.", t);
        }
    }

    /**
     * Returns an InputStream for the specified filename. This function will first try to open the file using the
     * filename as a relative or absolute filename. If that fails, then we'll try to find the file on the classpath.
     * 
     * @param filename
     *            the name of the file to search for
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
