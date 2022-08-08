/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import org.testng.annotations.Test;

/**
 * Tests the DataDefinitionUtil 
 */
public class DataDefinitionUtilTest {

    @Test
    public void testValid() {
        DataDefinitionUtil.assertValidName("ADMIN");
        DataDefinitionUtil.assertValidName("SET_TENANT");
        DataDefinitionUtil.assertValidName("SET123");
        DataDefinitionUtil.assertValidName("SET_123");

        DataDefinitionUtil.assertValidName("Admin");
        DataDefinitionUtil.assertValidName("admin123");
        DataDefinitionUtil.assertValidName("admin123user");
        DataDefinitionUtil.assertValidName("admin123User");
        DataDefinitionUtil.assertValidName("adminUser");
        DataDefinitionUtil.assertValidName("admin_user");
        DataDefinitionUtil.assertValidName("admin_User");
        DataDefinitionUtil.assertValidName("admin_123");
        DataDefinitionUtil.assertValidName("admin_User123");
        DataDefinitionUtil.assertValidName("admin_User_123");
        DataDefinitionUtil.assertValidName("admin_User_abc");

    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidNumber() {
        DataDefinitionUtil.assertValidName("123");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidNumberString() {
        DataDefinitionUtil.assertValidName("123str");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidQuote() {
        DataDefinitionUtil.assertValidName("ADMIN'");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidQuote2() {
        DataDefinitionUtil.assertValidName("'");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidSemi() {
        DataDefinitionUtil.assertValidName("NAME;");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInvalidComment() {
        DataDefinitionUtil.assertValidName("NAME--");
    }

}
