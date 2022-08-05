/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Base64;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.type.Base64Binary;

public class Base64BinaryTest {

    @Test
    public void testBase64BinaryValid1() throws Exception {
        Base64Binary base64Binary = Base64Binary.builder().value("ABCDEA==").build();
        Assert.assertNotNull(base64Binary);
    }

    @Test
    public void testBase64BinaryValid2() throws Exception {
        Base64Binary base64Binary = Base64Binary.builder().value("ABCDEFE=").build();
        Assert.assertNotNull(base64Binary);
    }

    @Test
    public void testBase64BinaryValid3() throws Exception {
        Base64Binary base64Binary = Base64Binary.builder().value("ABCDEFGH").build();
        Assert.assertNotNull(base64Binary);
    }

    @Test
    public void testBase64BinaryInvalidStringLength() throws Exception {
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
    public void testBase64BinaryNonZeroPaddingBits() throws Exception {
        try {
            Base64Binary.builder().value("ABCDEF==").build();
            fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            Assert.assertEquals(e.getMessage(), "Invalid base64 string: non-zero padding bits; character: 'F' found at index: 5 should be: 'A'");
        }
    }

    @Test
    public void testBase64BinaryValidOfBytes() throws Exception {
        Base64Binary base64Binary = Base64Binary.of("on-fhir-server".getBytes());
        Assert.assertNotNull(base64Binary);
        byte[] bytes = base64Binary.getValue();
        String str = new String(bytes);
        assertEquals(str, "on-fhir-server");
    }

    @Test
    public void testBase64BinaryValidOfString() throws Exception {
        String encoded = Base64.getEncoder().encodeToString("on-fhir-server".getBytes());
        Base64Binary base64Binary = Base64Binary.of(encoded);
        Assert.assertNotNull(base64Binary);

        byte[] bytes = base64Binary.getValue();
        String str = new String(bytes);
        assertEquals(str, "on-fhir-server");
    }
}