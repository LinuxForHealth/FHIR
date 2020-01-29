/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.schema.app.processor.action.bean.ActionBean;
import com.ibm.fhir.schema.app.processor.action.exceptions.SchemaActionException;

/**
 * ActionBean Test
 */
public class ActionBeanTest {
    @Test
    public void testActionBean() throws SchemaActionException {
        ActionBean actionBean = new ActionBean();

        IDatabaseTranslator translator = new Db2Translator();
        actionBean.setTranslator(translator);
        assertNotNull(actionBean.getTranslator());

        actionBean.setCompatible(true);
        assertTrue(actionBean.isCompatible());

        Properties props = new Properties();
        props.put("X", "Y");
        actionBean.setProperties(props);
        assertNotNull(actionBean.getProperties());

        assertEquals(actionBean.getExitStatus(), 0);
    }
    
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testLoadPropertyFileDoesntExist() {
        ActionBean actionBean = new ActionBean();
        actionBean.loadPropertyFile("IDONOTEXIST");
        fail();
    }
}