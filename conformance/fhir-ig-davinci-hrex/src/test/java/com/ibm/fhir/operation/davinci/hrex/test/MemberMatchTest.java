/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.test;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.operation.davinci.hrex.MemberMatchOperation;

/**
 *
 */
public class MemberMatchTest {
    @Test
    public void testOperationDefinition() {
        MemberMatchOperation operation = new MemberMatchOperation();
        OperationDefinition definition = operation.getDefinition();
        assertNotNull(definition);
    }
}
