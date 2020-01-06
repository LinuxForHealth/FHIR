/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.test;

import java.io.File;

public class TestHelper {

    /*
     * returns the absolute path to the properties file to run these tests. 
     */
    public static final String absolutePathToProperties() {
        File f = new File("db2.properties");
        return f.getAbsolutePath();
    }
}