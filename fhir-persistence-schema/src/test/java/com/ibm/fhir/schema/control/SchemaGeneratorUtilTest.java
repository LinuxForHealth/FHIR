/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.testng.annotations.Test;

/**
 * Tests Replacer and SchemaGeneratorUtil
 */
public class SchemaGeneratorUtilTest {
    @Test
    public void testProcessReplacer() {
        Replacer r = new Replacer("SCHEMA_NAME", "FHIR_ADMIN");
        String input = "A\n" + "B\n" + "C\n" + "{{SCHEMA_NAME}}\n" + "E";
        String output = r.process(input);
        String expected = "A\n" + "B\n" + "C\n" + "FHIR_ADMIN\n" + "E";
        assertEquals(output, expected);
    }

    @Test
    public void testProcessReplacerNotFound() {
        Replacer r = new Replacer("VALUEX", "ADMIN_SCHEMA_NAME");
        String input = "A\n" + "B\n" + "C\n" + "VALUE\n" + "E";
        String output = r.process(input);
        String expected = "A\n" + "B\n" + "C\n" + "VALUE\n" + "E";
        assertEquals(output, expected);
    }

    @Test
    public void testProcessReplacerWithSchemaGeneratorUtil() {
        Replacer replacer = new Replacer("SCHEMA_NAME", "FHIR_ADMIN");
        String line = "A\n" + "B\n" + "C\n" + "{{SCHEMA_NAME}}\n" + "E";
        String output = SchemaGeneratorUtil.mapper(line, Arrays.asList(replacer));
        String expected = "A\n" + "B\n" + "C\n" + "FHIR_ADMIN\n" + "E";
        assertEquals(output, expected);
    }

    @Test
    public void testProcessReplacerReadTemplateWithSchemaGeneratorUtil() throws IOException {
        Replacer replacer = new Replacer("XYZ", "ABC");
        Collection<Replacer> replacersArg = Arrays.asList(replacer);

        String line = "XYZ\n" + "{{ADMIN_SCHEMA_NAME}}\n" + "C\n" + "{{SCHEMA_NAME}}\n" + "E";
        try (ByteArrayInputStream bais = new ByteArrayInputStream(line.getBytes());) {
            String adminSchemaName = "ADMINSCHEMANAME";
            String schemaName = "SCHEMANAME";

            String output = SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, replacersArg, bais);
            String expected = "XYZ\n" + "ADMINSCHEMANAME\n" + "C\n" + "SCHEMANAME\n" + "E";
            assertEquals(output, expected);

            line = "{{XYZ}}\n" + "{{ADMIN_SCHEMA_NAME}}\n" + "C\n" + "{{SCHEMA_NAME}}\n" + "E";
            ByteArrayInputStream bais2 = new ByteArrayInputStream(line.getBytes());
            output   = SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, replacersArg, bais2);
            expected = "ABC\n" + "ADMINSCHEMANAME\n" + "C\n" + "SCHEMANAME\n" + "E";
            assertEquals(output, expected);

            line = "XYZ\n" + "{{ADMIN_SCHEMA_NAME}}\n" + "C\n" + "{{SCHEMA_NAME}}\n" + "E";
            ByteArrayInputStream bais3 = new ByteArrayInputStream(line.getBytes());
            output   = SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, null, bais3);
            expected = "XYZ\n" + "ADMINSCHEMANAME\n" + "C\n" + "SCHEMANAME\n" + "E";
            assertEquals(output, expected);
        }
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testProcessReplacerReadTemplateWithSchemaGeneratorUtilInvalid() throws IOException {
        Replacer replacer = new Replacer("XYZ", "ABC");
        Collection<Replacer> replacersArg = Arrays.asList(replacer);

        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("DUMMY");
            }

        };

        String adminSchemaName = "ADMINSCHEMANAME";
        String schemaName = "SCHEMANAME";
        SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, replacersArg, is);
        fail();
    }

    @Test
    public void testProcessReplacerReadTemplateWithSchemaGeneratorUtilClasspath() throws IOException {
        Replacer replacer = new Replacer("XYZ", "ABC");
        Collection<Replacer> replacersArg = Arrays.asList(replacer);

        String templateName = "add_resource_type.sql";
        String adminSchemaName = "ADMINSCHEMANAME";
        String schemaName = "SCHEMANAME";

        String output = SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, templateName, replacersArg);
        assertNotNull(output);
        assertFalse(output.isEmpty());
    }
    
    @Test(expectedExceptions = { IllegalArgumentException.class })
    public void testProcessReplacerReadTemplateWithSchemaGeneratorUtilNotExists() throws IOException {
        Replacer replacer = new Replacer("XYZ", "ABC");
        Collection<Replacer> replacersArg = Arrays.asList(replacer);

        String templateName = "add_resource_type.sqlDONOTEXISTS";
        String adminSchemaName = "ADMINSCHEMANAME";
        String schemaName = "SCHEMANAME";

        SchemaGeneratorUtil.readTemplate(adminSchemaName, schemaName, templateName, replacersArg);
    }
}