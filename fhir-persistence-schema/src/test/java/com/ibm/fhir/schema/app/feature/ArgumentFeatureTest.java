/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.feature;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.testng.annotations.Test;

import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;

/**
 * Parses arguments into an ActionBean used in downstream actions.
 */
public class ArgumentFeatureTest {
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureTooHighIndex() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkValidityOfIndex(12, 11);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureEqualLimit() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkValidityOfIndex(11, 11);
    }

    @Test
    public void testArgumentFeatureGood() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkValidityOfIndex(9, 11);
        assert true;
    }

    @Test
    public void testArgumentFeatureParseArgsEmpty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[0];
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureIllegalArgumentException() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--invalid-valid" };
        argumentFeature.parseArgs(args);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureIllegalArgumentExceptionWithValidAsWell() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--drop-admin", "--invalid-valid" };
        argumentFeature.parseArgs(args);
    }

    @Test
    public void testArgumentFeatureUpdatePropertyFile() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        File f = new File("src/test/resources/test.properties");
        String[] args = new String[] { "--prop-file", f.getAbsolutePath() };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureInvalidPropertyFile() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop-file", "./THIS_FILE_DOES_NOT_EXIT" };
        argumentFeature.parseArgs(args);
    }

    @Test
    public void testArgumentFeatureSchemaName() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        // LowerCase
        String[] args = new String[] { "--schema-name", "schema" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "SCHEMA");

        //MixedCase
        args       = new String[] { "--schema-name", "sChEmA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "SCHEMA");

        //UpperCase
        args       = new String[] { "--schema-name", "SCHEMA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "SCHEMA");

        //With Underscore
        args       = new String[] { "--schema-name", "SC_MA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "SC_MA");

        //With Underscore at beginning
        args       = new String[] { "--schema-name", "_SC_MA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "_SC_MA");

        //With Underscore at beginning and with numbers
        args       = new String[] { "--schema-name", "_SC_M123A" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getSchemaName());
        assertEquals(actionBean.getSchemaName(), "_SC_M123A");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureSchemaNameBadName() {
        // Can start with a-z,A-Z,_
        // Not a number
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--schema-name", "1schema" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureGrantTo() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        // LowerCase
        String[] args = new String[] { "--grant-to", "user" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "USER");

        //MixedCase
        args       = new String[] { "--grant-to", "uSeR" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "USER");

        //UpperCase
        args       = new String[] { "--grant-to", "USER" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "USER");

        //With Underscore
        args       = new String[] { "--grant-to", "USER_MA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "USER_MA");

        //With Underscore at beginning
        args       = new String[] { "--grant-to", "_USER_MA" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "_USER_MA");

        //With Underscore at beginning and with numbers
        args       = new String[] { "--grant-to", "_USER_3A" };
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getGrantTo());
        assertEquals(actionBean.getGrantTo(), "_USER_3A");
    }

    @Test
    public void testArgumentFeatureDryRun() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        String[] args = new String[] { "--dry-run" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getDryRun());
        assertTrue(actionBean.getDryRun().booleanValue());

        args       = new String[0];
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.getDryRun().booleanValue());
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureMissingPropertyValueNull() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkForMissingPropertyValue(null);
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureMissingPropertyValueEmpty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkForMissingPropertyValue("");
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeatureMissingPropertyValueInvalidStart() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkForMissingPropertyValue("--");
    }

    @Test
    public void testArgumentFeaturePropertyValueValidStart() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkForMissingPropertyValue("-");
        assert true;
    }

    @Test
    public void testArgumentFeatureTruePropertyValue() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        argumentFeature.checkForMissingPropertyValue("abcdef");
    }

    @Test
    public void testArgumentFeatureConfirmDrop() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        String[] args = new String[] { "--confirm-drop" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertTrue(actionBean.isConfirmDrop());

        args       = new String[0];
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isConfirmDrop());
    }

    @Test
    public void testPrintUsage() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true);) {
            ArgumentFeature argumentFeature = new ArgumentFeature();
            argumentFeature.printUsage(ps);
            assertNotNull(baos.toString());
        } catch (Exception e) {
            fail("Unable to print the usage menu", e);
        }
    }

    @Test
    public void testArgumentFeatureUpdateSchema() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        String[] args = new String[] { "--update-schema" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertTrue(actionBean.isUpdateSchema());
        assertFalse(actionBean.isDropSchema());

        args       = new String[0];
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isUpdateSchema());
        assertFalse(actionBean.isDropSchema());
    }

    @Test
    public void testArgumentFeatureDropSchema() {
        ArgumentFeature argumentFeature = new ArgumentFeature();

        String[] args = new String[] { "--drop-schema" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isUpdateSchema());
        assertTrue(actionBean.isDropSchema());

        args       = new String[0];
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isUpdateSchema());
        assertFalse(actionBean.isDropSchema());
    }

    @Test
    public void testArgumentFeaturePoolSize() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--pool-size", "1" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getMaxConnectionPoolSize());
        assertEquals(actionBean.getMaxConnectionPoolSize(), 1);
    }

    @Test(expectedExceptions = { NumberFormatException.class })
    public void testArgumentFeaturePoolSizeNotNumber() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--pool-size", "XYZ" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeaturePoolSizeNoSecondParameter() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--pool-size" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testArgumentFeaturePoolSizeInvalidProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--pool-size", "--bad" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureCreateSchemas() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--create-schemas" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isCreateFhirSchemas());
        assertTrue(actionBean.isCreateFhirSchemas());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isCreateFhirSchemas());
        assertFalse(actionBean.isCreateFhirSchemas());
    }

    @Test
    public void testArgumentFeatureUpdateProc() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--update-proc" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isUpdateProc());
        assertTrue(actionBean.isUpdateProc());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isUpdateProc());
        assertFalse(actionBean.isUpdateProc());
    }

    @Test
    public void testArgumentFeatureCheckCompatibility() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--check-compatibility" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isCheckCompatibility());
        assertTrue(actionBean.isCheckCompatibility());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.isCheckCompatibility());
        assertFalse(actionBean.isCheckCompatibility());
    }

    @Test
    public void testArgumentFeatureTestTenant() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--test-tenant", "abcde" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getTenantName());
        assertEquals(actionBean.getTenantName(), "abcde");
        assertTrue(actionBean.isTestTenant());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isTestTenant());
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureTestTenantInvalid() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--test-tenant" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureTestTenantInvalidSecondValue() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--test-tenant", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureAllocateTenantEmptyProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--allocate-tenant" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureAllocateTenantBadProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--allocate-tenant", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureAllocateTenant() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--allocate-tenant", "abcde" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getTenantName());
        assertEquals(actionBean.getTenantName(), "abcde");
        assertTrue(actionBean.isAllocateTenant());
        assertFalse(actionBean.isDropTenant());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isAllocateTenant());
        assertFalse(actionBean.isDropTenant());
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureDropTenantEmptyProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--drop-tenant" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureDropTenantBadProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--drop-tenant", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureDropTenant() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--drop-tenant", "abcde" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getTenantName());
        assertEquals(actionBean.getTenantName(), "abcde");
        assertFalse(actionBean.isAllocateTenant());
        assertTrue(actionBean.isDropTenant());

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertFalse(actionBean.isAllocateTenant());
        assertFalse(actionBean.isDropTenant());
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureTenantKeyEmptyProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--tenant-key" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureTenantKeyBadProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--tenant-key", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureTenantKeyTenant() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--tenant-key", "abcde" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getTenantKey());
        assertEquals(actionBean.getTenantKey(), "abcde");

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNull(actionBean.getTenantKey());
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureAddTenantKeyEmptyProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--add-tenant-key" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeatureAddTenantKeyBadProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--add-tenant-key", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureAddTenantKeyTenant() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--add-tenant-key", "abcde" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getAddKeyForTenant());
        assertEquals(actionBean.getAddKeyForTenant(), "abcde");

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNull(actionBean.getAddKeyForTenant());
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeaturePropEmptyProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeaturePropBadProperty() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "--abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeaturePropBadPropertyNoEqual() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "abcde" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test(expectedExceptions = { java.lang.IllegalArgumentException.class })
    public void testArgumentFeaturePropBadPropertyWithEqual() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "abcde=" };
        argumentFeature.parseArgs(args);
        fail();
    }

    @Test
    public void testArgumentFeatureProp() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "abcde=1" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        
        assertNotNull(actionBean.getProperties());
        assertEquals(actionBean.getProperties().get("abcde"), "1");

        args       = new String[] {};
        actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getProperties());
        assertTrue(actionBean.getProperties().isEmpty());
    }

    @Test
    public void testArgumentFeatureMultipleProp() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "abcde=1", "--prop", "fdefgh=2" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getProperties());
        assertEquals(actionBean.getProperties().get("abcde"), "1");
        assertEquals(actionBean.getProperties().get("fdefgh"), "2");
    }
    
    @Test
    public void testArgumentFeatureMultiplePropertiesReplaced() {
        ArgumentFeature argumentFeature = new ArgumentFeature();
        String[] args = new String[] { "--prop", "abcde=1", "--prop", "fdefgh=2", "--prop", "abcde=3", "--prop", "fdefgh=4" };
        ActionBean actionBean = argumentFeature.parseArgs(args);
        assertNotNull(actionBean);
        assertNotNull(actionBean.getProperties());
        assertEquals(actionBean.getProperties().get("abcde"), "3");
        assertEquals(actionBean.getProperties().get("fdefgh"), "4");
    }
}