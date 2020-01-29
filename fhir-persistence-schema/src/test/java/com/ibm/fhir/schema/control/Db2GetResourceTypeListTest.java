/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.dryrun.DryRunContainer;
import com.ibm.fhir.schema.app.processor.action.DatabaseSupport;
import com.ibm.fhir.schema.model.ResourceType;

/**
 * Tests db2 get resource type list test
 */
public class Db2GetResourceTypeListTest {
    @Test()
    public void testDb2GetResourceTypeList() {
        final String schemaName = "TESTSCHEMA";
        Db2GetResourceTypeList types = new Db2GetResourceTypeList(schemaName);
        IDatabaseTranslator translator = new Db2Translator();
        List<ResourceType> rtypes = types.run(translator, DatabaseSupport.generateConnection(true, null));
        assertNotNull(rtypes);
        assertFalse(rtypes.isEmpty());
    }

    @Test(dependsOnMethods = { "testDb2GetResourceTypeList" })
    public void testDb2GetResourceTypeListDryRun() {
        DryRunContainer container = DryRunContainer.getSingleInstance();
        container.setDryRun(Boolean.TRUE);
        final String schemaName = "TESTSCHEMA";
        Db2GetResourceTypeList types = new Db2GetResourceTypeList(schemaName);
        IDatabaseTranslator translator = new Db2Translator();
        List<ResourceType> rtypes = types.run(translator, DatabaseSupport.generateConnection(true, null));
        assertNotNull(rtypes);
        assertFalse(rtypes.isEmpty());
        container.setDryRun(Boolean.FALSE);
    }

    @Test(dependsOnMethods = { "testDb2GetResourceTypeListDryRun" }, expectedExceptions = { DataAccessException.class })
    public void testDb2GetResourceTypeListFail() {
        final String schemaName = "TESTSCHEMA";
        Db2GetResourceTypeList types = new Db2GetResourceTypeList(schemaName);
        IDatabaseTranslator translator = new Db2Translator();
        List<ResourceType> rtypes = types.run(translator, DatabaseSupport.generateConnection(false, null));
        assertNotNull(rtypes);
        assertFalse(rtypes.isEmpty());
    }
}