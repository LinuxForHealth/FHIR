/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test;

import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceType;

/**
 *
 */
public abstract class BaseSearchTest {
    // The base uri used for all search tests. This is used to derive the incoming system url
    // which is required when processing search parameters
    public static final String BASE = "https://example.com/";

    public static final boolean DEBUG = false;

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

    /**
     * This function returns a list containing the names of the SearchParameters contained in the input list.
     *
     * @param spList
     *            the list of SearchParameter from which to collect the names
     * @return the list of search parameter names
     */
    protected List<String> getSearchParameterCodes(List<SearchParameter> spList) {
        List<String> result = new ArrayList<>();
        for (SearchParameter sp : spList) {
            result.add(sp.getCode().getValue());
        }
        return result;
    }

    /*
     * The SearchParameters return an array of values, now the printSearchParameters returns all values.
     * @param label
     * @param spList
     */
    protected void printSearchParameters(String label, List<SearchParameter> spList) {
        if (DEBUG) {
            System.out.println("\nTest: " + label + "\nSearch Parameters:");
            for (SearchParameter sp : spList) {
                List<ResourceType> resources = sp.getBase();
                for (ResourceType resource : resources) {
                    System.out.println("\t" + resource.getValue() + ":" + sp.getCode().getValue());
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