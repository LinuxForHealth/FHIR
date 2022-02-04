/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.LogManager;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceTypeCode;

/**
 * A base search test with utilities for other search tests
 */
public abstract class BaseSearchTest {
    // The base uri used for all search tests. This is used to derive the incoming system url
    // which is required when processing search parameters
    public static final String BASE = "https://example.com/";

    public static final boolean DEBUG = false;

    @BeforeClass
    public void configureLogging() throws Exception {
        final InputStream inputStream = BaseSearchTest.class.getResourceAsStream("/logging.unitTest.properties");
        LogManager.getLogManager().readConfiguration(inputStream);
    }

    @BeforeMethod
    public void startMethod(Method method) {
        if (DEBUG) {
            System.out.println("Starting Test -> " + method.getName());
        }

        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        if (context == null) {
            context = new FHIRRequestContext();
        }
        context.setOriginalRequestUri(BASE);
        FHIRRequestContext.set(context);
    }

    @AfterMethod
    public void afterMethod(Method method) {
        if (DEBUG) {
            System.out.println("End of Test -> " + method.getName());
        }
    }

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    /*
     * The SearchParameters return an array of values, now the printSearchParameters returns all values.
     * @param label
     * @param spList
     */
    @SuppressWarnings("unused")
    protected void printSearchParameters(String label, Map<String, SearchParameter> parameters) {
        if (DEBUG && parameters != null) {
            System.out.println("\nTest: " + label + "\nSearch Parameters:");
            for (Entry<String, SearchParameter> entry : parameters.entrySet()) {
                String code = entry.getKey();
                SearchParameter sp = entry.getValue();

                String url = sp.getUrl().getValue();
                String version = (sp.getVersion() == null) ? null : sp.getVersion().getValue();
                String canonical = (version == null) ? url : url + "|" + version;

                List<ResourceTypeCode> resources = sp.getBase();
                for (ResourceTypeCode resource : resources) {
                    System.out.println("\t" + resource.getValue() + ":" + code + ":" + canonical);
                }
            }
        }
    }

    /**
     * Copies a file.
     *
     * @param fromFilename
     *            the file to copy from
     * @param toFilename
     *            the file to copy to
     */
    protected void copyFile(String fromFilename, String toFilename) throws Exception {
        File from = new File(fromFilename);
        File to = new File(toFilename);
        Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.setLastModifiedTime(to.toPath(), FileTime.fromMillis(System.currentTimeMillis()));
    }

    protected void deleteFile(String filename) throws Exception {
        File file = new File(filename);
        Files.deleteIfExists(file.toPath());
    }

    /**
     * prints the value types based on the incoming set.
     *
     * @param valueTypes
     */
    @SuppressWarnings("rawtypes")
    protected void printValueTypes(Set<Class<?>> valueTypes) {
        // Must be not null to loop.
        assertNotNull(valueTypes);
        for (Class vl : valueTypes) {
            System.out.println(vl.getSimpleName());
        }
    }
}