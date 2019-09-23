/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.plugin.test;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.testng.annotations.Test;

import com.ibm.fhir.model.plugin.ModelPlugin;

/**
 * 
 * @author pbastide
 *
 */
public class ModelPluginTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testModelPlugin() throws Exception {
        File pom = getTestFile("src/test/resources/modelplugin/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());
        ModelPlugin modelPlugin = (ModelPlugin) lookupMojo("generate-model", pom);
        assertNotNull(modelPlugin);

        // Ideally, the test executes -> <code>modelPlugin.execute();</code>
    }
}
