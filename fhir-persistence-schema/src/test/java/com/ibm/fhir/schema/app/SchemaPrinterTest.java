/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.testng.annotations.Test;

/**
 * SchemaPrinter Test
 */
public class SchemaPrinterTest {
    @Test
    public void testSchemaPrinter() throws FileNotFoundException {
        SchemaPrinter printer = new SchemaPrinter(false);
        printer.addCommand("SELECT * FROM FRED");
        printer.addCommand("CREATE OR REPLACE PROCEDURE PROC");
        printer.addCommand("  CREATE TABLE FRED(V,V,V)");
        printer.addCommand(" ");
        printer.printGrants();
        printer.print();
        
        SchemaPrinter printerToFile = new SchemaPrinter(true);
        assertNotNull(printerToFile);
        
        File grants = new File("grants.sql");
        assertTrue(grants.delete());
        File schema = new File("schema.sql");
        assertTrue(schema.delete());
        File stored_procedures = new File("stored-procedures.sql");
        assertTrue(stored_procedures.delete());
    }
}