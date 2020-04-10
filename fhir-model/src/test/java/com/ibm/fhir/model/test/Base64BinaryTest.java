/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.test;

import static org.testng.Assert.fail;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.type.Base64Binary;

public class Base64BinaryTest {
    @Test
    public void testBase64BinaryValid() throws Exception {
        Base64Binary base64Binary = Base64Binary.builder().value("ABCDEA==").build();
        Assert.assertNotNull(base64Binary);
    }

    @Test
    public void testBase64BinaryInvalid() throws Exception {
        try {
            Base64Binary.builder().value("ABCDEF").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Invalid base64 encoded string: incorrect padding");
        }
    }
}
