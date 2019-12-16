/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test;

import static com.ibm.fhir.profile.ValueSetSupport.expand;
import static com.ibm.fhir.profile.ValueSetSupport.getContains;
import static com.ibm.fhir.profile.ValueSetSupport.getValueSet;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.ValueSet;

public class ValueSetExpansionTest {
    public static boolean DEBUG = true;
    
    @Test
    public void testValueSetExpansion1() throws Exception {
        ValueSet expanded = expand(getValueSet("http://ibm.com/fhir/ValueSet/vs1|1.0.0"));
        
        debug(expanded);
        
        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());
                
        Assert.assertEquals(actual, Arrays.asList("a", "b", "c"));   
    }
    
    @Test
    public void testValueSetExpansion2() throws Exception {
        ValueSet expanded = expand(getValueSet("http://ibm.com/fhir/ValueSet/vs2|1.0.0"));
        
        debug(expanded);
        
        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());
                
        Assert.assertEquals(actual, Arrays.asList("a", "b", "c", "d", "e"));  
    }
    
    @Test
    public void testValueSetExpansion3() throws Exception {
        ValueSet expanded = expand(getValueSet("http://ibm.com/fhir/ValueSet/vs3|1.0.0"));
        
        debug(expanded);
        
        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());
                
        Assert.assertEquals(actual, Arrays.asList("g", "x", "h", "i"));  
    }
    
    @Test
    public void testValueSetExpansion4() throws Exception {
        ValueSet expanded = expand(getValueSet("http://ibm.com/fhir/ValueSet/vs4|1.0.0"));
        
        debug(expanded);
        
        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());
                
        Assert.assertEquals(actual, Arrays.asList("j", "l", "a", "b", "d", "m", "p", "q", "s", "o", "t", "u"));  
    }
    
    @Test
    public void testValueSetExpansion5() throws Exception {
        ValueSet expanded = expand(getValueSet("http://ibm.com/fhir/ValueSet/vs5|1.0.0"));
        
        debug(expanded);
        
        List<String> actual = getContains(expanded.getExpansion()).stream()
            .map(contains -> contains.getCode().getValue())
            .collect(Collectors.toList());
                
        Assert.assertEquals(actual, Arrays.asList("m", "p", "q", "s", "o", "t", "u"));  
    }
    
    private void debug(ValueSet valueSet) throws Exception {
        if (DEBUG) {
            FHIRGenerator.generator(Format.JSON, true).generate(valueSet, System.out);
            System.out.println("");
        }
    }
}