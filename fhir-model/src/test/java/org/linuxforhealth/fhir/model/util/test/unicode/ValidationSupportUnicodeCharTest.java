/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.util.test.unicode;

import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.util.ValidationSupport;

/**
 * Verifies the Unicode support and functionality of the ValidationSupport class.
 * The tested class is called in a number of places in the fhir-model module.
 */
public class ValidationSupportUnicodeCharTest {

    @Test
    public void testCheckString() {
        for (UnicodeChar c : UnicodeChar.forbidden()) {
            String baseTest = "ABCDEFGH";
            try {
                ValidationSupport.checkString(baseTest + c.getCharactersString());
            } catch (IllegalStateException ise) {
                continue;
            }
            fail("Unexpected pass with char: " + c.getEscapedValue());
        }
    }

    @Test
    public void testCheckUri() {
        for (UnicodeChar c : UnicodeChar.forbidden()) {
            String baseTest = "https://ABCDEFGH";
            try {
                ValidationSupport.checkUri(baseTest + c.getCharactersString());
            } catch (IllegalStateException ise) {
                continue;
            }
            fail("Unexpected pass with char: " + c.getEscapedValue());
        }
    }

    @Test
    public void testCheckId() {
        for (UnicodeChar c : UnicodeChar.forbidden()) {
            String baseTest = "A-Z-A-Z";
            try {
                ValidationSupport.checkId(baseTest + c.getCharactersString());
            } catch (IllegalStateException ise) {
                continue;
            }
            fail("Unexpected pass with char: " + c.getEscapedValue());
        }
    }

    @Test
    public void testCheckCode() {
        for (UnicodeChar c : UnicodeChar.forbidden()) {
            String baseTest = "A-Z-A-Z";
            try {
                ValidationSupport.checkCode(baseTest + c.getCharactersString());
            } catch (IllegalStateException ise) {
                continue;
            }
            fail("Unexpected pass with char: " + c.getEscapedValue());
        }
    }

    @Test
    public void testCheckXHTMLContent() {
        for (UnicodeChar c : UnicodeChar.forbidden()) {
            String baseTest = "<div xmlns=\"http://www.w3.org/1999/xhtml\">High Test XYZ</div>";
            try {
                ValidationSupport.checkXHTMLContent(baseTest.replace("XYZ", c.getCharactersString()));
            } catch (IllegalStateException ise) {
                continue;
            }
            fail("Unexpected pass with char: " + c.getEscapedValue());
        }
    }
}