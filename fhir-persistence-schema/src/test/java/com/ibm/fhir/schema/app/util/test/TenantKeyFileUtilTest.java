/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.util.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.schema.app.util.TenantKeyFileUtil;

public class TenantKeyFileUtilTest {
    @BeforeClass
    public void testRemoveTestData() {
        String fn = "src/test/resources/test/test3.tenant.key.nowrite";
        File file = new File(fn);
        try {
            if (file.exists() && file.delete()) {
                System.out.println("Deleted Test file [" + fn + "]");
            }
        } catch (Exception e) {
            // No Warning intentionally. 
        }
    }

    @Test
    public void readTenantFileTestNull() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.readTenantFile(null);
            fail("Null");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("Value for tenant-key-file-name is never set"));
        }
    }

    @Test
    public void readTenantFileTestDoesntExist() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.readTenantFile("src/test/resources/test/test0.tenant.key");
            fail("DoesntExist");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("does not exist"));
        }
    }

    @Test
    public void readTenantFileTestEmptyFile() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.readTenantFile("src/test/resources/test/test1.tenant.key");
            fail("Empty");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("tenant-key-file-name is empty"));
        }
    }

    @Test
    public void readTenantFileTestMoreThanOneLine() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.readTenantFile("src/test/resources/test/test2.tenant.key");
            fail("More Than One Line");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("tenant-key-file-name supports only one line in the file"));
        }
    }

    @Test
    public void readTenantFileTestOneLineTooLong() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.readTenantFile("src/test/resources/test/test4.tenant.key");
            fail("Too long");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("included tenant key is greater than 44 characters"));
        }
    }

    @Test
    public void readTenantFileTestCorrect() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        assertEquals("1-2-3-4-5-6", util.readTenantFile("src/test/resources/test/test3.tenant.key"));
    }

    @Test
    public void writeTenantFileNullFile() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.writeTenantFile(null, null);
            fail("Null - Write");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("Value for tenant-key-file-name is never set"));
        }
    }

    @Test
    public void writeTenantFileNullKey() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.writeTenantFile("src/test/resources/test/test2.tenant.key.nowrite", null);
            fail("Null Key - Write");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("Value for tenant-key is never set when writing tenant key"));
        }
    }

    @Test
    public void writeTenantFileNoOverwrite() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.writeTenantFile("src/test/resources/test/test2.tenant.key", "1");
            fail("Never Over Write");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("tenant-key-file-name must not exist"));
        }
    }

    @Test
    public void writeTenantFileDirDoesntExist() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            util.writeTenantFile("src/test/resources/test/test/test2.tenant.key", "1");
            fail("Directory Doesnt Exist");
        } catch (java.lang.IllegalArgumentException iae) {
            assertTrue(iae.getMessage().contains("not a writeable directory"));
        }
    }

    @Test
    public void writeTenantFileDir() {
        TenantKeyFileUtil util = new TenantKeyFileUtil();
        try {
            String fn = "src/test/resources/test/test3.tenant.key.nowrite";
            util.writeTenantFile(fn, "1");
            assertEquals(util.readTenantFile(fn), "1");
        } catch (java.lang.IllegalArgumentException iae) {
            fail("Directory Doesnt Exist", iae);
        }
    }
}
