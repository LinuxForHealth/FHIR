/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.erase;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * Verify the Configuration and that there are no uncovered non-abstract types.
 *
 * @implNote We must cover each resource type. This is a guard against the possible
 * gaps from adding support for new revs of the Specification.
 */
public class VerifyResourceTypesDidntChangeTest {
    @Test
    public void testResources() throws FHIRParserException, FileNotFoundException {
        List<String> resources = ModelSupport.getResourceTypes(false)
                .stream()
                .map(r -> r.getSimpleName())
                .sorted()
                .collect(Collectors.toList());
        OperationDefinition def = FHIRParser.parser(Format.JSON)
                .parse(new FileInputStream("src/main/resources/erase.json"));
        List<ResourceType> types = def.getResource();
        assertNotNull(types);
        assertFalse(types.isEmpty());
        for (ResourceType type : types) {
            assertTrue(resources.contains(type.getValue()));
        }
    }
}