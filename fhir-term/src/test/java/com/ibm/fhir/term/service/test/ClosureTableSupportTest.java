/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.term.util.ClosureTableSupport;
import com.ibm.fhir.term.util.CodeSystemSupport;

public class ClosureTableSupportTest {
    @Test
    public void testClosureTableSupport() throws Exception {
        CodeSystem codeSystem = CodeSystemSupport.getCodeSystem("http://ibm.com/fhir/CodeSystem/cs5|1.0.0");
        Set<String> actual = ClosureTableSupport.getClosureTable(codeSystem, Code.of("m"));
        Set<String> expected = new LinkedHashSet<>(Arrays.asList("m", "p", "q", "r"));
        Assert.assertEquals(actual, expected);
    }
}
