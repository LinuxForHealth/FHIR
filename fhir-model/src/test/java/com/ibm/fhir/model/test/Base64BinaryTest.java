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
    public void testBase64BinaryInvalidLength() throws Exception {
        try {
            Base64Binary.builder().value("ABCDEF").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Invalid base64 string length: 6");
        }
    }

    @Test
    public void testBase64BinaryUnexpectedPaddingCharacter() throws Exception {
        try {
            Base64Binary.builder().value("ABCDE===").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Unexpected base64 padding character: '=' found at index: 5");
        }
    }

    @Test
    public void testBase64BinaryIllegalCharacter() throws Exception {
        try {
            Base64Binary.builder().value("ABCDE&==").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Illegal base64 character: '&' found at index: 5");
        }
    }

    @Test
    public void testBase64BinaryNonZeroPadding() throws Exception {
        try {
            Base64Binary.builder().value("ABCDEF==").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Invalid base64 string: non-zero padding bits; character: 'F' found at index: 5 should be: 'A'");
        }
    }
}
