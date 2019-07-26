/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test;

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

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.model.resource.SearchParameter;
import com.ibm.watsonhealth.fhir.model.type.ResourceType;

/**
 *
 * @author paulbastide
 *
 */
public abstract class BaseSearchTest {
    
    public static final boolean DEBUG = false; 

    /*
     * used in test to make the compiled code accessible. 
     */
    static {
        // If issues, you should uncomment the following : 
        // System.setProperty("javax.xml.accessExternalSchema","file");
    }
    
    @BeforeMethod
    public void startMethod(Method method) {
        System.out.println("Starting Test -> " + method.getName());
    }
    
    @AfterMethod
    public void afterMethod(Method method) {
        System.out.println("End of Test -> " + method.getName());
    }
    
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    @BeforeMethod
    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }
    
    
    /**
     * This function returns a list containing the names of the SearchParameters contained in the input list.
     * 
     * @param result
     *            the list of SearchParameter from which to collect the names
     * @return the list of search parameter names
     */
    protected List<String> getSearchParameterNames(List<SearchParameter> spList) {
        List<String> result = new ArrayList<>();
        for (SearchParameter sp : spList) {
            result.add(sp.getName().getValue());
        }
        return result;
    }
    
    /*
     * The SearchParameters return an array of values, now the printSearchParameters returns all values. 
     * 
     * @param label
     * @param spList
     */
    protected void printSearchParameters(String label, List<SearchParameter> spList) {
        
        System.out.println("\nTest: " + label + "\nSearch Parameters:");
        for (SearchParameter sp : spList) {
            List<ResourceType> resources = sp.getBase();
            for(ResourceType resource : resources) {
                System.out.println("\t" + resource.getValue() + ":" + sp.getName().getValue());
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
     * @param valueTypes
     */
    protected void printValueTypes(Set<Class<?>> valueTypes) {
        // Must be not null to loop. 
        assertNotNull(valueTypes);
        for (@SuppressWarnings("rawtypes") Class vl : valueTypes) {
            System.out.println(vl.getSimpleName());
        }

    }
}
